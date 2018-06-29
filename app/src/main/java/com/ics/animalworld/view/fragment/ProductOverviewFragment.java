

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

    // SimpleRecyclerAdapter adapter;
    private KenBurnsView header;
    private Bitmap bitmap;
    private Toolbar mToolbar;
    private ViewPager viewPager;
    private CollapsingToolbarLayout collapsingToolbarLayout;
    private TabLayout tabLayout;
    private Spinner SortBy;
    public static String sortString = "";
    TextView SortTxt, LoadingTxt;
    ArrayAdapter<String> dataAdapter;
    List<String> animalList;
    public ProgressBar circularProgressBar;
    ArrayList<Animals> productList;
    //RecyclerView recyclerView;

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.frag_category_details,
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
                                    circularProgressBar.setVisibility(View.GONE);
                                    LoadingTxt.setVisibility(View.GONE);
                                    SortBy.setAdapter(dataAdapter);
                                    SortTxt.setVisibility(View.VISIBLE);
                                    SortBy.setVisibility(View.VISIBLE);
                                } catch (Exception e) {
                                    e.printStackTrace();
                                }
                            }
                        }
                    });
        } else {

            // set data to product map first. View is getting data from there
            LoadingTxt.setVisibility(View.VISIBLE);
            circularProgressBar = (ProgressBar) view.findViewById(R.id.circular_progress1);
            if (AppConstants.CURRENT_CATEGORY == 0) {
                FakeWebServer.getFakeWebServer().updateProductMapForCategory("Animals", productList);
                productList.clear();
                productList = tinydb.getListObject("Animal's Food", Animals.class);
                FakeWebServer.getFakeWebServer().updateProductMapForCategory("Animal's Food", productList);
                productList.clear();
                productList = tinydb.getListObject("Animal's Medicine", Animals.class);
                FakeWebServer.getFakeWebServer().updateProductMapForCategory("Animal's Medicine", productList);
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
            // FakeWebServer.getFakeWebServer().updateProductMapForCategory("Animals", productList);

            setUpUi();
            setupViewPager(viewPager);
            circularProgressBar.setVisibility(View.GONE);
            SortBy.setAdapter(dataAdapter);
            SortTxt.setVisibility(View.VISIBLE);
            SortBy.setVisibility(View.VISIBLE);
        }

        SortBy.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
            @Override
            public void onItemSelected(AdapterView<?> adapterView, View view, int i, long l) {
                productList.clear();
                FakeWebServer.getFakeWebServer().updateProductMapForCategory("Animals", productList);
                TinyDB tinydb = new TinyDB(getContext());
                productList = tinydb.getListObject("Animals", Animals.class);

                if (i > 0) {
                    if (sortString.equals("")) {
                        sortString = SortBy.getSelectedItem().toString();


                        ProductListFragment.recyclerView.setVisibility(View.GONE);

                        ArrayList<Animals> Sortedlist = new ArrayList<Animals>();
                        int cnt=0;
                        for (Animals item : productList) {
                            if (!item.SubCategory.toLowerCase().equals(sortString.toLowerCase())){
                               ProductListFragment.recyclerView.getAdapter().notifyItemRemoved(cnt);
                            }else{
//                                if(productList.size()>ProductListFragment.recyclerView.getAdapter().getItemCount())
//                                    ProductListFragment.recyclerView.getAdapter().notifyItemInserted(cnt);
                                Sortedlist.add(item);
                            }

                            cnt++;
                        }


                        if (Sortedlist.size() > 0){
                            productList.clear();
                            productList = Sortedlist;
                        }

                        FakeWebServer.getFakeWebServer().updateProductMapForCategory("Animals", productList);
                        ProductListFragment.recyclerView.setVisibility(View.VISIBLE);
                        sortString = "";
                    }
                }else{
                    FakeWebServer.getFakeWebServer().updateProductMapForCategory("Animals", productList);
                }
                ProductListFragment.recyclerView.getAdapter().notifyDataSetChanged();
            }

            @Override
            public void onNothingSelected(AdapterView<?> adapterView) {

            }
        });


        /*if (null != ((ECartHomeActivity) getContext()).getProgressBar())
            ((ECartHomeActivity) getContext()).getProgressBar().setVisibility(
                    View.GONE)*/
        ;

        return view;
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

                        switch (tab.getPosition()) {
                            case 0:

                                header.setImageResource(R.drawable.header_1);

                                bitmap = BitmapFactory.decodeResource(
                                        getResources(), R.drawable.header_1);

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
                                header.setImageResource(R.drawable.header_2);

                                bitmap = BitmapFactory.decodeResource(
                                        getResources(), R.drawable.header_2);

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

                                header.setImageResource(R.drawable.header2);

                                Bitmap bitmap = BitmapFactory.decodeResource(
                                        getResources(), R.drawable.header2);

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
            adapter.addFrag(new ProductListFragment(string), string);
        }

        viewPager.setAdapter(adapter);
