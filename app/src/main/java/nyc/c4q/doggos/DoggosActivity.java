package nyc.c4q.doggos;

import android.content.Context;
import android.net.ConnectivityManager;
import android.net.NetworkInfo;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.RecyclerView;
import android.support.v7.widget.StaggeredGridLayoutManager;
import android.widget.Toast;

import java.util.List;

import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;
import retrofit2.Retrofit;
import retrofit2.converter.gson.GsonConverterFactory;

public class DoggosActivity extends AppCompatActivity {

    public static final String SELECTED_BREED_KEY = "selectedBreedKey";
    private static final int COLUMN_COUNT = 2;

    private DoggosRecyclerViewAdapter adapter;
    private DoggoDbHelper doggoDbHelper;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_doggos);

        doggoDbHelper = DoggoDbHelper.getInstance(this);

        // Get name of selected breed
        final String breedName = getIntent().getStringExtra(SELECTED_BREED_KEY);

        if (breedName == null) {
            Toast.makeText(this, R.string.error_msg_no_doggos, Toast.LENGTH_SHORT).show();
            finish();
            return;
        }

        if (getSupportActionBar() != null) {
            getSupportActionBar().setTitle(
                    String.format(getString(R.string.doggos_activity_title), breedName)
            );
        }

        // Set up recycler view
        RecyclerView recyclerView = findViewById(R.id.doggos_list_recyclerview);

        recyclerView.setLayoutManager(
                new StaggeredGridLayoutManager(COLUMN_COUNT, StaggeredGridLayoutManager.VERTICAL));

        adapter = new DoggosRecyclerViewAdapter();
        recyclerView.setAdapter(adapter);

        // Get list of doggo image URLs
        if (connectedToInternet()) {
            Retrofit retrofit = new Retrofit.Builder()
                    .baseUrl(DogApiEndpoints.BASE_URL)
                    .addConverterFactory(GsonConverterFactory.create())
                    .build();

            DogApiEndpoints apiService = retrofit.create(DogApiEndpoints.class);

            Call<ImagesByBreedResponse> call = apiService.getImages(breedName);
            call.enqueue(new Callback<ImagesByBreedResponse>() {
                @Override
                public void onResponse(@NonNull Call<ImagesByBreedResponse> call,
                                       @NonNull Response<ImagesByBreedResponse> response) {

                    List<String> doggoImageUrls = response.body().getMessage();

                    // Update the recycler view adapter with the list of image URLs
                    adapter.refreshDoggoImages(doggoImageUrls);

                    // Save list of URLs to database
                    doggoDbHelper.saveDoggoImageUrls(breedName, doggoImageUrls);
                }

                @Override
                public void onFailure(@NonNull Call<ImagesByBreedResponse> call, @NonNull Throwable t) {
                    tryToGetImageUrlsFromDb(breedName);
                }
            });
        } else {
            tryToGetImageUrlsFromDb(breedName);
        }
    }

    private boolean connectedToInternet() {
        ConnectivityManager connectivityManager =
                (ConnectivityManager) getSystemService(Context.CONNECTIVITY_SERVICE);

        NetworkInfo activeNetwork = connectivityManager.getActiveNetworkInfo();

        return activeNetwork != null && activeNetwork.isConnectedOrConnecting();
    }

    private void tryToGetImageUrlsFromDb(String breedName) {
        // If not connected to internet, or if network call fails, check if DB is populated
        List<String> doggoImageUrls = doggoDbHelper.getDoggoImageUrls(breedName);

        if (!doggoImageUrls.isEmpty()) {
            // NOTE - we'll still need the internet to actually retrieve the images from the URLs
            // to display them, so this may not be terribly useful - although Picasso does cache
            // some images for you, so you may still be able to see something even if offline
            adapter.refreshDoggoImages(doggoImageUrls);
        } else {
            Toast.makeText(DoggosActivity.this, R.string.error_msg_no_doggos,
                    Toast.LENGTH_SHORT).show();
        }
    }
}