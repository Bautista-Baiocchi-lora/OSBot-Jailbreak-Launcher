package org.osbot.jailbreak.utils.reflection;

import java.lang.reflect.InvocationTargetException;
import java.lang.reflect.Method;
import java.lang.reflect.Modifier;

public class ReflectedMethod {
	private final Method method;
	private final Object instance;

	public ReflectedMethod(Method method, Object instance) {
		this.method = method;
		this.instance = instance;
	}

	public ReflectedMethod(Method method) {
		this(method, null);
	}

	public Object invoke() throws IllegalAccessException, IllegalArgumentException, InvocationTargetException {
		return invoke(new Object[] {});
	}

	public Object invoke(Object... parameters)
			throws IllegalAccessException, IllegalArgumentException, InvocationTargetException {
		if (!isStatic() && instance == null) {
			throw new IllegalStateException("Can not invoke non static method without an instance.");
		}
		if (!isAccessible()) {
			method.setAccessible(true);
		}
		try {
			return method.invoke(instance, parameters);
		} catch (Throwable t) {
			t.printStackTrace();
		}
		return null;
	}

	public int getParameterCount() {
		return method.getParameterCount();
	}

	public boolean isStatic() {
		return Modifier.isStatic(method.getModifiers());
	}

	public boolean isAccessible() {
		return method.isAccessible();
	}

	public boolean isSynthetic() {
		return method.isSynthetic();
	}

	public boolean isBridge() {
		return method.isBridge();
	}

	public Class<?> getReturnType() {
		return method.getReturnType();
	}

	public boolean isDefault() {
		return method.isDefault();
	}

	public boolean isVarArgs() {
		return method.isVarArgs();
	}

	public Method getRepresentedMethod() {
		return method;
	}

	public String getName() {
		return method.getName();
	}

}
