package com.example.data.repository

import com.example.data.local.AppDatabase
import com.example.data.local.CustomBlogEntity
import com.example.data.local.CustomCourseEntity
import com.example.data.local.EnquiryEntity
import com.example.data.local.NotificationEntity
import com.example.data.model.*
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.combine
import kotlinx.coroutines.flow.flowOf

class TechSpikeRepository(private val database: AppDatabase) {

    // Enquiries
    val allEnquiries: Flow<List<EnquiryEntity>> = database.enquiryDao().getAllEnquiries()
    val enquiryCount: Flow<Int> = database.enquiryDao().getEnquiryCount()

    suspend fun submitEnquiry(enquiry: EnquiryEntity): Long {
        return database.enquiryDao().insertEnquiry(enquiry)
    }

    suspend fun updateEnquiryStatus(id: Long, status: String, notes: String) {
        database.enquiryDao().updateEnquiryStatus(id, status, notes)
    }

    suspend fun deleteEnquiry(id: Long) {
        database.enquiryDao().deleteEnquiry(id)
    }

    // Notifications
    val allNotifications: Flow<List<NotificationEntity>> = database.notificationDao().getAllNotifications()

    suspend fun sendNotification(title: String, message: String, type: String): Long {
        return database.notificationDao().insertNotification(
            NotificationEntity(
                title = title,
                message = message,
                type = type
            )
        )
    }

    suspend fun markNotificationAsRead(id: Long) {
        database.notificationDao().markAsRead(id)
    }

    suspend fun markAllNotificationsAsRead() {
        database.notificationDao().markAllAsRead()
    }

    // Custom Courses (Admin added)
    val customCourses: Flow<List<CustomCourseEntity>> = database.customContentDao().getAllCustomCourses()

    suspend fun addCustomCourse(course: CustomCourseEntity) {
        database.customContentDao().insertCourse(course)
    }

    suspend fun deleteCustomCourse(id: String) {
        database.customContentDao().deleteCourse(id)
    }

    // Custom Blogs (Admin added)
    val customBlogs: Flow<List<CustomBlogEntity>> = database.customContentDao().getAllCustomBlogs()

    suspend fun addCustomBlog(blog: CustomBlogEntity) {
        database.customContentDao().insertBlog(blog)
    }

    suspend fun deleteCustomBlog(id: String) {
        database.customContentDao().deleteBlog(id)
    }

    // Static Services from TechSpike Solutions
    fun getServices(): List<ServiceItem> = staticServices

    fun getServiceById(id: String): ServiceItem? {
        return staticServices.find { it.id == id }
    }

    // Unified Courses stream combining official catalogue + admin-added courses
    fun getAllCourses(): Flow<List<CourseItem>> {
        return customCourses.combine(flowOf(staticCourses)) { customList, staticList ->
            val customMapped = customList.map { custom ->
                CourseItem(
                    id = custom.id,
                    title = custom.title,
                    category = custom.category,
                    duration = custom.duration,
                    mode = custom.mode,
                    eligibility = "Open to All Learners & Professionals",
                    whoShouldJoin = "Anyone interested in modern tech skills",
                    overview = custom.overview,
                    curriculum = listOf(
                        CurriculumModule(1, "Fundamentals", listOf("Foundations", "Setup", "Key Concepts")),
                        CurriculumModule(2, "Advanced Practice", listOf("Hands-on Projects", "Industry Best Practices"))
                    ),
                    careerOpportunities = listOf("Developer", "Specialist", "Engineer"),
                    practicalProjects = listOf("Capstone Industry Project"),
                    certificateInfo = "TechSpike Certified Professional Certificate",
                    nextBatch = "Upcoming Batch Starting Soon",
                    isPopular = true
                )
            }
            customMapped + staticList
        }
    }

    // Unified Blog Posts combining official editorial + admin-added posts
    fun getAllBlogPosts(): Flow<List<BlogPost>> {
        return customBlogs.combine(flowOf(staticBlogs)) { customList, staticList ->
            val customMapped = customList.map { custom ->
                BlogPost(
                    id = custom.id,
                    title = custom.title,
                    summary = custom.summary,
                    content = custom.content,
                    category = custom.category,
                    author = custom.author,
                    date = custom.date,
                    readTime = "4 min read",
                    viewsCount = 42
                )
            }
            customMapped + staticList
        }
    }

    fun getTestimonials(): List<Testimonial> = staticTestimonials

