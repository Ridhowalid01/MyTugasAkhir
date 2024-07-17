package com.example.mytugasakhir.BelajarHijaiyah

import android.content.Intent
import android.os.Bundle
import android.view.View
import android.widget.Button
import android.widget.ImageView
import android.widget.TextView
import androidx.appcompat.app.AppCompatActivity
import androidx.appcompat.widget.Toolbar
import com.example.mytugasakhir.Fragment.ImageZoomDialogFragment
import com.example.mytugasakhir.R

class BelajarHomeActivity : AppCompatActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.belajar_home)

        val toolbar = findViewById<Toolbar>(R.id.xml_toolbar)
        val toolbarTitle = findViewById<TextView>(R.id.toolbar_title)
        toolbarTitle.text = "Belajar Makharijul Huruf"

        val lanjutBelajarButton : Button = findViewById(R.id.lanjut_belajar_button)

        lanjutBelajarButton.setOnClickListener {
            val intent = Intent(this@BelajarHomeActivity, BelajarMenuActivity::class.java)
            startActivity(intent)
        }
        val imgAreaMakhraj = findViewById<ImageView>(R.id.img_area_makhraj)
        imgAreaMakhraj.setOnClickListener {
            // Tampilkan dialog ketika gambar di-klik
            val dialogFragment = ImageZoomDialogFragment()
            dialogFragment.show(supportFragmentManager, "ImageZoomDialogFragment")
        }
    }
    fun onBackButtonClicked(view: View) {
        onBackPressed() // Ini akan menampilkan perilaku default ketika tombol kembali ditekan
    }
}