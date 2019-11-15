package reactive;

import io.reactivex.Observable;
import javafx.util.Pair;
import java.io.IOException;
import java.nio.file.Path;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

public class Config {
    public Map<String, String> config;
    private ConfigStorage fileConfig;
    private Observable<String> configChanges;

    Config(Path path, String fileName) throws IOException {
        config = new HashMap<String, String>();
        ParseConfig(path, fileName);
        configChanges = fileConfig.watch();
    }

    private void ParseConfig(Path path, String fileName) throws IOException {
        fileConfig = new ConfigStorage(path, fileName);
        List<String> textConfig = fileConfig.load();
        for (String str: textConfig) {
            String[] parsed = str.split(" ");
            config.put(parsed[0], parsed[1]);
        }
    }

    public Observable<Pair<String,String>> ParseForKey(String key) {
        return configChanges.map(s -> {
            String[] str = s.split(" ");
            return new Pair<String,String>(str[0], str[1]);
        })
                .filter(s -> s.getKey().equals(key));
    }
}
