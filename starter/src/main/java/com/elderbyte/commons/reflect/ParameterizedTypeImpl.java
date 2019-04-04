package com.elderbyte.commons.reflect;

import com.elderbyte.commons.exceptions.ArgumentNullException;

import java.io.Serializable;
import java.lang.reflect.ParameterizedType;
import java.lang.reflect.Type;
import java.util.Arrays;
import java.util.Collection;
import java.util.Objects;
import java.util.stream.Collectors;
import java.util.stream.Stream;


public final class ParameterizedTypeImpl
      implements ParameterizedType, Serializable {

    private final Type ownerType;
    private final Type[] typeArguments;
    private final Class<?> rawType;


    public ParameterizedTypeImpl(Class<?> rawType, Type[] typeArguments) {
        this(rawType, typeArguments, null);
    }

    public ParameterizedTypeImpl(Class<?> rawType, Type[] typeArguments, Type ownerType) {
        this(rawType, Arrays.asList(typeArguments), ownerType);
    }

    public ParameterizedTypeImpl(Class<?> rawType, Collection<Type> typeArguments, Type ownerType) {

        if(rawType == null)
            throw new ArgumentNullException("rawType");

        if(typeArguments.size() != rawType.getTypeParameters().length)
            throw new ArgumentNullException("typeArguments count was wrong!" +
                    " It was " + typeArguments.size() + " but expected " + rawType.getTypeParameters().length);

      this.ownerType = ownerType;
      this.rawType = rawType;
      this.typeArguments = typeArguments.toArray(new Type[0]);
    }

    @Override public Type[] getActualTypeArguments() {
      return typeArguments;
    }

    @Override public Type getRawType() {
      return rawType;
    }

    @Override public Type getOwnerType() {
      return ownerType;
    }

    @Override public String toString() {
      StringBuilder builder = new StringBuilder();
      if (ownerType != null) {
        builder.append(ownerType.toString()).append('.');
      }
      builder.append(rawType.getName())
          .append('<')
          .append(Stream.of(typeArguments).map(Object::toString).collect(Collectors.joining(",")))
          .append('>');
      return builder.toString();
    }

    @Override public int hashCode() {
      return (ownerType == null ? 0 : ownerType.hashCode())
          ^ Arrays.hashCode(typeArguments) ^ rawType.hashCode();
    }

    @Override public boolean equals(Object other) {
      if (!(other instanceof ParameterizedType)) {
        return false;
      }
      ParameterizedType that = (ParameterizedType) other;
      return getRawType().equals(that.getRawType())
          && Objects.equals(getOwnerType(), that.getOwnerType())
          && Arrays.equals(
              getActualTypeArguments(), that.getActualTypeArguments());
    }

    private static final long serialVersionUID = 0;
  }
