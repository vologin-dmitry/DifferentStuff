package reactive;

import io.reactivex.*;
import io.reactivex.disposables.Disposable;
import io.reactivex.functions.Consumer;

import java.io.IOException;
import java.nio.charset.Charset;
import java.nio.file.*;
import java.sql.Time;
import java.util.concurrent.TimeUnit;

public class Program {
    public static void main(String[] args) throws IOException, InterruptedException {
        Config config = new Config(".\\src\\main\\resources", "ReadableThing.txt");
        First fst = new First("first", config);
        fst.write();
        TimeUnit.SECONDS.sleep(10);
        fst.write();
    }
}