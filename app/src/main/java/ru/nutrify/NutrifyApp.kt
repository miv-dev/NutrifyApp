package ru.nutrify

import android.app.Application
import ru.nutrify.di.ApplicationComponent
import ru.nutrify.di.DaggerApplicationComponent

class NutrifyApp: Application() {

    lateinit var appComponent: ApplicationComponent


    override fun onCreate() {
        super.onCreate()
        appComponent = DaggerApplicationComponent.factory().create(this)
    }

}