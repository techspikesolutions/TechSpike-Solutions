package com.example

import android.os.Bundle
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import androidx.activity.enableEdgeToEdge
import androidx.activity.viewModels
import androidx.compose.animation.AnimatedContent
import androidx.compose.animation.core.tween
import androidx.compose.animation.fadeIn
import androidx.compose.animation.fadeOut
import androidx.compose.animation.togetherWith
import androidx.compose.foundation.background
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.*
import androidx.compose.material.icons.outlined.*
import androidx.compose.material3.*
import androidx.compose.runtime.*
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.vector.ImageVector
import androidx.compose.ui.platform.testTag
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.navigation.NavGraph.Companion.findStartDestination
import androidx.navigation.NavHostController
import androidx.navigation.NavType
import androidx.navigation.compose.*
import androidx.navigation.navArgument
import com.example.ui.components.*
import com.example.ui.screens.*
import com.example.ui.theme.TechCyan
import com.example.ui.theme.TechDarkBlue
import com.example.ui.theme.TechMidnight
import com.example.ui.theme.TechSpikeTheme
import com.example.ui.viewmodel.TechSpikeViewModel

sealed class Screen(val route: String, val title: String, val icon: ImageVector, val selectedIcon: ImageVector) {
    object Home : Screen("home", "Home", Icons.Outlined.Home, Icons.Filled.Home)
    object Services : Screen("services", "Services", Icons.Outlined.BusinessCenter, Icons.Filled.BusinessCenter)
    object Training : Screen("training", "Training", Icons.Outlined.School, Icons.Filled.School)
    object Blog : Screen("blog", "Insights", Icons.Outlined.Article, Icons.Filled.Article)
    object Contact : Screen("contact", "Contact", Icons.Outlined.PhoneInTalk, Icons.Filled.PhoneInTalk)
    object Profile : Screen("profile", "Portal", Icons.Outlined.Person, Icons.Filled.Person)

    // Sub-screens
    object ServiceDetail : Screen("service_detail/{serviceId}", "Service Detail", Icons.Default.Info, Icons.Default.Info) {
        fun createRoute(serviceId: String) = "service_detail/$serviceId"
    }
    object CourseDetail : Screen("course_detail/{courseId}", "Course Detail", Icons.Default.School, Icons.Default.School) {
        fun createRoute(courseId: String) = "course_detail/$courseId"
    }
    object BlogDetail : Screen("blog_detail/{blogId}", "Article", Icons.Filled.Article, Icons.Filled.Article) {
        fun createRoute(blogId: String) = "blog_detail/$blogId"
    }
    object Admin : Screen("admin", "Admin CRM", Icons.Default.AdminPanelSettings, Icons.Default.AdminPanelSettings)
}

class MainActivity : ComponentActivity() {

    private val viewModel: TechSpikeViewModel by viewModels()

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        enableEdgeToEdge()

