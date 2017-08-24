package cel.dev.restaurants.presenterimpl;

import cel.dev.restaurants.uicontracts.ImageFragmentMVP;

/** Presenter for the image fragment
 *  The current responsibilities of the image fragment is quite small
 *  so the separation of the view and presenter is kind of unnecessary.
 *
 * */
public class ImageFragmentPresenterImpl implements ImageFragmentMVP.Presenter {

    private ImageFragmentMVP.View view;

    public ImageFragmentPresenterImpl(ImageFragmentMVP.View view) {
        this.view = view;
    }

    /** If the app has camera permissions a picture will be taken
     * otherwise the app will ask for camera permission
     * */
    @Override
    public void onTakePictureButtonClicked() {
        if (view.hasCameraPermission()) {
            view.takePictureWithCamera();
        } else {
            view.requestCameraPermission();
        }
    }
}
