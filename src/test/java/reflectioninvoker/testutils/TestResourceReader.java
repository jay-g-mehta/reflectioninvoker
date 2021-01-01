package reflectioninvoker.testutils;

import com.fasterxml.jackson.databind.ObjectMapper;
import org.apache.commons.io.IOUtils;

import java.io.File;
import java.io.IOException;
import java.io.InputStream;

public class TestResourceReader {
    private ObjectMapper objectMapper = new ObjectMapper();

    private static final String TEST_RESOURCE_ROOT_RELATIVE_PATH = "src/test/resources/";
    private static final String TEST_RESOURCE_ROOT = "./";

    public <T> T readTestResource(final String jsonConfigFileResource, final Class<T> clazz) throws IOException {
        InputStream file = this.getClass()
                .getClassLoader()
                .getResourceAsStream(TEST_RESOURCE_ROOT + jsonConfigFileResource);
        T object = objectMapper.readValue(file, clazz);
        return object;
    }

    public String readTestResource(final String jsonConfigFileResource) throws IOException {
        InputStream file = this.getClass()
                .getClassLoader()
                .getResourceAsStream(TEST_RESOURCE_ROOT + jsonConfigFileResource);
        return IOUtils.toString(file, "UTF-8");
    }

    public static String getTestResourceRootAbsolutePath() {
        File file = new File(TEST_RESOURCE_ROOT_RELATIVE_PATH);
        final String absolutePath = file.getAbsolutePath();
        return absolutePath;
    }
}
