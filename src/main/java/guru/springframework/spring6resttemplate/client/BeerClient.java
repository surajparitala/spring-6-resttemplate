package guru.springframework.spring6resttemplate.client;

import guru.springframework.spring6resttemplate.model.BeerDTO;
import guru.springframework.spring6resttemplate.model.BeerStyle;
import org.springframework.data.domain.Page;

import java.util.UUID;

/**
 * @author surajparitala
 * Date: 3/16/23
 */
public interface BeerClient {
        Page<BeerDTO> listBeers();
        Page<BeerDTO> listBeers(String beerName, BeerStyle beerStyle, Boolean showInventory, Integer pageNumber,
                                Integer pageSize);

        BeerDTO getBeerById(UUID beerId);

        BeerDTO saveBeer(BeerDTO beerDTO);

        BeerDTO updateBeer(BeerDTO savedDTO);

        void deleteBeer(UUID beerId);
}
