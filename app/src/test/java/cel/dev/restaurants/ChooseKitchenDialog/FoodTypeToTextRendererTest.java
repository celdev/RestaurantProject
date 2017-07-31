package cel.dev.restaurants.ChooseKitchenDialog;

import org.junit.Test;

import java.util.ArrayList;
import java.util.List;

import cel.dev.restaurants.Model.FoodType;

import static org.junit.Assert.*;

public class FoodTypeToTextRendererTest {


    @Test
    public void foodTypesToString() throws Exception {
        List<FoodType> foodTypes = new ArrayList<>();
        foodTypes.add(new FoodType("Thai"));
        foodTypes.add(new FoodType("Swedish"));
        foodTypes.add(new FoodType("German"));
        foodTypes.add(new FoodType("Japan"));
        assertEquals(FoodTypeToTextRenderer.foodTypesToString(foodTypes), "Thai, Swedish, German, Japan");
    }

}