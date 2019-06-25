package com.elderbyte.commons.measures;

import com.elderbyte.commons.exceptions.ArgumentNullException;
import java.util.Objects;

/**
 * Represents an immutable quantity.
 */
public class Quantity {

    public static Quantity from(Unit unit) {
        return new Quantity(unit);
    }

    public static Quantity from(Unit unit, double value) {
        return new Quantity(unit, value);
    }

    /***************************************************************************
     *                                                                         *
     * Fields                                                                  *
     *                                                                         *
     **************************************************************************/

    private double value;

    private Unit unit;

    /***************************************************************************
     *                                                                         *
     * Constructors                                                            *
     *                                                                         *
     **************************************************************************/

    protected Quantity(){}

    public Quantity(Unit unit) {
        this(unit, 0);
    }

    public Quantity(Unit unit, double value){

        if(unit == null) throw new ArgumentNullException("unit");

        this.value = value;
        this.unit = unit;
    }

    /***************************************************************************
     *                                                                         *
     * Properties                                                              *
     *                                                                         *
     **************************************************************************/

    public double getValue() {
        return value;
    }

    public Unit getUnit() {
        return unit;
    }

    /***************************************************************************
     *                                                                         *
     * Public API                                                              *
     *                                                                         *
     **************************************************************************/

    @Override
    public String toString() {
        return "Quantity{" +
                "value=" + value +
                ", unit=" + unit +
                '}';
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;
        Quantity quantity = (Quantity) o;
        return Double.compare(quantity.value, value) == 0 &&
                unit == quantity.unit;
    }

    @Override
    public int hashCode() {
        return Objects.hash(value, unit);
    }
}
