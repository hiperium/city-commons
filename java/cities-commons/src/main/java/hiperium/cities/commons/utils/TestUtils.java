package hiperium.cities.commons.utils;

import hiperium.cities.commons.loggers.HiperiumLogger;
import org.awaitility.Awaitility;
import org.springframework.lang.NonNull;
import org.springframework.messaging.Message;
import org.springframework.messaging.MessageHeaders;
import software.amazon.awssdk.services.dynamodb.DynamoDbClient;
import software.amazon.awssdk.services.dynamodb.model.DescribeTableRequest;
import software.amazon.awssdk.services.dynamodb.model.DescribeTableResponse;
import software.amazon.awssdk.services.dynamodb.model.ResourceNotFoundException;
import software.amazon.awssdk.services.dynamodb.model.TableStatus;

import java.time.Duration;
import java.util.Map;

/**
 * The TestUtils class provides utility methods for test-related operations.
 * It contains methods to wait for a DynamoDB table to be ready and to create a message with a byte array payload.
 */
public final class TestUtils {

    private static final HiperiumLogger LOGGER = new HiperiumLogger(TestUtils.class);

    private TestUtils() {
        throw new IllegalStateException("Utility classes cannot be instantiated.");
    }

    /**
     * Waits for a specified DynamoDB table to become active, polling at defined intervals
     * and up to a maximum wait time.
     *
     * @param dynamoDbClient The DynamoDB client used to communicate with the DynamoDB service.
     * @param tableName      The name of the DynamoDB table to check for readiness.
     * @param maxWaitTime    The maximum wait time, in seconds, before giving up.
     * @param checkInterval  The interval, in seconds, between successive status checks of the table.
     */
    public static void waitForDynamoDbToBeReady(final DynamoDbClient dynamoDbClient,
                                                final String tableName, int maxWaitTime, int checkInterval) {
        LOGGER.info("Waiting for table '{}' to be ready.", tableName);
        Awaitility.await()
            .atMost(Duration.ofSeconds(maxWaitTime))            // maximum wait time
            .pollInterval(Duration.ofSeconds(checkInterval))    // frequency check time
            .until(() -> {
                DescribeTableRequest request = DescribeTableRequest.builder()
                    .tableName(tableName)
                    .build();
                try {
                    DescribeTableResponse response = dynamoDbClient.describeTable(request);
                    return TableStatus.ACTIVE.equals(response.table().tableStatus());
                } catch (ResourceNotFoundException e) {
                    LOGGER.error("Table '{}' not found in DynamoDB.", tableName);
                }
                return false;
            });
    }

    /**
     * Creates a new message with the given byte array as the payload.
     *
     * @param bytes The byte array to be used as the payload.
     * @return The newly created message.
     */
    public static Message<byte[]> createMessage(byte[] bytes) {
        return new Message<>() {
            @NonNull
            @Override
            public byte[] getPayload() {
                return bytes;
            }

            @NonNull
            @Override
            public MessageHeaders getHeaders() {
                return new MessageHeaders(Map.of());
            }
        };
    }
}
