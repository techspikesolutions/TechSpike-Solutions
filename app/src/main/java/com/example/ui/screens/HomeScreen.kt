package com.example.ui.screens

import androidx.compose.foundation.BorderStroke
import androidx.compose.foundation.Image
import androidx.compose.foundation.background
import androidx.compose.foundation.border
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.LazyRow
import androidx.compose.foundation.lazy.items
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.automirrored.filled.ArrowForward
import androidx.compose.material.icons.filled.*
import androidx.compose.material.icons.outlined.*
import androidx.compose.material3.*
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.Brush
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.layout.ContentScale
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.style.TextOverflow
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.example.R
import com.example.data.model.BlogPost
import com.example.data.model.CourseItem
import com.example.data.model.ServiceItem
import com.example.data.model.Testimonial
import com.example.ui.components.*
import com.example.ui.theme.*

@Composable
fun HomeScreen(
    services: List<ServiceItem>,
    courses: List<CourseItem>,
    blogPosts: List<BlogPost>,
    testimonials: List<Testimonial>,
    onServiceClick: (String) -> Unit,
    onCourseClick: (String) -> Unit,
    onBlogClick: (String) -> Unit,
    onNavigateServices: () -> Unit,
    onNavigateTraining: () -> Unit,
    onNavigateContact: () -> Unit,
    onRequestQuote: () -> Unit,
    onCourseEnquiry: () -> Unit,
    onCareerCounselling: () -> Unit,
    onSpikeAiClick: () -> Unit
) {
    val context = LocalContext.current

    Box(modifier = Modifier.fillMaxSize()) {
        CircuitBackground()

        LazyColumn(
            modifier = Modifier.fillMaxSize(),
            contentPadding = PaddingValues(bottom = 100.dp)
        ) {
            // Hero Section
            item {
                HomeHeroSection(
                    onRequestQuote = onRequestQuote,
                    onNavigateServices = onNavigateServices,
                    onNavigateTraining = onNavigateTraining,
                    onNavigateContact = onNavigateContact,
                    onSpikeAiClick = onSpikeAiClick
                )
            }

            // Trust Badges row
            item {
                Row(
                    modifier = Modifier
                        .fillMaxWidth()
                        .padding(horizontal = 16.dp, vertical = 8.dp),
                    horizontalArrangement = Arrangement.spacedBy(8.dp)
                ) {
                    TrustBadge(
                        icon = Icons.Default.VerifiedUser,
                        title = "Enterprise Grade",
                        subtitle = "Quality Assured",
                        modifier = Modifier.weight(1f)
                    )
                    TrustBadge(
                        icon = Icons.Default.Public,
                        title = "India & Dubai",
                        subtitle = "Global Delivery",
                        modifier = Modifier.weight(1f)
                    )
                }
            }

            // Our Services Section
            item {
                SectionHeading(
                    title = "Our IT & Digital Services",
                    subtitle = "Enterprise technology engineered for real business growth",
                    actionText = "View All Services",
                    onActionClick = onNavigateServices
                )
            }

            items(services.take(5)) { service ->
                HomeServiceCard(
                    service = service,
                    onClick = { onServiceClick(service.id) },
                    onQuoteClick = onRequestQuote
                )
            }

            // Why Choose TechSpike Solutions
            item {
                WhyChooseTechSpikeSection()
            }

            // Training Institute Promotion & Popular Courses
            item {
                TrainingBannerSection(
                    onExploreClick = onNavigateTraining,
                    onCounsellingClick = onCareerCounselling
                )
            }

            item {
                SectionHeading(
                    title = "Popular Training Courses",
                    subtitle = "Learn practical, industry-focused technology skills in Ludhiana & Online",
                    actionText = "Explore All Courses",
                    onActionClick = onNavigateTraining
                )
            }

            item {
                LazyRow(
                    contentPadding = PaddingValues(horizontal = 16.dp),
                    horizontalArrangement = Arrangement.spacedBy(14.dp)
                ) {
                    items(courses) { course ->
                        HomeCourseCard(
                            course = course,
                            onClick = { onCourseClick(course.id) },
                            onEnquireClick = onCourseEnquiry
                        )
                    }
                }
            }

            // Testimonials Carousel
            item {
                SectionHeading(
                    title = "What Our Clients & Students Say",
                    subtitle = "Real feedback from businesses and certified learners"
                )
            }

            item {
                LazyRow(
                    contentPadding = PaddingValues(horizontal = 16.dp),
                    horizontalArrangement = Arrangement.spacedBy(14.dp)
                ) {
                    items(testimonials) { testimonial ->
                        TestimonialCard(testimonial = testimonial)
                    }
                }
            }

            // Latest Articles Section
            item {
                SectionHeading(
                    title = "Latest Technology Articles",
                    subtitle = "Insights on cloud computing, digital marketing, and cybersecurity"
                )
            }

            items(blogPosts.take(2)) { blog ->
                HomeBlogCard(
                    blog = blog,
                    onClick = { onBlogClick(blog.id) }
                )
            }

            // Bottom CTA Card
            item {
                BottomCtaSection(
                    onRequestQuote = onRequestQuote,
                    onNavigateContact = onNavigateContact
                )
            }
        }
    }
}

