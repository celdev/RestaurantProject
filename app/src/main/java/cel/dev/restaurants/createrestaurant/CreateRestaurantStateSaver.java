package cel.dev.restaurants.createrestaurant;

import android.os.Parcel;
import android.os.Parcelable;

public class CreateRestaurantStateSaver implements Parcelable{

    private byte[] image;
    private boolean hasImage;
    private boolean isEditMode;
    private int restaurantId;

    public CreateRestaurantStateSaver(byte[] image, boolean hasImage, boolean isEditMode, int restaurantId) {
        this.image = image;
        this.hasImage = hasImage;
        this.isEditMode = isEditMode;
        this.restaurantId = restaurantId;
    }

    protected CreateRestaurantStateSaver(Parcel in) {
        image = in.createByteArray();
        hasImage = in.readByte() != 0;
        isEditMode = in.readByte() != 0;
        restaurantId = in.readInt();
    }

    @Override
    public void writeToParcel(Parcel dest, int flags) {
        dest.writeByteArray(image);
        dest.writeByte((byte) (hasImage ? 1 : 0));
        dest.writeByte((byte) (isEditMode ? 1 : 0));
        dest.writeInt(restaurantId);
    }

    @Override
    public int describeContents() {
        return 0;
    }

    public static final Creator<CreateRestaurantStateSaver> CREATOR = new Creator<CreateRestaurantStateSaver>() {
        @Override
        public CreateRestaurantStateSaver createFromParcel(Parcel in) {
            return new CreateRestaurantStateSaver(in);
        }

        @Override
        public CreateRestaurantStateSaver[] newArray(int size) {
            return new CreateRestaurantStateSaver[size];
        }
    };
}
