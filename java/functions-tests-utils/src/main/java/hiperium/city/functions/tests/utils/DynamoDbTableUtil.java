package hiperium.city.functions.tests.utils;

import org.awaitility.Awaitility;
import software.amazon.awssdk.services.dynamodb.DynamoDbClient;
import software.amazon.awssdk.services.dynamodb.model.DescribeTableRequest;
import software.amazon.awssdk.services.dynamodb.model.DescribeTableResponse;
import software.amazon.awssdk.services.dynamodb.model.TableStatus;

import java.time.Duration;

/**
 * The DynamoDbTableUtil class provides utility methods for test-related operations.
 * It contains methods to wait for a DynamoDB table to be ready and to create a description with a byte array payload.
 */
public final class DynamoDbTableUtil {

    private DynamoDbTableUtil() {
        throw new UnsupportedOperationException("Utility classes should not be instantiated.");
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
                                                final String tableName,
                                                int maxWaitTime,
                                                int checkInterval) {
        Awaitility.await()
            .atMost(Duration.ofSeconds(maxWaitTime))            // maximum wait time
            .pollInterval(Duration.ofSeconds(checkInterval))    // frequency check time
            .until(() -> {
                DescribeTableRequest request = DescribeTableRequest.builder()
                    .tableName(tableName)
                    .build();
                DescribeTableResponse response = dynamoDbClient.describeTable(request);
                return TableStatus.ACTIVE.equals(response.table().tableStatus());
            });
    }
}
