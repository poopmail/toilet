package pm.poopmail.toilet;

import com.google.gson.Gson;
import com.google.gson.GsonBuilder;
import io.lettuce.core.RedisClient;
import io.lettuce.core.pubsub.api.async.RedisPubSubAsyncCommands;
import java.util.Arrays;
import java.util.Set;
import java.util.stream.Collectors;
import org.subethamail.smtp.server.SMTPServer;

/**
 * Application launcher
 *
 * @author Maximilian Dorn (Cerus)
 */
public class Launcher {

    private static final Gson GSON = new GsonBuilder().setPrettyPrinting().create();

    public static void main(final String[] args) {
        // Retrieve configuration
        final String host = System.getenv("TOILET_HOSTNAME");
        final int port = Integer.parseInt(System.getenv("TOILET_PORT"));
        final String redisUri = System.getenv("TOILET_REDIS_URI");
        final String redisKey = System.getenv("TOILET_REDIS_KEY");
        final Set<String> allowedDomains = Arrays.stream(System.getenv("TOILET_FILTER").split(","))
                .collect(Collectors.toSet());

        // Initialize redis
        final RedisClient redisClient = RedisClient.create(redisUri);
        final RedisPubSubAsyncCommands<String, String> redisPubSub = redisClient.connectPubSub().async();

        // Initialize mail server
        final SMTPServer server = new SMTPServer.Builder()
                .hostName(host)
                .port(port)
                .messageHandler(new PoopmailMessageHandler(redisPubSub, allowedDomains, GSON, redisKey))
                .build();
        server.start();
    }

}