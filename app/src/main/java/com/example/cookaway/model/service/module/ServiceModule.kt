

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
