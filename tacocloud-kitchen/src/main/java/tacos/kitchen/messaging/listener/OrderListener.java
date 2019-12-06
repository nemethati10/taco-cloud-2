package tacos.kitchen.messaging.listener;

import lombok.extern.slf4j.Slf4j;
import org.springframework.context.annotation.Profile;
import org.springframework.jms.annotation.JmsListener;
import org.springframework.stereotype.Component;
import tacos.Order;

@Profile("jms-listener")
@Component
@Slf4j
public class OrderListener {

    @JmsListener(destination = "tacocloud.order.queue")
    public void receiveOrder(final Order order) {
        log.info("RECEIVED ORDER: " + order);
    }
}
