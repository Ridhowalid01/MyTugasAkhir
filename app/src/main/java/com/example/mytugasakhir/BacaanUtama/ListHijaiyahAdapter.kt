package com.example.mytugasakhir.BacaanUtama

import android.content.Intent
import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.recyclerview.widget.RecyclerView
import com.bumptech.glide.Glide
import com.example.mytugasakhir.data.Hijaiyah
import com.example.mytugasakhir.databinding.ItemRowHijaiyahBinding

class ListHijaiyahAdapter : RecyclerView.Adapter<ListHijaiyahAdapter.ListViewHolder>() {

    private var listOfHijaiyah = ArrayList<Hijaiyah>()

    fun addHijaiyahList(list: List<Hijaiyah>) {
        this.listOfHijaiyah.clear()
        this.listOfHijaiyah.addAll(list)
        notifyDataSetChanged()
    }

    inner class ListViewHolder(private val binding: ItemRowHijaiyahBinding) :
        RecyclerView.ViewHolder(binding.root) {
        fun bind(position: Int) {
            val itemNow = listOfHijaiyah[position]

            Glide.with(itemView.context).load(itemNow.photo).fitCenter().into(binding.imgItemPhoto)

//            binding.tvItemName.text = itemNow.name
//            binding.tvItemText.text = itemNow.text

            binding.cardView.setOnClickListener {
                val intent = Intent(itemView.context, DetailActivity::class.java)
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
        return listOfHijaiyah.size
    }
}