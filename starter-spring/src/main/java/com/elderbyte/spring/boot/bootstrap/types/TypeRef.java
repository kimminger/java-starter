package com.elderbyte.spring.boot.bootstrap.types;

import com.elderbyte.commons.reflect.ParameterizedTypeImpl;
import org.springframework.core.ParameterizedTypeReference;

import java.lang.reflect.Type;

public class TypeRef<W, P, Combined extends W> extends ParameterizedTypeReference<Combined> {

    public static <W, P, Combined extends W>  TypeRef<W, P, Combined> from(Class<W> wrapperType, Class<P> resourceType){
        return new TypeRef<>(wrapperType, resourceType);
    }

    /***************************************************************************
     *                                                                         *
     * Fields                                                                  *
     *                                                                         *
     **************************************************************************/

    private final Class<W> wrapperType;
    private final Class<P> resourceType;
    private final Type type;

    /***************************************************************************
     *                                                                         *
     * Constructor                                                             *
     *                                                                         *
     **************************************************************************/

    /**
     * Creates a new TypeRef
     */
    private TypeRef(Class<W> wrapperType, Class<P> resourceType) {
        this.wrapperType = wrapperType;
        this.resourceType = resourceType;
        this.type = buildParameterizedType();
    }

    /***************************************************************************
     *                                                                         *
     * Public API                                                              *
     *                                                                         *
     **************************************************************************/

    @Override
    public Type getType() {
        return type;
    }

    @Override
    public boolean equals(Object other) {
        return (this == other || (other instanceof ParameterizedTypeReference &&
                this.type.equals(((ParameterizedTypeReference<?>) other).getType())));
    }

    @Override
    public int hashCode() {
        return this.type.hashCode();
    }

    @Override
    public String toString() {
        return "ParameterizedTypeReference<" + this.type + ">";
    }

    /***************************************************************************
     *                                                                         *
     * Private methods                                                         *
     *                                                                         *
     **************************************************************************/

    private Type buildParameterizedType(){
        Type[] responseWrapperActualTypes = { resourceType };
        return new ParameterizedTypeImpl(
                wrapperType,
                responseWrapperActualTypes
        );
    }
}
