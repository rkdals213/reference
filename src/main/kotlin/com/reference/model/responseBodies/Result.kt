package com.reference.model.responseBodies

import org.springframework.http.HttpStatus

import org.springframework.http.ResponseEntity
import java.lang.Exception

import java.util.HashMap

fun handleSuccess(data: Any): ResponseEntity<Map<String, Any?>> {
    val resultMap: MutableMap<String, Any> = HashMap()
    resultMap["status"] = true
    resultMap["data"] = data
    return ResponseEntity(resultMap, HttpStatus.OK)
}

fun handleSuccess(): ResponseEntity<Map<String, Any?>> {
    val resultMap: MutableMap<String, Any> = HashMap()
    resultMap["status"] = true
    return ResponseEntity(resultMap, HttpStatus.OK)
}

fun handleFail(message: String): ResponseEntity<Map<String, Any?>> {
    val resultMap: MutableMap<String, Any> = HashMap()
    resultMap["status"] = true
    resultMap["message"] = message
    return ResponseEntity(resultMap, HttpStatus.OK)
}

fun handleException(e: Exception): ResponseEntity<Map<String, Any?>> {
    val resultMap: MutableMap<String, Any?> = HashMap()
    resultMap["status"] = false
    resultMap["data"] = e.message
    return ResponseEntity(resultMap, HttpStatus.INTERNAL_SERVER_ERROR)
}