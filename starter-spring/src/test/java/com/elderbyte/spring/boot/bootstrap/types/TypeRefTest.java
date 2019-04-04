package com.elderbyte.spring.boot.bootstrap.types;

import org.junit.Assert;
import org.junit.Test;
import org.springframework.core.ParameterizedTypeReference;

import java.util.List;

import static org.junit.Assert.*;

public class TypeRefTest {

    @Test
    public void from() {
        var ref = TypeRef.from(List.class, TypeRefTest.class);
        var paty = new ParameterizedTypeReference<List<TypeRefTest>>(){};

        var typeA = ref.getType();
        var typeB = paty.getType();

        assertEquals(typeA, typeB);
    }

    @Test
    public void hash_code_equals() {
        var ref = TypeRef.from(List.class, TypeRefTest.class);
        var paty = new ParameterizedTypeReference<List<TypeRefTest>>(){};
        Assert.assertEquals(ref.hashCode(), paty.hashCode());
    }

    @Test
    public void equals() {
        var ref = TypeRef.from(List.class, TypeRefTest.class);
        var paty = new ParameterizedTypeReference<List<TypeRefTest>>(){};
        assertEquals(ref, paty);
    }
}
