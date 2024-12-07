package ge.luka.melodia.presentation.ui.components.singlepermission

sealed interface PermissionSideEffect {
    data object PermissionGranted: PermissionSideEffect
}
