package cel.dev.restaurants.repository;

import java.util.List;

import cel.dev.restaurants.model.KitchenType;

public interface KitchenTypeDAO {

    void saveKitchenType(KitchenType kitchenType);

    List<KitchenType> getAllFoodTypes();

}
