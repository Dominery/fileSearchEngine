package hust.cs.javacourse.search.run;

import hust.cs.javacourse.search.index.DocumentBuilder;
import hust.cs.javacourse.search.index.Index;
import hust.cs.javacourse.search.index.IndexBuilder;
import hust.cs.javacourse.search.parse.FilterFactory;
import hust.cs.javacourse.search.query.IndexSearcher;
import hust.cs.javacourse.search.query.ScoreCalculator;
import hust.cs.javacourse.search.query.impl.NullCalculator;


import java.io.File;
import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Paths;
import java.util.*;
import java.util.List;
import java.util.stream.Collectors;

/**
 * @author suyu
 * @create 2021-04-15-19:49
 */
public class Display {
    private final Map<String,Runnable> command = new HashMap<>();
    private final DocumentBuilder builder = new DocumentBuilder();
    private final IndexBuilder indexBuilder = new IndexBuilder(builder);
    private final IndexSearcher searcher = new IndexSearcher();
    private ScoreCalculator calculator = new NullCalculator();
    private boolean running = true;
    private final Scanner scanner = new Scanner(System.in);
    {
        command.put("1",()->{
            System.out.println("dir path:");
            String dir = scanner.nextLine();
            Index index = indexBuilder.buildIndex(new File(dir));
            searcher.setIndex(index);
        });

        command.put("2",()->{
            System.out.println("-l for min length -g for max length -e for english char -s for filter stop words");
            String input = scanner.nextLine();
            List<String> choices = Arrays.stream(input.split("-"))
                    .map(String::trim)
                    .collect(Collectors.toList());
            builder.setFilter(FilterFactory.create(choices));
        });

        command.put("3",()->{
            System.out.println("use & and | to search terms");
            String inputs = scanner.nextLine();
            List<Set<String>> queryTermsList =
                    Arrays.stream(inputs.split("\\|"))
                            .map(s -> s.trim().split("&"))
                            .map(array -> Arrays.stream(array)
                                    .map(String::trim)
                                    .collect(Collectors.toSet()))
                            .distinct()
                            .collect(Collectors.toList());
            searcher.search(queryTermsList, calculator)
                    .forEach(hit -> {
                        System.out.println("file path:"+hit.getDocPath());
                        try {
                            Files.lines(Paths.get(hit.getDocPath()))
                                    .filter(s -> s.length()>0)
                                    .forEach(System.out::println);
                        } catch (IOException exception) {
                            exception.printStackTrace();
                        }
                        System.out.println("pos:"+hit.getTermPostingMapping());
                    });
        });

        command.put("4",()->{
            System.out.println("input dir to save index.bat");
            String dir = scanner.nextLine();
            searcher.getIndex().save(new File(dir+"/index.bat"));
        });

        command.put("5",()->{
            System.out.println("please input the index.dat file path");
            String filepath = scanner.nextLine();
            searcher.open(filepath);
        });

        command.put("0",()-> running = false);
    }

    public void run(){
        while (running){
            showMenu();
            String s = scanner.nextLine();
            Runnable runnable = command.get(s.trim());
            if(runnable!=null)runnable.run();
        }
    }

    private void showMenu(){
        System.out.println("1.build index         2.change filter norm");
        System.out.println("3.search for terms    4.save index");
        System.out.println("5.load index          0.exit");
    }

    public static void main(String[] args) {
        new Display().run();
    }
}
