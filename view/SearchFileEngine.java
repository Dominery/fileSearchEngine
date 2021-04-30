package hust.cs.javacourse.search.view;

import hust.cs.javacourse.search.index.DocumentBuilder;
import hust.cs.javacourse.search.index.IndexBuilder;
import hust.cs.javacourse.search.parse.FilterFactory;
import hust.cs.javacourse.search.query.Hit;
import hust.cs.javacourse.search.query.IndexSearcher;

import javax.swing.*;
import javax.swing.filechooser.FileNameExtensionFilter;
import java.awt.*;
import java.awt.event.ActionListener;
import java.io.File;
import java.io.IOException;
import java.time.Instant;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;
import java.util.function.Consumer;

/**
 * @author suyu
 * @create 2021-04-28-12:18
 */
public class SearchFileEngine {
    private FileChooserActionBuilder loadEngine;
    private final FileChooserActionBuilder buildEngine;
    private FileTreePanel treePanel;
    private JFrame mainFrame;
    private JFrame startFrame;
    private DocumentBuilder documentBuilder=new DocumentBuilder();
    private IndexBuilder indexBuilder=new IndexBuilder(documentBuilder);
    private IndexSearcher indexSearcher=new IndexSearcher();

    public SearchFileEngine(){
        loadEngine = new FileChooserActionBuilder(JFileChooserBuilder.setDirectory(new File("."))
                .addFileFilter(new FileNameExtensionFilter("index file", "bat"))
                .build(), mainFrame);
        buildEngine = new FileChooserActionBuilder(JFileChooserBuilder.setDirectory(new File("../"))
                .setFileSelectionMode(JFileChooser.DIRECTORIES_ONLY)
                .build(), mainFrame);
        setUpMainFrame();
        setUpStartFrame();
//        startFrame.setVisible(true);
        mainFrame.setVisible(true);
    }

    private void setUpMainFrame(){
        ContentPanel contentPanel = new ContentPanel();
        treePanel = new FileTreePanel(file -> {
            try {
                contentPanel.clearContent();
                contentPanel.setContent(file);
            }catch (IOException e){
                e.printStackTrace();
            }
        });
        SearchTextPanel searchTextPanel = new SearchTextPanel();
        searchTextPanel.register(s-> {
            Hit[] search = indexSearcher.search(s);
            treePanel.pickNode(search);
        });
        buildEngine.addActions(treePanel::setUp)
                .addActions(file -> indexSearcher.setIndex(indexBuilder.buildIndex(file)));
        loadEngine.addActions(file->indexSearcher.getIndex().load(file))
                .addActions(file -> treePanel.setUp(indexSearcher.getIndex().getRootPath()));
        FileChooserActionBuilder saveEngine = new FileChooserActionBuilder(JFileChooserBuilder.setDirectory(new File("../"))
                                                .setFileSelectionMode(JFileChooser.DIRECTORIES_ONLY).build(),mainFrame);
        saveEngine.addActions(file -> {
            String s = Instant.now().toEpochMilli()+".bat";
            System.out.println(s);
            indexSearcher.getIndex().save(new File(file.getAbsolutePath()+File.separator+s));
        });
        JMenuBar menu = MenuBuilder.menuTye(new JMenuBar())
                .addMenu("File")
                .addMenuItem(new ImageIconSizer("images/build.png")
                        .scale(16,16),"build", buildEngine.getListener())
                .addMenuItem(new ImageIconSizer("images/export.png")
                        .scale(16,16),"load",loadEngine.getListener())
                .addMenuItem(new ImageIconSizer("images/save.png")
                        .scale(16,16),"save",saveEngine.getListener())
                .addMenu("System")
                .addMenuItem(new ImageIconSizer("images/back.png")
                        .scale(16,16),"back",e->{mainFrame.setVisible(false);startFrame.setVisible(true);})
                .addMenuItem(new ImageIconSizer("images/exit.png")
                        .scale(16,16),"exit",e->System.exit(0)).build();

        JSplitPane right = new JSplitPane(JSplitPane.VERTICAL_SPLIT, contentPanel, searchTextPanel);
        right.setDividerLocation(Integer.MAX_VALUE);
        right.setOneTouchExpandable(true);
        JSplitPane whole  =  new JSplitPane(JSplitPane.HORIZONTAL_SPLIT,treePanel,right);
        Arrays.asList(contentPanel, searchTextPanel, treePanel,right).forEach(c->c.setOpaque(false));
        mainFrame = FrameBuilder.setSize(0.5,0.5)
                .setBackgroundImage("images/little_prince.png")
                .setIcon("images/file_search.png")
//                .setLayout(new GridBagLayout())
                .add(whole,BorderLayout.CENTER)
//                .add(treePanel,new GBC(0,0,3,7).setFill(GBC.BOTH).setWeight(0,100))
//                .add(contentPanel,new GBC(3,0,4,5).setFill(GBC.BOTH).setWeight(100,100))
//                .add(searchTextPanel, new GBC(3,5,4,2).setAnchor(GBC.CENTER).setWeight(100,0))
                .setMenuBar(menu)
                .build();
//        treePanel.setMinimumSize(new Dimension(mainFrame.getWidth()*3/7,Integer.MAX_VALUE));
//        treePanel.setMaximumSize(new Dimension(mainFrame.getWidth()*3/7,Integer.MAX_VALUE));
    }

