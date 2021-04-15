package hust.cs.javacourse.search.index.impl;

import hust.cs.javacourse.search.index.AbstractPosting;
import hust.cs.javacourse.search.index.AbstractPostingList;

import java.io.IOException;
import java.io.ObjectInputStream;
import java.io.ObjectOutputStream;
import java.util.*;
import java.util.stream.IntStream;

/**
 * @author suyu
 * @create 2021-04-14-16:06
 */
public class PostingList extends AbstractPostingList {
    private static final long serialVersionUID = 667730L;
    public PostingList() {
    }

    public PostingList(List<AbstractPosting> list) {
        this.list = list;
    }

    /**
     * 添加Posting,要求不能有内容重复的posting
     *
     * @param posting ：Posting对象
     */
    @Override
    public void add(AbstractPosting posting) {
        if(!list.contains(posting)){
            list.add(posting);
        }
    }

    @Override
    public String toString() {
        return "PostingList{" +
                "list=" + list +
                '}';
    }

    /**
     * 添加Posting列表,,要求不能有内容重复的posting
     *
     * @param postings ：Posting列表
     */
    @Override
    public void add(List<AbstractPosting> postings) {
        Set<AbstractPosting> set = new HashSet<>(list);
        set.addAll(postings);
        list.clear();
        list.addAll(set);
    }

    /**
     * 返回指定下标位置的Posting
     *
     * @param index ：下标
     * @return： 指定下标位置的Posting
     */
    @Override
    public AbstractPosting get(int index) {
        return list.get(index);
    }

    /**
     * 返回指定Posting对象的下标
     *
     * @param posting ：指定的Posting对象
     * @return ：如果找到返回对应下标；否则返回-1
     */
    @Override
    public int indexOf(AbstractPosting posting) {
        return list.indexOf(posting);
    }

    /**
     * 返回指定文档id的Posting对象的下标
     *
     * @param docId ：文档id
     * @return ：如果找到返回对应下标；否则返回-1
     */
    @Override
    public int indexOf(int docId) {
        OptionalInt any = IntStream.range(0, list.size())
                .filter(index -> get(index).getDocId() == docId)
                .findAny();
        return any.orElse(-1);
    }

    /**
     * 是否包含指定Posting对象
     *
     * @param posting ： 指定的Posting对象
     * @return : 如果包含返回true，否则返回false
     */
    @Override
    public boolean contains(AbstractPosting posting) {
        return list.contains(posting);
    }

    /**
     * 删除指定下标的Posting对象
     *
     * @param index ：指定的下标
     */
    @Override
    public void remove(int index) {
        list.remove(index);
    }

    /**
     * 删除指定的Posting对象
     *
     * @param posting ：定的Posting对象
     */
    @Override
    public void remove(AbstractPosting posting) {
        list.remove(posting);
    }

    /**
     * 返回PostingList的大小，即包含的Posting的个数
     *
     * @return ：PostingList的大小
     */
    @Override
    public int size() {
        return list.size();
    }

    /**
     * 清除PostingList
     */
    @Override
    public void clear() {
        list.clear();
    }

    /**
     * PostingList是否为空
     *
     * @return 为空返回true;否则返回false
     */
    @Override
    public boolean isEmpty() {
        return list.isEmpty();
    }

    /**
     * 根据文档id的大小对PostingList进行从小到大的排序
     */
    @Override
    public void sort() {
        list.sort(AbstractPosting::compareTo);
    }

    /**
     * 写到二进制文件
     *
     * @param out :输出流对象
     */
    @Override
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
    @Override
    public void readObject(ObjectInputStream in) {
        try {
            PostingList o = (PostingList) in.readObject();
            this.list = o.list;
        } catch (IOException | ClassNotFoundException exception) {
            exception.printStackTrace();
        }
    }

    public List<AbstractPosting> getList(){
        return list;
    }
}
