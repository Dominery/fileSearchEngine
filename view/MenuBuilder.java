package hust.cs.javacourse.search.view;

import javax.swing.*;
import java.awt.event.ActionListener;
import java.util.Arrays;
import java.util.Optional;

/**
 * @author suyu
 * @create 2021-04-24-21:02
 */
 class MenuBuilder<T extends JComponent>{
    private final T menuBar;
    private JComponent menu;
    private MenuBuilder(T menuType){
        menuBar = menuType;
        menu = menuType;
    }
    public static<T extends JComponent> MenuBuilder<T> menuTye(T menuType){
        return new MenuBuilder<>(menuType);
    }
    public MenuBuilder<T> addMenuItem(ImageIcon icon, String itemName, ActionListener...listeners){
       return addMenuItem(new JMenuItem(itemName, icon), listeners);
    }
    public MenuBuilder<T> addMenuItems(ActionListener listener,JMenuItem...menuItems){
        Arrays.stream(menuItems)
                .peek(menuItem -> menuItem.addActionListener(listener))
                .forEach(menu::add);
        return this;
    }
    public MenuBuilder<T> addMenuItem(JMenuItem menuItem,ActionListener...listeners){
        Arrays.stream(listeners).forEach(menuItem::addActionListener);
        menu.add(menuItem);
        return this;
    }
    public MenuBuilder<T> addSingleMenu(ImageIcon icon, String itemName,ActionListener...listeners){
        JMenuItem menuItem = new JMenuItem(itemName, icon);
        Arrays.stream(listeners).forEach(menuItem::addActionListener);
        menuBar.add(menuItem);
        return this;
    }
    public MenuBuilder<T> addMenu(String menuName){
        menu = new JMenu(menuName);
        menuBar.add(menu);
        return this;
    }
    public T build(){
        return menuBar;
    }
}
