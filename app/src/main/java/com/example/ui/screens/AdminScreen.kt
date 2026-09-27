package com.example.ui.screens

import androidx.compose.foundation.background
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.LazyRow
import androidx.compose.foundation.lazy.items
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.*
import androidx.compose.material.icons.outlined.*
import androidx.compose.material3.*
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.example.data.local.EnquiryEntity
import com.example.data.model.BlogPost
import com.example.data.model.CourseItem
import com.example.ui.components.CircuitBackground
import com.example.ui.components.ContactHelper
import com.example.ui.components.SectionHeading
import com.example.ui.theme.*
import java.text.SimpleDateFormat
import java.util.Date
import java.util.Locale

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun AdminScreen(
    enquiries: List<EnquiryEntity>,
    courses: List<CourseItem>,
    blogPosts: List<BlogPost>,
    onUpdateEnquiryStatus: (id: Long, status: String, notes: String) -> Unit,
    onDeleteEnquiry: (id: Long) -> Unit,
    onAddCourse: (title: String, category: String, duration: String, mode: String, overview: String) -> Unit,
    onDeleteCourse: (id: String) -> Unit,
    onAddBlog: (title: String, category: String, summary: String, content: String, author: String) -> Unit,
    onDeleteBlog: (id: String) -> Unit,
    onSendNotification: (title: String, message: String, type: String) -> Unit,
    onBackClick: () -> Unit
) {
    var selectedTab by remember { mutableIntStateOf(0) }
    val tabs = listOf("Leads CRM", "Courses", "Publish Blog", "Broadcast", "Analytics")

    Box(modifier = Modifier.fillMaxSize()) {
        CircuitBackground()

        Column(modifier = Modifier.fillMaxSize()) {
            // Admin Top Header
            Surface(
                color = TechDarkBlue,
                tonalElevation = 4.dp,
                modifier = Modifier.fillMaxWidth()
            ) {
                Column(
                    modifier = Modifier
                        .fillMaxWidth()
                        .statusBarsPadding()
                        .padding(horizontal = 16.dp, vertical = 12.dp)
                ) {
                    Row(
                        modifier = Modifier.fillMaxWidth(),
                        verticalAlignment = Alignment.CenterVertically,
                        horizontalArrangement = Arrangement.SpaceBetween
                    ) {
                        Row(verticalAlignment = Alignment.CenterVertically) {
                            IconButton(onClick = onBackClick) {
                                Icon(Icons.Default.ArrowBack, contentDescription = "Back", tint = Color.White)
                            }
                            Spacer(modifier = Modifier.width(8.dp))
                            Column {
                                Text(
                                    text = "TechSpike Admin Portal",
                                    style = MaterialTheme.typography.titleMedium,
                                    fontWeight = FontWeight.Bold,
                                    color = Color.White
                                )
                                Text(
                                    text = "Command Center & CRM",
                                    style = MaterialTheme.typography.bodySmall,
                                    color = TechCyan
                                )
                            }
                        }

                        Box(
                            modifier = Modifier
                                .clip(RoundedCornerShape(6.dp))
                                .background(TechCyan.copy(alpha = 0.2f))
                                .padding(horizontal = 8.dp, vertical = 4.dp)
                        ) {
                            Text("ADMIN", fontSize = 10.sp, fontWeight = FontWeight.Bold, color = TechCyan)
                        }
                    }

                    Spacer(modifier = Modifier.height(8.dp))

                    ScrollableTabRow(
                        selectedTabIndex = selectedTab,
                        containerColor = TechDarkBlue,
                        contentColor = TechCyan,
                        edgePadding = 0.dp
                    ) {
                        tabs.forEachIndexed { index, title ->
                            Tab(
                                selected = selectedTab == index,
                                onClick = { selectedTab = index },
                                text = {
                                    Text(
                                        text = title,
                                        fontWeight = if (selectedTab == index) FontWeight.Bold else FontWeight.Normal,
                                        fontSize = 12.sp
                                    )
                                }
                            )
                        }
                    }
                }
            }

            // Tab Content
            Box(
                modifier = Modifier
                    .weight(1f)
                    .fillMaxWidth()
            ) {
                when (selectedTab) {
                    0 -> AdminLeadsTab(
                        enquiries = enquiries,
                        onUpdateStatus = onUpdateEnquiryStatus,
                        onDelete = onDeleteEnquiry
                    )
                    1 -> AdminCoursesTab(
                        courses = courses,
                        onAddCourse = onAddCourse,
                        onDeleteCourse = onDeleteCourse
                    )
                    2 -> AdminPublishBlogTab(
                        blogPosts = blogPosts,
                        onAddBlog = onAddBlog,
                        onDeleteBlog = onDeleteBlog
                    )
                    3 -> AdminBroadcastTab(onSendNotification = onSendNotification)
                    4 -> AdminAnalyticsTab(
                        enquiries = enquiries,
                        courses = courses,
                        blogPosts = blogPosts
                    )
                }
            }
        }
    }
}

