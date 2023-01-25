package com.app.data.features.response.repository

interface UserDataRepository {


    fun setToken(token: String)
    fun getToken(): String?

    fun setSessionId(session_id: String)
    fun getSessionId(): String?


    fun setRefreshToken(token: String)
    fun getRefreshToken(): String?


    fun setName(name: String)
    fun getName(): String?

    fun setSurName(surname: String)
    fun getSurName(): String?

    fun getFullName(): String?
    fun getFullNameFirstChars(): String?


    fun setPhone(phone: String)
    fun getPhone(): String?

    fun setLanguage(language: String)
    fun getLanguageKey(): LanguageKey
    fun getLanguageApi(): String
    fun getLanguageLocale(): String

    fun setThemeIndex(mode: Int)
    fun getThemeIndex(): Int

    fun clear()


}