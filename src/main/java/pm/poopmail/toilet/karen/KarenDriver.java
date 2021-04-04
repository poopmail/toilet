package pm.poopmail.toilet.karen;

import com.google.gson.JsonObject;
import io.lettuce.core.pubsub.api.async.RedisPubSubAsyncCommands;
import java.nio.charset.StandardCharsets;
import java.util.Base64;

public class KarenDriver {

    private final RedisPubSubAsyncCommands<String, String> redisPubSub;
    private final String key;

    public KarenDriver(final RedisPubSubAsyncCommands<String, String> redisPubSub, final String key) {
        this.redisPubSub = redisPubSub;
        this.key = key;
    }

    public void info(final String topic, final String desc) {
        this.send("INFO", topic, desc);
    }

    public void debug(final String topic, final String desc) {
        this.send("DEBUG", topic, desc);
    }

    public void warning(final String topic, final String desc) {
        this.send("WARNING", topic, desc);
    }

    public void error(final String topic, final String desc) {
        this.send("ERROR", topic, desc);
    }

    public void panic(final String topic, final String desc) {
        this.send("PANIC", topic, desc);
    }

    public void success(final String topic, final String desc) {
        this.send("SUCCESS", topic, desc);
    }

    public void send(final String type, final String topic, final String desc) {
        final JsonObject jsonObject = new JsonObject();
        jsonObject.addProperty("type", type);
        jsonObject.addProperty("topic", topic);
        jsonObject.addProperty("description", desc);
        jsonObject.addProperty("service", "toilet");
        this.redisPubSub.publish(this.key, Base64.getEncoder().encodeToString(jsonObject.toString().getBytes(StandardCharsets.UTF_8)));
    }

}
