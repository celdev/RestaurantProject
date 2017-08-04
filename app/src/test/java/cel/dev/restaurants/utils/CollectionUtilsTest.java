package cel.dev.restaurants.utils;

import org.junit.Test;

import java.util.ArrayList;
import java.util.List;

import cel.dev.restaurants.model.KitchenType;

import static org.junit.Assert.*;

public class CollectionUtilsTest {


    @Test
    public void enumToIntArr() throws Exception {
        List<KitchenType> kitchenTypes = new ArrayList<>();
        kitchenTypes.add(KitchenType.Fast);
        kitchenTypes.add(KitchenType.German);
        int[] ints = CollectionUtils.enumToIntArr(kitchenTypes);
        assertArrayEquals(ints, new int[]{KitchenType.Fast.ordinal(), KitchenType.German.ordinal()});
    }

    @Test
    public void intArrToEnum() throws Exception {
        int[] ints = new int[]{KitchenType.Fast.ordinal(), KitchenType.German.ordinal()};
        List<KitchenType> kitchenTypes = new ArrayList<>();
        kitchenTypes.add(KitchenType.Fast);
        kitchenTypes.add(KitchenType.German);
        List<KitchenType> kitchenTypes1 = CollectionUtils.intArrToEnum(ints, KitchenType.class);
        for (int i = 0; i < kitchenTypes.size(); i++) {
            assertEquals(kitchenTypes.get(i),kitchenTypes1.get(i));
        }
    }

}