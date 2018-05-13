package nyc.c4q.doggos.ui.breeds;

import android.app.Activity;
import android.support.annotation.NonNull;

import java.util.List;

import nyc.c4q.doggos.R;
import nyc.c4q.doggos.data.DoggoManager;

class BreedsPresenter implements BreedsContract.Presenter {

    private BreedsContract.View view;
    private DoggoManager doggoManager;

    BreedsPresenter(@NonNull BreedsContract.View view, @NonNull DoggoManager doggoManager) {
        this.view = view;
        this.doggoManager = doggoManager;
    }

    @Override
    public void start() {
        // Get list of breeds
        doggoManager.getBreedNames(new DoggoManager.BreedNamesCallback() {
            @Override
            public void onSuccess(List<String> breedNames) {
                if (view != null) {
                    // Send data to view!
                    view.displayBreedNames(breedNames);
                }
            }

            @Override
            public void onFailure() {
                if (view != null) {
                    // Send view an error message :(
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
