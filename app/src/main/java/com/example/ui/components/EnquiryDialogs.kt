package com.example.ui.components

import androidx.compose.foundation.background
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.foundation.verticalScroll
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.*
import androidx.compose.material.icons.outlined.*
import androidx.compose.material3.*
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.compose.ui.window.Dialog
import androidx.compose.ui.window.DialogProperties
import com.example.ui.theme.*

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun RequestQuoteDialog(
    initialService: String = "Website Development",
    onDismiss: () -> Unit,
    onSubmit: (
        name: String,
        email: String,
        phone: String,
        service: String,
        companyName: String,
        budget: String,
        message: String
    ) -> Unit
) {
    var name by remember { mutableStateOf("") }
    var company by remember { mutableStateOf("") }
    var phone by remember { mutableStateOf("") }
    var email by remember { mutableStateOf("") }
    var selectedService by remember { mutableStateOf(initialService) }
    var budgetRange by remember { mutableStateOf("₹25,000 - ₹50,000") }
    var description by remember { mutableStateOf("") }
    var isServiceDropdownExpanded by remember { mutableStateOf(false) }

    val servicesList = listOf(
        "AI & Automation Solutions",
        "Website Development",
        "Web Applications",
        "E-commerce Development",
        "SEO & Digital Marketing",
        "Google Ads / PPC",
        "Disaster Recovery & Backup",
        "Cloud Desktop (VDI)",
        "Network Solutions & Wi-Fi",
        "IT Support & Managed Services",
        "Other IT Consulting"
    )

    val budgetOptions = listOf(
        "< ₹25,000",
        "₹25,000 - ₹50,000",
        "₹50,000 - ₹1,00,000",
        "₹1,00,000 - ₹3,00,000",
        "₹3,00,000+"
    )

    Dialog(
        onDismissRequest = onDismiss,
        properties = DialogProperties(usePlatformDefaultWidth = false)
    ) {
        Surface(
            modifier = Modifier
                .fillMaxWidth(0.94f)
                .fillMaxHeight(0.88f),
            shape = RoundedCornerShape(20.dp),
            color = MaterialTheme.colorScheme.surface,
            tonalElevation = 6.dp
        ) {
            Column(
                modifier = Modifier
                    .fillMaxSize()
                    .padding(20.dp)
            ) {
                // Header
                Row(
                    modifier = Modifier.fillMaxWidth(),
                    horizontalArrangement = Arrangement.SpaceBetween,
                    verticalAlignment = Alignment.CenterVertically
                ) {
                    Column(modifier = Modifier.weight(1f)) {
                        Text(
                            text = "Request a Free Quote",
                            style = MaterialTheme.typography.titleLarge,
                            fontWeight = FontWeight.Bold,
                            color = MaterialTheme.colorScheme.onSurface
                        )
                        Text(
                            text = "TechSpike Business Solutions",
                            style = MaterialTheme.typography.bodySmall,
                            color = TechCyan
                        )
                    }
                    IconButton(onClick = onDismiss) {
                        Icon(Icons.Default.Close, contentDescription = "Close")
                    }
                }

                HorizontalDivider(modifier = Modifier.padding(vertical = 12.dp))

                Column(
                    modifier = Modifier
                        .weight(1f)
                        .verticalScroll(rememberScrollState()),
                    verticalArrangement = Arrangement.spacedBy(12.dp)
                ) {
                    OutlinedTextField(
                        value = name,
                        onValueChange = { name = it },
                        label = { Text("Your Full Name *") },
                        leadingIcon = { Icon(Icons.Outlined.Person, null) },
                        modifier = Modifier.fillMaxWidth(),
                        singleLine = true
                    )

                    OutlinedTextField(
                        value = company,
                        onValueChange = { company = it },
                        label = { Text("Company / Organization Name") },
                        leadingIcon = { Icon(Icons.Outlined.Business, null) },
                        modifier = Modifier.fillMaxWidth(),
                        singleLine = true
                    )

                    Row(
                        modifier = Modifier.fillMaxWidth(),
                        horizontalArrangement = Arrangement.spacedBy(8.dp)
                    ) {
                        OutlinedTextField(
                            value = phone,
                            onValueChange = { phone = it },
                            label = { Text("Phone Number *") },
                            leadingIcon = { Icon(Icons.Outlined.Phone, null) },
                            modifier = Modifier.weight(1f),
                            singleLine = true
                        )

                        OutlinedTextField(
                            value = email,
                            onValueChange = { email = it },
                            label = { Text("Email Address *") },
                            leadingIcon = { Icon(Icons.Outlined.Email, null) },
                            modifier = Modifier.weight(1f),
                            singleLine = true
                        )
                    }

                    // Service Dropdown
                    ExposedDropdownMenuBox(
                        expanded = isServiceDropdownExpanded,
                        onExpandedChange = { isServiceDropdownExpanded = !isServiceDropdownExpanded }
                    ) {
                        OutlinedTextField(
                            value = selectedService,
                            onValueChange = {},
                            readOnly = true,
                            label = { Text("Service Required *") },
                            trailingIcon = { ExposedDropdownMenuDefaults.TrailingIcon(expanded = isServiceDropdownExpanded) },
                            modifier = Modifier
                                .menuAnchor()
                                .fillMaxWidth()
                        )
                        ExposedDropdownMenu(
                            expanded = isServiceDropdownExpanded,
                            onDismissRequest = { isServiceDropdownExpanded = false }
                        ) {
                            servicesList.forEach { service ->
                                DropdownMenuItem(
                                    text = { Text(service) },
                                    onClick = {
                                        selectedService = service
                                        isServiceDropdownExpanded = false
                                    }
                                )
                            }
                        }
                    }

                    Text(
                        text = "Estimated Budget Range",
                        style = MaterialTheme.typography.labelMedium,
                        color = MaterialTheme.colorScheme.onSurfaceVariant
                    )

                    Row(
                        modifier = Modifier.fillMaxWidth(),
                        horizontalArrangement = Arrangement.spacedBy(6.dp)
                    ) {
                        budgetOptions.take(3).forEach { option ->
                            FilterChip(
                                selected = budgetRange == option,
                                onClick = { budgetRange = option },
                                label = { Text(option, fontSize = 11.sp) }
                            )
                        }
                    }

                    OutlinedTextField(
                        value = description,
                        onValueChange = { description = it },
                        label = { Text("Project Details & Specific Requirements") },
                        placeholder = { Text("Briefly describe what you're looking to achieve...") },
                        modifier = Modifier
                            .fillMaxWidth()
                            .height(100.dp),
                        maxLines = 4
                    )
                }

                Spacer(modifier = Modifier.height(12.dp))

                Button(
                    onClick = {
                        if (name.isNotBlank() && phone.isNotBlank()) {
                            onSubmit(name, email, phone, selectedService, company, budgetRange, description)
                            onDismiss()
                        }
                    },
                    modifier = Modifier
                        .fillMaxWidth()
                        .height(50.dp),
                    shape = RoundedCornerShape(12.dp),
                    colors = ButtonDefaults.buttonColors(
                        containerColor = TechCyan,
                        contentColor = TechMidnight
                    )
                ) {
                    Icon(Icons.Default.Send, contentDescription = null, modifier = Modifier.size(18.dp))
                    Spacer(modifier = Modifier.width(8.dp))
                    Text("Request Free Consultation", fontWeight = FontWeight.Bold)
                }
            }
        }
    }
}

