package cel.dev.restaurants.repository;

import java.util.ArrayList;
import java.util.List;

import cel.dev.restaurants.model.KitchenType;

public class KitchenTypeDAOImpl implements KitchenTypeDAO {

    private static List<KitchenType> kitchenTypes = new ArrayList<>();

    static {
        kitchenTypes.add(new KitchenType("Café"));
        kitchenTypes.add(new KitchenType("Chinese"));
        kitchenTypes.add(new KitchenType("Fast food"));
        kitchenTypes.add(new KitchenType("German"));
        kitchenTypes.add(new KitchenType("Italian"));
        kitchenTypes.add(new KitchenType("Japanese"));
        kitchenTypes.add(new KitchenType("Market"));
        kitchenTypes.add(new KitchenType("Mexican"));
        kitchenTypes.add(new KitchenType("Pizza"));
        kitchenTypes.add(new KitchenType("Thai"));
        kitchenTypes.add(new KitchenType("Steak"));
        kitchenTypes.add(new KitchenType("Swedish"));

    }

    @Override
    public void saveKitchenType(KitchenType kitchenType) {
        if (!kitchenTypes.contains(kitchenType)) {
            kitchenTypes.add(kitchenType);
        }
    }

    @Override
    public List<KitchenType> getAllFoodTypes() {
        return kitchenTypes;
    }
}
