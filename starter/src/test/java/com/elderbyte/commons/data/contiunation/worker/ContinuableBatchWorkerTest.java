package com.elderbyte.commons.data.contiunation.worker;

import com.elderbyte.commons.cancelation.CancellationToken;
import com.elderbyte.commons.data.contiunation.ContinuableListing;
import com.elderbyte.commons.data.contiunation.ContinuationToken;
import org.junit.Assert;
import org.junit.Test;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;
import java.util.function.Function;

import static org.junit.Assert.*;

public class ContinuableBatchWorkerTest {


    public Function<String, ContinuableListing<String>> mockChunkLoader = token -> {


        ContinuableListing<String> listing;

        if(token == null || token.equals("0")){
            listing = ContinuableListing.continuable(
                    Arrays.asList("1", "2", "3", "4", "5", "6", "7", "8", "9", "10"),
                    10,
                    30L,
                    ContinuationToken.from(token),
                    ContinuationToken.from("1")
            );
        }else if (token.equals("1")){
            listing = ContinuableListing.continuable(
                    Arrays.asList("11", "12", "13", "14", "15", "16", "17", "18", "19", "20"),
                    10,
                    30L,
                    ContinuationToken.from(token),
                    ContinuationToken.from("2")
            );
        }else if (token.equals("2")){
            listing = ContinuableListing.finiteChunk(
                    Arrays.asList("21", "22", "23", "24", "25", "26", "27", "28", "29", "30"),
                    10,
                    30L,
                    ContinuationToken.from(token)
            );
        }else{
            throw new IllegalStateException("Unexpected token: " + token);
        }

        return listing;
    };


    @Test
    public void processAll() {

        var reports = new ArrayList<ContinuableBatchWorker.Metrics>();

        var result = ContinuableBatchWorker.worker(mockChunkLoader, batch -> {})
                    .processAll(progress -> {
                        reports.add(progress);
                    }, CancellationToken.Never);



        Assert.assertEquals(3, reports.size(), 0);

        Assert.assertEquals(33.3333, calcProgress(reports.get(0)), 0.1);
        Assert.assertEquals(66.6666, calcProgress(reports.get(1)), 0.1);
        Assert.assertEquals(100.0, calcProgress(reports.get(2)), 0.1);


        Assert.assertEquals(100, calcProgress(result), 0);
    }

    private double calcProgress(ContinuableBatchWorker.Metrics metrics){
        return (100d / (double)metrics.getTotalItems().orElse(0L)) * (double)metrics.getProcessedItems();
    }
}