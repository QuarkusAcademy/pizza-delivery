package academy.quarkus.pizza.data;

import io.quarkus.runtime.StartupEvent;
import io.restassured.RestAssured;
import io.restassured.config.JsonConfig;
import io.restassured.path.json.config.JsonPathConfig;
import jakarta.enterprise.event.Observes;

public class InitRestAssured {
    public void init(@Observes StartupEvent ev){
        JsonConfig jsonConfig = JsonConfig.jsonConfig()
                .numberReturnType(JsonPathConfig.NumberReturnType.DOUBLE);

        RestAssured.config = RestAssured.config()
                .jsonConfig(jsonConfig);
    }
}
