package com.praeter.ui.mainactivity.fragment.metttheancient

import android.view.View
import com.praeter.data.local.model.Ancient

interface AncientClickListener {
    fun onAncientItemCLickListener(view: View?, item: Ancient, position: Int)
}