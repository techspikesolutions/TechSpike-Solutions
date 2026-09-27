package com.example.ui.components

import android.content.Context
import android.content.Intent
import android.net.Uri
import androidx.compose.foundation.Canvas
import androidx.compose.foundation.background
import androidx.compose.foundation.Image
import androidx.compose.foundation.border
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.*
import androidx.compose.material.icons.outlined.*
import androidx.compose.material3.*
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.geometry.Offset
import androidx.compose.ui.graphics.Brush
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.vector.ImageVector
import androidx.compose.ui.layout.ContentScale
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.text.style.TextOverflow
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.example.R
import com.example.ui.theme.*

object ContactHelper {
    const val PHONE_NUMBER = "+919888888476"
    const val WHATSAPP_NUMBER = "919888888476"
    const val EMAIL_PRIMARY = "contact@techspikesolutions.in"
    const val OFFICE_ADDRESS = "Plot No. 3211, Sector 32-A, Chandigarh Road, Ludhiana, Punjab 141010"
    const val INSTITUTE_ADDRESS = "Near Homeo cure, opp. Best Price, Mundian Khurd, Ludhiana, Punjab 141003"
    const val WEBSITE_URL = "https://www.techspikesolutions.in/"

    fun callPhone(context: Context) {
        val intent = Intent(Intent.ACTION_DIAL).apply {
            data = Uri.parse("tel:$PHONE_NUMBER")
        }
        context.startActivity(intent)
    }

    fun sendEmail(context: Context, subject: String = "TechSpike Enquiry") {
        val intent = Intent(Intent.ACTION_SENDTO).apply {
            data = Uri.parse("mailto:$EMAIL_PRIMARY")
            putExtra(Intent.EXTRA_SUBJECT, subject)
        }
        context.startActivity(Intent.createChooser(intent, "Send Email via"))
    }

    fun openWhatsApp(context: Context, customMessage: String) {
        val encodedMsg = Uri.encode(customMessage)
        val url = "https://api.whatsapp.com/send?phone=$WHATSAPP_NUMBER&text=$encodedMsg"
        val intent = Intent(Intent.ACTION_VIEW, Uri.parse(url))
        context.startActivity(intent)
    }

    fun openMapDirections(context: Context, address: String = OFFICE_ADDRESS) {
        val mapUri = Uri.parse("geo:0,0?q=${Uri.encode(address)}")
        val intent = Intent(Intent.ACTION_VIEW, mapUri)
        context.startActivity(intent)
    }

    fun openWebsite(context: Context) {
        val intent = Intent(Intent.ACTION_VIEW, Uri.parse(WEBSITE_URL))
        context.startActivity(intent)
    }
}

@Composable
fun CircuitBackground(modifier: Modifier = Modifier) {
    val cyanColor = TechCyan.copy(alpha = 0.08f)
    val blueColor = TechBlueAccent.copy(alpha = 0.06f)

    Canvas(modifier = modifier.fillMaxSize()) {
        val w = size.width
        val h = size.height

        // Subtle circuit lines
        drawLine(
            color = cyanColor,
            start = Offset(w * 0.1f, 0f),
            end = Offset(w * 0.1f, h * 0.3f),
            strokeWidth = 2f
        )
        drawLine(
            color = cyanColor,
            start = Offset(w * 0.1f, h * 0.3f),
            end = Offset(w * 0.35f, h * 0.45f),
            strokeWidth = 2f
        )
        drawCircle(color = cyanColor, radius = 5f, center = Offset(w * 0.35f, h * 0.45f))

        drawLine(
            color = blueColor,
            start = Offset(w * 0.9f, h * 0.2f),
            end = Offset(w * 0.65f, h * 0.38f),
            strokeWidth = 2f
        )
        drawLine(
            color = blueColor,
            start = Offset(w * 0.65f, h * 0.38f),
            end = Offset(w * 0.65f, h * 0.7f),
            strokeWidth = 2f
        )
        drawCircle(color = blueColor, radius = 6f, center = Offset(w * 0.65f, h * 0.7f))

        drawLine(
            color = cyanColor,
            start = Offset(0f, h * 0.85f),
            end = Offset(w * 0.4f, h * 0.85f),
            strokeWidth = 2f
        )
        drawLine(
            color = cyanColor,
            start = Offset(w * 0.4f, h * 0.85f),
            end = Offset(w * 0.55f, h),
            strokeWidth = 2f
        )
    }
}

