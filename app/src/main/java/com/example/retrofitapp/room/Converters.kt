package com.example.retrofitapp.room

import androidx.room.TypeConverter
import com.example.retrofitapp.model.Biography
import com.example.retrofitapp.model.Images
import com.example.retrofitapp.model.Powerstats


class Converters {

    @TypeConverter
    fun fromListToString(list: List<String>): String {
        return list.joinToString(", ")
    }

    @TypeConverter
    fun fromStringToList(string: String): List<String> {
        return string.trim().split(", ")
    }

    @TypeConverter
    fun fromBiographyToString(biography: Biography): String {
        val fullName = biography.fullName.replace(",", ";")
        val alterEgos = biography.alterEgos.replace(",", ";")
        val placeOfBirth = biography.placeOfBirth.replace(",", ";")
        val publisher = biography.publisher.replace(",", ";")
        val aliases = biography.aliases.joinToString(";")
        return "$fullName, $alterEgos, $placeOfBirth, $publisher, $aliases"
    }

    @TypeConverter
    fun fromStringToBiography(text: String): Biography {
        val input = text.split(", ")
        val fullName = input[0].replace(";", ",")
        val alterEgos = input[1].replace(";", ",")
        val placeOfBirth = input[2].replace(";", ",")
        val publisher = input[3].replace(";", ",")
        val aliases =
            if (input.size >= 5) {
                input[4].split(";")
            } else {
                emptyList()
            }
        return Biography(fullName, alterEgos, placeOfBirth, publisher, aliases)
    }


    @TypeConverter
    fun fromImagesToString(images: Images): String {
        return "${images.lg}, ${images.md}, ${images.sm}, ${images.xs}"
    }

    @TypeConverter
    fun fromStringToImages(text: String): Images {
        val input = text.trim().split(", ")
        return Images(input[0], input[1], input[2], input[3])
    }

    @TypeConverter
    fun fromPowerstatsToString(powerstats: Powerstats): String {

        return "${powerstats.combat}, ${powerstats.durability}, ${powerstats.intelligence}, ${powerstats.power}, ${powerstats.speed}, ${powerstats.strength}"
    }

    @TypeConverter
    fun fromStringToPowerstats(text: String): Powerstats {
        val input = text.trim().split(", ").map { it.toInt() }.toTypedArray()
        return Powerstats(input[0], input[1], input[2], input[3], input[4], input[5])
    }
}