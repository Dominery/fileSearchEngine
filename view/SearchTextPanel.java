package hust.cs.javacourse.search.view;

import javax.swing.*;
import java.awt.*;
import java.util.ArrayList;
import java.util.List;
import java.util.function.Consumer;

/**
 * @author suyu
 * @create 2021-04-24-9:33
 */
public class SearchTextPanel extends JPanel {
    private final JTextField textField;
    private final List<Consumer<String>> actions = new ArrayList<>();
    public SearchTextPanel(){
        textField = createTextFiled();
        add(textField);
        add(searchButton());
    }

    private JTextField createTextFiled(){
        JTextField textField = new JTextField("input words using & or | to connect",20);
        textField.setFont(new Font("Arial",Font.PLAIN,18));
        textField.setForeground(Color.WHITE);
        textField.setOpaque(false);
        return textField;
    }

    private JButton searchButton(){
        JButton button = new JButton();
        button.setIcon(new ImageIconSizer("images/search.png").scale(30,30));
        button.setContentAreaFilled(false);
        button.addActionListener(event->{
            String trim = textField.getText().trim();
            actions.forEach(consumer -> consumer.accept(trim));
            textField.setText("");
        });
        return button;
    }

    public void register(Consumer<String>consumer){
        actions.add(consumer);
    }
}
