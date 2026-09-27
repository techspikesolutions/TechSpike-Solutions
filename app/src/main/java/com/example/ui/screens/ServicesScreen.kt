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
import com.example.data.model.ServiceItem
import com.example.ui.components.CircuitBackground
import com.example.ui.components.ContactHelper
import com.example.ui.components.SectionHeading
import com.example.ui.theme.*

@Composable
fun ServicesScreen(
    services: List<ServiceItem>,
    onServiceClick: (String) -> Unit,
    onRequestQuote: (String) -> Unit
) {
    var selectedCategory by remember { mutableStateOf("All") }
    var searchQuery by remember { mutableStateOf("") }
    val context = LocalContext.current

    val categories = listOf("All", "AI & Automation", "Development", "Marketing", "Infrastructure", "Cloud", "Networking", "Consulting")

    val filteredServices = services.filter { service ->
        (selectedCategory == "All" || service.category.equals(selectedCategory, ignoreCase = true)) &&
        (searchQuery.isBlank() ||
         service.title.contains(searchQuery, ignoreCase = true) ||
         service.shortDescription.contains(searchQuery, ignoreCase = true) ||
         service.features.any { it.contains(searchQuery, ignoreCase = true) })
    }

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
                    shape = RoundedCornerShape(20.dp),
                    modifier = Modifier
                        .fillMaxWidth()
                        .padding(16.dp)
                ) {
                    Column(modifier = Modifier.padding(18.dp)) {
                        Row(
                            verticalAlignment = Alignment.CenterVertically,
                            modifier = Modifier
                                .clip(RoundedCornerShape(12.dp))
                                .background(TechCyan.copy(alpha = 0.2f))
                                .padding(horizontal = 10.dp, vertical = 4.dp)
                        ) {
                            Icon(Icons.Default.BusinessCenter, null, tint = TechCyan, modifier = Modifier.size(14.dp))
                            Spacer(modifier = Modifier.width(6.dp))
                            Text("ENTERPRISE IT SERVICES", fontSize = 10.sp, fontWeight = FontWeight.Bold, color = TechCyan)
                        }

                        Spacer(modifier = Modifier.height(10.dp))

                        Text(
                            text = "Comprehensive IT Solutions for Global Businesses",
                            style = MaterialTheme.typography.titleLarge,
                            fontWeight = FontWeight.Bold,
                            color = Color.White
                        )

                        Spacer(modifier = Modifier.height(6.dp))

                        Text(
                            text = "TechSpike Solutions provides end-to-end digital transformation, cloud workspaces, high-speed networking, and 24/7 technical consulting.",
                            style = MaterialTheme.typography.bodySmall,
                            color = DarkTextSecondary,
                            lineHeight = 18.sp
                        )

                        Spacer(modifier = Modifier.height(14.dp))

                        Button(
                            onClick = { onRequestQuote("IT Consulting & Services") },
                            colors = ButtonDefaults.buttonColors(
                                containerColor = TechCyan,
                                contentColor = TechMidnight
                            ),
                            shape = RoundedCornerShape(10.dp),
                            modifier = Modifier.fillMaxWidth()
                        ) {
                            Icon(Icons.Default.RequestQuote, null, modifier = Modifier.size(18.dp))
                            Spacer(modifier = Modifier.width(8.dp))
                            Text("Request Free Proposal & Evaluation", fontWeight = FontWeight.Bold)
                        }
                    }
                }
            }

            // Search Bar
            item {
                OutlinedTextField(
                    value = searchQuery,
                    onValueChange = { searchQuery = it },
                    placeholder = { Text("Search services, web, cloud, SEO, etc...") },
                    leadingIcon = { Icon(Icons.Default.Search, null, tint = TechCyan) },
                    trailingIcon = {
                        if (searchQuery.isNotEmpty()) {
                            IconButton(onClick = { searchQuery = "" }) {
                                Icon(Icons.Default.Close, null)
                            }
                        }
                    },
                    modifier = Modifier
                        .fillMaxWidth()
                        .padding(horizontal = 16.dp, vertical = 4.dp),
                    shape = RoundedCornerShape(14.dp),
                    singleLine = true
                )
            }

            // Category Filter Chips
            item {
                LazyRow(
                    contentPadding = PaddingValues(horizontal = 16.dp, vertical = 8.dp),
                    horizontalArrangement = Arrangement.spacedBy(8.dp)
                ) {
                    items(categories) { cat ->
                        FilterChip(
                            selected = selectedCategory == cat,
                            onClick = { selectedCategory = cat },
                            label = { Text(cat, fontSize = 12.sp) },
                            colors = FilterChipDefaults.filterChipColors(
                                selectedContainerColor = TechCyan,
                                selectedLabelColor = TechMidnight
                            )
                        )
                    }
                }
            }

            item {
                SectionHeading(
                    title = "Available Services (${filteredServices.size})",
                    subtitle = "Click any service to view problems solved, process, and case studies"
                )
            }

            items(filteredServices) { service ->
                FullServiceItemCard(
                    service = service,
                    onClick = { onServiceClick(service.id) },
                    onQuoteClick = { onRequestQuote(service.title) },
                    onWhatsAppClick = {
                        ContactHelper.openWhatsApp(
                            context,
                            "Hello TechSpike Solutions, I am interested in your ${service.title} services. I would like to discuss my requirements."
                        )
                    }
                )
            }
        }
    }
}

