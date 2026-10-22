package com.ahona.studentmanagerpro27.user_interface

import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.material3.Button
import androidx.compose.material3.OutlinedTextField
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Modifier
import androidx.compose.ui.unit.dp
import com.ahona.studentmanagerpro27.model.Student
import com.ahona.studentmanagerpro27.viewmodel.StudentViewModel
import androidx.compose.foundation.layout.Row
import androidx.compose.material3.TextButton
import androidx.compose.material3.AlertDialog
import androidx.compose.material3.MaterialTheme
import com.ahona.studentmanagerpro27.data.StudentOperationResult
import androidx.compose.material3.TextButton
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.text.font.FontWeight
import androidx.lifecycle.viewmodel.compose.viewModel
import androidx.compose.runtime.getValue


@Composable
fun StudentScreen(viewModel: StudentViewModel) {

    var name by remember { mutableStateOf("") }
    var age by remember { mutableStateOf("") }
    var email by remember { mutableStateOf("") }
    var editingStudentId by remember { mutableStateOf<Int?>(null) }
    var studentToDelete by remember {
        mutableStateOf<Student?>(null)
    }
    var validationMessage by remember {
        mutableStateOf<String?>(null)
    }

    var isSortedByName by remember {
        mutableStateOf(false)
    }

    var minimumAgeText by remember {
        mutableStateOf("")
    }

    var exactAgeText by remember { mutableStateOf("") }






    val students by viewModel.students.collectAsState()

    val searchQuery by viewModel.searchQuery.collectAsState()

    val filteredStudents by viewModel.filteredStudents.collectAsState()

    val studentsSortedByName by
    viewModel.studentsSortedByName.collectAsState()

    val studentsByMinimumAge by
    viewModel.studentsByMinimumAge.collectAsState()

    val studentsByExactAge by
    viewModel.studentsByExactAge.collectAsState()




    val visibleStudents = when {
        exactAgeText.isNotBlank() ->
            studentsByExactAge.filter { student ->
                student.name.contains(
                    searchQuery,
                    ignoreCase = true
                )
            }
        minimumAgeText.isNotBlank() ->
            studentsByMinimumAge.filter { student ->
                student.name.contains(
                    searchQuery,
                    ignoreCase = true
                )
            }

        isSortedByName ->
            studentsSortedByName.filter { student ->
                student.name.contains(
                    searchQuery,
                    ignoreCase = true
                )
            }

        else -> filteredStudents
    }



    Column(
        modifier = Modifier
            .fillMaxSize()
            .padding(16.dp),
        verticalArrangement = Arrangement.spacedBy(12.dp)
    ) {
        Text("Student Manager Pro 27")

        OutlinedTextField(
            value = name,
            onValueChange = { name = it },
            label = { Text("Student Name") },
            modifier = Modifier.fillMaxWidth()
        )

        OutlinedTextField(
            value = age,
            onValueChange = { age = it },
            label = { Text("Age") },
            modifier = Modifier.fillMaxWidth()
        )

        OutlinedTextField(
            value = email,
            onValueChange = { email = it },
            label = { Text("Email") },
            modifier = Modifier.fillMaxWidth()
        )

        Button(
            onClick = {

                val parsedAge = age.toIntOrNull()

                validationMessage = when {
                    name.isBlank() ->
                        "Please enter student name"

                    parsedAge == null || parsedAge <= 0 ->
                        "Please enter a valid age"

                    !android.util.Patterns.EMAIL_ADDRESS
                        .matcher(email.trim())
                        .matches() ->
                        "Please enter a valid email"

                    else -> null
                }

                if (validationMessage == null) {
                    viewModel.clearOperationResult()
                    val student = Student(
                        id = editingStudentId ?: 0,
                        name = name.trim(),
                        age = parsedAge!!,
                        email = email.trim()
                    )

                    if (editingStudentId == null) {
                        viewModel.addStudent(student)
                    } else {
                        viewModel.updateStudent(student)
                    }

                    name = ""
                    age = ""
                    email = ""
                    editingStudentId = null
                    validationMessage = null
                }
            },


            ) {
            Text(
                if(editingStudentId==null){
                    "Save student"
                }else{
                    "Update student"
                }
            )
        }

        if(editingStudentId != null){
            TextButton(
                onClick = {
                    editingStudentId==null
                    name = ""
                    age = ""
                    email = ""
                    validationMessage = null

                    viewModel.clearOperationResult()
                }
            ) {
                Text("Cancell Edit")
            }

        }


        validationMessage?.let { message ->
            Text(
                text = message,
                color = MaterialTheme.colorScheme.error
            )
        }




        when (val result = viewModel.operationResult) {
            is StudentOperationResult.Success -> {
                Row(
                    modifier = Modifier.fillMaxWidth(),
                    horizontalArrangement = Arrangement.SpaceBetween
                ) {
                    Text("Operation successful!")

                    TextButton(
                        onClick = {
                            viewModel.clearOperationResult()
                        }
                    ) {
                        Text("Dismiss")
                    }
                }
            }

            is StudentOperationResult.Error -> {
                Row(
                    modifier = Modifier.fillMaxWidth(),
                    horizontalArrangement = Arrangement.SpaceBetween
                ) {
                    Text("Error: ${result.message}")

                    TextButton(
                        onClick = {
                            viewModel.clearOperationResult()
                        }
                    ) {
                        Text("Dismiss")
                    }
                }
            }

            null -> Unit
        }

        if (viewModel.isLoading) {
            Text(text = "Loading students...")
        }



        OutlinedTextField(
            value = searchQuery,
            onValueChange = { query ->
                viewModel.updateSearchQuery(query)
            },
            label = { Text("Search student by name") },
            singleLine = true,
            modifier = Modifier.fillMaxWidth()
        )

        OutlinedTextField(
            value = minimumAgeText,
            onValueChange = { text ->
                minimumAgeText = text
                viewModel.updateMinimumAge(
                    text.toIntOrNull() ?: 0
                )
            },
            label = { Text("Search by Minimum age") },
            singleLine = true,
            modifier = Modifier.fillMaxWidth()
        )

        OutlinedTextField(
            value = exactAgeText,
            onValueChange = { text ->
                exactAgeText = text.filter { it.isDigit() }

                viewModel.updateExactAge(
                    exactAgeText.toIntOrNull()
                )
            },
            label = {
                Text("Search by Exact Age")
            },
            placeholder = {
                Text("Enter exact age")
            },
            modifier = Modifier.fillMaxWidth(),
            singleLine = true
        )

//        Button(
//            onClick = {
//                // Sorting UI will be connected in the next task
//            }
//        ) {
//            Text("Sort by Name (A–Z)")
//        }

        Button(
             onClick = {
                isSortedByName = !isSortedByName
            }
        ) {
            Text(
                if (isSortedByName) {
                    "Show Original List"
                } else {
                    "Sort by Name (A–Z)"
                }
            )
        }


        Text("Student List")

//        if (students.isEmpty()) {
//            Text("No students yet. Add your first student!",   color = Color(0xFF2E7D32),
//                fontWeight = FontWeight.Bold
//            )
//
//        }

//        val visibleStudents = when {
//            minimumAgeText.isNotBlank() ->
//                studentsByMinimumAge.filter { student ->
//                    student.name.contains(
//                        searchQuery,
//                        ignoreCase = true
//                    )
//                }
//
//            isSortedByName ->
//                studentsSortedByName.filter { student ->
//                    student.name.contains(
//                        searchQuery,
//                        ignoreCase = true
//                    )
//                }
//
//            else -> filteredStudents
//        }



//        if (visibleStudents.isEmpty()) {
//            Text(
//                if (searchQuery.isBlank()) {
//                    "No students yet. Add your first student!"
//                } else {
//                    "No matching students found."
//                },
//                color = Color(0xFF2E7D32),
//                fontWeight = FontWeight.Bold
//            )
//        }

        if (visibleStudents.isEmpty()) {
            Text(
                text = if (
                    students.isEmpty() &&
                    searchQuery.isBlank() &&
                    minimumAgeText.isBlank()
                ) {
                    "No students yet. Add your first student!"
                } else {
                    "No matching students found."
                },
                color = Color(0xFF2E7D32),
                fontWeight = FontWeight.Bold
            )
        }



        LazyColumn(
            verticalArrangement = Arrangement.spacedBy(8.dp)
        ) {

            items(items = visibleStudents, key = { it.id }) { student ->

                Row(
                    modifier = Modifier
                        .fillMaxWidth()
                        .padding(8.dp),
                    horizontalArrangement = Arrangement.SpaceBetween
                ) {
                    Column(
                        modifier = Modifier
                            .weight(1f)
                            .clickable {
                                editingStudentId = student.id
                                name = student.name
                                age = student.age.toString()
                                email = student.email
                            }
                    ) {
                        Text("Name: ${student.name}")
                        Text("Age: ${student.age}")
                        Text("Email: ${student.email}")
                        Text("Tap to Edit")
                    }


                    TextButton(
                        onClick = {
                            studentToDelete = student
                        }
                    ) {
                        Text("Delete")
                    }
                }
            }
        }
    }
    if (studentToDelete != null) {
        AlertDialog(
            onDismissRequest = {
                studentToDelete = null
            },
            title = {
                Text("Delete Student?")
            },
            text = {
                Text(
                    "Are you sure you want to delete " +
                            "${studentToDelete?.name}?"
                )
            },
            confirmButton = {
                TextButton(
                    onClick = {
                        studentToDelete?.let { student ->
                            viewModel.deleteStudent(student)

                            if (editingStudentId == student.id) {
                                editingStudentId = null
                                name = ""
                                age = ""
                                email = ""
                            }
                        }

                        studentToDelete = null
                    }
                ) {
                    Text("Confirm")
                }
            },
            dismissButton = {
                TextButton(
                    onClick = {
                        studentToDelete = null
                    }
                ) {
                    Text("Cancel")
                }
            }
        )
    }
}