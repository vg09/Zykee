package com.example.omarla.food2u_repo;

import android.content.Intent;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.os.Bundle;
import android.os.Handler;
import android.support.design.widget.NavigationView;
import android.support.v4.view.GravityCompat;
import android.support.v4.view.ViewPager;
import android.support.v4.widget.DrawerLayout;
import android.support.v7.app.ActionBarDrawerToggle;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import android.util.Base64;
import android.util.Log;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.view.View.OnClickListener;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import java.util.ArrayList;
import java.util.Timer;
import java.util.TimerTask;

import de.hdodenhof.circleimageview.CircleImageView;
import me.relex.circleindicator.CircleIndicator;


public class CustomerDashboard extends AppCompatActivity  implements NavigationView.OnNavigationItemSelectedListener {
      ImageView  imageView1;
      ImageView  imageView2;
      ImageView  imageView3;
      ImageView  imageView4;
      ImageView  imageView5;
      ImageView  imageView6;
      Bitmap bitmap1;
      TextView txt_name,txt_mail;
      CircleImageView imgview;
      String U_type;


    private static ViewPager mPager;
    private static int currentPage = 0;
    private static final Integer[] XMEN = { R.drawable.pizza,R.drawable.pastry,R.drawable.parantha, R.drawable.coffe,R.drawable.chopbox,R.drawable.thali};
    private ArrayList<Integer> XMENArray = new ArrayList<Integer>();

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_customer_dashboard);
        Toolbar toolbar = (Toolbar) findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);

        String v_name=SharedGetterSetter.getUserName(getApplicationContext());
        String v_mail=SharedGetterSetter.getUserMail(getApplicationContext());
        String v_img=SharedGetterSetter.getImgUrl(getApplicationContext());
        Bitmap bitmap2=decodeImage(v_img);




        U_type=SharedGetterSetter.getUserType(getApplicationContext());



        if (SharedGetterSetter.getUserName(CustomerDashboard.this).length() == 0) {
            Intent intent = new Intent(getApplicationContext(), LogIn.class);
            startActivity(intent);
        } else {

            init();

            DrawerLayout drawer = (DrawerLayout) findViewById(R.id.drawer_layout);
            ActionBarDrawerToggle toggle = new ActionBarDrawerToggle(
                    this, drawer, toolbar, R.string.navigation_drawer_open, R.string.navigation_drawer_close);
            drawer.addDrawerListener(toggle);
            toggle.syncState();

            NavigationView navigationView = (NavigationView) findViewById(R.id.nav_view);
            View hView =  navigationView.getHeaderView(0);
            txt_name=(TextView)hView.findViewById(R.id.headername);
            txt_mail=(TextView)hView.findViewById(R.id.headerMail);
            imgview=(CircleImageView) hView.findViewById(R.id.headerImage);



            txt_name.setText(v_name);
            txt_mail.setText(v_mail);
            imgview.setImageBitmap(bitmap2);
            imgview.setOnClickListener(new OnClickListener() {
                @Override
                public void onClick(View v) {
                    Intent intent=new Intent(getApplicationContext(),EditProfile.class);
                    startActivity(intent);
                }
            });

            Menu menu = navigationView.getMenu();

            // find MenuItem you want to change
            MenuItem LogButton = menu.findItem(R.id.logbtn);
            if (SharedGetterSetter.getUserName(getApplicationContext()).length() > 0)
                // set new title to the MenuItem
                LogButton.setTitle("Logout");
            navigationView.setNavigationItemSelectedListener(this);


            final String[] foodcourtname = {"BH-1 FoodCourt", "BH-3 FoodCourt", "Campus Cafe FoodCourt", "34-Block FoodCourt", "Mall_FoodCourt", "Other FoodCourt"};

            imageView1 = (ImageView) findViewById(R.id.sci1);
            imageView1.setOnClickListener(new OnClickListener() {
                @Override
                public void onClick(View v) {
                    Intent intent = new Intent(getApplicationContext(), Stalls_common.class);

                    intent.putExtra("text", foodcourtname[0]);
                    intent.putExtra("location", "bh1");
                    startActivity(intent);
                }
            });
            imageView2 = (ImageView) findViewById(R.id.sci2);
            imageView2.setOnClickListener(new OnClickListener() {
                @Override
                public void onClick(View v) {
                    Intent intent = new Intent(getApplicationContext(), Stalls_common.class);

                    intent.putExtra("text", foodcourtname[1]);
                    intent.putExtra("location", "bh3");
                    startActivity(intent);
                }
            });

            imageView3 = (ImageView) findViewById(R.id.sci3);
            imageView3.setOnClickListener(new OnClickListener() {
                @Override
                public void onClick(View v) {
                    Intent intent = new Intent(getApplicationContext(), Stalls_common.class);

                    intent.putExtra("text", foodcourtname[2]);
                    intent.putExtra("location", "34");
                    startActivity(intent);
                }
            });
            imageView4 = (ImageView) findViewById(R.id.sci4);
            imageView4.setOnClickListener(new OnClickListener() {
                @Override
                public void onClick(View v) {
                    Intent intent = new Intent(getApplicationContext(), Stalls_common.class);

                    intent.putExtra("text", foodcourtname[3]);
                    intent.putExtra("location", "cc");
                    startActivity(intent);
                }
            });


            imageView5 = (ImageView) findViewById(R.id.sci5);
            imageView5.setOnClickListener(new OnClickListener() {
                @Override
                public void onClick(View v) {
                    Intent intent = new Intent(getApplicationContext(), Stalls_common.class);

                    intent.putExtra("text", foodcourtname[4]);
                    intent.putExtra("location", "mall");
                    startActivity(intent);
                }
            });


            imageView6 = (ImageView) findViewById(R.id.sci6);
            imageView6.setOnClickListener(new OnClickListener() {
                @Override
                public void onClick(View v) {
                    Intent intent = new Intent(getApplicationContext(), Stalls_common.class);

                    intent.putExtra("text", foodcourtname[5]);
                    intent.putExtra("location", "other");
                    startActivity(intent);
                }
            });
        }
    }

    private void init() {
        for(int i=0;i<XMEN.length;i++)
            XMENArray.add(XMEN[i]);

        mPager = (ViewPager) findViewById(R.id.pager);
        mPager.setAdapter(new MySliderAdapter(CustomerDashboard.this,XMENArray));
        CircleIndicator indicator = (CircleIndicator) findViewById(R.id.indicator);
        indicator.setViewPager(mPager);

        // Auto start of viewpager
        final Handler handler = new Handler();
        final Runnable Update = new Runnable() {
            public void run() {
                if (currentPage == XMEN.length) {
                    currentPage = 0;
                }
                mPager.setCurrentItem(currentPage++, true);
            }
        };
        Timer swipeTimer = new Timer();
        swipeTimer.schedule(new TimerTask() {
            @Override
            public void run() {
                handler.post(Update);
            }
        }, 40000, 40000);
    }
        @Override
        public void onBackPressed () {
            DrawerLayout drawer = (DrawerLayout) findViewById(R.id.drawer_layout);
            if (drawer.isDrawerOpen(GravityCompat.START)) {
                drawer.closeDrawer(GravityCompat.START);
            } else {
                Intent startMain = new Intent(Intent.ACTION_MAIN);
                startMain.addCategory(Intent.CATEGORY_HOME);
                startMain.setFlags(Intent.FLAG_ACTIVITY_NEW_TASK);
                startActivity(startMain);

            }
        }




        @Override
        public boolean onCreateOptionsMenu (Menu menu){
            // Inflate the menu; this adds items to the action bar if it is present.
            getMenuInflater().inflate(R.menu.customer_dashboard, menu);
            return true;
        }

        @Override
        public boolean onOptionsItemSelected (MenuItem item){
            // Handle action bar item clicks here. The action bar will
            // automatically handle clicks on the Home/Up button, so long
            // as you specify a parent activity in AndroidManifest.xml.
            int id = item.getItemId();

            //noinspection SimplifiableIfStatement
            if (id == R.id.action_cart) {
              Intent intent=new Intent(getApplicationContext(),Cart.class);
              startActivity(intent);
            }

            return super.onOptionsItemSelected(item);
        }

        @SuppressWarnings("StatementWithEmptyBody")
        @Override
        public boolean onNavigationItemSelected (MenuItem item){
            // Handle navigation view item clicks here.
            int id = item.getItemId();

            if (id == R.id.account) {
                Intent intent=new Intent(CustomerDashboard.this,EditProfile.class);
                startActivity(intent);
            } else if (id == R.id.orders) {

                Intent i=new Intent(getApplicationContext(),HistoryActivity.class);
                startActivity(i);

            }else if(id==R.id.ven_option){
               String u_type= SharedGetterSetter.getUserType(CustomerDashboard.this);
              Intent intent=new Intent(getApplicationContext(),VendorDashboard.class);
                    startActivity(intent);

                }
                else if(id==R.id.rating){
                Intent intent=new Intent(getApplicationContext(),Rating_of_stall.class);
                startActivity(intent);
            }

            else if (id == R.id.logbtn) {

                SharedGetterSetter.setUserId(getApplicationContext(),"");
                SharedGetterSetter.setUserName(getApplicationContext(),"");
                SharedGetterSetter.setImgUrl(getApplicationContext(),"");
                SharedGetterSetter.setUserMail(getApplicationContext(),"");
                SharedGetterSetter.setUserMobile(getApplicationContext(),"");
                SharedGetterSetter.setUserType(getApplicationContext(),"");

//                if(LogIn.g_status=="1")
//                {
//                    LogIn logIn=new LogIn();
//                    logIn.signOut();
//                }
//                else {
                    Intent intent = new Intent(getApplicationContext(), LogIn.class);
                    startActivity(intent);
//                }



            } else if (id == R.id.nav_share) {
                Intent shareIntent = new Intent(Intent.ACTION_SEND);
                shareIntent.setType("text/plain");
                shareIntent.putExtra(Intent.EXTRA_SUBJECT,"INSERT THE DETAIL");
                String app_url = " https://play.google.com/store/apps/details?id=food2u";
                shareIntent.putExtra(android.content.Intent.EXTRA_TEXT,app_url);
                startActivity(Intent.createChooser(shareIntent, "Share via"));


            } else if (id == R.id.nav_send) {

                if(U_type.equalsIgnoreCase("admin")){
                Intent intent=new Intent(getApplicationContext(),AdminDashboard.class);
                startActivity(intent);}
                else{
                    Toast.makeText(this, "You Are Not Admin ", Toast.LENGTH_SHORT).show();
                }

            }

            DrawerLayout drawer = (DrawerLayout) findViewById(R.id.drawer_layout);
            drawer.closeDrawer(GravityCompat.START);
            return true;
        }

    public Bitmap decodeImage(String url){
        byte[] decodedString = Base64.decode(url, Base64.DEFAULT);
        Log.d("decoded",decodedString+"");
        bitmap1= BitmapFactory.decodeByteArray(decodedString, 0, decodedString.length);
        Log.d("image coming",bitmap1+"");
        return bitmap1;}

}
