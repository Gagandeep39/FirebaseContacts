package com.gagandeep.nuvococontacts;

import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.design.widget.BottomNavigationView;
import android.support.v4.app.Fragment;
import android.support.v7.app.AppCompatActivity;
import android.view.MenuItem;

import com.google.firebase.database.FirebaseDatabase;

//implement the interface OnNavigationItemSelectedListener in your activity class
public class MainActivity extends AppCompatActivity implements BottomNavigationView.OnNavigationItemSelectedListener {
	static boolean calledAlready = false;
	FirebaseDatabase database;
	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);


        setContentView(R.layout.activity_main);
        //loading the default fragment
//        FirebaseApp.initializeApp(this);


        loadFragment(new SearchFragment());

        //getting bottom navigation view and attaching the listener
		BottomNavigationView navigation = findViewById(R.id.navigation);
		navigation.setOnNavigationItemSelectedListener(this);
		navigation.setSelectedItemId(R.id.search);
	}

	Fragment recentFragment = null, profileFragment = null, searchFragment = null;
	int recentCounter = 0, profileCounter = 0, searchCounter = 0;


	@Override
	public boolean onNavigationItemSelected(@NonNull MenuItem item) {
        Fragment fragment = null;
        Fragment currentFragment = getSupportFragmentManager().findFragmentById(R.id.container);
		switch (item.getItemId()) {
			case R.id.favourite:
				if (!(currentFragment instanceof RecentFragment))
					fragment = new RecentFragment();


                break;

			case R.id.search:
                if (!(currentFragment instanceof SearchFragment))
                    fragment = new SearchFragment();

				break;

			case R.id.profile:
                if (!(currentFragment instanceof ProfileFragment))
                    fragment = new ProfileFragment();

				break;

        }

        return loadFragment(fragment);
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