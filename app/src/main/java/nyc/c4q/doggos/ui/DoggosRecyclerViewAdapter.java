package nyc.c4q.doggos.ui;

import android.support.annotation.NonNull;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;

import com.squareup.picasso.Picasso;

import java.util.ArrayList;
import java.util.List;

import nyc.c4q.doggos.R;

public class DoggosRecyclerViewAdapter
        extends RecyclerView.Adapter<DoggosRecyclerViewAdapter.DoggoViewHolder> {

    private final List<String> doggoImageUrls;

    DoggosRecyclerViewAdapter() {
        doggoImageUrls = new ArrayList<>();
    }

    public void refreshDoggoImages(List<String> doggoImageUrls) {
        this.doggoImageUrls.clear();
        this.doggoImageUrls.addAll(doggoImageUrls);
        notifyDataSetChanged();
    }

    @NonNull
    @Override
    public DoggoViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        return new DoggoViewHolder(
                LayoutInflater.from(parent.getContext())
                        .inflate(R.layout.doggo_view, parent, false)
        );
    }

    @Override
    public void onBindViewHolder(@NonNull DoggoViewHolder holder, int position) {
        holder.loadDoggoImage(doggoImageUrls.get(position));
    }

    @Override
    public int getItemCount() {
        return doggoImageUrls.size();
    }

    static class DoggoViewHolder extends RecyclerView.ViewHolder {

        private final ImageView doggoImageView;

        DoggoViewHolder(View itemView) {
            super(itemView);
            doggoImageView = itemView.findViewById(R.id.doggo_imageview);
        }

        void loadDoggoImage(String imageUrl) {
            Picasso.get()
                    .load(imageUrl)
                    .placeholder(R.drawable.ic_pets_black_24dp)
                    .error(R.drawable.ic_pets_black_24dp)
                    .into(doggoImageView);
        }
    }
}
