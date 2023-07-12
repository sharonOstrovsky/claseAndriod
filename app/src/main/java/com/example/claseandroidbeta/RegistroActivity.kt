package com.example.claseandroidbeta

import android.content.Intent
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.widget.Toast
import com.example.claseandroidbeta.databinding.ActivityRegistroBinding

class RegistroActivity : AppCompatActivity() {
    private lateinit var binding : ActivityRegistroBinding
    private var emailValido = false
    private var contraseniaValido = false

    private lateinit var dBmanager: DBmanager

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

        binding = ActivityRegistroBinding.inflate(layoutInflater)

        setContentView(binding.root)

        initListeners()

        //click en el boton "CANCELAR" para volver al mainActivity
        binding.btnCancelar.setOnClickListener{
            redirectMainActivity()
        }

    }

    private fun initListeners() {
        emailFocusListener()
        passwordFocusListener()
        submiteButton()
    }

    //validar si el editText de email esta en focus
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

    //valida que la contraseña y su confirmacion sean iguales
    private fun validateIdenticalPasswords(): Boolean {
        return binding.etRepetirContrasenia.text.toString() == binding.etContrasenia.text.toString()
    }


    //click en el boton CREAR
    private fun submiteButton(){

        binding.btnCrear.setOnClickListener {
            if(emailValido && contraseniaValido && validateIdenticalPasswords()){
                creatUser(binding.etEmail.text.toString(), binding.etContrasenia.text.toString())
                //redirectMainActivity()
            }else if(!validateIdenticalPasswords()){
                Toast.makeText(applicationContext, "Contraseñas diferentes", Toast.LENGTH_SHORT).show()
            }
        }


    }

    //si el usuario no existe crea el nuevo usuario y lo guarda en la base de datos
    private fun creatUser(email : String, password : String) : Boolean{
        dBmanager = DBmanager(applicationContext)
        dBmanager.open()

        val existingUser = dBmanager.checkCredentials(email, password)

        if(!existingUser){
            val usuario = User(email, password)
            dBmanager.insertModelUser(usuario)
            dBmanager.close()
            //redirectMainActivity()
            Toast.makeText(applicationContext, "Usuario creado", Toast.LENGTH_SHORT).show()
            return true
        }else{
            dBmanager.close()
            Toast.makeText(applicationContext, "Hubo un problema", Toast.LENGTH_SHORT).show()
            return false
        }

    }

    //ir al main activity
    private fun redirectMainActivity() {
        val intent = Intent(this, MainActivity::class.java)
        startActivity(intent)
    }





}