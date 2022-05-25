package io.petstore.filters;

import io.restassured.filter.Filter;
import io.restassured.filter.FilterContext;
import io.restassured.response.Response;
import io.restassured.specification.FilterableRequestSpecification;
import io.restassured.specification.FilterableResponseSpecification;
import lombok.extern.slf4j.Slf4j;
import org.apache.commons.lang3.StringUtils;
import org.springframework.stereotype.Component;

import java.util.concurrent.TimeUnit;

@Slf4j
@Component
public class LoggingFilter implements Filter {

    @Override
    public Response filter(FilterableRequestSpecification reqSpec,
                           FilterableResponseSpecification respSpec,
                           FilterContext ctx) {
        Response response = ctx.next(reqSpec, respSpec);
        log.info("Request: [{} {}]", reqSpec.getMethod(), reqSpec.getURI());

        if (!reqSpec.getMethod().equals("GET") && !StringUtils.isEmpty(reqSpec.getBody())) {
            log.info("Request Body: {}", reqSpec.getBody().toString());
        }

        log.info("Response Status: [{}]", response.getStatusLine());
        log.info("Response Time: [{} milliseconds]", response.timeIn(TimeUnit.MILLISECONDS));

        if (response.getStatusCode() >= 400) {
            log.error("Response Body: {}", response.asString());
        }

        log.info("Response Body: {}", response.asString());
        return response;
    }
}
