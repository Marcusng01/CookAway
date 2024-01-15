

package com.example.cookaway.common.ext

import com.example.cookaway.model.Task

fun Task?.hasDueDate(): Boolean {
  return this?.dueDate.orEmpty().isNotBlank()
}

fun Task?.hasDueTime(): Boolean {
  return this?.dueTime.orEmpty().isNotBlank()
}
