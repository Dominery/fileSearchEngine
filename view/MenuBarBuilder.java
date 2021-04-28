package hust.cs.javacourse.search.view;

import javax.swing.*;
import java.awt.event.ActionListener;
import java.util.Arrays;

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
    public MenuBarBuilder addMenuItem(String itemName, ActionListener ...listeners){
        JMenuItem menuItem = new JMenuItem(itemName);
        addMenuItem(menuItem,listeners);
        return this;
    }
    public MenuBarBuilder addMenuItem(ImageIcon icon,ActionListener...listeners){
        addMenuItem(new JMenuItem(icon),listeners);
        return this;
    }
    public MenuBarBuilder addMenuItem(ImageIcon icon,String itemName,ActionListener...listeners){
        addMenuItem(new JMenuItem(itemName, icon), listeners);
        return this;
    }
    private void addMenuItem(JMenuItem menuItem,ActionListener...listeners){
        Arrays.stream(listeners).forEach(menuItem::addActionListener);
        menu.add(menuItem);
    }
    public MenuBarBuilder addMenu(String menuName){
        menu = new JMenu(menuName);
        menuBar.add(menu);
        return this;
    }
    public JMenuBar build(){
        return menuBar;
    }
}
