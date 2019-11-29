import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.GlobalScope
import kotlinx.coroutines.channels.Channel
import kotlinx.coroutines.delay
import kotlinx.coroutines.launch
import java.io.File
import java.nio.file.*
import java.util.*
import kotlin.io.NoSuchFileException


class FileConfigSource(private val directory : Path, private val fileName : String) : ConfigSource{
    private var config: List<String>? = null
    private val file = Paths.get(directory.toAbsolutePath().toString() + File.separator + fileName)
    private val watchService: WatchService? = FileSystems.getDefault().newWatchService()
    private var key: WatchKey? = null

    init
    {
        directory.register(watchService, StandardWatchEventKinds.ENTRY_MODIFY)
    }

    override suspend fun subscribe(dataStream: Channel<RawProperty>)  {
            val run = GlobalScope.launch()
            {
                try {
                    for ( string  in Files.readAllLines(file))
                    {
                        val splitted = string.split(' ')
                        dataStream.send(RawString(StringDescription(splitted[0]), splitted[1]))
                    }
                    while (true) {
                        var updated = LinkedList<String>()
                        try {
                            var updated = LinkedList<String>()
                            updated = Files.readAllLines(file) as LinkedList<String>;
                            key = watchService!!.take();
                        }
                        catch (e : NoSuchFileException)
                        {
                            delay(10)
                            updated = Files.readAllLines(file) as LinkedList<String>;
                        }
                        for ( string  in giveMeListOfChanges(config, updated))
                        {
                            val splitted = string.split(' ')
                            dataStream.send(RawString(StringDescription(splitted[0]), splitted[1]))
                        }
                        key!!.reset();
                    }
                }
                catch (e: Exception)
                {
                    e.printStackTrace();
                }
            }
            run.start();
    }

    private suspend fun giveMeListOfChanges(previous: List<String>?, updated: List<String>): List<String> {
        val changes: LinkedList<String> = LinkedList()
        if (previous == null)
        {
            config = updated
            return updated
        }
        for (upd in updated) {
            if (!previous.contains(upd)) {
                changes.add(upd)
            }
        }
        config = updated
        return changes
    }

}