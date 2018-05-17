package com.elderbyte.spring.cloud.bootstrap.integration.data;

import com.elderbyte.commons.data.contiunation.ContinuationToken;
import com.elderbyte.commons.data.contiunation.JsonContinuationToken;
import com.elderbyte.commons.utils.Utf8Base64;
import com.elderbyte.spring.cloud.bootstrap.support.PageableDto;
import org.junit.Test;

import static org.junit.Assert.*;

public class JsonContinuationTokenTest {

    @Test
    public void from() {

        JsonContinuationToken jsonToken = JsonContinuationToken.from(
                JsonContinuationToken.buildToken(new PageableDto(1, 35)
                )
        );
        PageableDto dto = jsonToken.asJson(PageableDto.class).orElse(null);

        assertEquals(1, dto.pageIndex);
        assertEquals(35, dto.pageSize);
    }

    @Test
    public void buildToken() {
        ContinuationToken token = JsonContinuationToken.buildToken(new PageableDto(1, 35));
        assertEquals("{\"pageIndex\":1,\"pageSize\":35}", Utf8Base64.decodeUtf8(token.getToken()));
    }

    @Test
    public void getToken() {
    }

    @Test
    public void decodeBase64() {
    }

    @Test
    public void asJson() {
    }
}