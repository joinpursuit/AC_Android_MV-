package nyc.c4q.doggos.ui.breeds;

import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.GridLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.widget.Toast;

import java.util.List;

import nyc.c4q.doggos.R;
import nyc.c4q.doggos.data.DoggoManager;
import nyc.c4q.doggos.data.MyDoggoManager;
import nyc.c4q.doggos.data.db.DoggoDbHelper;

public class BreedsActivity extends AppCompatActivity implements BreedsContract.View {

    private static final int COLUMN_COUNT = 3;

    private BreedsContract.Presenter presenter;
    private BreedsRecyclerViewAdapter adapter;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_breeds);

        setTitle();
        setUpRecyclerView();

        DoggoManager doggoManager = new MyDoggoManager(DoggoDbHelper.getInstance(this));
        presenter = new BreedsPresenter(this, doggoManager);

        // Tell the presenter the view is good to go!
        presenter.start();
    }

    @Override
    protected void onStop() {
        super.onStop();
        presenter.stop(); // Tell the presenter to stop holding onto a reference to this activity
    }

    @Override
    public void displayBreedNames(@NonNull List<String> breedNames) {
        adapter.refreshBreedList(breedNames);
    }

    @Override
    public void displayErrorMessage(@NonNull String errorMessage) {
        Toast.makeText(BreedsActivity.this, errorMessage, Toast.LENGTH_SHORT).show();
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
