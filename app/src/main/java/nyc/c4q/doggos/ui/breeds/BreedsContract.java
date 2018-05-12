package nyc.c4q.doggos.ui.breeds;

import android.support.annotation.NonNull;

import java.util.List;

interface BreedsContract {

    interface View {
        void displayBreedNames(@NonNull List<String> breedNames);
    }

    interface Presenter {
        void onViewReady(); // Could also reasonably be named loadData() or start()
        void onBreedSelected();
    }
}
