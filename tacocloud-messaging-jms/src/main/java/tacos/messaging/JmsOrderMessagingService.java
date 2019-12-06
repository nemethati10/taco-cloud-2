package tacos.messaging;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Bean;
import org.springframework.jms.core.JmsTemplate;
import org.springframework.jms.support.converter.MappingJackson2MessageConverter;
import org.springframework.stereotype.Service;
import tacos.Order;

import javax.jms.JMSException;
import javax.jms.Message;
import java.util.HashMap;
import java.util.Map;

@Service
public class JmsOrderMessagingService implements OrderMessagingService {

    @Autowired
    private JmsTemplate jmsTemplate;

    /**
     * JmsTemplates’s convertAndSend() method simplifies message publication by eliminating
     * the need to provide a MessageCreator. Instead, you pass the object that’s to be
     * sent directly to convertAndSend(), and the object will be converted into a Message
     * before being sent.
     * <p>
     * the Order passed into convertAndSend() is converted into a Message before it’s sent.
     * Under the covers, this is achieved with an implementation of MessageConverter that does the
     * dirty work of converting objects to Messages.
     * <p>
     * By passing in a MessagePostProcessor as the final parameter to convertAnd-
     * Send(), you can do whatever you want with the Message after it has been created.
     *
     * @param order
     */
    @Override
    public void sendOrder(final Order order) {
        this.jmsTemplate.convertAndSend("tacocloud.order.queue", order, this::addOrderSource);
    }

    /**
     * Uses a MessagePostProcessor
     * to add the X_ORDER_SOURCE header before the message is sent
     *
     * @param message
     * @return
     * @throws JMSException
     */
    private Message addOrderSource(final Message message) throws JMSException {
        message.setStringProperty("X_ORDER_SOURCE", "WEB");
        return message;
    }

    /**
     * SimpleMessageConverter is the default, but it requires that the object being sent
     * implement Serializable. This may be a good idea, but you may prefer to use one of
     * the other message converters, such as MappingJackson2MessageConverter, to avoid
     * that restriction.
     * <p>
     * <p>
     * Notice that you called setTypeIdPropertyName() on the MappingJackson2Message-
     * Converter before returning it. This is very important, as it enables the receiver to know
     * what type to convert an incoming message to. By default, it will contain the fully qualified
     * classname of the type being converted. But that’s somewhat inflexible, requiring
     * that the receiver also have the same type, with the same fully qualified classname.
     * <p>
     * To allow for more flexibility, you can map a synthetic type name to the actual
     * type by calling setTypeIdMappings() on the message converter.
     *
     * @return
     */
    @Bean
    public MappingJackson2MessageConverter messageConverter() {
        MappingJackson2MessageConverter messageConverter = new MappingJackson2MessageConverter();
        messageConverter.setTypeIdPropertyName("id");
        final Map<String, Class<?>> typeIdMappings = new HashMap<String, Class<?>>();
        typeIdMappings.put("order", tacos.Order.class);
        messageConverter.setTypeIdMappings(typeIdMappings);

        return messageConverter;
    }


}
