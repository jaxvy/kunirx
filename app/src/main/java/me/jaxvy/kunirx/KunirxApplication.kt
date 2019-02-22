package me.jaxvy.kunirx

import android.app.Application
import me.jaxvy.kunirx.di.DaggerKunirxComponent
import me.jaxvy.kunirx.di.KunirxComponent
import me.jaxvy.kunirx.di.KunirxModule

class KunirxApplication : Application() {

    lateinit var kunirxComponent: KunirxComponent

    override fun onCreate() {
        super.onCreate()

        kunirxComponent = DaggerKunirxComponent.builder()
                .kunirxModule(KunirxModule())
                .build()
    }
}
