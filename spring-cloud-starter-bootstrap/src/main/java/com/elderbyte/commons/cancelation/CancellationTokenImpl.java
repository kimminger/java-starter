package com.elderbyte.commons.cancelation;

import com.elderbyte.commons.exceptions.ArgumentNullException;

import java.util.concurrent.CancellationException;

public class CancellationTokenImpl implements CancellationToken {

    private final CancellationTokenSource source;

    public CancellationTokenImpl(CancellationTokenSource source){
        if(source == null) throw new ArgumentNullException("source");
        this.source = source;
    }

    @Override
    public boolean isCancellationRequested() {
        return source.isCanceled();
    }

    @Override
    public void throwIfCancellationRequested() throws CancellationException {
        if(isCancellationRequested()){
            throw new CancellationException("Operation has been canceled!");
        }
    }


    static class CancellationTokenNeverImpl implements CancellationToken {
        @Override
        public boolean isCancellationRequested() {
            return false;
        }

        @Override
        public void throwIfCancellationRequested() throws CancellationException {  }
    }



}
