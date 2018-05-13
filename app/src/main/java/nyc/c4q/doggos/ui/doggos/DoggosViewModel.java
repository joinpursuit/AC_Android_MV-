package nyc.c4q.doggos.ui.doggos;

import android.arch.lifecycle.LiveData;
import android.arch.lifecycle.MutableLiveData;
import android.arch.lifecycle.ViewModel;

import java.util.List;

import nyc.c4q.doggos.data.DoggoManager;

class DoggosViewModel extends ViewModel {

    private String breedName;
    private DoggoManager doggoManager;
    private MutableLiveData<List<String>> doggoImageUrlsLiveData;

    DoggosViewModel(String breedName, DoggoManager doggoManager) {
        this.breedName = breedName;
        this.doggoManager = doggoManager;
    }

    LiveData<List<String>> getDoggoImageUrls() {
        if (doggoImageUrlsLiveData == null) {
            doggoImageUrlsLiveData = new MutableLiveData<>();
            loadDoggoImageUrls();
        }
        return doggoImageUrlsLiveData;
    }

    private void loadDoggoImageUrls() {
        // Get list of doggo image URLs
        doggoManager.getDoggoImageUrlsByBreed(breedName, new DoggoManager.DoggoImageUrlsCallback() {
            @Override
            public void onSuccess(List<String> doggoImageUrls) {
                // Update the LiveData object, which the view is observing
                doggoImageUrlsLiveData.setValue(doggoImageUrls);
            }

            @Override
            public void onFailure() {
                doggoImageUrlsLiveData.setValue(null);
            }
        });
    }
}
