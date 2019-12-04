package tacos.web;

import org.springframework.context.annotation.Configuration;

/**
 * By declaring a resource processor bean, however, you can add links to the list of
 * links that Spring Data REST automatically includes.
 */
@Configuration
public class SpringDataRestConfiguration {

    /**
     * if a PagedResources<Resource<Taco>> is returned from a controller, it will receive a
     * link for the most recently created tacos. This includes the response for requests for
     * /api/tacos.
     *
     * @param links
     * @return
     */
/*  @Bean
    public ResourceProcessor<PagedResources<Resource<Taco>>>
    tacoProcessor(final EntityLinks links) {

        return new ResourceProcessor<PagedResources<Resource<Taco>>>() {
            @Override
            public PagedResources<Resource<Taco>> process(PagedResources<Resource<Taco>> resource) {
                resource.add(
                        links.linkFor(Taco.class) //
                                .slash("recent") //
                                .withRel("recents"));
                return resource;
            }
        };
    }
*/
}
