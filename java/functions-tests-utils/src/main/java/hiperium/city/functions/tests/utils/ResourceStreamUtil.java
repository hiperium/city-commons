package hiperium.city.functions.tests.utils;

import java.io.File;
import java.io.FileInputStream;
import java.io.IOException;
import java.io.InputStream;
import java.nio.file.Files;
import java.nio.file.NoSuchFileException;
import java.nio.file.Paths;

/**
 * Provides utility methods for handling file resources located within the classpath.
 * This class offers methods to retrieve resource input streams and read resource contents as strings.
 */
public final class ResourceStreamUtil {

    private static final String TEST_RESOURCES_PATH = "src/test/resources/";

    private ResourceStreamUtil() {
        throw new UnsupportedOperationException("Utility classes should not be instantiated.");
    }

    /**
     * Reads and returns the content of a JSON file as a string from the specified file path.
     *
     * @param pathOfJsonDataFile The relative path to the JSON file within the test resources' directory.
     * @return A string containing the content of the specified JSON file.
     * @throws IOException If the file does not exist, or an error occurs during reading the file.
     */
    public static String getJsonFromFilePath(String pathOfJsonDataFile) throws IOException {
        File file = new File(TEST_RESOURCES_PATH + pathOfJsonDataFile);
        if (!file.exists()) {
            throw new NoSuchFileException("File not found: " + pathOfJsonDataFile);
        }
        return Files.readString(Paths.get(file.getPath()));
    }

    /**
     * Retrieves an input stream for reading the contents of a file located in the test resources directory.
     *
     * @param pathOfJsonDataFile The relative path of the file within the test resources directory.
     *                           This is appended to the base test resources path to locate the file.
     * @return InputStream pointing to the specified file.
     * @throws IOException If the specified file does not exist or an I/O error occurs.
     */
    public static InputStream getInputStreamFromFilePath(String pathOfJsonDataFile) throws IOException {
        File file = new File(TEST_RESOURCES_PATH + pathOfJsonDataFile);
        if (!file.exists()) {
            throw new NoSuchFileException("File not found: " + pathOfJsonDataFile);
        }
        return new FileInputStream(file);
    }
}
