package ge.luka.melodia.domain.repository

import kotlinx.coroutines.flow.Flow

interface PermissionRepository {
    fun getPermissionStatus(permissionEnabled: Boolean): Flow<Boolean>
}