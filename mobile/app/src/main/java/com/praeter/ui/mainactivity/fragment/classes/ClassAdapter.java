package com.praeter.ui.mainactivity.fragment.classes;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.ViewGroup;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.praeter.R;
import com.praeter.data.local.model.Class;

import java.util.List;

public class ClassAdapter extends RecyclerView.Adapter<ClassViewHolder> {

    private final Context context;
    private final List<Class> classList;
    private final ClassClickListener listener;

    public ClassAdapter(@NonNull Context context,
                        @NonNull List<Class> classList,
                        @NonNull ClassClickListener listener) {
        this.context = context;
        this.classList = classList;
        this.listener = listener;
    }

    @Override
    public int getItemCount() {
        if (null != classList) {
            return classList.size();
        }
        return 0;
    }

    @NonNull
    @Override
    public ClassViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        return new ClassViewHolder(context, LayoutInflater.from(parent.getContext())
                .inflate(R.layout.row_class_item, parent, false));
    }

    @Override
    public void onBindViewHolder(@NonNull ClassViewHolder holder, int position) {
        final Class item = classList.get(position);

        holder.bindData(item.getName(), item.getType());
        holder.itemCardView.setOnClickListener(
                view -> listener.onClassItemCLickListener(view, item, holder.getAdapterPosition()));
    }
}
