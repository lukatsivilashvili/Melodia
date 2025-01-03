package ge.luka.melodia.domain.repository

import ge.luka.melodia.presentation.ui.theme.themecomponents.AppTheme
import kotlinx.coroutines.flow.Flow

interface ThemeRepository {
    suspend fun setCurrentTheme(theme: AppTheme)
    suspend fun getCurrentTheme(): Flow<AppTheme>
}