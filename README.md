# LinguAI 🌍📱  
**Aplicație mobilă Android de traducere multimodală (Text • Voce • Imagine)**

LinguAI este o aplicație mobilă Android de traducere multimodală, care permite traducerea textelor scrise, a vorbirii și a textului extras din imagini, punând accent pe simplitate, viteză și utilizarea componentelor on-device disponibile pe platforma Android.

---

## ✨ Funcționalități principale

- 📝 **Traducere text** cu detectare automată a limbii sursă  
- 🎤 **Traducere vocală** (Speech → Text → Traducere)  
- 📷 **Traducere din imagine (OCR)** folosind procesare on-device   
- 🕓 **Istoric traduceri** asociat contului utilizatorului  
- 🔐 **Autentificare utilizator** (Firebase Authentication)  
- ⚙️ **Setări aplicație** (limbă, dark mode, notificări, ștergere istoric)

---

## 🧠 Tehnologii și concepte utilizate

### Platformă & limbaj
- **Android**
- **Kotlin**

### UI & arhitectură
- **Jetpack Compose** – UI declarativ
- **Navigation Compose** – navigare între ecrane
- **MVVM (Model–View–ViewModel)**

### AI & procesare on-device
- **ML Kit – Text Recognition (OCR)**
- **ML Kit – Language Identification**

### Servicii Android
- **SpeechRecognizer** – recunoaștere vocală

### Backend & networking
- **Firebase Authentication**
- **Firebase Firestore** – stocare istoric traduceri
- **MyMemory Translation API** – serviciu extern de traducere
- **Retrofit + JSON parsing**
- **Kotlin Coroutines** – operații asincrone

---

## 🖥️ Structura aplicației (overview)

Aplicația este organizată în jurul unui ecran principal (Home), care oferă acces rapid la cele patru funcționalități de bază:
- Text
- Voce
- Imagine (OCR)
- Istoric

Fiecare funcționalitate este implementată ca ecran separat, cu logică gestionată prin ViewModel-uri lifecycle-aware. Datele sunt propagate către UI într-un mod reactiv, asigurând o experiență fluentă pentru utilizator.

---

## 🚀 Rulare aplicație

### Cerințe
- Android Studio (versiune recentă)
- Android SDK 26+
- Emulator Android sau dispozitiv fizic
- Conexiune la internet (pentru traducere)

### Pași

```bash
git clone https://github.com/codmihaic/LinguAI.git
```

1. Deschide proiectul în **Android Studio**
2. Configurează un emulator sau conectează un dispozitiv Android
3. Rulează aplicația

---

## ⚠️ Limitări cunoscute

- Traducerea depinde de un **API extern gratuit**, cu limitări de calitate și disponibilitate
- **Nu este disponibilă traducerea offline**
- Aplicația este orientată în principal spre **scop educațional**

---

## 🔮 Posibile îmbunătățiri viitoare

- Integrarea unui **motor de traducere mai performant** (API comercial sau model AI propriu)
- Adăugarea unei **pagini de profil utilizator**
- Extinderea aplicației cu **funcționalități educaționale** (ex. lecții de vocabular)
- Optimizări suplimentare de **UI/UX**

---

## 👤 Autor

**Codreanu Mihai-Constantin**  
An IV – Tehnologia Informației  
Universitatea Politehnica din Timișoara
