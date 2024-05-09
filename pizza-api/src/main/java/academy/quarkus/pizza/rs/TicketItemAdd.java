package academy.quarkus.pizza.rs;

import java.math.BigDecimal;

public record TicketItemAdd(
    Long pizzaId,
    BigDecimal price,
    Integer quantity
) {

}
