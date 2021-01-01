package reflectioninvoker.targets.config;

import com.fasterxml.jackson.core.type.TypeReference;
import com.fasterxml.jackson.databind.ObjectMapper;
import lombok.AllArgsConstructor;
import reflectioninvoker.exception.TargetConfigTranslationException;
import reflectioninvoker.targets.Target;

import java.io.IOException;
import java.util.List;


/**
 * This is a default translator implementation which reads Target definitions for
 * StaticMethodReflectionInvoker. The schema is expected in JSON format:
 *[{
 *     "clazz": "org.apache.commons.lang3.ArrayUtils",
 *     "method": "contains",
 *     "methodArgs": [["Mumbai", "Paris", "Tokyo"], "Helsinki"],
 *     "methodArgsType": ["[Ljava.lang.Object;", "java.lang.Object"]
 *   }, ...
 * ]
 *
 * NOTE: This implementation is using Jackson for config deserialization
 */
@AllArgsConstructor
public class StaticMethodTargetsJsonTranslator implements TargetsConfigTranslator{
    private ObjectMapper objectMapper;

    @Override
    public List<Target> translate(final String config) {

        try {
            final List<Target> targets = objectMapper.readValue(config,
                    new TypeReference<List<Target>>() { });
            return targets;
        } catch (final IOException e) {
            throw new TargetConfigTranslationException(e);
        }
    }
}
