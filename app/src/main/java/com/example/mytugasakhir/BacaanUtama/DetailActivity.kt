package com.example.mytugasakhir.BacaanUtama

import android.Manifest
import android.app.Activity
import android.app.ProgressDialog
import android.content.Intent
import android.content.pm.PackageManager
import android.media.MediaPlayer
import android.net.Uri
import android.os.Build
import android.os.Bundle
import android.os.Environment
import android.os.Handler
import android.provider.MediaStore
import android.util.Log
import android.view.View
import android.widget.Button
import android.widget.ImageButton
import android.widget.LinearLayout
import android.widget.SeekBar
import android.widget.TextView
import android.widget.Toast
import androidx.activity.result.ActivityResultLauncher
import androidx.activity.result.contract.ActivityResultContracts
import androidx.appcompat.app.AppCompatActivity
import androidx.appcompat.widget.Toolbar
import androidx.core.content.ContextCompat
import com.example.mytugasakhir.R
import com.example.mytugasakhir.Services.ApiService
import com.example.mytugasakhir.Services.RetrofitClient
import com.example.mytugasakhir.data.Hijaiyah
import com.example.mytugasakhir.databinding.ActivityDetailBinding
import com.github.squti.androidwaverecorder.WaveRecorder
import okhttp3.MediaType.Companion.toMediaTypeOrNull
import okhttp3.MultipartBody
import okhttp3.RequestBody
import retrofit2.Call
import retrofit2.Callback
import retrofit2.Response
import java.io.File


const val REQUEST_CODE = 200

class DetailActivity : AppCompatActivity() {

    private lateinit var binding: ActivityDetailBinding
    private lateinit var hijaiyahData: Hijaiyah
    private var mediaPlayer: MediaPlayer? = null
    private lateinit var seekBar: SeekBar
    private val handler = Handler()
    private val BASE_URL = "https://sound-caring-rhino.ngrok-free.app"
    private val REQUEST_PERMISSION_CODE = 1001
    private lateinit var permissionLauncher: ActivityResultLauncher<Array<String>>


    private lateinit var recordLayout: LinearLayout
    private lateinit var playAudioButton: Button
    private lateinit var mulaiRekamanButton: Button
    private lateinit var kembaliButton: Button
    private lateinit var recordButton: ImageButton
    private lateinit var doneButton: ImageButton
    private lateinit var discardButton: ImageButton
    private lateinit var chooseButton: Button
    private lateinit var nilaiTv: TextView

    private lateinit var waveRecorder: WaveRecorder
    private var isRecording: Boolean = false
    private lateinit var filePath: String
//    private var audioBuffer: ByteArray? = null


    private var index = 0
    private lateinit var progressDialog: ProgressDialog


    private val updateSeekBar = object : Runnable {
        override fun run() {
            mediaPlayer?.let {
                seekBar.progress = it.currentPosition
                handler.postDelayed(this, 1000)
            }
        }
    }

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivityDetailBinding.inflate(layoutInflater)
        setContentView(binding.root)
//        if (!Python.isStarted()) {
//            Python.start(AndroidPlatform(this))
//        }
//        ActivityCompat.requestPermissions(
//            this,
//            arrayOf(Manifest.permission.READ_EXTERNAL_STORAGE),
//            1
//        )

        // Inisialisasi elemen UI
        initViewElements()


        hijaiyahData = getHijaiyahDataFromIntent()
        binding.tvItemDetail.text = hijaiyahData.text
        index = hijaiyahData.index
        progressDialog = ProgressDialog(this)
        progressDialog.setMessage("Processing...")
        Log.d("Index", "nomor index: $index")

        initToolbar()
        initMediaPlayer()

        // Setup Play Audio Button
        playAudioButton.setOnClickListener {
            handlePlayAudioButton()
        }

