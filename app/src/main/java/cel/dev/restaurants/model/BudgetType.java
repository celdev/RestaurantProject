package cel.dev.restaurants.model;

import android.support.annotation.StringRes;

import java.util.Arrays;
import java.util.Collection;
import java.util.Comparator;
import java.util.HashSet;
import java.util.Set;

import cel.dev.restaurants.R;

/** This enum represents four differnt types of budgets which a restaurant can have
 *
 *  Also contains some helper methods for sorting and translating a budget type into a String
 * */
public enum BudgetType {

    CHEAP(0), NORMAL(1), EXPENSIVE(2), VERY_EXPENSIVE(3);


    private final int order;

    BudgetType(int order) {

        this.order = order;
    }

    /** Converts a BudgetType to a @StringRes int value
     * */
    @StringRes
    public static Integer budgetTypeToString(BudgetType budgetType) {
        switch (budgetType) {
            case CHEAP:
                return R.string.budget_cheap;
            case NORMAL:
                return R.string.budget_normal;
            case EXPENSIVE:
                return R.string.budget_expensive;
            case VERY_EXPENSIVE:
                return R.string.budget_very_expensive;
        }
        throw new RuntimeException("IllegalBudgetType");
    }

    /** Sorts the BudgetType in the order CHEAP, NORMAL, EXPENSIVE, VERY_EXPENSIVE
     * */
    public static BudgetType[] sortBudgetType(BudgetType[] toSort) {
        Arrays.sort(toSort, new Comparator<BudgetType>() {
            @Override
            public int compare(BudgetType o1, BudgetType o2) {
                return o1.order - o2.order;
            }
        });
        return toSort;
    }

}
