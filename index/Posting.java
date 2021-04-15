package hust.cs.javacourse.search.index;


import java.io.Serializable;
import java.util.ArrayList;
import java.util.List;
import java.util.Objects;

/**
 * <pre>
 * AbstractPosting是Posting对象的抽象父类.
 *      Posting对象代表倒排索引里每一项， 一个Posting对象包括:
 *          包含单词的文档id.
 *          单词在文档里出现的次数.
 *          单词在文档里出现的位置列表（位置下标不是以字符为编号，而是以单词为单位进行编号.
 *      必须实现下面二个接口:
 *          Comparable：可比较大小（按照docId大小排序）,
 *                      当检索词为二个单词时，需要求这二个单词对应的PostingList的交集,
 *                      如果每个PostingList按docId从小到大排序，可以提高求交集的效率.
 *          FileSerializable：可序列化到文件或从文件反序列化
 *  </pre>
 */
public class Posting implements Comparable<Posting>, Serializable {
    /**
     * 包含单词的文档id
     */
    private int docId;
    /**
     * 单词在文档里出现的次数
     */
    private int freq;
    /**
     * 单词在文档里出现的位置列表（以单词为单位进行编号，如第1个单词，第2个单词，...), 单词可能在文档里出现多次，因此需要一个List来保存
     */
    private List<Integer> positions = new ArrayList<>();
    private static final long serialVersionUID = 667720L;
    /**
     * 缺省构造函数
     */
    public Posting(){

    }

    /**
     * 构造函数
     * @param docId ：包含单词的文档id
     * @param freq  ：单词在文档里出现的次数
     * @param positions   ：单词在文档里出现的位置
     */
    public Posting(int docId, int freq, List<Integer> positions){
        this.docId = docId;
        this.freq = freq;
        this.positions = positions;
    }

    @Override
    public boolean equals(Object obj) {
        if(this == obj)return true;
        if(obj==null||getClass()!=obj.getClass())return false;
        Posting posting = (Posting) obj;
        return docId==posting.getDocId()&&freq==posting.getFreq()&&
                positions.equals(posting.getPositions());
    }

    @Override
    public int hashCode() {
        return Objects.hash(docId, freq, positions);
    }

    @Override
    public String toString() {
        return "Positing{"+
                "docId="+docId+
                ",freq="+freq+
                ",positions="+positions+
                "}";
    }


    public int getDocId() {
        return docId;
    }


    public void setDocId(int docId) {
        this.docId = docId;
    }


    public int getFreq() {
        return freq;
    }


    public void setFreq(int freq) {
        this.freq = freq;
    }


    public List<Integer> getPositions() {
        return positions;
    }


    public void setPositions(List<Integer> positions) {
        this.positions = positions;
    }


    public int compareTo(Posting o) {
        if(o!=null){
            return Integer.compare(docId,o.getDocId());
        }
        return 0;
    }


    public void sort() {
        positions.sort(Integer::compareTo);
    }
}
