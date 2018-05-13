package nyc.c4q.doggos.ui.breeds;

import android.arch.lifecycle.Observer;
import android.arch.lifecycle.ViewModelProviders;
import android.os.Bundle;
import android.support.annotation.Nullable;
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

        setUpViewModel();
    }

    private void setUpViewModel() {
        DoggoManager doggoManager = new MyDoggoManager(DoggoDbHelper.getInstance(this));
        BreedsViewModelFactory factory = new BreedsViewModelFactory(doggoManager);

        BreedsViewModel viewModel = ViewModelProviders.of(this, factory)
                .get(BreedsViewModel.class);

        viewModel.getBreedNamesLiveData().observe(this, getObserver());
    }

    private Observer<List<String>> getObserver() {
        return new Observer<List<String>>() {
            @Override
            public void onChanged(@Nullable List<String> breedNames) {
                if (breedNames == null) {
                    Toast.makeText(BreedsActivity.this, R.string.error_msg_no_doggos,
                            Toast.LENGTH_LONG).show();
                } else {
                    adapter.refreshBreedList(breedNames);
                }
            }
        };
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
}
