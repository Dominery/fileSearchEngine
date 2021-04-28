package hust.cs.javacourse.search.view;

import com.sun.xml.internal.fastinfoset.tools.FI_DOM_Or_XML_DOM_SAX_SAXEvent;
import hust.cs.javacourse.search.query.Hit;

import javax.swing.*;
import javax.swing.text.*;
import java.awt.*;
import java.awt.event.MouseAdapter;
import java.awt.event.MouseEvent;
import java.io.File;
import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.util.List;
import java.util.Optional;
import java.util.Set;
import java.util.stream.Collectors;

/**
 * @author suyu
 * @create 2021-04-24-12:05
 */
public class ContentPanel{
    private final JTextPane textPane;
    private Optional<Path> file =Optional.empty();
    public ContentPanel(){
        textPane = new JTextPane();
//        textPane.setEditable(false);
        textPane.setOpaque(false);
        JPopupMenu popMenu = new JPopupMenu();
        JMenuItem save = new JMenuItem("save");
        save.addActionListener(e->{
            file.ifPresent(path->{
                try {
                    Files.write(path,textPane.getText().getBytes());
                } catch (IOException exception) {
                    exception.printStackTrace();
                }
            });
        });
        popMenu.add(save);
        textPane.addMouseListener(new MouseAdapter() {
            @Override
            public void mouseClicked(MouseEvent e) {
                if (e.getButton() == MouseEvent.BUTTON3) {

                    popMenu.show(textPane, e.getX(), e.getY());

                }
            }
        });
    }
    public JScrollPane getComponent(){
        JScrollPane scrollPane = new JScrollPane(textPane);
        scrollPane.setOpaque(false);
        scrollPane.getViewport().setOpaque(false);
        return scrollPane;
    }
    public void setContent(Object o) throws IOException{
        if(o instanceof File){
            File f = (File)o;
            file = Optional.of(f.toPath());
            setContent(f);
        }else if(o instanceof Hit){
            file = Optional.of(Paths.get(((Hit)o).getDocPath()));
            setContent((Hit)o);
        }
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
                insertText(word,Color.RED);
                index = i+word.length();
            }
            insertText("\n",Color.BLACK);
        });
    }

    public void insertText(String text, Color textColor){
        SimpleAttributeSet simpleAttributeSet = new SimpleAttributeSet();
        StyleConstants.setForeground(simpleAttributeSet, textColor);
        StyleConstants.setFontSize(simpleAttributeSet, 18);
        StyledDocument doc = textPane.getStyledDocument();
        try{
            doc.insertString(doc.getLength(),text,simpleAttributeSet);
        } catch (BadLocationException e) {
            e.printStackTrace();
        }
    }
}
