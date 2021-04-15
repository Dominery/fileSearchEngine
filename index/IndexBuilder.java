package hust.cs.javacourse.search.index;

import hust.cs.javacourse.search.util.Config;

import java.io.File;
import java.util.concurrent.ExecutorService;

/**
 * <pre>
 * AbstractIndexBuilder是索引构造器的抽象父类
 *      需要实例化一个具体子类对象完成索引构造的工作
 * </pre>
 */
public class IndexBuilder {
    /**
     * 构建索引必须解析文档构建Document对象，因此包含AbstractDocumentBuilder的子类对象
     */
    protected DocumentBuilder docBuilder;

    /**
     * docId计数器，每当解析一个文档并写入索引，计数器应该+1
     */
    protected int docId = 0;

    public IndexBuilder(DocumentBuilder docBuilder){
        this.docBuilder = docBuilder;
    }

    /**
     * <pre>
     * 构建指定目录下的所有文本文件的倒排索引.
     *      需要遍历和解析目录下的每个文本文件, 得到对应的Document对象，再依次加入到索引，并将索引保存到文件.
     * @param rootFile ：指定目录
     * @return ：构建好的索引
     * </pre>
     */
    public Index buildIndex(File rootFile) {
        Index index = new Index();
        File[] files = rootFile.listFiles();
        if(files==null)return index;
        for(File file:files){
            if (file.isDirectory()) {
                index.addIndex(buildIndex(file));
            }else{
                if(isPermittedFile(file))
                index.addDocument(docBuilder.build(docId++,file.getAbsolutePath(),file));
            }
        }
        return index;
    }

    public boolean isPermittedFile(File file){
        String[] split = file.getName().split("\\.");
        return Config.ACCEPT_EXTENSION.contains(split[split.length-1]);
    }

}
