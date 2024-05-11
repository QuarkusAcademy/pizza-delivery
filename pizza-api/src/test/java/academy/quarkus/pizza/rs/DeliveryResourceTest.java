package academy.quarkus.pizza.rs;

import academy.quarkus.pizza.model.Delivery;
import io.quarkus.logging.Log;
import io.quarkus.test.junit.QuarkusTest;
import jakarta.inject.Inject;
import org.junit.jupiter.api.Test;

import java.util.Map;

import static org.junit.jupiter.api.Assertions.*;
import static io.restassured.RestAssured.*;
import static org.hamcrest.Matchers.*;

@QuarkusTest
class DeliveryResourceTest {
    @Inject
    Delivery delivery;

    @Test
    public void testUpdateLocation() {
        Log.infof("Testing update location for delivery %s", delivery.id);
        // Send location update
        Double lat = -23.6505D, lon = -46.7333D;
        given()
                .pathParam("deliveryId", delivery.id)
                .body(Map.of(
                        "lat", lat,
                        "lon", lon
                ))
                .contentType("application/json")
                .when()
                .post("/delivery/{deliveryId}/updateLocation")
                .then()
                .statusCode(200);
        // Check if location updated
        given()
                .pathParam("deliveryId", delivery.id)
                .contentType("application/json")
                .when()
                .get("/delivery/{deliveryId}")
                .then()
                .statusCode(200)
                .body("currentLocation.lat", closeTo(lat, 0.0))
                .body("currentLocation.lon", closeTo(lon, 0.0));
    }
}