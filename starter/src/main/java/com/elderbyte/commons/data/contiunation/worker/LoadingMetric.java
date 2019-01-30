package com.elderbyte.commons.data.contiunation.worker;

public class LoadingMetric extends BaseMetric {

    /***************************************************************************
     *                                                                         *
     * Static builders                                                         *
     *                                                                         *
     **************************************************************************/

    public static LoadingMetric fromTime(long loadingTime){
        return new LoadingMetric(
                loadingTime
        );
    }

    public static LoadingMetric error(Throwable throwable, long loadingTime){
        return new LoadingMetric(loadingTime, throwable);
    }

    /***************************************************************************
     *                                                                         *
     * Fields                                                                  *
     *                                                                         *
     **************************************************************************/

    /**
     * The duration of this batch loading [nano]
     */
    private final long batchLoadingTime;


    /***************************************************************************
     *                                                                         *
     * Constructor                                                             *
     *                                                                         *
     **************************************************************************/

    public LoadingMetric(long batchLoadingTime) {
        this(batchLoadingTime, null);
    }

    public LoadingMetric(long batchLoadingTime, Throwable throwable){
        super(throwable);
        this.batchLoadingTime = batchLoadingTime;
    }

    /***************************************************************************
     *                                                                         *
     * Properties                                                              *
     *                                                                         *
     **************************************************************************/

    public long getBatchLoadingTime() {
        return batchLoadingTime;
    }
}
