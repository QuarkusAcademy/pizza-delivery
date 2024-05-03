package academy.quarkus.pizza.dev;

import academy.quarkus.pizza.model.Category;
import academy.quarkus.pizza.model.Pizza;
import academy.quarkus.pizza.model.Store;
import io.quarkus.runtime.LaunchMode;
import io.quarkus.runtime.StartupEvent;
import jakarta.enterprise.event.Observes;
import jakarta.inject.Inject;
import jakarta.transaction.Transactional;

public class SampleDataInit {
    @Inject
    LaunchMode mode;

    @Transactional
    public void init(@Observes StartupEvent ev){
        if (LaunchMode.NORMAL.equals(mode))
            return;
        var shack = Store.persist("Pizza Shack","__default__");

        var trad = Category.persist(shack, "Traditional", "14.99");
        var muss = Pizza.persist("Mozzarella");
        var napo = Pizza.persist("Napolitan");
        var capr = Pizza.persist("Caprese");
        trad.addPizzas(muss, napo, capr);

        var prem = Category.persist(shack, "Premium", "19.99");
        var mush = Pizza.persist("Mushroom");
        var blue = Pizza.persist("Gorgonzola");
        prem.addPizzas(mush, blue);

    }
}
