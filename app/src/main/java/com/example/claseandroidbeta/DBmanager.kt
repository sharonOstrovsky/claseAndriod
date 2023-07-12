package com.example.claseandroidbeta


import android.content.ContentValues
import android.content.Context
import android.database.sqlite.SQLiteDatabase
import android.util.Log
import java.sql.SQLException

class DBmanager(context : Context?) {

    private val _connection : DBconnection
    private var _baseDeDatos : SQLiteDatabase? = null

    init {
        _connection = DBconnection(context)
    }

    @Throws(SQLException::class)
    fun open(): DBmanager {
        _baseDeDatos = _connection.writableDatabase
        _connection.onCreate(_baseDeDatos!!)
        //_connection.onUpgrade(_baseDeDatos,0,1)
        return this
    }

    fun close() {
        _connection.close()
    }

    companion object {
        const val USER_TABLE_NAME = "user"
        const val USER_COLUMN_EMAIL = "email"
        const val USER_COLUMN_PASSWORD = "password"
        const val TABLE_USER_CREATE =
            "create table if not exists user(email text not null, password text not null);"
    }

    fun insertModelUser(
        user : User
    ) : Boolean{
        val contentValues = ContentValues()
        contentValues.put(USER_COLUMN_EMAIL, user.email)
        contentValues.put(USER_COLUMN_PASSWORD, user.password)
        val result =  _baseDeDatos?.insert(USER_TABLE_NAME, null,contentValues)
        return if(result==-1L){
            Log.d("insercion: ", "Incorrecta")
            false
        }else{
            Log.d("insercion: ", "Correcta")
            true
        }

    }



    fun checkCredentials(email: String, password: String): Boolean {
       // val query = "SELECT * FROM $USER_TABLE_NAME WHERE email='$email' AND password='$password'"
        val cursor =  _baseDeDatos?.rawQuery("select * from $USER_TABLE_NAME where email='$email' AND password='$password'", null)

        //val cursor = _baseDeDatos?.rawQuery(query, null)
        val matchFound = cursor?.count!! > 0
        cursor.close()
        return matchFound
    }

    fun getUsers() : List<User>{
        val cursor =  _baseDeDatos?.rawQuery("select * from $USER_TABLE_NAME", null)
        val storage  = mutableListOf<User>()
        if (cursor != null) {
            if(cursor.moveToFirst()){
                do{
                    storage.add(User(cursor.getString(0), cursor.getString(1)))
                }while(cursor.moveToNext())

            }
        }
        cursor?.close()
        return storage
    }


    fun searchUser(email : String) : User{
        var user : User = User("","")
        val cursor =  _baseDeDatos?.rawQuery("select * from $USER_TABLE_NAME where email='$email'", null)
        if (cursor != null) {
            if (cursor.moveToFirst()) {
                do {
                    user.email = cursor.getString(0)
                    user.password = cursor.getString(1)
                }while(cursor.moveToNext())
            }
        }
        cursor?.close()
        return user
    }

    fun deleteAllUserDataByEmail(email: String?){
        _baseDeDatos?.delete(USER_TABLE_NAME, "email='$email'",null)
    }

    fun deleteAllUserDataById(id: Int?){
        _baseDeDatos?.delete(USER_TABLE_NAME, "id=$id",null)
    }


}