@Composable
fun AdminLeadsTab(
    enquiries: List<EnquiryEntity>,
    onUpdateStatus: (id: Long, status: String, notes: String) -> Unit,
    onDelete: (id: Long) -> Unit
) {
    var filterStatus by remember { mutableStateOf("All") }
    val statuses = listOf("All", "New", "Contacted", "Follow-up", "Converted", "Closed")

    val filtered = enquiries.filter {
        filterStatus == "All" || it.status.equals(filterStatus, ignoreCase = true)
    }

    LazyColumn(
        modifier = Modifier.fillMaxSize(),
        contentPadding = PaddingValues(16.dp),
        verticalArrangement = Arrangement.spacedBy(10.dp)
    ) {
        item {
            LazyRow(
                horizontalArrangement = Arrangement.spacedBy(6.dp),
                modifier = Modifier.padding(bottom = 6.dp)
            ) {
                items(statuses) { st ->
                    FilterChip(
                        selected = filterStatus == st,
                        onClick = { filterStatus = st },
                        label = { Text(st, fontSize = 11.sp) },
                        colors = FilterChipDefaults.filterChipColors(
                            selectedContainerColor = TechCyan,
                            selectedLabelColor = TechMidnight
                        )
                    )
                }
            }
        }

        item {
            Text(
                text = "Total Leads Found: ${filtered.size}",
                style = MaterialTheme.typography.labelMedium,
                color = MaterialTheme.colorScheme.onSurfaceVariant
            )
        }

        if (filtered.isEmpty()) {
            item {
                Box(
                    modifier = Modifier
                        .fillMaxWidth()
                        .height(200.dp),
                    contentAlignment = Alignment.Center
                ) {
                    Text(
                        text = "No leads in this category.",
                        style = MaterialTheme.typography.bodyMedium,
                        color = MaterialTheme.colorScheme.onSurfaceVariant
                    )
                }
            }
        } else {
            items(filtered) { item ->
                AdminLeadCard(
                    lead = item,
                    onUpdateStatus = { newStatus, notes -> onUpdateStatus(item.id, newStatus, notes) },
                    onDelete = { onDelete(item.id) }
                )
            }
        }
    }
}

