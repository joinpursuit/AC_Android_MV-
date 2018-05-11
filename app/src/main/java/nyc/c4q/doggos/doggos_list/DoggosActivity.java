package nyc.c4q.doggos.doggos_list;

import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.RecyclerView;
import android.support.v7.widget.StaggeredGridLayoutManager;
import android.widget.Toast;

import java.util.List;

import nyc.c4q.doggos.R;
import nyc.c4q.doggos.breeds_list.DogApiEndpoints;
import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;
import retrofit2.Retrofit;
import retrofit2.converter.gson.GsonConverterFactory;

public class DoggosActivity extends AppCompatActivity {

    public static final String SELECTED_BREED_KEY = "selectedBreedKey";
    private static final int COLUMN_COUNT = 2;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_doggos);

        // Get name of selected breed
        String breedName = getIntent().getStringExtra(SELECTED_BREED_KEY);

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

        final DoggosRecyclerViewAdapter adapter = new DoggosRecyclerViewAdapter();
        recyclerView.setAdapter(adapter);

        // Get list of doggo image URLs
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
            }

            @Override
            public void onFailure(@NonNull Call<ImagesByBreedResponse> call, @NonNull Throwable t) {
                Toast.makeText(DoggosActivity.this, R.string.error_msg_no_doggos,
                        Toast.LENGTH_SHORT).show();
            }
        });
    }
}