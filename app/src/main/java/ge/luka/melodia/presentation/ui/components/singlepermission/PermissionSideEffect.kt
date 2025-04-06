package ge.luka.melodia.presentation.ui.components.singlepermission

sealed interface PermissionSideEffect {
    data class PermissionGranted(val folderUri: String?): PermissionSideEffect
}
