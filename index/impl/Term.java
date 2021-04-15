package hust.cs.javacourse.search.index.impl;

import hust.cs.javacourse.search.index.AbstractTerm;

import java.io.IOException;
import java.io.ObjectInputStream;
import java.io.ObjectOutputStream;

/**
 * @author suyu
 * @create 2021-04-14-13:57
 */
public class Term extends AbstractTerm {
    private static final long serialVersionUID = 667710L;

    public Term() {
    }

    public Term(String content) {
        super(content);
    }

    @Override
    public boolean equals(Object obj) {
        if(this == obj)return true;
        if(obj==null||getClass()!=obj.getClass())return false;
        Term term = (Term)obj;
        return content.equals(term.content);
    }

    @Override
    public String toString() {
        return content;
    }

    @Override
    public String getContent() {
        return content;
    }

    @Override
    public void setContent(String content) {
        this.content = content;
    }

    @Override
    public int compareTo(AbstractTerm o) {
        if(o!=null&&o.getContent()!=null){
            return o.getContent().compareTo(content);
        }
        return 0;
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
            Term term = (Term) in.readObject();
            setContent(term.content);
        } catch (IOException | ClassNotFoundException exception) {
            exception.printStackTrace();
        }
    }
}
