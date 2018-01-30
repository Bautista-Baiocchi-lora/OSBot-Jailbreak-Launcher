package org.osbot.jailbreak.utils.reflection;

import java.lang.reflect.Constructor;
import java.lang.reflect.InvocationTargetException;

public class ReflectedConstructor {

    private final Constructor<?> constructor;

    public ReflectedConstructor(Constructor<?> constructor) {
        this.constructor = constructor;
    }

    public boolean isAccessible() {
        return constructor.isAccessible();
    }

    public boolean isVarArgs() {
        return constructor.isVarArgs();
    }

    public boolean isSynthetic() {
        return constructor.isSynthetic();
    }

    public Class<?> getDeclaringClass() {
        return constructor.getDeclaringClass();
    }

    public int getParameterCount() {
        return constructor.getParameterCount();
    }

    public Constructor<?> getRepresentedConstructor() {
        return constructor;
    }

    public Class<?>[] getParameterTypes() {
        return constructor.getParameterTypes();
    }

    public ReflectedClass getNewInstance()
            throws InstantiationException, IllegalAccessException, IllegalArgumentException, InvocationTargetException {
        return getNewInstance(new Object[]{});
    }

    public ReflectedClass getNewInstance(Object... parameters)
            throws InstantiationException, IllegalAccessException, IllegalArgumentException, InvocationTargetException {
        if (!isAccessible()) {
            constructor.setAccessible(true);
        }
        return new ReflectedClass(constructor.newInstance(parameters));
    }

    public String getName() {
        return constructor.getName();
    }

}
