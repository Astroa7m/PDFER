package com.example.pdfer.ui.pdf_files_screen

import android.app.Application
import androidx.lifecycle.ViewModel
import com.example.pdfer.util.Constants.FOLDER_NAME
import java.io.File

class PDFsViewModel(private val application: Application) : ViewModel() {


    fun arrayOfFiles(): Array<File>? {
        val pdfsDir = File("${application.filesDir}/$FOLDER_NAME")
        val pdfs = pdfsDir.listFiles()
        return if (pdfsDir.exists().not()) {
            null
        } else {
            pdfs
        }
    }

}