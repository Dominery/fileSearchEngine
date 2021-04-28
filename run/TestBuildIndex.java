package hust.cs.javacourse.search.run;

import hust.cs.javacourse.search.index.DocumentBuilder;
import hust.cs.javacourse.search.index.Index;
import hust.cs.javacourse.search.index.IndexBuilder;
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
        IndexBuilder builder = new IndexBuilder(documentBuilder);
        Index index = builder.buildIndex(new File("C:\\Users\\Dominery\\ProgrameProject\\JavaProjects"));
//        System.out.println(index);
        index.save(new File(Config.INDEX_DIR+"index.bat"));
    }
}
