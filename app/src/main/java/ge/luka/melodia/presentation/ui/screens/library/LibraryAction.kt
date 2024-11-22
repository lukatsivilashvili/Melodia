package ge.luka.melodia.presentation.ui.screens.library

sealed class LibraryAction {
    data class LibraryItemClicked(val libraryItem: String) : LibraryAction()
}