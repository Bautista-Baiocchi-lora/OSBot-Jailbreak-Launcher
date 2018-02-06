package org.osbot.jailbreak.utils.reflection;

import java.lang.annotation.Annotation;
import java.lang.reflect.Constructor;
import java.lang.reflect.Field;
import java.lang.reflect.Method;
import java.lang.reflect.Modifier;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;
import java.util.Map.Entry;

public class ReflectedClass {
	private final Class<?> clazz;
	private Object instance;

	public ReflectedClass(Class<?> clazz) {
		this(clazz, null);
	}

	public ReflectedClass(Object instance) {
		this(instance.getClass(), instance);
	}

	public ReflectedClass(Class<?> clazz, Object instance) {
		this.clazz = clazz;
		this.instance = instance;
	}

	public String getName() {
		return clazz.getName();
	}

	public Object getInstance() {
		return instance;
	}

	public void setInstance(Object instance) {
		if (!clazz.isInstance(instance)) {
			throw new IllegalArgumentException(instance + " is not an instance of the class " + clazz);
		}
		this.instance = instance;
	}

	public ReflectedClass getSuperClass() {
		return new ReflectedClass(clazz.getSuperclass(), instance);
	}

	public String getSimpleName() {
		return clazz.getSimpleName();
	}

	public boolean isInstance(Object instance) {
		return clazz.isInstance(instance);
	}

	public Annotation[] getAnnotations() {
		return clazz.getAnnotations();
	}

	public boolean isInterface() {
		return clazz.isInterface();
	}

	public boolean isEnum() {
		return clazz.isEnum();
	}

	public Class<?> getRespresentedClass() {
		return clazz;
	}

	public Object getNewInstance() {
		if (clazz == null) {
			throw new NullPointerException("clazz == null!");
		}
		try {
			return clazz.newInstance();
		} catch (InstantiationException | IllegalAccessException e) {
			e.printStackTrace();
		}
		return null;
	}

	public ReflectedField[] getFields() {
		final ArrayList<ReflectedField> fields = new ArrayList<ReflectedField>();
		for (Field field : clazz.getDeclaredFields()) {
			if (Modifier.isStatic(field.getModifiers())) {
				fields.add(new ReflectedField(field, instance));
			}
		}
		if (instance != null) {
			for (Field field : clazz.getDeclaredFields()) {
				if (Modifier.isStatic(field.getModifiers())) {
					fields.add(new ReflectedField(field, instance));
				}
			}
		}
		return fields.toArray(new ReflectedField[fields.size()]);
	}

	public ReflectedField getField(Modifiers modifiers) {
		Fields:
		for (Field field : clazz.getDeclaredFields()) {
			for (Entry<Modifiers.Condition, ? extends Object> modifier : modifiers.getModifiers().entrySet()) {
				switch (modifier.getKey()) {
					case NAME:
						if (!field.getName().equals(modifier.getValue())) {
							continue Fields;
						}
						break;
					case STATIC:
						if (Modifier.isStatic(field.getModifiers()) != (boolean) ((Object) modifier.getValue())) {
							continue Fields;
						}
						break;
					case ABSTRACT:
						if (Modifier.isAbstract(field.getModifiers()) != (boolean) ((Object) modifier.getValue())) {
							continue Fields;
						}
						break;
					case TYPE:
						if (field.getType().equals((Class<?>) modifier.getValue())) {
							continue Fields;
						}
						break;
					case FINAL:
						if (Modifier.isFinal(field.getModifiers()) != (boolean) ((Object) modifier.getValue())) {
							continue Fields;
						}
						break;
					case VOLATILE:
						if (Modifier.isVolatile(field.getModifiers()) != (boolean) ((Object) modifier.getValue())) {
							continue Fields;
						}
						break;
					case PUBLIC:
						if (Modifier.isPublic(field.getModifiers()) != (boolean) ((Object) modifier.getValue())) {
							continue Fields;
						}
						break;
					case PRIVATE:
						if (Modifier.isPrivate(field.getModifiers()) != (boolean) ((Object) modifier.getValue())) {
							continue Fields;
						}
						break;
					case PROTECTED:
						if (Modifier.isProtected(field.getModifiers()) != (boolean) ((Object) modifier.getValue())) {
							continue Fields;
						}
						break;
					default:
						break;
				}
			}
			if (Modifier.isStatic(field.getModifiers())) {
				return new ReflectedField(field, instance);
			}
			return new ReflectedField(field, instance);
		}
		return null;
	}

