package com.praeter.core.views;

import android.content.Context;
import android.graphics.Bitmap;
import android.util.AttributeSet;
import android.view.LayoutInflater;
import android.view.View;
import android.widget.TextView;

import androidx.annotation.Nullable;
import androidx.appcompat.widget.AppCompatRadioButton;
import androidx.cardview.widget.CardView;

import com.bumptech.glide.Glide;
import com.bumptech.glide.load.DataSource;
import com.bumptech.glide.load.MultiTransformation;
import com.bumptech.glide.load.engine.GlideException;
import com.bumptech.glide.load.resource.bitmap.CenterCrop;
import com.bumptech.glide.load.resource.bitmap.RoundedCorners;
import com.bumptech.glide.request.RequestListener;
import com.bumptech.glide.request.RequestOptions;
import com.bumptech.glide.request.target.Target;
import com.praeter.R;

public class MyPlanRadioButton extends AppCompatRadioButton {

    private View view;
    private CardView cardView;
    private TextView titleTextView;
    private TextView descriptionTextView;
    //private ImageView imageView;

    public MyPlanRadioButton(Context context) {
        super(context);
        init(context);
    }

    public MyPlanRadioButton(Context context, AttributeSet attrs) {
        super(context, attrs);
        init(context);
    }

    public MyPlanRadioButton(Context context, AttributeSet attrs, int defStyleAttr) {
        super(context, attrs, defStyleAttr);
        init(context);
    }

    private final RequestListener<Bitmap> requestListener = new RequestListener<Bitmap>() {
        @Override
        public boolean onLoadFailed(@Nullable GlideException e, Object model, Target<Bitmap> target, boolean isFirstResource) {
            return false;
        }

        @Override
        public boolean onResourceReady(Bitmap resource, Object model, Target<Bitmap> target, DataSource dataSource, boolean isFirstResource) {
            //imageView.setImageBitmap(resource);
            redrawLayout();
            return false;
        }
    };

    public void setImageResource(int resId) {
        Glide.with(getContext())
                .asBitmap()
                .load(resId)
                .apply(RequestOptions.bitmapTransform(
                        new MultiTransformation<>(
                                new CenterCrop(),
                                new RoundedCorners(dp2px(getContext(), 24)))
                        )
                )
                .listener(requestListener)
                .submit();
    }

    public void setImageBitmap(Bitmap bitmap) {
        Glide.with(getContext())
                .asBitmap()
                .load(bitmap)
                .apply(RequestOptions.bitmapTransform(
                        new MultiTransformation<>(
                                new CenterCrop(),
                                new RoundedCorners(dp2px(getContext(), 24)))
                        )
                )
                .listener(requestListener)
                .submit();
    }

    // setText is a final method in ancestor, so we must take another name.
    public void setTitleTextWith(int resId) {
        titleTextView.setText(resId);
        redrawLayout();
    }

    public void setTitleTextWith(CharSequence text) {
        titleTextView.setText(text);
        redrawLayout();
    }

    // setText is a final method in ancestor, so we must take another name.
    public void setDescriptionTextWith(int resId) {
        descriptionTextView.setText(resId);
        redrawLayout();
    }

    public void setDescriptionTextWith(CharSequence text) {
        descriptionTextView.setText(text);
        redrawLayout();
    }

    private void init(Context context) {
        view = LayoutInflater.from(context).inflate(R.layout.premium_plan_radio_button_content, null);
        cardView = view.findViewById(R.id.cardView);
        titleTextView = view.findViewById(R.id.tv_plan_title);
        descriptionTextView = view.findViewById(R.id.tv_plan_description);
        //imageView = view.findViewById(R.id.imageView);
        redrawLayout();
    }

    private void redrawLayout() {
        view.setDrawingCacheEnabled(true);
        view.measure(View.MeasureSpec.UNSPECIFIED, View.MeasureSpec.UNSPECIFIED);
        view.layout(0, 0, view.getMeasuredWidth(), view.getMeasuredHeight());

        view.buildDrawingCache(true);
        //Bitmap bitmap = Bitmap.createBitmap(view.getDrawingCache());
        //setCompoundDrawablesWithIntrinsicBounds(new BitmapDrawable(getResources(), bitmap), null, null, null);
        view.setDrawingCacheEnabled(false);
    }

    private int dp2px(Context context, int dp) {
        return (int) (dp * context.getResources().getDisplayMetrics().density);
    }

}