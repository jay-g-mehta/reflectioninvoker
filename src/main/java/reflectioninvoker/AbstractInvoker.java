package reflectioninvoker;

import org.apache.commons.lang3.ClassUtils;
import reflectioninvoker.exception.ReflectionInvokeException;
import reflectioninvoker.targets.Target;

import java.lang.reflect.Constructor;
import java.lang.reflect.InvocationTargetException;
import java.lang.reflect.Method;
import java.util.List;
import java.util.stream.Collectors;


/**
 * Abstract invoker class defining common implementation of invoke(), not be used by client
 */
abstract class AbstractInvoker implements ReflectionInvoker {

    @Override
    public Object invoke(final Target target) {
        final String clazz = target.getClazz();
        final String method = target.getMethod();
        final List<Object> methodArgs = target.getMethodArgs();
        final List<String> methodArgsType = target.getMethodArgsType();

        try {
            final List<Object> invokedMethodArgs = methodArgsTargetToObjectConverter(methodArgs);
            final Class[] methodArgsTypeClassArray = ReflectionInvokerUtils.typeToClassConversion(methodArgsType);
            final Object[] methodArgsArray = ReflectionInvokerUtils.methodArgsCollectionToArrayConversion(
                    invokedMethodArgs, methodArgsType);
            final Class<?> targetProducerClass = ClassUtils.getClass(clazz);

            // i. Invoke if constructor
            if (isConstructor(targetProducerClass, method)) {
                return invokeConstructor(targetProducerClass, methodArgsTypeClassArray, methodArgsArray);
            }

            // ii. Invoke if static method or class instance method
            final Method targetMethod = targetProducerClass.getMethod(method, methodArgsTypeClassArray);
            final Object clazzInstance = constructClazzInstance(target.getClazzInstance());
            return invokeMethod(targetMethod, clazzInstance, methodArgsArray);

        } catch (final ClassNotFoundException | NoSuchMethodException | InvocationTargetException
                | IllegalAccessException | InstantiationException exception) {
            throw new ReflectionInvokeException(exception);
        }
    }


    /**
     * Default implementation of calling constructor of a class. Child classes can override default implementation.
     * @param targetProducerClass class whose instance is to be created
     * @param methodArgsTypeClassArray Class objects used to refer constructor to be invoked
     * @param methodArgsArray arguments passed while invoking constructor
     * @return Object/instance of the class
     */
    protected Object invokeConstructor(final Class<?> targetProducerClass, final Class[] methodArgsTypeClassArray,
                                       final Object[] methodArgsArray)
            throws NoSuchMethodException, IllegalAccessException, InvocationTargetException, InstantiationException {
        final Constructor constructor = targetProducerClass.getConstructor(methodArgsTypeClassArray);
        final Object result = constructor.newInstance(methodArgsArray);
        return result;
    }


    /**
     * Default implementation of calling method of a class. Child classes can override default implementation.
     * @param targetMethod method to be invoked
     * @param clazzInstance Object on which targetMethod will be called upon. Can be null for static method.
     * @param methodArgsArray arguments passed while invoking targetMethod
     * @return return value of targetMethod when invoked
     */
    protected Object invokeMethod(final Method targetMethod, final Object clazzInstance, final Object[] methodArgsArray)
            throws InvocationTargetException, IllegalAccessException {
        return targetMethod.invoke(clazzInstance, methodArgsArray);

    }


    /**
     * Default implementation of class instance construction
     * @param object class instance. If not null, this implementation returns it back. If it is of type Target, calls
     *               invoke() and returns its response
     * @return instance of class.
     */
    protected Object constructClazzInstance(final Object object) {
        if (null == object) {
            // This is possible if method to invoke is static or constructor
            return null;
        }

        if (object instanceof Target) {
            return invoke((Target) object);
        }

        // return the object itself it was already constructed
        return object;
    }


    /**
     * Default implementation to invoke() for each arg of method to be invoked, if arg is Target type.
     * @param methodArgs args of method to be invoked
     * @return List of method arg or result of invoke() if its of Target type
     */
    protected List<Object> methodArgsTargetToObjectConverter(final List<Object> methodArgs) {
        final List<Object> converted = methodArgs.stream().map(arg -> {
            if (arg instanceof Target) {
                return invoke((Target) arg);
            }
            return arg;
        }).collect(Collectors.toList());

        return converted;
    }

    private Boolean isConstructor(final Class<?> clazz, final String method) {
        return ClassUtils.getSimpleName(clazz).equals(method);
    }
}
