package com.example.ui.screens

import androidx.compose.foundation.Image
import androidx.compose.foundation.background
import androidx.compose.foundation.border
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.lazy.LazyColumn
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
import androidx.compose.ui.layout.ContentScale
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.example.R
import com.example.ui.components.CircuitBackground
import com.example.ui.components.ContactHelper
import com.example.ui.components.SectionHeading
import com.example.ui.theme.*

@Composable
fun ContactScreen(
    onSubmitMessage: (name: String, email: String, phone: String, subject: String, message: String) -> Unit
) {
    val context = LocalContext.current

    var name by remember { mutableStateOf("") }
    var email by remember { mutableStateOf("") }
    var phone by remember { mutableStateOf("") }
    var subject by remember { mutableStateOf("") }
    var message by remember { mutableStateOf("") }

    Box(modifier = Modifier.fillMaxSize()) {
        CircuitBackground()

        LazyColumn(
            modifier = Modifier.fillMaxSize(),
            contentPadding = PaddingValues(bottom = 100.dp)
        ) {
            // Header Banner
            item {
                Surface(
                    color = TechDarkBlue,
                    shape = RoundedCornerShape(bottomStart = 20.dp, bottomEnd = 20.dp),
                    modifier = Modifier.fillMaxWidth()
                ) {
                    Column(modifier = Modifier.padding(20.dp)) {
                        Row(
                            verticalAlignment = Alignment.CenterVertically,
                            modifier = Modifier
                                .clip(RoundedCornerShape(8.dp))
                                .background(TechCyan.copy(alpha = 0.2f))
                                .padding(horizontal = 10.dp, vertical = 4.dp)
                        ) {
                            Icon(Icons.Default.HeadsetMic, null, tint = TechCyan, modifier = Modifier.size(14.dp))
                            Spacer(modifier = Modifier.width(6.dp))
                            Text("GET IN TOUCH", fontSize = 10.sp, fontWeight = FontWeight.Bold, color = TechCyan)
                        }

                        Spacer(modifier = Modifier.height(12.dp))

                        Row(
                            verticalAlignment = Alignment.CenterVertically,
                            horizontalArrangement = Arrangement.spacedBy(12.dp)
                        ) {
                            Box(
                                modifier = Modifier
                                    .size(38.dp)
                                    .clip(RoundedCornerShape(8.dp))
                                    .background(TechDarkBlue.copy(alpha = 0.6f))
                                    .border(1.dp, TechCyan.copy(alpha = 0.25f), RoundedCornerShape(8.dp))
                                    .padding(3.dp),
                                contentAlignment = Alignment.Center
                            ) {
                                Image(
                                    painter = painterResource(id = R.drawable.ic_techspike_logo),
                                    contentDescription = "TechSpike Logo",
                                    modifier = Modifier.fillMaxSize(),
                                    contentScale = ContentScale.Fit
                                )
                            }
                            Text(
                                text = "Connect with TechSpike",
                                style = MaterialTheme.typography.titleLarge,
                                fontWeight = FontWeight.Bold,
                                color = Color.White
                            )
                        }

                        Spacer(modifier = Modifier.height(6.dp))

                        Text(
                            text = "Have questions regarding our IT services or training batches? We are here to help your business and career thrive.",
                            style = MaterialTheme.typography.bodySmall,
                            color = DarkTextSecondary,
                            lineHeight = 18.sp
                        )

                        Spacer(modifier = Modifier.height(16.dp))

                        // Fast Connect Buttons
                        Row(
                            modifier = Modifier.fillMaxWidth(),
                            horizontalArrangement = Arrangement.spacedBy(8.dp)
                        ) {
                            FilledTonalButton(
                                onClick = { ContactHelper.callPhone(context) },
                                shape = RoundedCornerShape(10.dp),
                                modifier = Modifier.weight(1f)
                            ) {
                                Icon(Icons.Default.Phone, null, modifier = Modifier.size(16.dp))
                                Spacer(modifier = Modifier.width(4.dp))
                                Text("Call", fontSize = 12.sp)
                            }

                            FilledTonalButton(
                                onClick = {
                                    ContactHelper.openWhatsApp(
                                        context,
                                        "Hello TechSpike Solutions, I would like to connect with your team."
                                    )
                                },
                                shape = RoundedCornerShape(10.dp),
                                colors = ButtonDefaults.filledTonalButtonColors(
                                    containerColor = TechGreen.copy(alpha = 0.2f),
                                    contentColor = TechGreen
                                ),
                                modifier = Modifier.weight(1.2f)
                            ) {
                                Icon(Icons.Default.Chat, null, modifier = Modifier.size(16.dp))
                                Spacer(modifier = Modifier.width(4.dp))
                                Text("WhatsApp", fontSize = 12.sp)
                            }

                            FilledTonalButton(
                                onClick = { ContactHelper.sendEmail(context) },
                                shape = RoundedCornerShape(10.dp),
                                modifier = Modifier.weight(1f)
                            ) {
                                Icon(Icons.Default.Email, null, modifier = Modifier.size(16.dp))
                                Spacer(modifier = Modifier.width(4.dp))
                                Text("Email", fontSize = 12.sp)
                            }
                        }
                    }
                }
            }

            // Facility Locations
            item {
                SectionHeading(
                    title = "Our Office & Training Campuses",
                    subtitle = "Ludhiana centers for corporate consultation & hands-on student labs"
                )
            }

            item {
                // Corporate Office Card
                ContactLocationCard(
                    title = "TechSpike Corporate Office",
                    badge = "Business Solutions",
                    address = ContactHelper.OFFICE_ADDRESS,
                    timing = "Mon - Sat: 9:30 AM - 6:30 PM",
                    onMapClick = { ContactHelper.openMapDirections(context, ContactHelper.OFFICE_ADDRESS) }
                )
            }

            item {
                Spacer(modifier = Modifier.height(6.dp))
                // Training Institute Campus Card
                ContactLocationCard(
                    title = "TechSpike Training Institute",
                    badge = "Classroom & Labs",
                    address = ContactHelper.INSTITUTE_ADDRESS,
                    timing = "Mon - Sat: 8:00 AM - 7:00 PM",
                    onMapClick = { ContactHelper.openMapDirections(context, ContactHelper.INSTITUTE_ADDRESS) }
                )
            }

            // Contact Form
            item {
                SectionHeading(
                    title = "Send Us a Message",
                    subtitle = "Fill in the details below and our team will respond within 24 hours"
                )
            }

            item {
                Surface(
                    color = MaterialTheme.colorScheme.surface,
                    shape = RoundedCornerShape(18.dp),
                    tonalElevation = 3.dp,
                    modifier = Modifier
                        .fillMaxWidth()
                        .padding(horizontal = 16.dp)
                ) {
                    Column(
                        modifier = Modifier.padding(18.dp),
                        verticalArrangement = Arrangement.spacedBy(12.dp)
                    ) {
                        OutlinedTextField(
                            value = name,
                            onValueChange = { name = it },
                            label = { Text("Your Name *") },
                            leadingIcon = { Icon(Icons.Outlined.Person, null) },
                            modifier = Modifier.fillMaxWidth(),
                            singleLine = true
                        )

                        OutlinedTextField(
                            value = phone,
                            onValueChange = { phone = it },
                            label = { Text("Phone / WhatsApp Number *") },
                            leadingIcon = { Icon(Icons.Outlined.Phone, null) },
                            modifier = Modifier.fillMaxWidth(),
                            singleLine = true
                        )

                        OutlinedTextField(
                            value = email,
                            onValueChange = { email = it },
                            label = { Text("Email Address *") },
                            leadingIcon = { Icon(Icons.Outlined.Email, null) },
                            modifier = Modifier.fillMaxWidth(),
                            singleLine = true
                        )

                        OutlinedTextField(
                            value = subject,
                            onValueChange = { subject = it },
                            label = { Text("Subject / Purpose of Enquiry") },
                            leadingIcon = { Icon(Icons.Outlined.Topic, null) },
                            modifier = Modifier.fillMaxWidth(),
                            singleLine = true
                        )

                        OutlinedTextField(
                            value = message,
                            onValueChange = { message = it },
                            label = { Text("Message") },
                            modifier = Modifier
                                .fillMaxWidth()
                                .height(110.dp),
                            maxLines = 4
                        )

                        Button(
                            onClick = {
                                if (name.isNotBlank() && phone.isNotBlank()) {
                                    onSubmitMessage(name, email, phone, subject.ifBlank { "General Inquiry" }, message)
                                    name = ""
                                    email = ""
                                    phone = ""
                                    subject = ""
                                    message = ""
                                }
                            },
                            colors = ButtonDefaults.buttonColors(
                                containerColor = TechCyan,
                                contentColor = TechMidnight
                            ),
                            shape = RoundedCornerShape(10.dp),
                            modifier = Modifier
                                .fillMaxWidth()
                                .height(48.dp)
                        ) {
                            Icon(Icons.Default.Send, null, modifier = Modifier.size(16.dp))
                            Spacer(modifier = Modifier.width(8.dp))
                            Text("Send Message", fontWeight = FontWeight.Bold)
                        }
                    }
                }
            }
        }
    }
}

