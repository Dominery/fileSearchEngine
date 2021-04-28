package hust.cs.javacourse.search.run;

import hust.cs.javacourse.search.query.Hit;
import hust.cs.javacourse.search.query.IndexSearcher;
import hust.cs.javacourse.search.query.impl.NullCalculator;
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
        HashSet<String> strings = new HashSet<>();
        strings.add("gender");
        strings.add("recognition");
        strings.add("simply");
        HashSet<String> strings1 = new HashSet<>();
        strings1.add("google");
        strings1.add("announced");
        Arrays.stream(searcher.search(Arrays.asList(strings1,strings), new NullCalculator()))
                .forEach(System.out::println);
    }
}
