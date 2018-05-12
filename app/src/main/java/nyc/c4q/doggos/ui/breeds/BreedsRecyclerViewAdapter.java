package nyc.c4q.doggos.ui.breeds;

import android.content.Intent;
import android.support.annotation.NonNull;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import java.util.ArrayList;
import java.util.List;

import nyc.c4q.doggos.ui.doggos.DoggosActivity;

public class BreedsRecyclerViewAdapter
        extends RecyclerView.Adapter<BreedsRecyclerViewAdapter.BreedViewHolder> {

    private final List<String> breedNames;

    BreedsRecyclerViewAdapter() {
        this.breedNames = new ArrayList<>();
    }

    @NonNull
    @Override
    public BreedViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        return new BreedViewHolder(
                LayoutInflater.from(parent.getContext())
                        .inflate(android.R.layout.simple_list_item_1, parent, false)
        );
    }

    @Override
    public void onBindViewHolder(@NonNull BreedViewHolder holder, int position) {
        holder.bindBreedNameToView(breedNames.get(position));
    }

    @Override
    public int getItemCount() {
        return breedNames.size();
    }

    public void refreshBreedList(List<String> breedNames) {
        this.breedNames.clear();
        this.breedNames.addAll(breedNames);
        notifyDataSetChanged();
    }

    static class BreedViewHolder extends RecyclerView.ViewHolder {

        private final TextView breedNameTextView;

        BreedViewHolder(View itemView) {
            super(itemView);
            breedNameTextView = itemView.findViewById(android.R.id.text1);

            breedNameTextView.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    // Launch the Doggos Activity & tell it which breed was selected
                    Intent intent = new Intent(v.getContext(), DoggosActivity.class);

                    intent.putExtra(DoggosActivity.SELECTED_BREED_KEY,
                            breedNameTextView.getText().toString());

                    v.getContext().startActivity(intent);
                }
            });
        }

        void bindBreedNameToView(String breedName) {
            breedNameTextView.setText(breedName);
        }
    }
}
