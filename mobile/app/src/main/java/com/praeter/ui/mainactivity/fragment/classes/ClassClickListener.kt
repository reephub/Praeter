package com.praeter.ui.mainactivity.fragment.classes

import android.view.View
import com.praeter.data.local.model.ClassModel

interface ClassClickListener {
    fun onClassItemCLickListener(view: View?, item: ClassModel, position: Int)
}