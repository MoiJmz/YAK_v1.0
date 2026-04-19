package com.example.yak.data

import android.content.Context

class YakPrefs(context: Context) {
    private val prefs = context.getSharedPreferences("yak_prefs", Context.MODE_PRIVATE)

    var onboardingCompletado: Boolean
        get() = prefs.getBoolean("onboarding_completado", false)
        set(value) = prefs.edit().putBoolean("onboarding_completado", value).apply()

    var bdInicializada: Boolean
        get() = prefs.getBoolean("bd_inicializada", false)
        set(value) = prefs.edit().putBoolean("bd_inicializada", value).apply()

    var usuarioIdLogueado: Int
        get() = prefs.getInt("usuario_id_logueado", -1)
        set(value) = prefs.edit().putInt("usuario_id_logueado", value).apply()

    var usuarioNombreLogueado: String?
        get() = prefs.getString("usuario_nombre_logueado", null)
        set(value) = prefs.edit().putString("usuario_nombre_logueado", value).apply()

    var modoOscuro: Boolean
        get() = prefs.getBoolean("modo_oscuro", false)
        set(value) = prefs.edit().putBoolean("modo_oscuro", value).apply()
}
