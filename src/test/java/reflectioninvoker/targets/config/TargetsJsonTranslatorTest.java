package reflectioninvoker.targets.config;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.google.common.collect.ImmutableList;
import org.junit.jupiter.api.Test;
import reflectioninvoker.targets.Target;
import reflectioninvoker.testutils.TestResourceReader;

import java.io.IOException;
import java.util.List;

import static org.junit.jupiter.api.Assertions.*;


class TargetsJsonTranslatorTest {
    TestResourceReader testResourceReader = new TestResourceReader();
    ObjectMapper objectMapper = new ObjectMapper();
    TargetsJsonTranslator targetsJsonTranslator = new TargetsJsonTranslator(objectMapper);

    @Test
    void should_translate() throws IOException {
        String json = testResourceReader.readTestResource("SampleStaticMethodTargetsConfig.json");
        List<Target> targets = targetsJsonTranslator.translate(json);
        assertEquals(Integer.valueOf(1), targets.size());
        assertEquals("org.apache.commons.lang3.ArrayUtils", targets.get(0).getClazz());
        assertEquals("contains", targets.get(0).getMethod());
        assertEquals(ImmutableList.of(ImmutableList.of("Mumbai", "Paris", "Tokyo"), "Helsinki"),
                targets.get(0).getMethodArgs());
        assertEquals(ImmutableList.of("[Ljava.lang.Object;", "java.lang.Object"),
                targets.get(0).getMethodArgsType());
        assertNull(targets.get(0).getClazzInstance());
    }

    @Test
    void should_translate_with_clazzInstance() throws IOException {
        String json = testResourceReader.readTestResource("SampleTargetConfig.json");
        List<Target> targets = targetsJsonTranslator.translate(json);
        assertEquals(Integer.valueOf(1), targets.size());
        assertEquals("java.util.Date", targets.get(0).getClazz());
        assertEquals("toString", targets.get(0).getMethod());
        assertEquals(ImmutableList.of(), targets.get(0).getMethodArgs());
        assertEquals(ImmutableList.of(), targets.get(0).getMethodArgsType());

        assertNotNull(targets.get(0).getClazzInstance());
        assertTrue(targets.get(0).getClazzInstance() instanceof Target);
        Target clazzInstance = (Target) targets.get(0).getClazzInstance();

        assertEquals("java.util.Date", clazzInstance.getClazz());
        assertEquals("Date", clazzInstance.getMethod());
        assertEquals(ImmutableList.of(1610996650749L), clazzInstance.getMethodArgs());
        assertEquals(ImmutableList.of("long"), clazzInstance.getMethodArgsType());
    }

    @Test
    void should_translate_clazzInstance_passed_as_methodArg() throws IOException {
        String json = testResourceReader.readTestResource("TargetAsMethodArg.json");
        List<Target> targets = targetsJsonTranslator.translate(json);
        assertEquals(Integer.valueOf(1), targets.size());
        assertEquals("java.util.Date", targets.get(0).getClazz());
        assertEquals("equals", targets.get(0).getMethod());
        assertEquals(ImmutableList.of("java.lang.Object"), targets.get(0).getMethodArgsType());

        assertTrue(targets.get(0).getMethodArgs().get(0) instanceof Target);
        Target firstMethodArg = (Target) targets.get(0).getMethodArgs().get(0);
        assertEquals("java.util.Date", firstMethodArg.getClazz());
        assertEquals("Date", firstMethodArg.getMethod());
        assertEquals(ImmutableList.of(1610996650749L), firstMethodArg.getMethodArgs());
        assertEquals(ImmutableList.of("long"), firstMethodArg.getMethodArgsType());

        assertTrue(targets.get(0).getClazzInstance() instanceof Target);
        Target clazzInstance = (Target) targets.get(0).getClazzInstance();
        assertEquals("java.util.Date", clazzInstance.getClazz());
        assertEquals("Date", clazzInstance.getMethod());
        assertEquals(ImmutableList.of(1610996650750L), clazzInstance.getMethodArgs());
        assertEquals(ImmutableList.of("long"), clazzInstance.getMethodArgsType());
    }
}