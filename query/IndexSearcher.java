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
     * 多个检索词的逻辑组合
     */
    public static enum LogicalCombination{
        /**
         * 与,即多个检索词必须都在命中文档里出现
         */
        ADN,
        /**
         * 或, 即任意一个检索词在命中文档里出现
         */
        OR
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


    private Stream<Posting> searchPositions(Set<String>queryTerms){
         return queryTerms.stream()
                .map(index::search)
                .reduce((pre, cur) -> {
                    Set<Integer> curId = cur.stream()
                            .map(Posting::getDocId)
                            .collect(Collectors.toSet());
                    return pre.stream()
                            .filter(posting -> curId.contains(posting.getDocId()))
                            .collect(Collectors.toSet());
                }).orElse(new HashSet<>())
                .stream();
    };

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
        Map<Integer, List<AbstractHit>> collect = queryTermsList.stream()
                .flatMap(queryTerms ->
                        mergePosAndTerms(searchPositions(queryTerms), queryTerms)
                ).collect(Collectors.groupingBy(AbstractHit::getDocId));
        List<AbstractHit> result = new ArrayList<>();
        collect.forEach((key,value)->{
            value.stream().reduce((l1, l2) -> {
                l1.getTermPostingMapping().putAll(l2.getTermPostingMapping());
                return l1;
            }).ifPresent(result::add);
        });
        sorter.sort(result);
        return result.toArray(new AbstractHit[1]);
    }

}
