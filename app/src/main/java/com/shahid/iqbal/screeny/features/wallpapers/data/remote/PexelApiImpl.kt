package com.shahid.iqbal.screeny.features.wallpapers.data.remote

import com.shahid.iqbal.screeny.features.wallpapers.data.utils.Constant.PER_PAGE_ITEMS
import com.shahid.iqbal.screeny.features.wallpapers.data.utils.HttpRoutes
import com.shahid.iqbal.screeny.features.wallpapers.models.PhotosResponse
import io.ktor.client.HttpClient
import io.ktor.client.call.body
import io.ktor.client.request.get
import io.ktor.client.request.parameter


class PexelApiImpl(private val httpClient: HttpClient) : PexelPhotoApi {

    override suspend fun getWallpapers(page: Int): PhotosResponse = httpClient.get(HttpRoutes.GET_WALLPAPERS) {
        parameter("page", page)
        parameter("per_page", PER_PAGE_ITEMS)
    }.body()

}