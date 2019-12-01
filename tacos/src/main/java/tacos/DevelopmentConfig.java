package tacos;

import org.springframework.boot.CommandLineRunner;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.context.annotation.Profile;
import org.springframework.security.crypto.password.PasswordEncoder;
import tacos.data.IngredientRepository;
import tacos.data.TacoRepository;
import tacos.data.UserRepository;

import java.util.Arrays;

@Profile("!prod")
@Configuration
public class DevelopmentConfig {

    @Bean
    public CommandLineRunner dataLoader(final IngredientRepository ingredientRepository,
                                        final UserRepository userRepository,
                                        final PasswordEncoder encoder,
                                        final TacoRepository tacoRepository) {

        return new CommandLineRunner() {
            @Override
            public void run(final String... args) throws Exception {
                Ingredient flourTortilla = new Ingredient("FLTO", "Flour Tortilla", Ingredient.Type.WRAP);
                Ingredient cornTortilla = new Ingredient("COTO", "Corn Tortilla", Ingredient.Type.WRAP);
                Ingredient groundBeef = new Ingredient("GRBF", "Ground Beef", Ingredient.Type.PROTEIN);
                Ingredient carnitas = new Ingredient("CARN", "Carnitas", Ingredient.Type.PROTEIN);
                Ingredient tomatoes = new Ingredient("TMTO", "Diced Tomatoes", Ingredient.Type.VEGGIES);
                Ingredient lettuce = new Ingredient("LETC", "Lettuce", Ingredient.Type.VEGGIES);
                Ingredient cheddar = new Ingredient("CHED", "Cheddar", Ingredient.Type.CHEESE);
                Ingredient jack = new Ingredient("JACK", "Monterrey Jack", Ingredient.Type.CHEESE);
                Ingredient salsa = new Ingredient("SLSA", "Salsa", Ingredient.Type.SAUCE);
                Ingredient sourCream = new Ingredient("SRCR", "Sour Cream", Ingredient.Type.SAUCE);

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


                userRepository.save(new User("habuma", encoder.encode("password"),
                        "Craig Walls", "123 North Street", "Cross Roads", "TX",
                        "76227", "123-123-1234"));

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
}
