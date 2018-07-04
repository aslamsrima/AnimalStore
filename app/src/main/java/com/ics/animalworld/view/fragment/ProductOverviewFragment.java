package com.ics.animalworld.view.fragment;

import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.os.Bundle;
import android.support.design.widget.CollapsingToolbarLayout;
import android.support.design.widget.TabLayout;
import android.support.v4.app.Fragment;
import android.support.v4.view.GravityCompat;
import android.support.v4.view.ViewPager;
import android.support.v7.graphics.Palette;
import android.support.v7.widget.Toolbar;
import android.view.KeyEvent;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.ProgressBar;
import android.widget.Spinner;
import android.widget.TextView;

import com.flaviofaria.kenburnsview.KenBurnsView;
import com.ics.animalworld.R;
import com.ics.animalworld.domain.mock.FakeWebServer;
import com.ics.animalworld.model.CenterRepository;
import com.ics.animalworld.model.entities.Animals;
import com.ics.animalworld.util.AppConstants;
import com.ics.animalworld.util.TinyDB;
import com.ics.animalworld.util.Utils;
import com.ics.animalworld.util.Utils.AnimationType;
import com.ics.animalworld.view.activities.ECartHomeActivity;
import com.ics.animalworld.view.adapter.ProductsInCategoryPagerAdapter;

import java.util.ArrayList;
import java.util.List;
import java.util.Set;

public class ProductOverviewFragment extends Fragment {

    private static ProductOverviewFragment localInstance;

    public static String sortString = "";
    private View view;
    public ProgressBar circularProgressBar;
    TextView SortTxt, LoadingTxt;
    ArrayAdapter<String> dataAdapter;
    List<String> animalList;
    ArrayList<Animals> productList;
    // SimpleRecyclerAdapter adapter;
    private KenBurnsView header;
    private Bitmap bitmap;
    private Toolbar mToolbar;
    private ViewPager viewPager;
    private CollapsingToolbarLayout collapsingToolbarLayout;
    private TabLayout tabLayout;
    private Spinner SortBy;
    //RecyclerView recyclerView;

