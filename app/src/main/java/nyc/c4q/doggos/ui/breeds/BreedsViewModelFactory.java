package nyc.c4q.doggos.ui.breeds;

import android.arch.lifecycle.ViewModel;
import android.arch.lifecycle.ViewModelProvider;
import android.support.annotation.NonNull;

import nyc.c4q.doggos.data.DoggoManager;

class BreedsViewModelFactory implements ViewModelProvider.Factory {

    private final DoggoManager doggoManager;

    BreedsViewModelFactory(@NonNull DoggoManager doggoManager) {
        this.doggoManager = doggoManager;
    }

    @SuppressWarnings("unchecked")
    @NonNull
    @Override
    public <T extends ViewModel> T create(@NonNull Class<T> modelClass) {
        return (T) new BreedsViewModel(doggoManager);
    }
}