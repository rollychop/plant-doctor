package com.brohit.plantdoctor.presentation.info_screen

import android.content.Intent
import android.text.method.LinkMovementMethod
import android.util.Patterns
import android.widget.TextView
import androidx.compose.foundation.background
import androidx.compose.foundation.layout.*
import androidx.compose.material.Icon
import androidx.compose.material.LinearProgressIndicator
import androidx.compose.material.Text
import androidx.compose.material.TextField
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Link
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.compose.ui.viewinterop.AndroidView
import androidx.core.text.HtmlCompat
import androidx.hilt.navigation.compose.hiltViewModel
import com.brohit.plantdoctor.BaseUrlDataStore
import com.brohit.plantdoctor.common.Configs
import com.brohit.plantdoctor.common.getCorrectedURL
import com.brohit.plantdoctor.domain.model.SnackbarManager
import com.brohit.plantdoctor.presentation.component.DestinationBar
import com.brohit.plantdoctor.presentation.component.PdButton
import com.brohit.plantdoctor.presentation.plant_detail_screen.LongText
import com.brohit.plantdoctor.presentation.ui.theme.PlantDoctorTheme
import com.ramcosta.composedestinations.annotation.Destination
import kotlinx.coroutines.launch


@Destination
@Composable
fun InfoScreen(
    viewModel: InfoViewModel = hiltViewModel(),
//    navigator: DestinationsNavigator
) {
    Column(
        verticalArrangement = Arrangement.Top,
        horizontalAlignment = Alignment.CenterHorizontally
    ) {
        val state = viewModel.state.value
        state.markdownText?.let { markdown ->
            DestinationBar(title = "About")
            AndroidView(
                modifier = Modifier
                    .fillMaxSize()
                    .background(Color.White)
                    .padding(horizontal = 24.dp),
                factory = { context ->
                    val textView = TextView(context)
                    textView.movementMethod = LinkMovementMethod.getInstance()
                    textView
                },
                update = {
                    it.text = HtmlCompat.fromHtml(markdown, HtmlCompat.FROM_HTML_MODE_COMPACT)
                }
            )
            return@Column
        }
        DestinationBar(title = "Configure")
        var url by remember {
            mutableStateOf("")
        }
        val scope = rememberCoroutineScope()
        val context = LocalContext.current
        val ds = BaseUrlDataStore(context)
        val baseUrl = ds.getBaseUrl.collectAsState(initial = "")
        var error by remember {
            mutableStateOf(false)
        }

        Spacer(modifier = Modifier.height(24.dp))
        if (state.error.isNotEmpty()) {
            Text(
                text = "Error!",
                color = PlantDoctorTheme.colors.error,
                modifier = Modifier.fillMaxWidth(.9f),
                textAlign = TextAlign.Center,
                fontSize = 24.sp
            )
            LongText(text = state.error)
        }
        if (state.isLoading) {
            LinearProgressIndicator()
        }

        Spacer(modifier = Modifier.height(24.dp))
        TextField(
            value = url,
            onValueChange = {
                error = Patterns.WEB_URL.matcher(it).matches().not()
                url = it
            },
            modifier = Modifier
                .padding(horizontal = 24.dp)
                .fillMaxWidth(),
            label = {
                Text(text = "Enter Base URL")
            },
            isError = error,
            leadingIcon = {
                Icon(
                    imageVector = Icons.Filled.Link,
                    contentDescription = "Link"
                )
            }

        )
        baseUrl.value?.let { Text(text = "Saved URL : $it") }
        Row(
            modifier = Modifier
                .fillMaxWidth(.9f)
                .padding(top = 24.dp, bottom = 32.dp),
            horizontalArrangement = Arrangement.SpaceEvenly
        ) {
            PdButton(onClick = {
                if (url.isEmpty()) {
                    url = baseUrl.value ?: ""
                }
                if (Patterns.WEB_URL.matcher(url).matches().not()) {
                    SnackbarManager.showMessage("URL is Invalid!")
                    return@PdButton
                }
                scope.launch {
                    scope.launch {
                        val url1 = getCorrectedURL(url)
                        ds.saveBaseUrl(url1)
                    }
                }
            }) {
                Text(text = if (url.isNotEmpty()) "Save" else "Edit")
            }
            PdButton(
                onClick = {
                    if (url.trim().isEmpty()) {
                        SnackbarManager.showMessage("URL is Same as Earlier! ${Configs.mutableURL}")
                        return@PdButton
                    }
                    val packageManager = context.packageManager
                    val intent = packageManager.getLaunchIntentForPackage(context.packageName)
                    val componentName = intent?.component
                    val mainIntent = Intent.makeRestartActivityTask(componentName)
                    context.startActivity(mainIntent)
                    Runtime.getRuntime().exit(0)
                }) {
                Text(text = "Restart App")
            }
        }
    }
}