@Composable
fun HomeHeroSection(
    onRequestQuote: () -> Unit,
    onNavigateServices: () -> Unit,
    onNavigateTraining: () -> Unit,
    onNavigateContact: () -> Unit,
    onSpikeAiClick: () -> Unit
) {
    Surface(
        modifier = Modifier
            .fillMaxWidth()
            .padding(16.dp),
        shape = RoundedCornerShape(24.dp),
        color = TechDarkBlue,
        tonalElevation = 4.dp
    ) {
        Column(
            modifier = Modifier
                .fillMaxWidth()
                .padding(20.dp)
        ) {
            // Top Badges Row: Company Identity + Free Consultation
            Row(
                verticalAlignment = Alignment.CenterVertically,
                horizontalArrangement = Arrangement.spacedBy(8.dp)
            ) {
                // Company badge
                Row(
                    verticalAlignment = Alignment.CenterVertically,
                    modifier = Modifier
                        .clip(RoundedCornerShape(20.dp))
                        .background(TechCyan.copy(alpha = 0.15f))
                        .padding(horizontal = 10.dp, vertical = 5.dp)
                ) {
                    Box(
                        modifier = Modifier.size(20.dp),
                        contentAlignment = Alignment.Center
                    ) {
                        Image(
                            painter = painterResource(id = R.drawable.ic_techspike_logo),
                            contentDescription = "TechSpike Logo",
                            modifier = Modifier.fillMaxSize(),
                            contentScale = ContentScale.Fit
                        )
                    }
                    Spacer(modifier = Modifier.width(6.dp))
                    Text(
                        text = "TECHSPIKE SOLUTIONS",
                        fontSize = 10.5.sp,
                        fontWeight = FontWeight.Bold,
                        color = TechCyan,
                        letterSpacing = 0.8.sp
                    )
                }

                // Free Consultation Badge
                Row(
                    verticalAlignment = Alignment.CenterVertically,
                    modifier = Modifier
                        .clip(RoundedCornerShape(20.dp))
                        .background(TechGreen.copy(alpha = 0.15f))
                        .border(1.dp, TechGreen.copy(alpha = 0.35f), RoundedCornerShape(20.dp))
                        .padding(horizontal = 9.dp, vertical = 5.dp)
                ) {
                    Icon(
                        Icons.Default.Verified,
                        contentDescription = null,
                        tint = TechGreen,
                        modifier = Modifier.size(12.dp)
                    )
                    Spacer(modifier = Modifier.width(4.dp))
                    Text(
                        text = "Free Consultation",
                        fontSize = 10.5.sp,
                        fontWeight = FontWeight.Bold,
                        color = TechGreen
                    )
                }
            }

            Spacer(modifier = Modifier.height(14.dp))

            Text(
                text = "Empowering Businesses\nThrough Technology",
                style = MaterialTheme.typography.headlineMedium,
                fontWeight = FontWeight.ExtraBold,
                color = Color.White,
                lineHeight = 32.sp
            )

            Spacer(modifier = Modifier.height(10.dp))

            Text(
                text = "Technology solutions, digital growth, and practical IT training. Claim your free consultation to kickstart your project today.",
                style = MaterialTheme.typography.bodyMedium,
                color = DarkTextSecondary,
                lineHeight = 22.sp
            )

            Spacer(modifier = Modifier.height(18.dp))

            // Hero Graphic Illustration
            Box(
                modifier = Modifier
                    .fillMaxWidth()
                    .height(160.dp)
                    .clip(RoundedCornerShape(16.dp))
            ) {
                Image(
                    painter = painterResource(id = R.drawable.img_hero_techspike),
                    contentDescription = "TechSpike Technology Banner",
                    contentScale = ContentScale.Crop,
                    modifier = Modifier.fillMaxSize()
                )
                Box(
                    modifier = Modifier
                        .fillMaxSize()
                        .background(
                            Brush.verticalGradient(
                                listOf(Color.Transparent, TechDarkBlue.copy(alpha = 0.8f))
                            )
                        )
                )
            }

            Spacer(modifier = Modifier.height(20.dp))

            // Primary buttons: Free Consultation & Contact Us
            Row(
                modifier = Modifier.fillMaxWidth(),
                horizontalArrangement = Arrangement.spacedBy(10.dp)
            ) {
                Button(
                    onClick = onRequestQuote,
                    colors = ButtonDefaults.buttonColors(
                        containerColor = TechCyan,
                        contentColor = TechMidnight
                    ),
                    shape = RoundedCornerShape(14.dp),
                    contentPadding = PaddingValues(horizontal = 8.dp, vertical = 0.dp),
                    elevation = ButtonDefaults.buttonElevation(defaultElevation = 3.dp),
                    modifier = Modifier
                        .weight(1.15f)
                        .height(50.dp)
                ) {
                    Icon(
                        Icons.Default.CalendarMonth,
                        contentDescription = null,
                        modifier = Modifier.size(17.dp)
                    )
                    Spacer(modifier = Modifier.width(5.dp))
                    Text(
                        text = "Free Consultation",
                        fontWeight = FontWeight.Bold,
                        fontSize = 12.5.sp,
                        maxLines = 1
                    )
                }

                OutlinedButton(
                    onClick = onNavigateContact,
                    shape = RoundedCornerShape(14.dp),
                    contentPadding = PaddingValues(horizontal = 8.dp, vertical = 0.dp),
                    border = BorderStroke(1.5.dp, TechCyan),
                    colors = ButtonDefaults.outlinedButtonColors(
                        contentColor = TechCyan
                    ),
                    modifier = Modifier
                        .weight(0.85f)
                        .height(50.dp)
                ) {
                    Icon(
                        Icons.Default.PhoneInTalk,
                        contentDescription = null,
                        modifier = Modifier.size(17.dp)
                    )
                    Spacer(modifier = Modifier.width(5.dp))
                    Text(
                        text = "Contact Us",
                        fontWeight = FontWeight.Bold,
                        fontSize = 12.5.sp,
                        maxLines = 1
                    )
                }
            }

            Spacer(modifier = Modifier.height(12.dp))

            // Dedicated, beautifully aligned Spike AI Assistant Card
            Surface(
                onClick = onSpikeAiClick,
                shape = RoundedCornerShape(14.dp),
                color = TechMidnight.copy(alpha = 0.65f),
                border = BorderStroke(1.dp, TechCyan.copy(alpha = 0.35f)),
                modifier = Modifier.fillMaxWidth()
            ) {
                Row(
                    modifier = Modifier
                        .fillMaxWidth()
                        .padding(horizontal = 14.dp, vertical = 10.dp),
                    verticalAlignment = Alignment.CenterVertically,
                    horizontalArrangement = Arrangement.SpaceBetween
                ) {
                    Row(
                        verticalAlignment = Alignment.CenterVertically,
                        modifier = Modifier.weight(1f)
                    ) {
                        Box(
                            modifier = Modifier
                                .size(34.dp)
                                .clip(CircleShape)
                                .background(TechCyan.copy(alpha = 0.18f))
                                .border(1.dp, TechCyan.copy(alpha = 0.3f), CircleShape),
                            contentAlignment = Alignment.Center
                        ) {
                            Icon(
                                Icons.Default.SmartToy,
                                contentDescription = null,
                                tint = TechCyan,
                                modifier = Modifier.size(19.dp)
                            )
                        }

                        Spacer(modifier = Modifier.width(10.dp))

                        Column {
                            Row(verticalAlignment = Alignment.CenterVertically) {
                                Text(
                                    text = "Ask Spike AI",
                                    fontWeight = FontWeight.Bold,
                                    fontSize = 13.sp,
                                    color = Color.White
                                )
                                Spacer(modifier = Modifier.width(6.dp))
                                Box(
                                    modifier = Modifier
                                        .clip(RoundedCornerShape(6.dp))
                                        .background(TechCyan.copy(alpha = 0.18f))
                                        .padding(horizontal = 5.dp, vertical = 1.5.dp)
                                ) {
                                    Text(
                                        text = "24/7 ASSISTANT",
                                        fontSize = 8.5.sp,
                                        fontWeight = FontWeight.Bold,
                                        color = TechCyan
                                    )
                                }
                            }
                            Text(
                                text = "Get instant answers on services, pricing & courses",
                                fontSize = 11.sp,
                                color = DarkTextSecondary
                            )
                        }
                    }

                    Icon(
                        Icons.AutoMirrored.Filled.ArrowForward,
                        contentDescription = "Open Spike AI",
                        tint = TechCyan,
                        modifier = Modifier.size(18.dp)
                    )
                }
            }

            Spacer(modifier = Modifier.height(10.dp))

            // Secondary Quick Links: Explore Services & Courses
            Row(
                modifier = Modifier.fillMaxWidth(),
                horizontalArrangement = Arrangement.Center,
                verticalAlignment = Alignment.CenterVertically
            ) {
                TextButton(onClick = onNavigateServices) {
                    Icon(
                        Icons.Default.BusinessCenter,
                        contentDescription = null,
                        modifier = Modifier.size(15.dp),
                        tint = TechCyan
                    )
                    Spacer(modifier = Modifier.width(5.dp))
                    Text(
                        text = "Explore Services",
                        color = TechCyan,
                        fontSize = 12.sp,
                        fontWeight = FontWeight.SemiBold
                    )
                }

                Box(
                    modifier = Modifier
                        .size(4.dp)
                        .clip(CircleShape)
                        .background(DarkTextSecondary.copy(alpha = 0.5f))
                )

                TextButton(onClick = onNavigateTraining) {
                    Icon(
                        Icons.Default.School,
                        contentDescription = null,
                        modifier = Modifier.size(15.dp),
                        tint = TechSky
                    )
                    Spacer(modifier = Modifier.width(5.dp))
                    Text(
                        text = "Courses & Training",
                        color = TechSky,
                        fontSize = 12.sp,
                        fontWeight = FontWeight.SemiBold
                    )
                }
            }
        }
    }
}

