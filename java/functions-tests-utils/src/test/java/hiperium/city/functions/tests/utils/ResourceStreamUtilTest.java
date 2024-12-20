package hiperium.city.functions.tests.utils;

import org.junit.jupiter.api.Test;

import java.io.IOException;
import java.io.InputStream;
import java.nio.file.NoSuchFileException;

import static org.assertj.core.api.Assertions.assertThat;
import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertThrows;

public class ResourceStreamUtilTest {

    @Test
    void testGetJsonFromFilePath_Success() throws IOException {
        String sampleJsonPath = "requests/valid-api-gw-v2-request.json";

        // Act
        String actualContent = ResourceStreamUtil.getJsonFromFilePath(sampleJsonPath);

        // Assert
        assertThat(actualContent).contains("headers");
        assertThat(actualContent).contains("body");
        assertThat(actualContent).contains("requestContext");

    }

    @Test
    void testGetJsonFromFilePath_FileNotFound() {
        String nonExistentFilePath = "nonexistent.json";

        // Assert
        assertThrows(NoSuchFileException.class, () ->
                ResourceStreamUtil.getJsonFromFilePath(nonExistentFilePath));
    }

    @Test
    void testGetJsonFromFilePath_FileIsEmpty() throws IOException {
        String emptyFilePath = "requests/empty-api-gw-request.json";

        // Act
        String actualContent = ResourceStreamUtil.getJsonFromFilePath(emptyFilePath);

        // Assert
        assertEquals("", actualContent);
    }

    @Test
    void testGetInputStreamFromFilePath_Success() throws IOException {
        String sampleFilePath = "requests/valid-api-gw-v2-request.json";

        // Act
        InputStream inputStream = ResourceStreamUtil.getInputStreamFromFilePath(sampleFilePath);

        // Assert
        assertThat(inputStream).isNotNull();
        inputStream.close(); // Ensure resources are closed properly
    }

    @Test
    void testGetInputStreamFromFilePath_FileNotFound() {
        String nonExistentFilePath = "nonexistent.json";

        // Assert
        assertThrows(NoSuchFileException.class, () ->
                ResourceStreamUtil.getInputStreamFromFilePath(nonExistentFilePath));
    }

    @Test
    void testGetInputStreamFromFilePath_FileIsEmpty() throws IOException {
        String emptyFilePath = "requests/empty-api-gw-request.json";

        // Act
        InputStream inputStream = ResourceStreamUtil.getInputStreamFromFilePath(emptyFilePath);
        int availableBytes = inputStream.available();

        // Assert
        assertEquals(0, availableBytes);
        inputStream.close(); // Ensure resources are closed properly
    }
}
