package hust.cs.javacourse.search.view;

import javax.swing.*;
import javax.swing.filechooser.FileNameExtensionFilter;
import java.awt.*;
import java.awt.event.ActionListener;
import java.io.File;
import java.util.function.Consumer;

/**
 * @author suyu
 * @create 2021-04-24-21:02
 */
class MenuBarBuilder{
    private final JMenuBar menuBar=new JMenuBar();
    private JMenu menu;
    private MenuBarBuilder(JMenu menu){
        this.menu = menu;
        menuBar.add(menu);
    }
    public static MenuBarBuilder mainMenu(String menuName){
        return new MenuBarBuilder(new JMenu(menuName));
    }
    public MenuBarBuilder addMenuItem(String itemName, ActionListener listener){
        JMenuItem menuItem = new JMenuItem(itemName);
        menuItem.addActionListener(listener);
        menu.add(menuItem);
        return this;
    }
    public MenuBarBuilder addMenu(String menuName){
        menu = new JMenu(menuName);
        menuBar.add(menu);
        return this;
    }
    public JMenuBar build(){
        return menuBar;
    }
//    public MenuBar(Consumer<File>buildConsumer){
//        load = JFileChooserBuilder.setDirectory(new File("."))
//                .addFileFilter(new FileNameExtensionFilter("index file","bat"))
//                .build();
//        build = JFileChooserBuilder.setDirectory(new File("../"))
//                .setFileSelectionMode(JFileChooser.DIRECTORIES_ONLY)
//                .build();
//        this.buildConsumer = buildConsumer;
//        addLoadFileMenu();
//    }
//    private JMenuItem build(){
//        JMenuItem buildItem = new JMenuItem("Build");
//        buildItem.addActionListener(e -> {
//            int i = build.showOpenDialog(this);
//            if(i==JFileChooser.APPROVE_OPTION){
//                buildConsumer.accept(build.getSelectedFile());
//            }
//        });
//        return buildItem;
//    }
}
