
package com.ahona.studentmanagerpro27

import android.os.Bundle
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import androidx.lifecycle.viewmodel.compose.viewModel
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Surface
import androidx.compose.ui.Modifier
import com.ahona.studentmanagerpro27.data.DatabaseProvider
import com.ahona.studentmanagerpro27.data.StudentRepositoryImpl
import com.ahona.studentmanagerpro27.user_interface.StudentScreen
import com.ahona.studentmanagerpro27.viewmodel.StudentViewModel
import com.ahona.studentmanagerpro27.viewmodel.StudentViewModelFactory

class MainActivity : ComponentActivity() {

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

        val database = DatabaseProvider.getDatabase(applicationContext)
        val repository = StudentRepositoryImpl(database.studentDao())
        val factory = StudentViewModelFactory(repository)

        setContent {
            val studentViewModel: StudentViewModel = viewModel(
                factory = factory
            )

            MaterialTheme {
                Surface(modifier = Modifier) {
                    StudentScreen(viewModel = studentViewModel)
                }
            }
        }
    }
}
