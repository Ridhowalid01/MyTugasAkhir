package com.example.mytugasakhir.BelajarHijaiyah

import android.content.Intent
import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.recyclerview.widget.RecyclerView
import com.bumptech.glide.Glide
import com.example.mytugasakhir.data.BelajarHijaiyah
import com.example.mytugasakhir.databinding.ItemRowHijaiyahBinding

class ListBelajarHijaiyahAdapter : RecyclerView.Adapter<ListBelajarHijaiyahAdapter.ListViewHolder>() {

    private var listOfBelajarHijaiyah = ArrayList<BelajarHijaiyah>()

    fun addBelajarHijaiyahList(list: List<BelajarHijaiyah>) {
        this.listOfBelajarHijaiyah.clear()
        this.listOfBelajarHijaiyah.addAll(list)
        notifyDataSetChanged()
    }

    inner class ListViewHolder(private val binding: ItemRowHijaiyahBinding) :
        RecyclerView.ViewHolder(binding.root) {
        fun bind(position: Int) {
            val itemNow = listOfBelajarHijaiyah[position]

            Glide.with(itemView.context).load(itemNow.main_photo).fitCenter().into(binding.imgItemPhoto)

//            binding.tvItemName.text = itemNow.name
//            binding.tvItemText.text = itemNow.text

            binding.cardView.setOnClickListener {
                val intent = Intent(itemView.context, BelajarDetailActivity::class.java)
                intent.putExtra("hijaiyahData", itemNow)
                itemView.context.startActivity(intent)
            }
        }
    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): ListViewHolder {
        return ListViewHolder(
            ItemRowHijaiyahBinding.inflate(
                LayoutInflater.from(parent.context), parent, false
            )
        )
    }

    override fun onBindViewHolder(holder: ListViewHolder, position: Int) {
        holder.bind(position)
    }

    override fun getItemCount(): Int {
        return listOfBelajarHijaiyah.size
    }
}