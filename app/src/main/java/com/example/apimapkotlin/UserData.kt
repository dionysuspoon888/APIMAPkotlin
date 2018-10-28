package com.example.apimapkotlin

/**
 * Created by D on 10/28/2018.
 */
class UserData{
    var latitude:Double
   var longitude: Double
   var picture: String
   var _id: String
    var name: String
   var email: String

    constructor(latitude: Double, longitude: Double, picture: String, _id: String, name: String, email: String) {
        this.latitude = latitude
        this.longitude = longitude
        this.picture = picture
        this._id = _id
        this.name = name
        this.email = email
    }
}