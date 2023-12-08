package com.technexus.solutions.soft.translator_activities;

import android.os.Bundle;
import android.view.MenuItem;
import android.view.View;
import android.widget.RelativeLayout;

import androidx.appcompat.app.AppCompatActivity;
import androidx.fragment.app.Fragment;
import androidx.fragment.app.FragmentTransaction;

import com.google.android.material.bottomnavigation.BottomNavigationView;
import com.technexus.solutions.soft.R;
import com.technexus.solutions.soft.ads.AdClass;
import com.technexus.solutions.soft.ads.SharePrefs;
import com.technexus.solutions.soft.ads.Utils;
import com.technexus.solutions.soft.translator_fragments.HistoryFragment;
import com.technexus.solutions.soft.translator_fragments.ImageTranslationFragment;
import com.technexus.solutions.soft.translator_fragments.MainFragment;
import com.technexus.solutions.soft.translator_fragments.VoiceFragment;
import com.technexus.solutions.soft.translator_utils.FragmentChangeListener;

public class HomeActivity extends AppCompatActivity implements FragmentChangeListener {
    BottomNavigationView bottomBar;
    boolean isHome;
    int adCount = 0;

    @Override
    protected void onCreate(Bundle bundle) {
        super.onCreate(bundle);
        setContentView(R.layout.activity_home);
        final MainFragment mainFragment = new MainFragment();
        final VoiceFragment voiceFragment = new VoiceFragment();
        final HistoryFragment historyFragment = new HistoryFragment();
        final ImageTranslationFragment imageTranslationFragment = new ImageTranslationFragment();
        FragmentTransaction beginTransaction = getSupportFragmentManager().beginTransaction();
        beginTransaction.replace(R.id.frameLayout, mainFragment);
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
                    AdClass.showInterAd(HomeActivity.this, new AdClass.OnInterClose() {
                        @Override
                        public void onInterClose() {
                            HomeActivity.this.adCount++;
                            FragmentTransaction beginTransaction2 = HomeActivity.this.getSupportFragmentManager().beginTransaction();
                            beginTransaction2.replace(R.id.frameLayout, historyFragment);
                            beginTransaction2.addToBackStack(null);
                            beginTransaction2.commit();
                            HomeActivity.this.isHome = false;
                        }
                    });
                    return true;
                } else if (itemId == R.id.home_item) {
                    AdClass.showInterAd(HomeActivity.this, new AdClass.OnInterClose() {
                        @Override
                        public void onInterClose() {
                            HomeActivity.this.adCount++;
                            FragmentTransaction beginTransaction3 = HomeActivity.this.getSupportFragmentManager().beginTransaction();
                            beginTransaction3.replace(R.id.frameLayout, mainFragment);
                            beginTransaction3.addToBackStack(null);
                            beginTransaction3.commit();
                            HomeActivity.this.isHome = true;
                        }
                    });

                    return true;
                } else if (itemId == R.id.settings) {
                    AdClass.showInterAd(HomeActivity.this, new AdClass.OnInterClose() {
                        @Override
                        public void onInterClose() {
                            HomeActivity.this.adCount++;
                            FragmentTransaction beginTransaction4 = HomeActivity.this.getSupportFragmentManager().beginTransaction();
                            beginTransaction4.replace(R.id.frameLayout, imageTranslationFragment);
                            beginTransaction4.addToBackStack(null);
                            beginTransaction4.commit();
                            HomeActivity.this.isHome = false;
                        }
                    });

                    return true;
                } else if (itemId == R.id.voice) {
                    AdClass.showInterAd(HomeActivity.this, new AdClass.OnInterClose() {
                        @Override
                        public void onInterClose() {
                            HomeActivity.this.adCount++;
                            FragmentTransaction beginTransaction5 = HomeActivity.this.getSupportFragmentManager().beginTransaction();
                            beginTransaction5.replace(R.id.frameLayout, voiceFragment);
                            beginTransaction5.addToBackStack(null);
                            beginTransaction5.commit();
                            HomeActivity.this.isHome = false;
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
        if (SharePrefs.getInstance(HomeActivity.this).getBoolean(Utils.BACK_PRESS_AD)) {
            AdClass.showInterAd(HomeActivity.this, new AdClass.OnInterClose() {
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
