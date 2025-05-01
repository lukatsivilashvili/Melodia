package ge.luka.melodia.presentation.ui.screens.library

import androidx.compose.runtime.Composable

data class LibraryViewState(
    val selectedTabIndex: Int,
    val currentTabScreen: String
)

data class TabScreen(val title: String, val screen: @Composable () -> Unit)