package hust.cs.javacourse.search.view;

import hust.cs.javacourse.search.query.Hit;
import hust.cs.javacourse.search.query.IndexSearcher;
import hust.cs.javacourse.search.query.ScoreCalculator;
import hust.cs.javacourse.search.query.impl.NullCalculator;
import hust.cs.javacourse.search.util.FileWalker;

import javax.swing.*;
import javax.swing.tree.DefaultMutableTreeNode;
import javax.swing.tree.DefaultTreeCellRenderer;
import javax.swing.tree.DefaultTreeModel;
import java.awt.*;
import java.io.File;
import java.nio.file.Path;
import java.util.Arrays;
import java.util.Enumeration;
import java.util.List;
import java.util.Set;
import java.util.function.Consumer;
import java.util.function.Predicate;
import java.util.stream.Collectors;
import java.util.stream.Stream;

/**
 * @author suyu
 * @create 2021-04-24-13:53
 */
public class FileTreePanel extends JPanel {
    private final IndexSearcher searcher = new IndexSearcher();
    private ScoreCalculator calculator = new NullCalculator();
//    private final IndexBuilder indexBuilder;

    public FileTreePanel(Dimension dimension){
        super();
//        indexBuilder = new IndexBuilder(builder);
    }
    public FileTreePanel(){
        super();
    }


    public void search(String queryInfo){
        List<Set<String>> queryTermsList =
                Arrays.stream(queryInfo.split("\\|"))
                        .map(s -> s.trim().split("&"))
                        .map(array -> Arrays.stream(array)
                                .map(String::trim)
                                .collect(Collectors.toSet()))
                        .distinct()
                        .collect(Collectors.toList());
        Stream<Hit> search = searcher.search(queryTermsList, calculator);
    }

    public void setUp(Path rootPath){
//        Index index = indexBuilder.buildIndex(rootPath.toFile());
//        searcher.setIndex(index);
        JTree jTree = new JTree(buildTree(rootPath.toFile()));
        jTree.addTreeSelectionListener(tsl->{
            DefaultMutableTreeNode selectedNode = (DefaultMutableTreeNode) jTree.getLastSelectedPathComponent();
            if(selectedNode==null)return;

            Object obj = selectedNode.getUserObject();
            if(selectedNode.isLeaf()){
                FileNode node = (FileNode)obj;
                System.out.println(node);
            }
        });
        filter(jTree);
        DefaultTreeCellRenderer renderer = new DefaultTreeCellRenderer();
        renderer.setLeafIcon(new ScaledImageIcon("images/file_icon.png").scale(15,15));
        renderer.setClosedIcon(new ScaledImageIcon("images/dir_close_icon.png").scale(15,20));
        renderer.setOpenIcon(new ScaledImageIcon("images/dir_open_icon.png").scale(16,20));
        jTree.setCellRenderer(renderer);
        JScrollPane jScrollPane = new JScrollPane(jTree);
        add(jScrollPane);
    }

    private DefaultMutableTreeNode buildTree(File rootFile){
        return new FileWalker<>(DefaultMutableTreeNode::add,(node,file)->
                node.add(new DefaultMutableTreeNode(new FileNode(file))))
                .walk(rootFile,file -> new DefaultMutableTreeNode(new FileNode(file)));
    }

    private void traverse(DefaultMutableTreeNode root,
                          Predicate<DefaultMutableTreeNode> pick,
                          Consumer<DefaultMutableTreeNode> process){
        Enumeration e = root.depthFirstEnumeration();
        while (e.hasMoreElements()){
            DefaultMutableTreeNode node = (DefaultMutableTreeNode) e.nextElement();
            if(pick.test(node)){
                process.accept(node);
            }
        }
    }

    private void filter(JTree jTree){
        DefaultTreeModel model = (DefaultTreeModel) jTree.getModel();
        DefaultMutableTreeNode root = (DefaultMutableTreeNode) model.getRoot();
        final boolean[] running = {true}; // if the result of filter is true, make running true
        Predicate<DefaultMutableTreeNode> filter = n->n.isLeaf()&&!((FileNode)n.getUserObject()).isFile();
        Consumer<DefaultMutableTreeNode> process = node->{
            traverse(root,
                    filter,
                    model::removeNodeFromParent);
            running[0] = true;
        };

        while (running[0]){
            running[0] = false;
            traverse(root,filter,process);
        }
    }

    private static class FileNode{
        private final File file;

        public FileNode(File file) {
            this.file = file;
        }

        public File getFile(){
            return file;
        }

        @Override
        public String toString() {
            return file.getName();
        }

        public boolean isFile(){
            return file.isFile();
        }
    }
}



class ScaledImageIcon{
    private final ImageIcon image;
    ScaledImageIcon(String path){
        this(new ImageIcon(path));
    }
    ScaledImageIcon(ImageIcon imageIcon){
        image = imageIcon;
    }

    Image getImage() {
        return image.getImage();
    }
    ImageIcon scale(int width,int height){
        return new ImageIcon(getImage().getScaledInstance(width,height,Image.SCALE_DEFAULT));
    }

    ImageIcon contain(int width,int height){
        return sizing(width,height,((picRate, boxRate) -> picRate>boxRate));
    }
    ImageIcon cover(int width,int height){
        return sizing(width,height,((picRate, boxRate) -> picRate<boxRate));
    }

    private ImageIcon sizing(int width,int height,BiIntPredicted predicted){
        if(predicted.test(image.getIconWidth()*height,image.getIconHeight()*width)){
            int newHeight = width*image.getIconHeight()/image.getIconWidth();
            return scale(width,newHeight);
        }else{
            int newWidth = height* image.getIconWidth()/image.getIconHeight();
            return scale(newWidth,height);
        }
    }
    @FunctionalInterface
    private interface BiIntPredicted{
        boolean test(int picRate,int boxRate);
    }
}