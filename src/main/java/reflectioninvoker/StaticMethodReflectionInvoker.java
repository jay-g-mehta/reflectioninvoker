package reflectioninvoker;

import reflectioninvoker.exception.ReflectionInvokeException;
import reflectioninvoker.targets.Target;

import java.lang.reflect.Array;
import java.lang.reflect.InvocationTargetException;
import java.lang.reflect.Method;
import java.lang.reflect.Modifier;
import java.util.Collection;
import java.util.Iterator;
import java.util.List;

/**
 * Invokes static methods of Target class using Reflection
 */
public class StaticMethodReflectionInvoker implements ReflectionInvoker{
    @Override
    public Object invoke(final Target target) {
        return invokeStaticMethods(target.getClazz(), target.getMethod(),
                target.getMethodArgs(), target.getMethodArgsType());
    }

    private Object invokeStaticMethods(final String clazz,
                                      final String method,
                                      final List<Object> methodArgs,
                                      final List<String> methodArgsType) {
        try {
            final Class[] methodArgsTypeClassArray = new Class[methodArgsType.size()];
            final Object[] methodArgsArray = new Object[methodArgs.size()];

            for (int i = 0; i < methodArgsType.size(); i++) {
                final Class<?> aClass = Class.forName(methodArgsType.get(i));
                methodArgsTypeClassArray[i] = aClass;
                if (aClass.isArray()) {
                    methodArgsArray[i] = shallowCastToArray(methodArgs.get(i), aClass);
                } else {
                    methodArgsArray[i] = methodArgs.get(i);
                }
            }

            final Class<?> targetProducerClass = Class.forName(clazz);
            final Method targetMethod = targetProducerClass.getMethod(method, methodArgsTypeClassArray);
            if (!Modifier.isStatic(targetMethod.getModifiers())) {
                final String message = "Invoking non static method are currently not supported";
                throw new UnsupportedOperationException(message);
            }
            final Object result = targetMethod.invoke(null, methodArgsArray);
            return result;
        } catch (final ClassNotFoundException | NoSuchMethodException
                | InvocationTargetException | IllegalAccessException exception) {
            throw new ReflectionInvokeException(exception);
        }
    }

    private Object shallowCastToArray(final Object arg, final Class<?> aClass) {
        if (arg instanceof Collection) {
            final Class<?> typeOfObjectsInCollection = aClass.getComponentType();
            final Object newArray = Array.newInstance(typeOfObjectsInCollection, ((Collection<?>) arg).size());
            Integer index = 0;
            for (Iterator i = ((Collection<?>) arg).iterator(); i.hasNext(); ) {
                Array.set(newArray, index, i.next());
                index = index + 1;
            }
            return newArray;
        }
        return arg;
    }
}
