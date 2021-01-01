package reflectioninvoker.executor;

import lombok.AllArgsConstructor;
import reflectioninvoker.ReflectionInvoker;
import reflectioninvoker.targets.TargetsProvider;

import java.util.List;
import java.util.stream.Collectors;

/**
 * This executor will serially invoke 1 target and wait for its completion before invoking another.
 */
@AllArgsConstructor
public class SerialSynchronousReflectionInvokerExecutor implements ReflectionInvokerExecutor{
    private final ReflectionInvoker invoker;

    @Override
    public List<Object> execute(final TargetsProvider targetsProvider) {
        final List<Object> targetResponse = targetsProvider.get().stream()
                .map(invoker::invoke)
                .collect(Collectors.toList());
        return targetResponse;
    }
}
