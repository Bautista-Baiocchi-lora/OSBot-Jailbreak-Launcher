package org.osbot.jailbreak.utils.reflection;

import java.lang.reflect.Field;
import java.lang.reflect.Modifier;

public class ReflectedField {

    private final Field field;
    private final Object instance;

    public ReflectedField(Field field) {
        this(field, null);
    }

    public ReflectedField(Field field, Object instance) {
        this.field = field;
        this.instance = instance;
    }

    public Object getValue() throws IllegalArgumentException, IllegalAccessException {
        if (!isStatic() && instance == null) {
            throw new IllegalStateException("Can not get static field value without instance");
        }
        if (!isAccessible()) {
            field.setAccessible(true);
        }
        return field.get(instance);
    }

    public void setValue(Object value) throws IllegalArgumentException, IllegalAccessException {
        if (!isStatic() && instance == null) {
            throw new IllegalStateException("Can not set non-static field value without instance");
        }
        if (!isAccessible()) {
            field.setAccessible(true);
        }
        field.set(instance, value);
    }

    public int getAsInt() throws IllegalArgumentException, IllegalAccessException {
        return (int) getValue();
    }

    public short getAsShort() throws IllegalArgumentException, IllegalAccessException {
        return (short) getValue();
    }

    public boolean getAsBoolean() throws IllegalArgumentException, IllegalAccessException {
        return (boolean) getValue();
    }

    public long getAsLong() throws IllegalArgumentException, IllegalAccessException {
        return (long) getValue();
    }

    public Double getAsDouble() throws IllegalArgumentException, IllegalAccessException {
        return (double) getValue();
    }

    public Float getAsFloat() throws IllegalArgumentException, IllegalAccessException {
        return (float) getValue();
    }

    public Byte getAsByte() throws IllegalArgumentException, IllegalAccessException {
        return (Byte) getValue();
    }

    public char getAsChar() throws IllegalArgumentException, IllegalAccessException {
        return (char) getValue();
    }

    public String getAsString() throws IllegalArgumentException, IllegalAccessException {
        return (String) getValue();
    }

    public boolean isAccessible() {
        return field.isAccessible();
    }

    public boolean isStatic() {
        return Modifier.isStatic(field.getModifiers());
    }

    public boolean isSynthetic() {
        return field.isSynthetic();
    }

    public boolean isEnumConstant() {
        return field.isEnumConstant();
    }

    public Class<?> getType() {
        return field.getType();
    }

    public Class<?> getDeclaringClass() {
        return field.getDeclaringClass();
    }

    public Field getRepresentedField() {
        return field;
    }

    public String getName() {
        return field.getName();
    }

}
