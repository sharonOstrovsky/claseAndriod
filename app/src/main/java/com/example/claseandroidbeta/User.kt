package com.example.claseandroidbeta

class User (
    var email : String,
    var password : String
        ){
    override fun toString(): String {
        return "User(email='$email', password='$password')"
    }
}