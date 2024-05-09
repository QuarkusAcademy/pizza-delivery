package academy.quarkus.pizza.event;

import io.quarkus.logging.Log;
import jakarta.enterprise.event.Observes;
import jakarta.enterprise.event.ObservesAsync;

public class NotificationHandler {

    public void onTicketSubmitted(@ObservesAsync TicketSubmitted ev){
        Log.infof("Send notifications [%s] at [%s]", ev.ticket().id, ev.submittedAt());
    }

}
