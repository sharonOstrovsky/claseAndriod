package com.example.claseandroidbeta

import android.content.Intent
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.widget.Toast
import com.example.claseandroidbeta.databinding.ActivityMainBinding

class MainActivity : AppCompatActivity() {
    private lateinit var binding: ActivityMainBinding

    private var emailValido = false
    private var contraseniaValido = false

    private lateinit var dBmanager: DBmanager

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

        binding = ActivityMainBinding.inflate(layoutInflater)

        setContentView(binding.root)

        initListeners()


    }

    private fun initListeners() {
        binding.btnRegistrarse.setOnClickListener {
            val intent = Intent(this, RegistroActivity::class.java)
            startActivity(intent)
        }

        emailFocusListener()
        passwordFocusListener()
        submiteButton()


    }


    private fun emailFocusListener() {

        binding.etEmail.setOnFocusChangeListener { _, focus ->
            validateEmail()
            if (!focus) {
                validateEmail()
            } else {
                binding.etEmail.backgroundTintList = getColorStateList(R.color.gyl_dark)
            }
        }
    }

    //validar los colores del editText del email
    private fun validateEmail(){
        if (!android.util.Patterns.EMAIL_ADDRESS.matcher(binding.etEmail.text).matches()) {
            binding.etEmail.backgroundTintList = getColorStateList(R.color.wrong)
            emailValido = false
        } else {
            binding.etEmail.backgroundTintList = getColorStateList(R.color.grey)
            emailValido = true
        }
    }

    //validar si el editText de ls contraseña esta en focus
    private fun passwordFocusListener() {
        binding.etContrasenia.setOnFocusChangeListener { _, focus ->
            if (!focus) {
                validatePassword()
            } else {
                binding.etContrasenia.backgroundTintList = getColorStateList(R.color.gyl_dark)
            }
        }
    }

    //validar los colores del editText de la contraseña
    private fun validatePassword() {
        val passworText = binding.etContrasenia.text.toString()
        if (passworText.length <= 1) {
            contraseniaValido = false
        } else {
            contraseniaValido = true
            binding.etContrasenia.backgroundTintList = getColorStateList(R.color.grey)
        }
    }


    //click en el boton INGRESAR
    private fun submiteButton(){

        binding.btnIngresar.setOnClickListener {
            binding.etContrasenia.clearFocus()
            binding.etEmail.clearFocus()
            if(emailValido && contraseniaValido){
                logIn(binding.etEmail.text.toString(), binding.etContrasenia.text.toString())
            }
        }

    }

    private fun logIn(email : String, password : String){

        dBmanager = DBmanager(applicationContext)
        dBmanager.open()
        val userExists =  dBmanager.checkCredentials(email, password)
        dBmanager.close()

        if(userExists){
            redirectMenuActivity(email)
        }else{
            Toast.makeText(applicationContext, "usuario incorrecto", Toast.LENGTH_SHORT).show()
        }

    }

    //ir al menu activity
    private fun redirectMenuActivity(email : String) {
        val intent = Intent(this, MenuActivity::class.java)
        intent.putExtra("email", email)
        startActivity(intent)
    }


}