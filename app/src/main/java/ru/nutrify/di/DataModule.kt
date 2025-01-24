package ru.nutrify.di

import android.content.Context
import android.content.SharedPreferences
import dagger.Binds
import dagger.Module
import dagger.Provides
import io.ktor.client.HttpClient
import io.ktor.client.call.body
import io.ktor.client.engine.cio.CIO
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
import kotlinx.serialization.Serializable
import kotlinx.serialization.json.Json
import ru.nutrify.R
import ru.nutrify.data.dto.TokenDTO
import ru.nutrify.data.local.TokenService
import ru.nutrify.data.remote.ApiEndpoints
import ru.nutrify.data.repository.AuthRepositoryImpl
import ru.nutrify.domain.repository.AuthRepository

@Module
interface DataModule {

    @[Binds ApplicationScope]
    fun bindsAuthRepository(impl: AuthRepositoryImpl): AuthRepository


    companion object {
        @[ApplicationScope Provides]
        fun provideSharedPreferences(context: Context): SharedPreferences =
            context.getSharedPreferences(
                context.getString(R.string.preference_file_key),
                Context.MODE_PRIVATE
            )


        @[ApplicationScope Provides]
        fun providesHttpClient(tokenService: TokenService): HttpClient {
            return HttpClient(CIO) {
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
                            val refresh = tokenService.loadRefreshToken()
                            val response = client.post {
                                markAsRefreshTokenRequest()
                                url(ApiEndpoints.REFRESH_TOKEN)
                                setBody(hashMapOf("refresh" to refresh))
                            }
                            if (response.status == HttpStatusCode.OK) {
                                val token = response.body<AccessToken>()

                                tokenService.setAccessToken(token.access)

                                return@refreshTokens BearerTokens(
                                    token.access,
                                    refresh
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
@Serializable
data class AccessToken(
    val access: String
)

private const val BASE_URL = "http://192.168.1.159:8080"
