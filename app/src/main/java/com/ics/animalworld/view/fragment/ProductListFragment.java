package com.ics.animalworld.view.fragment;

import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentTransaction;
import android.support.v4.widget.SwipeRefreshLayout;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.view.KeyEvent;
import android.view.LayoutInflater;
import android.view.MotionEvent;
import android.view.View;
import android.view.View.OnTouchListener;
import android.view.ViewGroup;

import com.ics.animalworld.R;
import com.ics.animalworld.domain.mock.FakeWebServer;
import com.ics.animalworld.model.CenterRepository;
import com.ics.animalworld.util.AppConstants;
import com.ics.animalworld.util.TinyDB;
import com.ics.animalworld.util.Utils;
import com.ics.animalworld.util.Utils.AnimationType;
import com.ics.animalworld.view.activities.ECartHomeActivity;
import com.ics.animalworld.view.adapter.ProductListAdapter;
import com.ics.animalworld.view.adapter.ProductListAdapter.OnItemClickListener;

public class ProductListFragment extends Fragment {
    public static RecyclerView recyclerView;
    public String subcategoryKey;
    SwipeRefreshLayout mSwipeRefreshLayout;
    private boolean isShoppingList;

    public ProductListFragment() {
        isShoppingList = true;
    }

    public ProductListFragment(String subcategoryKey) {

        isShoppingList = false;
        this.subcategoryKey = subcategoryKey;
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.frag_product_list_fragment, container,
                false);


        if (isShoppingList) {
            view.findViewById(R.id.slide_down).setVisibility(View.GONE);
            view.findViewById(R.id.slide_down).setOnTouchListener(
                    new OnTouchListener() {

                        @Override
                        public boolean onTouch(View v, MotionEvent event) {


                            Utils.switchContent(R.id.frag_container,
                                    Utils.HOME_FRAGMENT,
                                    ((ECartHomeActivity) (getContext())),
                                    AnimationType.SLIDE_DOWN);

//                            Utils.switchFragmentWithAnimation(
//                                    R.id.frag_container,
//                                    new HomeFragment(),
//                                    ((ECartHomeActivity) (getContext())), Utils.HOME_FRAGMENT,
//                                    AnimationType.SLIDE_DOWN);


                            return false;
                        }
                    });
        }

        mSwipeRefreshLayout = (SwipeRefreshLayout) view.findViewById(R.id.swipe_container);
        mSwipeRefreshLayout.setOnRefreshListener(new SwipeRefreshLayout.OnRefreshListener() {
            @Override
            public void onRefresh() {
                refreshContent();

            }
        });
        // Fill Recycler View

        recyclerView = (RecyclerView) view
                .findViewById(R.id.product_list_recycler_view);
        LinearLayoutManager linearLayoutManager = new LinearLayoutManager(
                getActivity().getBaseContext());
        recyclerView.setLayoutManager(linearLayoutManager);
        recyclerView.setHasFixedSize(true);

        ProductListAdapter adapter = new ProductListAdapter(subcategoryKey,
                getActivity(), ProductOverviewFragment.sortString);
        recyclerView.setAdapter(adapter);


        adapter.SetOnItemClickListener(new OnItemClickListener() {

            @Override
            public void onItemClick(View view, int position) {

                Utils.switchFragmentWithAnimation(R.id.frag_container,
                        new ProductDetailsFragment(subcategoryKey, position, false),
                        ((ECartHomeActivity) (getContext())), null,
                        AnimationType.SLIDE_LEFT);

            }
        });

        // Handle Back press
        view.setFocusableInTouchMode(true);
        view.requestFocus();
        view.setOnKeyListener(new View.OnKeyListener() {

            @Override
            public boolean onKey(View v, int keyCode, KeyEvent event) {

                if (event.getAction() == KeyEvent.ACTION_UP
                        && keyCode == KeyEvent.KEYCODE_BACK) {

//					Utils.switchContent(R.id.top_container,
//							Utils.HOME_FRAGMENT,
//							((ECartHomeActivity) (getContext())),
//							AnimationType.SLIDE_UP);

                    Utils.switchFragmentWithAnimation(
                            R.id.frag_container,
                            new HomeFragment(),
                            ((ECartHomeActivity) (getContext())), Utils.HOME_FRAGMENT,
                            AnimationType.SLIDE_UP);

                }
                return true;
            }
        });

        return view;
    }

//    public void adapterNotifier()
//    {
//        recyclerView.getAdapter().notifyDataSetChanged();
//    }

    private void refreshContent() {
        TinyDB tiny = new TinyDB(getContext());
        tiny.clear();
        CenterRepository.getCenterRepository().clear();
        FakeWebServer.getFakeWebServer().getAllProducts(
                AppConstants.CURRENT_CATEGORY, new FakeWebServer.FakeWebServiceResponseListener() {
                    @Override
                    public void onServiceResponse(boolean success) {
                        if (success) {
                            try {
                                mSwipeRefreshLayout.setRefreshing(false);
                                recyclerView.getAdapter().notifyDataSetChanged();
//                                Utils.switchContent(R.id.frag_container,
//                                        Utils.PRODUCT_OVERVIEW_FRAGMENT_TAG,
//                                        ((ECartHomeActivity) (getActivity())),
//                                        AnimationType.SLIDE_RIGHT);
                            } catch (Exception e) {
                                e.printStackTrace();
                            }
                        }
                    }
                });
        //ProductOverviewFragment overviewFragment= new ProductOverviewFragment();

        //overviewFragment.refresh();


    }

    ;


}

