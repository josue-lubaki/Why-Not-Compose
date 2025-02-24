/*
 * Copyright 2023 Md. Mahmudul Hasan Shohag
 *
 * Licensed under the Apache License, Version 2.0 (the "License");
 * you may not use this file except in compliance with the License.
 * You may obtain a copy of the License at
 *
 *     http://www.apache.org/licenses/LICENSE-2.0
 *
 * Unless required by applicable law or agreed to in writing, software
 * distributed under the License is distributed on an "AS IS" BASIS,
 * WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
 * See the License for the specific language governing permissions and
 * limitations under the License.
 *
 * ------------------------------------------------------------------------
 *
 * Project: Why Not Compose!
 * Developed by: @ImaginativeShohag
 *
 * Md. Mahmudul Hasan Shohag
 * imaginativeshohag@gmail.com
 *
 * Source: https://github.com/ImaginativeShohag/Why-Not-Compose
 */

package org.imaginativeworld.whynotcompose.cms.ui.screens.user.details

import androidx.compose.animation.AnimatedVisibility
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.imePadding
import androidx.compose.foundation.layout.navigationBarsPadding
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.statusBarsPadding
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.verticalScroll
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.rounded.Ballot
import androidx.compose.material.icons.rounded.Delete
import androidx.compose.material.icons.rounded.Edit
import androidx.compose.material.icons.rounded.TextSnippet
import androidx.compose.material3.AlertDialog
import androidx.compose.material3.Scaffold
import androidx.compose.material3.SnackbarHost
import androidx.compose.material3.SnackbarHostState
import androidx.compose.material3.SnackbarResult
import androidx.compose.material3.Text
import androidx.compose.material3.TextButton
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.saveable.rememberSaveable
import androidx.compose.runtime.setValue
import androidx.compose.ui.Modifier
import androidx.compose.ui.tooling.preview.PreviewLightDark
import androidx.compose.ui.unit.dp
import org.imaginativeworld.whynotcompose.base.models.Event
import org.imaginativeworld.whynotcompose.cms.models.ActionMessage
import org.imaginativeworld.whynotcompose.cms.models.user.User
import org.imaginativeworld.whynotcompose.cms.repositories.MockData
import org.imaginativeworld.whynotcompose.cms.theme.CMSAppTheme
import org.imaginativeworld.whynotcompose.cms.ui.compositions.GeneralAppBar
import org.imaginativeworld.whynotcompose.cms.ui.compositions.LoadingContainer
import org.imaginativeworld.whynotcompose.cms.ui.compositions.LoadingItem
import org.imaginativeworld.whynotcompose.cms.ui.compositions.button.GeneralFilledButton
import org.imaginativeworld.whynotcompose.cms.ui.compositions.button.GeneralOutlinedButton
import org.imaginativeworld.whynotcompose.cms.ui.screens.user.edit.UserEditSheet
import org.imaginativeworld.whynotcompose.cms.ui.screens.user.list.elements.UserItem

@Composable
fun UserDetailsScreen(
    viewModel: UserDetailsViewModel,
    userId: Int,
    goBack: () -> Unit,
    toggleUIMode: () -> Unit,
    onTodosClick: () -> Unit,
    onPostsClick: () -> Unit
) {
    val openEditUserSheet = rememberSaveable { mutableStateOf(false) }
    val state by viewModel.state.collectAsState()

    var openDeleteDialog by remember { mutableStateOf(false) }

    LaunchedEffect(Unit) {
        viewModel.getDetails(userId)
    }

    LaunchedEffect(state.deleteSuccess, goBack) {
        if (state.deleteSuccess?.getValueOnce() == true) {
            goBack()
        }
    }

    UserDetailsScreenSkeleton(
        user = state.user,
        showLoading = state.loading,
        showMessage = state.message,
        goBack = goBack,
        toggleUIMode = toggleUIMode,
        retryDataLoad = {
            viewModel.getDetails(userId)
        },
        onDeleteClick = { openDeleteDialog = true },
        onEditClick = {
            openEditUserSheet.value = !openEditUserSheet.value
        },
        onTodosClick = onTodosClick,
        onPostsClick = onPostsClick
    )

    // ----------------------------------------------------------------
    // Alerts
    // ----------------------------------------------------------------

    if (openDeleteDialog) {
        AlertDialog(
            onDismissRequest = {
                openDeleteDialog = false
            },
            title = {
                Text(text = "Delete")
            },
            text = {
                Text(text = "Are you sure you want to delete this user?")
            },
            confirmButton = {
                TextButton(
                    onClick = {
                        openDeleteDialog = false

                        viewModel.deleteUser(userId)
                    }
                ) {
                    Text("Yes")
                }
            },
            dismissButton = {
                TextButton(
                    onClick = {
                        openDeleteDialog = false
                    }
                ) {
                    Text("No")
                }
            }
        )
    }

    // ----------------------------------------------------------------
    // Bottom Sheet
    // ----------------------------------------------------------------

    if (openEditUserSheet.value) {
        UserEditSheet(
            showSheet = openEditUserSheet,
            userId = userId,
            onSuccess = {
                viewModel.getDetails(userId)
            }
        )
    }
}

