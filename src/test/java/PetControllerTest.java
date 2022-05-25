import io.petstore.dto.Category;
import io.petstore.dto.Pet;
import io.petstore.dto.Tag;
import io.restassured.response.Response;
import org.apache.http.HttpStatus;
import org.testng.annotations.Test;

import java.util.List;

import static java.util.Collections.singletonList;
import static org.assertj.core.api.Assertions.assertThat;
import static org.hamcrest.CoreMatchers.equalTo;
import static org.hamcrest.core.StringContains.containsString;

public class PetControllerTest extends BaseTest {
    private String PATH_TO_IMAGE = "src/main/resources/petImage.jpg";

    @Test
    public void shouldAddNewPet() {
        Category category = Category.ofName("home_pet");
        List<Tag> tag = singletonList(Tag.ofName("tag1"));
        String petName = "Richard";
        Pet petToAdd = Pet.builder()
                .name(petName)
                .status(Pet.PetStatus.SOLD)
                .tags(tag)
                .category(category)
                .photoUrls(singletonList("test"))
                .build();

        long addedPetId = successCreatePet(petToAdd).getId();
        Pet addedPet = petService.findPetById(addedPetId).as(Pet.class);

        assertThat(addedPet).as("Should return Pet equal to added one.").isEqualTo(petToAdd);
    }

    @Test
    public void shouldUpdatePetNameAndStatusById() {
        Pet petToAdd = Pet.ofNameAndStatus("Max", Pet.PetStatus.AVAILABLE);
        Pet addedPet = successCreatePet(petToAdd);

        String updatedName = addedPet.getName() + "_UPDATED";

        petService.updatePetNameAndStatusById(addedPet.getId(), updatedName, Pet.PetStatus.SOLD)
                .then()
                .statusCode(200);

        petService.findPetById(addedPet.getId())
                .then()
                .statusCode(200)
                .body("name", equalTo(updatedName))
                .body("status", equalTo(Pet.PetStatus.SOLD.getValue()));
    }

    @Test
    public void shouldReturnNotFound_whenUpdatingNonExistingPet() {
        Pet petToAdd = Pet.ofName("DummyPet");
        Pet addedPet = successCreatePet(petToAdd);

        petService.deletePetById(addedPet.getId())
                .then()
                .statusCode(200);

        petService.updatePetNameAndStatusById(addedPet.getId(), "Cat", Pet.PetStatus.SOLD)
                .then()
                .statusCode(HttpStatus.SC_NOT_FOUND)
                .body("message", equalTo("not found"));
    }

    @Test
    public void shouldReturnNotFound_whenFindingDeletedPetById() {
        Pet petToAdd = Pet.ofName("DummyPet");
        Pet addedPet = successCreatePet(petToAdd);

        petService.deletePetById(addedPet.getId())
                .then()
                .statusCode(200);

        petService.findPetById(addedPet.getId())
                .then()
                .statusCode(HttpStatus.SC_NOT_FOUND);
    }

    @Test
    public void shouldReturnSoldPetsByStatus() {
        Pet petToAdd = Pet.builder()
                .status(Pet.PetStatus.SOLD)
                .name("Rich")
                .build();
        successCreatePet(petToAdd);

        List<Pet> pets = petService.findPetsByStatus(Pet.PetStatus.SOLD)
                .jsonPath()
                .getList(".", Pet.class);
        assertThat(pets).hasSizeGreaterThan(0);
        for (Pet pet : pets) {
            assertThat(pet.getStatus()).isEqualTo(Pet.PetStatus.SOLD);
        }
    }

    @Test
    public void shouldReturnSoldAndPendingPetsByStatus() {
        Response response = petService.findPetsByStatus(Pet.PetStatus.SOLD, Pet.PetStatus.PENDING);
        List<Pet> pets = response.jsonPath().getList(".", Pet.class);
        assertThat(pets)
                .extracting("status")
                .containsAnyOf(Pet.PetStatus.SOLD, Pet.PetStatus.PENDING);
    }

    @Test
    public void shouldUploadPetsImageByPetId() {
        String additionalData = "FavouritePicture";
        petService.uploadImageByPetId(1L, PATH_TO_IMAGE, additionalData)
                .then()
                .statusCode(200)
                .body("message", containsString(additionalData));
    }

    @Test
    public void shouldReturnError_whenUploadingNotAnImage() {
        Pet petToAdd = Pet.ofName("addedPet");
        long id = petService.addPet(petToAdd)
                .then()
                .statusCode(200)
                .extract()
                .body()
                .jsonPath()
                .getLong("id");

        String pathToPdf = "src/main/resources/pdf-sample.pdf";
        Response response = petService.uploadImageByPetId(id, pathToPdf, "");
        assertThat(response.getStatusCode())
                .as("Upload not an image response code")
                .isEqualTo(HttpStatus.SC_BAD_REQUEST);
    }

    /**
     * Expect that we can't load image for non existing pet
     * 404 Not Found
     */
    @Test
    public void shouldReturnNotFound_whenUploadImageForNonExistingPet() {
        Pet petToAdd = Pet.ofName("DummyPet");
        Pet addedPet = successCreatePet(petToAdd);

        petService.deletePetById(addedPet.getId())
                .then()
                .statusCode(200);

        Response response = petService.uploadImageByPetId(addedPet.getId(), PATH_TO_IMAGE, "");
        assertThat(response.getStatusCode())
                .as("Upload image for non existing pet response code.")
                .isEqualTo(HttpStatus.SC_NOT_FOUND);
    }

    /**
     * 1. Upload image for pet by pet id
     * 2. Get pet by id
     * 3. Сheck if pet has photoUrl
     */
    @Test
    public void petShouldContainsImageUrlAfterImageUploading() {
        Pet petToAdd = Pet.ofName("PetWithPhoto");
        long id = petService.addPet(petToAdd)
                .then()
                .statusCode(200)
                .extract()
                .body()
                .jsonPath()
                .getLong("id");

        petService.uploadImageByPetId(id, PATH_TO_IMAGE, "")
                .then()
                .statusCode(200);

        Pet pet = petService.findPetById(id).as(Pet.class);
        assertThat(pet.getPhotoUrls()).isNotEmpty();
    }
}

