package com.tintracker

import android.app.Application
import com.tintracker.data.databaseModule
import com.tintracker.data.repository.categoryRepositoryModule
import com.tintracker.data.repository.projectRepositoryModule
import com.tintracker.data.repository.taskRepositoryModule
import com.tintracker.ui.category.categoryModule
import com.tintracker.ui.project.projectModule
import com.tintracker.ui.statistic.statisticModule
import com.tintracker.ui.task.taskModule
import org.koin.android.ext.koin.androidContext
import org.koin.android.ext.koin.androidLogger
import org.koin.core.context.startKoin

class TintrackerApp : Application() {

    override fun onCreate() {
        super.onCreate()
        INSTANCE = this

        startKoin {
            androidLogger()
            androidContext(this@TintrackerApp)
            modules(listOf(
                databaseModule,
                categoryRepositoryModule,
                categoryModule,
                projectRepositoryModule,
                projectModule,
                taskRepositoryModule,
                taskModule,
                statisticModule
            ))
        }
    }

    companion object {
        lateinit var INSTANCE: TintrackerApp
    }
}