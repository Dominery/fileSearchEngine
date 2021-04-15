package hust.cs.javacourse.search.parse;

import hust.cs.javacourse.search.util.Config;
import hust.cs.javacourse.search.util.StopWords;

import java.util.*;
import java.util.function.Predicate;

/**
 * @author suyu
 * @create 2021-04-15-19:34
 */
public class FilterFactory {
    private final static Map<String,Predicate<String>> filterMap=new HashMap<>();
    static {
        filterMap.put("l",word -> word.length()>= Config.TERM_FILTER_MINLENGTH);
        filterMap.put("g",word -> word.length() <= Config.TERM_FILTER_MAXLENGTH);
        filterMap.put("s",word -> !Arrays.asList(StopWords.STOP_WORDS).contains(word));
        filterMap.put("e",word -> word.matches(Config.TERM_FILTER_PATTERN));
    }

    public static Predicate<String> create(List<String> choices){
        return choices.stream()
                    .map(filterMap::get)
                    .filter(Objects::nonNull)
                    .reduce(Predicate::and)
                    .orElse(s->true);
    }
}
