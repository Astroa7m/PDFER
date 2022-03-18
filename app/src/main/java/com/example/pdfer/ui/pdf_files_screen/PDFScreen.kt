package com.example.pdfer.ui.pdf_files_screen

import androidx.compose.foundation.ExperimentalFoundationApi
import androidx.compose.foundation.Image
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.lazy.GridCells
import androidx.compose.foundation.lazy.LazyVerticalGrid
import androidx.compose.foundation.lazy.items
import androidx.compose.material.Card
import androidx.compose.material.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.unit.dp
import com.example.pdfer.R
import java.io.File

@OptIn(ExperimentalFoundationApi::class)
@Composable
fun PDFScreen(
    viewModel: PDFsViewModel,
    onItemClicked: (fileUri: String) -> Unit
) {

    val pdfFiles = viewModel.arrayOfFiles()

    Box(
        modifier = Modifier
            .fillMaxSize()
            .padding(16.dp)
    ) {

        if (pdfFiles.isNullOrEmpty()) {
            Text(
                "You currently don't have any pdfs downloaded",
                modifier = Modifier.align(Alignment.Center)
            )
        } else {
            LazyVerticalGrid(
                cells = GridCells.Fixed(3),
                modifier = Modifier.fillMaxSize()
            ) {

                items(pdfFiles) { item ->
                    PdfItem(
                        item = item,
                        onItemClicked = onItemClicked
                    )
                }

            }
        }
    }

}


@Composable
fun PdfItem(
    item: File,
    onItemClicked: (fileUri: String) -> Unit
) {
    Column(
        Modifier
            .padding(8.dp)
            .clickable { onItemClicked(item.absolutePath) },
        verticalArrangement = Arrangement.Center,
        horizontalAlignment = Alignment.CenterHorizontally
    ) {

        Card(
            backgroundColor = Color.Transparent,
            elevation = 0.dp
        ) {
            Image(
                painter = painterResource(id = R.mipmap.pdf_temp),
                contentDescription = "just pdf temp"
            )


        }

        Spacer(modifier = Modifier.height(4.dp))

        Text(text = item.nameWithoutExtension)
    }
}