@Composable
fun FullServiceItemCard(
    service: ServiceItem,
    onClick: () -> Unit,
    onQuoteClick: () -> Unit,
    onWhatsAppClick: () -> Unit
) {
    val icon = when (service.id) {
        "ai-solutions" -> Icons.Default.SmartToy
        "web-development" -> Icons.Default.Language
        "seo-digital-marketing" -> Icons.Default.TrendingUp
        "disaster-recovery" -> Icons.Default.Security
        "cloud-desktop" -> Icons.Default.Cloud
        "network-solutions" -> Icons.Default.Router
        else -> Icons.Default.SupportAgent
    }

    Surface(
        color = MaterialTheme.colorScheme.surface,
        shape = RoundedCornerShape(18.dp),
        tonalElevation = 3.dp,
        modifier = Modifier
            .fillMaxWidth()
            .padding(horizontal = 16.dp, vertical = 8.dp)
            .clickable(onClick = onClick)
    ) {
        Column(modifier = Modifier.padding(18.dp)) {
            Row(
                modifier = Modifier.fillMaxWidth(),
                verticalAlignment = Alignment.CenterVertically
            ) {
                Box(
                    modifier = Modifier
                        .size(50.dp)
                        .clip(RoundedCornerShape(14.dp))
                        .background(TechCyan.copy(alpha = 0.15f)),
                    contentAlignment = Alignment.Center
                ) {
                    Icon(icon, null, tint = TechCyan, modifier = Modifier.size(26.dp))
                }

                Spacer(modifier = Modifier.width(14.dp))

                Column(modifier = Modifier.weight(1f)) {
                    Text(
                        text = service.title,
                        style = MaterialTheme.typography.titleMedium,
                        fontWeight = FontWeight.Bold,
                        color = MaterialTheme.colorScheme.onSurface
                    )
                    Text(
                        text = service.category,
                        style = MaterialTheme.typography.labelSmall,
                        color = TechCyan,
                        fontWeight = FontWeight.SemiBold
                    )
                }
            }

            Spacer(modifier = Modifier.height(10.dp))

            Text(
                text = service.shortDescription,
                style = MaterialTheme.typography.bodyMedium,
                color = MaterialTheme.colorScheme.onSurfaceVariant,
                lineHeight = 20.sp
            )

            Spacer(modifier = Modifier.height(12.dp))

            // Problem solved teaser
            Column(verticalArrangement = Arrangement.spacedBy(4.dp)) {
                service.problemsSolved.take(2).forEach { problem ->
                    Row(verticalAlignment = Alignment.CenterVertically) {
                        Icon(
                            imageVector = Icons.Default.CheckCircleOutline,
                            contentDescription = null,
                            tint = TechCyan,
                            modifier = Modifier.size(14.dp)
                        )
                        Spacer(modifier = Modifier.width(6.dp))
                        Text(
                            text = problem,
                            style = MaterialTheme.typography.bodySmall,
                            color = MaterialTheme.colorScheme.onSurfaceVariant,
                            maxLines = 1
                        )
                    }
                }
            }

            Spacer(modifier = Modifier.height(16.dp))

            Row(
                modifier = Modifier.fillMaxWidth(),
                horizontalArrangement = Arrangement.spacedBy(8.dp)
            ) {
                OutlinedButton(
                    onClick = onClick,
                    shape = RoundedCornerShape(10.dp),
                    modifier = Modifier.weight(1f)
                ) {
                    Text("View Details", fontSize = 12.sp)
                }

                Button(
                    onClick = onQuoteClick,
                    shape = RoundedCornerShape(10.dp),
                    colors = ButtonDefaults.buttonColors(
                        containerColor = TechDarkBlue,
                        contentColor = TechCyan
                    ),
                    modifier = Modifier.weight(1f)
                ) {
                    Text("Request Quote", fontSize = 12.sp, fontWeight = FontWeight.Bold)
                }

                IconButton(
                    onClick = onWhatsAppClick,
                    modifier = Modifier
                        .size(40.dp)
                        .clip(RoundedCornerShape(10.dp))
                        .background(TechGreen.copy(alpha = 0.2f))
                ) {
                    Icon(
                        imageVector = Icons.Default.Chat,
                        contentDescription = "WhatsApp",
                        tint = TechGreen,
                        modifier = Modifier.size(20.dp)
                    )
                }
            }
        }
    }
}
