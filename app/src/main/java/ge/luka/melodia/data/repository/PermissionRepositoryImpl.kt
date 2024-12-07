package ge.luka.melodia.data.repository

import ge.luka.melodia.domain.repository.PermissionRepository
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.flow

class PermissionRepositoryImpl : PermissionRepository {
    override fun getPermissionStatus(permissionEnabled: Boolean): Flow<Boolean> = flow {
        emit(permissionEnabled)
    }
}