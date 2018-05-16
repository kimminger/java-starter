package com.elderbyte.commons.cancelation;

public class CancellationTokenSource {

    public static CancellationTokenSource newSource(){
        return new CancellationTokenSource();
    }

    private final CancellationToken token;
    private volatile boolean canceled = false;

    private CancellationTokenSource(){
        token = new CancellationTokenImpl(this);
    }


    public CancellationToken getToken(){
        return token;
    }

    public void cancel(){
        canceled = true;
    }

    public boolean isCanceled() {
        return canceled;
    }
}
