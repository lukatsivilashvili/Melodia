package ge.luka.melodia.data.repository

import android.content.Context
import android.util.Log.d
import androidx.datastore.preferences.SharedPreferencesMigration
import androidx.datastore.preferences.core.booleanPreferencesKey
import androidx.datastore.preferences.core.edit
import androidx.datastore.preferences.core.intPreferencesKey
import androidx.datastore.preferences.preferencesDataStore
import ge.luka.melodia.domain.repository.DataStoreRepository
import ge.luka.melodia.presentation.ui.theme.themecomponents.AppTheme
import ge.luka.melodia.presentation.ui.theme.themecomponents.CURRENT_THEME
import ge.luka.melodia.presentation.ui.theme.themecomponents.IS_DARK_MODE
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.map
import javax.inject.Inject

private const val USER_PREFERENCES_NAME = "user_preferences"

private val Context.dataStore by preferencesDataStore(
    name = USER_PREFERENCES_NAME,
    produceMigrations = { context ->
        listOf(SharedPreferencesMigration(context, USER_PREFERENCES_NAME))
    }
)

class DataStoreRepositoryImpl @Inject constructor(
    private val context: Context
) : DataStoreRepository {


    private companion object {
        val isDarkModeKey = booleanPreferencesKey(IS_DARK_MODE)
        val currentThemeKey = intPreferencesKey(CURRENT_THEME)
    }

    override suspend fun setDarkMode(isDarkMode: Boolean) {
        context.dataStore.edit { preferences ->
            preferences[isDarkModeKey] = isDarkMode
        }
    }

    override suspend fun setCurrentTheme(theme: AppTheme) {
        context.dataStore.edit { preferences ->
            preferences[currentThemeKey] = theme.ordinal
        }
    }

    override fun getDarkMode(): Flow<Boolean> = context.dataStore.data.map { preferences ->
        d("datastore", "getDarkMode Invoked")
        preferences[isDarkModeKey] ?: false
    }

    override fun getCurrentTheme(): Flow<AppTheme> = context.dataStore.data.map { preferences ->
        d("datastore", "getTheme Invoked")
        AppTheme.entries[preferences[currentThemeKey] ?: 0]
    }


}