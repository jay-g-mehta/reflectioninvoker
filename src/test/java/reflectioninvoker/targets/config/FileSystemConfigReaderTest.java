package reflectioninvoker.targets.config;

import org.junit.jupiter.api.Test;

import static org.junit.jupiter.api.Assertions.*;
import static reflectioninvoker.testutils.TestResourceReader.getTestResourceRootAbsolutePath;

class FileSystemConfigReaderTest {

    @Test
    void should_construct() {
        String path = "/tmp/1";
        String encoding = "UTF-8";
        FileSystemConfigReader fileSystemConfigReader = new FileSystemConfigReader(encoding, path);
        assertEquals(path, fileSystemConfigReader.getConfigPath());
        assertEquals(encoding, fileSystemConfigReader.getEncoding());
    }

    @Test
    void should_read() {
        String path = getTestResourceRootAbsolutePath() + "/SampleStaticMethodTargetsConfig.json";
        String encoding = "UTF-8";
        FileSystemConfigReader fileSystemConfigReader = new FileSystemConfigReader(encoding, path);
        assertNotNull(fileSystemConfigReader.read());
    }
}