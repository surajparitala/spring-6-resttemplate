package guru.springframework.spring6resttemplate.config;

import lombok.RequiredArgsConstructor;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.boot.autoconfigure.web.client.RestTemplateBuilderConfigurer;
import org.springframework.boot.web.client.RestTemplateBuilder;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.security.oauth2.client.*;
import org.springframework.security.oauth2.client.registration.ClientRegistrationRepository;
import org.springframework.web.util.DefaultUriBuilderFactory;

/**
 * @author surajparitala
 * Date: 3/17/23
 */
@Configuration
@RequiredArgsConstructor
public class RestTemplateBuilderConfig {

    @Value("${rest.template.rootUrl}")
    String rootUrl;

    private final ClientRegistrationRepository clientRegistrationRepository;
    private final OAuth2AuthorizedClientService oAuth2AuthorizedClientService;

    @Bean
    OAuth2AuthorizedClientManager oAuth2AuthorizedClientManager(){
        OAuth2AuthorizedClientProvider authorizedClientProvider
                = OAuth2AuthorizedClientProviderBuilder.builder().clientCredentials().build();

        AuthorizedClientServiceOAuth2AuthorizedClientManager authorizedClientServiceOAuth2AuthorizedClientManager
                = new AuthorizedClientServiceOAuth2AuthorizedClientManager(clientRegistrationRepository, oAuth2AuthorizedClientService);

        authorizedClientServiceOAuth2AuthorizedClientManager.setAuthorizedClientProvider(authorizedClientProvider);
        return authorizedClientServiceOAuth2AuthorizedClientManager;
    }
    @Bean
    RestTemplateBuilder restTemplateBuilder(RestTemplateBuilderConfigurer configurer,
                                            OAuthClientInterceptor interceptor) {
        assert rootUrl != null;

        return configurer.configure(new RestTemplateBuilder())
                .additionalInterceptors(interceptor)
                .uriTemplateHandler(new DefaultUriBuilderFactory(rootUrl));
    }
}
