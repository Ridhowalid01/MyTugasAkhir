package com.example.mytugasakhir.BelajarHijaiyah

import android.annotation.SuppressLint
import android.os.Bundle
import android.view.View
import android.widget.TextView
import androidx.appcompat.app.AppCompatActivity
import androidx.appcompat.widget.Toolbar
import androidx.recyclerview.widget.GridLayoutManager
import com.example.mytugasakhir.R
import com.example.mytugasakhir.data.BelajarHijaiyahData
import com.example.mytugasakhir.databinding.ActivityMenuBinding

class BelajarMenuActivity: AppCompatActivity() {

    lateinit var binding: ActivityMenuBinding

    @SuppressLint("MissingInflatedId")
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivityMenuBinding.inflate(layoutInflater)
        setContentView(binding.root)

        val toolbar = findViewById<Toolbar>(R.id.xml_toolbar)
        val toolbarTitle = findViewById<TextView>(R.id.toolbar_title)
        toolbarTitle.text = "Belajar Huruf Hijaiyah"

        showRecyclerList()

    }

//    override fun onCreateOptionMenu(menu: Menu?): Boolean {
//        menuInflater.inflate(R.menu.menu_main, menu)
//    }

    private fun showRecyclerList() {
        val listBelajarHijaiyahAdapter = ListBelajarHijaiyahAdapter()
        binding.rvHijaiyah.layoutManager = GridLayoutManager(this, 2)
        binding.rvHijaiyah.adapter = listBelajarHijaiyahAdapter

        listBelajarHijaiyahAdapter.addBelajarHijaiyahList(BelajarHijaiyahData.hijaiyahlist)
    }
    fun onBackButtonClicked(view: View) {
        onBackPressed() // Ini akan menampilkan perilaku default ketika tombol kembali ditekan
    }
}