@Composable
fun TechSpikeTopBar(
    title: String = "TechSpike Solutions",
    subtitle: String = "IT Solutions & Digital Services",
    unreadCount: Int = 0,
    onNotificationsClick: () -> Unit = {},
    onSpikeAiClick: () -> Unit = {},
    onSearchClick: () -> Unit = {},
    onBackClick: (() -> Unit)? = null
) {
    Surface(
        color = MaterialTheme.colorScheme.surface,
        tonalElevation = 3.dp,
        modifier = Modifier.fillMaxWidth()
    ) {
        Column(
            modifier = Modifier
                .fillMaxWidth()
                .statusBarsPadding()
                .padding(horizontal = 16.dp, vertical = 10.dp)
        ) {
            Row(
                modifier = Modifier.fillMaxWidth(),
                verticalAlignment = Alignment.CenterVertically,
                horizontalArrangement = Arrangement.SpaceBetween
            ) {
                Row(
                    verticalAlignment = Alignment.CenterVertically,
                    modifier = Modifier.weight(1f)
                ) {
                    if (onBackClick != null) {
                        IconButton(
                            onClick = onBackClick,
                            modifier = Modifier.size(44.dp)
                        ) {
                            Icon(
                                imageVector = Icons.Default.ArrowBack,
                                contentDescription = "Back",
                                tint = MaterialTheme.colorScheme.onSurface
                            )
                        }
                        Spacer(modifier = Modifier.width(8.dp))
                    } else {
                        // Official TechSpike Company Logo (Clean Brand Mark)
                        Box(
                            modifier = Modifier
                                .size(48.dp),
                            contentAlignment = Alignment.Center
                        ) {
                            Image(
                                painter = painterResource(id = R.drawable.ic_techspike_logo),
                                contentDescription = "TechSpike Solutions Logo",
                                modifier = Modifier.fillMaxSize(),
                                contentScale = ContentScale.Fit
                            )
                        }
                        Spacer(modifier = Modifier.width(12.dp))
                    }

                    Column(modifier = Modifier.weight(1f, fill = false)) {
                        Text(
                            text = title,
                            style = MaterialTheme.typography.titleMedium,
                            fontWeight = FontWeight.Bold,
                            color = MaterialTheme.colorScheme.onSurface,
                            maxLines = 1,
                            overflow = TextOverflow.Ellipsis
                        )
                        Text(
                            text = subtitle,
                            style = MaterialTheme.typography.bodySmall,
                            color = MaterialTheme.colorScheme.onSurfaceVariant,
                            maxLines = 1,
                            overflow = TextOverflow.Ellipsis
                        )
                    }
                }

                // Action Buttons
                Row(verticalAlignment = Alignment.CenterVertically) {
                    IconButton(
                        onClick = onSearchClick,
                        modifier = Modifier.size(40.dp)
                    ) {
                        Icon(
                            imageVector = Icons.Outlined.Search,
                            contentDescription = "Search",
                            tint = MaterialTheme.colorScheme.onSurfaceVariant
                        )
                    }

                    IconButton(
                        onClick = onSpikeAiClick,
                        modifier = Modifier.size(40.dp)
                    ) {
                        BadgedBox(
                            badge = {
                                Badge(
                                    containerColor = TechCyan,
                                    contentColor = TechMidnight
                                ) {
                                    Text("AI", fontSize = 9.sp, fontWeight = FontWeight.Bold)
                                }
                            }
                        ) {
                            Icon(
                                imageVector = Icons.Outlined.SmartToy,
                                contentDescription = "Spike AI Assistant",
                                tint = TechCyan
                            )
                        }
                    }

                    IconButton(
                        onClick = onNotificationsClick,
                        modifier = Modifier.size(40.dp)
                    ) {
                        BadgedBox(
                            badge = {
                                if (unreadCount > 0) {
                                    Badge(
                                        containerColor = MaterialTheme.colorScheme.error
                                    ) {
                                        Text("$unreadCount")
                                    }
                                }
                            }
                        ) {
                            Icon(
                                imageVector = Icons.Outlined.Notifications,
                                contentDescription = "Notifications",
                                tint = MaterialTheme.colorScheme.onSurfaceVariant
                            )
                        }
                    }
                }
            }
        }
    }
}

@Composable
fun SectionHeading(
    title: String,
    subtitle: String? = null,
    actionText: String? = null,
    onActionClick: (() -> Unit)? = null
) {
    Column(
        modifier = Modifier
            .fillMaxWidth()
            .padding(horizontal = 16.dp, vertical = 12.dp)
    ) {
        Row(
            modifier = Modifier.fillMaxWidth(),
            horizontalArrangement = Arrangement.SpaceBetween,
            verticalAlignment = Alignment.CenterVertically
        ) {
            Row(verticalAlignment = Alignment.CenterVertically) {
                Box(
                    modifier = Modifier
                        .width(4.dp)
                        .height(18.dp)
                        .clip(RoundedCornerShape(2.dp))
                        .background(TechCyan)
                )
                Spacer(modifier = Modifier.width(8.dp))
                Text(
                    text = title,
                    style = MaterialTheme.typography.titleMedium,
                    fontWeight = FontWeight.Bold,
                    color = MaterialTheme.colorScheme.onBackground
                )
            }

            if (actionText != null && onActionClick != null) {
                TextButton(
                    onClick = onActionClick,
                    contentPadding = PaddingValues(horizontal = 8.dp, vertical = 0.dp)
                ) {
                    Text(
                        text = actionText,
                        style = MaterialTheme.typography.labelMedium,
                        fontWeight = FontWeight.SemiBold,
                        color = TechCyan
                    )
                    Spacer(modifier = Modifier.width(4.dp))
                    Icon(
                        imageVector = Icons.Default.ChevronRight,
                        contentDescription = null,
                        modifier = Modifier.size(16.dp),
                        tint = TechCyan
                    )
                }
            }
        }
        if (subtitle != null) {
            Spacer(modifier = Modifier.height(2.dp))
            Text(
                text = subtitle,
                style = MaterialTheme.typography.bodySmall,
                color = MaterialTheme.colorScheme.onSurfaceVariant,
                modifier = Modifier.padding(start = 12.dp)
            )
        }
    }
}

