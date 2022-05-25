package io.petstore.dto;

import lombok.*;

@Data
@AllArgsConstructor
@NoArgsConstructor
@EqualsAndHashCode(exclude = "id")
@Builder
public class Category {
    private long id;
    private String name;

    public static Category ofName(String name) {
        return Category.builder().name(name).build();
    }

    @Override
    public String toString() {
        return "{" +
                "id=" + id +
                ", name='" + name + '\'' +
                '}';
    }
}
