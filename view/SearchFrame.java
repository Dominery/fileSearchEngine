package hust.cs.javacourse.search.view;

import hust.cs.javacourse.search.index.DocumentBuilder;
import hust.cs.javacourse.search.parse.FilterFactory;

import javax.swing.*;
import javax.swing.filechooser.FileNameExtensionFilter;
import java.awt.*;
import java.io.File;
import java.nio.file.Paths;

/**
 * @author suyu
 * @create 2021-04-24-9:23
 */
public class SearchFrame extends JFrame {
    private JFileChooser loadChooser = JFileChooserBuilder.setDirectory(new File("."))
                                    .addFileFilter(new FileNameExtensionFilter("index file","bat"))
                                    .build();
    private JFileChooser buildChooser = JFileChooserBuilder.setDirectory(new File("../"))
                                    .setFileSelectionMode(JFileChooser.DIRECTORIES_ONLY)
                                    .build();
    public SearchFrame(){
        super("File Search");
//        try {
//            UIManager.setLookAndFeel(UIManager.getSystemLookAndFeelClassName());
//        } catch (ClassNotFoundException | IllegalAccessException | InstantiationException | UnsupportedLookAndFeelException e) {
//            e.printStackTrace();
//        }
        setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        Dimension screenSize = Toolkit.getDefaultToolkit().getScreenSize();
        setSize(screenSize.width / 2, screenSize.height / 2);
        setResizable(false);
        setLocationByPlatform(true);
        addComponent();
        setVisible(true);
    }

    private void addComponent(){
        SearchTextPanel searchTextPanel = new SearchTextPanel();
        searchTextPanel.setOpaque(false);//make the panel transparent
        add(searchTextPanel,BorderLayout.SOUTH);
        FileTreePanel treePanel = new FileTreePanel();
        treePanel.setOpaque(false);
        treePanel.setUp(new File("C:\\Users\\Dominery\\Documents\\笔记\\java"));
        add(treePanel,BorderLayout.WEST);
        setBackgroundImage("images/little_prince.png");
        JMenuBar build = MenuBarBuilder.mainMenu("File").addMenuItem("build", e -> {
            int i = buildChooser.showOpenDialog(this);
            if (i == JFileChooser.APPROVE_OPTION) {
                treePanel.setUp(buildChooser.getSelectedFile());
            }
        }).build();
        build.setOpaque(false);
        setJMenuBar(build);
        DocumentBuilder documentBuilder = new DocumentBuilder();
        treePanel.setUp(new File("C:\\Users\\Dominery\\Documents\\笔记"));
        FilterPanel filterPanel = new FilterPanel(filters->documentBuilder.setFilter(FilterFactory.create(filters)));
        filterPanel.setOpaque(false);
        add(filterPanel,BorderLayout.EAST);
    }
    private void setBackgroundImage(String path){
        JLabel label = new JLabel(new ImageIconSizer(path).cover(getWidth(),getHeight()));
        getLayeredPane().add(label,new Integer(Integer.MIN_VALUE));
        label.setBounds(0,0,getWidth(),getHeight());
        ((JPanel)getContentPane()).setOpaque(false);
    }

    public static void main(String[] args) {
        new SearchFrame();
    }
}
