package academy.quarkus.pizza.dev;

import academy.quarkus.pizza.model.Category;
import academy.quarkus.pizza.model.Pizza;
import academy.quarkus.pizza.model.Store;
import io.quarkus.runtime.StartupEvent;
import jakarta.enterprise.event.Observes;
import jakarta.transaction.Transactional;

public class SampleDataInit {
    @Transactional
    public void init(@Observes StartupEvent ev){
        var store = Store.persist("Pizza Shack", "__default__");

        var trad = Category.persist(store, "Traditional", "10.99");
        var marg = Pizza.persist("Marguerita");
        var mush = Pizza.persist("Mushrooms");
        trad.addPizzas(marg, mush);

        var premium = Category.persist(store, "Premium", "14.99");
        var cheeses = Pizza.persist("4 Cheeses");
        var veggies = Pizza.persist("Vegetables");
        var napoles = Pizza.persist("Napoletana");
        premium.addPizzas(cheeses, veggies, napoles);
    }
}
