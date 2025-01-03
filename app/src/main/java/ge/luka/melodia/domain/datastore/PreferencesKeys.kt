package ge.luka.melodia.domain.datastore

import androidx.datastore.preferences.core.intPreferencesKey
import ge.luka.melodia.presentation.ui.theme.themecomponents.CURRENT_THEME

object PreferencesKeys {
    val CURRENT_THEME_KEY = intPreferencesKey(CURRENT_THEME)
}