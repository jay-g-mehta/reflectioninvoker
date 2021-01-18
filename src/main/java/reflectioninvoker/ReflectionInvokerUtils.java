package reflectioninvoker;

import org.apache.commons.lang3.ClassUtils;
import reflectioninvoker.exception.ReflectionInvokeException;

import java.lang.reflect.Array;
import java.util.Collection;
import java.util.Iterator;
import java.util.List;

/**
 *
 */
class ReflectionInvokerUtils {
    private static Object shallowCastToArray(final Object arg, final Class<?> aClass) {
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

    /**
     * For each arg in methodArgs, this utility method converts Collection to Array type based on type information
     * provided in methodArgsType
     * @param methodArgs
     * @param methodArgsType
     * @return
     */
    static Object[] methodArgsCollectionToArrayConversion(final List<Object> methodArgs,
                                                          final List<String> methodArgsType) {
        try {
            final Object[] methodArgsArray = new Object[methodArgs.size()];
            for (int i = 0; i < methodArgsType.size(); i++) {
                final Class<?> aClass = ClassUtils.getClass(methodArgsType.get(i));
                if (aClass.isArray()) {
                    methodArgsArray[i] = shallowCastToArray(methodArgs.get(i), aClass);
                } else {
                    methodArgsArray[i] = methodArgs.get(i);
                }
            }
            return methodArgsArray;
        } catch (final ClassNotFoundException e) {
            throw new ReflectionInvokeException(e);
        }
    }

    /**
     * For each arg in args, converts String type to Class object
     * @param args
     * @return
     */
    static Class[] typeToClassConversion(final List<String> args) {
        final Class[] argsClassArray = new Class[args.size()];
        try {
            for (int i = 0; i < args.size(); i++) {
                final Class<?> aClass = ClassUtils.getClass(args.get(i)); // this handles primitive types as well.
                argsClassArray[i] = aClass;
            }
            return argsClassArray;
        } catch (final ClassNotFoundException e) {
            throw new ReflectionInvokeException(e);
        }
    }
}
