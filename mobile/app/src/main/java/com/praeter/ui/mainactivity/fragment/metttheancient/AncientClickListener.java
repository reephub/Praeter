package com.praeter.ui.mainactivity.fragment.metttheancient;

import android.view.View;

import com.praeter.data.local.model.Ancient;

public interface AncientClickListener {
    void onAncientItemCLickListener(View view, Ancient item, int position);
}
