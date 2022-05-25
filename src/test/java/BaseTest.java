import io.petstore.dto.Pet;
import io.petstore.service.PetService;
import org.apache.http.HttpStatus;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.test.context.ContextConfiguration;
import org.springframework.test.context.testng.AbstractTestNGSpringContextTests;

@ContextConfiguration(locations = "classpath:context.xml")
public class BaseTest extends AbstractTestNGSpringContextTests {
    @Autowired
    protected PetStoreService petStoreService;

    @Autowired
    protected PetService petService;

    protected Pet successCreatePet(Pet petToAdd) {
        return petService.addPet(petToAdd)
                .then()
                .statusCode(HttpStatus.SC_OK)
                .extract()
                .as(Pet.class);
    }
}
