package hust.cs.javacourse.search.view;

import hust.cs.javacourse.search.index.DocumentBuilder;
import hust.cs.javacourse.search.index.Index;
import hust.cs.javacourse.search.index.IndexBuilder;
import hust.cs.javacourse.search.query.Hit;
import hust.cs.javacourse.search.query.IndexSearcher;
import hust.cs.javacourse.search.query.ScoreCalculator;
import hust.cs.javacourse.search.query.impl.NullCalculator;

import javax.swing.*;
import javax.swing.tree.DefaultMutableTreeNode;
import javax.swing.tree.DefaultTreeModel;
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
    private final IndexBuilder indexBuilder;

    public FileTreePanel(DocumentBuilder builder){
        super();
        indexBuilder = new IndexBuilder(builder);
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
        Index index = indexBuilder.buildIndex(rootPath.toFile());
        searcher.setIndex(index);
    }

    private void buildTree(){
        
    }
}
