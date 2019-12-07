package tacos;

import lombok.extern.slf4j.Slf4j;
import org.springframework.boot.CommandLineRunner;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.context.annotation.Profile;
import org.springframework.security.crypto.password.PasswordEncoder;
import tacos.data.IngredientRepository;
import tacos.data.TacoRepository;
import tacos.data.UserRepository;
import tacos.messaging.JmsOrderMessagingService;

import java.util.Arrays;

@Profile("!prod")
@Configuration
@Slf4j
public class DevelopmentConfig {

    @Bean
    public CommandLineRunner dataLoader(final IngredientRepository ingredientRepository,
                                        final UserRepository userRepository,
                                        final PasswordEncoder encoder,
                                        final TacoRepository tacoRepository) {

        return new CommandLineRunner() {
            @Override
            public void run(final String... args) throws Exception {
                final Ingredient flourTortilla = new Ingredient("FLTO", "Flour Tortilla", Ingredient.Type.WRAP);
                final Ingredient cornTortilla = new Ingredient("COTO", "Corn Tortilla", Ingredient.Type.WRAP);
                final Ingredient groundBeef = new Ingredient("GRBF", "Ground Beef", Ingredient.Type.PROTEIN);
                final Ingredient carnitas = new Ingredient("CARN", "Carnitas", Ingredient.Type.PROTEIN);
                final Ingredient tomatoes = new Ingredient("TMTO", "Diced Tomatoes", Ingredient.Type.VEGGIES);
                final Ingredient lettuce = new Ingredient("LETC", "Lettuce", Ingredient.Type.VEGGIES);
                final Ingredient cheddar = new Ingredient("CHED", "Cheddar", Ingredient.Type.CHEESE);
                final Ingredient jack = new Ingredient("JACK", "Monterrey Jack", Ingredient.Type.CHEESE);
                final Ingredient salsa = new Ingredient("SLSA", "Salsa", Ingredient.Type.SAUCE);
                final Ingredient sourCream = new Ingredient("SRCR", "Sour Cream", Ingredient.Type.SAUCE);

                ingredientRepository.save(flourTortilla);
                ingredientRepository.save(cornTortilla);
                ingredientRepository.save(groundBeef);
                ingredientRepository.save(carnitas);
                ingredientRepository.save(tomatoes);
                ingredientRepository.save(lettuce);
                ingredientRepository.save(cheddar);
                ingredientRepository.save(jack);
                ingredientRepository.save(salsa);
                ingredientRepository.save(sourCream);


                userRepository.save(new User("dev", encoder.encode("developer"),
                        "Craig Walls", "123 North Street", "Cross Roads", "TX",
                        "76227", "123-123-1234", "test@gmail.com"));

                final Taco taco1 = new Taco();
                taco1.setName("Carnivore");
                taco1.setIngredients(Arrays.asList(flourTortilla, groundBeef, carnitas, sourCream, salsa, cheddar));
                tacoRepository.save(taco1);

                final Taco taco2 = new Taco();
                taco2.setName("Bovine Bounty");
                taco2.setIngredients(Arrays.asList(cornTortilla, groundBeef, cheddar, jack, sourCream));
                tacoRepository.save(taco2);

                final Taco taco3 = new Taco();
                taco3.setName("Veg-Out");
                taco3.setIngredients(Arrays.asList(flourTortilla, cornTortilla, tomatoes, lettuce, salsa));
                tacoRepository.save(taco3);

            }
        };
    }

    @Profile({"jms-template", "jms-listener"})
    @Bean
    public CommandLineRunner sendOrderWithJms(final JmsOrderMessagingService jmsOrderMessagingService) {

        return new CommandLineRunner() {
            @Override
            public void run(final String... args) throws Exception {
                final Order order = new Order();
                order.setId(999L);
//                order.setPlacedAt(Date.from(Instant.now()));
//                order.setDeliveryZip("555555");
//                order.setDeliveryStreet("Street");
//                order.setDeliveryState("TN");
//                order.setDeliveryCity("TEST CITY");
//                order.setDeliveryName("JMS");
//                order.setCcNumber("342549548976904");
//                order.setCcCVV("456");
//                order.setCcExpiration("12/12");
//                final List<Taco> tacos = new ArrayList<>();
//                final Taco taco = new Taco();
//                taco.setName("Test taco");
//                tacos.add(taco);
//                order.setTacos(tacos);

                jmsOrderMessagingService.sendOrder(order);
                log.info("Order was sent via JMS: " + order);
            }

        };
    }
}
