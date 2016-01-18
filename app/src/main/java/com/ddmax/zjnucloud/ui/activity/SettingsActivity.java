package com.ddmax.zjnucloud.ui.activity;


import android.content.Intent;
import android.os.Bundle;
import android.preference.Preference;
import android.preference.PreferenceCategory;
import android.preference.PreferenceFragment;
import android.preference.SwitchPreference;
import android.support.v7.app.ActionBar;
import android.view.MenuItem;
import android.widget.Toast;

import com.ddmax.zjnucloud.R;
import com.ddmax.zjnucloud.base.BaseActivity;
import com.ddmax.zjnucloud.model.User;
import com.ddmax.zjnucloud.util.DataCleanManager;

import cn.bmob.v3.BmobUser;

public class SettingsActivity extends BaseActivity {

    private final PrefsFragment mFragment = new PrefsFragment();

    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        // Display the fragment as the main content.
        getFragmentManager().beginTransaction().replace(android.R.id.content,
                mFragment).commit();
        final ActionBar actionBar = getSupportActionBar();
        actionBar.setDisplayHomeAsUpEnabled(true);
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        if (item.getItemId() == android.R.id.home) {
            onBackPressed();
            return true;
        }

        return super.onOptionsItemSelected(item);
    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        mFragment.onActivityResult(requestCode, resultCode, data);
    }

    public static class PrefsFragment extends PreferenceFragment implements Preference.OnPreferenceClickListener {
        private static final String PREF_KEY_CATEGORY_GENERAL = "general";
        private static final String PREF_KEY_CLEAN_CACHE = "clean_cache";
        private static final String PREF_KEY_CATEGORY_USER = "user";
        private static final String PREF_KEY_USER_INFO = "user_info";
        private static final String PREF_KEY_LOGIN = "login";

        private static final int REQUEST_LOGIN = 0;
        private static final int REQUEST_LOGOUT = 1;
        private SwitchPreference mNotificationsPref;

        @Override
        public void onCreate(Bundle savedInstanceState) {
            super.onCreate(savedInstanceState);

            // Load the preferences from an XML resource
            addPreferencesFromResource(R.xml.pref_general);

            initGeneral();
            initUser();
        }

        private void initGeneral() {
            final PreferenceCategory general = (PreferenceCategory) findPreference(PREF_KEY_CATEGORY_GENERAL);
            final Preference loginPref = findPreference(PREF_KEY_LOGIN);
            final Preference cleanCachePref = findPreference(PREF_KEY_CLEAN_CACHE);

            if (BmobUser.getCurrentUser(getActivity()) != null) {
                general.removePreference(loginPref);
            } else {
                loginPref.setOnPreferenceClickListener(this);
            }

            cleanCachePref.setOnPreferenceClickListener(this);
        }

        private void initUser() {
            final PreferenceCategory user = (PreferenceCategory) findPreference(PREF_KEY_CATEGORY_USER);
            if (BmobUser.getCurrentUser(getActivity()) == null) {
                mNotificationsPref = null;
                getPreferenceScreen().removePreference(user);
                return;
            }

            final Preference infoPref = findPreference(PREF_KEY_USER_INFO);
            infoPref.setTitle(BmobUser.getCurrentUser(getActivity()).getUsername());
            infoPref.setOnPreferenceClickListener(this);
        }

        private void cleanCache() {
            DataCleanManager.cleanInternalCache(getActivity());
            DataCleanManager.cleanExternalCache(getActivity());
            Toast.makeText(getActivity(), getString(R.string.settings_clean_cache_finish), Toast.LENGTH_SHORT).show();
        }

        @Override
        public void onStart() {
            super.onStart();
        }

        @Override
        public void onActivityResult(int requestCode, int resultCode, Intent data) {
            if (resultCode == RESULT_OK) {
                switch (requestCode) {
                    case REQUEST_LOGIN:
                    case REQUEST_LOGOUT:
                        getActivity().recreate();
                        break;
                }
            }

            super.onActivityResult(requestCode, resultCode, data);
        }

        @Override
        public boolean onPreferenceClick(Preference preference) {
            switch (preference.getKey()) {
                case PREF_KEY_LOGIN:
                    startActivityForResult(new Intent(getActivity(), LoginActivity.class), 0);
                    return true;
                case PREF_KEY_CLEAN_CACHE:
                    cleanCache();
                    return true;
                case PREF_KEY_USER_INFO:
                    User user = BmobUser.getCurrentUser(getActivity(), User.class);
                    Intent intent = new Intent(getActivity(), ProfileActivity.class);
                    intent.putExtra("currentUser", user);
                    startActivityForResult(intent, REQUEST_LOGOUT);
                    return true;
            }

            return false;
        }
    }
}
