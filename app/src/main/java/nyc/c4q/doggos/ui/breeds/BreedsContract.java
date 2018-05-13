package nyc.c4q.doggos.ui.breeds;

import android.support.annotation.NonNull;

import java.util.List;

interface BreedsContract {

    interface View {
        void displayBreedNames(@NonNull List<String> breedNames);
        void displayErrorMessage(@NonNull String errorMessage);
    }

    interface Presenter {
        void start(); // sometimes people might name this onViewReady(), or loadData(), etc.
        void stop();
    }
}