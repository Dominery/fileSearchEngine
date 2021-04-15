package hust.cs.javacourse.search.query.impl;

import hust.cs.javacourse.search.index.AbstractPosting;
import hust.cs.javacourse.search.index.AbstractTerm;
import hust.cs.javacourse.search.query.AbstractHit;

import java.util.Map;

/**
 * @author suyu
 * @create 2021-04-14-20:25
 */
public class Hit extends AbstractHit {

    /**
     * 构造函数
     *
     * @param docId   : 文档id
     * @param docPath : 文档绝对路径
     */
    public Hit(int docId, String docPath) {
        super(docId, docPath);
    }

    /**
     * 构造函数
     *
     * @param docId              ：文档id
     * @param docPath            ：文档绝对路径
     * @param termPostingMapping ：命中的三元组列表
     */
    public Hit(int docId, String docPath, Map<AbstractTerm, AbstractPosting> termPostingMapping) {
        super(docId, docPath, termPostingMapping);
    }

    /**
     * 默认构造函数
     */
    public Hit() {
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

    public Map<AbstractTerm, AbstractPosting> getTermPostingMapping() {
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
                ",docId="+docId+
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
    public int compareTo(AbstractHit o) {
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
}
