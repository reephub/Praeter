package com.praeter.ui.mainactivity;

import android.annotation.SuppressLint;
import android.content.Context;
import android.os.Bundle;
import android.os.Parcelable;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.ImageButton;
import android.widget.TextView;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.appcompat.widget.AppCompatImageView;

import com.bumptech.glide.Glide;
import com.bumptech.glide.load.resource.bitmap.RoundedCorners;
import com.google.android.material.bottomsheet.BottomSheetDialogFragment;
import com.praeter.R;
import com.praeter.core.utils.PraeterNetworkManager;
import com.praeter.data.local.model.Class;

import org.parceler.Parcels;

import butterknife.BindView;
import butterknife.ButterKnife;
import butterknife.OnClick;
import jp.wasabeef.glide.transformations.BlurTransformation;
import timber.log.Timber;


@SuppressLint("NonConstantResourceId")
public class BottomSheetFragment extends BottomSheetDialogFragment {

    public static final String CLASS_ITEM_BUNDLE = "CLASS_ITEM_BUNDLE";

    private Context context;

    @BindView(R.id.close_bottom_sheet_btn)
    ImageButton btnCloseBottomSheet;
    @BindView(R.id.iv_class_thumb_blurred)
    AppCompatImageView ivThumbBlurred;
    @BindView(R.id.iv_class_thumb)
    AppCompatImageView ivThumb;
    @BindView(R.id.tv_name)
    TextView tvName;
    @BindView(R.id.tv_type)
    TextView tvType;
    @BindView(R.id.btn_book)
    Button btnBook;

    private Class storedClass;


    public static BottomSheetFragment newInstance() {

        Bundle args = new Bundle();

        BottomSheetFragment fragment = new BottomSheetFragment();
        fragment.setArguments(args);
        return fragment;
    }


    public static BottomSheetFragment newInstance(Class classItem) {

        Bundle args = new Bundle();

        Parcelable parcelable = Parcels.wrap(Class.class, classItem);

        args.putParcelable(CLASS_ITEM_BUNDLE, parcelable);

        BottomSheetFragment fragment = new BottomSheetFragment();
        fragment.setArguments(args);
        return fragment;
    }

    public BottomSheetFragment() {
        // Required empty public constructor
    }


    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        View rootView =
                inflater.inflate(
                        R.layout.fragment_bottom_sheet_dialog,
                        container,
                        false);
        ButterKnife.bind(this, rootView);
        return rootView;
    }

    @Override
    public void onViewCreated(@NonNull View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);
        Timber.i("onViewCreated()");

        context = getActivity();

        //Retrieve data
        getClassData();
    }


    @OnClick(R.id.btn_book)
    void onReserveButtonClicked() {
        if (!PraeterNetworkManager.isConnected(context)) {
            String errorMessage = "Not connected to the internet. Check your internet connection.";
            Timber.e(errorMessage);
            Toast.makeText(context, errorMessage, Toast.LENGTH_LONG).show();
            return;
        }

        String message = "You've successfully reserved the class " + storedClass.getName();
        Toast.makeText(context, message, Toast.LENGTH_LONG).show();

    }

    @OnClick(R.id.close_bottom_sheet_btn)
    void onCloseButtonClicked() {
        dismiss();
    }

    private void getClassData() {
        Timber.i("getDeviceInfo()");

        assert getArguments() != null;
        if (null == getArguments().getParcelable(CLASS_ITEM_BUNDLE)) {
            Timber.e("Bundle with key : CLASS_ITEM_BUNDLE, is null ");
            return;
        }

        storedClass = Parcels.unwrap(getArguments().getParcelable(CLASS_ITEM_BUNDLE));

        setViews(storedClass);
    }

    @SuppressLint("SetTextI18n")
    private void setViews(Class classItem) {
        Glide.with(this)
                .load(R.drawable.visa_icon)
                .transform(
                        new BlurTransformation(75),
                        new RoundedCorners(48))
                .into(ivThumbBlurred);

        tvName.setText(classItem.getName());
        tvType.setText(classItem.getType());
    }

    @Override
    public void onDestroyView() {
        super.onDestroyView();
        Timber.e("DestroyView()");
    }
}
