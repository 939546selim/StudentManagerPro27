
package com.ahona.studentmanagerpro27.viewmodel

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.ahona.studentmanagerpro27.data.StudentRepository
import com.ahona.studentmanagerpro27.model.Student
import kotlinx.coroutines.flow.SharingStarted
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.stateIn
import kotlinx.coroutines.launch
import com.ahona.studentmanagerpro27.data.StudentOperationResult
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.setValue

class StudentViewModel(
    private val repository: StudentRepository
) : ViewModel() {

    var operationResult by
    mutableStateOf<StudentOperationResult?>(null)
        private set

    val students: StateFlow<List<Student>> =
        repository.getAllStudents().stateIn(
            scope = viewModelScope,
            started = SharingStarted.WhileSubscribed(5_000),
            initialValue = emptyList()
        )

//    fun addStudent(student: Student) {
//        viewModelScope.launch {
//            repository.insertStudent(student)
//        }
//    }

    fun addStudent(student: Student) {
        viewModelScope.launch {
            operationResult = null
            operationResult = repository.insertStudent(student)
        }
    }

//    fun updateStudent(student: Student) {
//        viewModelScope.launch {
//            repository.updateStudent(student)
//        }
//    }
fun updateStudent(student: Student) {
    viewModelScope.launch {
        operationResult = null
        operationResult = repository.updateStudent(student)
    }
}

//    fun deleteStudent(student: Student) {
//        viewModelScope.launch {
//            repository.deleteStudent(student)
//        }
//    }

    fun deleteStudent(student: Student) {
        viewModelScope.launch {
            operationResult = null
            operationResult = repository.deleteStudent(student)
        }
    }

    fun clearOperationResult() {
        operationResult = null
    }



}


