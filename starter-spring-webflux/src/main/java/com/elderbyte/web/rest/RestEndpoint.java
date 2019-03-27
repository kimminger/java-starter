package com.elderbyte.web.rest;

import com.elderbyte.commons.data.contiunation.ContinuableListing;
import com.elderbyte.commons.data.contiunation.ContinuationToken;
import com.elderbyte.web.UriBuilderSupport;
import com.elderbyte.web.client.EndpointClientImpl;
import com.elderbyte.web.client.WebClientApi;
import com.elderbyte.web.client.RequestOptions;
import com.elderbyte.web.rest.api.CRUDResource;
import com.elderbyte.web.rest.api.EndpointClient;
import org.springframework.core.ParameterizedTypeReference;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.Sort;
import org.springframework.web.reactive.function.BodyInserters;
import org.springframework.web.util.UriBuilder;
import reactor.core.publisher.Mono;

import java.util.Collection;
import java.util.List;
import java.util.function.Function;

public class RestEndpoint<TBody, TId, TCreate>
        implements CRUDResource<TBody, TId, TCreate> {

    /***************************************************************************
     *                                                                         *
     * Fields                                                                  *
     *                                                                         *
     **************************************************************************/

    private final String idsQueryParam;
    private final WebClientApi client;
    private final EndpointClient endpoint;
    private final Class<TBody> resourceType;

    /***************************************************************************
     *                                                                         *
     * Constructor                                                             *
     *                                                                         *
     **************************************************************************/


    /**
     * Creates a new RestEndpoint
     */
    public RestEndpoint(
            WebClientApi client,
            String endpoint,
            Class<TBody> resourceType){
        this(client, endpoint, resourceType, "ids");
    }

    /**
     * Creates a new RestEndpoint
     */
    public RestEndpoint(
            WebClientApi client,
            String endpoint,
            Class<TBody> resourceType,
            String idsQueryParam
    ) {
        this.client = client;
        this.endpoint = new EndpointClientImpl(client, endpoint);
        this.resourceType = resourceType;
        this.idsQueryParam = idsQueryParam;
    }

    /***************************************************************************
     *                                                                         *
     * Properties                                                              *
     *                                                                         *
     **************************************************************************/

    public EndpointClient endpoint(){
        return endpoint;
    }

    public WebClientApi client(){
        return client;
    }

    /***************************************************************************
     *                                                                         *
     * Public API                                                              *
     *                                                                         *
     **************************************************************************/

    public Mono<TBody> getById(TId id){
        return getById(id, RequestOptions.empty());
    }

    public Mono<Void> delete(TId id){
        return delete(id, RequestOptions.empty());
    }

    public Mono<TBody> create(TCreate newEntity) {
        return create(newEntity, RequestOptions.empty());
    }

    public Mono<TBody> update(TId id, TBody entity) {
        return update(id, entity, RequestOptions.empty());
    }

    /***************************************************************************
     *                                                                         *
     * Protected API with custom options                                       *
     *                                                                         *
     **************************************************************************/


    protected Mono<Void> delete(TId id, RequestOptions options){
        return delete(id, options, Void.class);
    }

    protected <T> Mono<T> delete(TId id, RequestOptions options, Class<T> clazz){
        return endpoint().delete(uri -> uri.pathSegment(toPath(id)), clazz, options);
    }

    protected Mono<TBody> getById(TId id, RequestOptions options){
        return getOne(toPath(id), options);
    }

    protected Mono<TBody> create(TCreate newEntity, RequestOptions options) {
        return endpoint().post(
                uri -> uri,
                BodyInserters.fromObject(newEntity),
                resourceType,
                options
        );
    }

    protected Mono<TBody> update(TId id, TBody entity, RequestOptions options) {
        return endpoint().put(
                uri -> uri.pathSegment(toPath(id)),
                BodyInserters.fromObject(entity),
                resourceType,
                options
        );
    }

    /***************************************************************************
     *                                                                         *
     * Protected API with custom types + options                               *
     *                                                                         *
     **************************************************************************/

    protected Mono<TBody> getOne(
            String pathPart,
            RequestOptions options
    ){
        return endpoint().get(pathPart, options, resourceType);
    }

    protected Mono<Void> deleteAll(Collection<TId> ids){
        return deleteAll(
                RequestOptions.start()
                        .paramsConvert(idsQueryParam, ids)
        );
    }

    protected Mono<Void> deleteAll(RequestOptions options){
        return deleteAll(options, Void.class);
    }

    protected <T> Mono<T> deleteAll(RequestOptions options, Class<T> clazz){
        return endpoint().delete(clazz, options);
    }

    protected <T> Mono<T> deleteAll(RequestOptions options, ParameterizedTypeReference<T> clazz){
        return endpoint().delete(clazz, options);
    }

    protected Mono<List<TBody>> createAll(String pathPart, Collection<TCreate> newEntities) {
        return createAll(pathPart, newEntities, RequestOptions.empty());
    }

    protected Mono<List<TBody>> createAll(String pathPart, Collection<TCreate> newEntities, RequestOptions options) {
        return endpoint().post(
                pathPart,
                BodyInserters.fromObject(newEntities),
                new ParameterizedTypeReference<List<TBody>>() {},
                options
        );
    }

    protected Mono<List<TBody>> updateAll(Collection<TBody> updatedEntities, RequestOptions options) {
        return updateAll(null, updatedEntities, options);
    }

    protected Mono<List<TBody>> updateAll(String pathPart, Collection<TBody> updatedEntities, RequestOptions options) {
        return endpoint().put(
                uri -> pathPart != null ? uri.pathSegment(pathPart) : uri,
                BodyInserters.fromObject(updatedEntities),
                new ParameterizedTypeReference<List<TBody>>() {},
                options
        );
    }

    /***************************************************************************
     *                                                                         *
     * Protected list API in all flavors                                       *
     *                                                                         *
     **************************************************************************/

    protected Mono<List<TBody>> listAll(String subPath, RequestOptions filter, Sort sort){
        return listAll(b -> b.pathSegment(subPath), filter, sort);
    }

    /**
     * Get all items of this endpoint.
     * Expect an array/list response
     */
    protected Mono<List<TBody>> listAll(RequestOptions filter, Sort sort){
        return listAll(b -> b, filter, sort);
    }

    /**
     * Get all items of this endpoint.
     * Expect an array/list response
     */
    protected Mono<List<TBody>> listAll(Function<UriBuilder, UriBuilder> uriBuilder, RequestOptions filter, Sort sort){
        return endpoint().get(
                b -> uriBuilder.apply(UriBuilderSupport.apply(b, sort)),
                new ParameterizedTypeReference<List<TBody>>() {}, // Dont remove Type Arguments!
                filter
        );
    }

    /**
     * Get all items of this endpoint.
     * Expect an paged response
     */
    protected Mono<Page<TBody>> listAllPaged(
            RequestOptions options,
            Pageable pageable){

        return endpoint().get(
                b -> UriBuilderSupport.apply(b, pageable),
                new ParameterizedTypeReference<Page<TBody>>() {}, // Dont remove Type Arguments!
                options
        );
    }

    /**
     * Get all items of this endpoint.
     * Expect an continuable response
     */
    protected Mono<ContinuableListing<TBody>> listAllContinuable(
            RequestOptions options,
            ContinuationToken token,
            Sort sort){

        return endpoint().get(
                b -> UriBuilderSupport.apply(b, sort, token),
                new ParameterizedTypeReference<ContinuableListing<TBody>>() {}, // Dont remove Type Arguments!
                options
        );
    }

    /***************************************************************************
     *                                                                         *
     * Sub Resources                                                           *
     *                                                                         *
     **************************************************************************/

    public String getSubEndpoint(TId id) {
        return endpoint().getEndpointPath() + "/" + toPath(id);
    }

    protected  <TSBody, TSId, TSCreate>
    RestEndpoint<TSBody, TSId, TSCreate> subResource(TId id, Class<TSBody> subResourceType){
        return new RestEndpoint<>(
                client(),
                getSubEndpoint(id),
                subResourceType
        );
    }

    protected <TSBody, TSId, TSCreate, TSFilter extends RequestOptions>
    RestEndpointList<TSBody, TSId, TSCreate, TSFilter> subResourceList(TId id, Class<TSBody> subResourceType){
        return new RestEndpointList<>(
                client(),
                getSubEndpoint(id),
                subResourceType
        );
    }

    protected <TSBody, TSId, TSCreate, TSFilter extends RequestOptions>
    RestEndpointPaged<TSBody, TSId, TSCreate, TSFilter> subResourcePaged(TId id, Class<TSBody> subResourceType){
        return new RestEndpointPaged<>(
                client(),
                getSubEndpoint(id),
                subResourceType
        );
    }

    protected <TSBody, TSId, TSCreate, TSFilter extends RequestOptions>
    RestEndpointContinuable<TSBody, TSId, TSCreate, TSFilter> subResourceContinuable(TId id, Class<TSBody> subResourceType){
        return new RestEndpointContinuable<>(
                client(),
                getSubEndpoint(id),
                subResourceType
        );
    }

    /***************************************************************************
     *                                                                         *
     * Private methods                                                         *
     *                                                                         *
     **************************************************************************/

    private String toPath(TId id){
        return id.toString();
    }
}
