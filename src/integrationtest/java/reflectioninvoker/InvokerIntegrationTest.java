package reflectioninvoker;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.google.common.collect.ImmutableList;
import org.junit.jupiter.api.Test;
import reflectioninvoker.executor.SerialSynchronousReflectionInvokerExecutor;
import reflectioninvoker.targets.ConfigTargetsProvider;
import reflectioninvoker.targets.Target;
import reflectioninvoker.targets.config.FileSystemConfigReader;
import reflectioninvoker.targets.config.TargetsJsonTranslator;

import java.util.Date;
import java.util.List;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertFalse;
import static org.junit.jupiter.api.Assertions.assertTrue;
import static reflectioninvoker.testutils.TestResourceReader.getTestResourceRootAbsolutePath;


public class InvokerIntegrationTest {

    @Test
    public void test_should_invoke_target_constructed_using_builder() {
        ReflectionInvoker invoker = new Invoker();
        Target target = new Target();
        target.setClazz("org.apache.commons.lang3.ArrayUtils");
        target.setMethod("contains");
        target.setMethodArgs(ImmutableList.of(ImmutableList.of("Mumbai", "Paris", "Tokyo"), "Helsinki"));
        target.setMethodArgsType(ImmutableList.of("[Ljava.lang.Object;", "java.lang.Object"));

        Object actual = invoker.invoke(target);
        assertFalse((Boolean) actual);
    }

    @Test
    public void test_should_invoke_targets_defined_using_config() {
        String config = getTestResourceRootAbsolutePath() + "/SampleStaticMethodTargetsConfig.json";
        ConfigTargetsProvider configTargetsProvider = new ConfigTargetsProvider(
                new FileSystemConfigReader("UTF-8", config),
                new TargetsJsonTranslator(new ObjectMapper())
        );
        configTargetsProvider.build();

        ReflectionInvoker invoker = new Invoker();
        SerialSynchronousReflectionInvokerExecutor serialSynchronousReflectionInvokerExecutor =
                new SerialSynchronousReflectionInvokerExecutor(invoker);
        List<Object> actual = serialSynchronousReflectionInvokerExecutor.execute(configTargetsProvider);

        assertFalse((Boolean) actual.get(0));
    }

    @Test
    public void test_should_invoke_constructor() {

        ReflectionInvoker invoker = new Invoker();
        Target target = new Target();
        target.setClazz("java.util.Date");
        target.setMethod("Date");
        target.setMethodArgs(ImmutableList.of());
        target.setMethodArgsType(ImmutableList.of());

        Object actual = invoker.invoke(target);
        assertTrue(actual instanceof Date);
    }

    @Test
    public void test_should_invoke_parameterized_constructor_with_primitive_type_args() {

        Date expected = new Date(1610996650749L);

        ReflectionInvoker invoker = new Invoker();
        Target target = new Target();
        target.setClazz("java.util.Date");
        target.setMethod("Date");
        target.setMethodArgs(ImmutableList.of(1610996650749L));
        target.setMethodArgsType(ImmutableList.of("long"));

        Object actual = invoker.invoke(target);
        assertTrue(actual instanceof Date);
        assertEquals(expected, actual);
    }

    @Test
    public void test_should_invoke_nested_targets_defined_using_config() {
        String config = getTestResourceRootAbsolutePath() + "/TargetAsMethodArg.json";
        ConfigTargetsProvider configTargetsProvider = new ConfigTargetsProvider(
                new FileSystemConfigReader("UTF-8", config),
                new TargetsJsonTranslator(new ObjectMapper())
        );
        configTargetsProvider.build();

        ReflectionInvoker invoker = new Invoker();
        SerialSynchronousReflectionInvokerExecutor serialSynchronousReflectionInvokerExecutor =
                new SerialSynchronousReflectionInvokerExecutor(invoker);
        List<Object> actual = serialSynchronousReflectionInvokerExecutor.execute(configTargetsProvider);

        assertFalse((Boolean) actual.get(0));
    }

    @Test
    public void test_should_invoke_instance_method_for_passed_class_instance() {
        Date date = new Date(1610996650749L);

        ReflectionInvoker invoker = new Invoker();
        Target target = new Target();
        target.setClazz("java.util.Date");
        target.setMethod("toString");
        target.setMethodArgs(ImmutableList.of());
        target.setMethodArgsType(ImmutableList.of());
        target.setClazzInstance(date);

        String actual = (String) invoker.invoke(target);
        assertEquals(date.toString(), actual);
    }

    @Test
    public void test_should_invoke_instance_method_when_instance_is_Target_type() {
        Date date = new Date(1610996650749L);

        Target dateInstance = new Target();
        dateInstance.setClazz("java.util.Date");
        dateInstance.setMethod("Date");
        dateInstance.setMethodArgs(ImmutableList.of(1610996650749L));
        dateInstance.setMethodArgsType(ImmutableList.of("long"));

        ReflectionInvoker invoker = new Invoker();
        Target target = new Target();
        target.setClazz("java.util.Date");
        target.setMethod("toString");
        target.setMethodArgs(ImmutableList.of());
        target.setMethodArgsType(ImmutableList.of());
        target.setClazzInstance(dateInstance);

        String actual = (String) invoker.invoke(target);
        assertEquals(date.toString(), actual);
    }
}
