/*
Copyright 2022 Savvas Dalkitsis

Licensed under the Apache License, Version 2.0 (the "License");
you may not use this file except in compliance with the License.
You may obtain a copy of the License at

    http://www.apache.org/licenses/LICENSE-2.0

Unless required by applicable law or agreed to in writing, software
distributed under the License is distributed on an "AS IS" BASIS,
WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
See the License for the specific language governing permissions and
limitations under the License.
 */
package com.savvasdalkitsis.uhuruphotos.feature.auth.domain.implementation.module

import com.savvasdalkitsis.uhuruphotos.feature.auth.domain.implementation.cookies.CookieMonitor
import com.savvasdalkitsis.uhuruphotos.feature.auth.domain.implementation.service.TokenRefreshInterceptor
import com.savvasdalkitsis.uhuruphotos.feature.auth.domain.implementation.usecase.AuthenticationUseCase
import com.savvasdalkitsis.uhuruphotos.feature.auth.domain.implementation.usecase.ServerUseCase
import dagger.Binds
import dagger.Module
import dagger.hilt.InstallIn
import dagger.hilt.components.SingletonComponent
import okhttp3.Interceptor

@Module
@InstallIn(SingletonComponent::class)
abstract class AuthBindingsModule {

    @Binds
    @com.savvasdalkitsis.uhuruphotos.feature.auth.domain.api.TokenRefreshInterceptor
    abstract fun tokenRefreshInterceptor(tokenRefreshInterceptor: TokenRefreshInterceptor):
            Interceptor

    @Binds
    abstract fun authenticationUseCase(authenticationUseCase: AuthenticationUseCase):
            com.savvasdalkitsis.uhuruphotos.feature.auth.domain.api.usecase.AuthenticationUseCase

    @Binds
    abstract fun serverUseCase(serverUseCase: ServerUseCase):
            com.savvasdalkitsis.uhuruphotos.feature.auth.domain.api.usecase.ServerUseCase

    @Binds
    abstract fun cookieMonitor(cookieMonitor: CookieMonitor):
            com.savvasdalkitsis.uhuruphotos.feature.auth.domain.api.cookies.CookieMonitor
}