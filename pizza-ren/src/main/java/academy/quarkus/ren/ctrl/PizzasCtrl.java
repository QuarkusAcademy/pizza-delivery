package academy.quarkus.ren.ctrl;

import academy.quarkus.ren.AppConfig;
import academy.quarkus.ren.AppContext;
import academy.quarkus.ren.data.IndexData;
import academy.quarkus.ren.data.SliderItem;
import io.quarkiverse.renarde.Controller;
import io.quarkus.logging.Log;
import io.quarkus.qute.CheckedTemplate;
import io.quarkus.qute.TemplateInstance;
import jakarta.inject.Inject;
import jakarta.validation.constraints.NotBlank;
import jakarta.ws.rs.GET;
import jakarta.ws.rs.POST;
import jakarta.ws.rs.Path;
import jakarta.ws.rs.QueryParam;
import org.hibernate.validator.constraints.Length;
import org.jboss.resteasy.reactive.RestForm;

import java.util.*;


public class PizzasCtrl extends Controller {

    @CheckedTemplate
    static class Templates {
        public static native TemplateInstance index(AppContext context, IndexData data);
    }

    static List<SliderItem> sliderItems = List.of(
            new SliderItem(
                    "Welcome",
                    "We cooked your desired Pizza Recipe",
                    "A small river named Duden flows by their place and supplies it with the necessary regelialia",
                    "Order Now",
                    "View Menu",
                    "images/bg_1.png"
            ),
            new SliderItem(
                    "Crunchy",
                    "Italian Pizza",
                    "A small river named Duden flows by their place and supplies it with the necessary regelialia",
                    "Order Now",
                    "View Menu",
                    "images/bg_2.png"
            ),
            new SliderItem(
                    "Delicious",
                    "Italian Cuizine",
                    "A small river named Duden flows by their place and supplies it with the necessary regelialia",
                    "Order Now",
                    "View Menu",
                    "images/bg_3.jpg"
            )
    );

    @Inject
    AppContext context;


    @Path("/")
    @GET
    public TemplateInstance index(
            @QueryParam("setLocale") String setLocale
    ) {
        if(setLocale != null && setLocale.length() == 2)
            i18n.set(setLocale);
        var greeting = i18n.formatMessage("greeting");
        Log.infof(greeting);
        return Templates.index(context, new IndexData(sliderItems));
    }

    @POST
    public void doSendMessage(
            @NotBlank @Length(min=2, max=255) @RestForm String firstName,
            @NotBlank @Length(min=2, max=255) @RestForm String lastName,
            @NotBlank @Length(min=2, max=255) @RestForm String message
    ){
        // additional validation
        var profanity = message.contains("fuck");
        if(profanity){
            validation.addError("message", "Fala assim não!");
        }
        if (validation.hasErrors()) {
            validation.addError("someError", "Message failed, check your form data.");
        }
        // trust but verify
        if(validationFailed()){
            Log.infof("Validation failed for %s %s: \n %s", firstName, lastName, message);
        }
        Log.infof("Message uala from %s %s: \n %s", firstName, lastName, message);
        flash("flashMessage", "TUDO OK, Obrigado!");
        index(null);
    }
}
