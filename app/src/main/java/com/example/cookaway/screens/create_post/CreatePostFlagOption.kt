

package com.example.cookaway.screens.create_post

enum class CreatePostFlagOption {
  On,
  Off;

  companion object {
    fun getByCheckedState(checkedState: Boolean?): CreatePostFlagOption {
      val hasFlag = checkedState ?: false
      return if (hasFlag) On else Off
    }

    fun getBooleanValue(flagOption: String): Boolean {
      return flagOption == On.name
    }

    fun getOptions(): List<String> {
      val options = mutableListOf<String>()
      values().forEach { flagOption -> options.add(flagOption.name) }
      return options
    }
  }
}
