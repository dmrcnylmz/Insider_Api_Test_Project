package io.petstore.service;

import io.petstore.client.HttpClient;
import io.petstore.dto.Pet;
import io.restassured.http.ContentType;
import io.restassured.response.Response;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import java.io.File;
import java.util.HashMap;
import java.util.Map;
import java.util.stream.Stream;

import static io.restassured.RestAssured.given;

@Component
public class PetService {
    private static final String PET_PATH = "/pet";
    private static final String PET_ID_PATH = "/pet/{id}";
    private static final String FIND_PET_BY_STATUS_PATH = "/pet/findByStatus";
    private static final String UPLOAD_IMAGE_PATH = "/pet/{id}/uploadImage";


    @Autowired
    private HttpClient httpClient;

    public Response addPet(final Pet petToAdd) {
        return httpClient.doPost(PET_PATH, petToAdd);
    }

    public Response findPetsByStatus(Pet.PetStatus... statuses) {
        Map<String, Object> queryParams = new HashMap<>();
        Stream.of(statuses)
                .forEach(status -> queryParams.put("status", status.getValue()));
        return httpClient.doGet(FIND_PET_BY_STATUS_PATH, given().queryParams(queryParams));
    }

    public Response findPetById(long petId) {
        return httpClient.doGet(PET_ID_PATH, given().pathParam("id", petId));
    }

    public Response deletePetById(long petId) {
        return httpClient.doDelete(PET_ID_PATH, given().pathParam("id", petId));
    }

    public Response uploadImageByPetId(long petId, String pathToImage, String additionalMetadata) {
        return httpClient.doPost(UPLOAD_IMAGE_PATH, given()
                .contentType("multipart/form-data")
                .multiPart(new File(pathToImage))
                .multiPart("additionalMetadata", additionalMetadata)
                .pathParam("id", petId)
        );
    }

    public Response updatePetNameAndStatusById(long petId, String petName, Pet.PetStatus status) {
        return httpClient.doPost(PET_ID_PATH, given()
                .contentType(ContentType.URLENC)
                .formParam("name", petName)
                .formParam("status", status.getValue())
                .pathParam("id", petId)
        );
    }
}
