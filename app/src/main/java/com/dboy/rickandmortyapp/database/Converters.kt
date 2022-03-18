package com.dboy.rickandmortyapp.database

import androidx.room.TypeConverter
import com.dboy.rickandmortyapp.api.response.Location
import com.dboy.rickandmortyapp.api.response.Origin

class Converters {
    @TypeConverter
    fun listFromString(stringListString: String): List<String> {  //from db to object
        return stringListString.split(",").map { it }
    }

    @TypeConverter
    fun listToString(stringList: List<String>): String {  //to save on database
        return stringList.joinToString(separator = ",")
    }

    @TypeConverter
    fun locationToString(location: Location): String {
        return "${location.name},${location.url}"
    }

    @TypeConverter
    fun stringToLocation(locationString: String): Location {
        val location = locationString.split(",").map { it }
        return Location(name = location[0], url = location[1])
    }

    @TypeConverter
    fun originToString(origin: Origin): String {
        return "${origin.name},${origin.url}"
    }

    @TypeConverter
    fun stringToOrigin(originString: String): Origin {
        val origin = originString.split(",").map { it }
        return Origin(name = origin[0], url = origin[1])
    }
}