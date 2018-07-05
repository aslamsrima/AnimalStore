
package com.ics.animalworld.view.adapter;

import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentManager;
import android.support.v4.app.FragmentStatePagerAdapter;

import com.ics.animalworld.model.CenterRepository;
import com.ics.animalworld.view.fragment.ProductListFragment;

import java.util.ArrayList;
import java.util.List;
import java.util.Set;

public class ProductsInCategoryPagerAdapter extends FragmentStatePagerAdapter {
    private final List<Fragment> mFragmentList = new ArrayList<>();
    private final List<String> mFragmentTitleList = new ArrayList<>();

    public ProductsInCategoryPagerAdapter(FragmentManager manager) {
        super(manager);
    }

    @Override
    public Fragment getItem(int position) {
        System.out.println("ProductListFragment, getItem() category adapter");
        ProductListFragment listFragment = (ProductListFragment) mFragmentList.get(position);
        if (listFragment.recyclerView != null)
            listFragment.recyclerView.getAdapter().notifyDataSetChanged();
        return listFragment;
    }

    @Override
    public int getCount() {
        System.out.println("ProductListFragment, getCount() category adapter size :: " + mFragmentList.size());
        return mFragmentList.size();
    }

    @Override
    public int getItemPosition(Object object) {
        return POSITION_NONE;
    }

    public void addFrag(Fragment fragment, String title) {
        mFragmentList.add(fragment);
        mFragmentTitleList.add(title);

    }

    public Fragment getFragAtIndex(int index) {
        if (mFragmentList != null &&
                mFragmentList.size() > 0)
            return mFragmentList.get(index);
        else
            return null;
    }

    @Override
    public void notifyDataSetChanged() {
        Set<String> keys = CenterRepository.getCenterRepository().getMapOfProductsInCategory()
                .keySet();

        for (String string : keys) {
            ProductListFragment listFragment = new ProductListFragment(string);
            addFrag(listFragment, string);
        }
        super.notifyDataSetChanged();
    }

    @Override
    public CharSequence getPageTitle(int position) {
        return mFragmentTitleList.get(position);
    }
}
