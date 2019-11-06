package reactive;

import io.reactivex.Maybe;
import io.reactivex.Observable;
import io.reactivex.Observer;
import io.reactivex.disposables.Disposable;
import io.reactivex.functions.Consumer;
import javafx.util.Pair;
import java.io.IOException;

public class Reloadable {
    protected String value;
    private String key;
    private Config config;

    Reloadable(String key, Config config) throws IOException, InterruptedException {
        this.config =config;
        this.key = key;
        value = config.config.getOrDefault(key, "no");

    }

    public String Get() throws IOException, InterruptedException {
            Observable<Pair> cfg = config.ParseForKey(key);
                    cfg.subscribe(new Observer<Pair>() {
                        @Override
                        public void onSubscribe(Disposable d) {
                        }

                        @Override
                        public void onNext(Pair pair) {
                            value = (String)pair.getValue();
                        }

                        @Override public void onError(Throwable e) {
                        }
                        @Override public void onComplete() {
                        }
                    });
        return value;
    }
}