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
import jakarta.ws.rs.GET;
import jakarta.ws.rs.POST;
import jakarta.ws.rs.Path;
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
    public TemplateInstance index() {
        return Templates.index(context, new IndexData(sliderItems));
    }

    @POST
    public void doSendMessage(
            @RestForm String firstName,
            @RestForm String lastName,
            @RestForm String message
    ){
        Log.infof("Message from %s %s: \n %s", firstName, lastName, message);
        index();
    }
}
