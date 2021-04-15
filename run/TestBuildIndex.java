package hust.cs.javacourse.search.run;

import hust.cs.javacourse.search.index.DocumentBuilder;
import hust.cs.javacourse.search.index.AbstractIndex;
import hust.cs.javacourse.search.index.AbstractIndexBuilder;
import hust.cs.javacourse.search.index.impl.IndexBuilder;
import hust.cs.javacourse.search.util.Config;

import java.io.File;

/**
 * 测试索引构建
 */
public class TestBuildIndex {
    /**
     *  索引构建程序入口
     * @param args : 命令行参数
     */
    public static void main(String[] args){
        DocumentBuilder documentBuilder = new DocumentBuilder();
        AbstractIndexBuilder builder = new IndexBuilder(documentBuilder);
        AbstractIndex index = builder.buildIndex(Config.DOC_DIR);
//        System.out.println(index);
        index.save(new File(Config.INDEX_DIR+"index.bat"));
    }
}
