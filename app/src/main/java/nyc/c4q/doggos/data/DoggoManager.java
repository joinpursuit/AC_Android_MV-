package nyc.c4q.doggos.data;

import java.util.List;

public interface DoggoManager {

    void getBreedNames(BreedNamesCallback callback);

    void getDoggoImageUrlsByBreed(String breedName, DoggoImageUrlsCallback callback);

    // These callback interfaces are necessary so that any implementation of DoggoManager can
    // do its work on a background thread and pass data back to the main thread when complete.
    interface BreedNamesCallback {
        void onSuccess(List<String> breedNames);
        void onFailure();
    }

    interface DoggoImageUrlsCallback {
        void onSuccess(List<String> doggoImageUrls);
        void onFailure();
    }
}
