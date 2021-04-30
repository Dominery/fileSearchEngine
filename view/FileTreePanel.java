package hust.cs.javacourse.search.view;

import hust.cs.javacourse.search.query.Hit;
import hust.cs.javacourse.search.util.FileWalker;

import javax.swing.*;
import javax.swing.tree.DefaultMutableTreeNode;
import javax.swing.tree.DefaultTreeCellRenderer;
import javax.swing.tree.DefaultTreeModel;
import javax.swing.tree.TreePath;
import java.awt.*;
import java.awt.image.BufferedImage;
import java.io.File;
import java.util.*;
import java.util.function.Consumer;
import java.util.function.Predicate;

/**
 * @author suyu
 * @create 2021-04-24-13:53
 */
public class FileTreePanel extends JPanel {
    private final DefaultTreeModel model;
    private DefaultMutableTreeNode root;
    private final JTree tree;
    private final Consumer<Object> listener;
    private final JLabel label=new JLabel();


    public FileTreePanel(Consumer<Object>listener){
        super();
        this.listener = listener;
        DefaultTreeCellRenderer renderer = new DefaultTreeCellRenderer();
        renderer.setLeafIcon(new ImageIconSizer("images/file_icon.png").scale(15,15));
        renderer.setClosedIcon(new ImageIconSizer("images/dir_close_icon.png").scale(15,20));
        renderer.setOpenIcon(new ImageIconSizer("images/dir_open_icon.png").scale(16,20));
        tree = new JTree();
        model = (DefaultTreeModel) tree.getModel();
        tree.setCellRenderer(renderer);
        tree.setOpaque(false);
        setLayout(new BoxLayout(this,BoxLayout.Y_AXIS));
        JScrollPane treeScroll = new JScrollPane(tree);
        treeScroll.setOpaque(false);
        treeScroll.getViewport().setOpaque(false);
//        add(treeScroll);
        label.setOpaque(false);
        JScrollPane scrollPane = new JScrollPane(label);
        scrollPane.setOpaque(false);
        scrollPane.getViewport().setOpaque(false);
//        add(scrollPane);
//        treeScroll.setVerticalScrollBarPolicy(JScrollPane.VERTICAL_SCROLLBAR_NEVER);
        JSplitPane splitPane = new JSplitPane(JSplitPane.VERTICAL_SPLIT, treeScroll, label);
        splitPane.setOpaque(false);
        add(splitPane);
    }


    public void setUp(File rootPath){
        root = buildTree(rootPath);
        model.setRoot(root);
        tree.setModel(model);
        tree.addTreeSelectionListener(tsl->{
            DefaultMutableTreeNode selectedNode = (DefaultMutableTreeNode) tree.getLastSelectedPathComponent();
            if(selectedNode==null)return;
            Object obj = selectedNode.getUserObject();
            if(selectedNode.isLeaf()){
                FileNode node = (FileNode)obj;
                if(node.getHit()!=null)listener.accept(node.getHit());
                else listener.accept(node.file);
            }
        });
        filter();
    }

    public void pickNode(Hit[] documents){
        traverse(node->true,node->((FileNode)node.getUserObject()).setHit(null));
        final StringBuffer buffer = new StringBuffer();
        Arrays.stream(documents).forEach(hit -> {
            traverse(node->{
                FileNode fileNode = (FileNode) node.getUserObject();
                return fileNode.getFile().getAbsolutePath().equals(hit.getDocPath());
            },node->{
                FileNode n = (FileNode) node.getUserObject();
                n.setHit(hit);
                buffer.append(n).append("<br>");
                tree.makeVisible(new TreePath(model.getPathToRoot(node)));
            });
        });
        String info;
        if(buffer.length()==0)info = "Not Found!";
        else info = "<html><body>" + buffer.toString()+ "</body></html>";
        label.setText(info);
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
                    model::removeNodeFromParent);
            running[0] = true;
        };

        while (running[0]){
            running[0] = false;
            traverse(filter,process);
        }
    }

    private static class FileNode{
        private final File file;
        private Hit hit;

        public FileNode(File file) {
            this.file = file;
        }
        public void setHit(Hit hit){
            this.hit = hit;
        }
        public Hit getHit(){
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
    private  ImageIcon image;
    ImageIconSizer(String path){
        this(new ImageIcon(path));
    }
    ImageIconSizer(ImageIcon imageIcon){
        image = imageIcon;
    }

    private Image getImage() {
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