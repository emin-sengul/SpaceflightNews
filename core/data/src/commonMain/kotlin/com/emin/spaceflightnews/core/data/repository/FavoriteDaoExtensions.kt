package com.emin.spaceflightnews.core.data.repository

import com.emin.spaceflightnews.core.common.isNotNull
import com.emin.spaceflightnews.core.database.dao.FavoriteDao
import kotlinx.coroutines.flow.first

internal suspend fun FavoriteDao.favoriteIdSet(): Set<Long> = observeIds().first().toSet()

internal suspend fun FavoriteDao.isFavorite(id: Long): Boolean = observeById(id).first().isNotNull()
