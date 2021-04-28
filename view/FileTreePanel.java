package hust.cs.javacourse.search.view;

import hust.cs.javacourse.search.query.Hit;
import hust.cs.javacourse.search.util.FileWalker;

import javax.swing.*;
import javax.swing.tree.DefaultMutableTreeNode;
import javax.swing.tree.DefaultTreeCellRenderer;
import javax.swing.tree.DefaultTreeModel;
import javax.swing.tree.TreePath;
import java.awt.*;
import java.io.File;
import java.util.*;
import java.util.function.Consumer;
import java.util.function.Predicate;

/**
 * @author suyu
 * @create 2021-04-24-13:53
 */
public class FileTreePanel extends JPanel {
    private  Optional<DefaultTreeModel> model=Optional.empty();
    private DefaultMutableTreeNode root;
    private  Optional<JTree> tree=Optional.empty();
    private final DefaultTreeCellRenderer renderer;
    private final Consumer<File> listener;

    /**
     * Creates a new <code>JPanel</code> with a double buffer
     * and a flow layout.
     */
    public FileTreePanel() {
        this(null);
    }

    public FileTreePanel(Consumer<File>listener){
        super();
        this.listener = listener;
        renderer = new DefaultTreeCellRenderer();
        renderer.setLeafIcon(new ImageIconSizer("images/file_icon.png").scale(15,15));
        renderer.setClosedIcon(new ImageIconSizer("images/dir_close_icon.png").scale(15,20));
        renderer.setOpenIcon(new ImageIconSizer("images/dir_open_icon.png").scale(16,20));
    }


    public void setUp(File rootPath){
        root = buildTree(rootPath);
        model.orElseGet(()-> {
            DefaultTreeModel m = new DefaultTreeModel(root);
            model = Optional.of(m);
            return m;
        }).setRoot(root);
        model.ifPresent(m-> {
            tree.orElseGet(() -> { // if it's the first time
                JTree jTree = new JTree(m);
                jTree.setCellRenderer(renderer);
                add(new JScrollPane(jTree));
                tree = Optional.of(jTree);
                return jTree;
            }).setModel(m);
        });
        tree.ifPresent(t->t.addTreeSelectionListener(tsl->{
            DefaultMutableTreeNode selectedNode = (DefaultMutableTreeNode) t.getLastSelectedPathComponent();
            if(selectedNode==null)return;
            Object obj = selectedNode.getUserObject();
            if(selectedNode.isLeaf()){
                FileNode node = (FileNode)obj;
                listener.accept(node.file);
                System.out.println("hh");
            }
        }));
        filter();
    }

    public void pickNode(Hit[] documents){
        Arrays.stream(documents).forEach(hit -> {
            traverse(node->{
                FileNode fileNode = (FileNode) node.getUserObject();
                return fileNode.getFile().getAbsolutePath().equals(hit.getDocPath());
            },node->{
                ((FileNode) node.getUserObject()).setHit(hit);
                tree.orElseThrow(()->new RuntimeException("tree not exits"))
                        .makeVisible(new TreePath(model.orElseThrow(()->new RuntimeException("tree model not exits"))
                                .getPathToRoot(node)));
            });
        });
    }

    private DefaultMutableTreeNode buildTree(File rootFile){
        return new FileWalker<>(DefaultMutableTreeNode::add,(node,file)->
                node.add(new DefaultMutableTreeNode(new FileNode(file))))
                .walk(rootFile,file -> new DefaultMutableTreeNode(new FileNode(file)));
    }

    private void traverse(Predicate<DefaultMutableTreeNode> pick,
                          Consumer<DefaultMutableTreeNode> process){
        Enumeration e = root.depthFirstEnumeration();
        while (e.hasMoreElements()){
            DefaultMutableTreeNode node = (DefaultMutableTreeNode) e.nextElement();
            if(pick.test(node)){
                process.accept(node);
            }
        }
    }

    private void filter(){
        final boolean[] running = {true}; // if the result of filter is true, make running true
        Predicate<DefaultMutableTreeNode> filter = n->n.isLeaf()&&!((FileNode)n.getUserObject()).isFile();
        Consumer<DefaultMutableTreeNode> process = node->{
            traverse(
                    filter,
                    n->model.orElseThrow(()->new RuntimeException("tree model not exits"))
                            .removeNodeFromParent(n));
            running[0] = true;
        };

        while (running[0]){
            running[0] = false;
            traverse(filter,process);
        }
    }

    private static class FileNode{
        private final File file;
        private Optional<Hit> hit;

        public FileNode(File file) {
            this.file = file;
            hit = Optional.empty();
        }
        public void setHit(Hit hit){
            this.hit = Optional.ofNullable(hit);
        }
        public Optional<Hit> getHit(){
            return hit;
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



class ImageIconSizer {
    private final ImageIcon image;
    ImageIconSizer(String path){
        this(new ImageIcon(path));
    }
    ImageIconSizer(ImageIcon imageIcon){
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