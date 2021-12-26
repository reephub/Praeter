package com.praeter.ui.mainactivity.fragment.metttheancient

import android.content.Context
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.recyclerview.widget.RecyclerView
import com.praeter.data.local.model.Ancient
import com.praeter.databinding.RowAncientItemBinding

class AncientAdapter(
    private val context: Context,
    private val ancientList: List<Ancient>,
    private val listener: AncientClickListener
) : RecyclerView.Adapter<AncientViewHolder>() {

    override fun getItemCount(): Int {
        return ancientList.size
    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): AncientViewHolder {
        val binding: RowAncientItemBinding =
            RowAncientItemBinding.inflate(
                LayoutInflater.from(parent.context),
                parent,
                false
            )

        return AncientViewHolder(context, binding)
    }

    override fun onBindViewHolder(holder: AncientViewHolder, position: Int) {
        val item = ancientList[position]

        holder.bindData(item)

        holder.viewBinding.cvAncientRowItem.setOnClickListener { view: View? ->
            listener.onAncientItemCLickListener(
                view,
                item,
                holder.adapterPosition
            )
        }
    }
}