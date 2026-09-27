package com.example.ui.viewmodel

import android.app.Application
import androidx.lifecycle.AndroidViewModel
import androidx.lifecycle.viewModelScope
import com.example.data.local.AppDatabase
import com.example.data.local.CustomBlogEntity
import com.example.data.local.CustomCourseEntity
import com.example.data.local.EnquiryEntity
import com.example.data.local.NotificationEntity
import com.example.data.model.BlogPost
import com.example.data.model.CourseItem
import com.example.data.model.ServiceItem
import com.example.data.model.Testimonial
import com.example.data.model.UserAccount
import com.example.data.repository.TechSpikeRepository
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.SharingStarted
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.flow.combine
import kotlinx.coroutines.flow.stateIn
import kotlinx.coroutines.launch

data class ChatMessage(
    val id: String = java.util.UUID.randomUUID().toString(),
    val sender: String, // "user" or "spike"
    val text: String,
    val timestamp: Long = System.currentTimeMillis(),
    val suggestedActions: List<String> = emptyList()
)

data class UiSnackbarState(
    val message: String = "",
    val isVisible: Boolean = false
)

class TechSpikeViewModel(application: Application) : AndroidViewModel(application) {

    private val repository: TechSpikeRepository

    init {
        val db = AppDatabase.getDatabase(application)
        repository = TechSpikeRepository(db)
        seedInitialNotificationsIfEmpty()
    }

    // Services
    val services: List<ServiceItem> = repository.getServices()

    // Testimonials
    val testimonials: List<Testimonial> = repository.getTestimonials()

    // Courses Flow
    val courses: StateFlow<List<CourseItem>> = repository.getAllCourses()
        .stateIn(viewModelScope, SharingStarted.WhileSubscribed(5000), emptyList())

    // Blog Posts Flow
    val blogPosts: StateFlow<List<BlogPost>> = repository.getAllBlogPosts()
        .stateIn(viewModelScope, SharingStarted.WhileSubscribed(5000), emptyList())

    // Enquiries Flow (Admin and User)
    val enquiries: StateFlow<List<EnquiryEntity>> = repository.allEnquiries
        .stateIn(viewModelScope, SharingStarted.WhileSubscribed(5000), emptyList())

    // Notifications Flow
    val notifications: StateFlow<List<NotificationEntity>> = repository.allNotifications
        .stateIn(viewModelScope, SharingStarted.WhileSubscribed(5000), emptyList())

    // Search Query State
    private val _searchQuery = MutableStateFlow("")
    val searchQuery: StateFlow<String> = _searchQuery.asStateFlow()

    fun updateSearchQuery(query: String) {
        _searchQuery.value = query
    }

    // Selected Course Filter Category
    private val _courseCategoryFilter = MutableStateFlow("All")
    val courseCategoryFilter: StateFlow<String> = _courseCategoryFilter.asStateFlow()

    fun setCourseCategoryFilter(cat: String) {
        _courseCategoryFilter.value = cat
    }

    // Selected Blog Filter Category
    private val _blogCategoryFilter = MutableStateFlow("All")
    val blogCategoryFilter: StateFlow<String> = _blogCategoryFilter.asStateFlow()

    fun setBlogCategoryFilter(cat: String) {
        _blogCategoryFilter.value = cat
    }

    // Active User State
    private val _currentUser = MutableStateFlow(
        UserAccount(
            name = "Professional Client",
            email = "client@techspike.in",
            phone = "+91 98888 88476",
            role = "Client",
            isLoggedIn = true
        )
    )
    val currentUser: StateFlow<UserAccount> = _currentUser.asStateFlow()

    fun switchUserRole(role: String) {
        val updatedName = when (role) {
            "Admin" -> "TechSpike Administrator"
            "Student" -> "Rahul Verma (Student)"
            else -> "Professional Client"
        }
        _currentUser.value = _currentUser.value.copy(
            name = updatedName,
            role = role
        )
    }

