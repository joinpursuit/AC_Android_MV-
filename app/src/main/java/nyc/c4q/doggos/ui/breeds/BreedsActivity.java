package nyc.c4q.doggos.ui.breeds;

import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.GridLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.widget.Toast;

import java.util.List;

import nyc.c4q.doggos.R;
import nyc.c4q.doggos.data.DoggoManager;
import nyc.c4q.doggos.data.MyDoggoManager;
import nyc.c4q.doggos.data.db.DoggoDbHelper;

public class BreedsActivity extends AppCompatActivity {

    private static final int COLUMN_COUNT = 3;

    private BreedsRecyclerViewAdapter adapter;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_breeds);

        setTitle();
        setUpRecyclerView();
        populateRecyclerView();
    }

    private void setTitle() {
        if (getSupportActionBar() != null) {
            getSupportActionBar().setTitle(R.string.breeds_activity_title);
        }
    }

    private void setUpRecyclerView() {
        RecyclerView recyclerView = findViewById(R.id.breed_list_recyclerview);

        recyclerView.setLayoutManager(new GridLayoutManager(this, COLUMN_COUNT));

        adapter = new BreedsRecyclerViewAdapter();
        recyclerView.setAdapter(adapter);
    }

    private void populateRecyclerView() {
        // Get list of breeds
        DoggoManager doggoManager = new MyDoggoManager(DoggoDbHelper.getInstance(this));

        doggoManager.getBreedNames(new DoggoManager.BreedNamesCallback() {
            @Override
            public void onSuccess(List<String> breedNames) {
                adapter.refreshBreedList(breedNames);
            }

            @Override
            public void onFailure() {
                Toast.makeText(BreedsActivity.this, R.string.error_msg_no_doggos,
                        Toast.LENGTH_SHORT).show();
            }
        });
    }
}
