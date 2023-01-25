package com.app.data.features.response.repository

import com.app.data.base.datasource.LocalDataSource
import com.app.data.base.util.DataConstants.ACCESS_SESSION_ID
import com.app.data.base.util.DataConstants.ACCESS_TOKEN
import com.app.data.base.util.DataConstants.LANGUAGE
import com.app.data.base.util.DataConstants.NAME
import com.app.data.base.util.DataConstants.PHONE
import com.app.data.base.util.DataConstants.REFRESH_TOKEN
import com.app.data.base.util.DataConstants.SURNAME
import com.app.data.base.util.DataConstants.THEME
import java.util.*
import javax.inject.Inject
import javax.inject.Singleton

enum class LanguageKey(val local : String, val api : String){
    AZ("az","az-Az" ),
    EN("en","en-En"),
    RU("ru","ru-Ru")}

@Singleton
class UserDataRepositoryImpl @Inject constructor(private val localDataSource: LocalDataSource) :
    UserDataRepository {

    override fun setToken(token: String) = localDataSource.set(ACCESS_TOKEN, token)
    override fun getToken(): String? = localDataSource.get(ACCESS_TOKEN)

    override fun setSessionId(session_id: String) = localDataSource.set(ACCESS_SESSION_ID, session_id)
    override fun getSessionId(): String? = localDataSource.get(ACCESS_SESSION_ID)


    override fun setRefreshToken(token: String) = localDataSource.set(REFRESH_TOKEN, token)
    override fun getRefreshToken(): String? = localDataSource.get(REFRESH_TOKEN)


    override fun setName(name: String) = localDataSource.set(NAME, name)
    override fun getName(): String? = localDataSource.get(NAME,"")

    override fun setSurName(surname: String) = localDataSource.set(SURNAME, surname)
    override fun getSurName(): String? = localDataSource.get(SURNAME,"")

    override fun getFullName(): String?{
       return "${localDataSource.get(NAME,"")} ${localDataSource.get(SURNAME,"")}"
    }

    override fun getFullNameFirstChars(): String?{
        return "${localDataSource.get(NAME,"-")?.substring(0,1)}${localDataSource.get(SURNAME,"-")?.substring(0,1)}"
    }



    override fun setPhone(phone: String) = localDataSource.set(PHONE, phone)
    override fun getPhone(): String? = localDataSource.get(PHONE)

    override fun setLanguage(language: String) =  localDataSource.set(LANGUAGE, language)
    override fun getLanguageKey(): LanguageKey {
        return when (localDataSource.get(LANGUAGE, LanguageKey.AZ.local) ?: LanguageKey.AZ.local) {
            LanguageKey.RU.local -> LanguageKey.RU
            LanguageKey.EN.local -> LanguageKey.EN
            else -> LanguageKey.AZ
        }
    }
    override fun getLanguageApi(): String {
        return when(localDataSource.get(LANGUAGE,Locale.getDefault().language)){
            LanguageKey.RU.local -> LanguageKey.RU.api
            LanguageKey.AZ.local -> LanguageKey.AZ.api
            else -> LanguageKey.EN.api
        }
    }
    override fun getLanguageLocale(): String {
        return when(localDataSource.get(LANGUAGE,Locale.getDefault().language)){
            LanguageKey.RU.local -> LanguageKey.RU.local
            LanguageKey.AZ.local -> LanguageKey.AZ.local
            else -> LanguageKey.EN.local
        }
    }

    override fun setThemeIndex(mode: Int) = localDataSource.set(THEME, mode)
    override fun getThemeIndex(): Int = localDataSource.get(THEME,0)?:0

    override fun clear() = localDataSource.clearSharedPrefs()
}