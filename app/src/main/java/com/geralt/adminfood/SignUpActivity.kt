package com.geralt.adminfood

import android.content.Intent
import android.os.Bundle
import android.util.Log
import android.widget.ArrayAdapter
import android.widget.Toast
import androidx.appcompat.app.AppCompatActivity
import com.geralt.adminfood.model.UserModel
import com.geralt.adminfood.databinding.ActivitySignUpBinding
import com.google.firebase.Firebase
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.auth.auth
import com.google.firebase.database.DatabaseReference
import com.google.firebase.database.database

class SignUpActivity : AppCompatActivity() {
    private lateinit var name: String
    private lateinit var restaurantName: String
    private lateinit var password: String
    private lateinit var email: String
    private lateinit var binding: ActivitySignUpBinding
    private lateinit var auth: FirebaseAuth
    private lateinit var database: DatabaseReference

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivitySignUpBinding.inflate(layoutInflater)
        setContentView(binding.root)

        auth = Firebase.auth
        database = Firebase.database.reference

        val locationList = arrayOf("Pakistan", "India", "USA", "Nepal")
        val adapter = ArrayAdapter(this, R.layout.location_list, locationList)
        val autoCompleteTextView = binding.listOfLocation
        autoCompleteTextView.setAdapter(adapter)

        binding.already.setOnClickListener {
            startActivity(Intent(this, LoginActivity::class.java))
            finish()
        }

        binding.signupBtn.setOnClickListener{
            name = binding.name.text.toString().trim()
            restaurantName = binding.address.text.toString().trim()
            email = binding.email.text.toString().trim()
            password = binding.password.text.toString().trim()

            if(name.isBlank() || restaurantName.isBlank() || password.isBlank() || email.isBlank() ){
                Toast.makeText(this, "Please fill all the fields", Toast.LENGTH_LONG).show()
            }else{
                createAccount(email, password)
            }

        }

    }

    private fun createAccount(email: String, password: String){
        auth.createUserWithEmailAndPassword(email, password).addOnCompleteListener{ task ->
            if(task.isSuccessful){
                Toast.makeText(this, "Account created successfully", Toast.LENGTH_SHORT).show()
                saveUserData()
                startActivity(Intent(this, MainActivity::class.java))
                finish()
            }else{
                Toast.makeText(this, "Account Creation Failed : ${task.exception}", Toast.LENGTH_SHORT).show()
                Log.d("Account", "createAccount Failed", task.exception)
            }
        }
    }

    private fun saveUserData(){
        name = binding.name.text.toString().trim()
        restaurantName = binding.address.text.toString().trim()
        email = binding.email.text.toString().trim()
        password = binding.password.text.toString().trim()

        val user = UserModel(name,restaurantName,email,password)
        val userId = FirebaseAuth.getInstance().currentUser!!.uid

        database.child("Users").child(userId).setValue(user)
    }
}