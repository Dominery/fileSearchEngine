package hust.cs.javacourse.search.view;

import javax.swing.*;
import java.awt.*;

/**
 * @author suyu
 * @create 2021-04-28-11:52
 */
public class FrameBuilder {
    private final JFrame frame;
    private FrameBuilder(double widthRate,double heightRate){
        frame = new JFrame();
        frame.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        Dimension screenSize = Toolkit.getDefaultToolkit().getScreenSize();
        frame.setSize((int)(screenSize.width*widthRate),(int)(screenSize.height*heightRate));
        frame.setResizable(false);
        frame.setLocationByPlatform(true);
    }

    public static FrameBuilder setSize(double widthRate,double heightRate){
        return new FrameBuilder(widthRate,heightRate);
    }
    public FrameBuilder setTitle(String title){
        frame.setTitle(title);
        return this;
    }

    public FrameBuilder setMenuBar(JMenuBar menuBar){
        frame.setJMenuBar(menuBar);
        menuBar.setOpaque(false);
        return this;
    }

    public FrameBuilder add(JPanel panel){
        frame.add(panel);
        panel.setOpaque(false);
        return this;
    }

    public FrameBuilder setLayout(LayoutManager manager){
        frame.setLayout(manager);
        return this;
    }
    public FrameBuilder add(JPanel panel,Object constrains){
        frame.add(panel,constrains);
        panel.setOpaque(false);
        return this;
    }

    public JFrame build(){
        return frame;
    }
}
