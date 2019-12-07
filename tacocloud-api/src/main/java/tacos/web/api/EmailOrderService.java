package tacos.web.api;

import org.springframework.stereotype.Service;
import tacos.*;
import tacos.data.IngredientRepository;
import tacos.data.PaymentMethodRepository;
import tacos.data.UserRepository;

import java.util.ArrayList;
import java.util.Date;
import java.util.List;
import java.util.Optional;

@Service
public class EmailOrderService {
    private UserRepository userRepository;
    private IngredientRepository ingredientRepository;
    private PaymentMethodRepository paymentMethodRepository;

    public EmailOrderService(final UserRepository userRepository, final IngredientRepository ingredientRepository,
                             final PaymentMethodRepository paymentMethodRepository) {
        this.userRepository = userRepository;
        this.ingredientRepository = ingredientRepository;
        this.paymentMethodRepository = paymentMethodRepository;
    }

    public Order convertEmailOrderToDomainOrder(final EmailOrder emailOrder) {
        // TODO: Probably should handle unhappy case where email address doesn't match a given user or
        //       where the user doesn't have at least one payment method.
        final User user = userRepository.findByEmail(emailOrder.getEmail());
        final PaymentMethod paymentMethod = paymentMethodRepository.findByUserId(user.getId());

        final Order order = createOrder(user, paymentMethod);

        // TODO: Handle unhappy case where a given ingredient doesn't match
        final List<EmailOrder.EmailTaco> emailTacos = emailOrder.getTacos();
        for (final EmailOrder.EmailTaco emailTaco : emailTacos) {
            final Taco taco = new Taco();
            taco.setName(emailTaco.getName());
            final List<String> ingredientIds = emailTaco.getIngredients();
            final List<Ingredient> ingredients = createIngredients(ingredientIds);
            taco.setIngredients(ingredients);
            order.addDesign(taco);
        }

        return order;
    }

    private List<Ingredient> createIngredients(List<String> ingredientIds) {
        final List<Ingredient> ingredients = new ArrayList<>();
        for (final String ingredientId : ingredientIds) {
            final Optional<Ingredient> optionalIngredient = ingredientRepository.findById(ingredientId);
            if (optionalIngredient.isPresent()) {
                ingredients.add(optionalIngredient.get());
            }
        }

        return ingredients;
    }

    private Order createOrder(final User user, final PaymentMethod paymentMethod) {
        final Order order = new Order();
        order.setUser(user);
        order.setCcNumber(paymentMethod.getCcNumber());
        order.setCcCVV(paymentMethod.getCcCVV());
        order.setCcExpiration(paymentMethod.getCcExpiration());
        order.setDeliveryName(user.getFullname());
        order.setDeliveryStreet(user.getStreet());
        order.setDeliveryCity(user.getCity());
        order.setDeliveryState(user.getState());
        order.setDeliveryZip(user.getZip());
        order.setPlacedAt(new Date());

        return order;

    }
}
