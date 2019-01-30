package com.elderbyte.commons.data.contiunation.worker;

import com.elderbyte.commons.cancelation.CancellationToken;
import com.elderbyte.commons.data.contiunation.ContinuableListing;
import com.elderbyte.commons.data.contiunation.ContinuationToken;
import org.junit.Assert;
import org.junit.Test;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.function.Function;


public class ContinuableBatchWorkerTest {


    public Function<String, ContinuableListing<String>> mockChunkLoader = token -> {


        ContinuableListing<String> listing;

        if(token == null || token.equals("0")){
            listing = ContinuableListing.continuable(
                    Arrays.asList("1", "2", "3", "4", "5", "6", "7", "8", "9", "10"),
                    10,
                    28L,
                    ContinuationToken.from(token),
                    ContinuationToken.from("1")
            );
        }else if (token.equals("1")){
            listing = ContinuableListing.continuable(
                    Arrays.asList("11", "12", "13", "14", "15", "16", "17", "18", "19", "20"),
                    10,
                    28L,
                    ContinuationToken.from(token),
                    ContinuationToken.from("2")
            );
        }else if (token.equals("2")){
            listing = ContinuableListing.finiteChunk(
                    Arrays.asList("21", "22", "23", "24", "25", "26", "27", "28"),
                    8,
                    28L,
                    ContinuationToken.from(token)
            );
        }else{
            throw new IllegalStateException("Unexpected token: " + token);
        }

        return listing;
    };


    @Test
    public void processAll() {

        var processingMetrics = new ArrayList<ProcessingMetric>();
        var loadingMetrics = new ArrayList<LoadingMetric>();

        ContinuableBatchWorker.worker(mockChunkLoader, batch -> {})
                .loadingMetrics(loadingMetrics::add)
                .processingMetrics(processingMetrics::add)
                    .processAll(
                            CancellationToken.Never
                    );

        Assert.assertEquals(3, processingMetrics.size(), 0);
        Assert.assertEquals(3, loadingMetrics.size(), 0);

        Assert.assertEquals(10, processingMetrics.get(0).getBatchSize());
        Assert.assertEquals(10, processingMetrics.get(1).getBatchSize());
        Assert.assertEquals( 8, processingMetrics.get(2).getBatchSize());


        // Assert.assertEquals(100, calcProgress(records), 0);
    }

    /*
    private double calcProgress(ProcessingMetric metrics){
        return (100d / (double)metrics.getTotalItems().orElse(0L)) * (double)metrics.getBatchSize();
    }*/
}
