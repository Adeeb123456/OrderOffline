package com.example.adeeb.orderoffline;

import android.annotation.SuppressLint;
import android.app.AlertDialog;
import android.app.Dialog;
import android.content.Context;
import android.content.Intent;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.os.AsyncTask;
import android.os.Bundle;
import android.support.design.widget.CollapsingToolbarLayout;
import android.support.design.widget.NavigationView;
import android.support.design.widget.TabLayout;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentManager;
import android.support.v4.app.FragmentPagerAdapter;
import android.support.v4.view.ViewPager;
import android.support.v4.widget.DrawerLayout;
import android.support.v7.app.ActionBarDrawerToggle;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.graphics.Palette;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.support.v7.widget.Toolbar;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.view.ViewGroup;
import android.widget.FrameLayout;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;


import org.json.JSONException;
import org.json.JSONObject;

import java.io.BufferedReader;
import java.io.DataOutputStream;
import java.io.IOException;
import java.io.InputStreamReader;
import java.net.HttpURLConnection;
import java.net.MalformedURLException;
import java.net.URL;
import java.util.ArrayList;
import java.util.List;

public class TabsHeaderActivity extends AppCompatActivity {
    DrawerLayout drawerLayout;
    NavigationView navigationView;
    ActionBarDrawerToggle actionBarDrawerToggleRestroDistances;
    ImageView khImg, htabHeaderImg;
    TextView khText;
    boolean isUserFirstTime;
    public static final String PREF_USER_FIRST_TIME = "user_first_time";

    static String restrorantNumber;
    static Context contextg;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        isUserFirstTime = Boolean.valueOf(Utils.readSharedSetting(TabsHeaderActivity.this, PREF_USER_FIRST_TIME, "true"));

        Intent introIntent = new Intent(TabsHeaderActivity.this, PagerActivity.class);
        introIntent.putExtra(PREF_USER_FIRST_TIME, isUserFirstTime);

        if (isUserFirstTime)
            startActivity(introIntent);


        setContentView(R.layout.activity_tabs_header);




        final Toolbar toolbar = (Toolbar) findViewById(R.id.htab_toolbar);
        drawerLayout=(DrawerLayout)findViewById(R.id.drawerlout_restro_latilongi);
        navigationView=(NavigationView)findViewById(R.id.navigation_view);
        khImg=(ImageView)findViewById(R.id.imgicon);
        htabHeaderImg=(ImageView)findViewById(R.id.htab_header);
        khText=(TextView)findViewById(R.id.texthub);
        setSupportActionBar(toolbar);
        getSupportActionBar().setTitle("-");
        getSupportActionBar().setDisplayHomeAsUpEnabled(true);

        final ViewPager viewPager = (ViewPager) findViewById(R.id.htab_viewpager);
        setupViewPager(viewPager);


        TabLayout tabLayout = (TabLayout) findViewById(R.id.htab_tabs);
        tabLayout.setupWithViewPager(viewPager);

        final CollapsingToolbarLayout collapsingToolbarLayout = (CollapsingToolbarLayout) findViewById(R.id.htab_collapse_toolbar);
        collapsingToolbarLayout.setTitleEnabled(false);

        actionBarDrawerToggleRestroDistances=new ActionBarDrawerToggle(this,drawerLayout,toolbar,
                R.string.open,R.string.close);
        actionBarDrawerToggleRestroDistances.syncState(); // it will display hand barger manu , without wich it was not displaye
        // only back arrow will display;
        drawerLayout.setDrawerListener(actionBarDrawerToggleRestroDistances);

      //  ImageView header = (ImageView) findViewById(R.id.header);

       new AnimationXml(getApplicationContext()).animateCollapsingview(htabHeaderImg);





        Bitmap bitmap = BitmapFactory.decodeResource(getResources(),
                R.drawable.bgfood1);

