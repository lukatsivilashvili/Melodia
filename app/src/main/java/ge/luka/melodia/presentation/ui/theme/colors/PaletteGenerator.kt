package ge.luka.melodia.presentation.ui.theme.colors

import android.content.Context
import android.graphics.Bitmap
import android.graphics.BitmapFactory
import androidx.core.net.toUri
import androidx.palette.graphics.Palette
import java.io.IOException

object PaletteGenerator {

    fun convertImageToBitmap(
        imageUrl: String,
        context: Context
    ): Bitmap? {
        return try {
            val albumArtUri = imageUrl.toUri()
            context.contentResolver.openInputStream(albumArtUri)?.use { inputStream ->
                BitmapFactory.decodeStream(inputStream)
            }
        } catch (e: IOException) {
            e.printStackTrace()
            null
        }
    }

    fun extractColorsFromBitmap(bitmap: Bitmap): Map<String, String> {
        val maximumColorCount = 10
        return mapOf(
            "vibrant" to parseColorSwatch(
                color = Palette.from(bitmap).maximumColorCount(maximumColorCount)
                    .generate().vibrantSwatch
            ),
            "darkVibrant" to parseColorSwatch(
                color = Palette.from(bitmap).maximumColorCount(maximumColorCount)
                    .generate().darkVibrantSwatch
            ),
            "onDarkVibrant" to parseBodyColor(
                color = Palette.from(bitmap).maximumColorCount(maximumColorCount)
                    .generate().darkVibrantSwatch?.bodyTextColor
            ),
            "lightVibrant" to parseColorSwatch(
                color = Palette.from(bitmap).maximumColorCount(maximumColorCount)
                    .generate().lightVibrantSwatch
            ),
            "domainSwatch" to parseColorSwatch(
                color = Palette.from(bitmap).maximumColorCount(maximumColorCount)
                    .generate().dominantSwatch
            ),
            "mutedSwatch" to parseColorSwatch(
                color = Palette.from(bitmap).maximumColorCount(maximumColorCount)
                    .generate().mutedSwatch
            ),
            "lightMuted" to parseColorSwatch(
                color = Palette.from(bitmap).maximumColorCount(maximumColorCount)
                    .generate().lightMutedSwatch
            ),
            "darkMuted" to parseColorSwatch(
                color = Palette.from(bitmap).maximumColorCount(maximumColorCount)
                    .generate().darkMutedSwatch
            ),
        )
    }

    private fun parseColorSwatch(color: Palette.Swatch?): String {
        return if (color != null) {
            val parsedColor = Integer.toHexString(color.rgb)
            return "#$parsedColor"
        } else {
            "#000000"
        }
    }

    private fun parseBodyColor(color: Int?): String {
        return if (color != null) {
            val parsedColor = Integer.toHexString(color)
            "#$parsedColor"
        } else {
            "#FFFFFF"
        }
    }


}