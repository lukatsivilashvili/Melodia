package ge.luka.melodia.domain.datastore

import androidx.datastore.preferences.core.Preferences
import kotlinx.coroutines.flow.Flow

interface DataStoreManager {
    suspend fun <T> getPreference(key: Preferences.Key<T>, defaultValue: T): Flow<T>
    suspend fun <T> setPreference(key: Preferences.Key<T>, value: T)
    suspend fun removePreferences(keys: List<Preferences.Key<*>>)
}