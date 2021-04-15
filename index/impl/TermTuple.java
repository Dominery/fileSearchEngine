package hust.cs.javacourse.search.index.impl;

import hust.cs.javacourse.search.index.AbstractTerm;
import hust.cs.javacourse.search.index.AbstractTermTuple;

/**
 * @author suyu
 * @create 2021-04-14-13:59
 */
public class TermTuple extends AbstractTermTuple {
    public TermTuple(AbstractTerm term, int pos) {
        this.term = term;
        this.curPos = pos;
    }

    @Override
    public boolean equals(Object obj) {
        return false;
    }

    @Override
    public String toString() {
        return null;
    }
}
