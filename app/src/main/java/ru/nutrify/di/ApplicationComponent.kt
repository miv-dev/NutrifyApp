package ru.nutrify.di

import android.content.Context
import dagger.BindsInstance
import dagger.Component
import ru.nutrify.MainActivity

@ApplicationScope
@Component(
    modules = [DataModule::class, PresentationModule::class]
)
interface ApplicationComponent{

    fun inject(activity: MainActivity)

    @Component.Factory
    interface Factory {
        fun create(
            @BindsInstance context: Context
        ): ApplicationComponent
    }

}