@Composable
fun ContactLocationCard(
    title: String,
    badge: String,
    address: String,
    timing: String,
    onMapClick: () -> Unit
) {
    Surface(
        color = MaterialTheme.colorScheme.surface,
        shape = RoundedCornerShape(16.dp),
        tonalElevation = 2.dp,
        modifier = Modifier
            .fillMaxWidth()
            .padding(horizontal = 16.dp, vertical = 4.dp)
    ) {
        Column(modifier = Modifier.padding(16.dp)) {
            Row(
                modifier = Modifier.fillMaxWidth(),
                horizontalArrangement = Arrangement.SpaceBetween,
                verticalAlignment = Alignment.CenterVertically
            ) {
                Text(
                    text = title,
                    style = MaterialTheme.typography.titleMedium,
                    fontWeight = FontWeight.Bold,
                    color = MaterialTheme.colorScheme.onSurface
                )
                Box(
                    modifier = Modifier
                        .clip(RoundedCornerShape(6.dp))
                        .background(TechCyan.copy(alpha = 0.15f))
                        .padding(horizontal = 8.dp, vertical = 3.dp)
                ) {
                    Text(text = badge, fontSize = 10.sp, fontWeight = FontWeight.Bold, color = TechCyan)
                }
            }

            Spacer(modifier = Modifier.height(8.dp))

            Row(verticalAlignment = Alignment.Top) {
                Icon(Icons.Outlined.Place, null, tint = TechCyan, modifier = Modifier.size(18.dp))
                Spacer(modifier = Modifier.width(8.dp))
                Text(
                    text = address,
                    style = MaterialTheme.typography.bodySmall,
                    color = MaterialTheme.colorScheme.onSurfaceVariant,
                    lineHeight = 18.sp
                )
            }

            Spacer(modifier = Modifier.height(6.dp))

            Row(verticalAlignment = Alignment.CenterVertically) {
                Icon(Icons.Outlined.AccessTime, null, tint = DarkTextSecondary, modifier = Modifier.size(16.dp))
                Spacer(modifier = Modifier.width(8.dp))
                Text(
                    text = timing,
                    style = MaterialTheme.typography.labelSmall,
                    color = DarkTextSecondary
                )
            }

            Spacer(modifier = Modifier.height(12.dp))

            OutlinedButton(
                onClick = onMapClick,
                shape = RoundedCornerShape(8.dp),
                modifier = Modifier.fillMaxWidth()
            ) {
                Icon(Icons.Default.Directions, null, modifier = Modifier.size(16.dp))
                Spacer(modifier = Modifier.width(6.dp))
                Text("Get Directions on Google Maps", fontSize = 12.sp)
            }
        }
    }
}
