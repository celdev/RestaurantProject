package cel.dev.restaurants.uicontracts;

import android.content.Context;
import android.content.Intent;
import android.graphics.Bitmap;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.support.annotation.StringRes;
import android.widget.ImageView;

import java.util.List;

import cel.dev.restaurants.model.BudgetType;
import cel.dev.restaurants.model.KitchenType;
import cel.dev.restaurants.model.Restaurant;

/** "M"VP for this create restaurant use case
 *  The "M"/repository part is a bit unnecessary
 *  since this information can be retrieved
 *  from the RestaurantDao
 *
 * */
public interface CreateRestaurantMVP {



    interface View {

        /** Requests the location permissions
         * */
        void requestLocationPermission();

        /** returns true if the application has location permissions
         * */
        boolean checkHasLocationPermission();

        /** Shows the choose kitchen dialog
         * */
        void showSelectKitchenDialog();

        /** Updates the text of the chosen kitchen types
         * */
        void updateChosenKitchens(List<KitchenType> kitchenTypes);

        /** Getters for the information store in the views of the application
         * */
        Bitmap getRestaurantImage();
        String getRestaurantName();
        float getRestaurantRating();
        /** converts checked checkboxes into BudgetType objects
         *  if no budgettype is chosen null will be returned
         * */
        BudgetType[] getSelectedBudgetTypes();
        Double[] getPosition();

        /** Creates and shows a dialog containing the error message
         * */
        void createRestaurantError(int validationMessage);

        /** Called when the creation and saving of the restaurant has finished successfully
         *  Finishes this application
         * */
        void createRestaurantOk();

        /** Shows the String with the @StringRes id of the parameter as a message in a Toast
         * */
        void showError(@StringRes int errorResCode);

        /** This method will take the information in the restaurant object (name, image, budet etc)
         *  and update the view states accordingly
         *  Is used in the Edit-restaurant-mode
         * */
        void injectInformationToViews(@NonNull Restaurant restaurant);

    }




    interface Presenter {

        /** Tries to create the restaurant using the current information stored in the view and the repository
         *  if the creation is successful (all information needed was provided) then the restaurant will be
         *  saved. If this is successful then the createRestaurantOk will be called
         * */
        void onCreateRestaurantPressed();

        /** This method calls the view to show the chose kitchen dialog
         * */
        void onChooseKitchenBtnPressed();

        /** passes the kitchen types and it's new chosen state to the repository
         * */
        void chooseFoodType(KitchenType kitchenType, boolean chosen);

        /** Returns the chosen kitchen types
         * */
        List<KitchenType> getChosenKitchen();

        /** Tries to retrieve an address of the position passed as a parameter
         *
         *  The method should return a String, either the address of the position or
         *  a message saying that the position was retrieved
         */
         String getLocationStringFromLatLng(Context context, double latitude, double longitude);

        /** Checks if the Intent contains information about an restaurant to edit, if so then
         *  the mode of this activity should be to edit a restaurant.
         *
         *  This method tries to retrieve the restaurant in order to fill the view with its information
         *
         *  return true if the intent contains information about a restaurant to edit
         * */
        boolean getIsEditRestaurantMode(Intent intent, Context context);

        /** Retrieves the image of the restaurant and adds it onto the ImageView
         * */
        void injectImageOntoDrawable(ImageView imageView, Restaurant restaurant);


        /** This method allows the presenter to call functions that needs to be called
         *  when the activity of this presenter is being destroyed
         *
         *  Calls the repository to close the database
         * */
        void onCloseActivity();
    }

    interface Repository {

        /** Returns the all KitchenTypes as a list
         * */
        List<KitchenType> getKitchenTypes();

        /** return the chosen kitchen types
         * */
        List<KitchenType> chosenFoodTypes();


        /** If chosen is true
         *  adds the Kitchen type if it isn't already added
         *  if false
         *  tries to remove the KitchenType from the list of chosen types
         * */
        void chooseFoodType(KitchenType kitchenType, boolean chosen);

        /** Return a restaurant with the id passed as a parameter
         *  will return null if there isn't a restaurant with this id
         * */
        @Nullable
        Restaurant getRestaurant(long id);

        /** adds all KitchenTypes from the parameter array to the List of chosen KitchenTypes
         * */
        void setChosenFoodTypes(KitchenType[] kitchenTypes);

        /** This method tries to save or update the restaurant passed as an argument
         *  returns true if the save or update was successful
         * */
        boolean saveRestaurant(Restaurant restaurant);

        /** Retrieves the image of the restaurant and adds it onto the ImageView
         * */
        void injectImageOntoImageView(ImageView imageView, Restaurant restaurant);

        /** Calls the DAO to close the database
         * */
        void onClose();
    }

}
