/*
Copyright 2022 Google LLC

Licensed under the Apache License, Version 2.0 (the "License");
you may not use this file except in compliance with the License.
You may obtain a copy of the License at

    https://www.apache.org/licenses/LICENSE-2.0

Unless required by applicable law or agreed to in writing, software
distributed under the License is distributed on an "AS IS" BASIS,
WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
See the License for the specific language governing permissions and
limitations under the License.
 */

package com.example.cookaway.model.service.module

import com.example.cookaway.model.service.AccountService
import com.example.cookaway.model.service.ConfigurationService
import com.example.cookaway.model.service.LogService
import com.example.cookaway.model.service.PostStorageService
import com.example.cookaway.model.service.StorageService
import com.example.cookaway.model.service.UserStorageService
import com.example.cookaway.model.service.impl.AccountServiceImpl
import com.example.cookaway.model.service.impl.ConfigurationServiceImpl
import com.example.cookaway.model.service.impl.LogServiceImpl
import com.example.cookaway.model.service.impl.PostStorageServiceImpl
import com.example.cookaway.model.service.impl.StorageServiceImpl
import com.example.cookaway.model.service.impl.UserStorageServiceImpl
import dagger.Binds
import dagger.Module
import dagger.hilt.InstallIn
import dagger.hilt.components.SingletonComponent

@Module
@InstallIn(SingletonComponent::class)
abstract class ServiceModule {
  @Binds abstract fun provideAccountService(impl: AccountServiceImpl): AccountService

  @Binds abstract fun provideLogService(impl: LogServiceImpl): LogService

  @Binds abstract fun provideStorageService(impl: StorageServiceImpl): StorageService

  @Binds
  abstract fun provideConfigurationService(impl: ConfigurationServiceImpl): ConfigurationService
  @Binds
  abstract fun provideUserStorageService(impl: UserStorageServiceImpl): UserStorageService

  @Binds
  abstract fun providePostStorageService(impl: PostStorageServiceImpl): PostStorageService
}
