package reflectioninvoker.targets.config;

import com.fasterxml.jackson.core.type.TypeReference;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.google.common.collect.ImmutableSet;
import lombok.AllArgsConstructor;
import reflectioninvoker.exception.TargetConfigTranslationException;
import reflectioninvoker.targets.Target;

import java.io.IOException;
import java.util.List;
import java.util.Map;
import java.util.Set;
import java.util.stream.Collectors;


/**
 * This is a default translator implementation which deserializes Target definitions from JSON.
 * The schema is expected in JSON format:
 *[{
 *     "clazz": "org.apache.commons.lang3.ArrayUtils",
 *     "method": "contains",
 *     "methodArgs": [["Mumbai", "Paris", "Tokyo"], "Helsinki"],
 *     "methodArgsType": ["[Ljava.lang.Object;", "java.lang.Object"],
 *     "clazzInstance": {...}
 *   }, ...
 * ]
 *
 * NOTE:
 * 1. This implementation is using Jackson for config deserialization
 * 2. methodArgs and clazzInstance are deserialized to Target type if it were
 * 3. methodArgs and clazzInstance are NOT deserialized to class type if it were. Instead it will return as map.
 */
@AllArgsConstructor
public class TargetsJsonTranslator implements TargetsConfigTranslator {
    private final Set<String> TARGET_FIELDS = ImmutableSet.of("clazz", "method", "methodArgs", "methodArgsType");
    private ObjectMapper objectMapper;

    @Override
    public List<Target> translate(final String config) {

        try {
            final List<Target> targets = objectMapper.readValue(config,
                    new TypeReference<List<Target>>() { });

            targets.stream().forEach(target -> {
                    // translate methodArgs
                    final List<Object> methodArgs = target.getMethodArgs();
                    final List<Object> translatedMethodArgs = translateTargets(methodArgs);
                    target.setMethodArgs(translatedMethodArgs);

                // translate
                final Object clazzInstance = target.getClazzInstance();
                final Object translatedClazzInstance = translateTarget(clazzInstance);
                target.setClazzInstance(translatedClazzInstance);
                }
            );
            return targets;
        } catch (final IOException e) {
            throw new TargetConfigTranslationException(e);
        }
    }

    private List<Object> translateTargets(final List<Object> args) {
        return args.stream()
                .map(this::translateTarget)
                .collect(Collectors.toList());
    }

    private Object translateTarget(Object arg) {
        if (!(arg instanceof Map)) {
            return arg;
        }
        final Map<String, Object> map = (Map<String, Object>) arg;
        if (isTarget(map)) {
            final Target target = new Target();
            target.setClazz((String) map.get("clazz"));
            target.setMethod((String) map.get("method"));
            target.setMethodArgs(translateTargets((List<Object>) map.get("methodArgs")));
            target.setMethodArgsType((List<String>) map.get("methodArgsType"));
            if (map.containsKey("clazzInstance") && null != map.get("clazzInstance")) {
                target.setClazzInstance(translateTarget(map.get("clazzInstance")));
            }
            return target;
        }

        return arg;
    }

    private Boolean isTarget(final Map<String, Object> arg) {
        return TARGET_FIELDS.containsAll(arg.keySet());
    }
}
