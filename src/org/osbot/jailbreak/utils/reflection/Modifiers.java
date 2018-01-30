package org.osbot.jailbreak.utils.reflection;

import java.util.HashMap;

public class Modifiers {

    private final HashMap<Condition, ? extends Object> modifiers;

    private Modifiers(ModifierBuilder builder) {
        this.modifiers = builder.modifiers;
    }

    public HashMap<Condition, ? extends Object> getModifiers() {
        return modifiers;
    }

    public static enum Condition {
        STATIC, ABSTRACT, NAME, PARAMETER_TYPES, PARAMETER_COUNT, RETURN_TYPE, VOLATILE, FINAL, TYPE, PUBLIC, PRIVATE, PROTECTED;
    }

    public static class ModifierBuilder {

        private final HashMap<Condition, Object> modifiers;

        public ModifierBuilder() {
            modifiers = new HashMap<Condition, Object>();
        }

        public ModifierBuilder name(String name) {
            modifiers.put(Condition.NAME, name);
            return this;
        }

        public ModifierBuilder type(Class<?> type) {
            modifiers.put(Condition.TYPE, type);
            return this;
        }

        public ModifierBuilder isStatic(boolean isStatic) {
            modifiers.put(Condition.STATIC, isStatic);
            return this;
        }

        public ModifierBuilder isAbstract(boolean isAbstract) {
            modifiers.put(Condition.ABSTRACT, isAbstract);
            return this;
        }

        public ModifierBuilder returnType(Class<?> returnType) {
            modifiers.put(Condition.RETURN_TYPE, returnType);
            return this;
        }

        public ModifierBuilder parameterTypes(Class<?>... parameterTypes) {
            modifiers.put(Condition.PARAMETER_COUNT, parameterTypes.length);
            modifiers.put(Condition.PARAMETER_TYPES, parameterTypes);
            return this;
        }

        public ModifierBuilder parameterCount(int count) {
            modifiers.put(Condition.PARAMETER_COUNT, count);
            return this;
        }

        public ModifierBuilder isVolatile(boolean isVolatile) {
            modifiers.put(Condition.VOLATILE, isVolatile);
            return this;
        }

        public ModifierBuilder isFinal(boolean isFinal) {
            modifiers.put(Condition.FINAL, isFinal);
            return this;
        }

        public Modifiers build() {
            return new Modifiers(this);
        }
    }

}