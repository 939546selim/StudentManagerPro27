
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
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.flow.flatMapLatest
import kotlinx.coroutines.flow.map
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.flow.flowOf

class StudentViewModel(
    private val repository: StudentRepository
) : ViewModel() {

    var operationResult by
    mutableStateOf<StudentOperationResult?>(null)
        private set

    var isLoading by mutableStateOf(true)
        private set

//    var minimumAge by mutableStateOf(0)
//        private set

    private val minimumAgeFlow = MutableStateFlow(0)
    val minimumAge = minimumAgeFlow.asStateFlow()


    private val exactAgeFlow = MutableStateFlow<Int?>(null)
    val exactAge: StateFlow<Int?> =
        exactAgeFlow.asStateFlow()

    val students: StateFlow<List<Student>> =
        repository.getAllStudents().stateIn(
            scope = viewModelScope,
            started = SharingStarted.WhileSubscribed(5_000),
            initialValue = emptyList()
        )

    val studentsSortedByName: StateFlow<List<Student>> =
        repository.getStudentsSortedByName().stateIn(
            scope = viewModelScope,
            started = SharingStarted.WhileSubscribed(5_000),
            initialValue = emptyList()
        )

    val studentsByMinimumAge: StateFlow<List<Student>> =
        minimumAgeFlow
            .flatMapLatest { age ->
                repository.getStudentsByMinimumAge(age)
            }
            .stateIn(
                scope = viewModelScope,
                started = SharingStarted.WhileSubscribed(5_000),
                initialValue = emptyList()
            )




    val studentsByExactAge: StateFlow<List<Student>> =
        exactAgeFlow
            .flatMapLatest { age ->
                if (age == null) {
                    flowOf(emptyList())
                } else {
                    repository.getStudentsByExactAge(age)
                }
            }
            .stateIn(
                scope = viewModelScope,
                started = SharingStarted.WhileSubscribed(5_000),
                initialValue = emptyList()
            )



    private val searchQueryFlow = MutableStateFlow("")

    val searchQuery = searchQueryFlow.asStateFlow()


    init {
        viewModelScope.launch {
            try {
                repository.getAllStudents().collect {
                    isLoading = false
                }
            } finally {
                isLoading = false
            }
        }
    }

    fun updateMinimumAge(age: Int) {
        minimumAgeFlow.value = age
    }


    fun updateExactAge(age: Int?) {
        exactAgeFlow.value = age
    }

//    fun updateSearchQuery(query: String) {
//        searchQuery = query
//    }

    fun updateSearchQuery(query: String) {
        searchQueryFlow.value = query
    }

//    val filteredStudents: List<Student>
//        get() = students.value.filter { student ->
//            student.name.contains(
//                searchQuery,
//                ignoreCase = true
//            )
//        }

    val filteredStudents: StateFlow<List<Student>> =
        kotlinx.coroutines.flow.combine(
            students,
            searchQueryFlow
        ) { studentList, query ->
            studentList.filter { student ->
                student.name.contains(
                    query,
                    ignoreCase = true
                )
            }
        }.stateIn(
            scope = viewModelScope,
            started = SharingStarted.WhileSubscribed(5_000),
            initialValue = emptyList()
        )



    fun addStudent(student: Student) {
        viewModelScope.launch {
            operationResult = null
            operationResult = repository.insertStudent(student)
        }
    }

fun updateStudent(student: Student) {
    viewModelScope.launch {
        operationResult = null
        operationResult = repository.updateStudent(student)
    }
}


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


