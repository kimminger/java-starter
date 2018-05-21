package com.elderbyte.commons.utils;

/**
 * Provides the ability to instrument basic latency metrics by measuring time which passes between points.
 */
public class Stopwatch {

    /**
     * Creates a new stop watch and starts measuring time.
     * @return The new created running stop watch.
     */
    public static Stopwatch started(){
        var watch = new Stopwatch();
        watch.reset();
        return watch;
    }

    /***************************************************************************
     *                                                                         *
     * Fields                                                                  *
     *                                                                         *
     **************************************************************************/

    private long start;
    private long mark;

    /***************************************************************************
     *                                                                         *
     * Public API                                                              *
     *                                                                         *
     **************************************************************************/

    /**
     * Start the watch
     */
    public void reset(){
        this.start = System.nanoTime();
        this.mark = this.start;
    }

    /**
     * Returns the time since the last mark (or start if first call to mark)
     * Updates mark with now.
     */
    public long markNano(){
        var elapsed = elapsedSince(mark);
        mark = System.nanoTime();
        return elapsed;
    }

    /**
     * Returns the time since the last mark (or start if first call to mark)
     * Updates mark with now.
     */
    public long markMs(){
        return toMillis(markNano());
    }

    /**
     * Returns the total elapsed time since start
     */
    public long sinceStartNano(){
        return elapsedSince(start);
    }

    /**
     * Returns the total elapsed time since start
     */
    public long sinceStartMs(){
        return toMillis(sinceStartNano());
    }

    /***************************************************************************
     *                                                                         *
     * Private methods                                                         *
     *                                                                         *
     **************************************************************************/


    private long toMillis(long nano){
        return nano / (1000*1000);
    }

    private long elapsedSince(long since){
        return (System.nanoTime() - since);
    }
}