        seekBar.setOnSeekBarChangeListener(object : SeekBar.OnSeekBarChangeListener {
            override fun onProgressChanged(seekBar: SeekBar?, progress: Int, fromUser: Boolean) {
                if (fromUser) {
                    mediaPlayer?.seekTo(progress)
                }
            }

            override fun onStartTrackingTouch(seekBar: SeekBar?) {}
            override fun onStopTrackingTouch(seekBar: SeekBar?) {}
        })

        mulaiRekamanButton.setOnClickListener {
            toggleRecordingLayout(true)
            stopMediaPlayerIfPlaying()
        }

        kembaliButton.setOnClickListener {
            toggleRecordingLayout(false)
            resetMediaPlayer()
//            audioBuffer = null
        }

//        requestPermissions() -----> lokal
        chooseButton.setOnClickListener {
//            openFileChooser() ----> lokal
            openAudioPicker()
        }

        if (!checkPermissions()) {
            initPermissionLauncher()
            requestPermissions()
        }


        recordButton.setOnClickListener {
            if (isRecording) {
                pauseRecording()
            } else {
                startRecording()
            }
        }

        doneButton.setOnClickListener {
            stopRecording(save = true)
            uploadAudio(filePath)
//            deleteAudioFile()
        }

        discardButton.setOnClickListener {
            stopRecording(save = false)
        }
    }

    private fun initViewElements() {
        recordLayout = findViewById(R.id.record_layout)
        playAudioButton = findViewById(R.id.play_audio)
        mulaiRekamanButton = findViewById(R.id.btn_mulai_rekaman)
        kembaliButton = findViewById(R.id.btn_kembali)
        recordButton = findViewById(R.id.btn_record)
        doneButton = findViewById(R.id.btn_done)
        discardButton = findViewById(R.id.btn_discard)
        chooseButton = findViewById(R.id.btn_choose_file)
        nilaiTv = findViewById((R.id.tv_skor))
        seekBar = binding.seekBar
    }

    private fun getHijaiyahDataFromIntent(): Hijaiyah {
        return if (Build.VERSION.SDK_INT >= 33) {
            intent.getParcelableExtra("hijaiyahData", Hijaiyah::class.java)!!
        } else {
            @Suppress("DEPRECATION")
            intent.getParcelableExtra("hijaiyahData")!!
        }
    }

    private fun initToolbar() {
        val toolbar = findViewById<Toolbar>(R.id.xml_toolbar)
        val toolbarTitle = findViewById<TextView>(R.id.toolbar_title)
        toolbarTitle.text = hijaiyahData.name
    }

    private fun initMediaPlayer() {
        mediaPlayer = MediaPlayer.create(this, hijaiyahData.audio).apply {
            setOnPreparedListener {
                seekBar.max = duration
            }
            setOnCompletionListener {
                resetPlayButton()
                seekBar.progress = 0
                resetMediaPlayer()
            }
            setOnErrorListener { _, _, _ ->
                resetMediaPlayer()
                true
            }
        }
    }

    private fun handlePlayAudioButton() {
        mediaPlayer?.let {
            if (!it.isPlaying) {
                it.start()
                binding.playAudio.text = "Jeda"
                seekBar.visibility = View.VISIBLE
                handler.postDelayed(updateSeekBar, 0)
            } else {
                it.pause()
                binding.playAudio.text = "Putar"
            }
        }
    }

    private fun toggleRecordingLayout(showRecording: Boolean) {
        if (showRecording) {
            seekBar.visibility = View.INVISIBLE
            playAudioButton.visibility = View.INVISIBLE
            mulaiRekamanButton.visibility = View.INVISIBLE

            recordLayout.visibility = View.VISIBLE
            kembaliButton.visibility = View.VISIBLE
            chooseButton.visibility = View.VISIBLE
            nilaiTv.visibility = View.VISIBLE
        } else {
            seekBar.visibility = View.VISIBLE
            playAudioButton.visibility = View.VISIBLE
            mulaiRekamanButton.visibility = View.VISIBLE

            recordLayout.visibility = View.INVISIBLE
            kembaliButton.visibility = View.INVISIBLE
            chooseButton.visibility = View.INVISIBLE
            nilaiTv.visibility = View.INVISIBLE

            resetPlayButton()
        }
    }

    private fun stopMediaPlayerIfPlaying() {
        mediaPlayer?.let {
            if (it.isPlaying) {
                it.stop()
                it.reset()
                initMediaPlayer()
            }
        }
    }

    private fun resetMediaPlayer() {
        mediaPlayer?.release()
        initMediaPlayer()
    }

    private fun resetPlayButton() {
        binding.playAudio.text = "Putar"
    }


    override fun onDestroy() {
        super.onDestroy()
        mediaPlayer?.release()
        handler.removeCallbacks(updateSeekBar)

    }

    fun onBackButtonClicked(view: View) {
        onBackPressed()
    }

    //    ================================= Start of Processing Cloud===============================
    private fun openAudioPicker() {
        val intent = Intent(Intent.ACTION_PICK, MediaStore.Audio.Media.EXTERNAL_CONTENT_URI)
        intent.type = "audio/*"
        resultLauncher.launch(intent)
    }

    private val resultLauncher =
        registerForActivityResult(ActivityResultContracts.StartActivityForResult()) { result ->
            if (result.resultCode == Activity.RESULT_OK) {
                val data: Intent? = result.data
                val audioUri: Uri? = data?.data
                if (audioUri != null) {
                    val filePath = getPathFromUri(audioUri)
                    if (filePath != null) {
                        uploadAudio(filePath)
                    }
                }
            }
        }

    private fun getPathFromUri(uri: Uri): String? {
        val projection = arrayOf(MediaStore.Audio.Media.DATA)
        contentResolver.query(uri, projection, null, null, null)?.use { cursor ->
            if (cursor.moveToFirst()) {
                val columnIndex = cursor.getColumnIndexOrThrow(MediaStore.Audio.Media.DATA)
                return cursor.getString(columnIndex)
            }
        }
        return null
    }

    private fun uploadAudio(filePath: String) {
        progressDialog.show()  // Show the progress dialog

        val audioFile = File(filePath)
        val requestFile = RequestBody.create("audio/*".toMediaTypeOrNull(), audioFile)
        val body = MultipartBody.Part.createFormData("file", audioFile.name, requestFile)
        val index = RequestBody.create("text/plain".toMediaTypeOrNull(), index.toString())

        val apiService = RetrofitClient.getClient(BASE_URL).create(ApiService::class.java)
        val call = apiService.uploadAudio(body, index)

        call.enqueue(object : Callback<String> {
            override fun onResponse(call: Call<String>, response: Response<String>) {
                progressDialog.dismiss()  // Dismiss the progress dialog
                if (response.isSuccessful) {
                    val score = response.body()
                    binding.tvSkor.text = "Nilai: $score"
                    Toast.makeText(this@DetailActivity, "Score: $score", Toast.LENGTH_LONG).show()
                    Log.d("Index", "nomor index2: $index")
                    Log.d("Score", "Score : $score")
                } else {
                    Toast.makeText(this@DetailActivity, "Failed to get score", Toast.LENGTH_LONG)
                        .show()
                }
            }

            override fun onFailure(call: Call<String>, t: Throwable) {
                progressDialog.dismiss()  // Dismiss the progress dialog
                Log.e("Upload Error", t.message.orEmpty())
                Toast.makeText(this@DetailActivity, "Error: ${t.message}", Toast.LENGTH_LONG).show()
            }
        })
    }
