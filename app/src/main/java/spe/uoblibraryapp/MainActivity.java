package spe.uoblibraryapp;

import android.os.Bundle;
import android.support.v4.view.ViewPager;
import android.support.design.widget.NavigationView;
import android.support.v4.view.GravityCompat;
import android.support.v4.widget.DrawerLayout;
import android.support.v7.app.ActionBarDrawerToggle;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import android.view.Menu;
import android.view.MenuItem;

public class MainActivity extends AppCompatActivity implements NavigationView.OnNavigationItemSelectedListener {

    private static final String TAG = "MainActivity";
    private CustomPagerAdapter mAdapter;
    private ViewPager mViewPager;
    private int lastPage;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        Toolbar toolbar = (Toolbar) findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);

        DrawerLayout drawer = (DrawerLayout) findViewById(R.id.drawer_layout);
        ActionBarDrawerToggle toggle = new ActionBarDrawerToggle(
                this, drawer, toolbar, R.string.navigation_drawer_open, R.string.navigation_drawer_close);
        drawer.addDrawerListener(toggle);
        toggle.syncState();

        NavigationView navigationView = (NavigationView) findViewById(R.id.nav_view);
        navigationView.setCheckedItem(R.id.nav_current_loans_reservations);
        navigationView.setNavigationItemSelectedListener(this);

        mViewPager = (ViewPager) findViewById(R.id.container);
        setupViewPager(mViewPager);
        mViewPager.setCurrentItem(1);
        lastPage = mViewPager.getCurrentItem();
    }

    @Override
    public void onBackPressed() {
        DrawerLayout drawer = (DrawerLayout) findViewById(R.id.drawer_layout);
        if (drawer.isDrawerOpen(GravityCompat.START)) {
            drawer.closeDrawer(GravityCompat.START);
        } else {
            mViewPager.setCurrentItem(lastPage);
        }
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        // Inflate the menu; this adds items to the action bar if it is present.
        getMenuInflater().inflate(R.menu.main, menu);
        return true;
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        // Handle action bar item clicks here. The action bar will
        // automatically handle clicks on the Home/Up button, so long
        // as you specify a parent activity in AndroidManifest.xml.
        int id = item.getItemId();

        //noinspection SimplifiableIfStatement
        if (id == R.id.action_settings) {
            return true;
        }

        return super.onOptionsItemSelected(item);
    }

    @Override
    public boolean onNavigationItemSelected(MenuItem item) {
        // Handle navigation view item clicks here.
        int id = item.getItemId();

        if (id == R.id.nav_scanBook) {
            setViewPager("Scan");
        } else if (id == R.id.nav_current_loans_reservations) {
            setViewPager("Loans");
        } else if (id == R.id.nav_fines) {
            setViewPager("Fines");
        } else if (id == R.id.nav_loanhistory) {
            setViewPager("LoanHistory");
        } else if (id == R.id.nav_settings) {
            setViewPager("Settings");
        }

        DrawerLayout drawer = (DrawerLayout) findViewById(R.id.drawer_layout);
        drawer.closeDrawer(GravityCompat.START);
        return true;
    }

    private void setupViewPager(ViewPager viewPager){
        mAdapter  = new CustomPagerAdapter(getSupportFragmentManager());
        mAdapter.addFragment(new ScanFragment(), "Scan");
        mAdapter.addFragment(new LoansFragment(), "Loans");
        mAdapter.addFragment(new ReservationsFragment(), "Reservations");
        mAdapter.addFragment(new FinesFragment(), "Fines");
        mAdapter.addFragment(new LoanHistoryFragment(), "LoanHistory");
        mAdapter.addFragment(new SettingsFragment(), "Settings");
        viewPager.setAdapter(mAdapter);
        lastPage = mViewPager.getCurrentItem();
    }


    public void setViewPager(String fragmentName){
            if(mAdapter.fragmentExists(fragmentName))
                mViewPager.setCurrentItem(mAdapter.getFragmentIndex(fragmentName));
            //TODO: Think of something to return when programmer can't type correctly :)
    }
}
