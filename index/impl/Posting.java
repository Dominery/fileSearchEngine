package hust.cs.javacourse.search.index.impl;

import hust.cs.javacourse.search.index.AbstractPosting;

import java.io.IOException;
import java.io.ObjectInputStream;
import java.io.ObjectOutputStream;
import java.util.List;

/**
 * @author suyu
 * @create 2021-04-14-15:42
 */
public class Posting extends AbstractPosting {
    private static final long serialVersionUID = 667720L;
    public Posting() {
    }

    public Posting(int docId, int freq, List<Integer> positions) {
        super(docId, freq, positions);
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
    public String toString() {
        return "Positing{"+
                "docId="+docId+
                ",freq="+freq+
                ",positions="+positions+
                "}";
    }

    @Override
    public int getDocId() {
        return docId;
    }

    @Override
    public void setDocId(int docId) {
        this.docId = docId;
    }

    @Override
    public int getFreq() {
        return freq;
    }

    @Override
    public void setFreq(int freq) {
        this.freq = freq;
    }

    @Override
    public List<Integer> getPositions() {
        return positions;
    }

    @Override
    public void setPositions(List<Integer> positions) {
        this.positions = positions;
    }

    @Override
    public int compareTo(AbstractPosting o) {
        if(o!=null){
            return Integer.compare(docId,o.getDocId());
        }
        return 0;
    }

    @Override
    public void sort() {
        positions.sort(Integer::compareTo);
    }

    @Override
    public void writeObject(ObjectOutputStream out) {
        try {
            out.writeObject(this);
            out.flush();
        } catch (IOException exception) {
            exception.printStackTrace();
        }
    }

    @Override
    public void readObject(ObjectInputStream in) {
        try {
            Posting o = (Posting) in.readObject();
            setPositions(o.positions);
            setDocId(o.docId);
            setFreq(o.freq);
        } catch (IOException | ClassNotFoundException exception) {
            exception.printStackTrace();
        }
    }
}
