package com.brohit.plantdoctor.presentation.camera

import android.content.ContextWrapper
import androidx.lifecycle.ViewModel
import com.brohit.plantdoctor.R
import dagger.hilt.android.lifecycle.HiltViewModel
import java.io.File
import java.util.concurrent.ExecutorService
import java.util.concurrent.Executors
import javax.inject.Inject


@HiltViewModel
class CameraScreenViewModel @Inject constructor() : ViewModel() {
    private var executors: ExecutorService? = null

    fun getOutputDirectory(cw: ContextWrapper): File {
        val mediaDir = cw.externalMediaDirs.firstOrNull()?.let {
            File(it, cw.resources.getString(R.string.app_name)).apply { mkdirs() }
        }
        return if (mediaDir != null && mediaDir.exists()) mediaDir else cw.filesDir
    }

    fun getCameraExecutor(): ExecutorService {
        if (executors == null)
            executors = Executors.newSingleThreadExecutor()
        return executors!!
    }


    override fun onCleared() {
        super.onCleared()
        executors?.shutdown()
    }
}