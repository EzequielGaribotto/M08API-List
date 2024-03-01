package com.example.retrofitapp.model

data class Biography (
    val fullName:String,
    val alterEgos:String,
    val placeOfBirth:String,
    val publisher:String,
    val aliases:List<String>,
)
