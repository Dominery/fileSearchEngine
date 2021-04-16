package hust.cs.javacourse.search.query;

import hust.cs.javacourse.search.index.Index;
import hust.cs.javacourse.search.index.Posting;

import java.io.File;
import java.util.*;
import java.util.function.Function;
import java.util.stream.Collectors;
import java.util.stream.Stream;

/**
 * <pre>
 *  AbstractIndexSearcher是检索具体实现的抽象类
 * </pre>
 */
public class IndexSearcher {

    /**
     * 内存中的索引，子类对象被初始化时为空
     */
    protected Index index = new Index();

    public IndexSearcher() {
    }


    /**
     * 从指定索引文件打开索引，加载到index对象里. 一定要先打开索引，才能执行search方法
     *
     * @param indexFile ：指定索引文件
     */
    public void open(String indexFile) {
        index.load(new File(indexFile));
    }

    /*
    * 一个Term对应的PostList中所有的Posting的docId都是不相同的，
    * 所以如果想要找出Terms的共同Posting，那么可以通过寻找有同一个docId的Posing，
    * 如果这些Posting数量与Terms的数量相同，那么这些Posting即为所求。
    * */

    private Stream<Hit> transform(Set<String>queryTerms){
        Map<Integer, List<TermPosPair>> docPoses = queryTerms.stream()
                .flatMap(term->
                    index.search(term)
                            .map(Set::stream)
                            .orElseGet(Stream::empty)
                            .map(pos-> new TermPosPair(term, pos))
                    )
                .collect(Collectors.groupingBy(TermPosPair::getDocId));
        return docPoses.values()
                .stream()
                .filter(l->l.size()==queryTerms.size())
                .flatMap(List::stream)
                .map(posting -> {
                    Map<String, Posting> collect = new HashMap<>();
                    collect.put(posting.term,posting.pos);
                    String path = index.getDocName(posting.getDocId());
                    return new Hit(posting.getDocId(), path, collect);
                });
    }


    public Hit[] search(List<Set<String>>queryTermsList, ScoreCalculator sorter){
        Map<Integer, Hit> collect = queryTermsList.stream()
                .flatMap(this::transform)
                .collect(Collectors.toMap(
                        Hit::getDocId,
                        Function.identity(),(h1,h2)->
                        {
                            h1.getTermPostingMapping().putAll(h2.getTermPostingMapping());
                            return h1;
                        }));

        return collect.values()
                .stream()
                .peek(hit -> hit.setScore(sorter.calculate(hit)))
                .sorted(Comparator.comparingDouble(Hit::getScore))
                .toArray(Hit[]::new);
    }

    public void setIndex(Index index) {
        this.index = index;
    }

    public Index getIndex(){
        return index;
    }


    private static class TermPosPair{
        private final String term;
        private final Posting pos;
        public TermPosPair(String term, Posting pos){
            this.term = term;
            this.pos = pos;
        }
        public int getDocId(){
            return pos.getDocId();
        }

    }

}
