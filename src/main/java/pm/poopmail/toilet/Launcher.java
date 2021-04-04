package pm.poopmail.toilet;

import com.google.gson.Gson;
import com.google.gson.GsonBuilder;
import io.lettuce.core.RedisClient;
import io.lettuce.core.api.StatefulRedisConnection;
import io.lettuce.core.api.sync.RedisSetCommands;
import io.lettuce.core.pubsub.api.async.RedisPubSubAsyncCommands;
import org.subethamail.smtp.server.SMTPServer;
import pm.poopmail.toilet.debug.DebugRedisCommands;
import pm.poopmail.toilet.debug.DebugRedisPubSubAsyncCommands;

/**
 * Application launcher
 *
 * @author Maximilian Dorn (Cerus)
 */
public class Launcher {

    private static final Gson GSON = new GsonBuilder()
            .setPrettyPrinting()
            .disableHtmlEscaping()
            .create();

    public static void main(final String[] args) {
        // Retrieve configuration
        final String host = System.getenv("TOILET_HOSTNAME");
        final int port = Integer.parseInt(System.getenv("TOILET_PORT"));
        final String redisUri = System.getenv("TOILET_REDIS_URI");
        final String redisKey = System.getenv("TOILET_REDIS_KEY");
        final boolean debug = System.getenv("TOILET_DEBUG") != null;

        // Initialize redis
        final RedisPubSubAsyncCommands<String, String> redisPubSub;
        final RedisSetCommands<String, String> redis;
        final StatefulRedisConnection<String, String> redisCon;
        if (debug) {
            redisPubSub = new DebugRedisPubSubAsyncCommands();
            redis = new DebugRedisCommands();
            redisCon = null;
        } else {
            final RedisClient redisClient = RedisClient.create(redisUri);
            redisPubSub = redisClient.connectPubSub().async();
            redisCon = redisClient.connect();
            redis = redisCon.sync();
        }

        // Initialize mail server
        final SMTPServer server = new SMTPServer.Builder()
                .hostName(host)
                .port(port)
                .messageHandler(new PoopmailMessageHandler(redisPubSub, redis, GSON, redisKey))
                .build();
        server.start();

        // Cleanup
        Runtime.getRuntime().addShutdownHook(new Thread(() -> {
            server.stop();
            if (redisCon != null) {
                redisCon.close();
            }
        }));
    }

}
