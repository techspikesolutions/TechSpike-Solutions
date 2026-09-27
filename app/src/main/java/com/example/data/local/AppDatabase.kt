package com.example.data.local

import android.content.Context
import androidx.room.Dao
import androidx.room.Database
import androidx.room.Insert
import androidx.room.OnConflictStrategy
import androidx.room.Query
import androidx.room.Room
import androidx.room.RoomDatabase
import kotlinx.coroutines.flow.Flow

@Dao
interface EnquiryDao {
    @Query("SELECT * FROM enquiries ORDER BY timestamp DESC")
    fun getAllEnquiries(): Flow<List<EnquiryEntity>>

    @Insert(onConflict = OnConflictStrategy.REPLACE)
    suspend fun insertEnquiry(enquiry: EnquiryEntity): Long

    @Query("UPDATE enquiries SET status = :status, adminNotes = :notes WHERE id = :id")
    suspend fun updateEnquiryStatus(id: Long, status: String, notes: String)

    @Query("DELETE FROM enquiries WHERE id = :id")
    suspend fun deleteEnquiry(id: Long)

    @Query("SELECT COUNT(*) FROM enquiries")
    fun getEnquiryCount(): Flow<Int>
}

@Dao
interface NotificationDao {
    @Query("SELECT * FROM notifications ORDER BY timestamp DESC")
    fun getAllNotifications(): Flow<List<NotificationEntity>>

    @Insert(onConflict = OnConflictStrategy.REPLACE)
    suspend fun insertNotification(notification: NotificationEntity): Long

    @Query("UPDATE notifications SET isRead = 1 WHERE id = :id")
    suspend fun markAsRead(id: Long)

    @Query("UPDATE notifications SET isRead = 1")
    suspend fun markAllAsRead()

    @Query("DELETE FROM notifications WHERE id = :id")
    suspend fun deleteNotification(id: Long)
}

@Dao
interface CustomContentDao {
    @Query("SELECT * FROM custom_courses ORDER BY timestamp DESC")
    fun getAllCustomCourses(): Flow<List<CustomCourseEntity>>

    @Insert(onConflict = OnConflictStrategy.REPLACE)
    suspend fun insertCourse(course: CustomCourseEntity)

    @Query("DELETE FROM custom_courses WHERE id = :id")
    suspend fun deleteCourse(id: String)

    @Query("SELECT * FROM custom_blogs ORDER BY timestamp DESC")
    fun getAllCustomBlogs(): Flow<List<CustomBlogEntity>>

    @Insert(onConflict = OnConflictStrategy.REPLACE)
    suspend fun insertBlog(blog: CustomBlogEntity)

    @Query("DELETE FROM custom_blogs WHERE id = :id")
    suspend fun deleteBlog(id: String)
}

@Database(
    entities = [
        EnquiryEntity::class,
        NotificationEntity::class,
        CustomCourseEntity::class,
        CustomBlogEntity::class
    ],
    version = 1,
    exportSchema = false
)
abstract class AppDatabase : RoomDatabase() {
    abstract fun enquiryDao(): EnquiryDao
    abstract fun notificationDao(): NotificationDao
    abstract fun customContentDao(): CustomContentDao

    companion object {
        @Volatile
        private var INSTANCE: AppDatabase? = null

        fun getDatabase(context: Context): AppDatabase {
            return INSTANCE ?: synchronized(this) {
                val instance = Room.databaseBuilder(
                    context.applicationContext,
                    AppDatabase::class.java,
                    "techspike_database"
                )
                .fallbackToDestructiveMigration(false)
                .build()
                INSTANCE = instance
                instance
            }
        }
    }
}
