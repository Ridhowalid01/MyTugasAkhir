package com.example.mytugasakhir

import android.content.Intent
import android.os.Bundle
import android.view.View
import android.widget.Button
import androidx.appcompat.app.AppCompatActivity
import com.example.mytugasakhir.BacaanAlternatif.MenuHijaiyahAlternatifActivity
import com.example.mytugasakhir.BacaanUtama.MenuActivity
import com.example.mytugasakhir.BelajarHijaiyah.BelajarHomeActivity

class StartActivity : AppCompatActivity() {
    private lateinit var startButton: Button
    private lateinit var exitButton: Button
    private lateinit var belajarButton: Button
    private lateinit var utamaButton: Button
    private lateinit var alternatifButton: Button
    private lateinit var kembaliButton: Button

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_start)
//        if( !Python.isStarted() ) {
//            Python.start( AndroidPlatform( this ) )
//        }
//        val py = Python.getInstance()
//        val module = py.getModule( "tes" )
//        val output = module["main"]
//        val result = output?.call()
//        println("woi $result")

        startButton = findViewById(R.id.start_button)
        exitButton = findViewById(R.id.exit_button)
        belajarButton = findViewById((R.id.belajar_button))
        utamaButton = findViewById(R.id.utama_button)
        alternatifButton = findViewById(R.id.alternatif_button)
        kembaliButton  = findViewById(R.id.kembali_button)

        startButton.setOnClickListener {
            // Pindah ke halaman berikutnya
            startButton.visibility = View.GONE
            belajarButton.visibility = View.GONE
            exitButton.visibility = View.GONE

            // Tambahkan tombol baru "utama" dan "alternatif"
            utamaButton.visibility = View.VISIBLE
            alternatifButton.visibility = View.VISIBLE
            kembaliButton.visibility = View.VISIBLE

            utamaButton.setOnClickListener {
                val intent = Intent(this@StartActivity, MenuActivity::class.java)
                startActivity(intent)
            }

            alternatifButton.setOnClickListener {
                val intent = Intent(this@StartActivity, MenuHijaiyahAlternatifActivity::class.java)
                startActivity(intent)
            }

            kembaliButton.setOnClickListener {
                startButton.visibility = View.VISIBLE
                belajarButton.visibility = View.VISIBLE
                exitButton.visibility = View.VISIBLE

                // Tambahkan tombol baru "utama" dan "alternatif"
                utamaButton.visibility = View.INVISIBLE
                alternatifButton.visibility = View.INVISIBLE
                kembaliButton.visibility = View.INVISIBLE
            }

        }

        belajarButton.setOnClickListener {
            val intent = Intent(this@StartActivity, BelajarHomeActivity::class.java)
            startActivity(intent)
        }

        exitButton.setOnClickListener {
            // Keluar dari aplikasi
            finish()
        }

    }

    override fun onResume() {
        super.onResume()
        // Atur tampilan ke keadaan awal saat aktivitas menjadi aktif kembali
        resetToInitialState()
    }

    private fun resetToInitialState() {
        // Tampilkan tombol "Start" dan sembunyikan tombol "Utama" dan "Alternatif"

        utamaButton.visibility = View.INVISIBLE
        alternatifButton.visibility = View.INVISIBLE
        kembaliButton.visibility = View.INVISIBLE
        // Tampilkan kembali tombol "Belajar" dan "Keluar"
        startButton.visibility = View.VISIBLE
        belajarButton.visibility = View.VISIBLE
        exitButton.visibility = View.VISIBLE
    }
}