    // Snackbar alert state
    private val _snackbarState = MutableStateFlow(UiSnackbarState())
    val snackbarState: StateFlow<UiSnackbarState> = _snackbarState.asStateFlow()

    fun showToast(message: String) {
        _snackbarState.value = UiSnackbarState(message = message, isVisible = true)
    }

    fun dismissToast() {
        _snackbarState.value = UiSnackbarState(isVisible = false)
    }

    // Lead / Enquiry submission
    fun submitLead(
        type: String,
        name: String,
        email: String,
        phone: String,
        serviceOrCourse: String,
        companyName: String = "",
        budgetRange: String = "",
        preferredMode: String = "",
        preferredBatch: String = "",
        message: String = "",
        onSuccess: () -> Unit = {}
    ) {
        viewModelScope.launch {
            repository.submitEnquiry(
                EnquiryEntity(
                    type = type,
                    name = name.ifBlank { "Valued Inquirer" },
                    email = email.ifBlank { "contact@client.com" },
                    phone = phone.ifBlank { "+91 98888 88476" },
                    serviceOrCourse = serviceOrCourse,
                    companyName = companyName,
                    budgetRange = budgetRange,
                    preferredMode = preferredMode,
                    preferredBatch = preferredBatch,
                    message = message,
                    status = "New"
                )
            )
            // Trigger automatic notification for lead receipt
            repository.sendNotification(
                title = "Enquiry Received",
                message = "Thank you $name! TechSpike Solutions team will reach out regarding $serviceOrCourse shortly.",
                type = "OFFER"
            )
            showToast("Thank you! Our team will contact you shortly.")
            onSuccess()
        }
    }

    // Admin updates
    fun updateLeadStatus(id: Long, newStatus: String, notes: String) {
        viewModelScope.launch {
            repository.updateEnquiryStatus(id, newStatus, notes)
            showToast("Lead #$id status updated to $newStatus")
        }
    }

    fun deleteLead(id: Long) {
        viewModelScope.launch {
            repository.deleteEnquiry(id)
            showToast("Enquiry removed")
        }
    }

    fun addCustomCourse(title: String, category: String, duration: String, mode: String, overview: String) {
        viewModelScope.launch {
            val newId = "course-custom-${System.currentTimeMillis()}"
            repository.addCustomCourse(
                CustomCourseEntity(
                    id = newId,
                    title = title,
                    category = category,
                    duration = duration,
                    mode = mode,
                    overview = overview
                )
            )
            repository.sendNotification(
                title = "New Course Announced",
                message = "TechSpike Training Institute has launched: $title ($duration). Admissions open!",
                type = "COURSE"
            )
            showToast("Course added to catalogue!")
        }
    }

    fun deleteCustomCourse(id: String) {
        viewModelScope.launch {
            repository.deleteCustomCourse(id)
            showToast("Course removed from catalogue")
        }
    }

    fun addCustomBlog(title: String, category: String, summary: String, content: String, author: String) {
        viewModelScope.launch {
            val newId = "blog-custom-${System.currentTimeMillis()}"
            val dateStr = java.text.SimpleDateFormat("MMMM yyyy", java.util.Locale.US).format(java.util.Date())
            repository.addCustomBlog(
                CustomBlogEntity(
                    id = newId,
                    title = title,
                    category = category,
                    summary = summary,
                    content = content,
                    author = author.ifBlank { "TechSpike Editorial" },
                    date = dateStr
                )
            )
            repository.sendNotification(
                title = "New Article Published",
                message = title,
                type = "ANNOUNCEMENT"
            )
            showToast("Article published successfully!")
        }
    }

    fun deleteCustomBlog(id: String) {
        viewModelScope.launch {
            repository.deleteCustomBlog(id)
            showToast("Article deleted")
        }
    }

    fun sendAdminNotification(title: String, message: String, type: String) {
        viewModelScope.launch {
            repository.sendNotification(title, message, type)
            showToast("Broadcast notification pushed to users!")
        }
    }

    fun markNotificationRead(id: Long) {
        viewModelScope.launch {
            repository.markNotificationAsRead(id)
        }
    }

