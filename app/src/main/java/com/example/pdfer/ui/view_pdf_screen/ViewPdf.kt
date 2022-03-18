package com.example.pdfer.ui.view_pdf_screen

import android.view.ViewGroup
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.padding
import androidx.compose.material.Icon
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Share
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.unit.dp
import androidx.compose.ui.viewinterop.AndroidView
import com.github.barteksc.pdfviewer.PDFView
import com.github.barteksc.pdfviewer.util.FitPolicy
import java.io.File

@Composable
fun ViewPdf(
    uri: String,
    onShareFile: () -> Unit
) {

    Box(modifier = Modifier.fillMaxSize()) {
        AndroidView(
            factory = { context ->
                PDFView(context, null).also { view ->
                    view.layoutParams = ViewGroup.LayoutParams(
                        ViewGroup.LayoutParams.MATCH_PARENT,
                        ViewGroup.LayoutParams.MATCH_PARENT
                    )

                    view.fromFile(File(uri))
                        .enableSwipe(true)
                        .enableDoubletap(true)
                        .enableAntialiasing(true)
                        .enableAnnotationRendering(true)
                        .fitEachPage(true)
                        //we could save the last page viewed in viewModel
                        //in order to save the state on configuration changes
                        //.defaultPage(viewModel.lastPageViewed)
                        .pageFitPolicy(FitPolicy.BOTH)
                        .pageSnap(true)
                        .pageFling(true)
                        .swipeHorizontal(true)
                        .load()

                }
            },
            modifier = Modifier
                .fillMaxSize()
                .align(Alignment.Center)
        )
        Icon(
            imageVector = Icons.Default.Share,
            contentDescription = null,
            Modifier.clickable { onShareFile() }.align(Alignment.BottomCenter).padding(8.dp)
        )
    }


}


