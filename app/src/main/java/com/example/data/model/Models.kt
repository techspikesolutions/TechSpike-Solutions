package com.example.data.model

data class ServiceItem(
    val id: String,
    val title: String,
    val shortDescription: String,
    val fullDescription: String,
    val category: String,
    val problemsSolved: List<String>,
    val ourSolution: String,
    val features: List<String>,
    val benefits: List<String>,
    val processSteps: List<String>,
    val faqs: List<FaqItem>
)

data class FaqItem(
    val question: String,
    val answer: String
)

data class CourseItem(
    val id: String,
    val title: String,
    val category: String,
    val duration: String,
    val mode: String,
    val eligibility: String,
    val whoShouldJoin: String,
    val overview: String,
    val curriculum: List<CurriculumModule>,
    val careerOpportunities: List<String>,
    val practicalProjects: List<String>,
    val certificateInfo: String,
    val nextBatch: String,
    val rating: Float = 4.9f,
    val enrolledStudents: Int = 120,
    val isPopular: Boolean = false
)

data class CurriculumModule(
    val moduleNumber: Int,
    val title: String,
    val topics: List<String>
)

data class BlogPost(
    val id: String,
    val title: String,
    val summary: String,
    val content: String,
    val category: String,
    val author: String,
    val date: String,
    val readTime: String,
    val viewsCount: Int = 0
)

data class Testimonial(
    val id: String,
    val name: String,
    val role: String,
    val organizationOrCourse: String,
    val comment: String,
    val rating: Float = 5.0f
)

data class UserAccount(
    val name: String = "Guest User",
    val email: String = "guest@techspike.in",
    val phone: String = "+91 98888 88476",
    val role: String = "Client", // "Client", "Student", "Admin"
    val isLoggedIn: Boolean = false
)
