package hust.cs.javacourse.search.index;

import hust.cs.javacourse.search.parse.AbstractTermTupleStream;
import hust.cs.javacourse.search.parse.impl.TermTupleFilter;
import hust.cs.javacourse.search.parse.impl.TermTupleScanner;
import hust.cs.javacourse.search.util.Config;
import hust.cs.javacourse.search.util.StopWords;

import java.io.*;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;
import java.util.function.Predicate;

/**
 * <pre>
 * AbstractDocumentBuilder是Document构造器的抽象父类.
 *      Document构造器的功能应该是由解析文本文档得到的TermTupleStream，产生Document对象.
 * </pre>
 */
public class DocumentBuilder {

    /**
     * <pre>
     * 由解析文本文档得到的TermTupleStream,构造Document对象.
     * @param docId             : 文档id
     * @param docPath           : 文档绝对路径
     * @param termTupleStream   : 文档对应的TermTupleStream
     * @return ：Document对象
     * </pre>
     */
    private final Predicate<String> minlength = word -> word.length()> Config.TERM_FILTER_MINLENGTH;
    private final Predicate<String> maxlength = word -> word.length() <= Config.TERM_FILTER_MAXLENGTH;
    private final Predicate<String> stopWords = word -> !Arrays.asList(StopWords.STOP_WORDS).contains(word);
    private final Predicate<String> enChar = word -> word.matches(Config.TERM_FILTER_PATTERN);

    public Document build(int docId, String docPath, AbstractTermTupleStream termTupleStream) {
        List<TermTuple> list = new ArrayList<>();
        TermTuple term;
        while ((term=termTupleStream.next())!=null){
            list.add(term);
        }
        termTupleStream.close();
        return new Document(docId,docPath,list);
    }

    /**
     * <pre>
     * 由给定的File,构造Document对象.
     * 该方法利用输入参数file构造出AbstractTermTupleStream子类对象后,内部调用
     *      AbstractDocument build(int docId, String docPath, AbstractTermTupleStream termTupleStream)
     * @param docId     : 文档id
     * @param docPath   : 文档绝对路径
     * @param file      : 文档对应File对象
     * @return          : Document对象
     * </pre>
     */
    public Document build(int docId, String docPath, File file) {
        BufferedReader br = null;
        Document absDoc = null;
        try {
            br = new BufferedReader(new FileReader(file));
            AbstractTermTupleStream ats = new TermTupleFilter(new TermTupleScanner(br),
                    minlength.and(maxlength).and(stopWords).and(enChar));
            absDoc =  build(docId,docPath,ats);
        } catch (FileNotFoundException e) {
            e.printStackTrace();
        } finally {
            try {
                if(br!=null)br.close();
            } catch (IOException exception) {
                exception.printStackTrace();
            }
        }
        return absDoc;
    }
}
