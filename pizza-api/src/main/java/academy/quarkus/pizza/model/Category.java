package academy.quarkus.pizza.model;

import io.quarkus.hibernate.orm.panache.PanacheEntity;
import io.quarkus.panache.common.Sort;
import jakarta.persistence.*;

import java.math.BigDecimal;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

// Description
@Entity
public class Category extends PanacheEntity {
    public String name;

    @Column(precision = 10, scale = 2)
    public BigDecimal price;

    @ManyToOne
    Store store;

    @ManyToMany(cascade = {
            CascadeType.PERSIST,
            CascadeType.MERGE
    })
    @JoinTable(name = "pizza_category",
        joinColumns = @JoinColumn( name = "category_id" ),
        inverseJoinColumns = @JoinColumn (name = "pizza_id"))
    public List<Pizza> pizzas;

    public Category(){}

    public static Category persist(Store store, String name, String price){
        var result = new Category();
        result.store = store;
        result.name = name;
        result.price = new BigDecimal(price);
        result.pizzas = new ArrayList<>();
        result.persist();
        return result;
    }

    public void addPizzas(Pizza... ps) {
        pizzas.addAll(Arrays.asList(ps));
    }

    public static List<Category> listByStore(Store store){
        List<Category> result = list("store", Sort.by("name"), store);
        return result;
    }
}
