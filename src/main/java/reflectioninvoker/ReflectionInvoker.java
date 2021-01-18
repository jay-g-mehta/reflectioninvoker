package reflectioninvoker;

import reflectioninvoker.targets.Target;

/**
 * Generic interface which invokes method on Target
 *
 * Invoker should take care of constructing clazzInstance if Target.clazzInstance is of type Target
 *
 * Invoker should take care of calling invoke() for every arg in methodArgs if arg is of type Target.
 * This is useful for chaining.
 *
 */
public interface ReflectionInvoker {
    /**
     * Using Reflection, call method on target and return response
     * @param target to invoke
     * @return response from target's method invoked
     */
    Object invoke(final Target target);
}
