package hiperium.city.functions.common.requests;

import java.util.Map;

/**
 * Represents a request sent through AWS API Gateway.
 * This record encapsulates the headers, request context, and body of the request.
 *
 * @param headers        A map of HTTP headers included in the request.
 * @param requestContext The context of the request, providing metadata such as account ID,
 *                       route key, stage, request ID, and additional HTTP information.
 * @param body           The raw body of the incoming API Gateway request.
 */
public record FunctionRequest(
    Map<String, String> headers,
    RequestContext requestContext,
    String body
) {

    /**
     * Represents the context of a request sent through an API Gateway.
     * This record contains metadata related to the request, such as account ID,
     * API information, HTTP details, and other contextual data.
     *
     * @param accountId The AWS account ID associated with the request.
     * @param apiId The identifier of the API in which the request was made.
     * @param http HTTP-related metadata about the request, such as path, protocol, and source IP.
     * @param requestId A unique identifier for the specific request.
     * @param routeKey The route key that identifies the integration configuration.
     * @param stage The stage of the API Gateway where the request was routed.
     * @param time The timestamp when the request was received, as a formatted string.
     */
    public record RequestContext(
        String accountId,
        String apiId,
        Http http,
        String requestId,
        String routeKey,
        String stage,
        String time
    ) {

        /**
         * This record provides information about the HTTP request context, such as
         * the request path, protocol, and identifying details about the source of the
         * request, including the source IP address and user agent string.
         *
         * @param path The path of the HTTP request.
         * @param protocol The protocol used for the HTTP request (e.g., HTTP/1.1).
         * @param sourceIp The IP address of the request's source.
         * @param userAgent The user agent string from the HTTP request header, providing details
         *                  about the client making the request.
         */
        public record Http(
            String path,
            String protocol,
            String sourceIp,
            String userAgent
        ) {
        }
    }
}