    private void setUpStartFrame(){
        JButton load = new JButton("Load");
        load.setIcon(new ImageIconSizer("images/export.png").scale(30,30));
        load.setContentAreaFilled(false);
        JButton build = new JButton("Build");
        build.setIcon(new ImageIconSizer("images/build.png").scale(30,30));
        build.setContentAreaFilled(false);
        FilterPanel filterPanel = new FilterPanel(filters -> documentBuilder.setFilter(FilterFactory.create(filters)));
        FileChooserActionBuilder newBuildEngine = buildEngine.copy(startFrame)
                .addActions(file -> startFrame.setVisible(false))
                .addActions(file -> mainFrame.setVisible(true));
        FileChooserActionBuilder newLoadEngine = loadEngine.copy(startFrame)
                .addActions(file -> startFrame.setVisible(false))
                .addActions(file -> mainFrame.setVisible(true));
        load.addActionListener(newLoadEngine.getListener());
        build.addActionListener(newBuildEngine.getListener());
        startFrame = FrameBuilder.setSize(0.3,0.3)
                .setIcon("images/file_search.png")
                .setBackgroundImage("images/feeling.jpeg")
                .setLayout(new GridBagLayout())
                .add(build,new GBC(0,0).setAnchor(GBC.EAST).setWeight(0,0))
                .add(load,new GBC(0,1).setAnchor(GBC.CENTER).setWeight(0,0))
                .add(filterPanel,new GBC(1,0,4,2).setAnchor(GBC.WEST).setWeight(0,0))
                .build();
    }

    private static class FileChooserActionBuilder {
        private List<Consumer<File>> actions=new ArrayList<>();
        private final JFileChooser fileChooser;
        private final JFrame frame;
        public FileChooserActionBuilder(JFileChooser fileChooser, JFrame frame){
            this.fileChooser = fileChooser;
            this.frame = frame;
        }
        public FileChooserActionBuilder addActions(Consumer<File>action){
            actions.add(action);
            return this;
        }
        public ActionListener getListener(){
            return e->{
                int i = fileChooser.showOpenDialog(frame);
                if (i == JFileChooser.APPROVE_OPTION) {
                    File selectedFile = fileChooser.getSelectedFile();
                    actions.forEach(action -> action.accept(selectedFile));
                }
            };
        }
        public FileChooserActionBuilder copy(JFrame frame){
            FileChooserActionBuilder copy = new FileChooserActionBuilder(fileChooser, frame);
            copy.actions = new ArrayList<>(actions);
            return copy;
        }
    }

    public static void main(String[] args) throws IOException {
        try {
            UIManager.setLookAndFeel(UIManager.getSystemLookAndFeelClassName());
        } catch (ClassNotFoundException | IllegalAccessException | InstantiationException | UnsupportedLookAndFeelException e) {
            e.printStackTrace();
        }
        new SearchFileEngine();
    }
}
