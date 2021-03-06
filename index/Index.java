package hust.cs.javacourse.search.index;


import java.io.*;
import java.util.*;
import java.util.stream.Collectors;

/**
 * <pre>
 * AbstractIndex是内存中的倒排索引对象的抽象父类.
 *      一个倒排索引对象包含了一个文档集合的倒排索引.
 *      内存中的倒排索引结构为HashMap，key为Term对象，value为对应的PostingList对象.
 *      另外在AbstractIndex里还定义了从docId和docPath之间的映射关系.
 *      必须实现下面接口:
 *          FileSerializable：可序列化到文件或从文件反序列化.
 * </pre>
 */
public class Index implements Serializable{
    /**
     * 内存中的docId和docPath的映射关系, key为docId，value为对应的docPath.
     *      TreeMap可以对键值排序
     */
    private Map<Integer, String> docIdToDocPathMapping = new TreeMap<>();

    private File rootPath;

    /**
     * 内存中的倒排索引结构为HashMap，key为Term对象，value为对应的Set<Posting>对象.
     */
    private Map<String, Set<Posting>> termToPostingListMapping = new HashMap<>();

    private static final long serialVersionUID = 667750L;

    public Index(File rootPath) {
        this.rootPath = rootPath;
    }

    public Index() {
    }

    @Override
    public String toString() {
        return "Index{"+
                "termToPostingListMapping="+termToPostingListMapping
                +"}";
    }

    /**
     * 添加文档到索引，更新索引内部的HashMap
     *
     * @param document ：文档的AbstractDocument子类型表示
     */
    public void addDocument(Document document) {
        docIdToDocPathMapping.put(document.getDocId(),document.getDocPath());
        Map<String, List<Integer>> collect = document.getTuples()
                .collect(Collectors.groupingBy(
                        absTermTuple -> absTermTuple.term,
                        Collectors.mapping(absTermTuple -> absTermTuple.curPos,
                                Collectors.toList())
                ));
        collect.forEach((term,list)->{
            Posting posting = new Posting(document.getDocId(),list.size(),list);
            termToPostingListMapping
                    .computeIfAbsent(term,t->new HashSet<>())
                    .add(posting);
        });
    }

    public void addIndex(Index index){
        docIdToDocPathMapping.putAll(index.docIdToDocPathMapping);
        termToPostingListMapping.putAll(index.termToPostingListMapping);
    }
    public File getRootPath(){
        return rootPath;
    }

    /**
     * <pre>
     * 从索引文件里加载已经构建好的索引.内部调用FileSerializable接口方法readObject即可
     * @param file ：索引文件
     * </pre>
     */
    public void load(File file) {
        ObjectInputStream ois = null;
        try {
            ois = new ObjectInputStream(new FileInputStream(file));
            readObject(ois);
        } catch (IOException exception) {
            exception.printStackTrace();
        } finally {
            try {
                if(ois!=null)ois.close();
            } catch (IOException exception) {
                exception.printStackTrace();
            }
        }
    }

    /**
     * <pre>
     * 将在内存里构建好的索引写入到文件. 内部调用FileSerializable接口方法writeObject即可
     * @param file ：写入的目标索引文件
     * </pre>
     */
    public void save(File file) {
        ObjectOutputStream oos = null;
        try {
            oos = new ObjectOutputStream(new FileOutputStream(file));
            writeObject(oos);
        } catch (IOException exception) {
            exception.printStackTrace();
        } finally {
            try {
                if(oos!=null) oos.close();
            } catch (IOException exception) {
                exception.printStackTrace();
            }
        }
    }

    /**
     * 返回指定单词的PostingList
     *
     * @param term : 指定的单词
     * @return ：指定单词的PostingList;如果索引字典没有该单词，则返回空集合
     */
    public Optional<Set<Posting>> search(String term) {
        return Optional.ofNullable(termToPostingListMapping.get(term));
    }

    /**
     * 返回索引的字典.字典为索引里所有单词的并集
     *
     * @return ：索引中Term列表
     */
    public Set<String> getDictionary() {
        return termToPostingListMapping.keySet();
    }


    /**
     * 根据docId获得对应文档的完全路径名
     *
     * @param docId ：文档id
     * @return : 对应文档的完全路径名
     */
    public String getDocName(int docId) {
        return docIdToDocPathMapping.get(docId);
    }

    /**
     * 写到二进制文件
     *
     * @param out :输出流对象
     */
    public void writeObject(ObjectOutputStream out) {
        try {
            out.writeObject(this);
            out.flush();
        } catch (IOException exception) {
            exception.printStackTrace();
        }
    }

    /**
     * 从二进制文件读
     *
     * @param in ：输入流对象
     */
    public void readObject(ObjectInputStream in) {
        try {
            Index pre = (Index) in.readObject();
            termToPostingListMapping = pre.termToPostingListMapping;
            docIdToDocPathMapping = pre.docIdToDocPathMapping;
            rootPath = pre.rootPath;
        } catch (IOException | ClassNotFoundException exception) {
            exception.printStackTrace();
        }
    }
}