        Palette.from(bitmap).generate(new Palette.PaletteAsyncListener() {
            @SuppressWarnings("ResourceType")
            @Override
            public void onGenerated(Palette palette) {

                int vibrantColor = palette.getVibrantColor(R.color.primary_500);
                int vibrantDarkColor = palette.getDarkVibrantColor(R.color.primary_700);
                collapsingToolbarLayout.setContentScrimColor(vibrantColor);
                collapsingToolbarLayout.setStatusBarScrimColor(vibrantDarkColor);
            }
        });
      //  khImg.setImageResource(R.drawable.newf1);
        tabLayout.setOnTabSelectedListener(new TabLayout.OnTabSelectedListener() {
            @Override
            public void onTabSelected(TabLayout.Tab tab) {

                viewPager.setCurrentItem(tab.getPosition());

                switch (tab.getPosition()) {
                    case 0:
                      //  showToast("One");
                        khImg.setImageResource(0);
                        //htabHeaderImg.setImageResource(R.drawable.kh3f);
                        khImg.setImageResource(0);
                        //khImg.setImageResource(R.drawable.newf1);
                        khText.setText("Great Interviews");
                        break;
                    case 1:
                        //  showToast("Two");
                        //htabHeaderImg.setImageResource(R.drawable.kn4f);
                        khImg.setImageResource(0);
                       // khImg.setImageResource(R.drawable.armyf);
                        khText.setText("Armed Forces");
                        break;

                    case 2:
                      //  showToast("Two");
                       // htabHeaderImg.setImageResource(R.drawable.kn4f);
                        khImg.setImageResource(0);
                      //  khImg.setImageResource(R.drawable.everydayscif);
                        khText.setText("Every Day Science");
                        break;
                    case 3:
                      //  htabHeaderImg.setImageResource(R.drawable.kh3f);
                        khImg.setImageResource(0);
                     //   khImg.setImageResource(R.drawable.gnkf);
                        khText.setText("General Knowledge");

                        break;
                    case 4:
                      //  htabHeaderImg.setImageResource(R.drawable.khf6);
                   //     khImg.setImageResource(R.drawable.csf);
                        khText.setText("Computer Science");

                        break;
                    case 5:
                       // htabHeaderImg.setImageResource(R.drawable.kn4f);
                        khImg.setImageResource(0);
                       // khImg.setImageResource(R.drawable.islf);
                        khText.setText("Islamiat");

                        break;

                }
            }

            @Override
            public void onTabUnselected(TabLayout.Tab tab) {

            }

            @Override
            public void onTabReselected(TabLayout.Tab tab) {

            }
        });

