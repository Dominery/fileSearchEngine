package hust.cs.javacourse.search.view;

import com.sun.xml.internal.fastinfoset.tools.FI_DOM_Or_XML_DOM_SAX_SAXEvent;
import hust.cs.javacourse.search.query.Hit;
import hust.cs.javacourse.search.run.TestBuildIndex;
import javafx.scene.layout.Pane;

import javax.swing.*;
import javax.swing.plaf.basic.BasicComboBoxUI;
import javax.swing.text.*;
import java.awt.*;
import java.awt.event.ActionListener;
import java.awt.event.MouseAdapter;
import java.awt.event.MouseEvent;
import java.io.File;
import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.util.*;
import java.util.List;
import java.util.regex.Matcher;
import java.util.regex.Pattern;
import java.util.stream.Collectors;

/**
 * @author suyu
 * @create 2021-04-24-12:05
 */
public class ContentPanel extends JPanel{
    private final JTextPane textPane;
    private Optional<Path> file =Optional.empty();
    Pattern pattern = Pattern.compile("\\b(\\w+|-)\\b");
    private int fontSize=20;
    private String fontFamily="Serif";
    private int fontStyle;
    public ContentPanel(){
        textPane = new JTextPane();
//        textPane.setEditable(false);
        textPane.setOpaque(false);
        setLayout(new BoxLayout(this,BoxLayout.Y_AXIS));
        add(getComponent());
        JSlider fontSizeSlider = new JSlider(12, 26, 18);
        fontSizeSlider.setOpaque(false);
        fontSizeSlider.addChangeListener(event->{fontSize=((JSlider)event.getSource()).getValue();});
        fontSizeSlider.addMouseListener(new MouseAdapter() {
            @Override
            public void mouseReleased(MouseEvent e) {
                if(e.getButton()==MouseEvent.BUTTON1)update();
            }
        });
        JComboBox<String> fontFamilies = new JComboBox<>(GraphicsEnvironment.getLocalGraphicsEnvironment().getAvailableFontFamilyNames());
        fontFamilies.setOpaque(false);
        setComoBoxTransparent(fontFamilies);

        fontFamilies.addActionListener(event-> {
            fontFamily = (String) (fontFamilies.getSelectedItem());
            update();
        });
        JPanel panel = new JPanel();
        panel.setMaximumSize(new Dimension(Integer.MAX_VALUE,fontFamilies.getHeight()));
        panel.setOpaque(false);
        panel.add(fontSizeSlider);
        panel.add(fontFamilies);
        add(panel);
        addPopMenu();
    }

    private<T> void setComoBoxTransparent(JComboBox<T>comboBox){
        UIManager.put("ComboBox.background", new Color(0,0,0,0));

        comboBox.setUI(new BasicComboBoxUI(){
            @Override
            public void installUI(JComponent c) {
                super.installUI(c);
                listBox.setOpaque(false);
                listBox.setForeground(Color.BLACK);
                listBox.setSelectionBackground(Color.BLACK);
                listBox.setSelectionForeground(Color.WHITE);
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
            Matcher matcher = pattern.matcher(line);
            int start = 0;
            List<String> splits = new ArrayList<>();
            while (matcher.find()){
                splits.add(line.substring(start, matcher.start()));
                splits.add(matcher.group(1));
                start = matcher.end();
            }
            splits.add(line.substring(start)+"\n");
            splits.stream()
                    .filter(s->s.length()>0)
                    .forEach(word->{
                        if(strings.contains(word.toLowerCase()))insertText(word,Color.RED);
                        else insertText(word,Color.BLACK);
                    });
        });
    }

    public void insertText(String text, Color textColor){
        SimpleAttributeSet simpleAttributeSet = new SimpleAttributeSet();
        StyleConstants.setForeground(simpleAttributeSet, textColor);
        StyledDocument doc = textPane.getStyledDocument();
        try{
            doc.insertString(doc.getLength(),text,simpleAttributeSet);
        } catch (BadLocationException e) {
            e.printStackTrace();
        }
    }

    public void addPopMenu(){
        ButtonGroup buttonGroup = new ButtonGroup();
        JRadioButtonMenuItem italic = new JRadioButtonMenuItem("Italic");
        JRadioButtonMenuItem normal = new JRadioButtonMenuItem("normal");
        JRadioButtonMenuItem bold = new JRadioButtonMenuItem("bold");
        buttonGroup.add(italic);
        buttonGroup.add(normal);
        buttonGroup.add(bold);
        ActionListener action = e->{
            fontStyle = (italic.isSelected()?Font.ITALIC:0)+(bold.isSelected()?Font.BOLD:0);
            update();
        };
        JPopupMenu popMenu = MenuBuilder.menuTye(new JPopupMenu())
                .addSingleMenu(new ImageIconSizer("images/save.png").scale(16, 16), "save", e -> {
                    file.ifPresent(path -> {
                        try {
                            Files.write(path, textPane.getText().getBytes());
                        } catch (IOException exception) {
                            exception.printStackTrace();
                        }
                    });
                })
                .addMenu("FontType")
                .addMenuItems(action,italic,normal,bold)
                .addSingleMenu(new ImageIconSizer("images/copy.png").scale(16,16),"copy",e->textPane.copy())
                .addSingleMenu(new ImageIconSizer("images/paste.png").scale(16,16),"paste",e->textPane.paste())
                .addSingleMenu(new ImageIconSizer("images/cut.png").scale(16,16),"cut",e->textPane.cut()).build();
        textPane.addMouseListener(new MouseAdapter() {
            @Override
            public void mouseClicked(MouseEvent e) {
                if (e.getButton() == MouseEvent.BUTTON3) {

                    popMenu.show(textPane, e.getX(), e.getY());

                }
            }
        });
    }

    private void update(){
        textPane.setFont(new Font(fontFamily,fontStyle,fontSize));
        textPane.repaint();
    }
}
