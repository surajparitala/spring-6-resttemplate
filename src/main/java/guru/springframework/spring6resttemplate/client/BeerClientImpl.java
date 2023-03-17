package guru.springframework.spring6resttemplate.client;

import guru.springframework.spring6resttemplate.model.BeerDTO;
import guru.springframework.spring6resttemplate.model.BeerDTOPageImpl;
import lombok.RequiredArgsConstructor;
import org.springframework.boot.web.client.RestTemplateBuilder;
import org.springframework.data.domain.Page;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Service;
import org.springframework.web.client.RestTemplate;

/**
 * @author surajparitala
 * Date: 3/16/23
 */
@Service
@RequiredArgsConstructor
public class BeerClientImpl implements BeerClient {
    private final RestTemplateBuilder restTemplateBuilder;

    private static final String BASE_URL="http://localhost:8080";
    private static final String GET_BEER_PATH="/api/v1/beer";

    @Override
    public Page<BeerDTO> listBeers() {
        RestTemplate restTemplate= restTemplateBuilder.build();

//        ResponseEntity<String> stringResponse =
//                restTemplate.getForEntity("http://localhost:8080/api/v1/beer", String.class);

            ResponseEntity<BeerDTOPageImpl> pageResponseEntity=
                    restTemplate.getForEntity(BASE_URL+GET_BEER_PATH, BeerDTOPageImpl.class);
        return null;
    }
}
