package reactive;

import java.io.IOException;

public class First {
    private String key;
    private String toWrite;
    private Config config;

    First(String key, Config config) throws IOException, InterruptedException {
        this.key = key;
        this.config = config;
        toWrite = new Reloadable("first", config).Get();
    }

    public void write() throws IOException, InterruptedException {
        {
            System.out.println(toWrite);
        }
    }
}
