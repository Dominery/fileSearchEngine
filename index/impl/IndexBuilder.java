package hust.cs.javacourse.search.index.impl;

import hust.cs.javacourse.search.index.DocumentBuilder;
import hust.cs.javacourse.search.index.Index;
import hust.cs.javacourse.search.index.AbstractIndexBuilder;

import java.io.File;

/**
 * @author suyu
 * @create 2021-04-14-16:26
 */
public class IndexBuilder extends AbstractIndexBuilder {
    public IndexBuilder(DocumentBuilder docBuilder) {
        super(docBuilder);
    }

    /**
     * <pre>
     * 构建指定目录下的所有文本文件的倒排索引.
     *      需要遍历和解析目录下的每个文本文件, 得到对应的Document对象，再依次加入到索引，并将索引保存到文件.
     * @param rootDirectory ：指定目录
     * @return ：构建好的索引
     * </pre>
     */
    @Override
    public Index buildIndex(String rootDirectory) {
        Index index = new Index();
        DocumentBuilder documentBuilder = new DocumentBuilder();
        File dir = new File(rootDirectory);
        for(File file:dir.listFiles()){
            if (file.isFile()) {
                index.addDocument(documentBuilder.build(docId++,file.getAbsolutePath(),file));
            }
        }
        return index;
    }
}
