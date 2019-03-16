package com.elderbyte.web;

import com.elderbyte.commons.exceptions.ArgumentNullException;
import org.junit.Test;

import java.util.ArrayList;
import java.util.List;

import static org.junit.Assert.*;

public class QueryMapUtilTest {


    public static class ParamsMock {

        private String def = "huh";
        private String name;
        private int age;
        private double value;
        private List<String> labels = new ArrayList<>();
        private String ignoreMe;

        public String getName() {
            return name;
        }

        public void setName(String name) {
            this.name = name;
        }

        public int getAge() {
            return age;
        }

        public void setAge(int age) {
            this.age = age;
        }

        public double getValue() {
            return value;
        }

        public void setValue(double value) {
            this.value = value;
        }

        public List<String> getLabels() {
            return labels;
        }

        public void setLabels(List<String> labels) {
            this.labels = labels;
        }

        public String getIgnoreMe() {
            return ignoreMe;
        }

        public void setIgnoreMe(String ignoreMe) {
            this.ignoreMe = ignoreMe;
        }

        public String getDef() {
            return def;
        }

        public void setDef(String def) {
            this.def = def;
        }
    }


    @Test(expected = ArgumentNullException.class)
    public void toQueryMap_null_error() {
        QueryMapUtil.toQueryMap(null);
    }

    @Test
    public void toQueryMap() {

        var mock = new ParamsMock();
        mock.name = "unit";
        mock.age = 12;
        mock.value = 13.44563;
        mock.labels.add("woot");
        mock.labels.add("foobar");

        var values = QueryMapUtil.toQueryMap(mock);


        assertEquals("unit", values.getFirst("name"));
        assertEquals("12", values.getFirst("age"));
        assertEquals("13.44563", values.getFirst("value"));
        assertFalse("ignoreMe must not be serialized since its null!", values.containsKey("ignoreMe"));
        assertFalse("class must not be serialized since its on Object!", values.containsKey("class"));

        var labels = values.get("labels");

        var labelsStr = "[" + String.join(";", labels) + "]";

        assertTrue("labels must contain foobar: " + labelsStr, labels.contains("foobar"));
        assertTrue("labels must contain woot: " + labelsStr, labels.contains("woot"));
    }

    @Test
    public void toQueryMap_default_values() {

        var mock = new ParamsMock();
        var values = QueryMapUtil.toQueryMap(mock);


        assertFalse(values.containsKey("name must not be serialized since its null!"));
        assertEquals("huh", values.getFirst("def"));
        assertEquals("0", values.getFirst("age"));
        assertEquals("0.0", values.getFirst("value"));
        assertFalse("ignoreMe must not be serialized since its null!", values.containsKey("ignoreMe"));
        assertFalse("class must not be serialized since its on Object!", values.containsKey("class"));

        assertFalse("labels must not be serialized since its empty!", values.containsKey("labels"));
    }
}
