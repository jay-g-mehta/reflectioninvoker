package reflectioninvoker;


import com.google.common.collect.ImmutableList;
import org.junit.jupiter.api.Test;
import reflectioninvoker.executor.SerialSynchronousReflectionInvokerExecutor;
import reflectioninvoker.targets.BuildTargetsProvider;
import reflectioninvoker.targets.Target;
import reflectioninvoker.targets.TargetsProvider;

import java.util.List;

import static org.junit.jupiter.api.Assertions.assertFalse;


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
    public void test_should_invoke_using_SerialSynchronousReflectionInvokerExecutor() {

        StaticMethodReflectionInvoker staticMethodReflectionInvoker = new StaticMethodReflectionInvoker();
        SerialSynchronousReflectionInvokerExecutor serialSynchronousReflectionInvokerExecutor =
                new SerialSynchronousReflectionInvokerExecutor(staticMethodReflectionInvoker);

        Target targetInstance = new Target();
        targetInstance.setClazz("org.apache.commons.lang3.ArrayUtils");
        targetInstance.setMethod("contains");
        targetInstance.setMethodArgs(ImmutableList.of(ImmutableList.of("Mumbai", "Paris", "Tokyo"), "Helsinki"));
        targetInstance.setMethodArgsType(ImmutableList.of("[Ljava.lang.Object;", "java.lang.Object"));

        Target targetInstance2 = new Target();
        targetInstance2.setClazz("org.apache.commons.collections4.CollectionUtils");
        targetInstance2.setMethod("containsAny");
        targetInstance2.setMethodArgs(ImmutableList.of(ImmutableList.of("Tesla", "Einstein", "Homi Bhabha"),
                ImmutableList.of("Messi")));
        targetInstance2.setMethodArgsType(ImmutableList.of("java.util.Collection", "java.util.Collection"));

        TargetsProvider targetsProvider = BuildTargetsProvider.builder()
                .target(targetInstance)
                .target(targetInstance2)
                .build();

        List<Object> actual = serialSynchronousReflectionInvokerExecutor.execute(targetsProvider);

        assertFalse((Boolean) actual.get(0));
        assertFalse((Boolean) actual.get(1));
    }
}
