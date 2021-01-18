package reflectioninvoker;


import com.fasterxml.jackson.databind.ObjectMapper;
import com.google.common.collect.ImmutableList;
import org.junit.jupiter.api.Test;
import reflectioninvoker.executor.SerialSynchronousReflectionInvokerExecutor;
import reflectioninvoker.targets.ConfigTargetsProvider;
import reflectioninvoker.targets.Target;
import reflectioninvoker.targets.config.FileSystemConfigReader;
import reflectioninvoker.targets.config.TargetsJsonTranslator;

import java.util.List;

import static org.junit.jupiter.api.Assertions.assertFalse;
import static reflectioninvoker.testutils.TestResourceReader.getTestResourceRootAbsolutePath;


public class StaticMethodReflectionInvokerIntegrationTest {

    @Test
    public void test_should_invoke_using_StaticMethodReflectionInvoker() {
        StaticMethodReflectionInvoker staticMethodReflectionInvoker = new StaticMethodReflectionInvoker();
        Target target = new Target();
        target.setClazz("org.apache.commons.lang3.ArrayUtils");
        target.setMethod("contains");
        target.setMethodArgs(ImmutableList.of(ImmutableList.of("Mumbai", "Paris", "Tokyo"), "Helsinki"));
        target.setMethodArgsType(ImmutableList.of("[Ljava.lang.Object;", "java.lang.Object"));

        Object actual = staticMethodReflectionInvoker.invoke(target);
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

        StaticMethodReflectionInvoker staticMethodReflectionInvoker = new StaticMethodReflectionInvoker();
        SerialSynchronousReflectionInvokerExecutor serialSynchronousReflectionInvokerExecutor =
                new SerialSynchronousReflectionInvokerExecutor(staticMethodReflectionInvoker);
        List<Object> actual = serialSynchronousReflectionInvokerExecutor.execute(configTargetsProvider);

        assertFalse((Boolean) actual.get(0));
    }
}
