package com.praeter.ui.mainactivity.fragment.metttheancient;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.ViewGroup;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.praeter.R;
import com.praeter.data.local.model.Ancient;

import java.util.List;

public class AncientAdapter extends RecyclerView.Adapter<AncientViewHolder> {

    private final Context context;
    private final List<Ancient> ancientList;
    private final AncientClickListener listener;

    public AncientAdapter(@NonNull Context context,
                          @NonNull List<Ancient> ancientList,
                          @NonNull AncientClickListener listener) {
        this.context = context;
        this.ancientList = ancientList;
        this.listener = listener;
    }

    @Override
    public int getItemCount() {
        if (null != ancientList) {
            return ancientList.size();
        }
        return 0;
    }

    @NonNull
    @Override
    public AncientViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        return new AncientViewHolder(context, LayoutInflater.from(parent.getContext())
                .inflate(R.layout.row_ancient_item, parent, false));
    }

    @Override
    public void onBindViewHolder(@NonNull AncientViewHolder holder, int position) {
        final Ancient item = ancientList.get(position);

        holder.bindData(item.getName(), item.getName());
        holder.itemCardView.setOnClickListener(
                view -> listener.onAncientItemCLickListener(view, item, holder.getAdapterPosition()));
    }
}
