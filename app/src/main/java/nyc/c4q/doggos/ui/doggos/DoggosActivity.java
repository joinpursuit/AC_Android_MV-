package nyc.c4q.doggos.ui.doggos;

import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.RecyclerView;
import android.support.v7.widget.StaggeredGridLayoutManager;
import android.widget.Toast;

import java.util.List;

import nyc.c4q.doggos.R;
import nyc.c4q.doggos.data.DoggoManager;
import nyc.c4q.doggos.data.MyDoggoManager;
import nyc.c4q.doggos.data.db.DoggoDbHelper;

public class DoggosActivity extends AppCompatActivity {

    public static final String SELECTED_BREED_KEY = "selectedBreedKey";
    private static final int COLUMN_COUNT = 2;

    private String breedName;
    private DoggosRecyclerViewAdapter adapter;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_doggos);

        getSelectedBreedName();
        setTitle();
        setUpRecyclerView();
        populateRecyclerView();
    }

    private void getSelectedBreedName() {
        breedName = getIntent().getStringExtra(SELECTED_BREED_KEY);

        if (breedName == null) {
            Toast.makeText(this, R.string.error_msg_no_doggos, Toast.LENGTH_SHORT).show();
            finish();
        }
    }

    private void setTitle() {
        if (getSupportActionBar() != null) {
            getSupportActionBar().setTitle(
                    String.format(getString(R.string.doggos_activity_title), breedName)
            );
        }
    }

    private void setUpRecyclerView() {
        RecyclerView recyclerView = findViewById(R.id.doggos_list_recyclerview);

        recyclerView.setLayoutManager(
                new StaggeredGridLayoutManager(COLUMN_COUNT, StaggeredGridLayoutManager.VERTICAL));

        adapter = new DoggosRecyclerViewAdapter();
        recyclerView.setAdapter(adapter);
    }

    private void populateRecyclerView() {
        // Get list of doggo image URLs
        DoggoManager doggoManager = new MyDoggoManager(DoggoDbHelper.getInstance(this));

        doggoManager.getDoggoImageUrlsByBreed(breedName, new DoggoManager.DoggoImageUrlsCallback() {
            @Override
            public void onSuccess(List<String> doggoImageUrls) {
                // Update the recycler view adapter with the list of image URLs
                adapter.refreshDoggoImages(doggoImageUrls);
            }

            @Override
            public void onFailure() {
                Toast.makeText(DoggosActivity.this, R.string.error_msg_no_doggos,
                        Toast.LENGTH_SHORT).show();
            }
        });
    }
}