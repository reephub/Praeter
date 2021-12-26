package com.praeter.ui.mainactivity.fragment.metttheancient

import android.content.Context
import androidx.recyclerview.widget.RecyclerView
import com.praeter.data.local.model.Ancient
import com.praeter.databinding.RowAncientItemBinding

class AncientViewHolder(
    private val context: Context,
    private val itemBinding: RowAncientItemBinding
) : RecyclerView.ViewHolder(itemBinding.root) {

    val viewBinding: RowAncientItemBinding get() = itemBinding

    fun bindData(ancient: Ancient) {

        viewBinding.ancient = ancient

        /*Glide.with(context)
                .load(icon)
                .into(iconImageView);*/

    }
}