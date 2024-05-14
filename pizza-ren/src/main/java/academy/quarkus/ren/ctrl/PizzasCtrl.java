package academy.quarkus.ren.ctrl;

import academy.quarkus.ren.CtrlContext;
import academy.quarkus.ren.data.IndexData;
import academy.quarkus.ren.data.SliderItem;
import io.quarkiverse.renarde.Controller;
import io.quarkus.qute.CheckedTemplate;
import io.quarkus.qute.TemplateInstance;
import jakarta.inject.Inject;
import jakarta.ws.rs.GET;
import jakarta.ws.rs.Path;

import java.util.*;


public class PizzasCtrl extends Controller {

    @CheckedTemplate
    static class Templates {
        public static native TemplateInstance index(CtrlContext context, IndexData data);
    }

    @Inject
    CtrlContext context;


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

    @Path("/")
    @GET
    public TemplateInstance index() {
        return Templates.index(context, new IndexData(sliderItems));
    }
}
