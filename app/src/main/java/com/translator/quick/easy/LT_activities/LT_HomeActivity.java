package com.translator.quick.easy.LT_activities;

import android.os.Bundle;
import android.view.MenuItem;
import android.view.View;
import android.widget.RelativeLayout;

import androidx.appcompat.app.AppCompatActivity;
import androidx.fragment.app.Fragment;
import androidx.fragment.app.FragmentTransaction;

import com.google.android.material.bottomnavigation.BottomNavigationView;
import com.translator.quick.easy.R;
import com.translator.quick.easy.LT_ads.LT_AdClass;
import com.translator.quick.easy.LT_ads.LT_SharePrefs;
import com.translator.quick.easy.LT_ads.LT_Utils;
import com.translator.quick.easy.LT_fragments.LT_HistoryFragment;
import com.translator.quick.easy.LT_fragments.LT_ImageTranslationFragment;
import com.translator.quick.easy.LT_fragments.LT_MainFragment;
import com.translator.quick.easy.LT_fragments.LT_VoiceFragment;
import com.translator.quick.easy.LT_utils.LT_FragmentChangeListener;

public class LT_HomeActivity extends AppCompatActivity implements LT_FragmentChangeListener {
    BottomNavigationView bottomBar;
    boolean isHome;
    int adCount = 0;

    @Override
    protected void onCreate(Bundle bundle) {
        super.onCreate(bundle);
        setContentView(R.layout.activity_home);
        final LT_MainFragment LTMainFragment = new LT_MainFragment();
        final LT_VoiceFragment LTVoiceFragment = new LT_VoiceFragment();
        final LT_HistoryFragment LTHistoryFragment = new LT_HistoryFragment();
        final LT_ImageTranslationFragment LTImageTranslationFragment = new LT_ImageTranslationFragment();
        FragmentTransaction beginTransaction = getSupportFragmentManager().beginTransaction();
        beginTransaction.replace(R.id.frameLayout, LTMainFragment);
        beginTransaction.addToBackStack(null);
        beginTransaction.commit();
        this.isHome = true;
        BottomNavigationView bottomNavigationView = (BottomNavigationView) findViewById(R.id.bottomNav);
        this.bottomBar = bottomNavigationView;
        bottomNavigationView.setOnNavigationItemSelectedListener(new BottomNavigationView.OnNavigationItemSelectedListener() {
            @Override
            public boolean onNavigationItemSelected(MenuItem menuItem) {
                int itemId = menuItem.getItemId();
                if (itemId == R.id.history) {
                    LT_AdClass.showInterAd(LT_HomeActivity.this, new LT_AdClass.OnInterClose() {
                        @Override
                        public void onInterClose() {
                            LT_HomeActivity.this.adCount++;
                            FragmentTransaction beginTransaction2 = LT_HomeActivity.this.getSupportFragmentManager().beginTransaction();
                            beginTransaction2.replace(R.id.frameLayout, LTHistoryFragment);
                            beginTransaction2.addToBackStack(null);
                            beginTransaction2.commit();
                            LT_HomeActivity.this.isHome = false;
                        }
                    });
                    return true;
                } else if (itemId == R.id.home_item) {
                    LT_AdClass.showInterAd(LT_HomeActivity.this, new LT_AdClass.OnInterClose() {
                        @Override
                        public void onInterClose() {
                            LT_HomeActivity.this.adCount++;
                            FragmentTransaction beginTransaction3 = LT_HomeActivity.this.getSupportFragmentManager().beginTransaction();
                            beginTransaction3.replace(R.id.frameLayout, LTMainFragment);
                            beginTransaction3.addToBackStack(null);
                            beginTransaction3.commit();
                            LT_HomeActivity.this.isHome = true;
                        }
                    });

                    return true;
                } else if (itemId == R.id.settings) {
                    LT_AdClass.showInterAd(LT_HomeActivity.this, new LT_AdClass.OnInterClose() {
                        @Override
                        public void onInterClose() {
                            LT_HomeActivity.this.adCount++;
                            FragmentTransaction beginTransaction4 = LT_HomeActivity.this.getSupportFragmentManager().beginTransaction();
                            beginTransaction4.replace(R.id.frameLayout, LTImageTranslationFragment);
                            beginTransaction4.addToBackStack(null);
                            beginTransaction4.commit();
                            LT_HomeActivity.this.isHome = false;
                        }
                    });

                    return true;
                } else if (itemId == R.id.voice) {
                    LT_AdClass.showInterAd(LT_HomeActivity.this, new LT_AdClass.OnInterClose() {
                        @Override
                        public void onInterClose() {
                            LT_HomeActivity.this.adCount++;
                            FragmentTransaction beginTransaction5 = LT_HomeActivity.this.getSupportFragmentManager().beginTransaction();
                            beginTransaction5.replace(R.id.frameLayout, LTVoiceFragment);
                            beginTransaction5.addToBackStack(null);
                            beginTransaction5.commit();
                            LT_HomeActivity.this.isHome = false;
                        }
                    });
                    return true;
                }
                return false;
            }
        });
        final RelativeLayout relativeLayout = (RelativeLayout) findViewById(R.id.backPressedLayout);
        relativeLayout.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                relativeLayout.setVisibility(View.GONE);
            }
        });
    }

    @Override
    public void replaceFragment(Fragment fragment) {
        FragmentTransaction beginTransaction = getSupportFragmentManager().beginTransaction();
        beginTransaction.replace(R.id.frameLayout, fragment);
        this.bottomBar.getMenu().findItem(R.id.home_item).setChecked(true);
        beginTransaction.addToBackStack(null);
        beginTransaction.commit();
        this.isHome = true;
    }

    @Override
    public void onBackPressed() {
        if (LT_SharePrefs.getInstance(LT_HomeActivity.this).getBoolean(LT_Utils.BACK_PRESS_AD)) {
            LT_AdClass.showInterAd(LT_HomeActivity.this, new LT_AdClass.OnInterClose() {
                @Override
                public void onInterClose() {
                    finish();
                }
            });
        } else {
            finish();
        }
    }


    @Override
    protected void onDestroy() {
        super.onDestroy();
    }
}
