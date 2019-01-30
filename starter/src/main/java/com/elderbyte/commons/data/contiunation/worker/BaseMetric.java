package com.elderbyte.commons.data.contiunation.worker;

public abstract class BaseMetric {

    private final Throwable exceptionDetail;

    protected BaseMetric(){
        this(null);
    }

    protected BaseMetric(Throwable exceptionDetail){
        this.exceptionDetail = exceptionDetail;
    }


    public Throwable getExceptionDetail() {
        return exceptionDetail;
    }

    public boolean isError(){
        return exceptionDetail != null;
    }
}
