import kotlinx.coroutines.channels.Channel
import kotlinx.coroutines.flow.Flow

interface ConfigSource {
    suspend fun subscribe(dataStream : Channel<RawProperty>)
}