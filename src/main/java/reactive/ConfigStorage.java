package reactive;

import io.reactivex.Observable;
import io.reactivex.ObservableOnSubscribe;
import io.reactivex.subjects.PublishSubject;
import io.reactivex.subjects.Subject;

import java.io.IOException;
import java.nio.file.*;
import java.util.*;

public class ConfigStorage {
    private List<String> config;
    private Path directory;
    private Path file;
    private WatchService watchService;
    private WatchKey key;

    public ConfigStorage(String path, String fileName) throws IOException
    {
        this.directory = Paths.get(path);
        this.file = Paths.get(this.directory + "\\" + fileName);
        watchService = FileSystems.getDefault().newWatchService();
        directory.register(watchService, StandardWatchEventKinds.ENTRY_MODIFY);
    }

    public Observable<String> watch() throws IOException, InterruptedException {
        while ((key = watchService.take()) != null) {
            for (WatchEvent<?> event : key.pollEvents()) {
                return (Observable.fromIterable(giveMeListOfChanges(config, Files.readAllLines(file))));
            }
            key.reset();
        }
        return null;
    }

    public List<String> load() throws IOException {
        return config = Files.readAllLines(file);
    }

    private List<String> giveMeListOfChanges(List<String> previous, List<String> updated){
        List<String> changes = new LinkedList<String>();
        for(int i = 0; i <updated.size(); ++i)
        {
            if(!previous.get(i).equals(updated.get(i))){
                changes.add(updated.get(i));
            }
        }
        return changes;
    }
}