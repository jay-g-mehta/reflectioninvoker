package reflectioninvoker;

import java.lang.reflect.InvocationTargetException;
import java.lang.reflect.Method;
import java.lang.reflect.Modifier;


/**
 * Invokes static methods of Target class using Reflection
 */
public class StaticMethodReflectionInvoker extends AbstractInvoker implements ReflectionInvoker {

    @Override
    protected Object invokeMethod(final Method targetMethod, final Object clazzInstance, final Object[] methodArgsArray)
            throws InvocationTargetException, IllegalAccessException {
        if (!Modifier.isStatic(targetMethod.getModifiers())) {
            final String message = "Invoking non static method is not supported";
            throw new UnsupportedOperationException(message);
        }

        return super.invokeMethod(targetMethod, clazzInstance, methodArgsArray);
    }
}
