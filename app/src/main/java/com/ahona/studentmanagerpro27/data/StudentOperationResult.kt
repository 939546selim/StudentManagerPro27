package com.ahona.studentmanagerpro27.data

sealed interface  StudentOperationResult {
    data object Success : StudentOperationResult

    data class Error(
        val message: String
    ) : StudentOperationResult
}