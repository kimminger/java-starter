package com.elderbyte.web.rest.api;

public interface CRUDResource<TBody, TId, TCreate>
        extends
        GetResource<TBody, TId>,
        CreateResource<TBody, TCreate>,
        DeleteResource<TId>,
        UpdateResource<TBody, TId>
{
}
