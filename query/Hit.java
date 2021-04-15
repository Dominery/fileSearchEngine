package hust.cs.javacourse.search.query;

import hust.cs.javacourse.search.index.Posting;
import hust.cs.javacourse.search.util.FileUtil;

import java.util.Map;
import java.util.Objects;
import java.util.TreeMap;

/**
 * <pre>
 * AbstractHit是一个搜索命中结果的抽象类. 该类子类要实现Comparable接口.
 * 实现该接口是因为需要必须比较大小，用于命中结果的排序.
 * </pre>
 */
public class Hit implements Comparable<Hit>{
    /**
     * 文档id
     */
    protected int docId;
    /**
     * 文档绝对路径
     */
    protected String docPath;
    /**
     * 文档原文内容，显示搜索结果时有用
     */
    protected String content;
    /**
     * 命中的单词和对应的Posting键值对，对计算文档得分有用，对于一个查询命中结果，一个term对应的是Posting而不是PostingList
     */
    protected Map<String, Posting> termPostingMapping = new TreeMap<>();

    /**
     * 该命中文档的得分，文档的得分通过Sort接口计算.每个文档得分默认值为1.0
     */
    protected double score = 1.0;

    /**
     * 默认构造函数
     */
    public Hit(){

    }

    /**
     * 构造函数
     * @param docId     : 文档id
     * @param docPath   : 文档绝对路径
     */
    public Hit(int docId, String docPath){
        this.docId = docId;
        this.docPath = docPath;
        this.content = FileUtil.read(docPath);
    }

    /**
     * 构造函数
     * @param docId              ：文档id
     * @param docPath            ：文档绝对路径
     * @param termPostingMapping ：命中的三元组列表
     */
    public Hit(int docId, String docPath, Map<String, Posting> termPostingMapping){
        this.docId = docId;
        this.docPath = docPath;
        this.termPostingMapping.putAll(termPostingMapping);
        this.content = FileUtil.read(docPath);
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

    public String getContent() {
        return content;
    }

    public void setContent(String content) {
        this.content = content;
    }

    public Map<String, Posting> getTermPostingMapping() {
        return termPostingMapping;
    }

    /**
     * 获得命中结果的字符串表示, 用于显示搜索结果.
     *
     * @return : 命中结果的字符串表示
     */
    @Override
    public String toString() {
        return "Hit{"+
                "docId="+docId+
                ",docPath='"+docPath+"'"+
                ",content=" +getContent()
                +"}";
    }

    /**
     * 比较二个命中结果的大小，根据score比较
     *
     * @param o ：要比较的名字结果
     * @return ：二个命中结果得分的差值
     */
    @Override
    public int compareTo(Hit o) {
        if(o!=null){
            return Double.compare(score,o.getScore());
        }
        return 0;
    }


    public double getScore() {
        return score;
    }

    public void setScore(double score) {
        this.score = score;
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;
        Hit that = (Hit) o;
        return docId == that.docId &&
                Objects.equals(termPostingMapping, that.termPostingMapping);
    }

    @Override
    public int hashCode() {
        return Objects.hash(docId, termPostingMapping);
    }
}
