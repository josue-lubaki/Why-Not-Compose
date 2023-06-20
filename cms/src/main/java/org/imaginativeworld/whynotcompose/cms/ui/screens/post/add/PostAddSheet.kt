package org.imaginativeworld.whynotcompose.cms.ui.screens.post.add

import android.content.res.Configuration
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.imePadding
import androidx.compose.foundation.layout.navigationBarsPadding
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.statusBarsPadding
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.text.KeyboardOptions
import androidx.compose.foundation.verticalScroll
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.rounded.Add
import androidx.compose.material3.ModalBottomSheet
import androidx.compose.material3.OutlinedTextField
import androidx.compose.material3.Scaffold
import androidx.compose.material3.SheetValue
import androidx.compose.material3.SnackbarHost
import androidx.compose.material3.SnackbarHostState
import androidx.compose.material3.Text
import androidx.compose.material3.rememberModalBottomSheetState
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.MutableState
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.rememberCoroutineScope
import androidx.compose.runtime.saveable.rememberSaveable
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.text.input.KeyboardType
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import androidx.hilt.navigation.compose.hiltViewModel
import kotlinx.coroutines.launch
import org.imaginativeworld.whynotcompose.base.extensions.toast
import org.imaginativeworld.whynotcompose.base.models.Event
import org.imaginativeworld.whynotcompose.cms.theme.CMSAppTheme
import org.imaginativeworld.whynotcompose.cms.ui.compositions.GeneralSheetAppBar
import org.imaginativeworld.whynotcompose.cms.ui.compositions.LoadingContainer
import org.imaginativeworld.whynotcompose.cms.ui.compositions.button.GeneralFilledButton
import org.imaginativeworld.whynotcompose.cms.ui.compositions.button.GeneralOutlinedButton

@Composable
fun PostAddSheet(
    viewModel: PostAddViewModel = hiltViewModel(),
    userId: Int,
    showSheet: MutableState<Boolean>,
    onSuccess: () -> Unit
) {
    val context = LocalContext.current
    val scope = rememberCoroutineScope()
    val bottomSheetState = rememberModalBottomSheetState(
        skipPartiallyExpanded = true,
        confirmValueChange = { sheetState ->
            return@rememberModalBottomSheetState sheetState != SheetValue.Hidden
        }
    )

    val state by viewModel.state.collectAsState()

    val goBack: () -> Unit = {
        scope.launch {
            bottomSheetState.hide()
        }.invokeOnCompletion {
            if (!bottomSheetState.isVisible) {
                showSheet.value = false
            }
        }
    }

    LaunchedEffect(state.addPostSuccess) {
        state.addPostSuccess?.getValueOnce()?.let { isAddPostSuccess ->
            if (isAddPostSuccess) {
                context.toast("Post successfully added.")

                goBack()
                onSuccess()
            }
        }
    }

    ModalBottomSheet(
        onDismissRequest = { showSheet.value = false },
        sheetState = bottomSheetState
    ) {
        PostAddSheetSkeleton(
            showLoading = state.loading,
            showMessage = state.message,
            goBack = goBack,
            addPost = { title, body ->
                viewModel.addPost(
                    userId,
                    title,
                    body
                )
            }
        )
    }
}

@Preview
@Composable
fun PostAddSheetSkeletonPreview() {
    CMSAppTheme {
        PostAddSheetSkeleton()
    }
}

@Preview(uiMode = Configuration.UI_MODE_NIGHT_YES)
@Composable
fun PostAddSheetSkeletonPreviewDark() {
    CMSAppTheme {
        PostAddSheetSkeleton()
    }
}

@Composable
fun PostAddSheetSkeleton(
    showLoading: Boolean = false,
    showMessage: Event<String>? = null,
    goBack: () -> Unit = {},
    addPost: (
        title: String,
        body: String
    ) -> Unit = { _, _ -> }
) {
    val scrollState = rememberScrollState()
    val snackbarHostState = remember { SnackbarHostState() }
    var title by rememberSaveable { mutableStateOf("") }
    var body by rememberSaveable { mutableStateOf("") }

    LaunchedEffect(showMessage) {
        showMessage?.getValueOnce()?.let { message ->
            snackbarHostState.showSnackbar(message)
        }
    }

    Scaffold(
        Modifier
            .navigationBarsPadding()
            .imePadding()
            .statusBarsPadding(),
        topBar = {
            GeneralSheetAppBar(
                title = "Add Post",
                onCancelClicked = goBack
            )
        },
        snackbarHost = { SnackbarHost(snackbarHostState) }
    ) { innerPadding ->
        Column(
            Modifier
                .padding(innerPadding)
                .padding(horizontal = 16.dp)
                .fillMaxWidth()
                .verticalScroll(scrollState),
            horizontalAlignment = Alignment.CenterHorizontally
        ) {
            OutlinedTextField(
                modifier = Modifier.fillMaxWidth(),
                value = title,
                onValueChange = { title = it },
                label = { Text("Title") },
                singleLine = true
            )

            OutlinedTextField(
                modifier = Modifier.fillMaxWidth(),
                value = body,
                onValueChange = { body = it },
                label = { Text("Body") },
                keyboardOptions = KeyboardOptions.Default.copy(
                    keyboardType = KeyboardType.Email
                ),
                minLines = 5
            )

            Row(
                Modifier
                    .fillMaxWidth()
                    .padding(vertical = 8.dp),
                horizontalArrangement = Arrangement.Center
            ) {
                GeneralOutlinedButton(
                    caption = "Cancel",
                    onClick = goBack
                )

                Spacer(Modifier.width(8.dp))

                GeneralFilledButton(
                    caption = "Add Post",
                    icon = Icons.Rounded.Add,
                    onClick = {
                        addPost(
                            title,
                            body
                        )
                    }
                )
            }
        }

        LoadingContainer(
            show = showLoading
        )
    }
}
