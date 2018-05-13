package nyc.c4q.doggos.ui.doggos;

import android.arch.lifecycle.ViewModel;
import android.arch.lifecycle.ViewModelProvider;
import android.support.annotation.NonNull;

import nyc.c4q.doggos.data.DoggoManager;

class DoggosViewModelFactory implements ViewModelProvider.Factory {

    private final String breedName;
    private final DoggoManager doggoManager;

    public DoggosViewModelFactory(@NonNull String breedName, @NonNull DoggoManager doggoManager) {
        this.breedName = breedName;
        this.doggoManager = doggoManager;
    }

    @SuppressWarnings("unchecked")
    @NonNull
    @Override
    public <T extends ViewModel> T create(@NonNull Class<T> modelClass) {
        return (T) new DoggosViewModel(breedName, doggoManager);
    }
}