@Composable
fun HomeServiceCard(
    service: ServiceItem,
    onClick: () -> Unit,
    onQuoteClick: () -> Unit
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
        shape = RoundedCornerShape(16.dp),
        tonalElevation = 2.dp,
        modifier = Modifier
            .fillMaxWidth()
            .padding(horizontal = 16.dp, vertical = 6.dp)
            .clickable(onClick = onClick)
    ) {
        Column(modifier = Modifier.padding(16.dp)) {
            Row(
                modifier = Modifier.fillMaxWidth(),
                verticalAlignment = Alignment.CenterVertically
            ) {
                Box(
                    modifier = Modifier
                        .size(46.dp)
                        .clip(RoundedCornerShape(12.dp))
                        .background(TechCyan.copy(alpha = 0.15f)),
                    contentAlignment = Alignment.Center
                ) {
                    Icon(
                        imageVector = icon,
                        contentDescription = null,
                        tint = TechCyan,
                        modifier = Modifier.size(24.dp)
                    )
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
                        color = TechCyan
                    )
                }

                IconButton(onClick = onClick) {
                    Icon(
                        imageVector = Icons.Default.ArrowForward,
                        contentDescription = "Details",
                        tint = MaterialTheme.colorScheme.onSurfaceVariant
                    )
                }
            }

            Spacer(modifier = Modifier.height(10.dp))

            Text(
                text = service.shortDescription,
                style = MaterialTheme.typography.bodyMedium,
                color = MaterialTheme.colorScheme.onSurfaceVariant,
                lineHeight = 20.sp,
                maxLines = 2,
                overflow = TextOverflow.Ellipsis
            )

            Spacer(modifier = Modifier.height(14.dp))

            Row(
                modifier = Modifier.fillMaxWidth(),
                horizontalArrangement = Arrangement.SpaceBetween,
                verticalAlignment = Alignment.CenterVertically
            ) {
                TextButton(
                    onClick = onClick,
                    contentPadding = PaddingValues(0.dp)
                ) {
                    Text("Learn More", color = TechCyan, fontWeight = FontWeight.SemiBold)
                    Spacer(modifier = Modifier.width(4.dp))
                    Icon(Icons.Default.ChevronRight, null, modifier = Modifier.size(16.dp), tint = TechCyan)
                }

                FilledTonalButton(
                    onClick = onQuoteClick,
                    shape = RoundedCornerShape(8.dp),
                    contentPadding = PaddingValues(horizontal = 12.dp, vertical = 4.dp),
                    colors = ButtonDefaults.filledTonalButtonColors(
                        containerColor = TechCyan.copy(alpha = 0.2f),
                        contentColor = TechCyan
                    )
                ) {
                    Text("Quick Quote", fontSize = 12.sp, fontWeight = FontWeight.Bold)
                }
            }
        }
    }
}