    fun markAllNotificationsRead() {
        viewModelScope.launch {
            repository.markAllNotificationsAsRead()
            showToast("All notifications marked as read")
        }
    }

    // Spike AI Chat Assistant
    private val _chatMessages = MutableStateFlow<List<ChatMessage>>(
        listOf(
            ChatMessage(
                sender = "spike",
                text = "Hello! I am Spike AI, your TechSpike Solutions assistant.\n\nHow can I help you today? Ask me about our IT services, professional training courses in Ludhiana, or get a quotation!",
                suggestedActions = listOf(
                    "Explore Web Development",
                    "Course Batches & Fees",
                    "Disaster Recovery & Backup",
                    "Talk to a Human"
                )
            )
        )
    )
    val chatMessages: StateFlow<List<ChatMessage>> = _chatMessages.asStateFlow()

    private val _isChatLoading = MutableStateFlow(false)
    val isChatLoading: StateFlow<Boolean> = _isChatLoading.asStateFlow()

    fun sendChatMessage(query: String) {
        if (query.isBlank()) return

        val userMsg = ChatMessage(sender = "user", text = query)
        _chatMessages.value = _chatMessages.value + userMsg
        _isChatLoading.value = true

        viewModelScope.launch {
            kotlinx.coroutines.delay(600) // smooth typing feel
            val answer = answerKnowledgeBase(query)
            _chatMessages.value = _chatMessages.value + answer
            _isChatLoading.value = false
        }
    }

