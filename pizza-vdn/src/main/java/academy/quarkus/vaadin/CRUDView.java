package academy.quarkus.vaadin;

import jakarta.inject.Inject;
import jakarta.transaction.Transactional;

import java.util.List;

import org.vaadin.crudui.crud.impl.GridCrud;

import com.vaadin.flow.component.Key;
import com.vaadin.flow.component.button.Button;
import com.vaadin.flow.component.button.ButtonVariant;
import com.vaadin.flow.component.html.Paragraph;
import com.vaadin.flow.component.orderedlayout.VerticalLayout;
import com.vaadin.flow.component.textfield.TextField;
import com.vaadin.flow.router.Route;

import academy.quarkus.pizza.model.Store;

/**
 * The main view contains a button and a click listener.
 */
@Route("crud")
public class CRUDView extends VerticalLayout {

    public CRUDView() {
       GridCrud<Store> crud = new GridCrud<>(Store.class);
       crud.setFindAllOperation(this::listAll);
       crud.setAddOperation(this::add);
       add(crud);
    }

    public List<Store> listAll(){
        return Store.listAll();
    }

    @Transactional
    public Store add(Store store){
        store.persist();
        return store;
    }
}
