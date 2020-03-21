package com.postcrud.data.dto.schdule

data class LessonDto(
    val id: Long,
    val name: String,
    val teacherName: String,
    val teacherId: Long,
    val classRoom: String,
    val timeStart: Long,
    val timeEnd: Long
)