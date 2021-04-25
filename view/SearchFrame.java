package hust.cs.javacourse.search.view;

import javax.swing.*;
import java.awt.*;
import java.nio.file.Paths;

/**
 * @author suyu
 * @create 2021-04-24-9:23
 */
public class SearchFrame extends JFrame {

    public SearchFrame(){
        super("File Search");
        try {
            UIManager.setLookAndFeel(UIManager.getSystemLookAndFeelClassName());
        } catch (ClassNotFoundException | IllegalAccessException | InstantiationException | UnsupportedLookAndFeelException e) {
            e.printStackTrace();
        }
        setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        Dimension screenSize = Toolkit.getDefaultToolkit().getScreenSize();
        setSize(screenSize.width / 2, screenSize.height / 2);
        setResizable(false);
        setLocationByPlatform(true);
        addComponent();
        setVisible(true);
    }

    private void addComponent(){
        add(new SearchTextPanel(),BorderLayout.SOUTH);
        FileTreePanel treePanel = new FileTreePanel();
        treePanel.setUp(Paths.get("C:\\Users\\Dominery\\Documents\\笔记\\java"));
        add(treePanel,BorderLayout.EAST);
    }

    public static void main(String[] args) {
        new SearchFrame();
    }
}