@PreviewLightDark
@Composable
private fun UserDetailsScreenSkeletonPreview() {
    CMSAppTheme {
        UserDetailsScreenSkeleton(
            showLoading = false,
            showMessage = null,
            user = MockData.dummyUser
        )
    }
}

@Suppress("ktlint:compose:modifier-missing-check")
@Composable
fun UserDetailsScreenSkeleton(
    showLoading: Boolean,
    showMessage: Event<ActionMessage>?,
    user: User?,
    goBack: () -> Unit = {},
    toggleUIMode: () -> Unit = {},
    retryDataLoad: () -> Unit = {},
    onDeleteClick: () -> Unit = {},
    onEditClick: () -> Unit = {},
    onTodosClick: () -> Unit = {},
    onPostsClick: () -> Unit = {}
) {
    val snackbarHostState = remember { SnackbarHostState() }

    LaunchedEffect(showMessage, retryDataLoad) {
        showMessage?.getValueOnce()?.let { actionMessage ->
            when (actionMessage.action) {
                UserDetailsViewAction.USER_LOAD_ERROR -> {
                    val result = snackbarHostState.showSnackbar(
                        actionMessage.message,
                        actionLabel = "Retry"
                    )

                    if (result == SnackbarResult.ActionPerformed) {
                        retryDataLoad()
                    }
                }

                else -> {
                    snackbarHostState.showSnackbar(actionMessage.message)
                }
            }
        }
    }

    Scaffold(
        Modifier
            .navigationBarsPadding()
            .imePadding()
            .statusBarsPadding(),
        snackbarHost = { SnackbarHost(snackbarHostState) },
        topBar = {
            GeneralAppBar(
                subTitle = "User Details",
                goBack = goBack,
                toggleUIMode = toggleUIMode
            )
        }
    ) { innerPadding ->
        Column(
            Modifier
                .padding(innerPadding)
                .padding(horizontal = 16.dp)
                .fillMaxSize()
                .verticalScroll(rememberScrollState())
        ) {
            if (user == null) {
                LoadingItem(
                    Modifier
                        .padding(
                            top = 4.dp,
                            bottom = 16.dp
                        )
                )
            } else {
                UserItem(
                    modifier = Modifier.padding(
                        top = 4.dp
                    ),
                    name = user.name,
                    email = user.email,
                    gender = user.getGenderLabel(),
                    status = user.getStatusLabel(),
                    userImageUrl = user.getAvatarImageUrl(),
                    statusColor = user.getStatusColor(),
                    onClick = {}
                )
            }

            AnimatedVisibility(visible = user != null) {
                Column {
                    Spacer(Modifier.height(16.dp))

                    Row {
                        GeneralOutlinedButton(
                            caption = "Delete",
                            {
                                onDeleteClick()
                            },
                            modifier = Modifier.weight(1f),
                            icon = Icons.Rounded.Delete
                        )

                        Spacer(Modifier.width(16.dp))

                        GeneralOutlinedButton(
                            caption = "Edit",
                            {
                                onEditClick()
                            },
                            modifier = Modifier.weight(1f),
                            icon = Icons.Rounded.Edit
                        )
                    }

                    Row {
                        GeneralFilledButton(
                            caption = "Todos",
                            {
                                onTodosClick()
                            },
                            modifier = Modifier.weight(1f),
                            icon = Icons.Rounded.Ballot
                        )

                        Spacer(Modifier.width(16.dp))

                        GeneralFilledButton(
                            caption = "Posts",
                            {
                                onPostsClick()
                            },
                            modifier = Modifier.weight(1f),
                            icon = Icons.Rounded.TextSnippet
                        )
                    }
                }
            }
        }

        LoadingContainer(
            show = showLoading
        )
    }
}
