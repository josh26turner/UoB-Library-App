package spe.uoblibraryapp;

import android.content.Intent;
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

public class ActivityHome extends AppCompatActivity implements NavigationView.OnNavigationItemSelectedListener {

    private static final String TAG = "MainActivity";
    private CustomPagerAdapter mAdapter;
    private ViewPager mViewPager;
    private int lastPage;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_home);

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
        mViewPager.setCurrentItem(0);
        setTitle(mAdapter.getFragmentTitle(0));
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
            //start activity here.
            Intent NFCActivity = new Intent(getApplicationContext(), ActivityScanNFC.class);
            startActivity(NFCActivity);
        } else if (id == R.id.nav_current_loans_reservations) {
            setViewPager("Loans");
        } else if (id == R.id.nav_fines) {
            setViewPager("Fines");
        } else if (id == R.id.nav_loanhistory) {
            setViewPager("Loans History");
        } else if (id == R.id.nav_settings) {
            setViewPager("Settings");
        }

        DrawerLayout drawer = (DrawerLayout) findViewById(R.id.drawer_layout);
        drawer.closeDrawer(GravityCompat.START);
        return true;
    }

    private void setupViewPager(ViewPager viewPager){
        mAdapter  = new CustomPagerAdapter(getSupportFragmentManager());
        mAdapter.addFragment(new FragmentLoans(), "Loans");
        mAdapter.addFragment(new FragmentReservations(), "Reservations");
        mAdapter.addFragment(new FragmentFines(), "Fines");
        mAdapter.addFragment(new FragmentLoanHistory(), "Loans History");
        mAdapter.addFragment(new FragmentSettings(), "Settings");
        viewPager.setAdapter(mAdapter);
        lastPage = mViewPager.getCurrentItem();
    }

    public void setViewPager(String fragmentName){
        int index;
        if(mAdapter.fragmentExists(fragmentName)) {
            index = mAdapter.getFragmentIndex(fragmentName);
            //Show Appropriate Title
            setTitle(fragmentName);
            mViewPager.setCurrentItem(index);
        }
        else{
            //TODO: Think of something to return when programmer can't type correctly :)
        }
    }
    
}
