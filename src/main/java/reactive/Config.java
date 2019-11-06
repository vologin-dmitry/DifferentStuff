package reactive;

import io.reactivex.Observable;
import io.reactivex.Observer;
import io.reactivex.disposables.Disposable;
import javafx.util.Pair;
import java.io.IOException;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

public class Config {
    public Map<String, String> config;
    private ConfigStorage fileConfig;

    Config(String path, String fileName) throws IOException {
        config = new HashMap<String, String>();
        ParseConfig(path, fileName);
    }

    private void ParseConfig(String path, String fileName) throws IOException {
        fileConfig = new ConfigStorage(path, fileName);
        List<String> textConfig = fileConfig.load();
        for (int i = 0; i < textConfig.size(); ++i) {
            String[] parsed = textConfig.get(i).split(" ");
            config.put(parsed[0], parsed[1]);
        }
    }

    public Observable<Pair> ParseForKey(String key) throws IOException, InterruptedException {
        Observable<String> d = fileConfig.watch();
        return d.map(s -> {
            String[] str = s.split(" ");
            return new Pair(str[0], str[1]);
        })
                .filter(s -> ((String)((Pair) s).getKey()).equals(key));
    }
}