@Composable
fun AdminLeadCard(
    lead: EnquiryEntity,
    onUpdateStatus: (String, String) -> Unit,
    onDelete: () -> Unit
) {
    val context = LocalContext.current
    var isExpanded by remember { mutableStateOf(false) }
    var adminNotes by remember { mutableStateOf(lead.adminNotes) }

    val dateStr = SimpleDateFormat("dd MMM, hh:mm a", Locale.getDefault()).format(Date(lead.timestamp))
    val statusColor = when (lead.status) {
        "Converted" -> TechGreen
        "Contacted", "Follow-up" -> TechGold
        "Closed" -> MaterialTheme.colorScheme.outline
        else -> TechCyan
    }

    Surface(
        color = MaterialTheme.colorScheme.surface,
        shape = RoundedCornerShape(14.dp),
        tonalElevation = 2.dp,
        modifier = Modifier.fillMaxWidth()
    ) {
        Column(modifier = Modifier.padding(14.dp)) {
            Row(
                modifier = Modifier.fillMaxWidth(),
                horizontalArrangement = Arrangement.SpaceBetween,
                verticalAlignment = Alignment.CenterVertically
            ) {
                Column(modifier = Modifier.weight(1f)) {
                    Text(
                        text = lead.name,
                        style = MaterialTheme.typography.titleMedium,
                        fontWeight = FontWeight.Bold,
                        color = MaterialTheme.colorScheme.onSurface
                    )
                    Text(
                        text = "For: ${lead.serviceOrCourse}",
                        style = MaterialTheme.typography.bodySmall,
                        color = TechCyan,
                        fontWeight = FontWeight.SemiBold
                    )
                }

                Box(
                    modifier = Modifier
                        .clip(RoundedCornerShape(6.dp))
                        .background(statusColor.copy(alpha = 0.2f))
                        .padding(horizontal = 8.dp, vertical = 3.dp)
                ) {
                    Text(lead.status, fontSize = 10.sp, fontWeight = FontWeight.Bold, color = statusColor)
                }
            }

            Spacer(modifier = Modifier.height(8.dp))

            Row(
                modifier = Modifier.fillMaxWidth(),
                horizontalArrangement = Arrangement.spacedBy(16.dp)
            ) {
                Text("📞 ${lead.phone}", fontSize = 12.sp, color = MaterialTheme.colorScheme.onSurface)
                if (lead.email.isNotBlank()) {
                    Text("✉️ ${lead.email}", fontSize = 12.sp, color = MaterialTheme.colorScheme.onSurfaceVariant)
                }
            }

            if (lead.companyName.isNotBlank()) {
                Spacer(modifier = Modifier.height(4.dp))
                Text("🏢 Company: ${lead.companyName}", fontSize = 11.sp, color = MaterialTheme.colorScheme.onSurfaceVariant)
            }

            if (lead.message.isNotBlank()) {
                Spacer(modifier = Modifier.height(6.dp))
                Text(
                    text = "Message: ${lead.message}",
                    fontSize = 12.sp,
                    color = MaterialTheme.colorScheme.onSurfaceVariant
                )
            }

            Spacer(modifier = Modifier.height(6.dp))
            Text(text = "Received: $dateStr", fontSize = 10.sp, color = DarkTextSecondary)

            Spacer(modifier = Modifier.height(10.dp))

            // Action row: Call, WhatsApp, Expand Status Options
            Row(
                modifier = Modifier.fillMaxWidth(),
                horizontalArrangement = Arrangement.spacedBy(8.dp)
            ) {
                OutlinedButton(
                    onClick = {
                        val intent = android.content.Intent(android.content.Intent.ACTION_DIAL).apply {
                            data = android.net.Uri.parse("tel:${lead.phone}")
                        }
                        context.startActivity(intent)
                    },
                    contentPadding = PaddingValues(horizontal = 8.dp, vertical = 0.dp),
                    shape = RoundedCornerShape(8.dp),
                    modifier = Modifier.weight(1f)
                ) {
                    Icon(Icons.Default.Phone, null, modifier = Modifier.size(14.dp))
                    Spacer(modifier = Modifier.width(4.dp))
                    Text("Call", fontSize = 11.sp)
                }

                FilledTonalButton(
                    onClick = {
                        ContactHelper.openWhatsApp(
                            context,
                            "Hello ${lead.name}, this is TechSpike Solutions following up regarding your inquiry for ${lead.serviceOrCourse}."
                        )
                    },
                    contentPadding = PaddingValues(horizontal = 8.dp, vertical = 0.dp),
                    shape = RoundedCornerShape(8.dp),
                    colors = ButtonDefaults.filledTonalButtonColors(
                        containerColor = TechGreen.copy(alpha = 0.2f),
                        contentColor = TechGreen
                    ),
                    modifier = Modifier.weight(1f)
                ) {
                    Icon(Icons.Default.Chat, null, modifier = Modifier.size(14.dp))
                    Spacer(modifier = Modifier.width(4.dp))
                    Text("WhatsApp", fontSize = 11.sp)
                }

                Button(
                    onClick = { isExpanded = !isExpanded },
                    contentPadding = PaddingValues(horizontal = 8.dp, vertical = 0.dp),
                    shape = RoundedCornerShape(8.dp),
                    colors = ButtonDefaults.buttonColors(
                        containerColor = TechDarkBlue,
                        contentColor = TechCyan
                    ),
                    modifier = Modifier.weight(1.2f)
                ) {
                    Text(if (isExpanded) "Hide" else "Manage Status", fontSize = 11.sp)
                }
            }

            if (isExpanded) {
                Spacer(modifier = Modifier.height(12.dp))
                HorizontalDivider()
                Spacer(modifier = Modifier.height(10.dp))

                Text("Update Lead Status:", style = MaterialTheme.typography.labelSmall, fontWeight = FontWeight.Bold)
                Spacer(modifier = Modifier.height(6.dp))

                Row(
                    modifier = Modifier.fillMaxWidth(),
                    horizontalArrangement = Arrangement.spacedBy(6.dp)
                ) {
                    listOf("New", "Contacted", "Follow-up", "Converted", "Closed").take(4).forEach { st ->
                        FilterChip(
                            selected = lead.status == st,
                            onClick = { onUpdateStatus(st, adminNotes) },
                            label = { Text(st, fontSize = 10.sp) }
                        )
                    }
                }

                Spacer(modifier = Modifier.height(8.dp))

                OutlinedTextField(
                    value = adminNotes,
                    onValueChange = { adminNotes = it },
                    label = { Text("Admin Notes / Follow-up Details") },
                    modifier = Modifier.fillMaxWidth(),
                    maxLines = 2
                )

                Spacer(modifier = Modifier.height(8.dp))

                Row(
                    modifier = Modifier.fillMaxWidth(),
                    horizontalArrangement = Arrangement.SpaceBetween
                ) {
                    TextButton(
                        onClick = onDelete,
                        colors = ButtonDefaults.textButtonColors(contentColor = MaterialTheme.colorScheme.error)
                    ) {
                        Text("Delete Lead", fontSize = 11.sp)
                    }

                    Button(
                        onClick = {
                            onUpdateStatus(lead.status, adminNotes)
                            isExpanded = false
                        },
                        shape = RoundedCornerShape(8.dp),
                        colors = ButtonDefaults.buttonColors(containerColor = TechCyan, contentColor = TechMidnight)
                    ) {
                        Text("Save Notes", fontSize = 11.sp, fontWeight = FontWeight.Bold)
                    }
                }
            }
        }
    }
}

