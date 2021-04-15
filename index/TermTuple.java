package hust.cs.javacourse.search.index;

import hust.cs.javacourse.search.index.impl.Term;

import java.util.ArrayList;
import java.util.List;
import java.util.Objects;

/**
 * <pre>
 * AbstractTermTuple是所有TermTuple对象的抽象父类.
 *      一个TermTuple对象为三元组(单词，出现频率，出现的当前位置).
 *      当解析一个文档时，每解析到一个单词，应该产生一个三元组，其中freq始终为1(因为单词出现了一次).
 * </pre>
 *
 *
 */
public  class TermTuple {
    /**
     * 单词
     */
    public AbstractTerm term;
    /**
     * 出现次数，始终为1
     */
    public final int freq = 1;
    /**
     * 单词出现的当前位置
     */
    public int curPos ;

    public TermTuple(AbstractTerm term, int curPos) {
        this.term = term;
        this.curPos = curPos;
    }

    /**
     * 判断二个三元组内容是否相同
     * @param o ：要比较的另外一个三元组
     * @return 如果内容相等（三个属性内容都相等）返回true，否则返回false
     */

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;
        TermTuple termTuple = (TermTuple) o;
        return curPos == termTuple.curPos && Objects.equals(term, termTuple.term);
    }

    @Override
    public int hashCode() {
        return Objects.hash(term, freq, curPos);
    }
}
