package ge.luka.melodia.presentation.ui.components.singlepermission

import ge.luka.melodia.domain.model.SongModel

data class PermissionViewState(
    val scanningState: List<SongModel>,
    val scanningFinished: Boolean
)
