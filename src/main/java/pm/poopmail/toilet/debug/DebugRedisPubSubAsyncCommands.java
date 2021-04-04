package pm.poopmail.toilet.debug;

import io.lettuce.core.RedisFuture;
import io.lettuce.core.pubsub.RedisPubSubAsyncCommandsImpl;

public class DebugRedisPubSubAsyncCommands extends RedisPubSubAsyncCommandsImpl<String, String> {

    public DebugRedisPubSubAsyncCommands() {
        super(null, null);
    }

    @Override
    public RedisFuture<Long> publish(final String channel, final String message) {
        System.out.println("REDIS PUBLISH @ " + channel + ": " + message);
        return null;
    }

}
