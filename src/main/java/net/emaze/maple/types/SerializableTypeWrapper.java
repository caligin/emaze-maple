package net.emaze.maple.types;

import java.io.Serializable;
import java.lang.reflect.Field;
import java.lang.reflect.GenericArrayType;
import java.lang.reflect.InvocationHandler;
import java.lang.reflect.InvocationTargetException;
import java.lang.reflect.Method;
import java.lang.reflect.ParameterizedType;
import java.lang.reflect.Proxy;
import java.lang.reflect.Type;
import java.lang.reflect.TypeVariable;
import java.lang.reflect.WildcardType;

import org.springframework.util.Assert;
import org.springframework.util.ReflectionUtils;

abstract class SerializableTypeWrapper {

    private static final Class<?>[] SUPPORTED_SERIALIZABLE_TYPES = {
        GenericArrayType.class, ParameterizedType.class, TypeVariable.class, WildcardType.class};

    private static final Method EQUALS_METHOD = ReflectionUtils.findMethod(Object.class,
            "equals", Object.class);

    private static final Method GET_TYPE_PROVIDER_METHOD = ReflectionUtils.findMethod(
            SerializableTypeProxy.class, "getTypeProvider");

    private static final ConcurrentReferenceHashMap<Type, Type> cache
            = new ConcurrentReferenceHashMap<Type, Type>(256);

    /**
     * Return a {@link Serializable} variant of {@link Field#getGenericType()}.
     */
    public static Type forField(Field field) {
        Assert.notNull(field, "Field must not be null");
        return forTypeProvider(new FieldTypeProvider(field));
    }

    /**
     * Return a {@link Serializable} variant of
     * {@link MethodParameter#getGenericParameterType()}.
     */
    public static Type forMethodParameter(MethodParameter methodParameter) {
        return forTypeProvider(new MethodParameterTypeProvider(methodParameter));
    }

    /**
     * Return a {@link Serializable} variant of
     * {@link Class#getGenericSuperclass()}.
     */
    @SuppressWarnings("serial")
    public static Type forGenericSuperclass(final Class<?> type) {
        return forTypeProvider(new DefaultTypeProvider() {
            @Override
            public Type getType() {
                return type.getGenericSuperclass();
            }
        });
    }

    /**
     * Return a {@link Serializable} variant of
     * {@link Class#getGenericInterfaces()}.
     */
    @SuppressWarnings("serial")
    public static Type[] forGenericInterfaces(final Class<?> type) {
        Type[] result = new Type[type.getGenericInterfaces().length];
        for (int i = 0; i < result.length; i++) {
            final int index = i;
            result[i] = forTypeProvider(new DefaultTypeProvider() {
                @Override
                public Type getType() {
                    return type.getGenericInterfaces()[index];
                }
            });
        }
        return result;
    }

    /**
     * Return a {@link Serializable} variant of
     * {@link Class#getTypeParameters()}.
     */
    @SuppressWarnings("serial")
    public static Type[] forTypeParameters(final Class<?> type) {
        Type[] result = new Type[type.getTypeParameters().length];
        for (int i = 0; i < result.length; i++) {
            final int index = i;
            result[i] = forTypeProvider(new DefaultTypeProvider() {
                @Override
                public Type getType() {
                    return type.getTypeParameters()[index];
                }
            });
        }
        return result;
    }

    /**
     * Unwrap the given type, effectively returning the original
     * non-serializable type.
     *
     * @param type the type to unwrap
     * @return the original non-serializable type
     */
    @SuppressWarnings("unchecked")
    public static <T extends Type> T unwrap(T type) {
        Type unwrapped = type;
        while (unwrapped instanceof SerializableTypeProxy) {
            unwrapped = ((SerializableTypeProxy) type).getTypeProvider().getType();
        }
        return (T) unwrapped;
    }

    /**
     * Return a {@link Serializable} {@link Type} backed by a
     * {@link TypeProvider} .
     */
    static Type forTypeProvider(final TypeProvider provider) {
        Assert.notNull(provider, "Provider must not be null");
        if (provider.getType() instanceof Serializable || provider.getType() == null) {
            return provider.getType();
        }
        Type cached = cache.get(provider.getType());
        if (cached != null) {
            return cached;
        }
        for (Class<?> type : SUPPORTED_SERIALIZABLE_TYPES) {
            if (type.isAssignableFrom(provider.getType().getClass())) {
                ClassLoader classLoader = provider.getClass().getClassLoader();
                Class<?>[] interfaces = new Class<?>[]{type,
                    SerializableTypeProxy.class, Serializable.class};
                InvocationHandler handler = new TypeProxyInvocationHandler(provider);
                cached = (Type) Proxy.newProxyInstance(classLoader, interfaces, handler);
                cache.put(provider.getType(), cached);
                return cached;
            }
        }
        throw new IllegalArgumentException("Unsupported Type class " + provider.getType().getClass().getName());
    }

