package academy.quarkus.pizza.model;

import io.quarkus.hibernate.orm.panache.PanacheEntity;
import jakarta.persistence.CascadeType;
import jakarta.persistence.Entity;
import jakarta.persistence.EnumType;
import jakarta.persistence.Enumerated;
import jakarta.persistence.FetchType;
import jakarta.persistence.ManyToOne;
import jakarta.persistence.OneToMany;
import jakarta.transaction.Transactional;

import java.math.BigDecimal;
import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.List;

import academy.quarkus.pizza.rs.TicketItemAdd;

@Entity
public class Ticket extends PanacheEntity {
    public LocalDateTime startedAt;

    @ManyToOne
    public Person person;

    public String phone;
    public String addressMain;
    public String addressDetail;

    @OneToMany(
            mappedBy = "ticket",
            cascade = {
                    CascadeType.MERGE,
                    CascadeType.PERSIST
            },
            orphanRemoval = true,
            fetch = FetchType.EAGER
    )
    public List<TicketItem> items = new ArrayList<>();

    @Enumerated(EnumType.STRING)
    public TicketStatus status;

    public Ticket(){}

    @Transactional
    public static Ticket persist(Long personId, String addressMain, String addressDetail, String phone){
        Person person = Person.findById(personId);
        return persist(person, addressMain, addressDetail, phone);
    }

    @Transactional
    public static Ticket persist(Person person, String addressMain, String addressDetail, String phone){
        var result = new Ticket();
        result.person = person;
        result.addressMain = addressMain;
        result.addressDetail = addressDetail;
        result.phone = phone;
        result.status = TicketStatus.OPEN;
        result.startedAt = LocalDateTime.now();
        result.persist();
        return result;
    }

    public void addItem(Pizza pizza, BigDecimal price, Integer quantity){
        TicketItem item = TicketItem.persist(this, pizza, price, quantity);
        items.add(item);
        item.ticket = this;
    }

    public BigDecimal getValue(){
        var result = items.stream()
                .map(TicketItem::getValue)
                .reduce(BigDecimal.ZERO, BigDecimal::add);
        return result;
    }

    public void addItem(TicketItemAdd itemAdd) {
        Pizza pizza = Pizza.findById(itemAdd.pizzaId());
        addItem(pizza, itemAdd.price(), itemAdd.quantity());
    }

}
