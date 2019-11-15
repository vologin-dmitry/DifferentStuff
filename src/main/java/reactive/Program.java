package reactive;

import java.io.File;
import java.io.IOException;
import java.nio.file.*;
import java.util.concurrent.TimeUnit;

public class Program {
    public static void main(String[] args) throws IOException, InterruptedException {
        Path path = Paths.get(".", "src" + File.separator + "main" + File.separator + "resources");
        Config config = new Config(path, "ReadableThing.txt");
        First fst = new First("first", config);
        for (int i = 0; i < 10; ++i)
        {
            fst.write();
            TimeUnit.SECONDS.sleep(5);
        }
    }
}