package hust.cs.javacourse.search.view;

import hust.cs.javacourse.search.index.DocumentBuilder;
import hust.cs.javacourse.search.parse.FilterFactory;

import javax.swing.*;
import javax.swing.filechooser.FileNameExtensionFilter;
import java.awt.*;
import java.io.File;
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
    private JFrame mainFrame;
    public SearchFileEngine(){
        SearchTextPanel searchTextPanel = new SearchTextPanel();
        FileTreePanel treePanel = new FileTreePanel();
        treePanel.setUp(new File("C:\\Users\\Dominery\\Documents\\笔记\\java"));
        JMenuBar menu = MenuBarBuilder.mainMenu("File")
                .addMenuItem(new ImageIconSizer("images/build.png")
                        .scale(16,16),"build", e -> {
            int i = buildChooser.showOpenDialog(mainFrame);
            if (i == JFileChooser.APPROVE_OPTION) {
                treePanel.setUp(buildChooser.getSelectedFile());
            }
        }).build();
        DocumentBuilder documentBuilder = new DocumentBuilder();
        FilterPanel filterPanel = new FilterPanel(filters -> documentBuilder.setFilter(FilterFactory.create(filters)));
        mainFrame = FrameBuilder.setSize(0.5,0.5)
                .setBackgroundImage("images/little_prince.png")
                .add(searchTextPanel, BorderLayout.SOUTH)
                .add(treePanel,BorderLayout.WEST)
                .add(filterPanel, BorderLayout.EAST)
                .setMenuBar(menu)
                .build();
        mainFrame.setVisible(true);
    }

    public static void main(String[] args) {
        try {
            UIManager.setLookAndFeel(UIManager.getSystemLookAndFeelClassName());
        } catch (ClassNotFoundException | IllegalAccessException | InstantiationException | UnsupportedLookAndFeelException e) {
            e.printStackTrace();
        }
        new SearchFileEngine();
    }
}
