package com.example.registration

import android.os.Bundle
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import androidx.activity.enableEdgeToEdge
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Surface
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Modifier
import androidx.lifecycle.ViewModel
import androidx.lifecycle.ViewModelProvider
import androidx.room.Room
import com.example.registration.data.StudentDatabase
import com.example.registration.ui.LoginScreen
import com.example.registration.ui.StudentScreen
import com.example.registration.ui.theme.RegistrationTheme
import com.example.registration.viewmodel.LoginViewModel
import com.example.registration.viewmodel.StudentViewModel

class MainActivity : ComponentActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

        val database = Room.databaseBuilder(
            applicationContext,
            StudentDatabase::class.java,
            "student_db"
        )
            .fallbackToDestructiveMigration()
            .build()

        val studentViewModelFactory = object : ViewModelProvider.Factory {
            override fun <T : ViewModel> create(modelClass: Class<T>): T {
                @Suppress("UNCHECKED_CAST")
                return StudentViewModel(database.studentDao()) as T
            }
        }
        val studentViewModel = ViewModelProvider(this, studentViewModelFactory)[StudentViewModel::class.java]

        val loginViewModelFactory = object : ViewModelProvider.Factory {
            override fun <T : ViewModel> create(modelClass: Class<T>): T {
                @Suppress("UNCHECKED_CAST")
                return LoginViewModel() as T
            }
        }
        val loginViewModel = ViewModelProvider(this, loginViewModelFactory)[LoginViewModel::class.java]

        enableEdgeToEdge()
        setContent {
            RegistrationTheme {
                Surface(
                    modifier = Modifier.fillMaxSize(),
                    color = MaterialTheme.colorScheme.background
                ) {
                    var isLoggedIn by remember { mutableStateOf(false) }

                    if (isLoggedIn) {
                        StudentScreen(viewModel = studentViewModel)
                    } else {
                        LoginScreen(
                            viewModel = loginViewModel,
                            onLoginSuccess = {
                                isLoggedIn = true
                            }
                        )
                    }
                }
            }
        }
    }
}