	public ReflectedConstructor getConstructor(Modifiers modifiers) {
		Constructors:
		for (Constructor<?> constructor : clazz.getConstructors()) {
			for (Entry<Modifiers.Condition, ? extends Object> modifier : modifiers.getModifiers().entrySet()) {
				switch (modifier.getKey()) {
					case PARAMETER_COUNT:
						if (constructor.getParameterCount() != (int) ((Object) modifier.getValue())) {
							continue Constructors;
						}
						break;
					case PARAMETER_TYPES:
						List<Class<?>> parameterClasses = Arrays.asList(constructor.getParameterTypes());
						for (Class<?> parameter : (Class<?>[]) modifier.getValue()) {
							if (!parameterClasses.contains(parameter)) {
								continue Constructors;
							}
						}
						break;
					default:
						break;
				}
			}
			return new ReflectedConstructor(constructor);
		}
		return null;
	}

	public ReflectedMethod getMethod(Modifiers modifiers) {
		Methods:
		for (Method method : clazz.getDeclaredMethods()) {
			for (Entry<Modifiers.Condition, ? extends Object> modifier : modifiers.getModifiers().entrySet()) {
				switch (modifier.getKey()) {
					case NAME:
						if (!method.getName().equals(modifier.getValue())) {
							continue Methods;
						}
						break;
					case STATIC:
						if (Modifier.isStatic(method.getModifiers()) != (boolean) ((Object) modifier.getValue())) {
							continue Methods;
						}
						break;
					case ABSTRACT:

						if (Modifier.isAbstract(method.getModifiers()) != (boolean) ((Object) modifier.getValue())) {
							continue Methods;
						}
						break;
					case FINAL:

						if (Modifier.isFinal(method.getModifiers()) != (boolean) ((Object) modifier.getValue())) {
							continue Methods;
						}
						break;
					case VOLATILE:

						if (Modifier.isVolatile(method.getModifiers()) != (boolean) ((Object) modifier.getValue())) {
							continue Methods;
						}
						break;
					case PUBLIC:

						if (Modifier.isPublic(method.getModifiers()) != (boolean) ((Object) modifier.getValue())) {
							continue Methods;
						}
						break;
					case PRIVATE:
						if (Modifier.isPrivate(method.getModifiers()) != (boolean) ((Object) modifier.getValue())) {
							continue Methods;
						}
						break;
					case PROTECTED:

						if (Modifier.isProtected(method.getModifiers()) != (boolean) ((Object) modifier.getValue())) {
							continue Methods;
						}
						break;
					case PARAMETER_TYPES:
						List<Class<?>> parameterClasses = Arrays.asList(method.getParameterTypes());
						for (Class<?> parameter : (Class<?>[]) modifier.getValue()) {
							if (!parameterClasses.contains(parameter)) {
								continue Methods;
							}
						}
						break;
					case PARAMETER_COUNT:
						if (method.getParameterCount() != (int) ((Object) modifier.getValue())) {
							continue;
						}
						break;
					case RETURN_TYPE:
						if (!method.getReturnType().equals(modifier.getValue())) {
							continue Methods;
						}
						break;
					default:
						break;
				}
			}
			if (Modifier.isStatic(method.getModifiers())) {
				return new ReflectedMethod(method, instance);
			}
			return new ReflectedMethod(method, instance);
		}
		return null;
	}

	public ReflectedMethod[] getMethods() {
		final ArrayList<ReflectedMethod> methods = new ArrayList<ReflectedMethod>();
		for (Method method : clazz.getDeclaredMethods()) {
			if (!Modifier.isStatic(method.getModifiers())) {
				methods.add(new ReflectedMethod(method, instance));
			}
		}
		if (instance == null) {
			for (Method method : clazz.getDeclaredMethods()) {
				if (Modifier.isStatic(method.getModifiers())) {
					methods.add(new ReflectedMethod(method));
				}
			}
		}
		return methods.toArray(new ReflectedMethod[methods.size()]);
	}

	public ReflectedConstructor[] getConstructors() {
		final ArrayList<ReflectedConstructor> constructors = new ArrayList<ReflectedConstructor>();
		for (Constructor<?> constructor : clazz.getDeclaredConstructors()) {
			constructors.add(new ReflectedConstructor(constructor));
		}
		return constructors.toArray(new ReflectedConstructor[constructors.size()]);
	}
}