@Composable
fun WhatsAppActionButton(
    customMessage: String,
    modifier: Modifier = Modifier,
    label: String = "Chat on WhatsApp"
) {
    val context = LocalContext.current
    Button(
        onClick = { ContactHelper.openWhatsApp(context, customMessage) },
        colors = ButtonDefaults.buttonColors(
            containerColor = TechGreen,
            contentColor = Color.White
        ),
        shape = RoundedCornerShape(10.dp),
        modifier = modifier
    ) {
        Icon(
            imageVector = Icons.Default.Chat,
            contentDescription = "WhatsApp",
            modifier = Modifier.size(18.dp)
        )
        Spacer(modifier = Modifier.width(8.dp))
        Text(text = label, fontWeight = FontWeight.SemiBold)
    }
}

@Composable
fun QuickEnquiryFloatingBar(
    onQuoteClick: () -> Unit,
    onCourseEnquiryClick: () -> Unit
) {
    Surface(
        color = MaterialTheme.colorScheme.surface,
        tonalElevation = 8.dp,
        shadowElevation = 8.dp,
        shape = RoundedCornerShape(topStart = 16.dp, topEnd = 16.dp),
        modifier = Modifier.fillMaxWidth()
    ) {
        Row(
            modifier = Modifier
                .fillMaxWidth()
                .navigationBarsPadding()
                .padding(horizontal = 16.dp, vertical = 10.dp),
            horizontalArrangement = Arrangement.spacedBy(12.dp)
        ) {
            OutlinedButton(
                onClick = onCourseEnquiryClick,
                shape = RoundedCornerShape(10.dp),
                modifier = Modifier.weight(1f),
                colors = ButtonDefaults.outlinedButtonColors(
                    contentColor = TechCyan
                )
            ) {
                Icon(
                    imageVector = Icons.Outlined.School,
                    contentDescription = null,
                    modifier = Modifier.size(18.dp)
                )
                Spacer(modifier = Modifier.width(6.dp))
                Text("Course Enquiry", maxLines = 1)
            }

            Button(
                onClick = onQuoteClick,
                shape = RoundedCornerShape(10.dp),
                modifier = Modifier.weight(1f),
                colors = ButtonDefaults.buttonColors(
                    containerColor = TechCyan,
                    contentColor = TechMidnight
                )
            ) {
                Icon(
                    imageVector = Icons.Default.RequestQuote,
                    contentDescription = null,
                    modifier = Modifier.size(18.dp)
                )
                Spacer(modifier = Modifier.width(6.dp))
                Text("Get a Quote", fontWeight = FontWeight.Bold, maxLines = 1)
            }
        }
    }
}

@Composable
fun TrustBadge(
    icon: ImageVector,
    title: String,
    subtitle: String,
    modifier: Modifier = Modifier
) {
    Surface(
        color = MaterialTheme.colorScheme.surfaceVariant.copy(alpha = 0.7f),
        shape = RoundedCornerShape(12.dp),
        modifier = modifier
    ) {
        Row(
            modifier = Modifier.padding(horizontal = 12.dp, vertical = 10.dp),
            verticalAlignment = Alignment.CenterVertically
        ) {
            Box(
                modifier = Modifier
                    .size(36.dp)
                    .clip(CircleShape)
                    .background(TechCyan.copy(alpha = 0.15f)),
                contentAlignment = Alignment.Center
            ) {
                Icon(
                    imageVector = icon,
                    contentDescription = null,
                    tint = TechCyan,
                    modifier = Modifier.size(20.dp)
                )
            }
            Spacer(modifier = Modifier.width(10.dp))
            Column {
                Text(
                    text = title,
                    style = MaterialTheme.typography.labelLarge,
                    fontWeight = FontWeight.Bold,
                    color = MaterialTheme.colorScheme.onSurface
                )
                Text(
                    text = subtitle,
                    style = MaterialTheme.typography.bodySmall,
                    fontSize = 11.sp,
                    color = MaterialTheme.colorScheme.onSurfaceVariant
                )
            }
        }
    }
}

@Composable
fun RatingStars(rating: Float, modifier: Modifier = Modifier) {
    Row(verticalAlignment = Alignment.CenterVertically, modifier = modifier) {
        repeat(5) { index ->
            val icon = if (index < rating.toInt()) Icons.Filled.Star else Icons.Outlined.Star
            Icon(
                imageVector = icon,
                contentDescription = null,
                tint = TechGold,
                modifier = Modifier.size(14.dp)
            )
        }
        Spacer(modifier = Modifier.width(4.dp))
        Text(
            text = String.format("%.1f", rating),
            fontSize = 12.sp,
            fontWeight = FontWeight.Bold,
            color = MaterialTheme.colorScheme.onSurface
        )
    }
}
