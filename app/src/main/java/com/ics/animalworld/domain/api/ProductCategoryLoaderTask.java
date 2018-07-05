/*
 * Copyright (c) 2017. http://hiteshsahu.com- All Rights Reserved
 * Unauthorized copying of this file, via any medium is strictly prohibited
 * If you use or distribute this project then you MUST ADD A COPY OF LICENCE
 * along with the project.
 *  Written by Hitesh Sahu <hiteshkrsahu@Gmail.com>, 2017.
 */

package com.ics.animalworld.domain.api;

import android.content.Context;
import android.os.AsyncTask;
import android.support.v4.app.FragmentActivity;
import android.support.v7.widget.RecyclerView;
import android.view.View;

import com.ics.animalworld.R;
import com.ics.animalworld.domain.mock.DataManager;
import com.ics.animalworld.util.AppConstants;
import com.ics.animalworld.util.FragmentHolder;
import com.ics.animalworld.util.Utils;
import com.ics.animalworld.view.activities.ECartHomeActivity;
import com.ics.animalworld.view.adapter.CategoryListAdapter;
import com.ics.animalworld.view.adapter.CategoryListAdapter.OnItemClickListener;


public class ProductCategoryLoaderTask extends AsyncTask<String, Void, Void> {

    private static final int NUMBER_OF_COLUMNS = 2;
    private Context context;
    private RecyclerView recyclerView;

    public ProductCategoryLoaderTask(RecyclerView listView, Context context) {

        this.recyclerView = listView;
        this.context = context;
    }

    @Override
    protected void onPreExecute() {

        super.onPreExecute();

        if (null != ((ECartHomeActivity) context).getProgressBar())
            ((ECartHomeActivity) context).getProgressBar().setVisibility(
                    View.VISIBLE);

    }

    @Override
    protected void onPostExecute(Void result) {
        super.onPostExecute(result);

        if (null != ((ECartHomeActivity) context).getProgressBar())
            ((ECartHomeActivity) context).getProgressBar().setVisibility(
                    View.GONE);

        if (recyclerView != null) {
            CategoryListAdapter simpleRecyclerAdapter = new CategoryListAdapter(
                    context);

            recyclerView.setAdapter(simpleRecyclerAdapter);

            simpleRecyclerAdapter
                    .SetOnItemClickListener(new OnItemClickListener() {

                        @Override
                        public void onItemClick(View view, int position) {
                            AppConstants.CURRENT_CATEGORY = position;

                            /*Utils.switchContent(R.id.frag_container,
                                    Utils.PRODUCT_OVERVIEW_FRAGMENT_TAG,
                                    (FragmentActivity) context,
                                    AnimationType.SLIDE_LEFT);*/
                            Utils.switchFragmentWithAnimation(R.id.frag_container,
                                    FragmentHolder.getInstance().getProductOverviewFragment(),
                                    (FragmentActivity) context,
                                    Utils.PRODUCT_OVERVIEW_FRAGMENT_TAG,
                                    Utils.AnimationType.SLIDE_LEFT);
                        }
                    });
        }

    }

    @Override
    protected Void doInBackground(String... params) {

        try {
            Thread.sleep(3000);
        } catch (InterruptedException e) {
            e.printStackTrace();
        }

        DataManager.getInstance(context).addCategory();

        return null;
    }

}