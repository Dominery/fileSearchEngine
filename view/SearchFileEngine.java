package hust.cs.javacourse.search.view;

import hust.cs.javacourse.search.index.DocumentBuilder;
import hust.cs.javacourse.search.index.IndexBuilder;
import hust.cs.javacourse.search.parse.FilterFactory;
import hust.cs.javacourse.search.query.IndexSearcher;
import sun.awt.datatransfer.DataTransferer;

import javax.swing.*;
import javax.swing.filechooser.FileNameExtensionFilter;
import java.awt.*;
import java.awt.event.ActionListener;
import java.io.File;
import java.io.IOException;
import java.util.concurrent.BrokenBarrierException;

/**
 * @author suyu
 * @create 2021-04-28-12:18
 */
public class SearchFileEngine {
    private JFileChooser loadChooser = JFileChooserBuilder.setDirectory(new File("."))
            .addFileFilter(new FileNameExtensionFilter("index file","bat"))
            .build();
    private JFileChooser buildChooser = JFileChooserBuilder.setDirectory(new File("../"))
            .setFileSelectionMode(JFileChooser.DIRECTORIES_ONLY)
            .build();
    private FileTreePanel treePanel;
    private ActionListener buildListener;
    private JFrame mainFrame;
    private JFrame startFrame;
    private DocumentBuilder documentBuilder=new DocumentBuilder();
    private IndexBuilder indexBuilder=new IndexBuilder(documentBuilder);
    private IndexSearcher indexSearcher=new IndexSearcher();

    public SearchFileEngine(){
        setUpMainFrame();
//        setUpStartFrame();
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
        JScrollPane scrollPane = new JScrollPane(contentPanel);
        scrollPane.setOpaque(false);
        scrollPane.getViewport().setOpaque(false);
        SearchTextPanel searchTextPanel = new SearchTextPanel();
        searchTextPanel.register(s->treePanel.pickNode(indexSearcher.search(s)));
        buildListener = e -> {
                        int i = buildChooser.showOpenDialog(mainFrame);
                        if (i == JFileChooser.APPROVE_OPTION) {
                            File selectedFile = buildChooser.getSelectedFile();
                            treePanel.setUp(selectedFile);
                            indexSearcher.setIndex(indexBuilder.buildIndex(selectedFile));
                        }
                    };
        treePanel.setUp(new File("C:\\Users\\Dominery\\Documents\\笔记\\java"));
        JMenuBar menu = MenuBarBuilder.mainMenu("File")
                .addMenuItem(new ImageIconSizer("images/build.png")
                        .scale(16,16),"build", buildListener).build();
        FilterPanel filterPanel = new FilterPanel(filters -> documentBuilder.setFilter(FilterFactory.create(filters)));

        mainFrame = FrameBuilder.setSize(0.5,0.5)
                .setBackgroundImage("images/little_prince.png")
                .add(searchTextPanel, BorderLayout.SOUTH)
                .add(treePanel,BorderLayout.WEST)
                .add(filterPanel, BorderLayout.EAST)
                .add(scrollPane,BorderLayout.CENTER)
                .setMenuBar(menu)
                .build();
    }

    private void setUpStartFrame(){
        JButton load = new JButton("Load");
        JButton build = new JButton("Build");
        ActionListener listener =e -> {
            int i = buildChooser.showOpenDialog(mainFrame);
            if (i == JFileChooser.APPROVE_OPTION) {
                File selectedFile = buildChooser.getSelectedFile();
                treePanel.setUp(selectedFile);
                indexSearcher.setIndex(indexBuilder.buildIndex(selectedFile));
                startFrame.setVisible(false);
                mainFrame.setVisible(true);
            }
        };
        JMenuBar menu = MenuBarBuilder.mainMenu("File")
                .addMenuItem(new ImageIconSizer("images/build.png")
                        .scale(16,16),"build", listener).build();
        startFrame = FrameBuilder.setSize(0.3,0.3)
                .setBackgroundImage("images/feeling.jpeg")
                .setMenuBar(menu)
                .build();
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