//		viewPager.setPageTransformer(true,
//				Utils.currentPageTransformer(getActivity()));
    }


    // TODO
    //Below Code Work Well But requires JSOn to work
    // Below line of code does caching for offline usage

	
	/*void fillProductMapFromCache() {

		String cached_ProductMapJSON = PreferenceHelper
				.getPrefernceHelperInstace().getString(
						PreferenceHelper.ALL_PRODUCT_LIST_RESPONSE_JSON, null);

		if (null != cached_ProductMapJSON) {
			new JSONParser(NetworkConstants.GET_ALL_PRODUCT,
					cached_ProductMapJSON).parse();

			adapter.notifyDataSetChanged();

		}

	}

	public void fillCategoryData() {

		loadingIndicator.setVisibility(View.VISIBLE);

		JsonObjectRequest jsonObjReq = new JsonObjectRequest(Method.GET,
				NetworkConstants.URL_GET_PRODUCTS_MAP,
				new Response.Listener<JSONObject>() {

					@Override
					public void onResponse(JSONObject response) {

						if (getView() != null && getView().isShown()) {

							new JSONParser(NetworkConstants.GET_ALL_PRODUCT,
									response.toString()).parse();

							PreferenceHelper
									.getPrefernceHelperInstace()
									.setString(
											PreferenceHelper.ALL_PRODUCT_LIST_RESPONSE_JSON,
											response.toString());
							
							setUpPager();


							if (null != loadingIndicator) {
								loadingIndicator.setVisibility(View.GONE);
							}

						}
					}

				}, new Response.ErrorListener() {

					@Override
					public void onErrorResponse(VolleyError error) {

						fillProductMapFromCache();


						if (null != loadingIndicator) {
							loadingIndicator.setVisibility(View.GONE);
						}
						if (error instanceof TimeoutError
								|| error instanceof NoConnectionError) {


							if (null != getActivity())
								((ECartHomeActivity) getActivity())
										.ShowErrorMessage(Errorhandler.OFFLINE_MODE, true);

						} else if (error instanceof AuthFailureError) {
							// TODO
						} else if (error instanceof ServerError) {

							
							if (null != getActivity())
								((ECartHomeActivity) getActivity())
										.ShowErrorMessage(Errorhandler.SERVER_ERROR, true);
							// TODO
						} else if (error instanceof NetworkError) {

							
							if (null != getActivity())
								((ECartHomeActivity) getActivity())
										.ShowErrorMessage(Errorhandler.NETWORK_ERROR, true);

						} else if (error instanceof ParseError) {

							if (null != getActivity())
								Toast.makeText(
										getActivity(),
										"Parsing Error" + error.networkResponse
												+ error.getLocalizedMessage(),
										Toast.LENGTH_LONG).show();

						}
					}

				}) {

		};

		// jsonObjReq.setRetryPolicy(new DefaultRetryPolicy(60000 * 2, 0, 0));

		jsonObjReq.setRetryPolicy(new DefaultRetryPolicy((int) TimeUnit.SECONDS
				.toMillis(60), DefaultRetryPolicy.DEFAULT_MAX_RETRIES,
				DefaultRetryPolicy.DEFAULT_BACKOFF_MULT));

		AppController.getInstance().addToRequestQueue(jsonObjReq, tagJSONReq);

	}
*/
}