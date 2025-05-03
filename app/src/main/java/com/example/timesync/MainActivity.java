package com.example.timesync;

import android.graphics.Color;
import android.os.Bundle;
import android.widget.Button;
import android.widget.TextView;
import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;
import java.util.ArrayList;
import java.util.List;
import com.example.timesync.CircularScoreView;
import android.view.View;
import android.view.LayoutInflater;
import android.widget.LinearLayout;
import android.widget.ImageView;
import android.view.Gravity;
import android.animation.ObjectAnimator;
import android.view.MenuItem;
import com.google.android.material.bottomnavigation.BottomNavigationView;
import android.view.animation.DecelerateInterpolator;
import android.graphics.Typeface;
import android.os.Handler;
import android.content.res.ColorStateList;
import android.view.ViewGroup;

/**
 * MainActivity displays the statistics dashboard with tabs, overview, and app usage list.
 */
public class MainActivity extends AppCompatActivity {

    private RecyclerView appUsageRecycler;
    private TextView tabOverview, tabCategories, title;
    private Button dateButton;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        setupTopBar();
        setupTabs();
        setupScoreChart();
        setupBottomNavigation();
        
        // Delay loading app list to ensure scrolling works properly
        new Handler().postDelayed(() -> {
            setupAppList();
            
            // Enable scrolling to see all apps
            androidx.core.widget.NestedScrollView scrollView = findViewById(R.id.appListScrollView);
            scrollView.setFocusable(true);
            scrollView.setFocusableInTouchMode(true);
            scrollView.setNestedScrollingEnabled(true);
            scrollView.setVerticalScrollBarEnabled(true);
            
            // Make sure scroll indicators are visible
            scrollView.setScrollbarFadingEnabled(false);
            scrollView.setOverScrollMode(View.OVER_SCROLL_ALWAYS);
            
            // Add a more noticeable scroll hint showing the Facebook entry
            new Handler().postDelayed(() -> {
                // First scroll to show Facebook entry
                scrollView.smoothScrollBy(0, 150);
                
                // Then after delay, scroll further to show more entries including Facebook Messenger
                new Handler().postDelayed(() -> {
                    scrollView.smoothScrollBy(0, 150);
                    
                    // Finally scroll back to top after showing everything
                    new Handler().postDelayed(() -> scrollView.smoothScrollTo(0, 0), 800);
                }, 800);
            }, 500);
        }, 100);
    }

    /**
     * Sets up the top bar with title and date button.
     */
    private void setupTopBar() {
        title = findViewById(R.id.statisticsTitle);
        title.setText("Statistics");

        dateButton = findViewById(R.id.dateButton);
        // You can set a click listener here for date selection if needed
    }

    /**
     * Sets up the custom tabs and their click listeners.
     */
    private void setupTabs() {
        tabOverview = findViewById(R.id.tabOverview);
        tabCategories = findViewById(R.id.tabCategories);

        tabOverview.setOnClickListener(v -> selectTab(true));
        tabCategories.setOnClickListener(v -> selectTab(false));

        // Default: Overview selected
        selectTab(true);
    }

    /**
     * Handles tab selection UI.
     * @param isOverview true for Overview, false for Categories
     */
    private void selectTab(boolean isOverview) {
        if (isOverview) {
            tabOverview.setTextColor(getResources().getColor(android.R.color.white));
            tabCategories.setTextColor(getResources().getColor(R.color.colorUnproductive));
            // TODO: Show overview content, hide categories
        } else {
            tabOverview.setTextColor(getResources().getColor(R.color.colorUnproductive));
            tabCategories.setTextColor(getResources().getColor(android.R.color.white));
            // TODO: Show categories content, hide overview
        }
        // TODO: Animate or move the tab indicator if desired
    }

    /**
     * Sets up the app usage list.
     */
    private void setupAppList() {
        // Get the container
        LinearLayout appListContainer = findViewById(R.id.appListContainer);
        appListContainer.removeAllViews();
        
        // Get app usage data - using lots of items to ensure scrolling
        List<AppUsage> appUsageList = getDummyAppUsageData();
        
        // Add a header to the app list - smaller to save space
        TextView header = new TextView(this);
        header.setText("App Usage");
        header.setTextSize(18); // Smaller text size
        header.setTextColor(Color.parseColor("#111B2E"));
        header.setPadding(16, 8, 16, 8);
        header.setTypeface(null, Typeface.BOLD);
        appListContainer.addView(header);
        
        // Create a LayoutInflater
        LayoutInflater inflater = LayoutInflater.from(this);
        
        // Add each app item to the container
        for (AppUsage app : appUsageList) {
            View itemView = inflater.inflate(R.layout.item_app_usage, appListContainer, false);
            
            // Find views within the item
            ImageView icon = itemView.findViewById(R.id.appIcon);
            TextView name = itemView.findViewById(R.id.appName);
            TextView time = itemView.findViewById(R.id.appTime);
            CircularProgressView progressCircle = itemView.findViewById(R.id.appProgressCircle);
            
            // Set data to views
            icon.setImageResource(app.iconRes);
            name.setText(app.appName);
            time.setText(app.usageTime);
            progressCircle.setProgress(app.productivity);
            
            // Add spacing between items to make them visually separated
            LinearLayout.LayoutParams params = new LinearLayout.LayoutParams(
                LinearLayout.LayoutParams.MATCH_PARENT,
                LinearLayout.LayoutParams.WRAP_CONTENT
            );
            params.setMargins(0, 4, 0, 4); // Small margins but enough to see separation
            itemView.setLayoutParams(params);
            
            // Add the view to container
            appListContainer.addView(itemView);
        }
        
        // No need for an extra footer that creates empty space
    }

    /**
     * Sets up the circular score chart.
     */
    private void setupScoreChart() {
        CircularScoreView scoreView = findViewById(R.id.scoreChart);
        scoreView.setScore(56.9f);
        scoreView.setProductiveColor(Color.parseColor("#2196F3"));
        scoreView.setUnproductiveColor(Color.parseColor("#F44336"));
    }

    /**
     * Dummy data for app usage list - reordering to show Facebook prominently
     */
    private List<AppUsage> getDummyAppUsageData() {
        List<AppUsage> list = new ArrayList<>();
        
        // Only keeping specific apps and putting Facebook near the top
        list.add(new AppUsage("Spotify", "02h 30m", R.drawable.ic_spotify, 20));
        list.add(new AppUsage("Facebook", "01h 25m", R.drawable.ic_facebook_white, 40));  // Moved up in list
        list.add(new AppUsage("Whatsapp", "02h 30m", R.drawable.ic_whatsapp, 30));
        list.add(new AppUsage("Instagram", "01h 45m", R.drawable.ic_instagram, 25));
        
        // Adding one more Facebook entry to ensure it's visible
        list.add(new AppUsage("Facebook Messenger", "00h 45m", R.drawable.ic_facebook_white, 35));
        
        return list;
    }

    /**
     * Sets up bottom navigation with manual icons for guaranteed visibility
     */
    private void setupBottomNavigation() {
        // Setup for our custom navigation bar icons
        ImageView navHome = findViewById(R.id.navHome);
        ImageView navStats = findViewById(R.id.navStats);
        ImageView navAdd = findViewById(R.id.navAdd);
        ImageView navCalendar = findViewById(R.id.navCalendar);
        ImageView navProfile = findViewById(R.id.navProfile);
        
        // Make stats icon selected initially (since we're on stats page)
        navStats.setBackgroundResource(R.drawable.nav_selected_background);
        
        // Set up click listeners for each icon
        navHome.setOnClickListener(v -> {
            // Home icon clicked
            highlightNavIcon(navHome);
        });
        
        navStats.setOnClickListener(v -> {
            // Stats icon clicked  
            highlightNavIcon(navStats);
        });
        
        navAdd.setOnClickListener(v -> {
            // Add icon clicked
            highlightNavIcon(navAdd);
        });
        
        navCalendar.setOnClickListener(v -> {
            // Calendar icon clicked
            highlightNavIcon(navCalendar);
        });
        
        navProfile.setOnClickListener(v -> {
            // Profile icon clicked
            highlightNavIcon(navProfile);
        });
        
        // Make sure custom navigation container is on top
        LinearLayout customNavContainer = findViewById(R.id.customNavContainer);
        customNavContainer.setZ(9999f);
        customNavContainer.bringToFront();
        
        // Original navbar - hide it
        BottomNavigationView bottomNavigationView = findViewById(R.id.bottomNav);
        bottomNavigationView.setVisibility(View.GONE);
    }
    
    /**
     * Highlights the selected navigation icon and clears others
     */
    private void highlightNavIcon(ImageView selectedIcon) {
        // Remove highlight from all icons
        findViewById(R.id.navHome).setBackgroundResource(0);
        findViewById(R.id.navStats).setBackgroundResource(0);
        findViewById(R.id.navAdd).setBackgroundResource(0);
        findViewById(R.id.navCalendar).setBackgroundResource(0);
        findViewById(R.id.navProfile).setBackgroundResource(0);
        
        // Add highlight to selected icon
        selectedIcon.setBackgroundResource(R.drawable.nav_selected_background);
        
        // Add animation effect
        ObjectAnimator.ofFloat(selectedIcon, "scaleX", 1f, 1.2f, 1f).setDuration(300).start();
        ObjectAnimator.ofFloat(selectedIcon, "scaleY", 1f, 1.2f, 1f).setDuration(300).start();
    }
}