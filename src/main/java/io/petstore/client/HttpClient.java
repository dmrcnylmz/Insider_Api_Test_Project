package io.petstore.client;

import io.petstore.filters.LoggingFilter;
import io.restassured.RestAssured;
import io.restassured.builder.RequestSpecBuilder;
import io.restassured.builder.ResponseSpecBuilder;
import io.restassured.filter.Filter;
import io.restassured.filter.log.LogDetail;
import io.restassured.http.ContentType;
import io.restassured.response.Response;
import io.restassured.specification.RequestSpecification;
import org.springframework.stereotype.Component;

import static io.restassured.RestAssured.given;

@Component
public class HttpClient {

    public HttpClient(LoggingFilter filter) {
        RestAssured.requestSpecification = new RequestSpecBuilder()
                .setAccept(ContentType.JSON)
                .setContentType(ContentType.JSON)
                .setBaseUri(System.getProperty("baseUrl"))
                .build();

        RestAssured.filters(filter);
    }

    public Response doPost(String path, final Object body) {
        return given().body(body).post(path);
    }

    public Response doGet(String path, RequestSpecification spec) {
        return given().spec(spec).get(path);
    }

    public Response doGet(String path) {
        return given().get(path);
    }

    public Response doDelete(String path, RequestSpecification spec) {
        return given().spec(spec).delete(path);
    }

    public Response doDelete(String path) {
        return given().delete(path);
    }

    public Response doPut(String path, final Object body) {
        return given().body(body).post(path);
    }
}
