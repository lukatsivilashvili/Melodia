package ge.luka.melodia.domain.datastore

import androidx.datastore.preferences.core.booleanPreferencesKey
import androidx.datastore.preferences.core.intPreferencesKey
import ge.luka.melodia.presentation.ui.theme.themecomponents.CURRENT_THEME
import ge.luka.melodia.presentation.ui.theme.themecomponents.IS_DARK_MODE

object PreferencesKeys {
    val IS_DARK_MODE_KEY = booleanPreferencesKey(IS_DARK_MODE)
    val CURRENT_THEME_KEY = intPreferencesKey(CURRENT_THEME)
}