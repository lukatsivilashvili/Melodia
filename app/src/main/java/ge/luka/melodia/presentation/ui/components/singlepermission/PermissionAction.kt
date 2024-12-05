package ge.luka.melodia.presentation.ui.components.singlepermission

sealed interface PermissionAction {
    data object PermissionGranted: PermissionAction

}
