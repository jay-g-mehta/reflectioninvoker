package reflectioninvoker;

import reflectioninvoker.targets.Target;

/**
 * Generic interface which invokes method on Target
 */
public interface ReflectionInvoker {
    /**
     * Using Reflection, call method on target and return response
     * @param target to invoke
     * @return response from target's method invoked
     */
    Object invoke(final Target target);
}
