package ge.luka.melodia.data.database.typeconverters

import androidx.room.TypeConverter
import com.squareup.moshi.Moshi
import com.squareup.moshi.Types

object Converters {
    private val moshi = Moshi.Builder().build()
    private val mapType = Types.newParameterizedType(
        Map::class.java,
        String::class.java,
        String::class.java
    )
    private val adapter = moshi.adapter<Map<String, String>>(mapType)

    @TypeConverter
    @JvmStatic
    fun fromMap(map: Map<String, String>?): String? =
        map?.let { adapter.toJson(it) }

    @TypeConverter
    @JvmStatic
    fun toMap(json: String?): Map<String, String>? =
        json?.let { adapter.fromJson(it) }
}