@Composable
fun AdminCoursesTab(
    courses: List<CourseItem>,
    onAddCourse: (String, String, String, String, String) -> Unit,
    onDeleteCourse: (String) -> Unit
) {
    var title by remember { mutableStateOf("") }
    var category by remember { mutableStateOf("Development") }
    var duration by remember { mutableStateOf("3 Months") }
    var mode by remember { mutableStateOf("Classroom Lab & Online") }
    var overview by remember { mutableStateOf("") }
    var showAddForm by remember { mutableStateOf(false) }

    LazyColumn(
        modifier = Modifier.fillMaxSize(),
        contentPadding = PaddingValues(16.dp),
        verticalArrangement = Arrangement.spacedBy(12.dp)
    ) {
        item {
            Button(
                onClick = { showAddForm = !showAddForm },
                shape = RoundedCornerShape(10.dp),
                colors = ButtonDefaults.buttonColors(containerColor = TechCyan, contentColor = TechMidnight),
                modifier = Modifier.fillMaxWidth()
            ) {
                Icon(if (showAddForm) Icons.Default.Close else Icons.Default.Add, null)
                Spacer(modifier = Modifier.width(6.dp))
                Text(if (showAddForm) "Cancel" else "Add New Training Course", fontWeight = FontWeight.Bold)
            }
        }

        if (showAddForm) {
            item {
                Surface(
                    color = MaterialTheme.colorScheme.surface,
                    shape = RoundedCornerShape(14.dp),
                    tonalElevation = 3.dp,
                    modifier = Modifier.fillMaxWidth()
                ) {
                    Column(
                        modifier = Modifier.padding(16.dp),
                        verticalArrangement = Arrangement.spacedBy(10.dp)
                    ) {
                        Text("Course Details", fontWeight = FontWeight.Bold, color = TechCyan)

                        OutlinedTextField(
                            value = title,
                            onValueChange = { title = it },
                            label = { Text("Course Title (e.g., Full Stack Python)") },
                            modifier = Modifier.fillMaxWidth()
                        )

                        OutlinedTextField(
                            value = category,
                            onValueChange = { category = it },
                            label = { Text("Category (Development, Cloud, AI, Security)") },
                            modifier = Modifier.fillMaxWidth()
                        )

                        Row(modifier = Modifier.fillMaxWidth(), horizontalArrangement = Arrangement.spacedBy(8.dp)) {
                            OutlinedTextField(
                                value = duration,
                                onValueChange = { duration = it },
                                label = { Text("Duration") },
                                modifier = Modifier.weight(1f)
                            )
                            OutlinedTextField(
                                value = mode,
                                onValueChange = { mode = it },
                                label = { Text("Mode") },
                                modifier = Modifier.weight(1f)
                            )
                        }

                        OutlinedTextField(
                            value = overview,
                            onValueChange = { overview = it },
                            label = { Text("Overview & Highlights") },
                            modifier = Modifier.fillMaxWidth(),
                            maxLines = 3
                        )

                        Button(
                            onClick = {
                                if (title.isNotBlank()) {
                                    onAddCourse(title, category, duration, mode, overview)
                                    title = ""
                                    overview = ""
                                    showAddForm = false
                                }
                            },
                            colors = ButtonDefaults.buttonColors(containerColor = TechCyan, contentColor = TechMidnight),
                            shape = RoundedCornerShape(8.dp),
                            modifier = Modifier.fillMaxWidth()
                        ) {
                            Text("Publish Course to Catalogue", fontWeight = FontWeight.Bold)
                        }
                    }
                }
            }
        }

        item {
            Text(
                text = "Existing Courses (${courses.size})",
                style = MaterialTheme.typography.titleSmall,
                fontWeight = FontWeight.Bold
            )
        }

        items(courses) { course ->
            Surface(
                color = MaterialTheme.colorScheme.surface,
                shape = RoundedCornerShape(12.dp),
                tonalElevation = 1.dp,
                modifier = Modifier.fillMaxWidth()
            ) {
                Row(
                    modifier = Modifier.padding(14.dp),
                    horizontalArrangement = Arrangement.SpaceBetween,
                    verticalAlignment = Alignment.CenterVertically
                ) {
                    Column(modifier = Modifier.weight(1f)) {
                        Text(course.title, fontWeight = FontWeight.Bold, style = MaterialTheme.typography.bodyMedium)
                        Text("${course.category} • ${course.duration}", fontSize = 12.sp, color = MaterialTheme.colorScheme.onSurfaceVariant)
                    }

                    if (course.id.startsWith("course-custom")) {
                        IconButton(onClick = { onDeleteCourse(course.id) }) {
                            Icon(Icons.Default.Delete, contentDescription = "Delete", tint = MaterialTheme.colorScheme.error)
                        }
                    }
                }
            }
        }
    }
}

