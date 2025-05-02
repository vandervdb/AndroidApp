package org.vander.spotifyclientfake.fake

import android.content.Intent
import androidx.activity.result.ActivityResultLauncher
import androidx.activity.result.contract.ActivityResultContract
import androidx.core.app.ActivityOptionsCompat

val ActivityResultLauncherStub = object : ActivityResultLauncher<Intent>() {
    override val contract: ActivityResultContract<Intent, *>
        get() = object : ActivityResultContract<Intent, Intent?>() {
            override fun createIntent(context: android.content.Context, input: Intent): Intent =
                input

            override fun parseResult(resultCode: Int, intent: Intent?): Intent? = intent
        }

    override fun launch(
        input: Intent,
        options: ActivityOptionsCompat?
    ) {
        // nothing to do
    }

    override fun unregister() {
        // nothing to do
    }


}