        navigationView.setNavigationItemSelectedListener(new NavigationView.OnNavigationItemSelectedListener() {
            @Override
            public boolean onNavigationItemSelected(MenuItem item) {
                item.setChecked(true);
                drawerLayout.closeDrawers();

                switch (item.getItemId()) {

                    case R.id.nav2:

                        break;

                    case R.id.aboutus:
                        AlertDialog.Builder builder=new AlertDialog.Builder(TabsHeaderActivity.this);
                        builder.setTitle(".").
                                setMessage("Adeeb ul karim is a software engineer. He is passionate about Android development. \n " +
                                        "Feel free to ask any  help : \n+92 310 188 357 5 \n");
                        builder.setPositiveButton("OK", null);

                        Dialog dialog=builder.create();
                        dialog.show();
                        break;

                }
                return true;
            }
        });
    }


    void showToast(String msg) {
        Toast.makeText(this, msg, Toast.LENGTH_SHORT).show();
    }

    private void setupViewPager(ViewPager viewPager) {
        ViewPagerAdapter adapter = new ViewPagerAdapter(getSupportFragmentManager());


        adapter.addFrag(new DummyFragment(getResources().getColor(R.color.ripple_material_light)), "Pizza");
        adapter.addFrag(new DummyFragment(getResources().getColor(R.color.ripple_material_light)), "Burger");
        viewPager.setAdapter(adapter);
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        // Inflate the menu; this adds items to the action bar if it is present.
        getMenuInflater().inflate(R.menu.menu_main, menu);
        return true;
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        switch (item.getItemId()) {
            case android.R.id.home:
                finish();
                return true;
            case R.id.action_settings:
                return true;
        }
        return super.onOptionsItemSelected(item);
    }

    static class ViewPagerAdapter extends FragmentPagerAdapter {
        private final List<Fragment> mFragmentList = new ArrayList<>();
        private final List<String> mFragmentTitleList = new ArrayList<>();

        public ViewPagerAdapter(FragmentManager manager) {
            super(manager);
        }

        @Override
        public Fragment getItem(int position) {
            return mFragmentList.get(position);
        }

        @Override
        public int getCount() {
            return mFragmentList.size();
        }

        public void addFrag(Fragment fragment, String title) {
            mFragmentList.add(fragment);
            mFragmentTitleList.add(title);
        }

        @Override
        public CharSequence getPageTitle(int position) {
            return mFragmentTitleList.get(position);
        }
    }

    public static class DummyFragment extends Fragment {
        int color;
        MyCustomAdapter adapter;

        public DummyFragment() {
        }

        @SuppressLint("ValidFragment")
        public DummyFragment(int color) {
            this.color = color;
        }

        @Override
        public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
            View view = inflater.inflate(R.layout.dummy_fragment, container, false);

            final FrameLayout frameLayout = (FrameLayout) view.findViewById(R.id.dummyfrag_bg);
            frameLayout.setBackgroundColor(color);

            RecyclerView recyclerView = (RecyclerView) view.findViewById(R.id.dummyfrag_scrollableview);

            LinearLayoutManager linearLayoutManager = new LinearLayoutManager(getActivity().getBaseContext());
            recyclerView.setLayoutManager(linearLayoutManager);
            recyclerView.setHasFixedSize(true);

            ArrayList<Information> list = new ArrayList<Information>();



          //  for (int i = 0; i < IslamiatModel.isl.length; i++) {
           //     list.add(IslamiatModel.isl[i]);
           // }



            adapter = new MyCustomAdapter(getContext(),dummyFoodMenuList());
           // FetchFoodMenu(getContext(),restrorantNumber,adapter);

            recyclerView.setAdapter(adapter);

            return view;
        }
    }

    public static ArrayList<Information> dummyFoodMenuList() {
        ArrayList<Information> informationArrayList=new ArrayList<>();
        Information informationtemp=new Information();

          informationtemp.food_id=4;
         informationtemp.restaurant_id=1;
         informationtemp.title="kabab crust Pizza nm";
         informationtemp.food_catagory="pizza";
         informationtemp.list_foodprise=444;
         informationtemp.list_foodsize="small";
         informationtemp.ingredients="a b c vd";
         informationtemp.offerby="Italian Piza";
         informationtemp.restaurant_number="03101883575";
informationArrayList.add(informationtemp);


        return informationArrayList;
    }




    public static class HrInterviewFragment extends Fragment {
        int color;
        SimpleRecyclerAdapter adapter;
        FoodMenuInterface foodMenuInterface;

        public HrInterviewFragment() {
        }

        @SuppressLint("ValidFragment")
        public HrInterviewFragment(int color) {
            this.color = color;
        }

        @Override
        public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
            View view = inflater.inflate(R.layout.dummy_fragment, container, false);

            final FrameLayout frameLayout = (FrameLayout) view.findViewById(R.id.dummyfrag_bg);
            frameLayout.setBackgroundColor(color);

            RecyclerView recyclerView = (RecyclerView) view.findViewById(R.id.dummyfrag_scrollableview);

            LinearLayoutManager linearLayoutManager = new LinearLayoutManager(getActivity().getBaseContext());
            recyclerView.setLayoutManager(linearLayoutManager);
            recyclerView.setHasFixedSize(true);

            List<String> list = new ArrayList<String>();


           // for (int i = 0; i < HrInterviewModel.hr.length; i++) {
            //    list.add(HrInterviewModel.hr[i]);
          //  }

            adapter = new SimpleRecyclerAdapter(list);

            recyclerView.setAdapter(adapter);

            return view;
        }
    }




    public static void FetchFoodMenu(Context context,String restorantNumber, MyCustomAdapter RecyclerAdapter){
SimpleRecyclerAdapter simpleRecyclerAdapterAsyn;
         contextg=context;
//  notificationDialog.show();
       restrorantNumber=restorantNumber;
        new GetServerFoodMenuAsyn().execute();

    }

    public static class GetServerFoodMenuAsyn extends AsyncTask<Void,Void,JSONObject> {
        HttpURLConnection httpURLConnection;
        URL url = null;

        @Override
        protected JSONObject doInBackground(Void... params) {
            String data="";
            JSONObject jsonObject=new JSONObject();
            try {
                JSONObject serverJsondata;
                //  url=new URL("http://pakdigitalwaiter.netne.net/restroLatiLongiDownload.php");

                url=new URL("http://pdwaiter.net16.net/foodmenu.php");
                //     http://maps.googleapis.com/maps/api/distancematrix/json?origins=34.2007766,73.2466427&destinations=34.1887583,73.2312702&mode=driving&language=en-EN&sensor=false
                //   url=new URL("http://maps.googleapis.com/maps/api/distancematrix/json?origins=34.2007766,73.2466427&destinations=34.1887583,73.2312702&mode=driving&language=en-EN&sensor=false\n");
                httpURLConnection=(HttpURLConnection)url.openConnection();
                httpURLConnection.setChunkedStreamingMode(0);
                httpURLConnection.setDoInput(true);
                httpURLConnection.setDoOutput(true);

//httpURLConnection.setConnectTimeout(100);
                httpURLConnection.setReadTimeout(15000);
                httpURLConnection.setRequestMethod("POST");
                httpURLConnection.setRequestProperty("Content-Type",
                        "application/x-www-form-urlencoded");
                if(CheckInternetConnection.
                        myCheckInternet(contextg)) {

                    httpURLConnection.connect();

                    JSONObject data1 = new JSONObject();
                    data1.put("restaurantNumber", restrorantNumber);


                    DataOutputStream dataOutputStream = new DataOutputStream(httpURLConnection.getOutputStream());
                    dataOutputStream.writeBytes(data1.toString());
                    dataOutputStream.flush();
                    dataOutputStream.close();

                    int response = httpURLConnection.getResponseCode();
                    String line = "";
                    if (response == httpURLConnection.HTTP_OK) {

                        BufferedReader bufferedReader = new BufferedReader(new
                                InputStreamReader(httpURLConnection.getInputStream()));
                        while ((line = bufferedReader.readLine()) != null) {
                            data += line;

                        }
                        Log.i("TA", data);
                        serverJsondata = new JSONObject(data);
                        jsonObject = serverJsondata;
                    } else {
                        if (response == httpURLConnection.HTTP_CLIENT_TIMEOUT) {

                        } else {
                        }

                    }
                }

            } catch (MalformedURLException e) {
                e.printStackTrace();
                httpURLConnection.disconnect();
            } catch (IOException e) {
                httpURLConnection.disconnect();
                e.printStackTrace();
            } catch (JSONException e) {
                e.printStackTrace();
                httpURLConnection.disconnect();
            }


            return jsonObject;




        }

        @Override
        protected void onPostExecute(JSONObject jsonObject) {
            super.onPostExecute(jsonObject);
            Toast.makeText(contextg, "post execute", Toast.LENGTH_SHORT).show();
            if(jsonObject!=null||!(jsonObject.isNull("arraySiz"))){


//                UserLocalStore.storeJsonFoodMenu(foodmenuJobj, getApplicationContext());
              // new tab ma  adapter.updateFoodMenu(restrorantNumber );
              // new cmts  notificationDialog.dismiss();
            }
            else {
               // new coments fetchFoodMenuDataFromDB();

            }
        }

    }





    public static class EveryDayScienceFragment extends Fragment {
        int color;
        SimpleRecyclerAdapter adapter;

        public EveryDayScienceFragment() {
        }

        @SuppressLint("ValidFragment")
        public EveryDayScienceFragment(int color) {
            this.color = color;
        }

        @Override
        public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
            View view = inflater.inflate(R.layout.dummy_fragment, container, false);

            final FrameLayout frameLayout = (FrameLayout) view.findViewById(R.id.dummyfrag_bg);
            frameLayout.setBackgroundColor(color);

            RecyclerView recyclerView = (RecyclerView) view.findViewById(R.id.dummyfrag_scrollableview);

            LinearLayoutManager linearLayoutManager = new LinearLayoutManager(getActivity().getBaseContext());
            recyclerView.setLayoutManager(linearLayoutManager);
            recyclerView.setHasFixedSize(true);

            List<String> list = new ArrayList<String>();


          //  for (int i = 0; i < EveryDaySciModel.sci.length; i++) {
          //      list.add(EveryDaySciModel.sci[i]);
           // }

            adapter = new SimpleRecyclerAdapter(list);
            recyclerView.setAdapter(adapter);

            return view;
        }
    }

    public static class ComputerScienceFragment extends Fragment {
        int color;
        SimpleRecyclerAdapter adapter;

        public ComputerScienceFragment() {
        }

        @SuppressLint("ValidFragment")
        public ComputerScienceFragment(int color) {
            this.color = color;
        }

        @Override
        public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
            View view = inflater.inflate(R.layout.dummy_fragment, container, false);

            final FrameLayout frameLayout = (FrameLayout) view.findViewById(R.id.dummyfrag_bg);
            frameLayout.setBackgroundColor(color);

            RecyclerView recyclerView = (RecyclerView) view.findViewById(R.id.dummyfrag_scrollableview);

            LinearLayoutManager linearLayoutManager = new LinearLayoutManager(getActivity().getBaseContext());
            recyclerView.setLayoutManager(linearLayoutManager);
            recyclerView.setHasFixedSize(true);

            List<String> list = new ArrayList<String>();
           // for (int i = 0; i < ComputerModel.cs.length; i++) {
               // list.add(ComputerModel.cs[i]);
           // }

            adapter = new SimpleRecyclerAdapter(list);
            recyclerView.setAdapter(adapter);

            return view;
        }
    }

    public static class GeneralKnowledgeFragment extends Fragment {
        int color;
        SimpleRecyclerAdapter adapter;

        public GeneralKnowledgeFragment() {
        }

        @SuppressLint("ValidFragment")
        public GeneralKnowledgeFragment(int color) {
            this.color = color;
        }







        @Override
        public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
            View view = inflater.inflate(R.layout.dummy_fragment, container, false);

            final FrameLayout frameLayout = (FrameLayout) view.findViewById(R.id.dummyfrag_bg);
            frameLayout.setBackgroundColor(color);

            RecyclerView recyclerView = (RecyclerView) view.findViewById(R.id.dummyfrag_scrollableview);

            LinearLayoutManager linearLayoutManager = new LinearLayoutManager(getActivity().getBaseContext());
            recyclerView.setLayoutManager(linearLayoutManager);
            recyclerView.setHasFixedSize(true);

            List<String> list = new ArrayList<String>();


          //  for (int i = 0; i < KNModel.kn.length; i++) {
             //   list.add(KNModel.kn[i]);
          //  }

            adapter = new SimpleRecyclerAdapter(list);
            recyclerView.setAdapter(adapter);

            return view;
        }
    }


    public static class IssbNotesFragment extends Fragment {
        int color;
        SimpleRecyclerAdapter adapter;

        public IssbNotesFragment() {
        }

        @SuppressLint("ValidFragment")
        public IssbNotesFragment(int color) {
            this.color = color;
        }


        @Override
        public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
            View view = inflater.inflate(R.layout.dummy_fragment, container, false);

            final FrameLayout frameLayout = (FrameLayout) view.findViewById(R.id.dummyfrag_bg);
            frameLayout.setBackgroundColor(color);

            RecyclerView recyclerView = (RecyclerView) view.findViewById(R.id.dummyfrag_scrollableview);

            LinearLayoutManager linearLayoutManager = new LinearLayoutManager(getActivity().getBaseContext());
            recyclerView.setLayoutManager(linearLayoutManager);
            recyclerView.setHasFixedSize(true);

            List<String> list = new ArrayList<String>();


           // for (int i = 0; i < IssbModel.issb.length; i++) {
             //   list.add(IssbModel.issb[i]);
           // }

            adapter = new SimpleRecyclerAdapter(list);
            recyclerView.setAdapter(adapter);

            return view;
        }
    }
}
