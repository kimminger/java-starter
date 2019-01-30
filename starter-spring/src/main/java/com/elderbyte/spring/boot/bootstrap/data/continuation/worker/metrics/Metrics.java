package com.elderbyte.spring.boot.bootstrap.data.continuation.worker.metrics;

import java.util.Optional;

public class Metrics {

        private final Long totalItems;
        private final String completedToken;
        private final String nextToken;
        private final int processedItems;
        private final int processedBatches;
        private final long batchMaxTimeMs;
        private final long batchMinTimeMs;
        private final long totalTimeMs;

        Metrics(
                Long totalItems,
                String completedToken,
                String nextToken,
                int processedItems,
                int processedBatches,
                long batchMaxTimeMs,
                long batchMinTimeMs,
                long totalTimeMs) {

            this.completedToken = completedToken;
            this.nextToken = nextToken;
            this.totalItems = totalItems;
            this.processedItems = processedItems;
            this.processedBatches = processedBatches;
            this.batchMaxTimeMs = batchMaxTimeMs;
            this.batchMinTimeMs = batchMinTimeMs;
            this.totalTimeMs = totalTimeMs;
        }

        public int getProcessedItems() {
            return processedItems;
        }

        public int getProcessedBatches() {
            return processedBatches;
        }

        public long getBatchMaxTimeMs() {
            return batchMaxTimeMs;
        }

        public long getBatchMinTimeMs() {
            return batchMinTimeMs;
        }

        public long getTotalTimeMs() {
            return totalTimeMs;
        }

        public Optional<Long> getTotalItems() {
            return Optional.ofNullable(totalItems);
        }

        /**
         * The continuation token of the last completed batch.
         * Might be null for the first batch.
         */
        public String getCompletedToken() {
            return completedToken;
        }

        /**
         * The next continuation token of the last completed batch.
         * Might be null for the last batch.
         */
        public String getNextToken() {
            return nextToken;
        }
    }
