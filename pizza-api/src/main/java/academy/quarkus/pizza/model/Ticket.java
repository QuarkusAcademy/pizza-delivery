package academy.quarkus.pizza.model;

import io.quarkus.hibernate.orm.panache.PanacheEntity;
import jakarta.persistence.CascadeType;
import jakarta.persistence.Entity;
import jakarta.persistence.ManyToOne;
import jakarta.persistence.OneToMany;
import jakarta.transaction.Transactional;

import java.math.BigDecimal;
import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.List;

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
            orphanRemoval = true
    )
    public List<TicketItem> items = new ArrayList<>();

    public Ticket(){}

    @Transactional
    public static Ticket persist(Person person, String addressMain, String addressDetail){
        var result = new Ticket();
        result.person = person;
        result.addressMain = addressMain;
        result.addressDetail = addressDetail;
        result.persist();
        return result;
    }

    public void addItem(Pizza pizza, BigDecimal price, Integer quantity){
        TicketItem item = TicketItem.persist(this, pizza, price, quantity);
        items.add(item);
    }

    public BigDecimal getValue(){
        var result = items.stream()
                .map(TicketItem::getValue)
                .reduce(BigDecimal.ZERO, BigDecimal::add);
        return result;
    }

}
