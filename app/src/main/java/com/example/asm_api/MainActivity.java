package com.example.asm_api;

import android.content.DialogInterface;
import android.content.Intent;
import android.os.Bundle;
import android.view.MenuItem;
import android.view.View;
import android.widget.TextView;
import android.widget.Toast;

import androidx.activity.EdgeToEdge;
import androidx.annotation.NonNull;
import androidx.appcompat.app.ActionBarDrawerToggle;
import androidx.appcompat.app.AlertDialog;
import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.Toolbar;
import androidx.core.graphics.Insets;
import androidx.core.view.ViewCompat;
import androidx.core.view.WindowInsetsCompat;
import androidx.drawerlayout.widget.DrawerLayout;
import androidx.fragment.app.Fragment;
import androidx.fragment.app.FragmentManager;

import com.example.asm_api.Fragment.CartFragment;
import com.example.asm_api.Fragment.CategoryFragment;
import com.example.asm_api.Fragment.FavouriteFragment;
import com.example.asm_api.Fragment.HomeFragment;
import com.example.asm_api.Fragment.ProductFragment;
import com.example.asm_api.Fragment.ProfileFragment;
import com.example.asm_api.view.LoginActivity;
import com.google.android.material.bottomnavigation.BottomNavigationView;
import com.google.android.material.navigation.NavigationBarView;
import com.google.android.material.navigation.NavigationView;

public class MainActivity extends AppCompatActivity {
    DrawerLayout drawerLayout;
    Toolbar toolbar;
    NavigationView nav;
    BottomNavigationView bottomNavigationView;
    View view;
    TextView tvuser;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        EdgeToEdge.enable(this);
        setContentView(R.layout.activity_main);
        ViewCompat.setOnApplyWindowInsetsListener(findViewById(R.id.main), (v, insets) -> {
            Insets systemBars = insets.getInsets(WindowInsetsCompat.Type.systemBars());
            v.setPadding(systemBars.left, systemBars.top, systemBars.right, systemBars.bottom);
            return insets;
        });
        drawerLayout = findViewById(R.id.main);
        toolbar = findViewById(R.id.toolbar);
        nav = findViewById(R.id.nav);
        bottomNavigationView = findViewById(R.id.botomMenu);
//đổi màu title Toolbar
        toolbar.setTitleTextColor(getResources().getColor(R.color.white));
// gán toolbar
        setSupportActionBar(toolbar);
        ActionBarDrawerToggle toggle = new ActionBarDrawerToggle(this, drawerLayout, toolbar, R.string.open, R.string.close);
        drawerLayout.addDrawerListener(toggle);
        toggle.syncState();
        //sét mẫu icon về bản gốc
        nav.setItemIconTintList(null);
        view = nav.getHeaderView(0);
        tvuser = view.findViewById(R.id.tvWellcome);
        Intent intent = getIntent();
        String user = intent.getStringExtra("user");

        HomeFragment homeFragment = new HomeFragment();
        replaceFrg(homeFragment);
        nav.setNavigationItemSelectedListener(new NavigationView.OnNavigationItemSelectedListener() {
            @Override
            public boolean onNavigationItemSelected(@NonNull MenuItem item) {
                if (item.getItemId() == R.id.product) {
                    ProductFragment productFragment = new ProductFragment();
                    replaceFrg(productFragment);
                } else if (item.getItemId() == R.id.category) {
                    CategoryFragment categoryFragment = new CategoryFragment();
                    replaceFrg(categoryFragment);
                } else if (item.getItemId() == R.id.profile) {
                    ProfileFragment profileFragment = new ProfileFragment();
                    replaceFrg(profileFragment);
                } else if (item.getItemId() == R.id.DoiMk) {
//                    ChangePassFragment changePassFragment = new ChangePassFragment();
//                    replaceFrg(changePassFragment);

                } else {
                    AlertDialog.Builder builder = new AlertDialog.Builder(MainActivity.this);
                    builder.setTitle("Cảnh báo");
                    builder.setIcon(R.drawable.baseline_warning_24);
                    builder.setMessage("Bạn có muốn đăng xuất k?");
                    builder.setPositiveButton("Có", new DialogInterface.OnClickListener() {
                        @Override
                        public void onClick(DialogInterface dialog, int which) {
                            Toast.makeText(MainActivity.this, "Đăng xuất thành công !", Toast.LENGTH_SHORT).show();
                            Intent intent = new Intent(MainActivity.this, LoginActivity.class);
                            startActivity(intent);
                        }
                    });
                    builder.setNegativeButton("Không", new DialogInterface.OnClickListener() {
                        @Override
                        public void onClick(DialogInterface dialog, int which) {

                        }
                    });
                    builder.show();
                }
                getSupportActionBar().setTitle(item.getTitle()); //khi click vào item hiển thị tieu de lên toolbar
                drawerLayout.close(); //đóng nav
                return true;
            }
        });
        bottomNavigationView.setOnItemSelectedListener(new NavigationBarView.OnItemSelectedListener() {
            @Override
            public boolean onNavigationItemSelected(@NonNull MenuItem item) {
                if (item.getItemId() == R.id.home) {
                    HomeFragment homeFragment = new HomeFragment();
                    replaceFrg(homeFragment);
                } else if (item.getItemId() == R.id.favourite) {
                    FavouriteFragment favouriteFragment = new FavouriteFragment();
                    replaceFrg(favouriteFragment);
                } else {
                    CartFragment cartFragment = new CartFragment();
                    replaceFrg(cartFragment);
                }
                getSupportActionBar().setTitle(item.getTitle());
                return true;
            }
        });
    }

    public void replaceFrg(Fragment frg) {
        FragmentManager fm = getSupportFragmentManager();
        fm.beginTransaction().replace(R.id.frmnav, frg).commit();
    }

}