@Composable
fun CourseEnquiryDialog(
    initialCourse: String = "Web Design & Web Development",
    onDismiss: () -> Unit,
    onSubmit: (
        name: String,
        email: String,
        phone: String,
        course: String,
        mode: String,
        batch: String,
        message: String
    ) -> Unit
) {
    var studentName by remember { mutableStateOf("") }
    var mobileNumber by remember { mutableStateOf("") }
    var emailAddress by remember { mutableStateOf("") }
    var courseInterested by remember { mutableStateOf(initialCourse) }
    var preferredMode by remember { mutableStateOf("Classroom Lab (Ludhiana)") }
    var preferredBatch by remember { mutableStateOf("Upcoming Monday Batch") }
    var queryMessage by remember { mutableStateOf("") }

    val modeOptions = listOf(
        "Classroom Lab (Ludhiana)",
        "Live Online Interactive",
        "Weekend Batch Only"
    )

    Dialog(
        onDismissRequest = onDismiss,
        properties = DialogProperties(usePlatformDefaultWidth = false)
    ) {
        Surface(
            modifier = Modifier
                .fillMaxWidth(0.94f)
                .fillMaxHeight(0.85f),
            shape = RoundedCornerShape(20.dp),
            color = MaterialTheme.colorScheme.surface,
            tonalElevation = 6.dp
        ) {
            Column(
                modifier = Modifier
                    .fillMaxSize()
                    .padding(20.dp)
            ) {
                // Header
                Row(
                    modifier = Modifier.fillMaxWidth(),
                    horizontalArrangement = Arrangement.SpaceBetween,
                    verticalAlignment = Alignment.CenterVertically
                ) {
                    Column(modifier = Modifier.weight(1f)) {
                        Text(
                            text = "Course Enquiry & Admission",
                            style = MaterialTheme.typography.titleLarge,
                            fontWeight = FontWeight.Bold,
                            color = MaterialTheme.colorScheme.onSurface
                        )
                        Text(
                            text = "TechSpike Training Institute, Ludhiana",
                            style = MaterialTheme.typography.bodySmall,
                            color = TechCyan
                        )
                    }
                    IconButton(onClick = onDismiss) {
                        Icon(Icons.Default.Close, contentDescription = "Close")
                    }
                }

                HorizontalDivider(modifier = Modifier.padding(vertical = 12.dp))

                Column(
                    modifier = Modifier
                        .weight(1f)
                        .verticalScroll(rememberScrollState()),
                    verticalArrangement = Arrangement.spacedBy(12.dp)
                ) {
                    OutlinedTextField(
                        value = studentName,
                        onValueChange = { studentName = it },
                        label = { Text("Student Name *") },
                        leadingIcon = { Icon(Icons.Outlined.Person, null) },
                        modifier = Modifier.fillMaxWidth(),
                        singleLine = true
                    )

                    OutlinedTextField(
                        value = mobileNumber,
                        onValueChange = { mobileNumber = it },
                        label = { Text("Mobile / WhatsApp Number *") },
                        leadingIcon = { Icon(Icons.Outlined.Phone, null) },
                        modifier = Modifier.fillMaxWidth(),
                        singleLine = true
                    )

                    OutlinedTextField(
                        value = emailAddress,
                        onValueChange = { emailAddress = it },
                        label = { Text("Email Address") },
                        leadingIcon = { Icon(Icons.Outlined.Email, null) },
                        modifier = Modifier.fillMaxWidth(),
                        singleLine = true
                    )

                    OutlinedTextField(
                        value = courseInterested,
                        onValueChange = { courseInterested = it },
                        label = { Text("Course Interested In *") },
                        leadingIcon = { Icon(Icons.Outlined.School, null) },
                        modifier = Modifier.fillMaxWidth()
                    )

                    Text(
                        text = "Preferred Learning Mode",
                        style = MaterialTheme.typography.labelMedium,
                        color = MaterialTheme.colorScheme.onSurfaceVariant
                    )

                    Column(verticalArrangement = Arrangement.spacedBy(4.dp)) {
                        modeOptions.forEach { mode ->
                            Row(
                                verticalAlignment = Alignment.CenterVertically,
                                modifier = Modifier
                                    .fillMaxWidth()
                                    .clickable { preferredMode = mode }
                                    .padding(vertical = 4.dp)
                            ) {
                                RadioButton(
                                    selected = preferredMode == mode,
                                    onClick = { preferredMode = mode }
                                )
                                Spacer(modifier = Modifier.width(8.dp))
                                Text(text = mode, style = MaterialTheme.typography.bodyMedium)
                            }
                        }
                    }

                    OutlinedTextField(
                        value = queryMessage,
                        onValueChange = { queryMessage = it },
                        label = { Text("Any specific questions? (Fees, Timings, Placement)") },
                        modifier = Modifier
                            .fillMaxWidth()
                            .height(90.dp),
                        maxLines = 3
                    )
                }

                Spacer(modifier = Modifier.height(12.dp))

                Button(
                    onClick = {
                        if (studentName.isNotBlank() && mobileNumber.isNotBlank()) {
                            onSubmit(
                                studentName,
                                emailAddress,
                                mobileNumber,
                                courseInterested,
                                preferredMode,
                                preferredBatch,
                                queryMessage
                            )
                            onDismiss()
                        }
                    },
                    modifier = Modifier
                        .fillMaxWidth()
                        .height(50.dp),
                    shape = RoundedCornerShape(12.dp),
                    colors = ButtonDefaults.buttonColors(
                        containerColor = TechCyan,
                        contentColor = TechMidnight
                    )
                ) {
                    Icon(Icons.Default.School, contentDescription = null, modifier = Modifier.size(18.dp))
                    Spacer(modifier = Modifier.width(8.dp))
                    Text("Submit Enquiry", fontWeight = FontWeight.Bold)
                }
            }
        }
    }
}

