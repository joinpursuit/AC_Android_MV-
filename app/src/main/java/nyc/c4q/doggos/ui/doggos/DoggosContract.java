package nyc.c4q.doggos.ui.doggos;

import android.support.annotation.NonNull;

import java.util.List;

interface DoggosContract {
    interface View {
        void displayDoggos(@NonNull List<String> doggoImageUrls);
    }

    interface Presenter {
        void onViewReady(); // Could also reasonably be named loadData() or start()
    }
}
