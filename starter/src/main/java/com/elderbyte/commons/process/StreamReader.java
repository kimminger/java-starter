package com.elderbyte.commons.process;

import java.io.InputStream;


public interface StreamReader<T> {

    void read(InputStream stream);

    T getValue();

}
