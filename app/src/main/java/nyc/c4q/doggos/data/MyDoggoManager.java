package nyc.c4q.doggos.data;

import android.os.Handler;
import android.os.Looper;
import android.support.annotation.NonNull;
import android.util.Log;

import java.util.List;

import nyc.c4q.doggos.data.db.DoggoDbHelper;
import nyc.c4q.doggos.data.network.BreedsListResponse;
import nyc.c4q.doggos.data.network.DogApiEndpoints;
import nyc.c4q.doggos.data.network.ImagesByBreedResponse;
import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;
import retrofit2.Retrofit;
import retrofit2.converter.gson.GsonConverterFactory;

public class MyDoggoManager implements DoggoManager {
    private static final String TAG = "MyDoggoManager";

    private DoggoDbHelper doggoDbHelper;
    private DogApiEndpoints dogApiEndpoints;
    private Handler uiThreadHandler;

    public MyDoggoManager(DoggoDbHelper doggoDbHelper) {
        this.doggoDbHelper = doggoDbHelper;

        Retrofit retrofit = new Retrofit.Builder()
                .baseUrl(DogApiEndpoints.BASE_URL)
                .addConverterFactory(GsonConverterFactory.create())
                .build();

        dogApiEndpoints = retrofit.create(DogApiEndpoints.class);

        uiThreadHandler = new Handler(Looper.getMainLooper());
    }

    @Override
    public void getBreedNames(final BreedNamesCallback callback) {
        Call<BreedsListResponse> call = dogApiEndpoints.getBreedsList();

        // Let Retrofit handle the threading for us
        call.enqueue(new Callback<BreedsListResponse>() {
            @Override
            public void onResponse(@NonNull Call<BreedsListResponse> call,
                                   @NonNull Response<BreedsListResponse> response) {

                List<String> breedNames = response.body().getMessage();

                if (!breedNames.isEmpty()) {
                    saveBreedNamesToDb(breedNames);

                    // Pass names back to caller via the callback
                    callback.onSuccess(breedNames);
                } else {
                    tryToGetBreedNamesFromDb(callback);
                }
            }

            @Override
            public void onFailure(@NonNull Call<BreedsListResponse> call, @NonNull Throwable t) {
                // Try to get names from DB if network call failed
                tryToGetBreedNamesFromDb(callback);
            }
        });
    }

    @Override
    public void getDoggoImageUrlsByBreed(final String breedName, final DoggoImageUrlsCallback callback) {
        Call<ImagesByBreedResponse> call = dogApiEndpoints.getImages(breedName);

        // Let Retrofit handle the threading for us
        call.enqueue(new Callback<ImagesByBreedResponse>() {
            @Override
            public void onResponse(@NonNull Call<ImagesByBreedResponse> call,
                                   @NonNull Response<ImagesByBreedResponse> response) {

                List<String> doggoImageUrls = response.body().getMessage();

                if (!doggoImageUrls.isEmpty()) {
                    saveDoggoImageUrlsToDb(breedName, doggoImageUrls);

                    // Pass names back to caller via the callback
                    callback.onSuccess(doggoImageUrls);
                } else {
                    tryToGetDoggoImageUrlsFromDb(breedName, callback);
                }
            }

            @Override
            public void onFailure(@NonNull Call<ImagesByBreedResponse> call, @NonNull Throwable t) {
                tryToGetDoggoImageUrlsFromDb(breedName, callback);
            }
        });
    }

    private void saveBreedNamesToDb(final List<String> breedNames) {
        // Create a new background thread for the database operations
        new Thread(new Runnable() {
            @Override
            public void run() {
                doggoDbHelper.saveBreedNames(breedNames);
            }
        }).start();
    }

    private void saveDoggoImageUrlsToDb(final String breedName, final List<String> doggoImageUrls) {
        // Create a new background thread for the database operations
        new Thread(new Runnable() {
            @Override
            public void run() {
                doggoDbHelper.saveDoggoImageUrls(breedName, doggoImageUrls);
            }
        }).start();
    }

    private void tryToGetBreedNamesFromDb(final BreedNamesCallback callback) {
        // Create a new background thread for the database operations
        new Thread(new Runnable() {
            @Override
            public void run() {
                final List<String> breedNames = doggoDbHelper.getBreedNames();

                // Call the callback from the main/UI thread, not from the background thread
                uiThreadHandler.post(new Runnable() {
                    @Override
                    public void run() {
                        if (!breedNames.isEmpty()) {
                            Log.d(TAG, "run: asdkjghs;dgjh");
                            callback.onSuccess(breedNames);
                        } else {
                            Log.d(TAG, "run: fail");
                            callback.onFailure();
                        }
                    }
                });
            }
        }).start();
    }

    private void tryToGetDoggoImageUrlsFromDb(final String breedName, final DoggoImageUrlsCallback callback) {
        // Create a new background thread for the database operations
        new Thread(new Runnable() {
            @Override
            public void run() {
                final List<String> doggoImageUrls = doggoDbHelper.getDoggoImageUrls(breedName);

                // Call the callback from the main/UI thread, not from the background thread
                uiThreadHandler.post(new Runnable() {
                    @Override
                    public void run() {
                        if (!doggoImageUrls.isEmpty()) {
                            callback.onSuccess(doggoImageUrls);
                        } else {
                            callback.onFailure();
                        }
                    }
                });
            }
        }).start();
    }
}
