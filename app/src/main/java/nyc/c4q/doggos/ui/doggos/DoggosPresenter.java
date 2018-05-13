package nyc.c4q.doggos.ui.doggos;

import android.app.Activity;
import android.support.annotation.NonNull;

import java.util.List;

import nyc.c4q.doggos.R;
import nyc.c4q.doggos.data.DoggoManager;

class DoggosPresenter implements DoggosContract.Presenter {

    private DoggosContract.View view;
    private DoggoManager doggoManager;

    DoggosPresenter(@NonNull DoggosContract.View view, @NonNull DoggoManager doggoManager) {
        this.view = view;
        this.doggoManager = doggoManager;
    }

    @Override
    public void start(String breedName) {
        // Get list of doggo image URLs
        doggoManager.getDoggoImageUrlsByBreed(breedName, new DoggoManager.DoggoImageUrlsCallback() {
            @Override
            public void onSuccess(List<String> doggoImageUrls) {
                if (view !=null) {
                    view.displayDoggos(doggoImageUrls);
                }
            }

            @Override
            public void onFailure() {
                if (view != null) {
                    String errMsg = ((Activity) view).getString(R.string.error_msg_no_doggos);
                    view.displayErrorMessage(errMsg);
                }
            }
        });
    }

    @Override
    public void stop() {
        // Prevent memory leaks by removing the reference this holds to the view
        view = null;

        // This is important because the presenter might live on past the activity, waiting for
        // an asynchronous request to DoggoManager to complete. If it also holds a reference
        // to the activity that whole time, the garbage collector cannot clear the activity after
        // it is ended, and that creates the memory leak.
    }
}