        setContent {
            TechSpikeTheme {
                TechSpikeApp(viewModel = viewModel)
            }
        }
    }
}

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun TechSpikeApp(viewModel: TechSpikeViewModel) {
    val navController = rememberNavController()
    val navBackStackEntry by navController.currentBackStackEntryAsState()
    val currentRoute = navBackStackEntry?.destination?.route

    // ViewModel Flows
    val services = viewModel.services
    val courses by viewModel.courses.collectAsState()
    val blogPosts by viewModel.blogPosts.collectAsState()
    val testimonials = viewModel.testimonials
    val enquiries by viewModel.enquiries.collectAsState()
    val notifications by viewModel.notifications.collectAsState()
    val currentUser by viewModel.currentUser.collectAsState()
    val chatMessages by viewModel.chatMessages.collectAsState()
    val isChatLoading by viewModel.isChatLoading.collectAsState()
    val snackbarState by viewModel.snackbarState.collectAsState()

    val snackbarHostState = remember { SnackbarHostState() }

    LaunchedEffect(snackbarState) {
        if (snackbarState.isVisible && snackbarState.message.isNotBlank()) {
            snackbarHostState.showSnackbar(snackbarState.message)
            viewModel.dismissToast()
        }
    }

    // Interactive Dialog States
    var showQuoteDialog by remember { mutableStateOf(false) }
    var quoteInitialService by remember { mutableStateOf("Website Development") }

    var showCourseDialog by remember { mutableStateOf(false) }
    var courseInitialName by remember { mutableStateOf("Web Design & Web Development") }

    var showCareerCounsellingDialog by remember { mutableStateOf(false) }
    var showNotificationSheet by remember { mutableStateOf(false) }
    var showSpikeAiSheet by remember { mutableStateOf(false) }

    val unreadNotifs = notifications.count { !it.isRead }

    val bottomNavItems = listOf(
        Screen.Home,
        Screen.Services,
        Screen.Training,
        Screen.Blog,
        Screen.Profile
    )

    val isTopLevelRoute = currentRoute in bottomNavItems.map { it.route }

    var showSplashScreen by remember { mutableStateOf(true) }

    AnimatedContent(
        targetState = showSplashScreen,
        transitionSpec = {
            fadeIn(animationSpec = tween(400)) togetherWith fadeOut(animationSpec = tween(400))
        },
        label = "SplashAnimation"
    ) { isSplash ->
        if (isSplash) {
            SplashScreen(
                onSplashComplete = {
                    showSplashScreen = false
                }
            )
        } else {
            Scaffold(
        snackbarHost = { SnackbarHost(hostState = snackbarHostState) },
        topBar = {
            if (currentRoute != Screen.Admin.route) {
                TechSpikeTopBar(
                    unreadCount = unreadNotifs,
                    onNotificationsClick = { showNotificationSheet = true },
                    onSpikeAiClick = { showSpikeAiSheet = true },
                    onSearchClick = {
                        if (currentRoute != Screen.Services.route && currentRoute != Screen.Training.route) {
                            navController.navigate(Screen.Services.route)
                        }
                    },
                    onBackClick = if (!isTopLevelRoute) {
                        { navController.popBackStack() }
                    } else null
                )
            }
        },
        bottomBar = {
            if (isTopLevelRoute) {
                NavigationBar(
                    containerColor = MaterialTheme.colorScheme.surface,
                    tonalElevation = 4.dp
                ) {
                    bottomNavItems.forEach { screen ->
                        val selected = currentRoute == screen.route
                        NavigationBarItem(
                            icon = {
                                Icon(
                                    imageVector = if (selected) screen.selectedIcon else screen.icon,
                                    contentDescription = screen.title
                                )
                            },
                            label = { Text(screen.title, fontSize = 11.sp) },
                            selected = selected,
                            colors = NavigationBarItemDefaults.colors(
                                selectedIconColor = TechMidnight,
                                indicatorColor = TechCyan,
                                unselectedIconColor = MaterialTheme.colorScheme.onSurfaceVariant
                            ),
                            onClick = {
                                if (currentRoute != screen.route) {
                                    navController.navigate(screen.route) {
                                        popUpTo(navController.graph.findStartDestination().id) {
                                            saveState = true
                                        }
                                        launchSingleTop = true
                                        restoreState = true
                                    }
                                }
                            }
                        )
                    }
                }
            }
        },
        floatingActionButton = {
            if (isTopLevelRoute && currentRoute != Screen.Contact.route) {
                ExtendedFloatingActionButton(
                    onClick = {
                        quoteInitialService = "IT Consultation"
                        showQuoteDialog = true
                    },
                    containerColor = TechCyan,
                    contentColor = TechMidnight,
                    shape = RoundedCornerShape(16.dp),
                    modifier = Modifier.testTag("fab_enquire_now")
                ) {
                    Icon(Icons.Default.SupportAgent, contentDescription = null, modifier = Modifier.size(20.dp))
                    Spacer(modifier = Modifier.width(8.dp))
                    Text("Enquire Now", fontWeight = FontWeight.Bold)
                }
            }
        }
    ) { innerPadding ->
        NavHost(
            navController = navController,
            startDestination = Screen.Home.route,
            modifier = Modifier
                .fillMaxSize()
                .padding(innerPadding)
        ) {
            // Home Destination
            composable(Screen.Home.route) {
                HomeScreen(
                    services = services,
                    courses = courses,
                    blogPosts = blogPosts,
                    testimonials = testimonials,
                    onServiceClick = { serviceId ->
                        navController.navigate(Screen.ServiceDetail.createRoute(serviceId))
                    },
                    onCourseClick = { courseId ->
                        navController.navigate(Screen.CourseDetail.createRoute(courseId))
                    },
                    onBlogClick = { blogId ->
                        navController.navigate(Screen.BlogDetail.createRoute(blogId))
                    },
                    onNavigateServices = { navController.navigate(Screen.Services.route) },
                    onNavigateTraining = { navController.navigate(Screen.Training.route) },
                    onNavigateContact = { navController.navigate(Screen.Contact.route) },
                    onRequestQuote = {
                        quoteInitialService = "Website Development"
                        showQuoteDialog = true
                    },
                    onCourseEnquiry = {
                        courseInitialName = "Web Design & Web Development"
                        showCourseDialog = true
                    },
                    onCareerCounselling = {
                        showCareerCounsellingDialog = true
                    },
                    onSpikeAiClick = { showSpikeAiSheet = true }
                )
            }

            // Services Directory
            composable(Screen.Services.route) {
                ServicesScreen(
                    services = services,
                    onServiceClick = { serviceId ->
                        navController.navigate(Screen.ServiceDetail.createRoute(serviceId))
                    },
                    onRequestQuote = { serviceTitle ->
                        quoteInitialService = serviceTitle
                        showQuoteDialog = true
                    }
                )
            }

            // Service Deep-Dive
            composable(
                route = Screen.ServiceDetail.route,
                arguments = listOf(navArgument("serviceId") { type = NavType.StringType })
            ) { backStackEntry ->
                val serviceId = backStackEntry.arguments?.getString("serviceId")
                val service = services.find { it.id == serviceId } ?: services.first()

                ServiceDetailScreen(
                    service = service,
                    onBackClick = { navController.popBackStack() },
                    onRequestQuote = { sTitle ->
                        quoteInitialService = sTitle
                        showQuoteDialog = true
                    }
                )
            }

            // Training Catalogue
            composable(Screen.Training.route) {
                TrainingScreen(
                    courses = courses,
                    onCourseClick = { courseId ->
                        navController.navigate(Screen.CourseDetail.createRoute(courseId))
                    },
                    onCourseEnquiry = { courseTitle ->
                        courseInitialName = courseTitle
                        showCourseDialog = true
                    },
                    onCareerCounselling = {
                        showCareerCounsellingDialog = true
                    }
                )
            }

            // Course Deep-Dive
            composable(
                route = Screen.CourseDetail.route,
                arguments = listOf(navArgument("courseId") { type = NavType.StringType })
            ) { backStackEntry ->
                val courseId = backStackEntry.arguments?.getString("courseId")
                val course = courses.find { it.id == courseId } ?: courses.first()

                CourseDetailScreen(
                    course = course,
                    onBackClick = { navController.popBackStack() },
                    onEnquireClick = { cTitle ->
                        courseInitialName = cTitle
                        showCourseDialog = true
                    }
                )
            }

            // TechSpike Insights (Blog)
            composable(Screen.Blog.route) {
                BlogScreen(
                    blogPosts = blogPosts,
                    onBlogClick = { blogId ->
                        navController.navigate(Screen.BlogDetail.createRoute(blogId))
                    }
                )
            }

            // Blog Article Reader
            composable(
                route = Screen.BlogDetail.route,
                arguments = listOf(navArgument("blogId") { type = NavType.StringType })
            ) { backStackEntry ->
                val blogId = backStackEntry.arguments?.getString("blogId")
                val blog = blogPosts.find { it.id == blogId } ?: blogPosts.first()

                BlogDetailScreen(
                    blog = blog,
                    onBackClick = { navController.popBackStack() }
                )
            }

            // Contact Screen
            composable(Screen.Contact.route) {
                ContactScreen(
                    onSubmitMessage = { name, email, phone, subject, msg ->
                        viewModel.submitLead(
                            type = "CONTACT_GENERAL",
                            name = name,
                            email = email,
                            phone = phone,
                            serviceOrCourse = subject,
                            message = msg
                        )
                    }
                )
            }

            // Profile / User Portal
            composable(Screen.Profile.route) {
                UserPortalScreen(
                    user = currentUser,
                    enquiries = enquiries,
                    onRoleChange = { role -> viewModel.switchUserRole(role) },
                    onOpenAdminDashboard = { navController.navigate(Screen.Admin.route) },
                    onRequestQuote = {
                        quoteInitialService = "Custom IT Solution"
                        showQuoteDialog = true
                    },
                    onCourseEnquiry = {
                        courseInitialName = "Web Development"
                        showCourseDialog = true
                    },
                    onViewSplashScreen = {
                        showSplashScreen = true
                    }
                )
            }

            // Admin Portal
            composable(Screen.Admin.route) {
                AdminScreen(
                    enquiries = enquiries,
                    courses = courses,
                    blogPosts = blogPosts,
                    onUpdateEnquiryStatus = { id, status, notes ->
                        viewModel.updateLeadStatus(id, status, notes)
                    },
                    onDeleteEnquiry = { id -> viewModel.deleteLead(id) },
                    onAddCourse = { title, cat, duration, mode, overview ->
                        viewModel.addCustomCourse(title, cat, duration, mode, overview)
                    },
                    onDeleteCourse = { id -> viewModel.deleteCustomCourse(id) },
                    onAddBlog = { title, cat, summary, content, author ->
                        viewModel.addCustomBlog(title, cat, summary, content, author)
                    },
                    onDeleteBlog = { id -> viewModel.deleteCustomBlog(id) },
                    onSendNotification = { title, msg, type ->
                        viewModel.sendAdminNotification(title, msg, type)
                    },
                    onBackClick = { navController.popBackStack() }
                )
            }
        }
    }

    // Modal Dialogs
    if (showQuoteDialog) {
        RequestQuoteDialog(
            initialService = quoteInitialService,
            onDismiss = { showQuoteDialog = false },
            onSubmit = { name, email, phone, service, company, budget, desc ->
                viewModel.submitLead(
                    type = "SERVICE_QUOTE",
                    name = name,
                    email = email,
                    phone = phone,
                    serviceOrCourse = service,
                    companyName = company,
                    budgetRange = budget,
                    message = desc
                )
            }
        )
    }

    if (showCourseDialog) {
        CourseEnquiryDialog(
            initialCourse = courseInitialName,
            onDismiss = { showCourseDialog = false },
            onSubmit = { name, email, phone, course, mode, batch, msg ->
                viewModel.submitLead(
                    type = "COURSE_ENQUIRY",
                    name = name,
                    email = email,
                    phone = phone,
                    serviceOrCourse = course,
                    preferredMode = mode,
                    preferredBatch = batch,
                    message = msg
                )
            }
        )
    }

    if (showCareerCounsellingDialog) {
        CareerCounsellingDialog(
            onDismiss = { showCareerCounsellingDialog = false },
            onSubmit = { name, phone, edu, interest ->
                viewModel.submitLead(
                    type = "CAREER_COUNSELLING",
                    name = name,
                    email = "",
                    phone = phone,
                    serviceOrCourse = "Career Counselling ($interest)",
                    message = "Education Background: $edu"
                )
            }
        )
    }

    if (showNotificationSheet) {
        NotificationSheet(
            notifications = notifications,
            onDismiss = { showNotificationSheet = false },
            onMarkAllRead = { viewModel.markAllNotificationsRead() },
            onItemClick = { notif -> viewModel.markNotificationRead(notif.id) }
        )
    }

            if (showSpikeAiSheet) {
                SpikeAiSheet(
                    messages = chatMessages,
                    isLoading = isChatLoading,
                    onSendMessage = { query -> viewModel.sendChatMessage(query) },
                    onDismiss = { showSpikeAiSheet = false },
                    onRequestQuote = {
                        showSpikeAiSheet = false
                        showQuoteDialog = true
                    },
                    onCourseEnquiry = {
                        showSpikeAiSheet = false
                        showCourseDialog = true
                    }
                )
            }
        }
    }
}
