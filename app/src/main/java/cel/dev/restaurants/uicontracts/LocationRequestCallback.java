package cel.dev.restaurants.uicontracts;

/** Callback which allows for moving some location result methods out of the views
 * */
public interface LocationRequestCallback {

    /** This method allows for extracting requesting location information from an activity or fragment
     *  This allows the method which requests a location to callback to the requester in case of an e.g. an error
     * */
    void requestLocationCallback();
}
