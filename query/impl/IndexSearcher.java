package hust.cs.javacourse.search.query.impl;

import hust.cs.javacourse.search.index.Posting;
import hust.cs.javacourse.search.query.AbstractHit;
import hust.cs.javacourse.search.query.AbstractIndexSearcher;
import hust.cs.javacourse.search.query.Sort;

import java.io.File;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Set;

/**
 * @author suyu
 * @create 2021-04-14-20:23
 */
public class IndexSearcher extends AbstractIndexSearcher {

    /**
     * 从指定索引文件打开索引，加载到index对象里. 一定要先打开索引，才能执行search方法
     *
     * @param indexFile ：指定索引文件
     */
    @Override
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
    @Override
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

    /**
     * 根据二个检索词进行搜索
     *
     * @param queryTerm1 ：第1个检索词
     * @param queryTerm2 ：第2个检索词
     * @param sorter     ：    排序器
     * @param combine    ：   多个检索词的逻辑组合方式
     * @return ：命中结果数组
     */
    @Override
    public AbstractHit[] search(String queryTerm1, String queryTerm2, Sort sorter, LogicalCombination combine) {
        return new AbstractHit[0];
    }
}
