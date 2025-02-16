package com.shahid.iqbal.screeny.ui.screens.wallpapers

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.shahid.iqbal.screeny.data.repositories.FavouriteRepo
import com.shahid.iqbal.screeny.models.CommonWallpaperEntity
import com.shahid.iqbal.screeny.utils.WallpaperDownloader
import kotlinx.coroutines.flow.SharingStarted
import kotlinx.coroutines.flow.stateIn
import kotlinx.coroutines.launch

class ActionViewModel(
    private val favouriteRepo: FavouriteRepo,
    private val downloader: WallpaperDownloader
) : ViewModel() {


    val getAllFavourites
        get() = favouriteRepo.getAllFavourites.stateIn(viewModelScope, SharingStarted.WhileSubscribed(5000), emptyList())

    fun addOrRemove(wallpaper: CommonWallpaperEntity) {
        viewModelScope.launch {
            favouriteRepo.addOrRemove(wallpaper)
        }
    }


    fun downloadWallpaper(url: String) {
        downloader.downloadFile(url)
    }
}