package ge.luka.melodia.common.utils

import android.content.ContentValues
import android.content.Context
import android.provider.MediaStore

object FileHelper {
    fun createAppDirectory(context: Context, folderName: String): Boolean {
        try {
            // First check if directory already exists by trying to query it
            val existingQuery = context.contentResolver.query(
                MediaStore.Audio.Media.EXTERNAL_CONTENT_URI,
                arrayOf(MediaStore.MediaColumns.RELATIVE_PATH),
                "${MediaStore.MediaColumns.RELATIVE_PATH} LIKE ?",
                arrayOf("Music/$folderName%"),
                null
            )

            // If directory exists, return true - no need to create
            if (existingQuery?.moveToFirst() == true) {
                existingQuery.close()
                return true
            }
            existingQuery?.close()

            // If we get here, directory doesn't exist, so create it
            val values = ContentValues().apply {
                put(MediaStore.MediaColumns.RELATIVE_PATH, "Music/$folderName")
            }

            // Create directory by creating and deleting a temporary file
            val uri = context.contentResolver.insert(
                MediaStore.Audio.Media.EXTERNAL_CONTENT_URI,
                values
            )

            // Delete the temporary file but keep the directory
            uri?.let { context.contentResolver.delete(it, null, null) }

            return true
        } catch (e: Exception) {
            e.printStackTrace()
            return false
        }
    }
}