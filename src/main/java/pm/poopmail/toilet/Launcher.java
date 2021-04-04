package pm.poopmail.toilet;

import io.lettuce.core.RedisClient;
import io.lettuce.core.pubsub.api.async.RedisPubSubAsyncCommands;
import java.util.Base64;
import org.subethamail.smtp.server.SMTPServer;

/**
 * Application launcher
 */
public class Launcher {

    public static void main(final String[] args) {
        // Retrieve configuration
        final String host = System.getenv("TOILET_HOSTNAME");
        final int port = Integer.parseInt(System.getenv("TOILET_PORT"));
        final String redisUri = System.getenv("TOILET_REDIS_URI");
        final String redisKey = System.getenv("TOILET_REDIS_KEY");

        // Initialize redis
        final RedisClient redisClient = RedisClient.create(redisUri);
        final RedisPubSubAsyncCommands<String, String> redisPubSub = redisClient.connectPubSub().async();

        // Initialize mail server
        final SMTPServer server = new SMTPServer.Builder()
                .hostName(host)
                .port(port)
                .messageHandler((context, from, to, data) -> {
                    final String jsonStr = "{\"from\":\"" + from + "\"," +
                            "\"to\":\"" + to + "\"," +
                            "\"data\":\"" + Base64.getEncoder().encodeToString(data) + "\"}";
                    redisPubSub.publish(redisKey, jsonStr);
                })
                .build();
        server.start();
    }

}
