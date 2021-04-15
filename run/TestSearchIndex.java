package hust.cs.javacourse.search.run;

import hust.cs.javacourse.search.query.Hit;
import hust.cs.javacourse.search.query.IndexSearcher;
import hust.cs.javacourse.search.query.impl.NullSort;
import hust.cs.javacourse.search.util.Config;

import java.util.Arrays;
import java.util.HashSet;

/**
 * 测试搜索
 */
public class TestSearchIndex {
    /**
     *  搜索程序入口
     * @param args ：命令行参数
     */
    public static void main(String[] args){
        IndexSearcher searcher = new IndexSearcher();
        searcher.open(Config.INDEX_DIR+"index.bat");
//        AbstractHit[] aaas = searcher.search("world", new NullSort());
//        System.out.println(Arrays.toString(aaas));
        HashSet<String> strings = new HashSet<>();
        strings.add("world");
        strings.add("temperatures");
        strings.add("dddd");
        HashSet<String> strings1 = new HashSet<>();
        strings1.add("google");
        strings1.add("announced");
        Hit[] search = searcher.search(Arrays.asList(strings,strings1), new NullSort());
        System.out.println(Arrays.toString(search));
    }
}
