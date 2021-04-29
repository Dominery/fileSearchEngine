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
        frame.setLocation((screenSize.width-frame.getWidth())/2,(screenSize.height-frame.getHeight())/2);
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

    public FrameBuilder add(Component component,Object constrains){
        frame.add(component,constrains);
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
    public FrameBuilder add(JPanel panel){
        frame.add(panel);
        panel.setOpaque(false);
        return this;
    }
    public FrameBuilder setIcon(String path){
        frame.setIconImage(new ImageIcon(path).getImage());
        return this;
    }

    public FrameBuilder setBackgroundImage(String path){
        JLabel background = new JLabel(new ImageIconSizer(path).cover(frame.getWidth(),frame.getHeight()));
        frame.getLayeredPane().add(background,new Integer(Integer.MIN_VALUE));
        background.setBounds(0,0,frame.getWidth(),frame.getHeight());
        ((JPanel)frame.getContentPane()).setOpaque(false);
        return this;
    }

    public JFrame build(){
        return frame;
    }
}
