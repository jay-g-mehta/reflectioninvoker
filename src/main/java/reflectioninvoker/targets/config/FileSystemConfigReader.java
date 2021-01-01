package reflectioninvoker.targets.config;

import lombok.AllArgsConstructor;
import lombok.Getter;
import org.apache.commons.io.IOUtils;
import reflectioninvoker.exception.TargetsConfigReadException;

import javax.annotation.concurrent.ThreadSafe;
import java.io.FileInputStream;
import java.io.IOException;

/**
 * Reads & returns Target saved in a config file on local file system
 */
@ThreadSafe
@Getter
@AllArgsConstructor
public class FileSystemConfigReader implements TargetsConfigReader {
    private final String encoding;
    private final String configPath;

    @Override
    public String read() {
        try {
            FileInputStream file = new FileInputStream(configPath);
            String data = IOUtils.toString(file, encoding);
            return  data;
        } catch (final IOException e) {
            throw new TargetsConfigReadException(e);
        }
    }
}
