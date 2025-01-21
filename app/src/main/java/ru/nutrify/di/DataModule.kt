package ru.nutrify.di

import android.content.Context
import android.content.SharedPreferences
import dagger.Binds
import dagger.Module
import dagger.Provides
import io.ktor.client.HttpClient
import io.ktor.client.call.body
import io.ktor.client.plugins.auth.Auth
import io.ktor.client.plugins.auth.providers.BearerTokens
import io.ktor.client.plugins.auth.providers.bearer
import io.ktor.client.plugins.contentnegotiation.ContentNegotiation
import io.ktor.client.plugins.defaultRequest
import io.ktor.client.request.accept
import io.ktor.client.request.post
import io.ktor.client.request.setBody
import io.ktor.client.request.url
import io.ktor.http.ContentType
import io.ktor.http.HttpStatusCode
import io.ktor.http.contentType
import io.ktor.serialization.kotlinx.json.json
import kotlinx.serialization.json.Json
import ru.nutrify.R
import ru.nutrify.data.dto.TokenDTO
import ru.nutrify.data.local.TokenService
import java.util.prefs.Preferences

@Module
interface DataModule {

    companion object {
        @[ApplicationScope Provides]
        fun provideSharedPreferences(context: Context): SharedPreferences =
            context.getSharedPreferences(
                context.getString(R.string.preference_file_key),
                Context.MODE_PRIVATE
            )


        @[ApplicationScope Provides]
        fun providesHttpClient(tokenService: TokenService): HttpClient {
            return HttpClient {
                defaultRequest {
                    url(BASE_URL)
                    contentType(ContentType.Application.Json)
                    accept(ContentType.Application.Json)
                }
                install(ContentNegotiation) {
                    json(
                        Json {
                            ignoreUnknownKeys = true
                            prettyPrint = true
                            isLenient = true
                        }
                    )
                }
                install(Auth) {
                    bearer {
                        loadTokens {
                            val bearer = tokenService.loadAccessToken() ?: return@loadTokens null
                            val refresh = tokenService.loadRefreshToken() ?: return@loadTokens null

                            BearerTokens(bearer, refresh)
                        }
                        refreshTokens {

                            val response = client.post {
                                markAsRefreshTokenRequest()
                                url("auth/refresh")
                                setBody(hashMapOf("refreshToken" to tokenService.loadRefreshToken()))
                            }
                            if (response.status == HttpStatusCode.OK) {
                                val token = response.body<TokenDTO>()

                                tokenService.setAccessToken(token.access)
                                tokenService.setRefreshToken(token.refresh)

                                return@refreshTokens BearerTokens(
                                    token.access,
                                    token.refresh
                                )
                            }
                            null


                        }
                        this.sendWithoutRequest { true }
                    }
                }
            }
        }
    }
}

private const val BASE_URL = "http://192.168.1.56:8000"
