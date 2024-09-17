package com.shahid.iqbal.screeny.features.wallpapers.data.paging

import androidx.paging.ExperimentalPagingApi
import androidx.paging.LoadType
import androidx.paging.PagingState
import androidx.paging.RemoteMediator
import androidx.room.withTransaction
import com.shahid.iqbal.screeny.features.wallpapers.data.local.PexelWallpaperDatabase
import com.shahid.iqbal.screeny.features.wallpapers.data.remote.PexelWallpapersApi
import com.shahid.iqbal.screeny.features.wallpapers.models.Wallpaper
import com.shahid.iqbal.screeny.features.wallpapers.models.WallpaperRemoteKeys

@OptIn(ExperimentalPagingApi::class)
class PexelWallpaperRemoteMediator(
    private val wallpaperDatabase: PexelWallpaperDatabase,
    private val pexelWallpapersApi: PexelWallpapersApi
) : RemoteMediator<Int, Wallpaper>() {

    private val wallpaperDao by lazy {
        wallpaperDatabase.pexelWallpaperDao()
    }

    private val remoteKeysDao by lazy {
        wallpaperDatabase.pexelWallpaperRemoteKeysDao()
    }


    override suspend fun load(loadType: LoadType, state: PagingState<Int, Wallpaper>): MediatorResult {

        return try {

            val currentPage = when (loadType) {
                LoadType.REFRESH -> {
                    val remoteKeys = getRemoteKeyClosestToCurrentPosition(state)
                    remoteKeys?.nextPage?.minus(1) ?: 1
                }

                LoadType.PREPEND -> {
                    val remoteKeys = getRemoteKeyForFirstItem(state)
                    val prevPage = remoteKeys?.prevPage ?: return MediatorResult.Success(
                        endOfPaginationReached = remoteKeys != null
                    )
                    prevPage
                }

                LoadType.APPEND -> {
                    val remoteKeys = getRemoteKeyForLastItem(state)
                    val nextPage = remoteKeys?.nextPage ?: return MediatorResult.Success(
                        endOfPaginationReached = remoteKeys != null
                    )
                    nextPage
                }
            }

            val response = pexelWallpapersApi.getWallpapers(page = currentPage)
            val endOfPaginationReached = response.isEmpty()


            val prevPage = if (currentPage == 1) null else currentPage - 1
            val nextPage = if (endOfPaginationReached) null else currentPage + 1

            wallpaperDatabase.withTransaction {
                if (loadType == LoadType.REFRESH) {
                    wallpaperDao.deleteAllWallpapers()
                    remoteKeysDao.deleteAllRemoteKeys()
                }
                val keys = response.map { wallpaper ->
                    WallpaperRemoteKeys(
                        id = wallpaper.id, prevPage = prevPage, nextPage = nextPage
                    )
                }
                remoteKeysDao.addAllRemoteKeys(remoteKeys = keys)
                wallpaperDao.addWallpapers(response)
            }
            MediatorResult.Success(endOfPaginationReached = endOfPaginationReached)

            MediatorResult.Success(true)
        } catch (e: Exception) {
            MediatorResult.Error(e)
        }
    }


    /**
     * Retrieves the remote key for the item closest to the user's current scroll position.
     *
     * @param state The current state of the paging system, which includes the position of items loaded.
     * @return The corresponding remote key for the item closest to the current scroll position, or null if not found.
     */
    private suspend fun getRemoteKeyClosestToCurrentPosition(
        state: PagingState<Int, Wallpaper>
    ): WallpaperRemoteKeys? {

        // Get the user's current position in the list (anchorPosition).
        return state.anchorPosition?.let { position ->

            // Find the  Wallpaper closest to that position.
            state.closestItemToPosition(position)?.id?.let { id ->

                // Use the wallpaper's ID to retrieve the corresponding remote key from the database.
                remoteKeysDao.getRemoteKeys(id = id)
            }
        }
    }

    /**
     * Retrieves the remote key for the first item in the loaded data pages.
     *
     * @param state The current state of the paging system, which includes the pages of items loaded.
     * @return The corresponding remote key for the first item in the first loaded page, or null if not found.
     */
    private suspend fun getRemoteKeyForFirstItem(
        state: PagingState<Int, Wallpaper>
    ): WallpaperRemoteKeys? {

        // Find the first page that contains data (not empty).
        return state.pages.firstOrNull { it.data.isNotEmpty() }?.data?.firstOrNull()?.let { wallpaper ->

            // Use the first wallpaper's ID to retrieve the corresponding remote key from the database.
            remoteKeysDao.getRemoteKeys(id = wallpaper.id)
        }
    }


    /**
     * Retrieves the remote key for the last item in the loaded data pages.
     *
     * @param state The current state of the paging system, which includes the pages of items loaded.
     * @return The corresponding remote key for the last item in the last loaded page, or null if not found.
     */
    private suspend fun getRemoteKeyForLastItem(
        state: PagingState<Int, Wallpaper>
    ): WallpaperRemoteKeys? {

        // Find the last page that contains data (not empty).
        return state.pages.lastOrNull { it.data.isNotEmpty() }?.data?.lastOrNull()?.let { wallpaper ->

            // Use the last Wallpaper's ID to retrieve the corresponding remote key from the database.
            remoteKeysDao.getRemoteKeys(id = wallpaper.id)
        }
    }

}