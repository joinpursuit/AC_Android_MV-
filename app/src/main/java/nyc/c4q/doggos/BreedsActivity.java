package nyc.c4q.doggos;

import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.GridLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.widget.Toast;

import java.util.List;

import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;
import retrofit2.Retrofit;
import retrofit2.converter.gson.GsonConverterFactory;

public class BreedsActivity extends AppCompatActivity {

    private static final int COLUMN_COUNT = 3;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_breeds);

        if (getSupportActionBar() != null) {
            getSupportActionBar().setTitle(R.string.breeds_activity_title);
        }

        // Set up recycler view
        RecyclerView recyclerView = findViewById(R.id.breed_list_recyclerview);

        recyclerView.setLayoutManager(new GridLayoutManager(this, COLUMN_COUNT));

        final BreedsRecyclerViewAdapter adapter = new BreedsRecyclerViewAdapter();
        recyclerView.setAdapter(adapter);

        // Get list of breeds
        Retrofit retrofit = new Retrofit.Builder()
                .baseUrl(DogApiEndpoints.BASE_URL)
                .addConverterFactory(GsonConverterFactory.create())
                .build();

        DogApiEndpoints apiService = retrofit.create(DogApiEndpoints.class);

        Call<BreedsListResponse> call = apiService.getBreedsList();
        call.enqueue(new Callback<BreedsListResponse>() {
            @Override
            public void onResponse(@NonNull Call<BreedsListResponse> call,
                                   @NonNull Response<BreedsListResponse> response) {

                List<String> breedNames = response.body().getMessage();

                // Update the recycler view adapter with the list of breed names
                adapter.refreshBreedList(breedNames);
            }

            @Override
            public void onFailure(@NonNull Call<BreedsListResponse> call, @NonNull Throwable t) {
                Toast.makeText(BreedsActivity.this, R.string.error_msg_no_doggos,
                        Toast.LENGTH_SHORT).show();
            }
        });
    }
}