@Composable
fun WhyChooseTechSpikeSection() {
    val reasons = listOf(
        Pair("Expert Engineering Team", "Certified engineers with enterprise IT and development pedigree."),
        Pair("100% Customer Satisfaction", "Proven track record delivering reliable business solutions."),
        Pair("Innovative Solutions", "Modern tech stacks, AI integration, and cutting-edge architectures."),
        Pair("Customized Scalable Services", "Solutions engineered specifically to match your operational goals."),
        Pair("Practical Training Labs", "Industry-ready curriculum taught on real-world production projects."),
        Pair("Long-Term SLA Support", "24/7 proactive monitoring, maintenance, and technical helpdesk.")
    )

    Column(
        modifier = Modifier
            .fillMaxWidth()
            .padding(16.dp)
    ) {
        SectionHeading(
            title = "Why Choose TechSpike Solutions",
            subtitle = "Excellence, reliability, and business impact built into every delivery"
        )

        Column(verticalArrangement = Arrangement.spacedBy(10.dp)) {
            reasons.forEach { (title, desc) ->
                Surface(
                    color = MaterialTheme.colorScheme.surfaceVariant.copy(alpha = 0.4f),
                    shape = RoundedCornerShape(12.dp),
                    modifier = Modifier.fillMaxWidth()
                ) {
                    Row(
                        modifier = Modifier.padding(14.dp),
                        verticalAlignment = Alignment.Top
                    ) {
                        Box(
                            modifier = Modifier
                                .size(28.dp)
                                .clip(CircleShape)
                                .background(TechCyan.copy(alpha = 0.2f)),
                            contentAlignment = Alignment.Center
                        ) {
                            Icon(
                                imageVector = Icons.Default.Check,
                                contentDescription = null,
                                tint = TechCyan,
                                modifier = Modifier.size(16.dp)
                            )
                        }

                        Spacer(modifier = Modifier.width(12.dp))

                        Column {
                            Text(
                                text = title,
                                style = MaterialTheme.typography.titleSmall,
                                fontWeight = FontWeight.Bold,
                                color = MaterialTheme.colorScheme.onSurface
                            )
                            Spacer(modifier = Modifier.height(2.dp))
                            Text(
                                text = desc,
                                style = MaterialTheme.typography.bodySmall,
                                color = MaterialTheme.colorScheme.onSurfaceVariant
                            )
                        }
                    }
                }
            }
        }
    }
}

