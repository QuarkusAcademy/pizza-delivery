package academy.quarkus.pizza.rs;

import academy.quarkus.pizza.model.Delivery;
import jakarta.ws.rs.*;
import jakarta.ws.rs.core.MediaType;

import java.util.Map;

@Path("delivery")
public class DeliveryResource {

    Long getLong(Map<String, Object> params, String key){
        if (! params.containsKey(key))
            throw new BadRequestException("key [%s] not found".formatted(key));
        return ((Number) params.get(key)).longValue();
    }

    Double getDouble(Map<String, Object> params, String key){
        if (! params.containsKey(key))
            throw new BadRequestException("key [%s] not found".formatted(key));
        return ((Number) params.get(key)).doubleValue();
    }

    Integer getInt(Map<String, Object> params, String key){
        if (! params.containsKey(key))
            throw new BadRequestException("key [%s] not found".formatted(key));
        return ((Number) params.get(key)).intValue();
    }

    @POST
    @Produces(MediaType.APPLICATION_JSON)
    @Consumes(MediaType.APPLICATION_JSON)
    public Delivery createDelivery(Map<String, Object> params){
        Long ticketId = getLong(params,"ticketId");
        Long storeId = getLong(params,"storeId");
        Long courierId =  getLong(params,"courierId");
        Delivery delivery = Delivery.persist(ticketId, storeId, courierId);
        return delivery;
    }

    @GET
    @Path("{deliveryId}")
    @Produces(MediaType.APPLICATION_JSON)
    public Delivery readDelivery(@PathParam("deliveryId") Long deliveryId){
        return Delivery.findById(deliveryId);
    }

    @POST
    @Path("{deliveryId}/updateLocation")
    @Produces(MediaType.APPLICATION_JSON)
    public Map<String, Object> updateLocation(@PathParam("deliveryId") Long deliveryId,
                                              Map<String, Object> params){
        Double lat = getDouble(params, "lat");
        Double lon = getDouble(params, "lon");
        Delivery.updateLocation(deliveryId, lat, lon);
        return Map.of("deliveryId","%s".formatted(deliveryId));
    }

    @POST
    @Path("{deliveryId}/updateRating")
    @Produces(MediaType.APPLICATION_JSON)
    public Map<String, Object> updateRating(@PathParam("deliveryId") Long deliveryId,
                                            Map<String, Object> params){
        Integer rating = getInt(params, "rating");
        Delivery.updateRating(deliveryId, rating);
        return Map.of("deliveryId","%s".formatted(deliveryId));
    }
}