    /**
     * Additional interface implemented by the type proxy.
     */
    static interface SerializableTypeProxy {

        /**
         * Return the underlying type provider.
         */
        TypeProvider getTypeProvider();

    }

    /**
     * A {@link Serializable} interface providing access to a {@link Type}.
     */
    static interface TypeProvider extends Serializable {

        /**
         * Return the (possibly non {@link Serializable}) {@link Type}.
         */
        Type getType();

        /**
         * Return the source of the type or {@code null}.
         */
        Object getSource();
    }

    /**
     * Default implementation of {@link TypeProvider} with a {@code null}
     * source.
     */
    @SuppressWarnings("serial")
    private static abstract class DefaultTypeProvider implements TypeProvider {

        @Override
        public Object getSource() {
            return null;
        }

    }

    /**
     * {@link Serializable} {@link InvocationHandler} used by the Proxied
     * {@link Type}. Provides serialization support and enhances any methods
     * that return {@code Type} or {@code Type[]}.
     */
    @SuppressWarnings("serial")
    private static class TypeProxyInvocationHandler implements InvocationHandler, Serializable {

        private final TypeProvider provider;

        public TypeProxyInvocationHandler(TypeProvider provider) {
            this.provider = provider;
        }

        @Override
        public Object invoke(Object proxy, Method method, Object[] args) throws Throwable {
            if (GET_TYPE_PROVIDER_METHOD.equals(method)) {
                return this.provider;
            }
            if (EQUALS_METHOD.equals(method)) {
                Object other = args[0];
                // Unwrap proxies for speed
                if (other instanceof Type) {
                    other = unwrap((Type) other);
                }
                return this.provider.getType().equals(other);
            }
            if (Type.class.equals(method.getReturnType()) && args == null) {
                return forTypeProvider(new MethodInvokeTypeProvider(this.provider, method, -1));
            }
            if (Type[].class.equals(method.getReturnType()) && args == null) {
                Type[] result = new Type[((Type[]) method.invoke(this.provider.getType(), args)).length];
                for (int i = 0; i < result.length; i++) {
                    result[i] = forTypeProvider(new MethodInvokeTypeProvider(this.provider, method, i));
                }
                return result;
            }
            try {
                return method.invoke(this.provider.getType(), args);
            } catch (InvocationTargetException ex) {
                throw ex.getTargetException();
            }
        }
    }

    /**
     * {@link TypeProvider} for {@link Type}s obtained from a {@link Field}.
     */
    @SuppressWarnings("serial")
    static class FieldTypeProvider implements TypeProvider {

        private final String fieldName;

        private final Class<?> declaringClass;

        private transient Field field;

        public FieldTypeProvider(Field field) {
            this.fieldName = field.getName();
            this.declaringClass = field.getDeclaringClass();
            this.field = field;
        }

        @Override
        public Type getType() {
            return this.field.getGenericType();
        }

        @Override
        public Object getSource() {
            return this.field;
        }
    }

    /**
     * {@link TypeProvider} for {@link Type}s obtained from a
     * {@link MethodParameter}.
     */
    static class MethodParameterTypeProvider implements TypeProvider {

        private final String methodName;

        private final Class<?>[] parameterTypes;

        private final Class<?> declaringClass;

        private final int parameterIndex;

        private transient MethodParameter methodParameter;

        public MethodParameterTypeProvider(MethodParameter methodParameter) {
            if (methodParameter.getMethod() != null) {
                this.methodName = methodParameter.getMethod().getName();
                this.parameterTypes = methodParameter.getMethod().getParameterTypes();
            } else {
                this.methodName = null;
                this.parameterTypes = methodParameter.getConstructor().getParameterTypes();
            }
            this.declaringClass = methodParameter.getDeclaringClass();
            this.parameterIndex = methodParameter.getParameterIndex();
            this.methodParameter = methodParameter;
        }

        @Override
        public Type getType() {
            return this.methodParameter.getGenericParameterType();
        }

        @Override
        public Object getSource() {
            return this.methodParameter;
        }
    }

    /**
     * {@link TypeProvider} for {@link Type}s obtained by invoking a no-arg
     * method.
     */
    static class MethodInvokeTypeProvider implements TypeProvider {

        private final TypeProvider provider;

        private final String methodName;

        private final int index;

        private transient Object result;

        public MethodInvokeTypeProvider(TypeProvider provider, Method method, int index) {
            this.provider = provider;
            this.methodName = method.getName();
            this.index = index;
            this.result = ReflectionUtils.invokeMethod(method, provider.getType());
        }

        @Override
        public Type getType() {
            if (this.result instanceof Type || this.result == null) {
                return (Type) this.result;
            }
            return ((Type[]) this.result)[this.index];
        }

        @Override
        public Object getSource() {
            return null;
        }
    }

}