@Composable
fun TrainingBannerSection(
    onExploreClick: () -> Unit,
    onCounsellingClick: () -> Unit
) {
    Surface(
        color = TechNavy,
        shape = RoundedCornerShape(20.dp),
        modifier = Modifier
            .fillMaxWidth()
            .padding(16.dp)
    ) {
        Column(modifier = Modifier.padding(18.dp)) {
            Row(
                verticalAlignment = Alignment.CenterVertically,
                modifier = Modifier
                    .clip(RoundedCornerShape(16.dp))
                    .background(TechSky.copy(alpha = 0.2f))
                    .padding(horizontal = 10.dp, vertical = 4.dp)
            ) {
                Icon(Icons.Default.School, null, tint = TechSky, modifier = Modifier.size(14.dp))
                Spacer(modifier = Modifier.width(6.dp))
                Text(
                    "TECHSPIKE TRAINING INSTITUTE",
                    fontSize = 10.sp,
                    fontWeight = FontWeight.Bold,
                    color = TechSky
                )
            }

            Spacer(modifier = Modifier.height(12.dp))

            Text(
                text = "Learn Practical, Industry-Focused\nTechnology Skills",
                style = MaterialTheme.typography.titleLarge,
                fontWeight = FontWeight.Bold,
                color = Color.White
            )

            Spacer(modifier = Modifier.height(8.dp))

            Text(
                text = "Hands-on coding labs, certified instructors, live capstone projects, and placement assistance at our Ludhiana campus and live online.",
                style = MaterialTheme.typography.bodySmall,
                color = DarkTextSecondary,
                lineHeight = 20.sp
            )

            Spacer(modifier = Modifier.height(14.dp))

            Box(
                modifier = Modifier
                    .fillMaxWidth()
                    .height(140.dp)
                    .clip(RoundedCornerShape(12.dp))
            ) {
                Image(
                    painter = painterResource(id = R.drawable.img_training_banner),
                    contentDescription = "Training Institute Banner",
                    contentScale = ContentScale.Crop,
                    modifier = Modifier.fillMaxSize()
                )
            }

            Spacer(modifier = Modifier.height(16.dp))

            Row(
                modifier = Modifier.fillMaxWidth(),
                horizontalArrangement = Arrangement.spacedBy(10.dp)
            ) {
                Button(
                    onClick = onCounsellingClick,
                    shape = RoundedCornerShape(10.dp),
                    colors = ButtonDefaults.buttonColors(
                        containerColor = TechCyan,
                        contentColor = TechMidnight
                    ),
                    modifier = Modifier.weight(1f)
                ) {
                    Text("Free Counselling", fontSize = 12.sp, fontWeight = FontWeight.Bold)
                }

                OutlinedButton(
                    onClick = onExploreClick,
                    shape = RoundedCornerShape(10.dp),
                    colors = ButtonDefaults.outlinedButtonColors(contentColor = Color.White),
                    modifier = Modifier.weight(1f)
                ) {
                    Text("View Batches", fontSize = 12.sp)
                }
            }
        }
    }
}

