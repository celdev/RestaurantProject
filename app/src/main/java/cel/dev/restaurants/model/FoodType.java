package cel.dev.restaurants.model;

import android.os.Parcel;
import android.os.Parcelable;

public class FoodType implements Parcelable{

    private final static String EMPTY_NAME = "Unnamed";

    private String name;

    public FoodType(String name) {
        this.name = name;
        if (name == null) {
            this.name = EMPTY_NAME;
        }
    }

    protected FoodType(Parcel in) {
        name = in.readString();
    }

    public static final Creator<FoodType> CREATOR = new Creator<FoodType>() {
        @Override
        public FoodType createFromParcel(Parcel in) {
            return new FoodType(in);
        }

        @Override
        public FoodType[] newArray(int size) {
            return new FoodType[size];
        }
    };

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;

        FoodType foodType = (FoodType) o;

        return name != null ? name.equals(foodType.name) : foodType.name == null;
    }

    @Override
    public String toString() {
        return "FoodType{" +
                "name='" + name + '\'' +
                '}';
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


    @Override
    public void writeToParcel(Parcel dest, int flags) {
        dest.writeString(name);
    }

    @Override
    public int describeContents() {
        return 0;
    }

}
