package com.praeter.ui.mainactivity.fragment.classes

import android.content.Context
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.recyclerview.widget.RecyclerView
import com.praeter.R
import com.praeter.data.local.model.ClassModel
import com.praeter.databinding.RowAncientItemBinding
import com.praeter.databinding.RowClassItemBinding
import com.praeter.ui.mainactivity.fragment.metttheancient.AncientViewHolder

class ClassAdapter(
    private val context: Context,
    private val classModelList: List<ClassModel>,
    private val listener: ClassClickListener
) : RecyclerView.Adapter<ClassViewHolder>() {

    override fun getItemCount(): Int {
        return classModelList.size
    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): ClassViewHolder {
        val binding: RowClassItemBinding =
            RowClassItemBinding.inflate(
                LayoutInflater.from(parent.context),
                parent,
                false
            )

        return ClassViewHolder(context, binding)
    }

    override fun onBindViewHolder(holder: ClassViewHolder, position: Int) {
        val item = classModelList[position]

        holder.bindData(item)
        holder.viewBinding.cvClassRowItem.setOnClickListener { view: View? ->
            listener.onClassItemCLickListener(
                view,
                item,
                holder.adapterPosition
            )
        }
    }
}