@Composable
fun AdminPublishBlogTab(
    blogPosts: List<BlogPost>,
    onAddBlog: (String, String, String, String, String) -> Unit,
    onDeleteBlog: (String) -> Unit
) {
    var title by remember { mutableStateOf("") }
    var category by remember { mutableStateOf("Cloud Computing") }
    var author by remember { mutableStateOf("TechSpike Editorial") }
    var summary by remember { mutableStateOf("") }
    var content by remember { mutableStateOf("") }

    LazyColumn(
        modifier = Modifier.fillMaxSize(),
        contentPadding = PaddingValues(16.dp),
        verticalArrangement = Arrangement.spacedBy(12.dp)
    ) {
        item {
            Surface(
                color = MaterialTheme.colorScheme.surface,
                shape = RoundedCornerShape(14.dp),
                tonalElevation = 3.dp,
                modifier = Modifier.fillMaxWidth()
            ) {
                Column(
                    modifier = Modifier.padding(16.dp),
                    verticalArrangement = Arrangement.spacedBy(10.dp)
                ) {
                    Text("Publish New Article", fontWeight = FontWeight.Bold, color = TechCyan)

                    OutlinedTextField(
                        value = title,
                        onValueChange = { title = it },
                        label = { Text("Article Title") },
                        modifier = Modifier.fillMaxWidth()
                    )

                    Row(modifier = Modifier.fillMaxWidth(), horizontalArrangement = Arrangement.spacedBy(8.dp)) {
                        OutlinedTextField(
                            value = category,
                            onValueChange = { category = it },
                            label = { Text("Category") },
                            modifier = Modifier.weight(1f)
                        )
                        OutlinedTextField(
                            value = author,
                            onValueChange = { author = it },
                            label = { Text("Author") },
                            modifier = Modifier.weight(1f)
                        )
                    }

                    OutlinedTextField(
                        value = summary,
                        onValueChange = { summary = it },
                        label = { Text("Short Summary") },
                        modifier = Modifier.fillMaxWidth(),
                        maxLines = 2
                    )

                    OutlinedTextField(
                        value = content,
                        onValueChange = { content = it },
                        label = { Text("Full Article Content") },
                        modifier = Modifier
                            .fillMaxWidth()
                            .height(130.dp),
                        maxLines = 6
                    )

                    Button(
                        onClick = {
                            if (title.isNotBlank() && content.isNotBlank()) {
                                onAddBlog(title, category, summary, content, author)
                                title = ""
                                summary = ""
                                content = ""
                            }
                        },
                        colors = ButtonDefaults.buttonColors(containerColor = TechCyan, contentColor = TechMidnight),
                        shape = RoundedCornerShape(8.dp),
                        modifier = Modifier.fillMaxWidth()
                    ) {
                        Icon(Icons.Default.Publish, null, modifier = Modifier.size(16.dp))
                        Spacer(modifier = Modifier.width(6.dp))
                        Text("Publish to App", fontWeight = FontWeight.Bold)
                    }
                }
            }
        }

        item {
            Text("Published Articles (${blogPosts.size})", fontWeight = FontWeight.Bold)
        }

        items(blogPosts) { post ->
            Surface(
                color = MaterialTheme.colorScheme.surface,
                shape = RoundedCornerShape(12.dp),
                tonalElevation = 1.dp,
                modifier = Modifier.fillMaxWidth()
            ) {
                Row(
                    modifier = Modifier.padding(14.dp),
                    horizontalArrangement = Arrangement.SpaceBetween,
                    verticalAlignment = Alignment.CenterVertically
                ) {
                    Column(modifier = Modifier.weight(1f)) {
                        Text(post.title, fontWeight = FontWeight.Bold, style = MaterialTheme.typography.bodyMedium)
                        Text("${post.category} • ${post.author}", fontSize = 12.sp, color = MaterialTheme.colorScheme.onSurfaceVariant)
                    }

                    if (post.id.startsWith("blog-custom")) {
                        IconButton(onClick = { onDeleteBlog(post.id) }) {
                            Icon(Icons.Default.Delete, null, tint = MaterialTheme.colorScheme.error)
                        }
                    }
                }
            }
        }
    }
}

