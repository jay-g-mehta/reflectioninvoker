package reflectioninvoker;

import reflectioninvoker.targets.Target;

/**
 * Generic interface which invokes method on Target
 */
public interface ReflectionInvoker {
    /**
     * Using Reflection, call method on target and return response
     * @param target
     * @return
     */
    Object invoke(final Target target);
}
