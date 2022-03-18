package com.example.pdfer.ui

import android.content.Intent
import android.net.Uri
import android.os.Bundle
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import androidx.activity.viewModels
import androidx.navigation.NavType
import androidx.navigation.compose.NavHost
import androidx.navigation.compose.composable
import androidx.navigation.compose.rememberNavController
import androidx.navigation.navArgument
import com.example.pdfer.ui.main_screen.MainScreen
import com.example.pdfer.ui.main_screen.MainViewModel
import com.example.pdfer.ui.pdf_files_screen.PDFScreen
import com.example.pdfer.ui.pdf_files_screen.PDFsViewModel
import com.example.pdfer.ui.view_pdf_screen.ViewPdf
import com.example.pdfer.ui.vm_factory.ViewModelsFactory
import com.example.pdfer.util.Constants.DOWNLOADED_SCREEN
import com.example.pdfer.util.Constants.HOME_SCREEN
import com.example.pdfer.util.Constants.VIEW_PDF_SCREEN
import java.net.URLDecoder
import java.net.URLEncoder
import java.nio.charset.StandardCharsets


class MainActivity : ComponentActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        val mainVM: MainViewModel by viewModels { ViewModelsFactory(application) }
        val pdfScreenVm: PDFsViewModel by viewModels { ViewModelsFactory(application) }
        super.onCreate(savedInstanceState)
        setContent {

            val navController = rememberNavController()
            NavHost(navController = navController, startDestination = HOME_SCREEN) {

                composable(HOME_SCREEN) {
                    MainScreen(mainVM) {
                        navController.navigate(DOWNLOADED_SCREEN) {
                            launchSingleTop = true
                        }
                    }
                }

                composable(DOWNLOADED_SCREEN) {
                    PDFScreen(viewModel = pdfScreenVm) { fileUri ->
                        val encodeUri =
                            URLEncoder.encode(fileUri, StandardCharsets.UTF_8.toString())
                        navController.navigate("$VIEW_PDF_SCREEN/$encodeUri") {
                            launchSingleTop = true
                        }
                    }
                }

                composable(
                    "$VIEW_PDF_SCREEN/{fileUri}",
                    arguments = listOf(navArgument("fileUri") {
                        type = NavType.StringType
                    })
                ) { backStackEntry ->

                    val decodedUri = URLDecoder.decode(
                        backStackEntry.arguments?.getString("fileUri"),
                        StandardCharsets.UTF_8.toString()
                    )

                    ViewPdf(uri = decodedUri){
                        shareFile(decodedUri)
                    }
                }
            }
        }
    }

    private fun shareFile(uri: String) {
        Intent(Intent.ACTION_SEND).apply {
            putExtra(Intent.EXTRA_STREAM, Uri.parse(uri))
            type = "application/pdf"
            Intent.createChooser(this, "share via:").also { startActivity(it) }
        }
    }

}
