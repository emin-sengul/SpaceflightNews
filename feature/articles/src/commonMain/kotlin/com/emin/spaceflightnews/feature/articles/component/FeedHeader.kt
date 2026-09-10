package com.emin.spaceflightnews.feature.articles.component

import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.unit.dp
import com.emin.spaceflightnews.core.designsystem.component.BrandLogo
import com.emin.spaceflightnews.core.designsystem.theme.SpaceflightBrand

@Composable
internal fun FeedHeader(
    query: String,
    onQueryChange: (String) -> Unit,
    modifier: Modifier = Modifier,
) {
    Column(modifier = modifier.padding(bottom = 6.dp)) {
        Row(
            verticalAlignment = Alignment.CenterVertically,
            modifier = Modifier.padding(top = 10.dp, bottom = 12.dp),
        ) {
            BrandTile()

            Text(
                text = "Spaceflight News",
                style = MaterialTheme.typography.headlineSmall,
                color = MaterialTheme.colorScheme.onBackground,
                modifier = Modifier.padding(start = 12.dp),
            )
        }

        SearchField(
            query = query,
            onQueryChange = onQueryChange,
            modifier = Modifier.fillMaxWidth(),
        )
    }
}

@Composable
private fun BrandTile(modifier: Modifier = Modifier) {
    Box(
        modifier = modifier
            .size(TILE_SIZE)
            .clip(RoundedCornerShape(12.dp))
            .background(SpaceflightBrand.gradient),
        contentAlignment = Alignment.Center,
    ) {
        BrandLogo(modifier = Modifier.size(LOGO_SIZE))
    }
}

private val TILE_SIZE = 38.dp
private val LOGO_SIZE = 34.dp

