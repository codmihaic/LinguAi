package com.example.linguai.ui

import androidx.compose.runtime.Composable
import android.os.Build
import androidx.compose.ui.platform.LocalConfiguration
import java.util.Locale

/*
 * Chei pentru textele din UI.
 */
enum class UiTextKey {
    // Home
    HomeTitle,
    HomeTileText,
    HomeTileVoice,
    HomeTileImage,
    HomeTileHistory,
    HomeTileSettings,
    HomeTileAuth,

    // Text translate
    TextTitle,
    TextFromLabel,
    TextToLabel,
    TextInputLabel,
    TextTranslateButtonIdle,
    TextTranslateButtonLoading,

    // Voice translate
    VoiceTitle,
    VoiceHint,
    VoiceInputLanguageLabel,
    VoiceStartListening,
    VoiceStopListening,
    VoiceListening,
    VoiceRecognizedLabel,
    VoiceTranslationLabel,
    VoiceTranslatingLabel,
    VoiceDetectedLangLabel,

    // Settings
    SettingsTitle,
    SettingsAppLanguageLabel,
    SettingsApplyButton,
    SettingsLanguagePanelTitle,
    SettingsDarkMode,
    SettingsNotifications,
    SettingsClearHistory,
    SettingsAbout,
    SettingsAboutDescription,
    SettingsLogout,


    // Image OCR
    ImageTitle,
    ImageHint,
    ImageTakePhoto,
    ImagePickFromGallery,
    ImageRecognizedLabel,
    ImageTranslateButtonIdle,
    ImageTranslateButtonLoading,
    ImageAutoDetectLabel,

    // Auth
    AuthTitleLogin,
    AuthTitleRegister,
    AuthEmailLabel,
    AuthPasswordLabel,
    AuthButtonLogin,
    AuthButtonRegister,
    AuthSwitchToLogin,
    AuthSwitchToRegister,
    AuthProcessing,
    AuthLoggedInAs,
    AuthLoginSuccess,
    AuthLogout,
    AuthBack,
    AuthFillEmailPassword,
    AuthPasswordMin8,
    AuthGenericError,
    AuthGenericErrorGoogle,
    AuthGoogleButton,

    // History
    HistoryTitle,
    HistoryLoginRequired,
    HistoryGoToLogin,
    HistoryDelete,
    SaveTranslation,
    SaveRequiresLogin,
    SaveSuccess,
    SaveFailed,

    // Common actions
    CommonCopy,
    CommonShare,
    CommonSave,
    CommonToLabel,
    CommonFromLabel,
    CommonCancel,

    // Errors / Toasts / Internal
    ErrorTextEmptyInput,
    ErrorNoRecognizedText,
    ErrorTranslateFormat2,           // %s %s
    ErrorOcrFormat1,                 // %s
    ErrorReadImageFormat1,           // %s
    ErrorReadGalleryFormat1,         // %s
    ToastNoTextFound,
    ToastPhotoFailed,
    ErrorCameraPermissionDenied,
    ErrorMicPermissionDenied,
    ErrorSpeechUnavailable,
    ErrorSpeechCodeFormat1,          // %s
    InternalMissingAppLanguageState,

    // Splash
    SplashWelcome,
}

/**
 * Dicționar simplu: fiecare cheie are un map (cod limbă -> text).
 * Dacă nu găsim limba, cădem pe engleză sau pe primul text definit.
 */
object UiTextDictionary {

