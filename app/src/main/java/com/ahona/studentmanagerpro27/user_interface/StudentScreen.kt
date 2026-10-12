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

@Composable
fun StudentScreen(viewModel: StudentViewModel) {

    var name by remember { mutableStateOf("") }
    var age by remember { mutableStateOf("") }
    var email by remember { mutableStateOf("") }
    var editingStudentId by remember { mutableStateOf<Int?>(null) }
    var studentToDelete by remember {
        mutableStateOf<Student?>(null)
    }

    val students by viewModel.students.collectAsState()

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

                if (
                    name.isNotBlank() &&
                    parsedAge != null &&
                    email.isNotBlank()
                ) {
                    val student = Student(
                        id = editingStudentId ?: 0,
                        name = name.trim(),
                        age = parsedAge,
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
                }
            },


            ) {
            Text("Save Student")
        }

        Text("Student List")

        LazyColumn(
            verticalArrangement = Arrangement.spacedBy(8.dp)
        ) {
//            items(students, key = { it.id }) { student ->
//                Column(modifier = Modifier.fillMaxWidth()) {
//                    Text("Name: ${student.name}")
//                    Text("Age: ${student.age}")
//                    Text("Email: ${student.email}")
//                }
//            }
            items(students, key = { it.id }) { student ->
//                Column(
//                    modifier = Modifier
//                        .fillMaxWidth()
//                        .clickable {
//                            editingStudentId = student.id
//                            name = student.name
//                            age = student.age.toString()
//                            email = student.email
//                        }
//                        .padding(8.dp)
//                ) {
//                    Text("Name: ${student.name}")
//                    Text("Age: ${student.age}")
//                    Text("Email: ${student.email}")
//
//                    Text("Tap to Edit")
//                }
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

//                    TextButton(
//                        onClick = {
//                            viewModel.deleteStudent(student)
//
//                            if (editingStudentId == student.id) {
//                                editingStudentId = null
//                                name = ""
//                                age = ""
//                                email = ""
//                            }
//                        }
//                    ) {
//                        Text("Delete")
//                    }
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