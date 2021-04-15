package hust.cs.javacourse.search.query;

import hust.cs.javacourse.search.index.Index;
import hust.cs.javacourse.search.index.Posting;
import hust.cs.javacourse.search.query.impl.Hit;

import java.io.File;
import java.util.*;
import java.util.function.BinaryOperator;
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

    public IndexSearcher(Index index) {
        this.index = index;
    }


    /**
     * 从指定索引文件打开索引，加载到index对象里. 一定要先打开索引，才能执行search方法
     *
     * @param indexFile ：指定索引文件
     */
    public void open(String indexFile) {
        index.load(new File(indexFile));
    }

    /**
     * 根据单个检索词进行搜索
     *
     * @param queryTerm ：检索词
     * @param sorter    ：排序器
     * @return ：命中结果数组
     */
    public AbstractHit[] search(String queryTerm, Sort sorter) {
        Set<Posting> search = index.search(queryTerm);
        List<AbstractHit> hits = new ArrayList<>();
        for(Posting posting:search){
            HashMap<String, Posting> map = new HashMap<>();
            map.put(queryTerm,posting);
            Hit hit = new Hit(posting.getDocId(),
                    index.getDocName(posting.getDocId()),
                    map);
            hit.setScore(sorter.score(hit));
            hits.add(hit);
        }
        sorter.sort(hits);
        return hits.toArray(new AbstractHit[1]);
    }
    /*
    * 一个Term对应的PostList中所有的Posting的docId都是不相同的，
    * 所以如果想要找出Terms的共同Posting，那么可以通过寻找有同一个docId的Posing，
    * 如果这些Posting数量与Terms的数量相同，那么这些Posting即为所求。
    * */

    private Stream<Posting> searchPositions(Set<String>queryTerms){
        Map<Integer, List<Posting>> docPoses = queryTerms.stream()
                .map(index::search)
                .filter(Objects::nonNull)
                .flatMap(Set::stream)
                .collect(Collectors.groupingBy(Posting::getDocId));
        return docPoses.values()
                .stream()
                .filter(l->l.size()==queryTerms.size())
                .flatMap(List::stream);
    }

    private Stream<Hit> mergePosAndTerms(Stream<Posting>posStream,Set<String> queryTerms){
        return posStream
                .map(posting -> {
                    Map<String, Posting> collect = queryTerms.stream()
                            .collect(Collectors.toMap(Function.identity(), s -> posting));
                    String path = index.getDocName(posting.getDocId());
                    return new Hit(posting.getDocId(), path, collect);
                });
    }


    public AbstractHit[] search(List<Set<String>>queryTermsList, Sort sorter){
        Map<Integer, AbstractHit> collect = queryTermsList.stream()
                .flatMap(queryTerms ->
                        mergePosAndTerms(searchPositions(queryTerms), queryTerms)
                ).collect(Collectors.toMap(
                        AbstractHit::getDocId,
                        Function.<AbstractHit>identity(),
                        (t1, t2) -> {
                    t1.getTermPostingMapping().putAll(t2.getTermPostingMapping());
                    return t1;
                }));

        /*        Map<Integer, AbstractHit> collect = queryTermsList.stream()
                .flatMap(queryTerms ->
                        mergePosAndTerms(searchPositions(queryTerms), queryTerms)
                ).collect(Collectors.groupingBy(AbstractHit::getDocId
                ,Collectors.collectingAndThen(
                        Collectors.reducing((t1,t2)->{
                            t1.getTermPostingMapping().putAll(t2.getTermPostingMapping());
                            return t1;}),Optional::get)));
       */
        Collection<AbstractHit> values = collect.values();
        return values.toArray(new AbstractHit[1]);
    }

}
