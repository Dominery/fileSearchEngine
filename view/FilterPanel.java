package hust.cs.javacourse.search.view;

import hust.cs.javacourse.search.parse.FilterFactory;

import javax.swing.*;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.function.Consumer;
import java.util.function.Predicate;
import java.util.stream.Collectors;

/**
 * @author suyu
 * @create 2021-04-24-10:09
 */
public class FilterPanel extends JPanel {
    private final Map<JCheckBox,String> choices = new HashMap<>();
    private Consumer<Predicate<String>> receiver;
    public FilterPanel(){
        FilterFactory.filterChars.forEach(this::registerFilter);
    }


    private void registerFilter(String name,String value){
        JCheckBox checkBox = new JCheckBox(name);
        checkBox.addActionListener(event->{
            List<String> filters = choices.keySet().stream()
                    .filter(JCheckBox::isSelected)
                    .map(choices::get)
                    .map(String::trim)
                    .collect(Collectors.toList());
            receiver.accept(FilterFactory.create(filters));
        });
        choices.put(new JCheckBox(name),value);
    }

    public void register(Consumer<Predicate<String>> consumer){
        receiver = consumer;
    }
}
