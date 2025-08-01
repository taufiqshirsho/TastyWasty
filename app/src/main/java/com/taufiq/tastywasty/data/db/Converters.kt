package com.taufiq.tastywasty.data.db
import androidx.room.TypeConverter

class Converters {
    @TypeConverter
    fun fromStringList(list: List<String>): String {
        return list.joinToString(",")
    }

    @TypeConverter
    fun toStringList(data: String): List<String> {
        return if (data.isEmpty()) emptyList() else data.split(",")
    }
}
