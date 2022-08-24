/*
 * Husky -- A Pleroma client for Android
 *
 * Copyright (C) 2022  The Husky Developers
 * Copyright (C) 2018  Conny Duck
 *
 * This program is free software: you can redistribute it and/or modify
 * it under the terms of the GNU General Public License as published by
 * the Free Software Foundation, either version 3 of the License, or
 * (at your option) any later version.
 *
 * This program is distributed in the hope that it will be useful,
 * but WITHOUT ANY WARRANTY; without even the implied warranty of
 * MERCHANTABILITY or FITNESS FOR A PARTICULAR PURPOSE.  See the
 * GNU General Public License for more details.
 *
 * You should have received a copy of the GNU General Public License
 * along with this program.  If not, see <https://www.gnu.org/licenses/>.
 */

package com.keylesspalace.tusky

import android.content.Intent
import android.os.Bundle
import android.text.SpannableString
import android.text.method.LinkMovementMethod
import android.text.style.URLSpan
import android.text.util.Linkify
import android.view.MenuItem
import android.widget.TextView
import androidx.annotation.StringRes
import com.keylesspalace.tusky.core.extensions.gone
import com.keylesspalace.tusky.core.extensions.viewBinding
import com.keylesspalace.tusky.databinding.ActivityAboutBinding
import com.keylesspalace.tusky.di.Injectable
import com.keylesspalace.tusky.util.CustomURLSpan

class AboutActivity : BottomSheetActivity(), Injectable {

    private val binding by viewBinding(ActivityAboutBinding::inflate)

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(binding.root)

        setSupportActionBar(binding.includedToolbar.toolbar)
        supportActionBar?.run {
            setDisplayHomeAsUpEnabled(true)
            setDisplayShowHomeEnabled(true)
        }

        setTitle(R.string.about_title_activity)

        binding.versionTextView.text = getString(
            R.string.about_app_version,
            getString(R.string.app_name),
            BuildConfig.VERSION_NAME
        )

        if(BuildConfig.CUSTOM_INSTANCE.isBlank()) {
            binding.aboutPoweredByTusky.gone()
        }

        binding.aboutLicenseInfoTextView.setClickableTextWithoutUnderlines(R.string.about_tusky_license)
        binding.aboutWebsiteInfoTextView.setClickableTextWithoutUnderlines(R.string.about_project_site)
        binding.aboutBugsFeaturesInfoTextView.setClickableTextWithoutUnderlines(R.string.about_bug_feature_request_site)

        binding.tuskyProfileButton.setOnClickListener {
            viewUrl(BuildConfig.SUPPORT_ACCOUNT_URL)
        }

        binding.aboutLicensesButton.setOnClickListener {
            startActivityWithSlideInAnimation(Intent(this, LicenseActivity::class.java))
        }
    }

    override fun onOptionsItemSelected(item: MenuItem): Boolean {
        when(item.itemId) {
            android.R.id.home -> {
                onBackPressed()
                return true
            }
        }
        return super.onOptionsItemSelected(item)
    }
}

private fun TextView.setClickableTextWithoutUnderlines(@StringRes textId: Int) {
    val text = SpannableString(context.getText(textId))

    Linkify.addLinks(text, Linkify.WEB_URLS)


    text.getSpans(0, text.length, URLSpan::class.java).forEach { span ->
        val start = text.getSpanStart(span)
        val end = text.getSpanEnd(span)
        val flags = text.getSpanFlags(span)

        val customSpan = object : CustomURLSpan(span.url) {}

        text.removeSpan(span)
        text.setSpan(customSpan, start, end, flags)
    }
    /*val urlSpans = text.getSpans(0, text.length, URLSpan::class.java)
    for (span in urlSpans) {
        val start = text.getSpanStart(span)
        val end = text.getSpanEnd(span)
        val flags = text.getSpanFlags(span)

        val customSpan = object : CustomURLSpan(span.url) {}

        text.removeSpan(span)
        text.setSpan(customSpan, start, end, flags)
    }*/

    setText(text)
    linksClickable = true
    movementMethod = LinkMovementMethod.getInstance()
}
