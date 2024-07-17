package com.example.mytugasakhir.BelajarHijaiyah

import android.media.MediaPlayer
import android.os.Build
import android.os.Bundle
import android.os.Handler
import android.view.View
import android.widget.TextView
import android.widget.Switch
import androidx.appcompat.app.AppCompatActivity
import androidx.appcompat.widget.Toolbar
import com.example.mytugasakhir.R
import com.example.mytugasakhir.data.BelajarHijaiyah
import com.example.mytugasakhir.databinding.BelajarDetailBinding

class BelajarDetailActivity : AppCompatActivity() {

    private lateinit var binding: BelajarDetailBinding
    private lateinit var hijaiyahData: BelajarHijaiyah
    private var mediaPlayer: MediaPlayer? = null
    private val handler = Handler()

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = BelajarDetailBinding.inflate(layoutInflater)
        setContentView(binding.root)

        hijaiyahData = if (Build.VERSION.SDK_INT >= 33) {
            intent.getParcelableExtra("hijaiyahData", BelajarHijaiyah::class.java)!!
        } else {
            @Suppress("DEPRECIATION")
            intent.getParcelableExtra("hijaiyahData")!!
        }
        val toolbar = findViewById<Toolbar>(R.id.xml_toolbar)
        val toolbarTitle = findViewById<TextView>(R.id.toolbar_title)
        toolbarTitle.text = hijaiyahData.name

        binding.ivImg1.setImageResource(hijaiyahData.photo1)
        binding.ivImg2.setImageResource(hijaiyahData.photo2)
        binding.ivImg3.setImageResource(hijaiyahData.photo3)
        binding.ivImg4.setImageResource(hijaiyahData.photo4)
        binding.ivImg5.setImageResource(hijaiyahData.photo5)
        binding.ivImg6.setImageResource(hijaiyahData.photo6)

        binding.tvDesc1.text = hijaiyahData.desc1
        binding.tvDesc2.text = hijaiyahData.desc2
        binding.tvDesc3.text = hijaiyahData.desc3
        binding.tvDesc4.text = hijaiyahData.desc4
        binding.tvDesc5.text = hijaiyahData.desc5
        binding.tvDesc6.text = hijaiyahData.desc6

        setupSwitch()
        setupAudioButton(binding.btnAudio1, hijaiyahData.male_audio1, hijaiyahData.female_audio1)
        setupAudioButton(binding.btnAudio2, hijaiyahData.male_audio3, hijaiyahData.female_audio3)
        setupAudioButton(binding.btnAudio3, hijaiyahData.male_audio5, hijaiyahData.female_audio5)
        setupAudioButton(binding.btnAudio4, hijaiyahData.male_audio2, hijaiyahData.female_audio2)
        setupAudioButton(binding.btnAudio5, hijaiyahData.male_audio4, hijaiyahData.female_audio4)
        setupAudioButton(binding.btnAudio6, hijaiyahData.male_audio6, hijaiyahData.female_audio6)
    }

    private fun setupSwitch() {
        val switchGender = findViewById<Switch>(R.id.switch_gender)
        switchGender.text = if (switchGender.isChecked) "Perempuan" else "Laki-laki"

        switchGender.setOnCheckedChangeListener { _, isChecked ->
            switchGender.text = if (isChecked) "Perempuan" else "Laki-laki"
        }
    }

    private fun setupAudioButton(button: TextView, maleAudioResId: Int, femaleAudioResId: Int) {
        val switchGender = findViewById<Switch>(R.id.switch_gender)

        button.setOnClickListener {
            val isFemale = switchGender.isChecked
            val audioResId = if (isFemale) femaleAudioResId else maleAudioResId

            if (mediaPlayer?.isPlaying == true) {
                mediaPlayer?.pause()
                mediaPlayer?.seekTo(0)
                button.text = "Putar"
            } else {
                mediaPlayer?.release() // Release any previous mediaPlayer
                mediaPlayer = MediaPlayer.create(this, audioResId).apply {
                    setOnCompletionListener {
                        button.text = "Putar"
                        seekTo(0)
                    }
                }
                mediaPlayer?.start()
                button.text = "Hentikan"
            }
        }
    }

    override fun onDestroy() {
        super.onDestroy()
        mediaPlayer?.release()
        mediaPlayer = null
    }

    fun onBackButtonClicked(view: View) {
        onBackPressed() // Ini akan menampilkan perilaku default ketika tombol kembali ditekan
    }
}
