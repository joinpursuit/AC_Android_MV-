package nyc.c4q.doggos.ui.doggos;

import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.RecyclerView;
import android.support.v7.widget.StaggeredGridLayoutManager;
import android.widget.Toast;

import java.util.List;

import nyc.c4q.doggos.R;
import nyc.c4q.doggos.data.DoggoManager;
import nyc.c4q.doggos.data.MyDoggoManager;
import nyc.c4q.doggos.data.db.DoggoDbHelper;

public class DoggosActivity extends AppCompatActivity implements DoggosContract.View {

    public static final String SELECTED_BREED_KEY = "selectedBreedKey";
    private static final int COLUMN_COUNT = 2;

    private DoggosContract.Presenter presenter;
    private String breedName;
    private DoggosRecyclerViewAdapter adapter;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_doggos);

        getSelectedBreedName();
        setTitle();
        setUpRecyclerView();

        DoggoManager doggoManager = new MyDoggoManager(DoggoDbHelper.getInstance(this));
        presenter = new DoggosPresenter(this, doggoManager);

        // Tell the presenter the view is good to go!
        presenter.start(breedName);
    }

    @Override
    protected void onStop() {
        super.onStop();
        presenter.stop(); // Tell the presenter to stop holding onto a reference to this activity
    }

    @Override
    public void displayDoggos(@NonNull List<String> doggoImageUrls) {
        adapter.refreshDoggoImages(doggoImageUrls);
    }

    @Override
    public void displayErrorMessage(@NonNull String errorMessage) {
        Toast.makeText(DoggosActivity.this, errorMessage, Toast.LENGTH_SHORT).show();
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
}