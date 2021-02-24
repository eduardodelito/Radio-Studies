package com.radiostudies.main.ui.adapter

import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.databinding.DataBindingUtil
import androidx.recyclerview.widget.RecyclerView
import com.radiostudies.main.ui.fragment.R
import com.radiostudies.main.ui.fragment.databinding.ItemDiaryBinding
import com.radiostudies.main.model.DiaryModel

/**
 * Created by eduardo.delito on 10/11/20.
 */
class DiaryAdapter(private val listener: OnDiaryAdapterListener) : RecyclerView.Adapter<DiaryAdapter.DiaryViewHolder>() {

    private var list: List<DiaryModel> = mutableListOf()

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): DiaryViewHolder {
        val itemDiaryBinding: ItemDiaryBinding = DataBindingUtil.inflate(
            LayoutInflater.from(parent.context),
            R.layout.item_diary,
            parent,
            false)
        return DiaryViewHolder(itemDiaryBinding)
    }

    override fun getItemCount() = list.size

    fun updateData(list: List<DiaryModel>) {
        this.list = list
        notifyDataSetChanged()
    }

    override fun onBindViewHolder(holder: DiaryViewHolder, position: Int) {
        holder.itemDiaryBinding.diaryModel = list[position]
        holder.itemDiaryBinding.executePendingBindings()
        holder.itemDiaryBinding.itemLayout.setOnClickListener {
            listener.onItemSelected(it, list[position])
        }
    }

    inner class DiaryViewHolder(val itemDiaryBinding: ItemDiaryBinding) :
        RecyclerView.ViewHolder(itemDiaryBinding.root)

    /**
     * Interface to navigate after item selection.
     *
     * Adapter listener
     */
    interface OnDiaryAdapterListener {
        /**
         * onClick method to display details for mobile/tablet.
         * @param view used to navigate fragment.
         * @param diaryModel data to display details.
         */
        fun onItemSelected(view: View, diaryModel: DiaryModel?)
    }
}