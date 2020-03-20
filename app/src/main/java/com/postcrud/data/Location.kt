package com.postcrud.data

class Location(
    private val lat: Double,
    private val lng: Double) {
    operator fun plus(other: Location) = Location(
        lat + other.lat, lng + other.lng
    )
    operator fun component1() = lat
    operator fun component2() = lng
}