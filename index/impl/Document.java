package hust.cs.javacourse.search.index.impl;

import hust.cs.javacourse.search.index.AbstractDocument;
import hust.cs.javacourse.search.index.AbstractTermTuple;

import java.util.ArrayList;
import java.util.List;

/**
 * @author suyu
 * @create 2021-04-14-15:57
 */
public class Document extends AbstractDocument {


    public Document() {
    }

    public Document(int docId, String docPath) {
        super(docId, docPath);
    }

    public Document(int docId, String docPath, List<AbstractTermTuple> tuples) {
        super(docId, docPath, tuples);
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

    public List<AbstractTermTuple> getTuples() {
        return tuples;
    }

    @Override
    public void addTuple(AbstractTermTuple tuple) {
        tuples.add(tuple);
    }

    @Override
    public boolean contains(AbstractTermTuple tuple) {
        return tuples.contains(tuple);
    }

    @Override
    public AbstractTermTuple getTuple(int index) {
        return tuples.get(index);
    }

    @Override
    public int getTupleSize() {
        return tuples.size();
    }

    public void setTuples(List<AbstractTermTuple> tuples) {
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
