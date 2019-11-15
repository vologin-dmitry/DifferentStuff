package reactive;

import java.io.IOException;

public class First {
    private String key;
    private Reloadable toWrite;
    private Config config;

    First(String key, Config config) throws IOException, InterruptedException {
        this.key = key;
        this.config = config;
        toWrite = new Reloadable(key, config);
    }

    public void write() throws IOException, InterruptedException {
        {
            System.out.println(toWrite.get());
        }
    }
}
