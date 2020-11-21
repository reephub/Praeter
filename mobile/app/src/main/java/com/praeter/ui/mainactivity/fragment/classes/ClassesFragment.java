package com.praeter.ui.mainactivity.fragment.classes;

import android.annotation.SuppressLint;
import android.content.Context;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;
import androidx.recyclerview.widget.DefaultItemAnimator;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.praeter.R;
import com.praeter.core.utils.PraeterNetworkManager;
import com.praeter.data.local.model.Class;

import java.util.ArrayList;
import java.util.List;

import butterknife.BindView;
import butterknife.ButterKnife;
import butterknife.Unbinder;
import timber.log.Timber;


@SuppressLint("NonConstantResourceId")
public class ClassesFragment extends Fragment
        implements ClassClickListener {

    private Context context;

    @BindView(R.id.rv_classes)
    RecyclerView rvClasses;
    private Unbinder unbinder;

    /**
     * passing data between fragments
     */
    private OnClassItemSelectedListener listener;

    public interface OnClassItemSelectedListener {
        void onClassClick(String className, String classType);
    }


    public static ClassesFragment newInstance() {

        Bundle args = new Bundle();

        ClassesFragment fragment = new ClassesFragment();
        fragment.setArguments(args);
        return fragment;
    }


    ///////////////////////////////////////////
    //
    //  Override methods
    //
    ///////////////////////////////////////////
    @Override
    public void onAttach(Context context) {
        super.onAttach(context);
        if (context instanceof OnClassItemSelectedListener) {
            listener = (OnClassItemSelectedListener) context;
        } else {
            throw new ClassCastException(context.toString()
                    + " must implement MainActivity.OnClassItemSelectedListener");
        }
    }

    @Override
    public View onCreateView(@NonNull LayoutInflater inflater,
                             ViewGroup container,
                             Bundle savedInstanceState) {

        View root = inflater.inflate(R.layout.fragment_classes, container, false);

        unbinder = ButterKnife.bind(this, root);

        return root;
    }

    @Override
    public void onActivityCreated(@Nullable Bundle savedInstanceState) {
        super.onActivityCreated(savedInstanceState);

        context = getActivity();

        // TODO : check internet connection
        if (!PraeterNetworkManager.isConnected(context)) {
            String errorMessage = "Not connected to the internet. Check your internet connection.";
            Timber.e(errorMessage);
            Toast.makeText(context, errorMessage, Toast.LENGTH_LONG).show();
        } else {


            List<Class> classList = prepareClassData();

            ClassAdapter adapter = new ClassAdapter(context, classList, this);

            LinearLayoutManager linearLayoutManager = new LinearLayoutManager(context);
            rvClasses.setLayoutManager(linearLayoutManager);
            rvClasses.setItemAnimator(new DefaultItemAnimator());
            rvClasses.setAdapter(adapter);
        }
    }

    private List<Class> prepareClassData() {
        List<Class> list = new ArrayList<>();

        list.add(new Class("Cours 1 ", "Dance Class"));
        list.add(new Class("Cours 2 ", "Art Class"));
        list.add(new Class("Cours 3 ", "Gastronomic Class"));
        list.add(new Class("Cours 4 ", "Gastronomic Class"));
        list.add(new Class("Cours 5 ", "Dance Class"));
        list.add(new Class("Cours 6 ", "Art Class"));
        list.add(new Class("Cours 7 ", "Art Class"));

        return list;
    }

    @Override
    public void onDestroyView() {
        if (null != unbinder)
            unbinder.unbind();
        super.onDestroyView();
    }

    @Override
    public void onClassItemCLickListener(View view, Class item, int position) {

        // send data to activity
        listener.onClassClick(item.getName(), item.getType());
    }
}