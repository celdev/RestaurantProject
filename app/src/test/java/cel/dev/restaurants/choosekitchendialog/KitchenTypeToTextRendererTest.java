package cel.dev.restaurants.choosekitchendialog;

import org.junit.Test;

import java.util.ArrayList;
import java.util.List;

import cel.dev.restaurants.model.KitchenType;

import static org.junit.Assert.*;

public class KitchenTypeToTextRendererTest {


    @Test
    public void foodTypesToString() throws Exception {
        List<KitchenType> kitchenTypes = new ArrayList<>();
        kitchenTypes.add(new KitchenType("Thai"));
        kitchenTypes.add(new KitchenType("Swedish"));
        kitchenTypes.add(new KitchenType("German"));
        kitchenTypes.add(new KitchenType("Japan"));
        assertEquals(FoodTypeToTextRenderer.foodTypesToString(kitchenTypes), "Thai, Swedish, German, Japan");
    }

}