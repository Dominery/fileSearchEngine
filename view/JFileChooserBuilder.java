package hust.cs.javacourse.search.view;

import com.sun.javafx.logging.JFRInputEvent;

import javax.swing.*;
import javax.swing.filechooser.FileNameExtensionFilter;
import java.io.File;

/**
 * @author suyu
 * @create 2021-04-27-20:18
 */
public class JFileChooserBuilder {
    private final JFileChooser fileChooser;
    private JFileChooserBuilder(File file){
        fileChooser = new JFileChooser(file);
    }
    public static JFileChooserBuilder setDirectory(File file){
        return new JFileChooserBuilder(file);
    }
    public JFileChooserBuilder setSelectedFile(File file){
        fileChooser.setSelectedFile(file);
        return this;
    }
    public JFileChooserBuilder setFileSelectionMode(int mode){
        fileChooser.setFileSelectionMode(mode);
        return this;
    }
    public JFileChooserBuilder addFileFilter(FileNameExtensionFilter fileFilter){
        fileChooser.addChoosableFileFilter(fileFilter);
        return this;
    }
    public JFileChooser build(){
        return fileChooser;
    }
}
