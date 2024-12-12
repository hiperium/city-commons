package hiperium.city.functions.tests.utils;

import com.amazonaws.services.lambda.runtime.events.APIGatewayProxyRequestEvent;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.messaging.Message;
import org.springframework.messaging.converter.MessageConverter;
import org.springframework.messaging.support.MessageBuilder;

import java.nio.charset.StandardCharsets;
import java.util.Objects;

/**
 * Utility class for testing the deserialization of JSON into {@link APIGatewayProxyRequestEvent}
 * objects and wrapping the deserialized object within a {@link Message}.
 */
public final class EventDeserializerTest {

    private static final Logger LOGGER = LoggerFactory.getLogger(EventDeserializerTest.class);

    private EventDeserializerTest() {
        throw new UnsupportedOperationException("Utility classes should not be instantiated.");
    }

    /**
     * Deserializes a JSON string into an {@link APIGatewayProxyRequestEvent} and wraps it within a {@link Message}.
     *
     * @param jsonContent       The JSON string that represents the API Gateway Proxy Request Event.
     * @param messageConverter  The {@link MessageConverter} instance used for deserialization.
     * @return A {@link Message} containing the deserialized {@link APIGatewayProxyRequestEvent}.
     * @throws IllegalArgumentException if the JSON content cannot be deserialized into an {@link APIGatewayProxyRequestEvent}.
     */
    public static Message<APIGatewayProxyRequestEvent> deserializeRequestEvent(final String jsonContent,
                                                                               final MessageConverter messageConverter) {
        LOGGER.info("Converting JSON request using the {} converter.", messageConverter.getClass().getSimpleName());

        Message<byte[]> jsonMessage = MessageBuilder
            .withPayload(jsonContent.getBytes(StandardCharsets.UTF_8))
            .build();

        APIGatewayProxyRequestEvent eventMessage = (APIGatewayProxyRequestEvent)
            messageConverter.fromMessage(jsonMessage, APIGatewayProxyRequestEvent.class);

        if (Objects.isNull(eventMessage)) {
            throw new IllegalArgumentException("Failed to convert JSON to APIGatewayProxyRequestEvent.");
        }
        return MessageBuilder
            .withPayload(eventMessage)
            .copyHeadersIfAbsent(eventMessage.getHeaders())
            .build();
    }
}
