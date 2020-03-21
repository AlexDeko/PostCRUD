package com.postcrud.data.dto.schdule

data class ScheduleDayRequestDto(
    val date: String,
    val lessons: List<LessonRequestDto>
)