# QuizApp 🎉
**QuizApp** is an exciting Android application featuring quizzes. The quiz questions were generated using the **Chat GPT API**. Dive into its amazing features! 🧠🎓

------
# Key Features 👑
1. **Application Architecture** - The application is built in a **`Single Activity`** format. All screens are fragments, and transitions between fragments are managed by **`Android Jetpack's Navigation`** 📐✏️👷‍♀️.

   <img src="https://github.com/quqveik1/QuizApp/assets/64206443/aefb478e-070d-402b-a7b7-89ddb7af8a87" width="400">

3. **Dynamic Content** - Questions are fetched using **`Retrofit`** from a [server](https://github.com/quqveik1/QuizServer) written in **`Spring`** 🌐🔗🌱.
4. **Question Generation** - Questions were generated using the Chat GPT API, specifically the `gpt-3.5-turbo` model. Within **5 minutes**, 300 good-quality questions were generated for 24 topics (a total of **7200**). The questions were generated in [json](https://github.com/quqveik1/QuizApp/blob/main/app/src/main/java/com/kurlic/quizapp/game/QuizQuestion.kt) format and then validated on the server.👨🏼‍🎓🏅📃
   ```kotlin
   @Parcelize
    data class QuizQuestion(
        val question: String,
        val answers: List<String>,
        val correctAnswerIndex: Int
    ) : Parcelable, Serializable
   ```
5. **Lists and Statistics** - **`RecyclerView`** is extensively used in the project for displaying [question lists](https://github.com/quqveik1/QuizApp/blob/main/app/src/main/java/com/kurlic/quizapp/home/HomeFragment.kt) and [statistics](https://github.com/quqveik1/QuizApp/blob/main/app/src/main/java/com/kurlic/quizapp/stats/UserStatsFragment.kt). Image loading is performed asynchronously using the **`Glide`** library to ensure smooth scrolling.🖼  📇📸

<img src="https://github.com/quqveik1/QuizApp/assets/64206443/3246683a-c5d7-485f-9764-031d401f34f3" width="200">
<img src="https://github.com/quqveik1/QuizApp/assets/64206443/bfa86b19-670f-475d-82fa-10ec9c02a6ac" width="200">
<img src="https://github.com/quqveik1/QuizApp/assets/64206443/7e3ac36c-1eaf-498b-952a-6fec4efccec9" width="200">
<img src="https://github.com/quqveik1/QuizApp/assets/64206443/17f1cc51-91ad-40be-95d4-7d64837f3443" width="200">

6. **User Statistics** - [User statistics](https://github.com/quqveik1/QuizApp/blob/main/app/src/main/java/com/kurlic/quizapp/stats/UserStats.kt) are collected and stored. 📊🔬 🧾
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
7. **Quality Illustrations** - High-quality images have been selected for each topic. 🎨
8. **Tablet Adaptation** - The interface has been meticulously improved and adapted for tablets. 📱💻

 <img src="https://github.com/quqveik1/QuizApp/assets/64206443/2b9a90ac-c22d-41fc-8865-e65b213d8211" width="400">
 <img src="https://github.com/quqveik1/QuizApp/assets/64206443/f9767e20-43ea-4f2b-a948-85f4f38e1497" width="400">
 
9. **Themes** - Light and Dark themes are supported, offering users a choice to suit their preference. 🌙☀️

<img src="https://github.com/quqveik1/QuizApp/assets/64206443/b3fa60cd-e6e9-4e44-b4bd-c0721e2461fd" width="200">
<img src="https://github.com/quqveik1/QuizApp/assets/64206443/62bd03c8-e61d-492e-bc9b-b0bc4a7d9ccd" width="200">

---
# Libraries Used 📚
- `Retrofit`
- `Spring`
- `Glide`
- `GSON`
----
# Installation 📲
1. The application is recommended to be installed via [Rustore](https://apps.rustore.ru/app/com.kurlic.quizapp) (where the latest build is always available).
2. The application can also be installed via [GitHub](https://github.com/quqveik1/QuizApp/releases/latest).
