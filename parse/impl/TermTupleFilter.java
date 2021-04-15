package hust.cs.javacourse.search.parse.impl;

import hust.cs.javacourse.search.index.TermTuple;
import hust.cs.javacourse.search.parse.AbstractTermTupleFilter;
import hust.cs.javacourse.search.parse.AbstractTermTupleStream;

import java.util.function.Predicate;

/**
 * @author suyu
 * @create 2021-04-14-14:20
 */
public class TermTupleFilter extends AbstractTermTupleFilter {
    private Predicate<String> p = word->true;
    /**
     * 构造函数
     *
     * @param input ：Filter的输入，类型为AbstractTermTupleStream
     */
    public TermTupleFilter(AbstractTermTupleStream input) {
        super(input);
    }
    public TermTupleFilter(AbstractTermTupleStream input,Predicate<String> pre){
        super(input);
        p = pre;
    }
    @Override
    public TermTuple next() {
        TermTuple next;
        while ((next=input.next())!=null){
            String word = next.term.getContent();
            if(p.test(word)){
                return next;
            }
        }
        return null;
    }

}
/*
* word.length()>= Config.TERM_FILTER_MINLENGTH&&
               word.length() <= Config.TERM_FILTER_MAXLENGTH&&
               !Arrays.asList(StopWords.STOP_WORDS).contains(word)&&
               word.matches(Config.TERM_FILTER_PATTERN)
* */