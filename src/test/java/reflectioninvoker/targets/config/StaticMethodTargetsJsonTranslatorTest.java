package reflectioninvoker.targets.config;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.google.common.collect.ImmutableList;
import org.junit.jupiter.api.Test;
import reflectioninvoker.targets.Target;
import reflectioninvoker.testutils.TestResourceReader;

import java.io.IOException;
import java.util.List;

import static org.junit.jupiter.api.Assertions.*;


class StaticMethodTargetsJsonTranslatorTest {
    TestResourceReader testResourceReader = new TestResourceReader();
    ObjectMapper objectMapper = new ObjectMapper();
    StaticMethodTargetsJsonTranslator staticMethodTargetsJsonTranslator = new StaticMethodTargetsJsonTranslator(
            objectMapper);

    @Test
    void should_translate() throws IOException {
        String json = testResourceReader.readTestResource("SampleStaticMethodTargetsConfig.json");
        List<Target> targets = staticMethodTargetsJsonTranslator.translate(json);
        assertEquals(Integer.valueOf(1), targets.size());
        assertEquals("org.apache.commons.lang3.ArrayUtils", targets.get(0).getClazz());
        assertEquals("contains", targets.get(0).getMethod());
        assertEquals(ImmutableList.of(ImmutableList.of("Mumbai", "Paris", "Tokyo"), "Helsinki"),
                targets.get(0).getMethodArgs());
        assertEquals(ImmutableList.of("[Ljava.lang.Object;", "java.lang.Object"),
                targets.get(0).getMethodArgsType());
        assertNull(targets.get(0).getClazzInstance());
    }
}