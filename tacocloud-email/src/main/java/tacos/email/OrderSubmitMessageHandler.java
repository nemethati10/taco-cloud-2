package tacos.email;

import org.springframework.integration.handler.GenericHandler;
import org.springframework.stereotype.Component;
import org.springframework.web.client.RestTemplate;
import tacos.email.domain.Order;

import java.util.Map;

@Component
public class OrderSubmitMessageHandler implements GenericHandler<Order> {

    private RestTemplate restTemplate;
    private ApiProperties apiProperties;

    public OrderSubmitMessageHandler(final ApiProperties apiProperties, final RestTemplate restTemplate) {
        this.apiProperties = apiProperties;
        this.restTemplate = restTemplate;
    }

    @Override
    public Object handle(final Order order, final Map<String, Object> headers) {
        restTemplate.postForObject(apiProperties.getUrl(), order, String.class);
        return null;
    }
}
