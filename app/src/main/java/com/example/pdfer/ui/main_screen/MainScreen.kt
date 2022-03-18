package com.example.pdfer.ui.main_screen

import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.*
import androidx.compose.material.*
import androidx.compose.runtime.*
import androidx.compose.runtime.livedata.observeAsState
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.unit.dp
import androidx.work.WorkInfo
import com.example.pdfer.R
import com.example.pdfer.util.Constants
import com.example.pdfer.util.Constants.WORKER_ERROR

@Composable
fun MainScreen(
    mainVM: MainViewModel,
    onActionClicked: () -> Unit
) {

    val scaffoldState = rememberScaffoldState()

    val workInfo = mainVM.workInfoLiveData.observeAsState().value?.find {
        mainVM.downloadPdfWorkerId?.let { id ->
            id == it.id
        } == true
    }

    var currentWorkState by remember {
        mutableStateOf("")
    }

    var shouldShow by remember {
        mutableStateOf(false)
    }

    Scaffold(
        scaffoldState = scaffoldState,
        modifier = Modifier.fillMaxSize(),
        topBar = {
            TopAppBar(
                title = { Text("PDFER") },
                actions = {
                    Icon(
                        painter = painterResource(id = R.drawable.ic_folder),
                        contentDescription = null,
                        modifier = Modifier.clickable { onActionClicked() }
                    )
                }
            )
        }
    ) {

        Box(
            modifier = Modifier
                .fillMaxSize()
                .padding(16.dp),
            contentAlignment = Alignment.Center
        ) {


            Column(
                modifier = Modifier
                    .fillMaxSize()
                    .padding(16.dp),
                verticalArrangement = Arrangement.Center,
                horizontalAlignment = Alignment.CenterHorizontally
            ) {

                OutlinedTextField(
                    value = mainVM.urlText,
                    onValueChange = mainVM::onUrlTextChange,
                    modifier = Modifier.fillMaxWidth()
                )
                Spacer(modifier = Modifier.height(8.dp))
                Row(
                    modifier = Modifier.fillMaxWidth(),
                    horizontalArrangement = Arrangement.SpaceBetween
                ) {
                    Button(onClick = { mainVM.doWork(false) }) {
                        Text(text = "Download")
                    }
                    Button(onClick = { mainVM.doWork(true) }) {
                        Text(text = "Test")
                    }
                }
            }

            when (workInfo?.state) {
                WorkInfo.State.RUNNING -> {
                    shouldShow = true
                    currentWorkState = "Your pdf file is being downloaded please wait"

                }
                WorkInfo.State.SUCCEEDED -> {
                    currentWorkState = "Your pdf file is ready"
                }
                WorkInfo.State.FAILED -> {
                    currentWorkState = workInfo.outputData.getString(WORKER_ERROR)!!
                }
                WorkInfo.State.CANCELLED ->{
                    currentWorkState = "Download has been cancelled"
                }
                else -> Unit
            }
            if (workInfo?.state?.isFinished == true) {
                workInfo.outputData.getString(Constants.FILE_URI_KEY)
                    ?.let { mainVM.setOutputFile(it) }
            }

            if(shouldShow){
                LaunchedEffect(key1 = workInfo) {
                    if (currentWorkState.isNotEmpty()) {
                        scaffoldState.snackbarHostState.showSnackbar(currentWorkState)
                        if (workInfo?.state == WorkInfo.State.SUCCEEDED || workInfo?.state == WorkInfo.State.FAILED)
                            shouldShow = false
                    }
                }
            }

        }
    }

}