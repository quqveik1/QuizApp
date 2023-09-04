# QuizApp 🎉
**QuizApp** ist eine faszinierende Android-App für Quizspiele. Die Fragen für die Quizspiele wurden mit der **Chat GPT API** generiert. Tauchen wir in ihre erstaunlichen Möglichkeiten ein! 🧠🎓

------
# Hauptmerkmale der App 👑
1. **App-Architektur** - Die App ist im **`Single Activity`**-Format gestaltet. Alle Bildschirme sind Fragmente, und die Navigation zwischen den Fragmenten erfolgt mit **`Android Jetpack's Navigation`** 📐✏️👷‍♀️.
   
   <img src="https://github.com/quqveik1/QuizApp/assets/64206443/aefb478e-070d-402b-a7b7-89ddb7af8a87" width="400">

3. **Dynamischer Inhalt** - Die Fragen werden mit **`Retrofit`** von einem [Server](https://github.com/quqveik1/QuizServer) abgerufen, der in **`Spring`** geschrieben ist 🌐🔗🌱.
4. **Fragengenerierung** - Die Fragen wurden mit der Chat GPT API generiert. Mit dem Modell `gpt-3.5-turbo` konnten in **5** Minuten 300 gute Fragen zu 24 Themen (insgesamt **7200**) generiert werden. Die Fragen wurden im [json](https://github.com/quqveik1/QuizApp/blob/main/app/src/main/java/com/kurlic/quizapp/game/QuizQuestion.kt)-Format generiert und anschließend auf dem Server auf ihre Richtigkeit überprüft.👨🏼‍🎓🏅📃
   ```kotlin
    data class QuizQuestion(
        val question: String,
        val answers: List<String>,
        val correctAnswerIndex: Int
    ) : Parcelable, Serializable
   ```
  
5. **Listen und Statistiken** - Das Projekt verwendet intensiv **`RecylerView`** zur Anzeige der [Fragenliste](https://github.com/quqveik1/QuizApp/blob/main/app/src/main/java/com/kurlic/quizapp/home/HomeFragment.kt) und [Statistiken](https://github.com/quqveik1/QuizApp/blob/main/app/src/main/java/com/kurlic/quizapp/stats/UserStatsFragment.kt). Um ein flüssiges Scrollen zu ermöglichen, werden Bilder asynchron mit der **`Glide`**-Bibliothek geladen. 🖼  📇📸
  
   <img src="https://github.com/quqveik1/QuizApp/assets/64206443/3246683a-c5d7-485f-9764-031d401f34f3" width="200">
   <img src="https://github.com/quqveik1/QuizApp/assets/64206443/bfa86b19-670f-475d-82fa-10ec9c02a6ac" width="200">
   <img src="https://github.com/quqveik1/QuizApp/assets/64206443/7e3ac36c-1eaf-498b-952a-6fec4efccec9" width="200">
   <img src="https://github.com/quqveik1/QuizApp/assets/64206443/17f1cc51-91ad-40be-95d4-7d64837f3443" width="200">

6. **Benutzerstatistik** - [Statistiken](https://github.com/quqveik1/QuizApp/blob/main/app/src/main/java/com/kurlic/quizapp/stats/UserStats.kt) des Benutzers werden gesammelt und gespeichert. 📊🔬 🧾
   ```kotlin
    class UserStats : Serializable
    {
       companion object const val statsKey = "STATSKEY"
       var totalTime: Long = 0
       var completedQuestions: Int = 0
       var completedRightQuestions: Int = 0
       var themesQuestionsMap: MutableMap<String, Int> = mutableMapOf()
  
       fun addNewGame(gameData: GameData)
    }
    ```
  
7. **Hochwertige Illustrationen** - Für jedes Thema wurden hochwertige Bilder ausgewählt. 🎨
8. **Tablet-Optimierung** - Die Benutzeroberfläche wurde sorgfältig verbessert und für Tablets optimiert. 📱💻
  
   <img src="https://github.com/quqveik1/QuizApp/assets/64206443/2b9a90ac-c22d-41fc-8865-e65b213d8211" width="400">
   <img src="https://github.com/quqveik1/QuizApp/assets/64206443/f9767e20-43ea-4f2b-a948-85f4f38e1497" width="400">
   
9. **Design-Themen** - Es werden sowohl ein heller als auch ein dunkler Modus unterstützt, sodass jeder die für sich passende Option wählen kann. 🌙☀️
  
   <img src="https://github.com/quqveik1/QuizApp/assets/64206443/b3fa60cd-e6e9-4e44-b4bd-c0721e2461fd" width="200">
   <img src="https://github.com/quqveik1/QuizApp/assets/64206443/62bd03c8-e61d-492e-bc9b-b0bc4a7d9ccd" width="200">

---
# Verwendete Bibliotheken 📚
- `Retrofit`
- `Spring`
- `Glide`
- `GSON`
----
# Installation 📲
1. Es wird empfohlen, die App über [Rustore](https://apps.rustore.ru/app/com.kurlic.quizapp) zu installieren (dort ist immer die neueste Version verfügbar).
2. Die App kann auch über [GitHub](https://github.com/quqveik1/QuizApp/releases/latest) installiert werden.
