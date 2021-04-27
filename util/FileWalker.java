package hust.cs.javacourse.search.util;

import java.io.File;
import java.util.function.BiConsumer;
import java.util.function.Consumer;
import java.util.function.Function;
import java.util.function.Predicate;

/**
 * @author suyu
 * @create 2021-04-25-8:09
 */
public class FileWalker<R> {
    private final BiConsumer<R,R> dirProcessor;
    private final BiConsumer<R,File> fileProcessor;
    private Predicate<File> fileFilter=this::isPermittedFile;

    public FileWalker(BiConsumer<R,R> dirProcessor, BiConsumer<R,File> fileProcessor) {
        this.dirProcessor = dirProcessor;
        this.fileProcessor = fileProcessor;
    }

    public FileWalker(BiConsumer<R,R> dirProcessor, BiConsumer<R,File> fileProcessor, Predicate<File> fileFilter) {
        this(dirProcessor,fileProcessor);
        this.fileFilter = fileFilter;
    }

    public R walk(File rootFile, Function<File,R>initial){
        R node = initial.apply(rootFile);
        File[] files = rootFile.listFiles();
        if (files==null) return node;
        for(File file:files){
            if(file.isDirectory()){
                dirProcessor.accept(node,walk(file,initial));
            }else{
                if(fileFilter.test(file)){
                    fileProcessor.accept(node,file);
                }
            }
        }
        return node;
    }
    private boolean isPermittedFile(File file){
        String[] split = file.getName().split("\\.");
        return Config.ACCEPT_EXTENSION.contains(split[split.length-1]);
    }
}



