package com.elderbyte.spring.boot.bootstrap.data.continuation.worker.metrics;

public class MetricsReporter {

        private Long totalItems = null;
        private String completedToken = null;
        private String nextToken = null;

        private int processedItems;
        private int processedBatches;

        private long batchMaxTimeMs = 0;
        private long batchMinTimeMs = Long.MAX_VALUE;
        private long totalTimeNano = 0;


        public void reportLoadingBatch(long loadingTimeNano) {
        }

        public void reportProcessedBatch(int items, long processingTimeNano, Long total, String completedToken, String nextToken){

            this.totalItems = total;
            this.completedToken = completedToken;
            this.nextToken = nextToken;

            this.totalTimeNano += processingTimeNano;
            this.processedItems += items;
            this.processedBatches++;

            var msTime = nanoToMillis(processingTimeNano);
            this.batchMaxTimeMs = Math.max(batchMaxTimeMs, msTime);
            this.batchMinTimeMs = Math.min(batchMinTimeMs, msTime);
        }

        public Metrics getSnapshot(){
            return new Metrics(
                    totalItems,
                    completedToken,
                    nextToken,
                    processedItems,
                    processedBatches,
                    batchMaxTimeMs,
                    batchMinTimeMs,
                    nanoToMillis(totalTimeNano)
            );
        }

        private long nanoToMillis(long nano){
            return nano / (1000*1000);
        }


    /***************************************************************************
     *                                                                         *
     *  Time tracker                                                           *
     *                                                                         *
     **************************************************************************/


}
