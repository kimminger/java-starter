package com.elderbyte.commons.measures;

import java.util.HashMap;
import java.util.Map;

public class DimensionUtil {

    /***************************************************************************
     *                                                                         *
     * Fields                                                                  *
     *                                                                         *
     **************************************************************************/

    private static final Map<Unit, Dimension> lookUpMap = initLookUp();

    /***************************************************************************
     *                                                                         *
     * Constructors                                                            *
     *                                                                         *
     **************************************************************************/

    private DimensionUtil(){}

    /***************************************************************************
     *                                                                         *
     * Public API                                                              *
     *                                                                         *
     **************************************************************************/

    public static Dimension getDimension(Quantity quantity) {
        if (quantity == null || quantity.getUnit() == null) {
            throw new IllegalArgumentException();
        }
        return getDimensionByUnit(quantity.getUnit());

    }

    public static Dimension getDimensionByUnit(Unit si) {
        var lookUp = lookUpMap.get(si);
        return (lookUp != null) ?  lookUp : Dimension.OTHER;
    }

    /***************************************************************************
     *                                                                         *
     * Private Methods                                                         *
     *                                                                         *
     **************************************************************************/

    private static Map<Unit, Dimension> initLookUp() {
        var lookUpMap = new HashMap<Unit, Dimension>();

        /* Mass */
        lookUpMap.put(Unit.mg, Dimension.MASS);
        lookUpMap.put(Unit.g, Dimension.MASS);
        lookUpMap.put(Unit.kg, Dimension.MASS);
        lookUpMap.put(Unit.t, Dimension.MASS);

        /* Length */
        lookUpMap.put(Unit.mm, Dimension.LENGTH);
        lookUpMap.put(Unit.cm, Dimension.LENGTH);
        lookUpMap.put(Unit.dm, Dimension.LENGTH);
        lookUpMap.put(Unit.m, Dimension.LENGTH);
        lookUpMap.put(Unit.km, Dimension.LENGTH);

        /* Volume */
        lookUpMap.put(Unit.mm3, Dimension.VOLUME);
        lookUpMap.put(Unit.cm3, Dimension.VOLUME);
        lookUpMap.put(Unit.dm3, Dimension.VOLUME);
        lookUpMap.put(Unit.m3, Dimension.VOLUME);
        lookUpMap.put(Unit.ml, Dimension.VOLUME);
        lookUpMap.put(Unit.cl, Dimension.VOLUME);
        lookUpMap.put(Unit.dl, Dimension.VOLUME);
        lookUpMap.put(Unit.l, Dimension.VOLUME);

        /* Time */
        lookUpMap.put(Unit.sec, Dimension.TIME);
        lookUpMap.put(Unit.min, Dimension.TIME);
        lookUpMap.put(Unit.hr, Dimension.TIME);
        lookUpMap.put(Unit.da, Dimension.TIME);
        lookUpMap.put(Unit.mo, Dimension.TIME);
        lookUpMap.put(Unit.yr, Dimension.TIME);

        return lookUpMap;
    }

}
