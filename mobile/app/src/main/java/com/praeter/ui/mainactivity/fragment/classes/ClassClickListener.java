package com.praeter.ui.mainactivity.fragment.classes;

import android.view.View;

import com.praeter.data.local.model.Class;

public interface ClassClickListener {
    void onClassItemCLickListener(View view, Class item, int position);
}