@Composable
fun AdminBroadcastTab(onSendNotification: (String, String, String) -> Unit) {
    var title by remember { mutableStateOf("") }
    var message by remember { mutableStateOf("") }
    var selectedType by remember { mutableStateOf("ANNOUNCEMENT") }

    val types = listOf("ANNOUNCEMENT", "COURSE", "SERVICE", "OFFER")

    Column(
        modifier = Modifier
            .fillMaxSize()
            .padding(16.dp),
        verticalArrangement = Arrangement.spacedBy(14.dp)
    ) {
        Surface(
            color = MaterialTheme.colorScheme.surface,
            shape = RoundedCornerShape(16.dp),
            tonalElevation = 3.dp,
            modifier = Modifier.fillMaxWidth()
        ) {
            Column(
                modifier = Modifier.padding(18.dp),
                verticalArrangement = Arrangement.spacedBy(12.dp)
            ) {
                Text(
                    text = "Push Broadcast Notification",
                    style = MaterialTheme.typography.titleMedium,
                    fontWeight = FontWeight.Bold,
                    color = TechCyan
                )

                Text(
                    text = "Broadcast alert notifications will instantly appear in all users' notification centers.",
                    style = MaterialTheme.typography.bodySmall,
                    color = MaterialTheme.colorScheme.onSurfaceVariant
                )

                OutlinedTextField(
                    value = title,
                    onValueChange = { title = it },
                    label = { Text("Notification Title (e.g., Weekend Batch Admissions Open)") },
                    modifier = Modifier.fillMaxWidth()
                )

                OutlinedTextField(
                    value = message,
                    onValueChange = { message = it },
                    label = { Text("Message Body") },
                    modifier = Modifier
                        .fillMaxWidth()
                        .height(100.dp),
                    maxLines = 3
                )

                Text("Alert Type:", style = MaterialTheme.typography.labelSmall)

                Row(
                    modifier = Modifier.fillMaxWidth(),
                    horizontalArrangement = Arrangement.spacedBy(6.dp)
                ) {
                    types.forEach { t ->
                        FilterChip(
                            selected = selectedType == t,
                            onClick = { selectedType = t },
                            label = { Text(t, fontSize = 10.sp) }
                        )
                    }
                }

                Button(
                    onClick = {
                        if (title.isNotBlank() && message.isNotBlank()) {
                            onSendNotification(title, message, selectedType)
                            title = ""
                            message = ""
                        }
                    },
                    colors = ButtonDefaults.buttonColors(containerColor = TechCyan, contentColor = TechMidnight),
                    shape = RoundedCornerShape(10.dp),
                    modifier = Modifier.fillMaxWidth()
                ) {
                    Icon(Icons.Default.Campaign, null, modifier = Modifier.size(18.dp))
                    Spacer(modifier = Modifier.width(8.dp))
                    Text("Broadcast Alert Now", fontWeight = FontWeight.Bold)
                }
            }
        }
    }
}

