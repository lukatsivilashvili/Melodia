package ge.luka.melodia.presentation.ui.components.singlepermission

sealed interface PermissionAction {
    data class PermissionGranted(val folderUri: String?): PermissionAction

}
