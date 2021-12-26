package com.praeter.ui.mainactivity.fragment.classes

import android.content.Context
import android.widget.TextView
import androidx.appcompat.widget.AppCompatImageView
import androidx.cardview.widget.CardView
import androidx.recyclerview.widget.RecyclerView
import com.praeter.data.local.model.Ancient
import com.praeter.data.local.model.ClassModel
import com.praeter.databinding.RowAncientItemBinding
import com.praeter.databinding.RowClassItemBinding

class ClassViewHolder(
    private val context: Context,
    private val itemBinding: RowClassItemBinding
) : RecyclerView.ViewHolder(itemBinding.root) {

    val viewBinding: RowClassItemBinding get() = itemBinding

    fun bindData(classModel: ClassModel) {

        viewBinding.classModel = classModel

        /*Glide.with(context)
                .load(icon)
                .into(iconImageView);*/

    }
}