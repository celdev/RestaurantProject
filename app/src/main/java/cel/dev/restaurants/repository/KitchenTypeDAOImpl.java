package cel.dev.restaurants.repository;

import java.util.Arrays;
import java.util.List;

import cel.dev.restaurants.model.KitchenType;

public class KitchenTypeDAOImpl implements KitchenTypeDAO {

    @Override
    public List<KitchenType> getAllFoodTypes() {
        return Arrays.asList(KitchenType.values());
    }

}
