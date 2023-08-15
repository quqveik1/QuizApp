# QuizApp
Это приложение с квизами для Android. А вопросы к квизам были сгенированы при помощи **Chat GPT API**
------
# Главное о приложении
1. Приложение выполнено в формате **`Single Activity`**. Все экраны - фрагменты, переключение между фрагментами осуществляется при помощи **`Android Jetpack's Navigation`**.
<img src="https://github.com/quqveik1/QuizApp/assets/64206443/aefb478e-070d-402b-a7b7-89ddb7af8a87" width="400">

2. Вопросы получаются при помощи **`Retrofit`** с [сервера](https://github.com/quqveik1/QuizServer), написанного на **`Spring`**.
3. Вопросы были сгенрированы при помощи Chat GPT API. Это позволило за **5** минут сгенировать 300 хороших(вы можете сами в этом убедиться, запустив приложение) вопросов на 24 темы(всего **7200**). Была использована модель `gpt-3.5-turbo`. Вопросы генeрировались в формате [json](https://github.com/quqveik1/QuizApp/blob/main/app/src/main/java/com/kurlic/quizapp/game/QuizQuestion.kt) и потом их корректность проверялась на сервере.
   ```kotlin
   @Parcelize
    data class QuizQuestion(
        val question: String,
        val answers: List<String>,
        val correctAnswerIndex: Int
    ) : Parcelable, Serializable
   ```
4. В проекте активно используется **`RecylerView`** для отображения [списка вопросов](https://github.com/quqveik1/QuizApp/blob/main/app/src/main/java/com/kurlic/quizapp/home/HomeFragment.kt) и [статистики](https://github.com/quqveik1/QuizApp/blob/main/app/src/main/java/com/kurlic/quizapp/stats/UserStatsFragment.kt).
   Чтобы пролистывание не тормозило, загрузка картинок происходит асинхронно через библиотеку **`Glide`**.
   
   <img src="https://github.com/quqveik1/QuizApp/assets/64206443/3246683a-c5d7-485f-9764-031d401f34f3" width="200">
   <img src="https://github.com/quqveik1/QuizApp/assets/64206443/bfa86b19-670f-475d-82fa-10ec9c02a6ac" width="200">
   <img src="https://github.com/quqveik1/QuizApp/assets/64206443/7e3ac36c-1eaf-498b-952a-6fec4efccec9" width="200">
   <img src="https://github.com/quqveik1/QuizApp/assets/64206443/17f1cc51-91ad-40be-95d4-7d64837f3443" width="200">

6. Собирается и хранится [статистика](https://github.com/quqveik1/QuizApp/blob/main/app/src/main/java/com/kurlic/quizapp/stats/UserStats.kt) пользователя.
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
7. Для каждой темы были найдены иллюстрации в высоком разрешении.
8. Интерфейс был тчательно улучшен для планшетной развертки
   
    <img src="https://github.com/quqveik1/QuizApp/assets/64206443/2b9a90ac-c22d-41fc-8865-e65b213d8211" width="400">
    <img src="https://github.com/quqveik1/QuizApp/assets/64206443/f9767e20-43ea-4f2b-a948-85f4f38e1497" width="400">
9. Поддерживается Белая и Тёмная тема
   
   <img src="https://github.com/quqveik1/QuizApp/assets/64206443/b3fa60cd-e6e9-4e44-b4bd-c0721e2461fd" width="200">
   <img src="https://github.com/quqveik1/QuizApp/assets/64206443/62bd03c8-e61d-492e-bc9b-b0bc4a7d9ccd" width="200">

----
# Установка
1. Приложение рекомендуется установить через [Rustore](https://apps.rustore.ru/app/com.kurlic.quizapp) (там будет всегда последняя собранная версия)
2. Также приложение можно установить через [GitHub](https://github.com/quqveik1/QuizApp/releases/latest)



    
   
