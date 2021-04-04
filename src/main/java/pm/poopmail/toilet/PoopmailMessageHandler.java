package pm.poopmail.toilet;

import com.google.gson.Gson;
import com.google.gson.JsonObject;
import io.lettuce.core.pubsub.api.async.RedisPubSubAsyncCommands;
import jakarta.mail.Address;
import jakarta.mail.Session;
import jakarta.mail.internet.MimeMessage;
import java.io.ByteArrayInputStream;
import java.nio.charset.StandardCharsets;
import java.util.Base64;
import java.util.Set;
import java.util.stream.Collectors;
import org.subethamail.smtp.MessageContext;
import org.subethamail.smtp.RejectException;
import org.subethamail.smtp.helper.BasicMessageListener;

/**
 * Handler for incoming emails
 */
public class PoopmailMessageHandler implements BasicMessageListener {

    private final RedisPubSubAsyncCommands<String, String> redisPubSub;
    private final Set<String> allowedDomains;
    private final Gson gson;
    private final String redisKey;

    public PoopmailMessageHandler(final RedisPubSubAsyncCommands<String, String> redisPubSub,
                                  final Set<String> allowedDomains,
                                  final Gson gson,
                                  final String redisKey) {
        this.redisPubSub = redisPubSub;
        this.allowedDomains = allowedDomains;
        this.gson = gson;
        this.redisKey = redisKey;
    }

    @Override
    public void messageArrived(final MessageContext context, final String from, final String to, final byte[] data) throws RejectException {
        try {
            // Parse message
            final MimeMessage mimeMessage = new MimeMessage(Session.getInstance(System.getProperties()), new ByteArrayInputStream(data));
            final MimeMessageParser message = new MimeMessageParser(mimeMessage).parse();

            // Parse and check receivers
            final Set<String> receivers = message.getTo().stream()
                    .map(Address::toString)
                    .filter(s -> this.allowedDomains.contains(this.parseDomain(s)))
                    .collect(Collectors.toSet());
            if (receivers.isEmpty()) {
                throw new RejectException("Invalid receiver domain(s)");
            }

            // Get bytes of json string
            final byte[] jsonBytes = this.gson.toJson(this.toJson(message, receivers)).getBytes(StandardCharsets.UTF_8);

            // Publish email through Redis
            this.redisPubSub.publish(this.redisKey, Base64.getEncoder().encodeToString(jsonBytes));
        } catch (final Exception e) {
            // Reject email
            e.printStackTrace();
            throw new RejectException("Internal server error: " + e.getClass().getSimpleName() + ": " + e.getMessage());
        }
    }

    /**
     * Transforms a MIME message into a json object
     *
     * @param message The message
     *
     * @return A json object
     *
     * @throws Exception If something goes wrong
     */
    private JsonObject toJson(final MimeMessageParser message, final Set<String> receivers) throws Exception {
        final JsonObject rootObj = new JsonObject();
        rootObj.addProperty("from", message.getFrom());
        rootObj.add("to", this.gson.toJsonTree(receivers));
        rootObj.addProperty("subject", this.emptyIfNull(message.getSubject()));
        rootObj.addProperty("attachments", message.getAttachmentList().size());

        final JsonObject contentObj = new JsonObject();
        contentObj.addProperty("plain", this.emptyIfNull(message.getPlainContent()));
        contentObj.addProperty("html", this.emptyIfNull(message.getHtmlContent()));
        rootObj.add("content", contentObj);
        return rootObj;
    }

    /**
     * Returns an empty string if the input string is null, otherwise the input string will be returned
     *
     * @param str The input string
     *
     * @return Empty string or input string
     */
    private String emptyIfNull(final String str) {
        return str == null ? "" : str;
    }

    /**
     * Attempts to parse the domain from a email address
     *
     * @param s The address
     *
     * @return The parsed domain
     */
    private String parseDomain(final String s) {
        return s.contains("@") ? s.split("@")[1] : s;
    }

}
