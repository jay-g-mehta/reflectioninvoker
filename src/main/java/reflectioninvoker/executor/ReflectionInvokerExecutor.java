package reflectioninvoker.executor;

import reflectioninvoker.targets.TargetsProvider;

import java.util.List;

/**
 * Generic interface for reflection invoker executors
 */
public interface ReflectionInvokerExecutor {

    List<Object> execute(final TargetsProvider targetsProvider);
}
