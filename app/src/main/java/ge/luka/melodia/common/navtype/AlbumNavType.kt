package ge.luka.melodia.common.navtype

import android.net.Uri
import android.os.Bundle
import androidx.navigation.NavType
import ge.luka.melodia.domain.model.AlbumModel
import kotlinx.serialization.encodeToString
import kotlinx.serialization.json.Json

object AlbumNavType {
    val AlbumType = object : NavType<AlbumModel>(isNullableAllowed = false) {
        override fun get(bundle: Bundle, key: String): AlbumModel? {
            return Json.decodeFromString(bundle.getString(key) ?: return null)
        }

        override fun parseValue(value: String): AlbumModel {
            return Json.decodeFromString(Uri.decode(value))
        }

        override fun serializeAsValue(value: AlbumModel): String {
            return Uri.encode(Json.encodeToString(value))
        }

        override fun put(bundle: Bundle, key: String, value: AlbumModel) {
            bundle.putString(key, Json.encodeToString(value))
        }

    }
}