    // mapă principală: Key -> (langCode -> string)
    private val strings: Map<UiTextKey, Map<String, String>> = mapOf(

        // ---------- Home ----------
        UiTextKey.HomeTitle to mapOf(
            "ro" to "LinguAI",
            "en" to "LinguAI",
            "it" to "LinguAI",
            "fr" to "LinguAI",
            "de" to "LinguAI",
            "es" to "LinguAI"
        ),
        UiTextKey.HomeTileText to mapOf(
            "ro" to "Text",
            "en" to "Text",
            "it" to "Testo",
            "fr" to "Texte",
            "de" to "Text",
            "es" to "Texto"
        ),
        UiTextKey.HomeTileVoice to mapOf(
            "ro" to "Voce",
            "en" to "Voice",
            "it" to "Voce",
            "fr" to "Voix",
            "de" to "Sprache",
            "es" to "Voz"
        ),
        UiTextKey.HomeTileImage to mapOf(
            "ro" to "Imagine (OCR)",
            "en" to "Image (OCR)",
            "it" to "Immagine (OCR)",
            "fr" to "Image (OCR)",
            "de" to "Bild (OCR)",
            "es" to "Imagen (OCR)"
        ),
        UiTextKey.HomeTileHistory to mapOf(
            "ro" to "Istoric",
            "en" to "History",
            "it" to "Cronologia",
            "fr" to "Historique",
            "de" to "Verlauf",
            "es" to "Historial"
        ),
        UiTextKey.HomeTileSettings to mapOf(
            "ro" to "Setări",
            "en" to "Settings",
            "it" to "Impostazioni",
            "fr" to "Paramètres",
            "de" to "Einstellungen",
            "es" to "Ajustes"
        ),
        UiTextKey.HomeTileAuth to mapOf(
            "ro" to "Autentificare",
            "en" to "Log In",
            "it" to "Impostazioni",
            "fr" to "Paramètres",
            "de" to "Einstellungen",
            "es" to "Ajustes"
        ),

        // ---------- Text translate ----------
        UiTextKey.TextTitle to mapOf(
            "ro" to "Traducere text",
            "en" to "Text translation",
            "it" to "Traduzione testo",
            "fr" to "Traduction de texte",
            "de" to "Textübersetzung",
            "es" to "Traducción de texto"
        ),
        UiTextKey.TextFromLabel to mapOf(
            "ro" to "Din",
            "en" to "From",
            "it" to "Da",
            "fr" to "De",
            "de" to "Aus",
            "es" to "De"
        ),
        UiTextKey.TextToLabel to mapOf(
            "ro" to "În",
            "en" to "To",
            "it" to "A",
            "fr" to "Vers",
            "de" to "Nach",
            "es" to "A"
        ),
        UiTextKey.TextInputLabel to mapOf(
            "ro" to "Introdu text",
            "en" to "Enter text",
            "it" to "Inserisci testo",
            "fr" to "Saisir le texte",
            "de" to "Text eingeben",
            "es" to "Introduce texto"
        ),
        UiTextKey.TextTranslateButtonIdle to mapOf(
            "ro" to "Tradu",
            "en" to "Translate",
            "it" to "Traduci",
            "fr" to "Traduire",
            "de" to "Übersetzen",
            "es" to "Traducir"
        ),
        UiTextKey.TextTranslateButtonLoading to mapOf(
            "ro" to "Se traduce...",
            "en" to "Translating...",
            "it" to "Traduzione...",
            "fr" to "Traduction...",
            "de" to "Übersetzung...",
            "es" to "Traduciendo..."
        ),

        // ---------- Voice translate ----------
        UiTextKey.VoiceTitle to mapOf(
            "ro" to "Voce → Traducere",
            "en" to "Voice → Translate",
            "it" to "Voce → Traduci",
            "fr" to "Voix → Traduire",
            "de" to "Sprache → Übersetzen",
            "es" to "Voz → Traducir"
        ),
        UiTextKey.VoiceHint to mapOf(
            "ro" to "Apasă pe buton, vorbește, apoi textul va fi recunoscut și tradus automat.",
            "en" to "Press the button, speak, and the text will be recognized and translated automatically.",
            "it" to "Premi il pulsante, parla e il testo sarà riconosciuto e tradotto automaticamente.",
            "fr" to "Appuie sur le bouton, parle, et le texte sera reconnu et traduit automatiquement.",
            "de" to "Drücke die Taste, sprich, und der Text wird automatisch erkannt und übersetzt.",
            "es" to "Pulsa el botón, habla y el texto será reconocido y traducido automáticamente."
        ),
        UiTextKey.VoiceInputLanguageLabel to mapOf(
            "ro" to "Limba de intrare:",
            "en" to "Input language:",
            "it" to "Lingua di input:",
            "fr" to "Langue d'entrée :",
            "de" to "Eingabesprache:",
            "es" to "Idioma de entrada:"
        ),
        UiTextKey.VoiceStartListening to mapOf(
            "ro" to "Începe ascultarea",
            "en" to "Start listening",
            "it" to "Avvia ascolto",
            "fr" to "Commencer l'écoute",
            "de" to "Zuhören starten",
            "es" to "Empezar a escuchar"
        ),
        UiTextKey.VoiceStopListening to mapOf(
            "ro" to "Oprește ascultarea",
            "en" to "Stop listening",
            "it" to "Ferma ascolto",
            "fr" to "Arrêter l'écoute",
            "de" to "Zuhören stoppen",
            "es" to "Detener escucha"
        ),
        UiTextKey.VoiceListening to mapOf(
            "ro" to "Ascult...",
            "en" to "Listening...",
            "it" to "Ascolto...",
            "fr" to "J’écoute...",
            "de" to "Ich höre zu...",
            "es" to "Escuchando..."
        ),
        UiTextKey.VoiceRecognizedLabel to mapOf(
            "ro" to "Text recunoscut:",
            "en" to "Recognized text:",
            "it" to "Testo riconosciuto:",
            "fr" to "Texte reconnu :",
            "de" to "Erkannter Text:",
            "es" to "Texto reconocido:"
        ),
        UiTextKey.VoiceTranslationLabel to mapOf(
            "ro" to "Traducere:",
            "en" to "Translation:",
            "it" to "Traduzione:",
            "fr" to "Traduction :",
            "de" to "Übersetzung:",
            "es" to "Traducción:"
        ),
        UiTextKey.VoiceTranslatingLabel to mapOf(
            "ro" to "Se traduce",
            "ro" to "Se traduce...",
            "en" to "Translating...",
            "it" to "Traduzione...",
            "fr" to "Traduction...",
            "de" to "Übersetzung...",
            "es" to "Traducción..."
        ),
        UiTextKey.VoiceDetectedLangLabel to mapOf(
            "ro" to "Limbă detectată:",
            "en" to "Detected language:",
            "it" to "Lingua rilevata:",
            "fr" to "Langue détectée :",
            "de" to "Erkannte Sprache:",
            "es" to "Idioma detectado:"
        ),

        // ---------- Settings ----------
        UiTextKey.SettingsTitle to mapOf(
            "ro" to "Setări",
            "en" to "Settings",
            "it" to "Impostazioni",
            "fr" to "Paramètres",
            "de" to "Einstellungen",
            "es" to "Ajustes"
        ),
        UiTextKey.SettingsAppLanguageLabel to mapOf(
            "ro" to "Limba aplicației",
            "en" to "App language",
            "it" to "Lingua dell’app",
            "fr" to "Langue de l'application",
            "de" to "App-Sprache",
            "es" to "Idioma de la aplicación"
        ),
        UiTextKey.SettingsApplyButton to mapOf(
            "ro" to "Aplică",
            "en" to "Apply",
            "it" to "Applica",
            "fr" to "Appliquer",
            "de" to "Übernehmen",
            "es" to "Aplicar"
        ),

        UiTextKey.SettingsLanguagePanelTitle to mapOf(
            "ro" to "Selectează limba aplicației",
            "en" to "Select application language",
            "it" to "Seleziona la lingua dell'app",
            "fr" to "Sélectionner la langue de l'application",
            "de" to "App-Sprache auswählen",
            "es" to "Selecciona el idioma de la app"
        ),
        UiTextKey.SettingsDarkMode to mapOf(
            "ro" to "Mod Întunecat (Dark Mode)",
            "en" to "Dark mode",
            "it" to "Modalità scura",
            "fr" to "Mode sombre",
            "de" to "Dunkelmodus",
            "es" to "Modo oscuro"
        ),
        UiTextKey.SettingsNotifications to mapOf(
            "ro" to "Notificări",
            "en" to "Notifications",
            "it" to "Notifiche",
            "fr" to "Notifications",
            "de" to "Benachrichtigungen",
            "es" to "Notificaciones"
        ),
        UiTextKey.SettingsClearHistory to mapOf(
            "ro" to "Șterge istoric",
            "en" to "Clear history",
            "it" to "Cancella cronologia",
            "fr" to "Effacer l'historique",
            "de" to "Verlauf löschen",
            "es" to "Borrar historial"
        ),
        UiTextKey.SettingsAbout to mapOf(
            "ro" to "Despre LinguAI",
            "en" to "About LinguAI",
            "it" to "Informazioni su LinguAI",
            "fr" to "À propos de LinguAI",
            "de" to "Über LinguAI",
            "es" to "Acerca de LinguAI"
        ),
        UiTextKey.SettingsAboutDescription to mapOf(
            "ro" to "LinguAI este o aplicație mobilă de traducere multimodală (text, voce și imagine), creată pentru comunicare rapidă în călătorii, la cursuri și în viața de zi cu zi. Poți traduce texte scrise, vorbite sau din poze, cu OCR on-device (ML Kit) pentru viteză și confidențialitate. Aplicația detectează automat limba sursă, oferă redare audio (TTS) și păstrează un istoric local al traducerilor, astfel încât să poți reutiliza ușor expresiile frecvente.",
            "en" to "LinguAI is a multimodal mobile translation app (text, voice, and image) built for quick communication while traveling, studying, and in everyday life. Translate written text, speech, or photos, with on-device OCR (ML Kit) for speed and privacy. The app auto-detects the source language, provides text-to-speech (TTS), and keeps a local translation history so you can quickly reuse frequent phrases.",
            "it" to "LinguAI è un’app mobile di traduzione multimodale (testo, voce e immagine) pensata per comunicare rapidamente in viaggio, a lezione e nella vita quotidiana. Puoi tradurre testi scritti, parlati o da foto, con OCR on-device (ML Kit) per velocità e privacy. L’app rileva automaticamente la lingua di partenza, offre la sintesi vocale (TTS) e salva una cronologia locale delle traduzioni per riutilizzare facilmente le frasi più comuni.",
            "fr" to "LinguAI est une application mobile de traduction multimodale (texte, voix et image) conçue pour communiquer rapidement en voyage, à l’université et au quotidien. Traduisez du texte écrit, de la parole ou des photos, avec OCR sur l’appareil (ML Kit) pour la vitesse et la confidentialité. L’application détecte automatiquement la langue source, propose la synthèse vocale (TTS) et conserve un historique local des traductions pour réutiliser facilement les expressions fréquentes.",
            "de" to "LinguAI ist eine multimodale Übersetzungs-App (Text, Sprache und Bild) für schnelle Kommunikation auf Reisen, im Studium und im Alltag. Übersetze geschriebenen Text, Sprache oder Fotos – mit On-Device-OCR (ML Kit) für Tempo und Datenschutz. Die App erkennt die Ausgangssprache automatisch, bietet Text-to-Speech (TTS) und speichert einen lokalen Übersetzungsverlauf, damit du häufige Ausdrücke schnell wiederverwenden kannst.",
            "es" to "LinguAI es una app móvil de traducción multimodal (texto, voz e imagen) pensada para comunicarte rápidamente en viajes, en clase y en el día a día. Traduce texto escrito, hablado o fotos, con OCR en el dispositivo (ML Kit) para velocidad y privacidad. La app detecta automáticamente el idioma de origen, ofrece texto a voz (TTS) y guarda un historial local de traducciones para reutilizar fácilmente frases frecuentes."
        ),
        UiTextKey.SettingsLogout to mapOf(
            "ro" to "Deconectare",
            "en" to "Log out",
            "it" to "Esci",
            "fr" to "Se déconnecter",
            "de" to "Abmelden",
            "es" to "Cerrar sesión"
        ),

        // ---------- History ----------
        UiTextKey.HistoryTitle to mapOf(
            "ro" to "Istoric",
            "en" to "History",
            "it" to "Cronologia",
            "fr" to "Historique",
            "de" to "Verlauf",
            "es" to "Historial"
        ),
        UiTextKey.HistoryLoginRequired to mapOf(
            "ro" to "Trebuie să fii autentificat pentru a vedea istoricul.",
            "en" to "You must be logged in to view history.",
            "it" to "Devi essere autenticato per vedere la cronologia.",
            "fr" to "Vous devez être connecté pour voir l'historique.",
            "de" to "Du musst angemeldet sein, um den Verlauf zu sehen.",
            "es" to "Debes iniciar sesión para ver el historial."
        ),
        UiTextKey.HistoryGoToLogin to mapOf(
            "ro" to "Mergi la autentificare",
            "en" to "Go to login",
            "it" to "Vai al login",
            "fr" to "Aller à la connexion",
            "de" to "Zur Anmeldung",
            "es" to "Ir a iniciar sesión"
        ),
        UiTextKey.HistoryDelete to mapOf(
            "ro" to "Șterge",
            "en" to "Delete",
            "it" to "Elimina",
            "fr" to "Supprimer",
            "de" to "Löschen",
            "es" to "Eliminar"
        ),
        UiTextKey.SaveTranslation to mapOf(
            "ro" to "Salvează traducerea",
            "en" to "Save translation",
            "it" to "Salva traduzione",
            "fr" to "Enregistrer la traduction",
            "de" to "Übersetzung speichern",
            "es" to "Guardar traducción"
        ),
        UiTextKey.SaveRequiresLogin to mapOf(
            "ro" to "Autentifică-te pentru a salva traduceri.",
            "en" to "Log in to save translations.",
            "it" to "Accedi per salvare le traduzioni.",
            "fr" to "Connectez-vous pour enregistrer des traductions.",
            "de" to "Melde dich an, um Übersetzungen zu speichern.",
            "es" to "Inicia sesión para guardar traducciones."
        ),
        UiTextKey.SaveSuccess to mapOf(
            "ro" to "Traducere salvată cu succes.",
            "en" to "Translation saved successfully.",
            "it" to "Traduzione salvata con successo.",
            "fr" to "Traduction enregistrée avec succès.",
            "de" to "Übersetzung erfolgreich gespeichert.",
            "es" to "Traducción guardada correctamente."
        ),

        UiTextKey.SaveFailed to mapOf(
            "ro" to "Eroare la salvarea traducerii.",
            "en" to "Failed to save translation.",
            "it" to "Errore nel salvataggio della traduzione.",
            "fr" to "Erreur lors de l'enregistrement de la traduction.",
            "de" to "Fehler beim Speichern der Übersetzung.",
            "es" to "Error al guardar la traducción."
        ),

        // ---------- Image OCR ----------
        UiTextKey.ImageTitle to mapOf(
            "ro" to "Imagine (OCR)",
            "en" to "Image (OCR)",
            "it" to "Immagine (OCR)",
            "fr" to "Image (OCR)",
            "de" to "Bild (OCR)",
            "es" to "Imagen (OCR)"
        ),
        UiTextKey.ImageHint to mapOf(
            "ro" to "Alege o imagine sau fă o poză, vom extrage textul (OCR) și apoi îl poți traduce.",
            "en" to "Pick an image or take a photo, we'll extract the text (OCR) and then you can translate it.",
            "it" to "Scegli un'immagine o scatta una foto, estrarremo il testo (OCR) e poi potrai tradurlo.",
            "fr" to "Choisissez une image ou prenez une photo, nous extrairons le texte (OCR) puis vous pourrez le traduire.",
            "de" to "Wähle ein Bild oder mache ein Foto, wir extrahieren den Text (OCR) und du kannst ihn anschließend übersetzen.",
            "es" to "Elige una imagen o toma una foto, extraeremos el texto (OCR) y luego podrás traducirlo."
        ),
        UiTextKey.ImageTakePhoto to mapOf(
            "ro" to "Fă poză",
            "en" to "Take photo",
            "it" to "Scatta foto",
            "fr" to "Prendre une photo",
            "de" to "Foto machen",
            "es" to "Tomar foto"
        ),
        UiTextKey.ImagePickFromGallery to mapOf(
            "ro" to "Alege din galerie",
            "en" to "Pick from gallery",
            "it" to "Scegli dalla galleria",
            "fr" to "Choisir depuis la galerie",
            "de" to "Aus Galerie wählen",
            "es" to "Elegir de la galería"
        ),
        UiTextKey.ImageRecognizedLabel to mapOf(
            "ro" to "Text recunoscut (editabil):",
            "en" to "Recognized text (editable):",
            "it" to "Testo riconosciuto (modificabile):",
            "fr" to "Texte reconnu (modifiable) :",
            "de" to "Erkannter Text (bearbeitbar):",
            "es" to "Texto reconocido (editable):"
        ),
        UiTextKey.ImageTranslateButtonIdle to mapOf(
            "ro" to "Tradu",
            "en" to "Translate",
            "it" to "Traduci",
            "fr" to "Traduire",
            "de" to "Übersetzen",
            "es" to "Traducir"
        ),
        UiTextKey.ImageTranslateButtonLoading to mapOf(
            "ro" to "Se traduce...",
            "en" to "Translating...",
            "it" to "Traduzione...",
            "fr" to "Traduction...",
            "de" to "Übersetzung...",
            "es" to "Traduciendo..."
        ),
        UiTextKey.ImageAutoDetectLabel to mapOf(
            "ro" to "Auto (detectare limbă via ML Kit)",
            "en" to "Auto (language detection via ML Kit)",
            "it" to "Auto (rilevamento lingua via ML Kit)",
            "fr" to "Auto (détection de langue via ML Kit)",
            "de" to "Auto (Spracherkennung über ML Kit)",
            "es" to "Auto (detección de idioma vía ML Kit)"
        ),

        // ---------- Auth ----------
        UiTextKey.AuthTitleLogin to mapOf(
            "ro" to "Autentificare",
            "en" to "Sign in",
            "it" to "Accedi",
            "fr" to "Connexion",
            "de" to "Anmelden",
            "es" to "Iniciar sesión"
        ),
        UiTextKey.AuthTitleRegister to mapOf(
            "ro" to "Creează cont",
            "en" to "Create account",
            "it" to "Crea account",
            "fr" to "Créer un compte",
            "de" to "Konto erstellen",
            "es" to "Crear cuenta"
        ),
        UiTextKey.AuthEmailLabel to mapOf(
            "ro" to "Email",
            "en" to "Email",
            "it" to "Email",
            "fr" to "Email",
            "de" to "E-Mail",
            "es" to "Email"
        ),
        UiTextKey.AuthPasswordLabel to mapOf(
            "ro" to "Parolă",
            "en" to "Password",
            "it" to "Password",
            "fr" to "Mot de passe",
            "de" to "Passwort",
            "es" to "Contraseña"
        ),
        UiTextKey.AuthButtonLogin to mapOf(
            "ro" to "Login",
            "en" to "Sign in",
            "it" to "Accedi",
            "fr" to "Se connecter",
            "de" to "Anmelden",
            "es" to "Entrar"
        ),
        UiTextKey.AuthButtonRegister to mapOf(
            "ro" to "Înregistrare",
            "en" to "Register",
            "it" to "Registrati",
            "fr" to "S’inscrire",
            "de" to "Registrieren",
            "es" to "Registrarse"
        ),
        UiTextKey.AuthSwitchToLogin to mapOf(
            "ro" to "Ai deja cont? Login",
            "en" to "Already have an account? Sign in",
            "it" to "Hai già un account? Accedi",
            "fr" to "Vous avez déjà un compte ? Connexion",
            "de" to "Schon ein Konto? Anmelden",
            "es" to "¿Ya tienes cuenta? Iniciar sesión"
        ),
        UiTextKey.AuthSwitchToRegister to mapOf(
            "ro" to "Nu ai cont? Înregistrare",
            "en" to "No account? Register",
            "it" to "Nessun account? Registrati",
            "fr" to "Pas de compte ? Inscription",
            "de" to "Kein Konto? Registrieren",
            "es" to "¿Sin cuenta? Regístrate"
        ),
        UiTextKey.AuthProcessing to mapOf(
            "ro" to "Se procesează...",
            "en" to "Processing...",
            "it" to "Elaborazione...",
            "fr" to "Traitement...",
            "de" to "Wird verarbeitet...",
            "es" to "Procesando..."
        ),
        UiTextKey.AuthLoggedInAs to mapOf(
            "ro" to "Logat ca:",
            "en" to "Signed in as:",
            "it" to "Accesso come:",
            "fr" to "Connecté en tant que :",
            "de" to "Angemeldet als:",
            "es" to "Conectado como:"
        ),
        UiTextKey.AuthLogout to mapOf(
            "ro" to "Deconectare",
            "en" to "Sign out",
            "it" to "Esci",
            "fr" to "Déconnexion",
            "de" to "Abmelden",
            "es" to "Cerrar sesión"
        ),
        UiTextKey.AuthBack to mapOf(
            "ro" to "Înapoi",
            "en" to "Back",
            "it" to "Indietro",
            "fr" to "Retour",
            "de" to "Zurück",
            "es" to "Atrás"
        ),
        UiTextKey.AuthFillEmailPassword to mapOf(
            "ro" to "Completează email și parolă.",
            "en" to "Please enter email and password.",
            "it" to "Inserisci email e password.",
            "fr" to "Saisissez l’email et le mot de passe.",
            "de" to "Bitte E-Mail und Passwort eingeben.",
            "es" to "Introduce email y contraseña."
        ),
        UiTextKey.AuthPasswordMin8 to mapOf(
            "ro" to "Parola trebuie să aibă minim 8 caractere.",
            "en" to "Password must be at least 8 characters.",
            "it" to "La password deve avere almeno 8 caratteri.",
            "fr" to "Le mot de passe doit contenir au moins 8 caractères.",
            "de" to "Passwort muss mindestens 8 Zeichen haben.",
            "es" to "La contraseña debe tener al menos 8 caracteres."
        ),
        UiTextKey.AuthGenericError to mapOf(
            "ro" to "Eroare autentificare",
            "en" to "Authentication error",
            "it" to "Errore di autenticazione",
            "fr" to "Erreur d'authentification",
            "de" to "Authentifizierungsfehler",
            "es" to "Error de autenticación"
        ),
        UiTextKey.AuthGenericErrorGoogle to mapOf(
            "ro" to "Eroare autentificare Google",
            "en" to "Google Authentication error",
            "it" to "Errore di autenticazione Google",
            "fr" to "Erreur d'authentification Google",
            "de" to "Google Authentifizierungsfehler",
            "es" to "Error de autenticación Google"
        ),
        UiTextKey.AuthGoogleButton to mapOf(
            "ro" to "Continuă cu Google",
            "en" to "Continue with Google",
            "it" to "Continua con Google",
            "fr" to "Continuer avec Google",
            "de" to "Mit Google fortfahren",
            "es" to "Continuar con Google"
        ),
        UiTextKey.AuthLoginSuccess to mapOf(
            "ro" to "Logare reușită",
            "en" to "Logged in successfully",
            "it" to "Accesso riuscito",
            "fr" to "Connexion réussie",
            "de" to "Anmeldung erfolgreich",
            "es" to "Inicio de sesión exitoso"
        ),

        // ---------- Common ----------
        UiTextKey.CommonCopy to mapOf(
            "ro" to "Copiază",
            "en" to "Copy",
            "it" to "Copia",
            "fr" to "Copier",
            "de" to "Kopieren",
            "es" to "Copiar"
        ),
        UiTextKey.CommonShare to mapOf(
            "ro" to "Distribuie",
            "en" to "Share",
            "it" to "Condividi",
            "fr" to "Partager",
            "de" to "Teilen",
            "es" to "Compartir"
        ),
        UiTextKey.CommonSave to mapOf(
            "ro" to "Salvează",
            "en" to "Save",
            "it" to "Salva",
            "fr" to "Enregistrer",
            "de" to "Speichern",
            "es" to "Guardar"
        ),

        UiTextKey.CommonFromLabel to mapOf(
            "ro" to "Din",
            "en" to "From",
            "it" to "Da",
            "fr" to "De",
            "de" to "Von",
            "es" to "De"
        ),
        UiTextKey.CommonToLabel to mapOf(
            "ro" to "În",
            "en" to "To",
            "it" to "In",
            "fr" to "Vers",
            "de" to "Nach",
            "es" to "A"
        ),
        UiTextKey.CommonCancel to mapOf(
            "ro" to "Anulează",
            "en" to "Cancel",
            "it" to "Annulla",
            "fr" to "Annuler",
            "de" to "Abbrechen",
            "es" to "Cancelar"
        ),

        // ---------- NEW: Errors / Toasts / Internal ----------
        UiTextKey.ErrorTextEmptyInput to mapOf(
            "ro" to "Introdu un text pentru traducere.",
            "en" to "Enter text to translate.",
            "it" to "Inserisci un testo da tradurre.",
            "fr" to "Saisissez un texte à traduire.",
            "de" to "Gib einen Text zum Übersetzen ein.",
            "es" to "Introduce un texto para traducir."
        ),
        UiTextKey.ErrorNoRecognizedText to mapOf(
            "ro" to "Nu există text recunoscut pentru traducere.",
            "en" to "No recognized text to translate.",
            "it" to "Nessun testo riconosciuto da tradurre.",
            "fr" to "Aucun texte reconnu à traduire.",
            "de" to "Kein erkannter Text zum Übersetzen.",
            "es" to "No hay texto reconocido para traducir."
        ),
        UiTextKey.ErrorTranslateFormat2 to mapOf(
            "ro" to "Eroare la traducere: %s %s",
            "en" to "Translation error: %s %s",
            "it" to "Errore di traduzione: %s %s",
            "fr" to "Erreur de traduction : %s %s",
            "de" to "Übersetzungsfehler: %s %s",
            "es" to "Error de traducción: %s %s"
        ),
        UiTextKey.ErrorOcrFormat1 to mapOf(
            "ro" to "Eroare la OCR: %s",
            "en" to "OCR error: %s",
            "it" to "Errore OCR: %s",
            "fr" to "Erreur OCR : %s",
            "de" to "OCR-Fehler: %s",
            "es" to "Error de OCR: %s"
        ),
        UiTextKey.ErrorReadImageFormat1 to mapOf(
            "ro" to "Eroare la citirea imaginii: %s",
            "en" to "Failed to read image: %s",
            "it" to "Errore nella lettura dell’immagine: %s",
            "fr" to "Erreur de lecture de l’image : %s",
            "de" to "Fehler beim Lesen des Bildes: %s",
            "es" to "Error al leer la imagen: %s"
        ),
        UiTextKey.ErrorReadGalleryFormat1 to mapOf(
            "ro" to "Eroare la citirea imaginii din galerie: %s",
            "en" to "Failed to read gallery image: %s",
            "it" to "Errore nella lettura dell’immagine dalla galleria: %s",
            "fr" to "Erreur de lecture depuis la galerie : %s",
            "de" to "Fehler beim Lesen aus der Galerie: %s",
            "es" to "Error al leer desde la galería: %s"
        ),
        UiTextKey.ToastNoTextFound to mapOf(
            "ro" to "Nu am găsit text în imagine.",
            "en" to "No text found in the image.",
            "it" to "Nessun testo trovato nell’immagine.",
            "fr" to "Aucun texte trouvé dans l’image.",
            "de" to "Kein Text im Bild gefunden.",
            "es" to "No se encontró texto en la imagen."
        ),
        UiTextKey.ToastPhotoFailed to mapOf(
            "ro" to "Fotografia a eșuat sau a fost anulată.",
            "en" to "Photo failed or was canceled.",
            "it" to "La foto è fallita o è stata annullata.",
            "fr" to "La photo a échoué ou a été annulée.",
            "de" to "Foto fehlgeschlagen oder abgebrochen.",
            "es" to "La foto falló o fue cancelada."
        ),
        UiTextKey.ErrorCameraPermissionDenied to mapOf(
            "ro" to "Permisiunea pentru cameră a fost refuzată.",
            "en" to "Camera permission was denied.",
            "it" to "Permesso fotocamera negato.",
            "fr" to "Autorisation caméra refusée.",
            "de" to "Kameraberechtigung verweigert.",
            "es" to "Permiso de cámara denegado."
        ),
        UiTextKey.ErrorMicPermissionDenied to mapOf(
            "ro" to "Permisiunea pentru microfon a fost refuzată.",
            "en" to "Microphone permission was denied.",
            "it" to "Permesso microfono negato.",
            "fr" to "Autorisation micro refusée.",
            "de" to "Mikrofonberechtigung verweigert.",
            "es" to "Permiso de micrófono denegado."
        ),
        UiTextKey.ErrorSpeechUnavailable to mapOf(
            "ro" to "Serviciul de recunoaștere vocală nu este disponibil pe acest device/emulator.",
            "en" to "Speech recognition is not available on this device/emulator.",
            "it" to "Il riconoscimento vocale non è disponibile su questo device/emulatore.",
            "fr" to "La reconnaissance vocale n’est pas disponible sur cet appareil/émulateur.",
            "de" to "Spracherkennung ist auf diesem Gerät/Emulator nicht verfügbar.",
            "es" to "El reconocimiento de voz no está disponible en este dispositivo/emulador."
        ),
        UiTextKey.ErrorSpeechCodeFormat1 to mapOf(
            "ro" to "Eroare recunoaștere (cod %s)",
            "en" to "Recognition error (code %s)",
            "it" to "Errore riconoscimento (codice %s)",
            "fr" to "Erreur de reconnaissance (code %s)",
            "de" to "Erkennungsfehler (Code %s)",
            "es" to "Error de reconocimiento (código %s)"
        ),
        UiTextKey.InternalMissingAppLanguageState to mapOf(
            "ro" to "Eroare internă: AppLanguageState nu a fost furnizat.",
            "en" to "Internal error: AppLanguageState was not provided.",
            "it" to "Errore interno: AppLanguageState non è stato fornito.",
            "fr" to "Erreur interne : AppLanguageState n’a pas été fourni.",
            "de" to "Interner Fehler: AppLanguageState wurde nicht bereitgestellt.",
            "es" to "Error interno: AppLanguageState no fue proporcionado."
        ),

        // ---------- Splash ----------
        UiTextKey.SplashWelcome to mapOf(
            "ro" to "Bine ați venit!",
            "en" to "Welcome!",
            "it" to "Benvenuto!",
            "fr" to "Bienvenue !",
            "de" to "Willkommen!",
            "es" to "¡Bienvenido!"
        ),
    )

    fun resolve(key: UiTextKey, langCode: String): String {
        val langMap = strings[key] ?: return key.name
        return langMap[langCode]
            ?: langMap["en"]
            ?: langMap.values.first()
    }
}

@Composable
fun t(key: UiTextKey): String {
    val appLang = LocalAppLanguageState.current.current
    return UiTextDictionary.resolve(key, appLang.code)
}

@Composable
fun t(key: UiTextKey, vararg args: Any): String {
    val base = t(key)
    return if (args.isEmpty()) base else String.format(Locale.getDefault(), base, *args)
}

@Composable
fun tSystem(key: UiTextKey): String {
    val cfg = LocalConfiguration.current

    val sysLang = if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.N) {
        cfg.locales[0]?.language ?: Locale.getDefault().language
    } else {
        @Suppress("DEPRECATION")
        cfg.locale.language
    }

    return UiTextDictionary.resolve(key, sysLang)
}