@Composable
fun HomeCourseCard(
    course: CourseItem,
    onClick: () -> Unit,
    onEnquireClick: () -> Unit
) {
    Surface(
        color = MaterialTheme.colorScheme.surface,
        shape = RoundedCornerShape(16.dp),
        tonalElevation = 3.dp,
        modifier = Modifier
            .width(260.dp)
            .clickable(onClick = onClick)
    ) {
        Column(modifier = Modifier.padding(16.dp)) {
            Row(
                modifier = Modifier.fillMaxWidth(),
                horizontalArrangement = Arrangement.SpaceBetween,
                verticalAlignment = Alignment.CenterVertically
            ) {
                Box(
                    modifier = Modifier
                        .clip(RoundedCornerShape(6.dp))
                        .background(TechCyan.copy(alpha = 0.15f))
                        .padding(horizontal = 8.dp, vertical = 3.dp)
                ) {
                    Text(
                        text = course.category,
                        fontSize = 11.sp,
                        fontWeight = FontWeight.Bold,
                        color = TechCyan
                    )
                }

                RatingStars(rating = course.rating)
            }

            Spacer(modifier = Modifier.height(10.dp))

            Text(
                text = course.title,
                style = MaterialTheme.typography.titleMedium,
                fontWeight = FontWeight.Bold,
                color = MaterialTheme.colorScheme.onSurface,
                maxLines = 2,
                overflow = TextOverflow.Ellipsis
            )

            Spacer(modifier = Modifier.height(6.dp))

            Row(verticalAlignment = Alignment.CenterVertically) {
                Icon(
                    imageVector = Icons.Outlined.Schedule,
                    contentDescription = null,
                    tint = MaterialTheme.colorScheme.onSurfaceVariant,
                    modifier = Modifier.size(14.dp)
                )
                Spacer(modifier = Modifier.width(4.dp))
                Text(
                    text = course.duration,
                    style = MaterialTheme.typography.bodySmall,
                    color = MaterialTheme.colorScheme.onSurfaceVariant
                )
            }

            Spacer(modifier = Modifier.height(4.dp))

            Row(verticalAlignment = Alignment.CenterVertically) {
                Icon(
                    imageVector = Icons.Outlined.Place,
                    contentDescription = null,
                    tint = MaterialTheme.colorScheme.onSurfaceVariant,
                    modifier = Modifier.size(14.dp)
                )
                Spacer(modifier = Modifier.width(4.dp))
                Text(
                    text = course.mode,
                    style = MaterialTheme.typography.bodySmall,
                    color = MaterialTheme.colorScheme.onSurfaceVariant,
                    maxLines = 1,
                    overflow = TextOverflow.Ellipsis
                )
            }

            Spacer(modifier = Modifier.height(14.dp))

            Button(
                onClick = onEnquireClick,
                shape = RoundedCornerShape(8.dp),
                colors = ButtonDefaults.buttonColors(
                    containerColor = TechDarkBlue,
                    contentColor = TechCyan
                ),
                modifier = Modifier.fillMaxWidth()
            ) {
                Text("Enquire Now", fontWeight = FontWeight.Bold, fontSize = 12.sp)
            }
        }
    }
}

