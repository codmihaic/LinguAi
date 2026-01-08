package com.example.linguai.model

data class Language(
    val code: String,
    val name: String
) {
    companion object {
        /** Lista comună de limbi pentru Text & Voice. */
        val defaultSupported = listOf(
            Language("ro", "Română"),
            Language("en", "English"),
            Language("it", "Italiano"),
            Language("fr", "Français"),
            Language("de", "Deutsch"),
            Language("es", "Español")
        )
        val uiSupported = listOf(
            Language("ro", "Română"),
            Language("en", "English"),
            Language("it", "Italiano"),
            Language("fr", "Français"),
            Language("de", "Deutsch"),
            Language("es", "Español")
        )

        fun uiDefault(): Language = uiSupported.first()  // Română
    }
}
