package academy.quarkus.pizza.event;

import academy.quarkus.pizza.model.Ticket;

import java.time.LocalDateTime;

public record TicketSubmitted(
        Ticket ticket,
        LocalDateTime submittedAt
) {
}
