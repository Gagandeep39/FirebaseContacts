package com.gagandeep.nuvococontacts;

import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.design.widget.BottomNavigationView;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentManager;
import android.support.v7.app.AppCompatActivity;
import android.view.MenuItem;

import com.google.firebase.database.FirebaseDatabase;

//implement the interface OnNavigationItemSelectedListener in your activity class
public class MainActivity extends AppCompatActivity implements BottomNavigationView.OnNavigationItemSelectedListener {
	static boolean calledAlready = false;
	FirebaseDatabase database;
    final Fragment fragment1 = new RecentFragment();
    final Fragment fragment2 = new SearchFragment();
    final Fragment fragment3 = new ProfileFragment();
    final FragmentManager fm = getSupportFragmentManager();
    Fragment active = fragment1;

	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

		BottomNavigationView navigation = findViewById(R.id.navigation);
        fm.beginTransaction().add(R.id.main_container, fragment3, "3").hide(fragment3).commit();


        fm.beginTransaction().add(R.id.main_container, fragment2, "2").commit();
        fm.beginTransaction().add(R.id.main_container, fragment1, "1").hide(fragment1).commit();
		navigation.setOnNavigationItemSelectedListener(this);
		navigation.setSelectedItemId(R.id.search);
	}


	@Override
	public boolean onNavigationItemSelected(@NonNull MenuItem item) {
		switch (item.getItemId()) {
			case R.id.favourite:
                fm.beginTransaction().hide(active).show(fragment1).commit();
                active = fragment1;
//				if (!(getSupportFragmentManager().findFragmentById(R.id.main_container) instanceof RecentFragment))
//					active = new RecentFragment();
//					loadFragment(active);

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



	private boolean loadFragment(Fragment fragment) {
		//switching fragment
		if (fragment != null) {
			getSupportFragmentManager()
					.beginTransaction()
					.replace(R.id.main_container, fragment, fragment.getTag())
					.commit();
			return true;
		}

		return false;
	}
}