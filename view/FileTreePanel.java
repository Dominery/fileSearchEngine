package hust.cs.javacourse.search.view;

import hust.cs.javacourse.search.index.DocumentBuilder;
import hust.cs.javacourse.search.index.Index;
import hust.cs.javacourse.search.index.IndexBuilder;
import hust.cs.javacourse.search.query.Hit;
import hust.cs.javacourse.search.query.IndexSearcher;
import hust.cs.javacourse.search.query.ScoreCalculator;
import hust.cs.javacourse.search.query.impl.NullCalculator;
import hust.cs.javacourse.search.util.FileWalker;

import javax.swing.*;
import javax.swing.tree.DefaultMutableTreeNode;
import javax.swing.tree.DefaultTreeModel;
import java.awt.*;
import java.io.File;
import java.nio.file.Path;
import java.util.Arrays;
import java.util.List;
import java.util.Set;
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

    public FileTreePanel(DocumentBuilder builder){
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
        JScrollPane jScrollPane = new JScrollPane(jTree);
        jScrollPane.setPreferredSize(new Dimension(180,500));
        add(jScrollPane);
    }

    private DefaultMutableTreeNode buildTree(File rootFile){
        return new FileWalker<>(DefaultMutableTreeNode::add,(node,file)->
                node.add(new DefaultMutableTreeNode(new FileNode(file))))
                .walk(rootFile,file -> new DefaultMutableTreeNode(new FileNode(file)));
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
    }
}
