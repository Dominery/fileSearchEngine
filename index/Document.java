package hust.cs.javacourse.search.index;

import java.util.List;

/**
 *<pre>
 *     Document是文档对象.
 *          文档对象是解析一个文本文件得到结果，文档对象里面包含：
 *              文档id.
 *              文档的绝对路径.
 *              文档包含的三元组对象列表，一个三元组对象是抽象类AbstractTermTuple的子类实例
 *</pre>
 */
public class Document {
    /**
     * 文档id
     */
    private int docId;
    /**
     * 文档绝对路径
     */
    private String docPath;
    /**
     * 文档包含的三元组列表
     */
    private List<TermTuple> tuples;

    public Document(int docId, String docPath, List<TermTuple> tuples) {
        this.docId = docId;
        this.docPath = docPath;
        this.tuples = tuples;
    }


    public int getDocId() {
        return docId;
    }

    public void setDocId(int docId) {
        this.docId = docId;
    }

    public String getDocPath() {
        return docPath;
    }

    public void setDocPath(String docPath) {
        this.docPath = docPath;
    }

    public List<TermTuple> getTuples() {
        return tuples;
    }

    public void setTuples(List<TermTuple> tuples) {
        this.tuples = tuples;
    }

    @Override
    public String toString() {
        return "Document{" +
                "docId=" + docId +
                ", docPath='" + docPath + '\'' +
                ", tuples=" + tuples +
                '}';
    }

}
