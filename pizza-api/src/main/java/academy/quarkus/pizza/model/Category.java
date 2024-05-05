package academy.quarkus.pizza.model;

import com.fasterxml.jackson.annotation.JsonIgnore;
import io.quarkus.hibernate.orm.panache.PanacheEntity;
import io.quarkus.panache.common.Sort;
import jakarta.persistence.*;
import jakarta.transaction.Transactional;

import java.math.BigDecimal;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

@Entity
public class Category extends PanacheEntity {
    public String name;

    @Column(precision = 10, scale = 2)
    public BigDecimal price;

    @JsonIgnore
    @ManyToOne
    public Store store;

    @ManyToMany(cascade = {
            CascadeType.MERGE,
            CascadeType.PERSIST
    })
    @JoinTable( name = "pizza_category",
        joinColumns = @JoinColumn(name = "category_id"),
        inverseJoinColumns = @JoinColumn(name = "pizza_id")
    )
    public List<Pizza> pizzas;

    public Category(){}

    @Transactional()
    public static Category persist(Store store, String name, String price){
        var result = new Category();
        result.store = store;
        result.name = name;
        result.price = new BigDecimal(price);
        result.pizzas = new ArrayList<>();
        result.persist();
        return result;
    }

    public void addPizzas(Pizza... ps){
        this.pizzas.addAll(Arrays.asList(ps));
    }

    public static List<Category> listByStore(Store store){
        List<Category> result = list("store", Sort.by("price").ascending(), store);
        return result;
    }

}
