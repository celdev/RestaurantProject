package cel.dev.restaurants.uicontracts;

/** All fragments which are to be contained in the MainActivity must implement this interface
 *  since the handling of a Floating Action Button press will be handled by the current active fragment
 * */
public interface FABFragmentHandler {

    void handleFABClick();
}
