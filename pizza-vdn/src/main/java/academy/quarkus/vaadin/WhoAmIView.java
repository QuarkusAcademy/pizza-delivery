package academy.quarkus.vaadin;

import java.net.URI;

import org.eclipse.microprofile.rest.client.inject.RestClient;

import com.vaadin.flow.component.Key;
import com.vaadin.flow.component.button.Button;
import com.vaadin.flow.component.button.ButtonVariant;
import com.vaadin.flow.component.html.Paragraph;
import com.vaadin.flow.component.orderedlayout.VerticalLayout;
import com.vaadin.flow.router.Route;

import jakarta.annotation.PostConstruct;
import jakarta.inject.Inject;

@Route("whoami")
public class WhoAmIView extends VerticalLayout{
    @Inject
    @RestClient
    WhoAmIClient whoami;
    
    @PostConstruct
    public void init(){
        

        // Button click listeners can be defined as lambda expressions
        Button button = new Button("Who am i?", e -> {
            var user = whoami.get();
            var description = "";
            description +=  "name: " + user.get("name");
            add(new Paragraph(description));
        });

        add(button);
    }
}
