package com.ics.animalworld.util;

import com.ics.animalworld.view.fragment.ContactUsFragment;
import com.ics.animalworld.view.fragment.HomeFragment;
import com.ics.animalworld.view.fragment.MyCartFragment;
import com.ics.animalworld.view.fragment.ProductDetailsFragment;
import com.ics.animalworld.view.fragment.ProductOverviewFragment;
import com.ics.animalworld.view.fragment.SearchProductFragment;
import com.ics.animalworld.view.fragment.SettingsFragment;

/**
 * Created by anjum.shrimali on 7/4/18.
 */

public class FragmentHolder {
    private static FragmentHolder localInstance;

    private HomeFragment homeFragment;
    private ContactUsFragment contactUsFragment;
    private MyCartFragment myCartFragment;
    private ProductDetailsFragment productDetailsFragment;
    private ProductOverviewFragment productOverviewFragment;
    private SearchProductFragment searchProductFragment;
    private SettingsFragment settingsFragment;

    public HomeFragment getHomeFragment() {
        if (homeFragment == null) {
            homeFragment = new HomeFragment();
        }
        return homeFragment;
    }

    public ContactUsFragment getContactUsFragment() {
        if (contactUsFragment == null)
            contactUsFragment = new ContactUsFragment();
        return contactUsFragment;
    }

    public MyCartFragment getMyCartFragment() {
        if (myCartFragment == null)
            myCartFragment = new MyCartFragment();
        return myCartFragment;
    }

    public ProductDetailsFragment getProductDetailsFragment(String subcategoryKey, int productNumber,
                                                            boolean isFromCart) {
        if (productDetailsFragment == null)
            productDetailsFragment = new ProductDetailsFragment(subcategoryKey, productNumber, isFromCart);
        else
            productDetailsFragment.updateDetails(subcategoryKey, productNumber, isFromCart);
        return productDetailsFragment;
    }

    public ProductOverviewFragment getProductOverviewFragment() {
//        if (productOverviewFragment == null)
        productOverviewFragment = new ProductOverviewFragment();
        return productOverviewFragment;
    }

    public SearchProductFragment getSearchProductFragment() {
        if (searchProductFragment == null)
            searchProductFragment = new SearchProductFragment();
        return searchProductFragment;
    }

    public SettingsFragment getSettingsFragment() {
        if (settingsFragment == null) {
            settingsFragment = new SettingsFragment();
        }
        return settingsFragment;
    }

    private FragmentHolder() {

    }

    public static FragmentHolder getInstance() {
        if (localInstance == null) {
            localInstance = new FragmentHolder();
        }

        return localInstance;
    }


}
