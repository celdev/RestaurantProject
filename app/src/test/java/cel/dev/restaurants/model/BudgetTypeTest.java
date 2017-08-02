package cel.dev.restaurants.model;

import org.junit.Test;

import java.util.Arrays;

import static org.junit.Assert.*;

public class BudgetTypeTest {

    @Test
    public void testSortBudgetType() {
        BudgetType[] expecteds = {BudgetType.CHEAP, BudgetType.NORMAL, BudgetType.EXPENSIVE, BudgetType.VERY_EXPENSIVE};
        BudgetType[] toSort = {BudgetType.VERY_EXPENSIVE, BudgetType.EXPENSIVE, BudgetType.CHEAP, BudgetType.NORMAL};
        assertArrayEquals(expecteds,
                BudgetType.sortBudgetType(toSort)
        );
    }

    @Test
    public void testSortBudgetType3() {
        assertArrayEquals(new BudgetType[]{BudgetType.CHEAP, BudgetType.EXPENSIVE},
                BudgetType.sortBudgetType(new BudgetType[]{BudgetType.EXPENSIVE, BudgetType.CHEAP})
        );
    }

    @Test(expected = AssertionError.class)
    public void testFail() {
        assertArrayEquals(new BudgetType[]{BudgetType.NORMAL, BudgetType.EXPENSIVE},
                BudgetType.sortBudgetType(new BudgetType[]{BudgetType.EXPENSIVE, BudgetType.CHEAP})
        );
    }

}