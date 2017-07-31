package cel.dev.restaurants.Model;

public class FoodType {

    private final static String EMPTY_NAME = "Unnamed";

    private String name;

    public FoodType(String name) {
        this.name = name;
        if (name == null) {
            this.name = EMPTY_NAME;
        }
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;

        FoodType foodType = (FoodType) o;

        return name != null ? name.equals(foodType.name) : foodType.name == null;

    }

    @Override
    public int hashCode() {
        return name != null ? name.hashCode() : 0;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }
}
