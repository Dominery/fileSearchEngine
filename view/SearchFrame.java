package hust.cs.javacourse.search.view;

import hust.cs.javacourse.search.index.Index;

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
        searchTextPanel.setOpaque(false);
        add(searchTextPanel,BorderLayout.SOUTH);
        FileTreePanel treePanel = new FileTreePanel();
        treePanel.setOpaque(false);
        treePanel.setUp(Paths.get("C:\\Users\\Dominery\\Documents\\笔记\\java"));
        add(treePanel,BorderLayout.EAST);
        setBackgroundImage("images/little_prince.png");
    }
    private void setBackgroundImage(String path){
        JLabel label = new JLabel(new ScaledImageIcon(path).cover(getWidth(),getHeight()));
        getLayeredPane().add(label,new Integer(Integer.MIN_VALUE));
        label.setBounds(0,0,getWidth(),getHeight());
        ((JPanel)getContentPane()).setOpaque(false);
    }

    public static void main(String[] args) {
        new SearchFrame();
    }
}
