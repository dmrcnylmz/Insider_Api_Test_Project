package io.petstore.dto;

import com.fasterxml.jackson.annotation.JsonProperty;
import lombok.*;

import java.util.List;

@Data
@Builder
@AllArgsConstructor
@NoArgsConstructor
@EqualsAndHashCode(exclude = "id")
public class Pet {
    private long id;
    private Category category;
    private String name;
    private List<Tag> tags;
    private List<String> photoUrls;
    private PetStatus status;

    public static Pet ofName(String petName) {
        return Pet.builder().name(petName).build();
    }

    public static Pet ofNameAndStatus(String petName, PetStatus status) {
        return Pet.builder().name(petName).status(status).build();
    }

    @Override
    public String toString() {
        return "{" +
                "id=" + id +
                ", category=" + category +
                ", name='" + name + '\'' +
                ", tags=" + tags +
                ", photoUrls=" + photoUrls +
                ", status=" + status +
                '}';
    }

    public enum PetStatus {
        @JsonProperty("available")
        AVAILABLE("available"),

        @JsonProperty("pending")
        PENDING("pending"),

        @JsonProperty("sold")
        SOLD("sold");

        private String value;

        public String getValue() {
            return value;
        }

        PetStatus(String value) {
            this.value = value;
        }
    }
}
