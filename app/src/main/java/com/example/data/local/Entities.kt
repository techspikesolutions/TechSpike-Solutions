package com.example.data.local

import androidx.room.Entity
import androidx.room.PrimaryKey

@Entity(tableName = "enquiries")
data class EnquiryEntity(
    @PrimaryKey(autoGenerate = true)
    val id: Long = 0,
    val type: String, // "SERVICE_QUOTE", "COURSE_ENQUIRY", "CAREER_COUNSELLING", "CONTACT_GENERAL"
    val name: String,
    val email: String,
    val phone: String,
    val serviceOrCourse: String,
    val companyName: String = "",
    val budgetRange: String = "",
    val preferredMode: String = "",
    val preferredBatch: String = "",
    val message: String = "",
    val status: String = "New", // "New", "Contacted", "Follow-up", "Converted", "Closed"
    val adminNotes: String = "",
    val timestamp: Long = System.currentTimeMillis()
)

@Entity(tableName = "notifications")
data class NotificationEntity(
    @PrimaryKey(autoGenerate = true)
    val id: Long = 0,
    val title: String,
    val message: String,
    val type: String, // "COURSE", "SERVICE", "ANNOUNCEMENT", "OFFER"
    val isRead: Boolean = false,
    val timestamp: Long = System.currentTimeMillis()
)

@Entity(tableName = "custom_courses")
data class CustomCourseEntity(
    @PrimaryKey
    val id: String,
    val title: String,
    val category: String,
    val duration: String,
    val mode: String,
    val overview: String,
    val curriculumRaw: String = "",
    val timestamp: Long = System.currentTimeMillis()
)

@Entity(tableName = "custom_blogs")
data class CustomBlogEntity(
    @PrimaryKey
    val id: String,
    val title: String,
    val category: String,
    val summary: String,
    val content: String,
    val author: String,
    val date: String,
    val isPublished: Boolean = true,
    val timestamp: Long = System.currentTimeMillis()
)
