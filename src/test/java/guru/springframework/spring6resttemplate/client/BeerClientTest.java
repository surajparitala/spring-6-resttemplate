package guru.springframework.spring6resttemplate.client;

import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.ObjectMapper;
import guru.springframework.spring6resttemplate.config.RestTemplateBuilderConfig;
import guru.springframework.spring6resttemplate.model.BeerDTO;
import guru.springframework.spring6resttemplate.model.BeerDTOPageImpl;
import guru.springframework.spring6resttemplate.model.BeerStyle;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.mockito.Mock;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.client.RestClientTest;
import org.springframework.boot.test.web.client.MockServerRestTemplateCustomizer;
import org.springframework.boot.web.client.RestTemplateBuilder;
import org.springframework.context.annotation.Import;
import org.springframework.data.domain.Page;
import org.springframework.http.HttpMethod;
import org.springframework.http.MediaType;
import org.springframework.test.web.client.MockRestServiceServer;
import org.springframework.web.client.RestTemplate;
import org.springframework.web.util.UriComponentsBuilder;

import java.math.BigDecimal;
import java.net.URI;
import java.util.Arrays;
import java.util.Collections;
import java.util.UUID;

import static org.assertj.core.api.Assertions.*;
import static org.mockito.Mockito.when;
import static org.springframework.test.web.client.match.MockRestRequestMatchers.*;
import static org.springframework.test.web.client.response.MockRestResponseCreators.*;

/**
 * @author surajparitala
 * Date: 3/22/23
 */
@RestClientTest(BeerClientImpl.class)
@Import(RestTemplateBuilderConfig.class)
class BeerClientTest {
    BeerClient beerClient;
    MockRestServiceServer server;
    @Autowired
    RestTemplateBuilder restTemplateBuilderConfigured;
    @Autowired
    ObjectMapper objectMapper;
    @Mock
    RestTemplateBuilder mockRestTemplateBuilder= new RestTemplateBuilder(new MockServerRestTemplateCustomizer());

    static final String URL= "http://localhost:8080";

    BeerDTO dto;
    String dtoToJson;

    @BeforeEach
    void setUp() throws JsonProcessingException {
        RestTemplate restTemplate= restTemplateBuilderConfigured.build();

        server = MockRestServiceServer.bindTo(restTemplate).build();
        when(mockRestTemplateBuilder.build()).thenReturn(restTemplate);
        beerClient = new BeerClientImpl(mockRestTemplateBuilder);
        dto = getBeerDTO();
        dtoToJson = objectMapper.writeValueAsString(dto);
    }

    @Test
    void testListBeers() throws JsonProcessingException {
        String payload = objectMapper.writeValueAsString(getPage());

        server.expect(method(HttpMethod.GET)).andExpect(requestTo(URL+BeerClientImpl.GET_BEER_PATH))
                .andRespond(withSuccess(payload, MediaType.APPLICATION_JSON));

        Page<BeerDTO> beerDTOS = beerClient.listBeers();
        assertThat(beerDTOS.getContent().size()).isPositive();
    }

    @Test
    void testGetBeerById() {
        mockGetOperation();

        BeerDTO beerById = beerClient.getBeerById(dto.getId());
        assertThat(beerById.getId()).isEqualTo(beerById.getId());
    }

    private void mockGetOperation() {
        server.expect(method(HttpMethod.GET)).andExpect(requestToUriTemplate(URL+BeerClientImpl.GET_BEER_BY_ID, dto.getId()))
                .andRespond(withSuccess(dtoToJson, MediaType.APPLICATION_JSON));
    }

    @Test
    void testSaveBeer() {
        URI uri= UriComponentsBuilder.fromPath(BeerClientImpl.GET_BEER_BY_ID).build(dto.getId());

        server.expect(method(HttpMethod.POST)).andExpect(requestTo(URL+BeerClientImpl.GET_BEER_PATH))
                        .andRespond(withAccepted().location(uri));
        mockGetOperation();

        BeerDTO savedDTO = beerClient.saveBeer(dto);
        assertThat(savedDTO.getId()).isEqualTo(dto.getId());
    }

    @Test
    void testUpdateBeer() {
        server.expect(method(HttpMethod.PUT)).andExpect(requestToUriTemplate(URL+BeerClientImpl.GET_BEER_BY_ID, dto.getId()))
                .andRespond(withNoContent());
        mockGetOperation();

        BeerDTO savedDTO = beerClient.updateBeer(dto);
        assertThat(savedDTO.getId()).isEqualTo(dto.getId());
    }

    @Test
    void testDeleteBeer() {
        server.expect(method(HttpMethod.DELETE)).andExpect(requestToUriTemplate(URL+BeerClientImpl.GET_BEER_BY_ID, dto.getId()))
                .andRespond(withNoContent());

        beerClient.deleteBeer(dto.getId());
        server.verify();
    }

    BeerDTO getBeerDTO() {
        return BeerDTO.builder()
                .id(UUID.randomUUID())
                .price(BigDecimal.valueOf(10.99))
                .beerName("Mango")
                .beerStyle(BeerStyle.IPA)
                .quantityOnHand(500)
                .upc("12345")
                .build();
    }

    BeerDTOPageImpl getPage() {
        return new BeerDTOPageImpl(Collections.singletonList(getBeerDTO()),1,25,1);
    }
}
