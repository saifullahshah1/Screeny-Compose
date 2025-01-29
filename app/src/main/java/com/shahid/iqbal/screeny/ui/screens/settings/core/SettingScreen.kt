package com.shahid.iqbal.screeny.ui.screens.settings.core

import androidx.compose.foundation.ExperimentalFoundationApi
import androidx.compose.foundation.LocalOverscrollConfiguration
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.wrapContentHeight
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.verticalScroll
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.CompositionLocalProvider
import androidx.compose.runtime.getValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.unit.dp
import androidx.lifecycle.compose.collectAsStateWithLifecycle
import androidx.navigation.NavController
import com.shahid.iqbal.screeny.R
import com.shahid.iqbal.screeny.ui.routs.Routs
import com.shahid.iqbal.screeny.ui.screens.settings.components.SettingItem
import com.shahid.iqbal.screeny.ui.screens.settings.utils.AppMode
import com.shahid.iqbal.screeny.ui.screens.settings.utils.findLanguageByCode
import org.koin.androidx.compose.koinViewModel
import java.util.Locale

@OptIn(ExperimentalFoundationApi::class)
@Composable
fun SettingScreen(
    navController: NavController,
    modifier: Modifier = Modifier,
    settingViewModel: SettingViewModel = koinViewModel()
) {

    val userPreference by settingViewModel.userPreference.collectAsStateWithLifecycle()


    CompositionLocalProvider(LocalOverscrollConfiguration provides null) {

        Column(
            modifier = modifier
                .fillMaxSize()
                .verticalScroll(rememberScrollState()), horizontalAlignment = Alignment.CenterHorizontally
        ) {

            Text(
                text = stringResource(R.string.general), maxLines = 1,
                style = MaterialTheme.typography.labelLarge.copy(fontWeight = FontWeight.Bold), modifier = Modifier
                    .fillMaxWidth(0.95f)
                    .wrapContentHeight(),
                textAlign = TextAlign.Start
            )

            Spacer(Modifier.height(10.dp))

            SettingItem(title = R.string.app_lanuage, description = if (Locale.getDefault().language.contains(userPreference.languageCode)) stringResource(R.string.system_default) else findLanguageByCode(userPreference.languageCode).languageName, icon = R.drawable.language_icon,
                onClick = { navController.navigate(Routs.Language) })

            SettingItem(title = R.string.dynamic_color, description =
            if (userPreference.shouldShowDynamicColor) stringResource(R.string.on).uppercase()
            else stringResource(R.string.off).uppercase(), icon = R.drawable.dynamic_color, onClick = {})

            SettingItem(title = R.string.app_mode, description = when (userPreference.appMode) {
                AppMode.LIGHT -> stringResource(R.string.light)
                AppMode.DARK -> stringResource(R.string.dark)
                AppMode.DEFAULT -> stringResource(R.string.system_default)

            }, icon = R.drawable.app_mode, onClick = {})


            Spacer(Modifier.height(10.dp))

            Text(
                text = stringResource(R.string.others), maxLines = 1,
                style = MaterialTheme.typography.labelLarge.copy(fontWeight = FontWeight.Bold), modifier = Modifier
                    .fillMaxWidth(0.95f)
                    .wrapContentHeight(),
                textAlign = TextAlign.Start
            )
            Spacer(Modifier.height(10.dp))

            SettingItem(title = R.string.share_app, description = null, icon = R.drawable.share_app_icon, onClick = {})
            SettingItem(title = R.string.rate_us, description = null, icon = R.drawable.rate_us_icon, onClick = {})
            SettingItem(title = R.string.feedback, description = null, icon = R.drawable.feeback_icon, onClick = {})
            SettingItem(title = R.string.privacy_policy, description = null, icon = R.drawable.privacy_policy_icon, onClick = {})
        }


    }

}

