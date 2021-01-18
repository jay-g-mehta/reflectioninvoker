package reflectioninvoker;


/**
 * General purpose reflection invoker to invoke:
 * i. static method defined using Target
 * ii. class instance method defined using Target
 * iii. class constructor defined using Target
 *
 * Invoker takes care of constructing clazzInstance if Target.clazzInstance is of type Target
 *
 * Invoker also takes care of recursively calling itself for every arg in methodArgs if
 * arg is of type Target. This is useful for chaining.
 */
public class Invoker extends AbstractInvoker implements ReflectionInvoker {

}