@Composable
fun AdminAnalyticsTab(
    enquiries: List<EnquiryEntity>,
    courses: List<CourseItem>,
    blogPosts: List<BlogPost>
) {
    val totalLeads = enquiries.size
    val convertedLeads = enquiries.count { it.status == "Converted" }
    val newLeads = enquiries.count { it.status == "New" }
    val conversionRate = if (totalLeads > 0) (convertedLeads * 100 / totalLeads) else 0

    LazyColumn(
        modifier = Modifier.fillMaxSize(),
        contentPadding = PaddingValues(16.dp),
        verticalArrangement = Arrangement.spacedBy(12.dp)
    ) {
        item {
            Text("Operations & CRM Analytics", fontWeight = FontWeight.Bold, style = MaterialTheme.typography.titleMedium)
        }

        item {
            Row(
                modifier = Modifier.fillMaxWidth(),
                horizontalArrangement = Arrangement.spacedBy(10.dp)
            ) {
                MetricCard("Total Inquiries", "$totalLeads", TechCyan, Modifier.weight(1f))
                MetricCard("New Pending", "$newLeads", TechGold, Modifier.weight(1f))
            }
        }

        item {
            Row(
                modifier = Modifier.fillMaxWidth(),
                horizontalArrangement = Arrangement.spacedBy(10.dp)
            ) {
                MetricCard("Converted Clients", "$convertedLeads", TechGreen, Modifier.weight(1f))
                MetricCard("Conversion %", "$conversionRate%", TechBlueAccent, Modifier.weight(1f))
            }
        }

        item {
            Row(
                modifier = Modifier.fillMaxWidth(),
                horizontalArrangement = Arrangement.spacedBy(10.dp)
            ) {
                MetricCard("Active Courses", "${courses.size}", TechSky, Modifier.weight(1f))
                MetricCard("Published Blogs", "${blogPosts.size}", DarkTextPrimary, Modifier.weight(1f))
            }
        }

        item {
            Surface(
                color = MaterialTheme.colorScheme.surface,
                shape = RoundedCornerShape(14.dp),
                tonalElevation = 2.dp,
                modifier = Modifier.fillMaxWidth()
            ) {
                Column(modifier = Modifier.padding(16.dp)) {
                    Text("Lead Breakdown by Type", fontWeight = FontWeight.Bold, color = TechCyan)
                    Spacer(modifier = Modifier.height(10.dp))

                    val types = enquiries.groupBy { it.type }
                    if (types.isEmpty()) {
                        Text("No inquiries recorded yet.", fontSize = 12.sp, color = MaterialTheme.colorScheme.onSurfaceVariant)
                    } else {
                        types.forEach { (type, list) ->
                            Row(
                                modifier = Modifier
                                    .fillMaxWidth()
                                    .padding(vertical = 4.dp),
                                horizontalArrangement = Arrangement.SpaceBetween
                            ) {
                                Text(type.replace('_', ' '), fontSize = 12.sp)
                                Text("${list.size} leads", fontWeight = FontWeight.Bold, fontSize = 12.sp, color = TechCyan)
                            }
                        }
                    }
                }
            }
        }
    }
}

@Composable
fun MetricCard(title: String, value: String, accentColor: Color, modifier: Modifier) {
    Surface(
        color = MaterialTheme.colorScheme.surface,
        shape = RoundedCornerShape(14.dp),
        tonalElevation = 2.dp,
        modifier = modifier
    ) {
        Column(modifier = Modifier.padding(16.dp)) {
            Text(text = title, fontSize = 11.sp, color = MaterialTheme.colorScheme.onSurfaceVariant)
            Spacer(modifier = Modifier.height(6.dp))
            Text(text = value, fontSize = 24.sp, fontWeight = FontWeight.ExtraBold, color = accentColor)
        }
    }
}
