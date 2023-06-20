package org.imaginativeworld.whynotcompose.cms.ui.screens.comment.list.elements

import android.content.res.Configuration
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.material3.Card
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Surface
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.text.style.TextOverflow
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import coil.compose.AsyncImage
import org.imaginativeworld.whynotcompose.cms.repositories.MockData
import org.imaginativeworld.whynotcompose.cms.theme.CMSAppTheme
import org.imaginativeworld.whynotcompose.common.compose.R

@Composable
fun CommentItem(
    modifier: Modifier = Modifier,
    name: String,
    email: String,
    body: String,
    userImageUrl: String,
    isPreview: Boolean,
    onClick: () -> Unit = {}
) {
    Card(
        onClick = onClick,
        modifier = modifier
            .fillMaxWidth()
    ) {
        Column(Modifier.padding(16.dp)) {
            Text(
                body,
                style = MaterialTheme.typography.titleLarge,
                overflow = TextOverflow.Ellipsis,
                maxLines = if (isPreview) 1 else Int.MAX_VALUE
            )

            Row(verticalAlignment = Alignment.CenterVertically) {
                AsyncImage(
                    modifier = Modifier
                        .padding(end = 8.dp)
                        .size(16.dp)
                        .clip(CircleShape),
                    model = userImageUrl,
                    contentDescription = "User Image",
                    placeholder = painterResource(id = R.drawable.default_placeholder)
                )

                Text(
                    name,
                    style = MaterialTheme.typography.labelLarge,
                    overflow = TextOverflow.Ellipsis,
                    maxLines = 1
                )
            }

            Text(
                email,
                style = MaterialTheme.typography.labelLarge,
                overflow = TextOverflow.Ellipsis,
                maxLines = 1
            )
        }
    }
}

@Preview(showBackground = true)
@Composable
fun CommentItemPreview() {
    CMSAppTheme {
        Surface {
            Column(Modifier.padding(16.dp)) {
                CommentItem(
                    name = MockData.dummyComment.name,
                    email = MockData.dummyComment.email,
                    body = MockData.dummyComment.body,
                    isPreview = true,
                    userImageUrl = MockData.dummyComment.getAvatarImageUrl()
                )

                Spacer(Modifier.height(16.dp))

                CommentItem(
                    name = MockData.dummyComment.name,
                    email = MockData.dummyComment.email,
                    body = MockData.dummyComment.body,
                    isPreview = false,
                    userImageUrl = MockData.dummyComment.getAvatarImageUrl()
                )
            }
        }
    }
}

@Preview(showBackground = true, uiMode = Configuration.UI_MODE_NIGHT_YES)
@Composable
fun CommentItemPreviewDark() {
    CMSAppTheme {
        Surface {
            Column(Modifier.padding(16.dp)) {
                CommentItem(
                    name = MockData.dummyComment.name,
                    email = MockData.dummyComment.email,
                    body = MockData.dummyComment.body,
                    isPreview = true,
                    userImageUrl = MockData.dummyComment.getAvatarImageUrl()
                )

                Spacer(Modifier.height(16.dp))

                CommentItem(
                    name = MockData.dummyComment.name,
                    email = MockData.dummyComment.email,
                    body = MockData.dummyComment.body,
                    isPreview = false,
                    userImageUrl = MockData.dummyComment.getAvatarImageUrl()
                )
            }
        }
    }
}
