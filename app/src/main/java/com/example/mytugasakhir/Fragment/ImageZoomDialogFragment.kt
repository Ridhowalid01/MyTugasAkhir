package com.example.mytugasakhir.Fragment

import android.app.Dialog
import android.os.Bundle
import android.view.View
import android.view.ViewGroup
import android.view.WindowManager
import androidx.appcompat.app.AlertDialog
import androidx.fragment.app.DialogFragment
import com.example.mytugasakhir.R
import com.github.chrisbanes.photoview.PhotoView

class ImageZoomDialogFragment : DialogFragment() {
    override fun onCreateDialog(savedInstanceState: Bundle?): Dialog {
        val inflater = requireActivity().layoutInflater
        val view = inflater.inflate(R.layout.dialog_zoom_image, null)

        // Ambil PhotoView dari layout dialog
        val zoomableImageView = view.findViewById<PhotoView>(R.id.zoomable_image)
        // Set gambar yang ingin ditampilkan dalam modus zoom
        zoomableImageView.setImageResource(R.drawable.area_makhraj)

        // Bangun dialog
        val builder = AlertDialog.Builder(requireActivity())
        builder.setView(view)
        val dialog = builder.create()

        // Set dialog window to full-screen
        dialog.window?.apply {
            // Hide the system UI (status bar, navigation bar, etc.)
            decorView.systemUiVisibility = View.SYSTEM_UI_FLAG_FULLSCREEN or
                    View.SYSTEM_UI_FLAG_HIDE_NAVIGATION or
                    View.SYSTEM_UI_FLAG_IMMERSIVE_STICKY

            // Set layout params to match parent to make the dialog full-screen
            setLayout(ViewGroup.LayoutParams.MATCH_PARENT, ViewGroup.LayoutParams.MATCH_PARENT)

            // Optional: if you want the background of the dialog to be dimmed
            addFlags(WindowManager.LayoutParams.FLAG_DIM_BEHIND)
            setDimAmount(0.5f) // Adjust this value to control the dim amount
        }

        // Tambahkan onClickListener untuk menutup dialog saat latar belakang diklik
        view.setOnClickListener {
            dialog.dismiss()
        }

        return dialog
    }
}

