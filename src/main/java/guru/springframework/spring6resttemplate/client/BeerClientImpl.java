package guru.springframework.spring6resttemplate.client;

import guru.springframework.spring6resttemplate.model.BeerDTO;
import org.springframework.data.domain.Page;

/**
 * @author surajparitala
 * Date: 3/16/23
 */
public class BeerClientImpl implements BeerClient {
    @Override
    public Page<BeerDTO> listBeers() {
        return null;
    }
}
