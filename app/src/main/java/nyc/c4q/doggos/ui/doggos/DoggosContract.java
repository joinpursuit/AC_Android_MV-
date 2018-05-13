package nyc.c4q.doggos.ui.doggos;

import android.support.annotation.NonNull;

import java.util.List;

interface DoggosContract {

    interface View {
        void displayDoggos(@NonNull List<String> doggoImageUrls);
        void displayErrorMessage(@NonNull String errorMessage);
    }

    interface Presenter {
        void start(String breedName); // sometimes people might name this onViewReady(), or loadData(), etc.
        void stop();
    }
}
