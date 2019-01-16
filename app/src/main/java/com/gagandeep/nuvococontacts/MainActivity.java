package com.gagandeep.nuvococontacts;

import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.design.widget.BottomNavigationView;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentManager;
import android.support.v7.app.AppCompatActivity;
import android.view.MenuItem;


//implement the interface OnNavigationItemSelectedListener in your activity class
public class MainActivity extends AppCompatActivity implements BottomNavigationView.OnNavigationItemSelectedListener {

    //Create an Instance of Each Fragment
    final Fragment fragment1 = new FavouriteFragment();
    final Fragment fragment2 = new SearchFragment();
    final Fragment fragment3 = new ProfileFragment();
    final FragmentManager fm = getSupportFragmentManager();
    Fragment active = fragment1;        //object for currently active fragment

	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

		BottomNavigationView navigation = findViewById(R.id.navigation);

        //Show 1 fragment and hide other
        fm.beginTransaction().add(R.id.main_container, fragment3, "3").hide(fragment3).commit();
        fm.beginTransaction().add(R.id.main_container, fragment2, "2").commit();
        fm.beginTransaction().add(R.id.main_container, fragment1, "1").hide(fragment1).commit();
		navigation.setOnNavigationItemSelectedListener(this);
		navigation.setSelectedItemId(R.id.search);
	}

    //To show a fragment on selecting one of the bottom button
	@Override
	public boolean onNavigationItemSelected(@NonNull MenuItem item) {
		switch (item.getItemId()) {
			case R.id.favourite:
                fm.beginTransaction().hide(active).show(fragment1).commit();
                active = fragment1;
                return true;

			case R.id.search:
                fm.beginTransaction().hide(active).show(fragment2).commit();
                active = fragment2;
                return true;

			case R.id.profile:
                fm.beginTransaction().hide(active).show(fragment3).commit();
                active = fragment3;

                return true;
        }
        return false;
	}


}