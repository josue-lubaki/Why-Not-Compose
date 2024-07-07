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

package org.imaginativeworld.whynotcompose.cms.ui.screens.post.list

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import androidx.paging.Pager
import androidx.paging.PagingConfig
import androidx.paging.PagingData
import androidx.paging.cachedIn
import dagger.hilt.android.lifecycle.HiltViewModel
import javax.inject.Inject
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.flow.catch
import kotlinx.coroutines.flow.combine
import kotlinx.coroutines.flow.emptyFlow
import kotlinx.coroutines.launch
import org.imaginativeworld.whynotcompose.base.models.Event
import org.imaginativeworld.whynotcompose.cms.datasource.PostPagingSource
import org.imaginativeworld.whynotcompose.cms.models.Post
import org.imaginativeworld.whynotcompose.cms.repositories.PostRepository

@HiltViewModel
class PostListViewModel @Inject constructor(
    private val postRepository: PostRepository
) : ViewModel() {
    private val eventShowLoading = MutableStateFlow(false)
    private val eventShowMessage = MutableStateFlow<Event<String>?>(null)
    private var items = MutableStateFlow<Flow<PagingData<Post>>>(emptyFlow())

    // ----------------------------------------------------------------

    private val _state = MutableStateFlow(PostListViewState())
    val state = _state.asStateFlow()

    // ----------------------------------------------------------------

    init {
        viewModelScope.launch {
            combine(
                eventShowLoading,
                eventShowMessage,
                items
            ) { showLoading, showMessage, items ->

                PostListViewState(
                    loading = showLoading,
                    message = showMessage,
                    items = items
                )
            }.catch { throwable ->
                // TODO: emit a UI error here. For now we'll just rethrow
                throw throwable
            }.collect {
                _state.value = it
            }
        }
    }

    // ----------------------------------------------------------------

    fun loadPosts(userId: Int) {
        items.value = Pager(PagingConfig(pageSize = 20)) {
            PostPagingSource(userId, postRepository)
        }
            .flow
            .cachedIn(viewModelScope)
    }
}

data class PostListViewState(
    val loading: Boolean = false,
    val message: Event<String>? = null,
    val items: Flow<PagingData<Post>> = emptyFlow()
)
