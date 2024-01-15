

package com.example.cookaway.model.service

interface LogService {
  fun logNonFatalCrash(throwable: Throwable)
}
