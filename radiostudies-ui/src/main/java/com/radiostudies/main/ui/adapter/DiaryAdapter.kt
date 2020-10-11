package com.radiostudies.main.ui.adapter

import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.databinding.DataBindingUtil
import androidx.recyclerview.widget.RecyclerView
import com.radiostudies.main.ui.fragment.R
import com.radiostudies.main.ui.fragment.databinding.ItemDiaryBinding
import com.radiostudies.main.ui.model.diary.DiaryModel

/**
 * Created by eduardo.delito on 10/11/20.
 */
class DiaryAdapter : RecyclerView.Adapter<DiaryAdapter.DiaryViewHolder>() {

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
    }

    inner class DiaryViewHolder(val itemDiaryBinding: ItemDiaryBinding) :
        RecyclerView.ViewHolder(itemDiaryBinding.root)
}