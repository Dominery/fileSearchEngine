package hust.cs.javacourse.search.view;

import hust.cs.javacourse.search.parse.FilterFactory;

import javax.swing.*;
import javax.swing.border.Border;
import javax.swing.border.EtchedBorder;
import javax.swing.border.TitledBorder;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.function.Consumer;
import java.util.stream.Collectors;

/**
 * @author suyu
 * @create 2021-04-24-10:09
 */
public class FilterPanel extends JPanel {
    private final Map<JCheckBox,String> choices = new HashMap<>();
    private final Consumer<List<String>> receiver;
    public FilterPanel(Consumer<List<String>>receiver){
        setLayout(new BoxLayout(this,BoxLayout.Y_AXIS));
        FilterFactory.filterChars.forEach(this::registerFilter);
        this.receiver = receiver;
        Border border = BorderFactory.createEtchedBorder(EtchedBorder.LOWERED);
        TitledBorder titled = BorderFactory.createTitledBorder(border, "filter");
        setBorder(titled);
        choices.keySet().forEach(this::add);
    }


    private void registerFilter(String value,String name){
        JCheckBox checkBox = new JCheckBox(name);
        checkBox.setOpaque(false);
        checkBox.addActionListener(event->{
            List<String> filters = choices.keySet().stream()
                    .filter(JCheckBox::isSelected)
                    .map(choices::get)
                    .map(String::trim)
                    .collect(Collectors.toList());
            receiver.accept(filters);
        });
        choices.put(checkBox,value);
    }
}
