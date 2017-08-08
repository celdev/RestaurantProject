package cel.dev.restaurants.utils;

import org.junit.Test;

import java.util.List;

import cel.dev.restaurants.model.KitchenType;

import static org.junit.Assert.*;

public class DBUtilsTest {


    @Test
    public void enumsToString() throws Exception {
        KitchenType[] kitchenTypes = new KitchenType[]{
                KitchenType.Café,KitchenType.Fast,KitchenType.Japanese
        };
        String s = AndroidUtils.DBUtils.enumsToString(kitchenTypes);
        assertEquals("0,2,5",s);
    }

    @Test
    public void stringToEnum() throws Exception {
        String s = "0,2,5";
        List<KitchenType> kitchenTypes = AndroidUtils.DBUtils.stringToEnum(s, KitchenType.class);
        assertArrayEquals(new KitchenType[]{KitchenType.Café, KitchenType.Fast, KitchenType.Japanese},
                kitchenTypes.toArray(new KitchenType[kitchenTypes.size()])
        );
    }

}