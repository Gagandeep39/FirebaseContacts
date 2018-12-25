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


	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		FirebaseDatabase.getInstance().setPersistenceEnabled(true);
		setContentView(R.layout.activity_main);
		//loading the default fragment
//        FirebaseApp.initializeApp(this);


		loadFragment(new RecentFragment());

		//getting bottom navigation view and attaching the listener
		BottomNavigationView navigation = findViewById(R.id.navigation);
		navigation.setOnNavigationItemSelectedListener(this);
	}


	@Override
	public boolean onNavigationItemSelected(@NonNull MenuItem item) {
		Fragment fragment = null;
		Fragment currentFragment = getSupportFragmentManager().findFragmentById(R.id.container);
		switch (item.getItemId()) {
			case R.id.recents:
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
					.replace(R.id.container, fragment)
					.commit();
			return true;
		}
		return false;
	}
}