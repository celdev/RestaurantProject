package cel.dev.restaurants.createrestaurant.ImageFragment;

class ImageFragmentPresenterImpl implements ImageFragmentMVP.Presenter {

    private ImageFragmentMVP.View view;

    ImageFragmentPresenterImpl(ImageFragmentMVP.View view) {
        this.view = view;
    }

    @Override
    public void onTakePictureButtonClicked() {
        if (view.hasCameraPermission()) {
            view.takePictureWithCamera();
        } else {
            view.requestCameraPermission();
        }
    }
}
