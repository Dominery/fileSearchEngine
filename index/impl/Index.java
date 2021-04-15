package hust.cs.javacourse.search.index.impl;

import hust.cs.javacourse.search.index.*;

import java.io.*;
import java.util.Iterator;
import java.util.List;
import java.util.Map;
import java.util.Set;
import java.util.stream.Collectors;

/**
 * AbstractIndex的具体实现类
 */
public class Index extends AbstractIndex {
    /**
     * 返回索引的字符串表示
     *
     * @return 索引的字符串表示
     */
    private static final long serialVersionUID = 667750L;
    @Override
    public String toString() {
        return "Index{"+
                "termToPostingListMapping="+termToPostingListMapping
                +"}";
    }

    /**
     * 添加文档到索引，更新索引内部的HashMap
     *
     * @param document ：文档的AbstractDocument子类型表示
     */
    @Override
    public void addDocument(AbstractDocument document) {
        docIdToDocPathMapping.put(document.getDocId(),document.getDocPath());
        List<AbstractTermTuple> tuples = document.getTuples();
        Map<AbstractTerm, List<Integer>> collect = tuples.stream()
                .collect(Collectors.groupingBy(
                        absTermTuple -> absTermTuple.term,
                        Collectors.mapping(absTermTuple -> absTermTuple.curPos,
                                Collectors.toList())
                ));
        collect.forEach((term,list)->{
            AbstractPosting posting = new Posting(document.getDocId(),list.size(),list);
            AbstractPostingList l = termToPostingListMapping.get(term);
            if(l!=null) l.add(posting);
            else{
                PostingList newPostList = new PostingList();
                newPostList.add(posting);
                termToPostingListMapping.put(term,newPostList);
            }
        });

    }

    /**
     * <pre>
     * 从索引文件里加载已经构建好的索引.内部调用FileSerializable接口方法readObject即可
     * @param file ：索引文件
     * </pre>
     */
    @Override
    public void load(File file) {
        ObjectInputStream ois = null;
        try {
            ois = new ObjectInputStream(new FileInputStream(file));
            readObject(ois);
            optimize();
        } catch (IOException exception) {
            exception.printStackTrace();
        } finally {
            try {
                if(ois!=null)ois.close();
            } catch (IOException exception) {
                exception.printStackTrace();
            }
        }
    }

    /**
     * <pre>
     * 将在内存里构建好的索引写入到文件. 内部调用FileSerializable接口方法writeObject即可
     * @param file ：写入的目标索引文件
     * </pre>
     */
    @Override
    public void save(File file) {
        ObjectOutputStream oos = null;
        try {
            oos = new ObjectOutputStream(new FileOutputStream(file));
            writeObject(oos);
        } catch (IOException exception) {
            exception.printStackTrace();
        } finally {
            try {
                if(oos!=null) oos.close();
            } catch (IOException exception) {
                exception.printStackTrace();
            }
        }
    }

    /**
     * 返回指定单词的PostingList
     *
     * @param term : 指定的单词
     * @return ：指定单词的PostingList;如果索引字典没有该单词，则返回null
     */
    @Override
    public AbstractPostingList search(AbstractTerm term) {
        return termToPostingListMapping.get(term);
    }

    /**
     * 返回索引的字典.字典为索引里所有单词的并集
     *
     * @return ：索引中Term列表
     */
    @Override
    public Set<AbstractTerm> getDictionary() {
        return termToPostingListMapping.keySet();
    }

    /**
     * <pre>
     * 对索引进行优化，包括：
     *      对索引里每个单词的PostingList按docId从小到大排序
     *      同时对每个Posting里的positions从小到大排序
     * 在内存中把索引构建完后执行该方法
     * </pre>
     */
    @Override
    public void optimize() {
        for (Map.Entry<AbstractTerm, AbstractPostingList> next : termToPostingListMapping.entrySet()) {
            AbstractPostingList value = next.getValue();
            value.sort();
            for (int i = 0; i < value.size(); i++) {
                value.get(i).getPositions().sort(Integer::compareTo);
            }
        }
    }

    /**
     * 根据docId获得对应文档的完全路径名
     *
     * @param docId ：文档id
     * @return : 对应文档的完全路径名
     */
    @Override
    public String getDocName(int docId) {
        return docIdToDocPathMapping.get(docId);
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
            Index pre = (Index) in.readObject();
            termToPostingListMapping = pre.termToPostingListMapping;
            docIdToDocPathMapping = pre.docIdToDocPathMapping;
        } catch (IOException | ClassNotFoundException exception) {
            exception.printStackTrace();
        }
    }

}
