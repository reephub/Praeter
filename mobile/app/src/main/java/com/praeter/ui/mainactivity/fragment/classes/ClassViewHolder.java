package com.praeter.ui.mainactivity.fragment.classes;

import android.annotation.SuppressLint;
import android.content.Context;
import android.view.View;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.appcompat.widget.AppCompatImageView;
import androidx.cardview.widget.CardView;
import androidx.recyclerview.widget.RecyclerView;

import com.praeter.R;

import butterknife.BindView;
import butterknife.ButterKnife;

@SuppressLint("NonConstantResourceId")
public class ClassViewHolder extends RecyclerView.ViewHolder {

    private Context context;

    @BindView(R.id.cv_class_row_item)
    public CardView itemCardView;

    @BindView(R.id.row_icon_imageView)
    AppCompatImageView iconImageView;

    @BindView(R.id.row_title_textView)
    TextView titleTextView;

    @BindView(R.id.row_description_textView)
    TextView descriptionTextView;

    public ClassViewHolder(@NonNull Context context, @NonNull View itemView) {
        super(itemView);
        this.context = context;

        ButterKnife.bind(this, itemView);
    }


    public void bindData(@NonNull String name, @NonNull String type) {

        /*Glide.with(context)
                .load(icon)
                .into(iconImageView);*/

        titleTextView.setText(name);
        descriptionTextView.setText(type);
    }
}
