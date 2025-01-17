package com.shahid.iqbal.screeny.data.repositories

import com.shahid.iqbal.screeny.data.local.dao.UserPreferenceDao
import com.shahid.iqbal.screeny.ui.screens.settings.language.utils.LanguageEntity
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.withContext

class UserPreferenceRepo (private val dao: UserPreferenceDao) {

    val uerPreference get() = dao.getUserPreference()


    suspend fun updateAppLanguage(languageEntity: LanguageEntity) = withContext(Dispatchers.IO){
        dao.updateLanguage(languageEntity.languageCode)
    }
}