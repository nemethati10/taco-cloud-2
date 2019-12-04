package tacos.messaging;

import tacos.Order;

public interface OrderMessagingService {

    void sendOrder(final Order order);
}
