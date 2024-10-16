package com.shahid.iqbal.screeny.ui.screens.components

import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.fillMaxHeight
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.pager.PagerState
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.graphicsLayer
import androidx.compose.ui.unit.dp
import androidx.compose.ui.util.lerp
import coil.ImageLoader
import coil.compose.rememberAsyncImagePainter
import com.shahid.iqbal.screeny.ui.shared.SharedWallpaperViewModel
import kotlin.math.absoluteValue

@Composable
fun SinglePageContent(
    wallpaperUrl: String, imageLoader: ImageLoader, pagerState: PagerState, page: Int, sharedWallpaperViewModel: SharedWallpaperViewModel
) {


    Box(
        modifier = Modifier
            .carouselTransition(page, pagerState)
            .padding(10.dp),
        contentAlignment = Alignment.BottomCenter
    ) {


        WallpaperItem(
            wallpaper = wallpaperUrl, imageLoader = imageLoader, modifier = Modifier
                .fillMaxHeight(0.7f)
                .fillMaxWidth(),
            getDrawable = {
                if (page == pagerState.currentPage) {
                    sharedWallpaperViewModel.updateWallpaper(it)
                }
            }
        )
    }
}


@Composable
private fun Modifier.carouselTransition(page: Int, pagerState: PagerState) = graphicsLayer {
    val pageOffset = ((pagerState.currentPage - page) + pagerState.currentPageOffsetFraction).absoluteValue

    val transformation = lerp(
        start = 0.7f, stop = 1f, fraction = 1f - pageOffset.coerceIn(0f, 1f)
    )
    alpha = transformation
    scaleY = transformation
}