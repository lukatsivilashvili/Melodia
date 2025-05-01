package ge.luka.melodia.presentation.ui.screens.library

sealed class LibraryAction {
    data class LibraryTabClicked(val tabItem: TabScreen) : LibraryAction()
}