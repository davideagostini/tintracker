package com.tintracker.data

import android.content.Context
import androidx.room.Database
import androidx.room.Room
import androidx.room.RoomDatabase
import androidx.room.TypeConverters
import androidx.sqlite.db.SupportSQLiteDatabase
import com.tintracker.TintrackerApp
import com.tintracker.data.dao.CategoryDao
import com.tintracker.data.dao.ProjectDao
import com.tintracker.data.dao.TaskDao
import com.tintracker.data.model.Category
import com.tintracker.data.model.Project
import com.tintracker.data.model.StatusProjectConverter
import com.tintracker.data.model.Task
import org.koin.android.ext.koin.androidContext
import org.koin.dsl.module
import java.util.concurrent.Executors

val databaseModule = module {
    single { AppDatabase.getInstance(androidContext()) }
    single { get<AppDatabase>().getCategoryDao() }
    single { get<AppDatabase>().getProjectDao() }
    single { get<AppDatabase>().getTaskDao() }
}

@Database(
    entities = [Category::class, Project::class, Task::class],
    version = AppDatabase.DB_VERSION,
    exportSchema = false
)
@TypeConverters(StatusProjectConverter::class)
abstract class AppDatabase : RoomDatabase() {

    abstract fun getCategoryDao(): CategoryDao
    abstract fun getProjectDao(): ProjectDao
    abstract fun getTaskDao(): TaskDao

    companion object {
        const val DB_VERSION = 1
        const val DB_NAME = "tintracker_database"

        fun getInstance(context: Context): AppDatabase {
            return Room
                .databaseBuilder(context, AppDatabase::class.java, DB_NAME)
                .addCallback(CALLBACK)
                .build()
        }

        private val CALLBACK = object : RoomDatabase.Callback() {
            override fun onCreate(db: SupportSQLiteDatabase) {
                super.onCreate(db)
                Executors.newSingleThreadScheduledExecutor().execute {
                    getInstance(TintrackerApp.INSTANCE).getCategoryDao().insertAll(
                        Category.populateCategory())

                    getInstance(TintrackerApp.INSTANCE).getProjectDao().insertAll(
                        Project.populateProject())
                }
            }
        }
    }
}