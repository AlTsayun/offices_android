package com.tsayun.offices.ui.settings

import android.os.Bundle
import androidx.preference.Preference
import androidx.preference.PreferenceFragmentCompat
import androidx.preference.PreferenceManager.getDefaultSharedPreferences
import com.tasyun.offices.R


class SettingsFragment : PreferenceFragmentCompat() {
    override fun onCreatePreferences(savedInstanceState: Bundle?, rootKey: String?) {
        setPreferencesFromResource(R.xml.preferences, rootKey)
        preferenceManager.findPreference<Preference>(getString(R.string.pref_key_log_out))
            ?.setOnPreferenceClickListener { _ ->
                val editor = preferenceManager.sharedPreferences.edit()
                editor.remove(getString(R.string.pref_key_logged_in_user))
                editor.apply()
                return@setOnPreferenceClickListener true
            }
    }
}