// ======================================= END OF PROCESSING CLOUD ===============================

// ======================================= START OF RECORDING ====================================

    private fun initPermissionLauncher() {
        permissionLauncher = registerForActivityResult(ActivityResultContracts.RequestMultiplePermissions()) { permissions ->
            permissions.entries.forEach {
//                val permissionName = it.key
//                val isGranted = it.value
//                if (isGranted) {
//                    // Izin diberikan
//                    when (permissionName) {
//                        Manifest.permission.RECORD_AUDIO -> {
//                            // Akses mikrofon diberikan
//                        }
//                        Manifest.permission.WRITE_EXTERNAL_STORAGE, Manifest.permission.READ_EXTERNAL_STORAGE -> {
//                            // Akses penyimpanan diberikan
//                        }
//                        Manifest.permission.READ_MEDIA_AUDIO -> {
//                            // Akses mikrofon diberikan
//                        }
//                    }
//                } else {
//                    // Izin ditolak
//                    Toast.makeText(this, "Izin $permissionName ditolak", Toast.LENGTH_SHORT).show()
//                }
            }
        }
    }
    private fun checkPermissions(): Boolean {
        val permissions = arrayOf(
            Manifest.permission.RECORD_AUDIO,
            Manifest.permission.WRITE_EXTERNAL_STORAGE,
            Manifest.permission.READ_MEDIA_AUDIO,
            Manifest.permission.READ_EXTERNAL_STORAGE
        )

        return permissions.all {
            ContextCompat.checkSelfPermission(this, it) == PackageManager.PERMISSION_GRANTED
        }
    }

    private fun requestPermissions() {
        val permissionsToRequest = mutableListOf<String>()
        if (ContextCompat.checkSelfPermission(this, Manifest.permission.RECORD_AUDIO) != PackageManager.PERMISSION_GRANTED) {
            permissionsToRequest.add(Manifest.permission.RECORD_AUDIO)
        }
        if (ContextCompat.checkSelfPermission(this, Manifest.permission.WRITE_EXTERNAL_STORAGE) != PackageManager.PERMISSION_GRANTED) {
            permissionsToRequest.add(Manifest.permission.WRITE_EXTERNAL_STORAGE)
        }
        if (ContextCompat.checkSelfPermission(this, Manifest.permission.READ_EXTERNAL_STORAGE) != PackageManager.PERMISSION_GRANTED) {
            permissionsToRequest.add(Manifest.permission.READ_EXTERNAL_STORAGE)
        }
        if (ContextCompat.checkSelfPermission(this, Manifest.permission.READ_MEDIA_AUDIO) != PackageManager.PERMISSION_GRANTED) {
            permissionsToRequest.add(Manifest.permission.READ_MEDIA_AUDIO)
        }

        if (permissionsToRequest.isNotEmpty()) {
            permissionLauncher.launch(permissionsToRequest.toTypedArray())
        }
    }

    private fun startRecording() {
//        filePath = externalCacheDir?.absolutePath + "/audioFile.wav"
        val dir = getExternalFilesDir(Environment.DIRECTORY_MUSIC)
        if (!dir?.exists()!!) {
            dir.mkdirs()
        }
        filePath = "${dir.absolutePath}/audioFile.wav"
        waveRecorder = WaveRecorder(filePath).apply {
            waveConfig.sampleRate = 48000
            waveConfig.channels = android.media.AudioFormat.CHANNEL_IN_STEREO
            waveConfig.audioEncoding = android.media.AudioFormat.ENCODING_PCM_16BIT
        }

        waveRecorder.startRecording()
        isRecording = true
        recordButton.setImageResource(R.drawable.ic_pause)
        Log.d("AudioRecorder", "Recording started, file path: $filePath")
        Toast.makeText(this, "Recording started", Toast.LENGTH_SHORT).show()
    }

    private fun pauseRecording() {
        waveRecorder.stopRecording()
        isRecording = false
        recordButton.setImageResource(R.drawable.ic_record)
        Log.d("AudioRecorder", "Recording paused")
        Toast.makeText(this, "Recording paused", Toast.LENGTH_SHORT).show()
    }

    private fun stopRecording(save: Boolean) {
        if (isRecording) {
            pauseRecording()
        }
        if (!save) {
            val file = File(filePath)
            file.delete()
            Log.d("AudioRecorder", "Recording discarded, file path: $filePath")
            Toast.makeText(this, "Recording discarded", Toast.LENGTH_SHORT).show()
        } else {
            Log.d("AudioRecorder", "Recording saved, file path: $filePath")
//            Toast.makeText(this, "Recording saved", Toast.LENGTH_SHORT).show()
        }
    }
    private fun deleteAudioFile() {
        val file = File(filePath)
        if (file.exists()) {
            file.delete()
            Log.d("AudioRecorder", "Recording file deleted")
        }
    }

