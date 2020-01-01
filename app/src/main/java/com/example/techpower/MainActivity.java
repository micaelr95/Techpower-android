package com.example.techpower;

import androidx.annotation.NonNull;
import androidx.appcompat.app.ActionBarDrawerToggle;
import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.util.Log;
import android.view.Menu;
import android.view.MenuItem;
import androidx.appcompat.widget.Toolbar;
import androidx.core.view.GravityCompat;
import androidx.drawerlayout.widget.DrawerLayout;
import androidx.fragment.app.Fragment;
import androidx.fragment.app.FragmentManager;

import com.example.techpower.models.Category;
import com.example.techpower.models.SingletonStore;
import com.google.android.material.navigation.NavigationView;

import java.util.ArrayList;

public class MainActivity extends AppCompatActivity implements NavigationView.OnNavigationItemSelectedListener {

    private DrawerLayout mDrawerLayout;
    private FragmentManager mFragmentManager;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        Toolbar toolbar = findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);

        NavigationView navigationView = findViewById(R.id.nav_view);
        mDrawerLayout = findViewById(R.id.drawer);

        Menu menu = navigationView.getMenu();

        SingletonStore.getInstance(getApplicationContext()).getAllCategoriesAPI(getApplicationContext(), SingletonStore.isConnectedInternet(getApplicationContext()));
        // Add category submenu
        ArrayList<Category> categoryList = SingletonStore.getInstance(getApplicationContext()).getCategoriesDB();
        Menu categoriesSubMenu = menu.addSubMenu(Menu.NONE, Menu.NONE, 0, R.string.nav_categories);
        for (Category category: categoryList) {
            categoriesSubMenu.add(Menu.NONE, category.getId(), Menu.NONE,category.getName());
        }
        navigationView.invalidate();

        // Add account submenu
        SharedPreferences preferences = getSharedPreferences(getString(R.string.app_preferences), MODE_PRIVATE);
        Menu accountSubMenu = menu.addSubMenu(Menu.NONE, Menu.NONE, 1,"Account");
        if (preferences.getString("authkey", null) == null) {
            accountSubMenu.add(Menu.NONE, R.string.nav_login, Menu.NONE, R.string.nav_login);
            accountSubMenu.add(Menu.NONE, R.string.nav_signup, Menu.NONE, R.string.nav_signup);
        } else {
            accountSubMenu.add(Menu.NONE, R.string.nav_logout, Menu.NONE, R.string.nav_logout);
        }
        navigationView.invalidate();

        ActionBarDrawerToggle toggle = new ActionBarDrawerToggle(this, mDrawerLayout, toolbar, R.string.navigation_drawer_open, R.string.navigation_drawer_close);
        toggle.syncState();
        mDrawerLayout.addDrawerListener(toggle);
        navigationView.setNavigationItemSelectedListener(this);

        mFragmentManager = getSupportFragmentManager();

        // TODO: Show user data on header

        Fragment fragment = new ProductListFragment();
        setTitle("Techpower");
        mFragmentManager.beginTransaction().replace(R.id.contentFragment, fragment).commit();
    }

    @Override
    public boolean onNavigationItemSelected(@NonNull MenuItem menuItem) {

        switch (menuItem.getItemId()) {
            case R.string.nav_login:
                Intent login_intent = new Intent(this, LoginActivity.class);
                startActivity(login_intent);
                break;

            case R.string.nav_signup:
                Intent signup_intent = new Intent(this, SignUpActivity.class);
                startActivity(signup_intent);
                break;

            case R.string.nav_logout:
                // TODO: Remove user data
                break;

            case R.id.nav_settings:
                Intent settings_intent = new Intent(this, SettingsActivity.class);
                startActivity(settings_intent);
                break;
        }

        mDrawerLayout.closeDrawer(GravityCompat.START);

        return true;
    }
}
