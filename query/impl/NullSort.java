package hust.cs.javacourse.search.query.impl;

import hust.cs.javacourse.search.query.Hit;
import hust.cs.javacourse.search.query.ScoreCalculator;

import java.util.List;

/**
 * @author suyu
 * @create 2021-04-14-20:57
 */
public class NullSort implements ScoreCalculator {

    /**
     * <pre>
     * 计算命中文档的得分, 作为命中结果排序的依据.
     *      计算文档的得分可以采取不同的策略, 因此这里的设计模式采用了策略模式，没有把这个方法放到AbstractHit及其子类里.
     *      而是放到接口Sort里，当我们需要不同的排序策略，只需要重新实现Sort的子类即可。即排序策略与被排序的对象(AbstractHit及其子类)应该分开。
     *      比如如果不排序，只需实现一个最简单的Sort接口实现类，比如叫NullSort类，在这个类里把所有文档的得分设置成一样的值。
     *      文档的得分值计算出来后要设置到AbstractHit子类对象里.
     * @param hit ：命中文档
     * @return ：命中文档的得分
     * </pre>
     */
    @Override
    public double calculate(Hit hit) {
        return hit.getScore();
    }
}