@Composable
fun TestimonialCard(testimonial: Testimonial) {
    Surface(
        color = MaterialTheme.colorScheme.surfaceVariant.copy(alpha = 0.5f),
        shape = RoundedCornerShape(16.dp),
        modifier = Modifier.width(300.dp)
    ) {
        Column(modifier = Modifier.padding(16.dp)) {
            RatingStars(rating = testimonial.rating)
            Spacer(modifier = Modifier.height(10.dp))
            Text(
                text = "\"${testimonial.comment}\"",
                style = MaterialTheme.typography.bodyMedium,
                color = MaterialTheme.colorScheme.onSurface,
                lineHeight = 20.sp,
                maxLines = 4,
                overflow = TextOverflow.Ellipsis
            )
            Spacer(modifier = Modifier.height(12.dp))
            HorizontalDivider(color = MaterialTheme.colorScheme.outline.copy(alpha = 0.3f))
            Spacer(modifier = Modifier.height(8.dp))
            Text(
                text = testimonial.name,
                fontWeight = FontWeight.Bold,
                style = MaterialTheme.typography.labelLarge,
                color = TechCyan
            )
            Text(
                text = "${testimonial.role} • ${testimonial.organizationOrCourse}",
                style = MaterialTheme.typography.bodySmall,
                color = MaterialTheme.colorScheme.onSurfaceVariant,
                maxLines = 1,
                overflow = TextOverflow.Ellipsis
            )
        }
    }
}