@Composable
fun CareerCounsellingDialog(
    onDismiss: () -> Unit,
    onSubmit: (name: String, phone: String, education: String, interest: String) -> Unit
) {
    var name by remember { mutableStateOf("") }
    var phone by remember { mutableStateOf("") }
    var education by remember { mutableStateOf("Undergraduate / In College") }
    var interest by remember { mutableStateOf("Not decided yet (Need guidance)") }

    val interestOptions = listOf(
        "Web & App Development",
        "Cyber Security & Ethical Hacking",
        "Digital Marketing & SEO",
        "Computer Networks & IT Admin",
        "Not decided yet (Need guidance)"
    )

    Dialog(onDismissRequest = onDismiss) {
        Surface(
            shape = RoundedCornerShape(20.dp),
            color = MaterialTheme.colorScheme.surface,
            tonalElevation = 6.dp,
            modifier = Modifier.fillMaxWidth(0.95f)
        ) {
            Column(
                modifier = Modifier
                    .fillMaxWidth()
                    .padding(20.dp),
                verticalArrangement = Arrangement.spacedBy(12.dp)
            ) {
                Row(
                    modifier = Modifier.fillMaxWidth(),
                    horizontalArrangement = Arrangement.SpaceBetween,
                    verticalAlignment = Alignment.CenterVertically
                ) {
                    Text(
                        text = "Free Career Counselling",
                        style = MaterialTheme.typography.titleLarge,
                        fontWeight = FontWeight.Bold,
                        color = MaterialTheme.colorScheme.onSurface
                    )
                    IconButton(onClick = onDismiss) {
                        Icon(Icons.Default.Close, contentDescription = "Close")
                    }
                }

                Text(
                    text = "Speak with TechSpike senior technology mentors. We help you choose the right IT career path based on your background.",
                    style = MaterialTheme.typography.bodySmall,
                    color = MaterialTheme.colorScheme.onSurfaceVariant
                )

                OutlinedTextField(
                    value = name,
                    onValueChange = { name = it },
                    label = { Text("Your Name *") },
                    modifier = Modifier.fillMaxWidth(),
                    singleLine = true
                )

                OutlinedTextField(
                    value = phone,
                    onValueChange = { phone = it },
                    label = { Text("Phone / WhatsApp *") },
                    modifier = Modifier.fillMaxWidth(),
                    singleLine = true
                )

                OutlinedTextField(
                    value = education,
                    onValueChange = { education = it },
                    label = { Text("Current Education / Background") },
                    modifier = Modifier.fillMaxWidth(),
                    singleLine = true
                )

                Text(
                    text = "Area of Interest:",
                    style = MaterialTheme.typography.labelMedium,
                    color = MaterialTheme.colorScheme.onSurface
                )

                Column(verticalArrangement = Arrangement.spacedBy(2.dp)) {
                    interestOptions.take(3).forEach { opt ->
                        FilterChip(
                            selected = interest == opt,
                            onClick = { interest = opt },
                            label = { Text(opt, fontSize = 11.sp) },
                            modifier = Modifier.fillMaxWidth()
                        )
                    }
                }

                Button(
                    onClick = {
                        if (name.isNotBlank() && phone.isNotBlank()) {
                            onSubmit(name, phone, education, interest)
                            onDismiss()
                        }
                    },
                    modifier = Modifier
                        .fillMaxWidth()
                        .height(48.dp),
                    shape = RoundedCornerShape(12.dp),
                    colors = ButtonDefaults.buttonColors(
                        containerColor = TechCyan,
                        contentColor = TechMidnight
                    )
                ) {
                    Text("Book Free 1-on-1 Session", fontWeight = FontWeight.Bold)
                }
            }
        }
    }
}
