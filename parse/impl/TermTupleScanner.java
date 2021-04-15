package hust.cs.javacourse.search.parse.impl;

import hust.cs.javacourse.search.index.TermTuple;
import hust.cs.javacourse.search.index.impl.Term;
import hust.cs.javacourse.search.parse.AbstractTermTupleScanner;
import hust.cs.javacourse.search.util.Config;

import java.io.BufferedReader;
import java.io.IOException;
import java.util.ArrayList;
import java.util.List;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

/**
 * @author suyu
 * @create 2021-04-14-13:48
 */
public class TermTupleScanner extends AbstractTermTupleScanner {
    private final List<String> cache = new ArrayList<>();
    private int index;
    private int currentPos;
    private final Pattern regex = Pattern.compile("\\b(\\w+|-)\\b");

    public TermTupleScanner(BufferedReader input) {
        super(input);
    }

    @Override
    public TermTuple next() {
        if(index==cache.size()){
            cache.clear();
            String s = null;
            try {
                s = input.readLine();
            } catch (IOException exception) {
                exception.printStackTrace();
            }
            index = 0;
            if(s==null) {
                return null;
            }
            else if(s.length()==0){
                return next();
            }
            else {
                Matcher matcher = regex.matcher(s);
                while (matcher.find()){
                    cache.add(matcher.group(1));
                }
            }
        }
        String word = cache.get(index++);
        if(Config.IGNORE_CASE)word = word.toLowerCase();
        return new TermTuple(new Term(word),++currentPos);

    }

}
