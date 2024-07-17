package com.example.mytugasakhir.BacaanAlternatif

import android.annotation.SuppressLint
import android.os.Bundle
import android.view.View
import android.widget.TextView
import androidx.appcompat.app.AppCompatActivity
import androidx.appcompat.widget.Toolbar
import androidx.recyclerview.widget.GridLayoutManager
import com.example.mytugasakhir.R
import com.example.mytugasakhir.data.HijaiyahAlternatifData
import com.example.mytugasakhir.databinding.ActivityMenuBinding

class MenuHijaiyahAlternatifActivity:AppCompatActivity() {

    lateinit var binding: ActivityMenuBinding

    @SuppressLint("MissingInflatedId")
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivityMenuBinding.inflate(layoutInflater)
        setContentView(binding.root)

        val toolbar = findViewById<Toolbar>(R.id.xml_toolbar)
        val toolbarTitle = findViewById<TextView>(R.id.toolbar_title)
        toolbarTitle.text = "Bacaan Alternatif"

//        setSupportActionBar(toolbar)
//        supportActionBar?.title = "Huruf Hijaiyah"

        showRecyclerList()

    }
    fun onBackButtonClicked(view: View) {
        onBackPressed() // Ini akan menampilkan perilaku default ketika tombol kembali ditekan
    }

//    override fun onCreateOptionMenu(menu: Menu?): Boolean {
//        menuInflater.inflate(R.menu.menu_main, menu)
//    }

    private fun showRecyclerList() {
        val listHijaiyahAlternatifAdapter = ListHijaiyahAlternatifAdapter()
        binding.rvHijaiyah.layoutManager = GridLayoutManager(this, 2)
        binding.rvHijaiyah.adapter = listHijaiyahAlternatifAdapter

        listHijaiyahAlternatifAdapter.addHijaiyahAlternatifList(HijaiyahAlternatifData.hijaiyahlist)
    }
}