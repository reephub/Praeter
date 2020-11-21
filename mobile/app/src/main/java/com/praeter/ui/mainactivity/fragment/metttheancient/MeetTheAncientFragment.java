package com.praeter.ui.mainactivity.fragment.metttheancient;

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
import com.praeter.data.local.model.Ancient;

import java.util.ArrayList;
import java.util.List;

import butterknife.BindView;
import butterknife.ButterKnife;
import butterknife.Unbinder;
import timber.log.Timber;


@SuppressLint("NonConstantResourceId")
public class MeetTheAncientFragment extends Fragment
        implements AncientClickListener {

    private Context context;

    @BindView(R.id.rv_ancients)
    RecyclerView rvAncients;
    private Unbinder unbinder;

    /**
     * passing data between fragments
     */
    private OnMeetTheAncientItemSelectedListener listener;


    public interface OnMeetTheAncientItemSelectedListener {
        void onAncientClick(String ancientName, String ancientDescription);
    }


    public static MeetTheAncientFragment newInstance() {

        Bundle args = new Bundle();

        MeetTheAncientFragment fragment = new MeetTheAncientFragment();
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
        if (context instanceof MeetTheAncientFragment.OnMeetTheAncientItemSelectedListener) {
            listener = (OnMeetTheAncientItemSelectedListener) context;
        } else {
            throw new ClassCastException(context.toString()
                    + " must implement MainActivity.OnMeetTheAncientItemSelectedListener");
        }
    }

    @Override
    public View onCreateView(@NonNull LayoutInflater inflater,
                             ViewGroup container, Bundle savedInstanceState) {
        View root = inflater.inflate(R.layout.fragment_meet_the_ancient, container, false);
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

            List<Ancient> ancientList = prepareAncientData();

            AncientAdapter adapter = new AncientAdapter(context, ancientList, this);

            LinearLayoutManager linearLayoutManager = new LinearLayoutManager(context);
            rvAncients.setLayoutManager(linearLayoutManager);
            rvAncients.setItemAnimator(new DefaultItemAnimator());
            rvAncients.setAdapter(adapter);
        }
    }

    private List<Ancient> prepareAncientData() {
        List<Ancient> list = new ArrayList<>();

        list.add(new Ancient("Ancient 1 "));
        list.add(new Ancient("Ancient 2 "));
        list.add(new Ancient("Ancient 3 "));

        return list;
    }

    @Override
    public void onDestroyView() {
        if (null != unbinder)
            unbinder.unbind();
        super.onDestroyView();
    }


    @Override
    public void onAncientItemCLickListener(View view, Ancient item, int position) {

        // send data to activity
        listener.onAncientClick(item.getName(), item.getName());
    }
}