// ======================================== END OF RECORDING =====================================
//    ================================= START OF PROCESSING LOKAL ===============================
//    private fun hasPermissions(context: Context, permissions: Array<String>): Boolean =
//        permissions.all {
//            ContextCompat.checkSelfPermission(context, it) == PackageManager.PERMISSION_GRANTED
//        }
//
//    private fun requestPermissions() {
//        if (!hasPermissions(this, REQUIRED_PERMISSIONS)) {
//            ActivityCompat.requestPermissions(this, REQUIRED_PERMISSIONS, REQUEST_CODE_PERMISSIONS)
//        }
//    }
//
//    private fun openFileChooser() {
//        val intent = Intent(Intent.ACTION_GET_CONTENT).apply {
//            type = "audio/*"
//            addCategory(Intent.CATEGORY_OPENABLE)
//        }
//        startActivityForResult(
//            Intent.createChooser(intent, "Select Audio"),
//            PICK_AUDIO_REQUEST_CODE
//        )
//    }
//
//    override fun onActivityResult(requestCode: Int, resultCode: Int, data: Intent?) {
//        super.onActivityResult(requestCode, resultCode, data)
//        if (requestCode == PICK_AUDIO_REQUEST_CODE && resultCode == Activity.RESULT_OK) {
//            data?.data?.let { uri ->
//                val filePath = getPathFromUri(uri).toString()
//                // Use the file path
//                Log.d("AudioPath", "Selected audio path: $filePath")
//                Log.d("Index", "nomor index2: $index")
//
//                skor = callPythonModule(filePath, index)
//                binding.tvSkor.text = "Nilai : $skor"
//            }
//        }
//    }
//
//    private fun getPathFromUri(uri: Uri): String? {
//        var filePath: String? = null
//        if (DocumentsContract.isDocumentUri(this, uri)) {
//            val documentId = DocumentsContract.getDocumentId(uri)
//            val split = documentId.split(":")
//            val type = split[0]
//            if ("primary".equals(type, true)) {
//                filePath = Environment.getExternalStorageDirectory().toString() + "/" + split[1]
//            }
//        } else if ("content".equals(uri.scheme, true)) {
//            val projection = arrayOf(MediaStore.Audio.Media.DATA)
//            contentResolver.query(uri, projection, null, null, null)?.use { cursor ->
//                if (cursor.moveToFirst()) {
//                    val columnIndex = cursor.getColumnIndexOrThrow(MediaStore.Audio.Media.DATA)
//                    filePath = cursor.getString(columnIndex)
//                }
//            }
//        } else if ("file".equals(uri.scheme, true)) {
//            filePath = uri.path
//        }
//        return filePath
//    }
//
//    private fun callPythonModule(filePath: String?, index: Int): Int {
//            val py = Python.getInstance()
//            val module = py.getModule("module")
//            val pyFunc = module["main"]
//            val result = pyFunc?.call(filePath, index)
//            //        println("woi $result")
//            return result?.toInt() ?: 0
//    }
//====================== END OF PROSESSING LOKAL ========================


//    private fun handleSelectedFile(uri: Uri) {
//        // Open an input stream to read the selected audio file
//        contentResolver.openInputStream(uri)?.use { inputStream ->
//            // Read the audio file content into a buffer
//            val buffer = ByteArray(inputStream.available())
//            inputStream.read(buffer)
//
//            // Store the audio buffer in the variable
//            audioBuffer = buffer
//
//            // Notify the user or perform other actions
//            Toast.makeText(this, "Audio file loaded into buffer", Toast.LENGTH_SHORT).show()
//        }
//    }

}
