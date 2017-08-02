package cel.dev.restaurants.model;

import android.os.Parcel;
import android.os.Parcelable;

public class KitchenType implements Parcelable{

    private String name;

    public KitchenType(String name) {
        this.name = name;
    }

    protected KitchenType(Parcel in) {
        name = in.readString();
    }

    public static final Creator<KitchenType> CREATOR = new Creator<KitchenType>() {
        @Override
        public KitchenType createFromParcel(Parcel in) {
            return new KitchenType(in);
        }

        @Override
        public KitchenType[] newArray(int size) {
            return new KitchenType[size];
        }
    };

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;

        KitchenType kitchenType = (KitchenType) o;

        return name != null ? name.equals(kitchenType.name) : kitchenType.name == null;
    }

    @Override
    public String toString() {
        return "KitchenType{" +
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