    public static ProductOverviewFragment getInstance() {
        if (localInstance == null) {
            localInstance = new ProductOverviewFragment();
        }
        return localInstance;
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        view = inflater.inflate(R.layout.frag_category_details,
                container, false);

        View view1 = inflater.inflate(R.layout.frag_product_list_fragment, container,
                false);

        //getActivity().setTitle("Animal World");

        // TODO We Can use Async task But pallete creation is problemitic job
        // will
        // get back to it later

        // new ProductLoaderTask(null, getActivity(), viewPager, tabLayout);

        // Volley can be used here very efficiently but Fake JSON creation is
        // time consuming Leain it now

        viewPager = (ViewPager) view.findViewById(R.id.htab_viewpager);
        SortBy = (Spinner) view.findViewById(R.id.Sort);
        SortBy.setVisibility(View.GONE);
        SortTxt = (TextView) view.findViewById(R.id.SortTxt);
        SortTxt.setVisibility(View.GONE);
        collapsingToolbarLayout = (CollapsingToolbarLayout) view
                .findViewById(R.id.htab_collapse_toolbar);
        collapsingToolbarLayout.setTitleEnabled(false);

        header = (KenBurnsView) view.findViewById(R.id.htab_header);
        header.setImageResource(R.drawable.header);
        LoadingTxt = (TextView) view.findViewById(R.id.loadertxt);
        LoadingTxt.setVisibility(View.GONE);
        tabLayout = (TabLayout) view.findViewById(R.id.htab_tabs);
        // recyclerView = (RecyclerView)view1.findViewById(R.id.product_list_recycler_view);
        mToolbar = (Toolbar) view.findViewById(R.id.htab_toolbar);
//        circularProgressBar = (ProgressView) view.findViewById(R.id.circular_progress);
//        circularProgressBar = (ProgressView) view.findViewById(R.id.circular_progress1);

        if (mToolbar != null) {
            ((ECartHomeActivity) getActivity()).setSupportActionBar(mToolbar);
        }

        if (mToolbar != null) {
            ((ECartHomeActivity) getActivity()).getSupportActionBar()
                    .setDisplayHomeAsUpEnabled(true);

            mToolbar.setNavigationIcon(R.drawable.ic_drawer);

        }

        mToolbar.setNavigationOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                ((ECartHomeActivity) getActivity()).getmDrawerLayout()
                        .openDrawer(GravityCompat.START);
            }
        });
        animalList = new ArrayList<String>();
        animalList.add("Select Animal");
        animalList.add("Buffloes");
        animalList.add("Cows");
        animalList.add("Goats");
        animalList.add("Horse");
        animalList.add("Rabbit");
        animalList.add("Sheeps");


        dataAdapter = new ArrayAdapter<String>(this.getActivity(), android.R.layout.simple_spinner_item, animalList);
        dataAdapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);


        view.setFocusableInTouchMode(true);
        view.requestFocus();
        view.setOnKeyListener(new View.OnKeyListener() {

            @Override
            public boolean onKey(View v, int keyCode, KeyEvent event) {

                if (event.getAction() == KeyEvent.ACTION_UP
                        && keyCode == KeyEvent.KEYCODE_BACK) {

                    Utils.switchContent(R.id.frag_container,
                            Utils.HOME_FRAGMENT,
                            ((ECartHomeActivity) (getContext())),
                            AnimationType.SLIDE_RIGHT);

                }
                return true;
            }
        });

        // Simulate Web service calls
        /*if (null != ((ECartHomeActivity) getContext()).getProgressBar())
            ((ECartHomeActivity) getContext()).getProgressBar().setVisibility(
                    View.VISIBLE);*/


        TinyDB tinydb = new TinyDB(this.getContext().getApplicationContext());
        if (AppConstants.CURRENT_CATEGORY == 0)
            productList = tinydb.getListObject("Animals", Animals.class);
        if (AppConstants.CURRENT_CATEGORY == 1)
            productList = tinydb.getListObject("Pet", Animals.class);
        if (productList.size() == 0) {
            LoadingTxt.setVisibility(View.VISIBLE);
            circularProgressBar = (ProgressBar) view.findViewById(R.id.circular_progress1);
            FakeWebServer.getFakeWebServer().getAllProducts(
                    AppConstants.CURRENT_CATEGORY, new FakeWebServer.FakeWebServiceResponseListener() {
                        @Override
                        public void onServiceResponse(boolean success) {
                            if (success) {
                                try {
                                    setUpUi();
                                    setupViewPager(viewPager);
                                    SortBy.setAdapter(dataAdapter);
                                    circularProgressBar.setVisibility(View.GONE);
                                    LoadingTxt.setVisibility(View.GONE);
                                    SortTxt.setVisibility(View.VISIBLE);
                                    SortBy.setVisibility(View.VISIBLE);
                                } catch (Exception e) {
                                    e.printStackTrace();
                                }
                            }
                        }
                    });
        } else {
            refreshData();
        }
        SortBy.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
            @Override
            public void onItemSelected(AdapterView<?> adapterView, View view, int i, long l) {
                productList.clear();
                TabLayout.Tab tab = tabLayout.getTabAt(tabLayout.getSelectedTabPosition());
                TinyDB tinydb = new TinyDB(getContext());
                productList = tinydb.getListObject(tab.getText().toString(), Animals.class);
                // FakeWebServer.getFakeWebServer().updateProductMapForCategory(tab.getText().toString(), productList);

                if (i > 0) {
                    if (sortString.equals("")) {

                        sortString = SortBy.getSelectedItem().toString();
                        ArrayList<Animals> Sortedlist = new ArrayList<Animals>();
                        for (Animals item : productList) {
                            if (item.SubCategory.toLowerCase().equals(sortString.toLowerCase())) {
                                Sortedlist.add(item);
                            }
                        }

                        if (Sortedlist.size() > 0) {
                            productList.clear();
                            productList = Sortedlist;
                        }

                        FakeWebServer.getFakeWebServer().updateProductMapForCategory(tab.getText().toString(), productList);

                        sortString = "";
                    }
                } else {
                     FakeWebServer.getFakeWebServer().updateProductMapForCategory(tab.getText().toString(), productList);
                }
                if (CenterRepository.getCenterRepository().recyclerViewRef != null)
                    CenterRepository.getCenterRepository().recyclerViewRef.getAdapter().notifyDataSetChanged();
            }

            @Override
            public void onNothingSelected(AdapterView<?> adapterView) {

            }
        });
        return view;
    }

    public void refreshData() {
        // set data to product map first. View is getting data from there
        TinyDB tinydb = new TinyDB(this.getContext().getApplicationContext());
         LoadingTxt.setVisibility(View.VISIBLE);
         circularProgressBar = (ProgressBar) view.findViewById(R.id.circular_progress1);
        if (AppConstants.CURRENT_CATEGORY == 0) {
            FakeWebServer.getFakeWebServer().updateProductMapForCategory("Animals", tinydb.getListObject("Animals", Animals.class));
            FakeWebServer.getFakeWebServer().updateProductMapForCategory("Animal's Food", tinydb.getListObject("Animal's Food", Animals.class));
            FakeWebServer.getFakeWebServer().updateProductMapForCategory("Animal's Medicine", tinydb.getListObject("Animal's Medicine", Animals.class));
        } else if (AppConstants.CURRENT_CATEGORY == 1) {

            FakeWebServer.getFakeWebServer().updateProductMapForCategory("Pet", productList);
            productList.clear();
            productList = tinydb.getListObject("Pet's Food", Animals.class);
            FakeWebServer.getFakeWebServer().updateProductMapForCategory("Pet's Food", productList);
            productList.clear();
            productList = tinydb.getListObject("Pet's Medicine", Animals.class);
            FakeWebServer.getFakeWebServer().updateProductMapForCategory("Pet's Medicine", productList);

        }
         LoadingTxt.setVisibility(View.GONE);

         setUpUi();
         setupViewPager(viewPager);
         circularProgressBar.setVisibility(View.GONE);
         SortBy.setAdapter(dataAdapter);
        SortTxt.setVisibility(View.VISIBLE);
        SortBy.setVisibility(View.VISIBLE);
        System.out.println("ProductListFragment, overview refreshData called");
    }



    private void setUpUi() {

        setupViewPager(viewPager);
        tabLayout.setupWithViewPager(viewPager);
        bitmap = BitmapFactory
                .decodeResource(getResources(), R.drawable.header);

        Palette.from(bitmap).generate(new Palette.PaletteAsyncListener() {
            @SuppressWarnings("ResourceType")
            @Override
            public void onGenerated(Palette palette) {

                int vibrantColor = palette.getVibrantColor(R.color.primary_500);
                int vibrantDarkColor = palette
                        .getDarkVibrantColor(R.color.primary_700);
                collapsingToolbarLayout.setContentScrimColor(vibrantColor);
                collapsingToolbarLayout
                        .setStatusBarScrimColor(vibrantDarkColor);
            }
        });

        tabLayout
                .setOnTabSelectedListener(new TabLayout.OnTabSelectedListener() {
                    @Override
                    public void onTabSelected(TabLayout.Tab tab) {

                        viewPager.setCurrentItem(tab.getPosition());
                        ProductListFragment listFragment = (ProductListFragment) ((ProductsInCategoryPagerAdapter)viewPager.getAdapter())
                                .getFragAtIndex(tab.getPosition());
                        listFragment.refreshData(tab.getText().toString());
                        switch (tab.getPosition()) {
                            case 0:

                                header.setImageResource(R.drawable.header_1);

                                bitmap = BitmapFactory.decodeResource(
                                        getResources(), R.drawable.header);

                                Palette.from(bitmap).generate(
                                        new Palette.PaletteAsyncListener() {
                                            @SuppressWarnings("ResourceType")
                                            @Override
                                            public void onGenerated(Palette palette) {

                                                int vibrantColor = palette
                                                        .getVibrantColor(R.color.primary_500);
                                                int vibrantDarkColor = palette
                                                        .getDarkVibrantColor(R.color.primary_700);
                                                collapsingToolbarLayout
                                                        .setContentScrimColor(vibrantColor);
                                                collapsingToolbarLayout
                                                        .setStatusBarScrimColor(vibrantDarkColor);
                                            }
                                        });
                                break;
                            case 1:
                                dataAdapter = new ArrayAdapter<String>(getContext(), android.R.layout.simple_spinner_item, animalList);
                                dataAdapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
                                SortBy.setAdapter(dataAdapter);
                                header.setImageResource(R.drawable.header);

                                bitmap = BitmapFactory.decodeResource(
                                        getResources(), R.drawable.header);

                                Palette.from(bitmap).generate(
                                        new Palette.PaletteAsyncListener() {
                                            @SuppressWarnings("ResourceType")
                                            @Override
                                            public void onGenerated(Palette palette) {

                                                int vibrantColor = palette
                                                        .getVibrantColor(R.color.primary_500);
                                                int vibrantDarkColor = palette
                                                        .getDarkVibrantColor(R.color.primary_700);
                                                collapsingToolbarLayout
                                                        .setContentScrimColor(vibrantColor);
                                                collapsingToolbarLayout
                                                        .setStatusBarScrimColor(vibrantDarkColor);
                                            }
                                        });

                                break;
                            case 2:

                                header.setImageResource(R.drawable.header);

                                Bitmap bitmap = BitmapFactory.decodeResource(
                                        getResources(), R.drawable.header);

                                Palette.from(bitmap).generate(
                                        new Palette.PaletteAsyncListener() {
                                            @SuppressWarnings("ResourceType")
                                            @Override
                                            public void onGenerated(Palette palette) {

                                                int vibrantColor = palette
                                                        .getVibrantColor(R.color.primary_500);
                                                int vibrantDarkColor = palette
                                                        .getDarkVibrantColor(R.color.primary_700);
                                                collapsingToolbarLayout
                                                        .setContentScrimColor(vibrantColor);
                                                collapsingToolbarLayout
                                                        .setStatusBarScrimColor(vibrantDarkColor);
                                            }
                                        });

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

    }

    private void setupViewPager(ViewPager viewPager) {
        ProductsInCategoryPagerAdapter adapter = new ProductsInCategoryPagerAdapter(
                getActivity().getSupportFragmentManager());
        Set<String> keys = CenterRepository.getCenterRepository().getMapOfProductsInCategory()
                .keySet();

        for (String string : keys) {
            ProductListFragment listFragment = new ProductListFragment(string);
            adapter.addFrag(listFragment, string);
//            listFragment.refreshData(string);
        }

        viewPager.setAdapter(adapter);
//		viewPager.setPageTransformer(true,
//				Utils.currentPageTransformer(getActivity()));
    }






}