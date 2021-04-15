package hust.cs.javacourse.search.run;

import hust.cs.javacourse.search.index.Index;
import hust.cs.javacourse.search.index.IndexBuilder;
import hust.cs.javacourse.search.index.DocumentBuilder;
import hust.cs.javacourse.search.query.AbstractHit;
import hust.cs.javacourse.search.query.AbstractIndexSearcher;
import hust.cs.javacourse.search.query.impl.IndexSearcher;
import hust.cs.javacourse.search.query.impl.NullSort;
import hust.cs.javacourse.search.util.Config;

import java.io.File;
import java.util.Arrays;

/**
 * 测试搜索
 */
public class TestSearchIndex {
    /**
     *  搜索程序入口
     * @param args ：命令行参数
     */
    public static void main(String[] args){
        AbstractIndexSearcher searcher = new IndexSearcher();
        searcher.open(Config.INDEX_DIR+"index.bat");
        AbstractHit[] aaas = searcher.search("world", new NullSort());
        System.out.println(Arrays.toString(aaas));
    }
}
