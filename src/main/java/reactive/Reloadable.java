package reactive;

import io.reactivex.Observer;
import io.reactivex.disposables.Disposable;
import javafx.util.Pair;
import java.io.IOException;

public class Reloadable {

    volatile String value;

    Reloadable(String key, Config config) throws IOException, InterruptedException {
        value = config.config.getOrDefault(key, "error");
        config.ParseForKey(key).subscribe(new Observer<Pair<String,String>>() {
            @Override public void onSubscribe(Disposable d) { }
            @Override public void onNext(Pair<String,String> pair) {
                value = pair.getValue();
            }
            @Override public void onError(Throwable e) { }
            @Override public void onComplete() { }
        });
    }

    public String get() {
        return value;
    }
}