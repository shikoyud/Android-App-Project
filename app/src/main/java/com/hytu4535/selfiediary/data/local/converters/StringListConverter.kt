package com.hytu4535.selfiediary.data.local.converters

import androidx.room.TypeConverter

/**
 * TypeConverter for Room Database to convert List<String> to String and vice versa
 */
class StringListConverter {

    @TypeConverter
    fun fromStringList(value: List<String>?): String {
        return value?.joinToString(",") ?: ""
    }

    @TypeConverter
    fun toStringList(value: String?): List<String> {
        if (value.isNullOrEmpty()) return emptyList()
        return value.split(",").filter { it.isNotEmpty() }
    }
}