    private fun answerKnowledgeBase(query: String): ChatMessage {
        val q = query.lowercase().trim()

        return when {
            q.contains("human") || q.contains("call") || q.contains("phone") || q.contains("representative") || q.contains("person") -> {
                ChatMessage(
                    sender = "spike",
                    text = "You can speak directly with our TechSpike advisory team:\n\n📞 Phone: +91 98888 88476\n✉️ Email: contact@techspikesolutions.in\n📍 Office: Plot No. 3211, Sector 32-A, Chandigarh Road, Ludhiana\n\nWould you like to start a WhatsApp chat with our advisor right away?",
                    suggestedActions = listOf("Open WhatsApp Now", "Book Free Consultation", "Course Inquiries")
                )
            }
            q.contains("web") || q.contains("website") || q.contains("ecommerce") || q.contains("react") || q.contains("full stack") -> {
                ChatMessage(
                    sender = "spike",
                    text = "TechSpike Solutions specializes in modern Web Development:\n\n• Custom business websites & corporate portals\n• High-conversion E-commerce solutions\n• Responsive SPAs & Web Apps using Next.js / React\n• Fast load times (< 1.5s) and SEO-ready architecture\n\nWe also offer 3-to-6 month practical Web Development training in our Ludhiana institute lab!",
                    suggestedActions = listOf("Request a Web Quote", "Web Dev Course Syllabus", "Talk to a Human")
                )
            }
            q.contains("course") || q.contains("training") || q.contains("student") || q.contains("learn") || q.contains("batch") || q.contains("fee") || q.contains("syllabus") -> {
                ChatMessage(
                    sender = "spike",
                    text = "TechSpike Training Institute offers industry-accredited courses with 100% practical lab work:\n\n1. Web Design & Web Development (3-6 Months)\n2. Digital Marketing & SEO Mastery (3 Months)\n3. Computer Networking & Hardware (3 Months)\n4. Cyber Security & Ethical Hacking (4-6 Months)\n5. C / C++ Programming Fundamentals (2 Months)\n6. SQL & Relational Database Design (1.5 Months)\n\nModes: Offline Classroom & Online. Batches start 1st & 15th of every month.",
                    suggestedActions = listOf("Book Free Counselling", "Register for a Batch", "Visit Campus in Ludhiana")
                )
            }
            q.contains("disaster") || q.contains("backup") || q.contains("ransomware") || q.contains("recovery") -> {
                ChatMessage(
                    sender = "spike",
                    text = "Our Disaster Recovery & Business Continuity services guarantee:\n\n• Real-time automated cloud & on-prem backups\n• Immutable off-site vaults protected against ransomware\n• Recovery Time Objective (RTO) in minutes, not days\n• ISO 9001:2015 compliant data recovery protocols\n\nWould you like an evaluation of your current data backup setup?",
                    suggestedActions = listOf("Request Free Evaluation", "Speak to DR Specialist", "View All Services")
                )
            }
            q.contains("cloud") || q.contains("desktop") || q.contains("vdi") || q.contains("remote") -> {
                ChatMessage(
                    sender = "spike",
                    text = "TechSpike Cloud Desktop enables secure work-from-anywhere for your team:\n\n• Virtual Desktop Infrastructure (VDI) with dedicated CPU/GPU\n• Zero data leakage: files stay in encrypted cloud data centers\n• Cuts office PC hardware CapEx by up to 45%\n• Instant provisioning for remote team members in minutes",
                    suggestedActions = listOf("Get Cloud Desktop Demo", "Request a Quote", "Talk to a Human")
                )
            }
            q.contains("seo") || q.contains("digital marketing") || q.contains("google ads") || q.contains("traffic") -> {
                ChatMessage(
                    sender = "spike",
                    text = "Our Digital Marketing team delivers measurable revenue growth:\n\n• Local SEO & Google Business Profile dominance\n• Google Ads PPC management with high ROAS\n• Meta / Instagram / LinkedIn social campaigns\n• Conversion Rate Optimization & monthly ROI dashboards",
                    suggestedActions = listOf("Request Free SEO Audit", "Digital Marketing Course", "Talk to a Human")
                )
            }
            q.contains("network") || q.contains("wifi") || q.contains("cabling") || q.contains("router") || q.contains("switch") -> {
                ChatMessage(
                    sender = "spike",
                    text = "TechSpike Network Solutions covers enterprise connectivity:\n\n• Structured Cat6/Fiber cabling & server rack neatening\n• High-density Wi-Fi 6 mesh deployment without dead zones\n• Cisco, MikroTik & Ubiquiti routing, VLANs & firewalls\n• 24/7 network monitoring & dual-ISP failover",
                    suggestedActions = listOf("Network Assessment", "Networking Course", "Contact Engineering")
                )
            }
            q.contains("address") || q.contains("location") || q.contains("where") || q.contains("office") || q.contains("ludhiana") -> {
                ChatMessage(
                    sender = "spike",
                    text = "TechSpike Solutions has two facilities in Ludhiana, Punjab:\n\n🏢 Corporate Office:\nPlot No. 3211, Sector 32-A, Chandigarh Road, Ludhiana, 141010\n\n🎓 Training Institute:\nNear Homeo cure, opp. Best Price, Mundian Khurd, Ludhiana, Punjab 141003\n\nWe also have international entity registration in Dubai, UAE!",
                    suggestedActions = listOf("Get Map Directions", "Call Office", "Talk to a Human")
                )
            }
            else -> {
                ChatMessage(
                    sender = "spike",
                    text = "Thank you for reaching out to TechSpike Solutions! As an ISO certified IT provider and training institute, we help businesses expand through cutting-edge technology and equip students with industry-ready skills.\n\nWould you like to discuss a project quotation or explore our courses?",
                    suggestedActions = listOf("Request a Quote", "Explore Courses", "Talk to a Human")
                )
            }
        }
    }

    private fun seedInitialNotificationsIfEmpty() {
        viewModelScope.launch {
            val existing = repository.allNotifications
            // Seed a welcome notification
            repository.sendNotification(
                title = "Welcome to TechSpike Solutions!",
                message = "Empowering businesses through IT solutions and practical training. Explore our services or register for new batches.",
                type = "ANNOUNCEMENT"
            )
            repository.sendNotification(
                title = "New Batches Starting Soon",
                message = "Admissions open for Web Development and Cyber Security programs at our Ludhiana campus and online.",
                type = "COURSE"
            )
        }
    }
}
