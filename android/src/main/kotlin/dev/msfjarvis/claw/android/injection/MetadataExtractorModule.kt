/*
 * Copyright © 2021-2023 Harsh Shandilya.
 * Use of this source code is governed by an MIT-style
 * license that can be found in the LICENSE file or at
 * https://opensource.org/licenses/MIT.
 */
package dev.msfjarvis.claw.android.injection

import com.deliveryhero.whetstone.app.ApplicationScope
import com.squareup.anvil.annotations.ContributesTo
import dagger.Module
import dagger.Provides
import io.github.aakira.napier.Napier
import me.saket.unfurl.UnfurlLogger
import me.saket.unfurl.Unfurler
import okhttp3.OkHttpClient

@Module
@ContributesTo(ApplicationScope::class)
object MetadataExtractorModule {
  @Provides
  fun provideUnfurlLogger(): UnfurlLogger {
    return object : UnfurlLogger {
      override fun log(message: String) {
        Napier.d { message }
      }

      override fun log(e: Throwable, message: String) {
        Napier.e(e) { message }
      }
    }
  }

  @Provides
  fun provideUnfurler(
    okHttpClient: OkHttpClient,
    logger: UnfurlLogger,
  ): Unfurler {
    return Unfurler(
      httpClient = okHttpClient,
      logger = logger,
    )
  }
}
