package com.elderbyte.commons.measures;

import java.util.List;
import java.util.stream.Collectors;

public class QuantityUtil {

    /***************************************************************************
     *                                                                         *
     * Public API                                                              *
     *                                                                         *
     **************************************************************************/

    public static Quantity sumUpQuantities(List<Quantity> quantities) {
        if(quantities==null || quantities.size()==0){
            return null;
        }

        var units = quantities.stream()
                .map(quantity -> quantity.getUnit())
                .collect(Collectors.toSet());

        if (units.size() != 1) {
            throw new RuntimeException("Different unit or dimensions can't be summed up.");
        }

        var sum = quantities.stream()
                .mapToDouble(quantity -> quantity.getValue())
                .sum();

        return Quantity.from(units.iterator().next(), sum);

    }


}
