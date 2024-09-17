package com.shahid.iqbal.screeny.features.wallpapers.data.local

import androidx.room.Database
import androidx.room.RoomDatabase
import com.shahid.iqbal.screeny.features.wallpapers.data.local.dao.PexelWallpaperDao
import com.shahid.iqbal.screeny.features.wallpapers.data.local.dao.PexelWallpaperRemoteKeysDao
import com.shahid.iqbal.screeny.features.wallpapers.models.Wallpaper

@Database(entities = [Wallpaper::class], version = 1)
abstract class PexelWallpaperDatabase : RoomDatabase() {

    abstract fun pexelWallpaperDao(): PexelWallpaperDao

    abstract fun pexelWallpaperRemoteKeysDao(): PexelWallpaperRemoteKeysDao
}