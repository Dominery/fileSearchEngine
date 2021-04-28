package hust.cs.javacourse.search.view;

import hust.cs.javacourse.search.query.Hit;

import javax.swing.*;
import javax.swing.text.*;
import java.awt.*;
import java.io.File;
import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.util.List;
import java.util.Set;
import java.util.stream.Collectors;

/**
 * @author suyu
 * @create 2021-04-24-12:05
 */
public class ContentPanel extends JPanel {
    private JTextPane textPane=new JTextPane();
    public ContentPanel(){
        super();
        JScrollPane scrollPane = new JScrollPane(textPane);
        scrollPane.setOpaque(false);
        scrollPane.getViewport().setOpaque(false);
        textPane.setEditable(false);
        setOpaque(false);
    }
    public void setContent(File file)throws IOException{
        Files.lines(file.toPath()).forEach(line->{
            insertText(line+"\n",Color.BLACK);
        });
    }
    public void clearContent(){
        textPane.setText("");
    }

    public void setContent(Hit hit) throws IOException {
        Path path = Paths.get(hit.getDocPath());
        Set<String> strings = hit.getTermPostingMapping().keySet();
        Files.lines(path).forEach(line->{
            List<String> contains = strings.stream()
                    .filter(line::contains)
                    .collect(Collectors.toList());
            if(contains.size()==0){
                insertText(line,Color.BLACK);
                return;
            }
            int index = 0;
            for(String word:contains){
                int i = line.indexOf(word);
                String substring = line.substring(index, i);
                insertText(substring,Color.BLACK);
                insertText(word,Color.PINK);
                index = i+word.length();
            }
            insertText("\n",Color.BLACK);
        });
    }

    public void insertText(String text, Color textColor){
        SimpleAttributeSet simpleAttributeSet = new SimpleAttributeSet();
        StyleConstants.setForeground(simpleAttributeSet, textColor);
        StyleConstants.setFontSize(simpleAttributeSet, 14);
        StyledDocument doc = textPane.getStyledDocument();
        try{
            doc.insertString(doc.getLength(),text,simpleAttributeSet);
        } catch (BadLocationException e) {
            e.printStackTrace();
        }
    }
}
