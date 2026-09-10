package com.emin.spaceflightnews.core.database.entity

import androidx.room.TypeConverter
import com.emin.spaceflightnews.core.common.joinNotBlank
import com.emin.spaceflightnews.core.common.takeIfNotBlank

class AuthorListConverter {
    @TypeConverter
    fun fromAuthors(authors: List<String>): String = authors.joinNotBlank(SEPARATOR)

    @TypeConverter
    fun toAuthors(value: String): List<String> =
        value.takeIfNotBlank()?.split(SEPARATOR).orEmpty()

    private companion object {
        const val SEPARATOR = " | "
    }
}
