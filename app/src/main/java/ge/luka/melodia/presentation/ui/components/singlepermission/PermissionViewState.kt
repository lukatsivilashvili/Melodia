package ge.luka.melodia.presentation.ui.components.singlepermission

import ge.luka.melodia.domain.model.AlbumModel
import ge.luka.melodia.domain.model.ArtistModel
import ge.luka.melodia.domain.model.SongModel

data class PermissionViewState(
    val scanningSongState: List<SongModel>,
    val scanningAlbumState: List<AlbumModel>,
    val scanningArtistState: List<ArtistModel>,
    val scanningFinished: Boolean
)
