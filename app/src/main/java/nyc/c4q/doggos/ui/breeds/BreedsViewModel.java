package nyc.c4q.doggos.ui.breeds;

import android.arch.lifecycle.LiveData;
import android.arch.lifecycle.MutableLiveData;
import android.arch.lifecycle.ViewModel;

import java.util.List;

import nyc.c4q.doggos.data.DoggoManager;

class BreedsViewModel extends ViewModel {

    private DoggoManager doggoManager;
    private MutableLiveData<List<String>> breedNamesLiveData;

    BreedsViewModel(DoggoManager doggoManager) {
        this.doggoManager = doggoManager;
    }

    LiveData<List<String>> getBreedNamesLiveData() {
        if (breedNamesLiveData == null) {
            breedNamesLiveData = new MutableLiveData<>();
            loadBreedNames();
        }
        return breedNamesLiveData;
    }

    private void loadBreedNames() {
        // Get list of breeds
        doggoManager.getBreedNames(new DoggoManager.BreedNamesCallback() {
            @Override
            public void onSuccess(List<String> breedNames) {
                // Update the LiveData object, which the view is observing
                breedNamesLiveData.setValue(breedNames);
            }

            @Override
            public void onFailure() {
                breedNamesLiveData.setValue(null);
            }
        });
    }
}
