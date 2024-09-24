package com.shahid.iqbal.screeny.di

import coil.ImageLoader
import coil.disk.DiskCache
import coil.memory.MemoryCache
import com.shahid.iqbal.screeny.R
import com.shahid.iqbal.screeny.data.repositories.WallpaperRepository
import org.koin.dsl.module

val sharedWallpaperModule = module {

    single<WallpaperRepository> { WallpaperRepository(get(), get()) }

    single<ImageLoader> { ImageLoader.Builder(get()).crossfade(true).build() }
}