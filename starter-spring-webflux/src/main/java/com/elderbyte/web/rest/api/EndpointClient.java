package com.elderbyte.web.rest.api;

import com.elderbyte.web.client.WebClientApi;

public interface EndpointClient extends WebClientApi {
    String getEndpointPath();
}
