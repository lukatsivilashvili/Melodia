package ge.luka.melodia.data.repository

import ge.luka.melodia.domain.datastore.DataStoreManager
import ge.luka.melodia.domain.datastore.PreferencesKeys
import ge.luka.melodia.domain.repository.ThemeRepository
import ge.luka.melodia.presentation.ui.theme.themecomponents.AppTheme
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.map
import javax.inject.Inject

class ThemeRepositoryImpl @Inject constructor(
    private val dataStoreManager: DataStoreManager
) : ThemeRepository {
    override suspend fun setDarkMode(isDarkMode: Boolean) {
        dataStoreManager.setPreference(key = PreferencesKeys.IS_DARK_MODE_KEY, value = isDarkMode)

    }

    override suspend fun setCurrentTheme(theme: AppTheme) {
        dataStoreManager.setPreference(
            key = PreferencesKeys.CURRENT_THEME_KEY,
            value = theme.ordinal
        )
    }

    override suspend fun getDarkMode(): Flow<Boolean> =
        dataStoreManager.getPreference(PreferencesKeys.IS_DARK_MODE_KEY, defaultValue = false)
            .map { isDarkMode ->
                isDarkMode
            }

    override suspend fun getCurrentTheme(): Flow<AppTheme> =
        dataStoreManager.getPreference(PreferencesKeys.CURRENT_THEME_KEY, defaultValue = 0)
            .map { currentTheme ->
                AppTheme.entries[currentTheme]
            }
}