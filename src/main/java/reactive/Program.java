package reactive;

import io.reactivex.*;
import java.io.IOException;
import java.nio.charset.Charset;
import java.nio.file.*;

public class Program {
    public static void main(String[] args) throws IOException, InterruptedException {
        WatchService watchService = FileSystems.getDefault().newWatchService();
        Path directory = Paths.get(".\\src\\main\\resources");
        directory.register(watchService, StandardWatchEventKinds.ENTRY_MODIFY);
        WatchKey key;

        Observable.fromIterable(Files.readAllLines(Paths.get(directory + "\\ReadableThing.txt"), Charset.forName("cp1252"))).subscribe(new Observer());
        while ((key = watchService.take()) != null) {
            for (WatchEvent<?> event : key.pollEvents()) {
                Observable.fromIterable(Files.readAllLines(Paths.get(directory + "\\ReadableThing.txt"), Charset.forName("cp1252"))).subscribe(new Observer());
            }
            key.reset();
        }
    }
}