@Composable
fun HomeBlogCard(blog: BlogPost, onClick: () -> Unit) {
    Surface(
        color = MaterialTheme.colorScheme.surface,
        shape = RoundedCornerShape(14.dp),
        tonalElevation = 2.dp,
        modifier = Modifier
            .fillMaxWidth()
            .padding(horizontal = 16.dp, vertical = 6.dp)
            .clickable(onClick = onClick)
    ) {
        Column(modifier = Modifier.padding(16.dp)) {
            Row(
                modifier = Modifier.fillMaxWidth(),
                horizontalArrangement = Arrangement.SpaceBetween
            ) {
                Text(
                    text = blog.category,
                    color = TechCyan,
                    fontSize = 11.sp,
                    fontWeight = FontWeight.Bold
                )
                Text(
                    text = blog.readTime,
                    color = MaterialTheme.colorScheme.onSurfaceVariant,
                    fontSize = 11.sp
                )
            }

            Spacer(modifier = Modifier.height(6.dp))

            Text(
                text = blog.title,
                style = MaterialTheme.typography.titleSmall,
                fontWeight = FontWeight.Bold,
                color = MaterialTheme.colorScheme.onSurface
            )

            Spacer(modifier = Modifier.height(4.dp))

            Text(
                text = blog.summary,
                style = MaterialTheme.typography.bodySmall,
                color = MaterialTheme.colorScheme.onSurfaceVariant,
                maxLines = 2,
                overflow = TextOverflow.Ellipsis
            )

            Spacer(modifier = Modifier.height(8.dp))

            Row(verticalAlignment = Alignment.CenterVertically) {
                Text(
                    text = "Read Article",
                    fontSize = 12.sp,
                    fontWeight = FontWeight.SemiBold,
                    color = TechCyan
                )
                Spacer(modifier = Modifier.width(4.dp))
                Icon(
                    imageVector = Icons.Default.ArrowForward,
                    contentDescription = null,
                    tint = TechCyan,
                    modifier = Modifier.size(14.dp)
                )
            }
        }
    }
}

@Composable
fun BottomCtaSection(
    onRequestQuote: () -> Unit,
    onNavigateContact: () -> Unit
) {
    Surface(
        color = TechDarkBlue,
        shape = RoundedCornerShape(20.dp),
        modifier = Modifier
            .fillMaxWidth()
            .padding(16.dp)
    ) {
        Column(
            modifier = Modifier.padding(20.dp),
            horizontalAlignment = Alignment.CenterHorizontally
        ) {
            Text(
                text = "Ready to Transform Your Business?",
                style = MaterialTheme.typography.titleLarge,
                fontWeight = FontWeight.Bold,
                color = Color.White
            )

            Spacer(modifier = Modifier.height(8.dp))

            Text(
                text = "Schedule a complimentary technical evaluation or discuss your web development, cloud, and IT infrastructure requirements.",
                style = MaterialTheme.typography.bodySmall,
                color = DarkTextSecondary,
                lineHeight = 20.sp
            )

            Spacer(modifier = Modifier.height(18.dp))

            Row(
                modifier = Modifier.fillMaxWidth(),
                horizontalArrangement = Arrangement.spacedBy(10.dp)
            ) {
                Button(
                    onClick = onRequestQuote,
                    colors = ButtonDefaults.buttonColors(
                        containerColor = TechCyan,
                        contentColor = TechMidnight
                    ),
                    shape = RoundedCornerShape(12.dp),
                    contentPadding = PaddingValues(horizontal = 8.dp, vertical = 0.dp),
                    elevation = ButtonDefaults.buttonElevation(defaultElevation = 2.dp),
                    modifier = Modifier
                        .weight(1.15f)
                        .height(48.dp)
                ) {
                    Icon(
                        Icons.Default.CalendarMonth,
                        contentDescription = null,
                        modifier = Modifier.size(16.dp)
                    )
                    Spacer(modifier = Modifier.width(5.dp))
                    Text(
                        text = "Free Consultation",
                        fontWeight = FontWeight.Bold,
                        fontSize = 12.5.sp,
                        maxLines = 1
                    )
                }

                OutlinedButton(
                    onClick = onNavigateContact,
                    shape = RoundedCornerShape(12.dp),
                    contentPadding = PaddingValues(horizontal = 8.dp, vertical = 0.dp),
                    border = BorderStroke(1.5.dp, TechCyan.copy(alpha = 0.8f)),
                    colors = ButtonDefaults.outlinedButtonColors(contentColor = Color.White),
                    modifier = Modifier
                        .weight(0.85f)
                        .height(48.dp)
                ) {
                    Icon(
                        Icons.Default.PhoneInTalk,
                        contentDescription = null,
                        modifier = Modifier.size(16.dp),
                        tint = TechCyan
                    )
                    Spacer(modifier = Modifier.width(5.dp))
                    Text(
                        text = "Contact Us",
                        fontWeight = FontWeight.Bold,
                        fontSize = 12.5.sp,
                        maxLines = 1
                    )
                }
            }
        }
    }
}
