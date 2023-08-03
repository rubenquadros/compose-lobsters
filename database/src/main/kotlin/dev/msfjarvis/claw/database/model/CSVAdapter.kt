/*
 * Copyright © 2021-2023 Harsh Shandilya.
 * Use of this source code is governed by an MIT-style
 * license that can be found in the LICENSE file or at
 * https://opensource.org/licenses/MIT.
 */
package dev.msfjarvis.claw.database.model

import app.cash.sqldelight.ColumnAdapter

object CSVAdapter : ColumnAdapter<List<String>, String> {
  private const val SEPARATOR = ","

  override fun decode(databaseValue: String): List<String> {
    return databaseValue.split(SEPARATOR)
  }

  override fun encode(value: List<String>): String {
    return value.joinToString(SEPARATOR)
  }
}
