package guru.springframework.spring6resttemplate.client;

import guru.springframework.spring6resttemplate.model.BeerDTO;
import guru.springframework.spring6resttemplate.model.BeerStyle;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.data.domain.Page;

import java.math.BigDecimal;

import static org.junit.jupiter.api.Assertions.*;

/**
 * @author surajparitala
 * Date: 3/16/23
 */
@SpringBootTest
class BeerClientImplTest {

    @Autowired
    BeerClientImpl beerClient;
    @Test
    void listBeers() {
        beerClient.listBeers(null, null, null, null, null);
    }

    @Test
    void getBeerById() {
        Page<BeerDTO> beerDTOS = beerClient.listBeers();
        BeerDTO beerDTO = beerDTOS.getContent().get(0);
        BeerDTO beerDTO1= beerClient.getBeerById(beerDTO.getId());
        assertNotNull(beerDTO1);
    }

    @Test
    void testCreateBeer() {
        BeerDTO dto= BeerDTO.builder()
                .price(BigDecimal.valueOf(10.99))
                .beerName("Mango")
                .beerStyle(BeerStyle.IPA)
                .quantityOnHand(500)
                .upc("12345")
                .build();
        BeerDTO savedDTO= beerClient.saveBeer(dto);
        assertNotNull(savedDTO);
    }

    @Test
    void testUpdateBeer() {
        BeerDTO dto= BeerDTO.builder()
                .price(BigDecimal.valueOf(10.99))
                .beerName("Mango")
                .beerStyle(BeerStyle.IPA)
                .quantityOnHand(500)
                .upc("12345")
                .build();
        BeerDTO savedDTO= beerClient.saveBeer(dto);
        assertNotNull(savedDTO);

        final String newName= "Mango Bobs";
        savedDTO.setBeerName(newName);
        BeerDTO updatedBeer= beerClient.updateBeer(savedDTO);

        assertEquals(newName, updatedBeer.getBeerName());
    }

    @Test
    void listBeersWithName() {
        beerClient.listBeers("ALE",null, null, null, null);
    }
}