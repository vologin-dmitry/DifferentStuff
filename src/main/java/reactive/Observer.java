package reactive;

import io.reactivex.disposables.Disposable;

public class Observer implements io.reactivex.Observer<String> {

    @Override
    public void onSubscribe(Disposable d) {
    }

    @Override
    public void onNext(String s) {
        System.out.print(s);
        System.out.print("\n");
    }

    @Override
    public void onError(Throwable e) {
        System.err.print("Error " + e);
    }

    @Override
    public void onComplete() {
        System.out.print("______________________");
    }
}