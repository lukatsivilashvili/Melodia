package ge.luka.melodia.domain.repository

import ge.luka.melodia.presentation.ui.theme.AppTheme
import kotlinx.coroutines.flow.Flow

interface DataStoreRepository {
    suspend fun setDarkMode(isDarkMode: Boolean)
    suspend fun setCurrentTheme(theme: AppTheme)
    fun getDarkMode(): Flow<Boolean>
    fun getCurrentTheme(): Flow<AppTheme>
}