    companion object {
        val staticServices = listOf(
            ServiceItem(
                id = "ai-solutions",
                title = "AI & Automation Solutions",
                shortDescription = "Generative AI integration, smart conversational bots, automated workflow pipelines, and custom machine learning.",
                fullDescription = "Accelerate business productivity and eliminate operational friction with custom enterprise Artificial Intelligence. TechSpike Solutions builds intelligent agents, automated workflow pipelines, smart chatbots, and predictive analytics that integrate seamlessly into your business stack.",
                category = "AI & Automation",
                problemsSolved = listOf(
                    "Hours lost every day on repetitive manual data entry and document sorting",
                    "Slow customer inquiry response times resulting in lost sales leads",
                    "Unstructured company documents yielding zero actionable data insights",
                    "High operational overhead required to scale customer service and lead follow-ups",
                    "Uncertainty around securely deploying AI without compromising proprietary company data"
                ),
                ourSolution = "We architect enterprise-grade AI integrations using private Retrieval-Augmented Generation (RAG), Gemini multimodal APIs, and Robotic Process Automation (RPA) tailored to your exact business rules.",
                features = listOf(
                    "Custom 24/7 AI Chatbots & Customer Engagement Virtual Assistants",
                    "Intelligent Workflow Automation & Robotic Process Automation (RPA)",
                    "Generative AI & Multimodal Model Integration (Gemini, Vision, Text)",
                    "Intelligent Document Processing (IDP) & Automated OCR Extraction",
                    "Private Enterprise RAG (Retrieval-Augmented Generation) on Company Files",
                    "Predictive Analytics, Trend Forecasting & Custom Machine Learning Models"
                ),
                benefits = listOf(
                    "Up to 70% reduction in repetitive manual administrative task hours",
                    "Instant, 24/7 lead qualification and automated customer response",
                    "100% data privacy and security with isolated enterprise access controls",
                    "Faster, data-backed operational decision making and forecasting"
                ),
                processSteps = listOf(
                    "1. AI Opportunity Assessment - Pinpointing high-ROI automation targets across your operations",
                    "2. Data & Model Architecture - Selecting optimal LLMs, embeddings, and secure system connectors",
                    "3. Rapid Prototype - Deploying a working proof-of-concept tailored to your company documents",
                    "4. Production Deployment - Secure integration with your CRM, website, and database APIs",
                    "5. Continuous Fine-Tuning - Evaluation, accuracy monitoring, and ongoing model optimization"
                ),
                faqs = listOf(
                    FaqItem("Can the AI be customized to our private company documents?", "Yes, absolutely. We use secure Retrieval-Augmented Generation (RAG) so the AI references your specific catalogs, pricing, policies, and documents without leaking data to public models."),
                    FaqItem("How quickly can an AI solution be deployed?", "Standard smart chatbots and automated workflows can be operational in 1 to 2 weeks, while customized enterprise model pipelines take 3 to 5 weeks."),
                    FaqItem("Will this integrate with our existing software and CRM?", "Yes, we build secure REST APIs, webhooks, and direct connectors for leading CRMs, ERPs, Google Workspace, and internal databases.")
                )
            ),
            ServiceItem(
                id = "web-development",
                title = "Web Development",
                shortDescription = "High-performance websites, custom web apps, and high-conversion e-commerce platforms.",
                fullDescription = "TechSpike Solutions delivers end-to-end web engineering that drives tangible business growth. From bespoke corporate portals to lightning-fast e-commerce stores, we engineer scalable, responsive, and SEO-optimized web experiences.",
                category = "Development",
                problemsSolved = listOf(
                    "Slow website loading speeds losing potential customers",
                    "Outdated design that fails to establish authority and trust",
                    "Incompatible mobile rendering across modern smartphones and tablets",
                    "High cart abandonment rates on non-optimized checkout flows",
                    "Lack of automated lead capture and CRM synchronization"
                ),
                ourSolution = "We architect modern digital experiences using industry-standard modern frameworks, server-side performance rendering, and conversion-centered UI/UX design.",
                features = listOf(
                    "Custom Website Development & CMS Customization",
                    "Enterprise Web Applications & Dashboards",
                    "E-commerce Stores with Multi-Currency & Secure Payments",
                    "Ultra-Responsive Mobile & Tablet Viewport Layouts",
                    "Core Web Vitals Optimization & Instant Page Loading",
                    "API Integration, Cloud Hosting & Continuous Maintenance"
                ),
                benefits = listOf(
                    "Up to 3x increase in qualified inbound business leads",
                    "Sub-second page speeds boosting Google search rankings",
                    "Scalable cloud architecture that grows with your business",
                    "Rock-solid security against vulnerabilities and attacks"
                ),
                processSteps = listOf(
                    "1. Discovery & Strategy - Analyzing your brand, competitors, and conversion goals",
                    "2. Wireframing & UI/UX - Crafting high-fidelity interactive user experiences",
                    "3. Modern Development - Clean, modular, well-tested code implementation",
                    "4. Rigorous QA & Security - Cross-browser testing and speed benchmarking",
                    "5. Deployment & Support - Cloud server rollout with 24/7 reliability"
                ),
                faqs = listOf(
                    FaqItem("How long does a custom website project take?", "Typically between 2 to 5 weeks depending on scope, custom features, and design revisions."),
                    FaqItem("Will my website be mobile-friendly and SEO-ready?", "Yes, 100%. Every single project from TechSpike adheres to modern responsive standards and technical SEO best practices."),
                    FaqItem("Do you offer post-launch support?", "Yes, we provide continuous maintenance, security updates, backups, and feature additions.")
                )
            ),
            ServiceItem(
                id = "seo-digital-marketing",
                title = "SEO & Digital Marketing",
                shortDescription = "Data-driven SEO, Google Ads PPC, Local Business Profile optimization, and social media growth.",
                fullDescription = "Maximize your online visibility and drive high-intent customer traffic. Our comprehensive digital marketing methodology blends technical SEO, localized Google Maps optimization, targeted PPC advertising, and brand storytelling.",
                category = "Marketing",
                problemsSolved = listOf(
                    "Zero visibility on Google page 1 for lucrative commercial keywords",
                    "Wasted budget on poorly targeted Google Ads with low ROAS",
                    "Invisible Google Business Profile in local Ludhiana and regional searches",
                    "Inconsistent social media presence with weak engagement metrics"
                ),
                ourSolution = "We run holistic search and paid campaigns focused on measurable business ROI, customer acquisition cost reduction, and organic authority building.",
                features = listOf(
                    "Comprehensive On-Page, Off-Page & Technical SEO Audits",
                    "Google Business Profile (GBP) & Local Map Pack Dominance",
                    "High-ROAS Google Ads (Search, Display, Shopping, Retargeting)",
                    "Social Media Marketing & Paid Campaigns (Meta, LinkedIn, Instagram)",
                    "High-Quality Content Marketing & Authority Link Building",
                    "Conversion Rate Optimization (CRO) & Transparent Analytics"
                ),
                benefits = listOf(
                    "Consistent influx of high-intent organic customer leads",
                    "Top 3 placement in local Google Map searches",
                    "Lower cost-per-lead through optimized bidding and copy",
                    "Clear, jargon-free monthly ROI performance reporting"
                ),
                processSteps = listOf(
                    "1. Deep Audit - Evaluating current search rankings, technical hurdles, and competitor gaps",
                    "2. Keyword & Strategy Blueprint - Targeting high-intent commercial terms",
                    "3. Technical & Content Optimization - Fixing indexation, metadata, speed, and schema",
                    "4. Campaign Launch & Scaling - Setting up PPC funnels and social promotions",
                    "5. Review & Growth - Weekly monitoring, A/B split testing, and monthly ROI reports"
                ),
                faqs = listOf(
                    FaqItem("How soon can we see SEO results?", "Technical improvements and local map optimizations often yield traffic boosts in 4–8 weeks; competitive organic keywords typically establish dominance over 3–6 months."),
                    FaqItem("Do you manage Google Ads budgets directly?", "Yes, we structure, monitor, and optimize your ad spend daily to ensure maximum returns.")
                )
            ),
            ServiceItem(
                id = "disaster-recovery",
                title = "Disaster Recovery",
                shortDescription = "Enterprise-grade data backup, business continuity planning, and instant ransomware recovery.",
                fullDescription = "Safeguard your mission-critical company assets from catastrophic data loss, hardware crashes, cyberattacks, and unplanned disruptions. We engineer fail-safe business continuity plans.",
                category = "Infrastructure",
                problemsSolved = listOf(
                    "Crippling ransomware attacks locking critical business data",
                    "Server hardware failure causing days of halted operations",
                    "Accidental file deletion with no historical version backup",
                    "Non-compliance with data protection and ISO compliance standards"
                ),
                ourSolution = "We deploy automated multi-tiered backup architectures combining on-premises snapshots and encrypted off-site cloud storage with guaranteed RTO/RPO SLAs.",
                features = listOf(
                    "Automated Real-Time & Incremental Cloud Backups",
                    "Encrypted Off-Site Storage with Immutable Snapshots",
                    "Disaster Recovery Planning (DRP) & Business Continuity (BCP)",
                    "Rapid System Recovery & Bare-Metal Server Restores",
                    "Automated Disaster Simulation Drills & Integrity Testing",
                    "Continuous Compliance Monitoring & Audit Documentation"
                ),
                benefits = listOf(
                    "Near-zero data loss with automated incremental snapshots",
                    "System recovery in minutes instead of costly days of downtime",
                    "Complete peace of mind knowing critical records are bulletproof",
                    "Guaranteed compliance with regulatory and ISO standards"
                ),
                processSteps = listOf(
                    "1. Vulnerability Assessment - Mapping all databases, files, and server dependencies",
                    "2. Architecture Design - Configuring hybrid cloud and local backup schedules",
                    "3. Deployment - Installing lightweight, secure backup agents with end-to-end AES-256 encryption",
                    "4. Drill Simulation - Testing mock failure recoveries to prove RTO timelines",
                    "5. 24/7 Monitoring - Automated alerts and verified restore readiness"
                ),
                faqs = listOf(
                    FaqItem("What is the difference between backup and disaster recovery?", "Backup is saving copies of files; disaster recovery is the orchestrated strategy to restore the entire operating environment (servers, networking, databases) back to full operation quickly."),
                    FaqItem("Can we recover from ransomware?", "Yes. Our immutable, isolated cloud backups cannot be encrypted by ransomware, allowing clean rollbacks.")
                )
            ),
            ServiceItem(
                id = "cloud-desktop",
                title = "Cloud Desktop",
                shortDescription = "Secure Virtual Desktop Infrastructure (VDI), DaaS, and high-performance remote work environments.",
                fullDescription = "Empower your workforce to operate securely from anywhere on any device. TechSpike Cloud Desktop delivers virtual workstations equipped with licensed tools, centralized management, and high-speed cloud infrastructure.",
                category = "Cloud",
                problemsSolved = listOf(
                    "Expensive upfront capital expenditure for high-end office PCs",
                    "Data theft risks when employees download sensitive files to personal laptops",
                    "Slow collaboration among distributed or remote branch offices",
                    "Cumbersome manual software installation and license auditing across machines"
                ),
                ourSolution = "We provision enterprise Virtual Desktop Infrastructure (VDI) and Desktop as a Service (DaaS) hosted in high-availability Tier-4 data centers.",
                features = listOf(
                    "Dedicated High-Performance Virtual Desktops (CPU & GPU options)",
                    "Centralized Data Storage & Zero Data Leakage Architecture",
                    "Multi-Factor Authentication (MFA) & Granular Role Permissions",
                    "Anywhere Access from Thin Clients, Laptops, Tablets, or Phones",
                    "Automatic Nightly Snapshots & Rapid Machine Reset",
                    "Fully Managed OS Patching, Antivirus, and Software Upgrades"
                ),
                benefits = listOf(
                    "Up to 45% reduction in IT hardware and maintenance overhead",
                    "100% corporate data remains secure in the cloud, never on local disks",
                    "Instant onboarding of new team members in under 10 minutes",
                    "Seamless remote and hybrid work support"
                ),
                processSteps = listOf(
                    "1. Requirement Analysis - Assessing software workloads and user compute needs",
                    "2. Image Template Creation - Packaging customized OS, tools, and security policies",
                    "3. Pilot Testing - Deploying test workstations with key team members",
                    "4. Organization Rollout - Seamless cutover with zero downtime",
                    "5. Ongoing SLA Management - Performance scaling and 24/7 user support"
                ),
                faqs = listOf(
                    FaqItem("Can employees run heavy accounting or design software?", "Yes, our cloud desktop instances can be scaled with dedicated vCPUs, RAM, and GPU accelerators as required."),
                    FaqItem("What happens if an employee loses their laptop?", "Company data is 100% safe. Desktops run in the cloud and cannot be accessed without MFA credentials.")
                )
            ),
            ServiceItem(
                id = "network-solutions",
                title = "Network Solutions",
                shortDescription = "Enterprise Wi-Fi 6, structured cabling, router/switch configuration, and network security firewalls.",
                fullDescription = "Build a bulletproof, high-speed foundation for your organization. From multi-story office structured cabling to high-density commercial Wi-Fi and perimeter firewall security, TechSpike provides end-to-end network engineering.",
                category = "Networking",
                problemsSolved = listOf(
                    "Persistent Wi-Fi dead spots, packet loss, and frustrating signal drops",
                    "Tangled, undocumented server rack cabling causing downtime",
                    "Unsecured guest Wi-Fi exposing internal corporate servers",
                    "Slow file transfers and internet bottlenecks during peak hours"
                ),
                ourSolution = "We design, install, configure, and maintain robust enterprise network architectures using carrier-grade hardware (Cisco, MikroTik, Ubiquiti, Fortinet).",
                features = listOf(
                    "Enterprise Structured Cabling & Server Rack Management",
                    "High-Density Wi-Fi 6 Mesh Deployments & Heatmap Planning",
                    "VLAN Segmentation & Quality of Service (QoS) Optimization",
                    "Next-Generation Firewall (NGFW) & VPN Gateway Installation",
                    "Bandwidth Throttling, Load Balancing & Failover Internet Routing",
                    "24/7 Network Monitoring & Automated Intrusion Detection"
                ),
                benefits = listOf(
                    "99.9% network reliability with automated dual-ISP failover",
                    "Crystal-clear video conferencing with no buffering or jitter",
                    "Isolated, secure guest networks protecting sensitive assets",
                    "Clean, professional server room architecture with cable labeling"
                ),
                processSteps = listOf(
                    "1. Physical Site Survey - RF spectrum analysis and floorplan heatmapping",
                    "2. Network Topology Blueprint - Detailed switch, router, and AP schematics",
                    "3. Certified Cable & Hardware Installation - Clean structured cabling",
                    "4. Security & Traffic Configuration - VLANs, ACLs, firewall rules, and QoS",
                    "5. Stress Testing & Certification - Speed benchmarks and formal documentation"
                ),
                faqs = listOf(
                    FaqItem("Do you support existing hardware?", "Yes, we can optimize and secure your existing switches and routers, or recommend scalable modern upgrades."),
                    FaqItem("Can we link multiple branch offices together?", "Yes, we configure secure Site-to-Site IPsec VPN tunnels to interconnect all office locations seamlessly.")
                )
            ),
            ServiceItem(
                id = "it-support-consulting",
                title = "IT Support & Consulting",
                shortDescription = "Comprehensive Managed IT Services (MSP), 24/7 helpdesk, CTO consulting, and infrastructure troubleshooting.",
                fullDescription = "Your outsourced, full-strength IT department. TechSpike Solutions takes total ownership of your technology ecosystem so your team can focus entirely on core business operations.",
                category = "Consulting",
                problemsSolved = listOf(
                    "Employee productivity halted by constant computer and printer glitches",
                    "Lack of an experienced in-house IT team to guide strategic decisions",
                    "No centralized ticketing system or SLA accountability",
                    "Vulnerability to security flaws due to unpatched systems"
                ),
                ourSolution = "We deliver proactive Managed IT Services with rapid response remote helpdesk, dedicated on-site technician dispatches, and executive technology roadmaps.",
                features = listOf(
                    "Rapid Response Remote Desktop Support & Dedicated Helpdesk",
                    "Proactive Server & Endpoint Health Monitoring 24/7",
                    "Automated Patch Management & Antivirus Fleet Administration",
                    "IT Asset Procurement & Vendor Contract Management",
                    "Strategic Virtual CTO Advisory & Cloud Migration Roadmaps",
                    "Periodic Technology Health Audits & Executive Summaries"
                ),
                benefits = listOf(
                    "Immediate resolution of day-to-day employee IT headaches",
                    "Zero overhead of maintaining costly full-time internal IT staff",
                    "Preventative maintenance that eliminates costly unexpected outages",
                    "Strategic technology alignment for accelerated business expansion"
                ),
                processSteps = listOf(
                    "1. Ecosystem Health Audit - Reviewing all PCs, servers, software, and licenses",
                    "2. SLA Onboarding - Setting up ticketing channels and support contact lines",
                    "3. Endpoint Management Agent Rollout - Centralized monitoring & telemetry",
                    "4. Daily Proactive Support - Immediate helpdesk ticket resolution",
                    "5. Strategic Quarterly Reviews - Aligning IT capacity with business goals"
                ),
                faqs = listOf(
                    FaqItem("How fast does your helpdesk respond?", "Our standard critical SLA response is under 15 minutes, with general inquiries handled within the hour."),
                    FaqItem("Do you offer on-site visits in Ludhiana & Punjab?", "Yes, we have mobile technician units available for urgent physical on-site visits.")
                )
            )
        )

        val staticCourses = listOf(
            CourseItem(
                id = "course-web-dev",
                title = "Web Design & Web Development",
                category = "Development",
                duration = "3 to 6 Months",
                mode = "Classroom Lab & Live Online",
                eligibility = "10+2, BCA, MCA, B.Tech, or anyone passionate about coding",
                whoShouldJoin = "College students, aspiring frontend/fullstack developers, and entrepreneurs looking to build digital products.",
                overview = "Master modern web development from ground up. Learn HTML5, CSS3, JavaScript (ES6+), React.js, backend fundamentals, REST APIs, Git, and cloud deployment with 100% practical lab assignments.",
                curriculum = listOf(
                    CurriculumModule(1, "UI/UX & Web Foundations", listOf("HTML5 Semantic Markup", "CSS3 Flexbox & Grid", "Modern UI Principles", "Responsive Web Design")),
                    CurriculumModule(2, "Core JavaScript & DOM", listOf("ES6+ Syntax & Features", "DOM Manipulation & Events", "Asynchronous JS & Fetch API", "Debugging & DevTools")),
                    CurriculumModule(3, "Frontend Framework (React)", listOf("Components & JSX", "Hooks & State Management", "Tailwind CSS & Component Libraries", "Routing & Single Page Apps")),
                    CurriculumModule(4, "Backend, Database & Deployment", listOf("Node.js & Express Basics", "RESTful API Integration", "MySQL / MongoDB Connectivity", "Git, GitHub & Vercel/Netlify Deployment"))
                ),
                careerOpportunities = listOf("Frontend Developer", "Full Stack Developer", "Web Designer", "Freelance Web Consultant"),
                practicalProjects = listOf("Corporate Business Website", "Dynamic E-Commerce Product Catalog", "Personal Portfolio with Live Hosting"),
                certificateInfo = "TechSpike Certified Web Developer Certification (ISO 9001:2015 Accredited)",
                nextBatch = "Starts 1st & 15th of every month",
                rating = 4.9f,
                enrolledStudents = 240,
                isPopular = true
            ),
            CourseItem(
                id = "course-digital-marketing",
                title = "Digital Marketing & SEO Mastery",
                category = "Marketing",
                duration = "3 Months",
                mode = "Classroom Lab & Live Online",
                eligibility = "Students, Graduates, Business Owners, Freelancers",
                whoShouldJoin = "Marketing students, job seekers, small business owners, and creators wanting to master high-paying growth skills.",
                overview = "A hands-on training program covering search engine optimization, Google Business Profile rankings, Google Ads PPC campaigns, Meta social advertising, content strategy, and client freelancing.",
                curriculum = listOf(
                    CurriculumModule(1, "Search Engine Optimization (SEO)", listOf("Keyword Research & Competitor Analysis", "On-Page SEO & Content Optimization", "Technical SEO & Schema Markup", "Backlink Building Strategies")),
                    CurriculumModule(2, "Local SEO & Google Business Profile", listOf("Google Maps Verification", "Local Citations & Reviews Strategy", "Local Pack Dominance")),
                    CurriculumModule(3, "Google Ads (PPC)", listOf("Search Ads & Quality Score", "Display & YouTube Advertising", "Bidding Strategies & Budget Control", "Conversion Tracking")),
                    CurriculumModule(4, "Social Media Marketing & Analytics", listOf("Meta Ads Manager (Facebook & Instagram)", "LinkedIn Growth Tactics", "Google Analytics 4 (GA4)", "Freelance Client Acquisition"))
                ),
                careerOpportunities = listOf("SEO Specialist", "Digital Marketing Executive", "PPC Campaign Manager", "Social Media Strategist"),
                practicalProjects = listOf("Live Google Ads Campaign Budget Run", "Complete Website SEO Audit & Optimization", "Local Business Ranking Blueprint"),
                certificateInfo = "TechSpike Digital Marketing Professional Certificate & Google Ads Prep",
                nextBatch = "Starts upcoming Monday",
                rating = 4.8f,
                enrolledStudents = 195,
                isPopular = true
            ),
            CourseItem(
                id = "course-networking",
                title = "Computer Networks & Hardware",
                category = "Networking",
                duration = "3 Months",
                mode = "Practical Hardware Lab & Online",
                eligibility = "Diploma, BCA, B.Tech, IT Enthusiasts",
                whoShouldJoin = "Students preparing for network administration roles, system engineers, and IT support technicians.",
                overview = "Get hands-on experience with real switches, routers, cabling, and network simulation software. Learn OSI/TCP-IP models, subnetting, VLANs, routing protocols, and enterprise troubleshooting.",
                curriculum = listOf(
                    CurriculumModule(1, "Networking Basics & Physical Layer", listOf("OSI & TCP/IP Protocol Suites", "Ethernet Cabling & Crimping Lab", "Network Topologies & Hardware Overview")),
                    CurriculumModule(2, "IP Addressing & Subnetting", listOf("IPv4 Classful & Classless Addressing", "VLSM Calculation & Practice", "IPv6 Essentials", "DHCP & DNS Architecture")),
                    CurriculumModule(3, "Routing & Switching Configuration", listOf("Cisco Packet Tracer Lab", "VLANs & Inter-VLAN Routing", "Static Routing & Dynamic OSPF", "Switch Security & Port Fast")),
                    CurriculumModule(4, "Network Security & Troubleshooting", listOf("Firewalls & Access Control Lists (ACLs)", "Wi-Fi Security Protocols", "Network Diagnostic Tools (Ping, Traceroute, Wireshark)"))
                ),
                careerOpportunities = listOf("Network Engineer", "IT Support Specialist", "System Administrator", "Infrastructure Technician"),
                practicalProjects = listOf("Multi-Branch Office Network Architecture", "Cisco Router & Switch Configuration Lab", "Wi-Fi Heatmap & Access Point Rollout"),
                certificateInfo = "TechSpike Certified Network Associate Certificate",
                nextBatch = "New batch admissions open",
                rating = 4.9f,
                enrolledStudents = 160,
                isPopular = false
            ),
            CourseItem(
                id = "course-cyber-security",
                title = "Cyber Security & Ethical Hacking",
                category = "Security",
                duration = "4 to 6 Months",
                mode = "Security Lab & Live Online",
                eligibility = "Basic computer knowledge / networking basics",
                whoShouldJoin = "Students wanting to break into the booming cybersecurity domain, future penetration testers, and security analysts.",
                overview = "Learn offensive and defensive cybersecurity methodologies. Master Linux command line, reconnaissance, web vulnerability testing (OWASP Top 10), ethical hacking tools, and incident prevention.",
                curriculum = listOf(
                    CurriculumModule(1, "Security Fundamentals & Kali Linux", listOf("Introduction to Information Security", "Kali Linux Setup & Shell Scripting", "Anonymity, VPNs, and Proxies")),
                    CurriculumModule(2, "Reconnaissance & Footprinting", listOf("Passive & Active Information Gathering", "Nmap Scanning & OS Fingerprinting", "Vulnerability Scanning with Nessus")),
                    CurriculumModule(3, "System & Web Hacking Defense", listOf("OWASP Top 10 Web Vulnerabilities", "SQL Injection & XSS Exploitation", "Password Cracking Techniques", "Metasploit Framework Hands-on")),
                    CurriculumModule(4, "Defensive Security & Forensics", listOf("Network Forensics with Wireshark", "Firewalls, IDS & IPS Configuration", "Security Best Practices & Incident Response"))
                ),
                careerOpportunities = listOf("Cyber Security Analyst", "Junior Penetration Tester", "SOC Analyst", "Security Consultant"),
                practicalProjects = listOf("CTF (Capture The Flag) Challenge Walkthrough", "OWASP Top 10 Web Application Vulnerability Report", "Hardened Linux Server Security Blueprint"),
                certificateInfo = "TechSpike Cyber Security Specialist Certification",
                nextBatch = "Starts next week",
                rating = 4.9f,
                enrolledStudents = 210,
                isPopular = true
            ),
            CourseItem(
                id = "course-c-cpp",
                title = "C / C++ Programming Fundamentals",
                category = "Programming",
                duration = "2 Months",
                mode = "Classroom Coding Lab & Online",
                eligibility = "School students, BCA, B.Tech, MCA, Absolute Beginners",
                whoShouldJoin = "Students looking to build unbreakable coding fundamentals, understand memory pointers, and master Object-Oriented Programming (OOP).",
                overview = "The timeless foundation of computer science. Master procedural programming in C and object-oriented paradigms in C++ with 100+ coding challenges, logic building, and data structures.",
                curriculum = listOf(
                    CurriculumModule(1, "Logic Building in C", listOf("Variables, Data Types & Operators", "Conditional Statements & Loops", "Functions & Variable Scope")),
                    CurriculumModule(2, "Advanced C Concepts", listOf("1D and 2D Arrays", "Strings & Character Handling", "Pointers, References & Memory Allocation", "Structures & File I/O")),
                    CurriculumModule(3, "Object-Oriented Programming (C++)", listOf("Classes, Objects & Constructors", "Inheritance & Polymorphism", "Function & Operator Overloading", "Encapsulation & Data Hiding")),
                    CurriculumModule(4, "Templates & Introduction to Data Structures", listOf("Templates & Exception Handling", "Standard Template Library (STL) Vectors", "Stack & Queue basics"))
                ),
                careerOpportunities = listOf("Software Developer Trainee", "Systems Programmer", "Embedded Systems Developer", "Competitive Programmer"),
                practicalProjects = listOf("Student Management System in C", "Bank Account OOP Application in C++", "Mini Text Processing Engine"),
                certificateInfo = "TechSpike Programming Proficiency Certificate",
                nextBatch = "Weekend and Weekday batches available",
                rating = 4.8f,
                enrolledStudents = 180,
                isPopular = false
            ),
            CourseItem(
                id = "course-sql",
                title = "SQL & Relational Database Design",
                category = "Database",
                duration = "1.5 to 2 Months",
                mode = "Practical Lab & Online",
                eligibility = "Any student, analyst, or programmer working with data",
                whoShouldJoin = "Aspiring data analysts, backend engineers, and business professionals seeking to master database queries and analytics.",
                overview = "Learn relational database design, complex SQL joins, aggregation, subqueries, indexing, and stored procedures in MySQL and PostgreSQL.",
                curriculum = listOf(
                    CurriculumModule(1, "RDBMS & Basic Querying", listOf("Relational Concepts & Schemas", "SELECT, WHERE, ORDER BY, LIMIT", "Filtering with LIKE, IN, BETWEEN")),
                    CurriculumModule(2, "Aggregations & Grouping", listOf("COUNT, SUM, AVG, MIN, MAX", "GROUP BY & HAVING Clauses", "Data Cleaning & String Functions")),
                    CurriculumModule(3, "Joins & Subqueries", listOf("INNER, LEFT, RIGHT, FULL Joins", "Correlated & Nested Subqueries", "Common Table Expressions (CTEs)", "Window Functions")),
                    CurriculumModule(4, "Database Administration & Optimization", listOf("Table Constraints (Primary & Foreign Keys)", "Indexes & Query Performance Tuning", "Views, Transactions (ACID) & Procedures"))
                ),
                careerOpportunities = listOf("Database Administrator (DBA)", "Data Analyst", "Backend SQL Specialist", "BI Developer"),
                practicalProjects = listOf("Retail Inventory Management Database", "E-Commerce Customer Analytics Query Suite"),
                certificateInfo = "TechSpike SQL & Database Professional Certificate",
                nextBatch = "Starts this Saturday",
                rating = 4.9f,
                enrolledStudents = 145,
                isPopular = false
            )
        )

        val staticBlogs = listOf(
            BlogPost(
                id = "blog-cloud-desktop-future",
                title = "How Cloud Desktops Are Revolutionizing Remote Work in 2026",
                summary = "Discover how Virtual Desktop Infrastructure (VDI) cuts corporate hardware costs by 45% while guaranteeing military-grade security for hybrid workforces.",
                content = """In today's fast-moving business environment, agility and security are paramount. Traditional corporate laptops come with massive upfront capital costs, cumbersome maintenance, and high risks of sensitive data leaks.

Virtual Desktop Infrastructure (VDI) and Desktop as a Service (DaaS) completely change this paradigm. By hosting desktops in enterprise-grade secure data centers, employees can access their high-performance computing environment from any device, anywhere in the world.

Key Benefits for Businesses:
1. Zero Data Leakage: All corporate files stay securely stored inside encrypted cloud storage, never downloaded onto local hard drives.
2. 45% Cost Reduction: Avoid expensive periodic hardware refreshes. Thin clients and standard laptops can run resource-intensive CAD or accounting software with ease.
3. Rapid Disaster Recovery: If a physical device fails or gets lost, the employee can log in from a new machine in seconds with zero work lost.

At TechSpike Solutions, we design and deploy tailored cloud desktop architectures that empower growing companies across India and globally to scale effortlessly.""",
                category = "Cloud Computing",
                author = "TechSpike Engineering Team",
                date = "September 2026",
                readTime = "4 min read",
                viewsCount = 512
            ),
            BlogPost(
                id = "blog-seo-local-dominance",
                title = "Dominating Google Maps & Local Search: The Complete Guide",
                summary = "Master local SEO tactics, Google Business Profile optimization, and geotargeted content to capture high-intent customers in your region.",
                content = """Over 80% of local service inquiries now originate from Google Maps and local 3-pack search results. If your business is not visible in the top spots, you are surrendering potential leads directly to your competitors.

Here is TechSpike's proven blueprint for local search supremacy:
1. Google Business Profile Optimization: Ensure 100% profile completeness, precise primary and secondary category assignments, and consistent operating hours.
2. NAP Consistency: Name, Address, and Phone number must match character-for-character across your website, Google profile, and local directories.
3. Review Acquisition Strategy: Actively encourage happy clients to leave authentic reviews mentioning specific services and locations.
4. Localized Schema Markup: Implement JSON-LD LocalBusiness structured data on your website to make your service areas crystal clear to Google's search crawlers.

Partnering with an experienced digital agency like TechSpike Solutions ensures your digital storefront stays consistently ahead of algorithm shifts.""",
                category = "Digital Marketing",
                author = "Growth Marketing Team",
                date = "August 2026",
                readTime = "5 min read",
                viewsCount = 684
            ),
            BlogPost(
                id = "blog-disaster-recovery-ransomware",
                title = "Why Every Small & Medium Business Needs a True Disaster Recovery Plan",
                summary = "A simple USB backup is no longer enough. Learn how automated immutable snapshots protect companies from devastating cyber threats.",
                content = """Many businesses assume that having an external hard drive with last week's files constitutes a backup. Unfortunately, modern ransomware actively hunts down connected USB drives and network shares, encrypting both the live data and the backup simultaneously.

A true Disaster Recovery Plan (DRP) incorporates the 3-2-1 backup rule:
- 3 copies of your vital data
- 2 different types of storage media
- 1 copy stored securely off-site in an immutable cloud vault

Recovery Time Objective (RTO) vs. Recovery Point Objective (RPO):
Your business must know how much data loss it can tolerate (RPO) and how quickly systems must be restored (RTO). TechSpike's automated disaster recovery solutions guarantee automated hourly snapshots and quick failover servers, reducing potential downtime from weeks to minutes.""",
                category = "Disaster Recovery",
                author = "IT Infrastructure Specialist",
                date = "July 2026",
                readTime = "6 min read",
                viewsCount = 420
            ),
            BlogPost(
                id = "blog-in-demand-tech-careers",
                title = "Top In-Demand IT Career Paths in 2026: What Should You Learn?",
                summary = "Comparing Web Development, Cyber Security, and Cloud Networking to help students make the right career choice with maximum industry growth.",
                content = """The technology job market is evolving rapidly. Whether you are a college graduate or seeking a career transition, focusing on practical, hands-on skills is the key to securing rewarding opportunities.

1. Full Stack Web Development:
With every business establishing an online presence, demand for skilled developers who can build fast, responsive applications using React and Node.js remains exceptionally strong.

2. Cyber Security & Ethical Hacking:
As digital transformation accelerates, cyber attacks are escalating in frequency. Certified security analysts and penetration testers command top-tier compensation worldwide.

3. Computer Networking & Cloud Infrastructure:
Modern hybrid workplaces rely heavily on robust network infrastructure and cloud setups. Network engineers who understand VLANs, firewalls, and cloud routing are indispensable.

At TechSpike Training Institute in Ludhiana, we train students on real-world projects with dedicated lab infrastructure and industry-recognized certifications.""",
                category = "Career & Training",
                author = "Career Advisory Cell",
                date = "June 2026",
                readTime = "5 min read",
                viewsCount = 890
            )
        )

        val staticTestimonials = listOf(
            Testimonial(
                id = "test-1",
                name = "Harpreet Singh",
                role = "Managing Director",
                organizationOrCourse = "Apex Industrial Fabricators",
                comment = "TechSpike Solutions completely transformed our business website and digital presence. Our inquiries from Punjab and international buyers surged by 250% within three months. Their team is extremely professional and responsive.",
                rating = 5.0f
            ),
            Testimonial(
                id = "test-2",
                name = "Dr. Maninder Kaur",
                role = "Clinic Director",
                organizationOrCourse = "Ludhiana Healthcare Specialists",
                comment = "The Disaster Recovery and Cloud Desktop solution set up by TechSpike gave us absolute peace of mind. Patient records are safe, encrypted, and our medical staff can access files securely from anywhere.",
                rating = 5.0f
            ),
            Testimonial(
                id = "test-3",
                name = "Aman Sharma",
                role = "Student (Web Development Batch)",
                organizationOrCourse = "Placed as Frontend Developer",
                comment = "The practical lab sessions at TechSpike Training Institute were incredible. We built real React and Node applications rather than just reading theory. Within 1 month of course completion, I got placed at an IT firm in Mohali!",
                rating = 5.0f
            ),
            Testimonial(
                id = "test-4",
                name = "Rajesh Gupta",
                role = "Operations Head",
                organizationOrCourse = "Gupta Logistics & Supply Chain",
                comment = "Their network solutions team revamped our entire 3-story office Wi-Fi and structured cabling. No more dropped video calls or network hiccups. Truly top-tier engineering!",
                rating = 5.0f
            )
        )
    }
}
