/*
 * Copyright 2021 Md. Mahmudul Hasan Shohag
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

package org.imaginativeworld.whynotcompose.repositories

import org.imaginativeworld.whynotcompose.base.models.User
import org.imaginativeworld.whynotcompose.base.models.github.GithubRepo
import org.imaginativeworld.whynotcompose.base.models.github.Owner
import org.imaginativeworld.whynotcompose.models.ListItem

object MockData {
    val dummyUser = User(
        id = 1,
        name = "Md. Mahmudul Hasan Shohag"
    )

    val dummyListItem = listOf(
        ListItem(id = 1, name = "Cupcake"),
        ListItem(id = 2, name = "Donut"),
        ListItem(id = 3, name = "Eclair"),
        ListItem(id = 4, name = "Froyo"),
        ListItem(id = 5, name = "Gingerbread"),
        ListItem(id = 6, name = "Honeycomb"),
        ListItem(id = 7, name = "Ice cream sandwich"),
        ListItem(id = 8, name = "Jelly bean"),
        ListItem(id = 9, name = "KitKat"),
        ListItem(id = 10, name = "Lollipop"),
        ListItem(id = 11, name = "Marshmallow"),
        ListItem(id = 12, name = "Nougat"),
        ListItem(id = 13, name = "Oreo"),
        ListItem(id = 14, name = "Pie")
    )

    val dummyGithubRepo = GithubRepo(
        id = 1,
        name = "Why-Not-Compose",
        fullName = "ImaginativeShohag/Why-Not-Compose",
        owner = Owner(
            login = "ImaginativeShohag",
            avatarUrl = ""
        ),
        description = "A collection of animations, compositions, UIs using Jetpack Compose. You can say Jetpack Compose cookbook or play-ground if you want!",
        stargazersCount = 100,
        watchersCount = 200,
        forksCount = 300
    )
}
