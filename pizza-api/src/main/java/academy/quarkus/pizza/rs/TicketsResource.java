package academy.quarkus.pizza.rs;

import java.time.LocalDateTime;
import java.util.Map;

import academy.quarkus.pizza.event.TicketSubmitted;
import academy.quarkus.pizza.model.Ticket;
import academy.quarkus.pizza.model.TicketStatus;
import io.quarkus.runtime.annotations.ConfigDocDefault;
import jakarta.enterprise.event.Event;
import jakarta.inject.Inject;
import jakarta.transaction.Transactional;
import jakarta.ws.rs.BadRequestException;
import jakarta.ws.rs.Consumes;
import jakarta.ws.rs.DELETE;
import jakarta.ws.rs.GET;
import jakarta.ws.rs.NotFoundException;
import jakarta.ws.rs.POST;
import jakarta.ws.rs.PUT;
import jakarta.ws.rs.Path;
import jakarta.ws.rs.PathParam;
import jakarta.ws.rs.Produces;
import jakarta.ws.rs.core.MediaType;

@Path("/tickets")
@Transactional
public class TicketsResource {
    @Inject
    Event<TicketSubmitted> events;

    @POST
    @Produces(MediaType.APPLICATION_JSON)
    @Consumes(MediaType.APPLICATION_JSON)
    // curl -sL -X POST -H 'Content-Type: application/json' -d '{"personId":1, "addressMain":"Rua 1", "addressDetail":"Casa 1", "phone":"123456789"}' http://localhost:8080/api/tickets | jq
    public Ticket createTicket(Map<String, Object> params){
        Long personId = ((Number) params.get("personId")).longValue();
        String addressMain = (String) params.get("addressMain");
        String addressDetail = (String) params.get("addressDetail");
        String phone = (String) params.get("phone");
        Ticket ticket = Ticket.persist(personId, addressMain, addressDetail, phone);
        return ticket;
    }

    @GET
    @Path("/{id}")
    @Produces(MediaType.APPLICATION_JSON)
    // curl -s http://localhost:8080/api/tickets/1 | jq
    public Ticket readTicket(@PathParam("id") Long id){
        return Ticket.findById(id);
    }

    @DELETE
    @Path("/{id}")
    @Produces(MediaType.APPLICATION_JSON)
    // curl -s -X DELETE http://localhost:8080/api/tickets/1 | jq
    public Ticket deleteTicket(Long id){
        Ticket t = readTicket(id);
        t.status = TicketStatus.DELETED;
        return t;
    }

    @PUT
    @Path("/{id}")
    @Produces(MediaType.APPLICATION_JSON)
    // curl -s -X PUT -H "Content-Type: application/json" -d '{"pizzaId": 1, "price":"10.99", "quantity": "2"}' http://localhost:8080/api/tickets/1 | jq
    public Ticket addItem(@PathParam("id") Long id, TicketItemAdd itemAdd){
        Ticket ticket = readTicket(id);
        if (ticket == null){
            throw new NotFoundException("Ticket Not Found");
        }
        if (! TicketStatus.OPEN.equals(ticket.status)){
            throw new BadRequestException("Ticket not open");
        }
        if (itemAdd.quantity().intValue() <= 0 
            || itemAdd.quantity().intValue() >= 99){
            throw new BadRequestException("Invalid quantity");
        }
        ticket.addItem(itemAdd);
        return ticket;
    }

    @POST
    @Path("/{id}/submit")
    // curl -sL -X POST http://localhost:8080/api/tickets/1/submit | jq
    public Ticket submitTicket(Long id){
        Ticket ticket = readTicket(id);
        if (! TicketStatus.OPEN.equals(ticket.status)){
            throw new BadRequestException("Ticket not open");
        }
        ticket.status = TicketStatus.SUBMITTED;
        ticket.persistAndFlush();
        events.fire(new TicketSubmitted(
                ticket,
                LocalDateTime.now()
        ));
        return ticket;
    }
}
