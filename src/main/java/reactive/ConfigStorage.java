package reactive;

import io.reactivex.Observable;

import java.io.File;
import java.io.IOException;
import java.nio.file.*;
import java.util.*;
import java.util.concurrent.TimeUnit;

public class ConfigStorage {
    private List<String> config;
    private Path file;
    private WatchService watchService;
    private WatchKey key;

    public ConfigStorage(Path path, String fileName) throws IOException
    {
        this.file = Paths.get(path + File.separator + fileName);
        watchService = FileSystems.getDefault().newWatchService();
        path.register(watchService, StandardWatchEventKinds.ENTRY_MODIFY);
        config = load();
    }


    public Observable<String> watch() {
        return Observable.create(
                subscriber->
                {
                    Thread thr = new Thread(() -> {
                        try {
                            List<String> updated;
                            while ((key = watchService.take()) != null) {
                                try {
                                    updated = Files.readAllLines(file);
                                }
                                catch (NoSuchFileException e)
                                {
                                    TimeUnit.MILLISECONDS.sleep(10);
                                    updated = Files.readAllLines(file);
                                }
                                for (String string : giveMeListOfChanges(config, updated))
                                {
                                    subscriber.onNext(string);
                                }
                                key.reset();
                            }
                        }
                        catch (Exception e)
                        {
                            e.printStackTrace();
                        }
                    });
                    thr.start();
                }
        );
    }

    public List<String> load() throws IOException {
        return config = Files.readAllLines(file);
    }

    private List<String> giveMeListOfChanges(List<String> previous, List<String> updated){
        List<String> changes = new LinkedList<>();
        for(String upd : updated)
        {
            if(!previous.contains(upd)){
                changes.add(upd);
            }
        }
        config = updated;
        return changes;
    }
}