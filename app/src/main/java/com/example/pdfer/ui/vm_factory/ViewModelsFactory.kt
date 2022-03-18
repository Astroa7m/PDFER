package com.example.pdfer.ui.vm_factory

import android.app.Application
import androidx.lifecycle.ViewModel
import androidx.lifecycle.ViewModelProvider
import com.example.pdfer.ui.main_screen.MainViewModel
import com.example.pdfer.ui.pdf_files_screen.PDFsViewModel

@Suppress("UNCHECKED_CAST")
class ViewModelsFactory(private val application: Application) : ViewModelProvider.Factory {

    override fun <T : ViewModel> create(modelClass: Class<T>): T {
        return when  {
            modelClass.isAssignableFrom(PDFsViewModel::class.java) -> PDFsViewModel(application) as T
            modelClass.isAssignableFrom(MainViewModel::class.java) -> MainViewModel(application) as T
            else -> throw IllegalArgumentException("Unknown ViewModel class")
        }
    }
}