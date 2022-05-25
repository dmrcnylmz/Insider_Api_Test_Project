package io.petstore.dto;

import com.fasterxml.jackson.annotation.JsonProperty;
import lombok.*;

import java.time.Instant;
import java.util.Date;

@Data
@Builder
@AllArgsConstructor
@NoArgsConstructor
@EqualsAndHashCode(exclude = "id")
public class Order {
    private long id;
    private long petId;
    private long quantity;
    private Instant shipDate;
    private OrderStatus status;
    private boolean complete;

    @Override
    public String toString() {
        return "{" +
                "id=" + id +
                ", petId=" + petId +
                ", quantity=" + quantity +
                ", shipDate=" + shipDate +
                ", status=" + status +
                ", complete=" + complete +
                '}';
    }

    public enum OrderStatus {
        @JsonProperty("placed")
        PLACED("placed"),
        @JsonProperty("approved")
        APPROVED("approved"),
        @JsonProperty("delivered")
        DELIVERED("delivered");

        private String value;

        public String getValue() {
            return value;
        }

        OrderStatus(String value) {
            this.value = value;
        }
    }
}
