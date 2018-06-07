package com.ics.animalworld.view.fragment;

import android.content.Intent;
import android.graphics.Color;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.support.v4.view.GravityCompat;
import android.support.v7.widget.Toolbar;
import android.text.Spannable;
import android.text.Spanned;
import android.text.style.StrikethroughSpan;
import android.view.KeyEvent;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.TextView.BufferType;

import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.ics.animalworld.R;
import com.ics.animalworld.model.CenterRepository;
import com.ics.animalworld.model.entities.Money;
import com.ics.animalworld.util.ColorGenerator;
import com.ics.animalworld.util.Utils;
import com.ics.animalworld.util.Utils.AnimationType;
import com.ics.animalworld.view.activities.ECartHomeActivity;
import com.ics.animalworld.view.activities.LogInActivity;
import com.ics.animalworld.view.adapter.SimilarProductsPagerAdapter;
import com.ics.animalworld.view.customview.ClickableViewPager;
import com.ics.animalworld.view.customview.ClickableViewPager.OnItemClickListener;
import com.ics.animalworld.view.customview.LabelView;
import com.ics.animalworld.view.customview.TextDrawable;
import com.ics.animalworld.view.customview.TextDrawable.IBuilder;
import com.squareup.picasso.Callback;
import com.squareup.picasso.NetworkPolicy;
import com.squareup.picasso.Picasso;

import java.math.BigDecimal;

// TODO: Auto-generated Javadoc

/**
 * Fragment that appears in the "content_frame", shows a animal.
 */
public class ProductDetailsFragment extends Fragment {

    private int productListNumber;
    private ImageView itemImage;
    private TextView itemSellPrice, itemName, quanitity, itemdescription, itembreed, itemage, itemgender, itemaddress;
    private IBuilder mDrawableBuilder;
    private Button BtnContact;
    private TextDrawable drawable;
    private ColorGenerator mColorGenerator = ColorGenerator.MATERIAL;
    private String subcategoryKey;
    private boolean isFromCart;
    private ClickableViewPager similarProductsPager;
    private ClickableViewPager topSellingPager;
    private Toolbar mToolbar;
    private FirebaseAuth mAuth;


    /**
     * Instantiates a new product details fragment.
     */
    public ProductDetailsFragment(String subcategoryKey, int productNumber,
                                  boolean isFromCart) {

        this.subcategoryKey = subcategoryKey;
        this.productListNumber = productNumber;
        this.isFromCart = isFromCart;
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        View rootView = inflater.inflate(R.layout.frag_product_detail,
                container, false);
        mAuth = FirebaseAuth.getInstance();
        mToolbar = (Toolbar) rootView.findViewById(R.id.htab_toolbar);
        if (mToolbar != null) {
            ((ECartHomeActivity) getActivity()).setSupportActionBar(mToolbar);
        }

        if (mToolbar != null) {
            ((ECartHomeActivity) getActivity()).getSupportActionBar()
                    .setDisplayHomeAsUpEnabled(true);

            mToolbar.setNavigationIcon(R.drawable.ic_drawer);

        }

        mToolbar.setTitleTextColor(Color.WHITE);

        mToolbar.setNavigationOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                ((ECartHomeActivity) getActivity()).getmDrawerLayout()
                        .openDrawer(GravityCompat.START);
            }
        });

        ((ECartHomeActivity) getActivity()).getSupportActionBar()
                .setDisplayHomeAsUpEnabled(true);

        similarProductsPager = (ClickableViewPager) rootView
                .findViewById(R.id.similar_products_pager);

        topSellingPager = (ClickableViewPager) rootView
                .findViewById(R.id.top_selleing_pager);

        itemSellPrice = ((TextView) rootView
                .findViewById(R.id.category_discount));

        itemName = ((TextView) rootView.findViewById(R.id.product_name));

        itembreed = ((TextView) rootView
                .findViewById(R.id.product_breed));

        itemage = ((TextView) rootView
                .findViewById(R.id.product_Age));

        itemgender = ((TextView) rootView
                .findViewById(R.id.product_Gender));

        itemaddress = ((TextView) rootView
                .findViewById(R.id.product_Addreess));

        itemdescription = ((TextView) rootView
                .findViewById(R.id.product_description));

        itemImage = (ImageView) rootView.findViewById(R.id.product_image);

        BtnContact = (Button) rootView.findViewById(R.id.btnContact);

        BtnContact.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                FirebaseUser currentUser = mAuth.getCurrentUser();
                if(currentUser!=null){

                }else{
                    Intent intent = new Intent();
                    intent.setClass(getActivity(), LogInActivity.class);
                    startActivity(intent);
                }
            }
        });
        fillProductData();

//        rootView.findViewById(R.id.add_item).setOnClickListener(
//                new OnClickListener() {
//
//                    @Override
//                    public void onClick(View v) {
//
//                        if (isFromCart) {
//
//                            //Update Quantity on shopping List
//                            CenterRepository
//                                    .getCenterRepository()
//                                    .getListOfProductsInShoppingList()
//                                    .get(productListNumber)
//                                    .setQuantity(
//                                            String.valueOf(
//
//                                                    Integer.valueOf(CenterRepository
//                                                            .getCenterRepository()
//                                                            .getListOfProductsInShoppingList()
//                                                            .get(productListNumber)
//                                                            .getQuantity()) + 1));
//
//
//                            //Update Ui
//                            quanitity.setText(CenterRepository
//                                    .getCenterRepository().getListOfProductsInShoppingList()
//                                    .get(productListNumber).getQuantity());
//
//                            Utils.vibrate(getActivity());
//
//                            //Update checkout amount on screen
//                            ((ECartHomeActivity) getActivity()).updateCheckOutAmount(
//                                    BigDecimal.valueOf(Long
//                                            .valueOf(CenterRepository
//                                                    .getCenterRepository()
//                                                    .getListOfProductsInShoppingList()
//                                                    .get(productListNumber)
//                                                    .getSellMRP())), true);
//
//                        } else {
//
//                            // current object
//                            Product tempObj = CenterRepository
//                                    .getCenterRepository().getMapOfProductsInCategory()
//                                    .get(subcategoryKey).get(productListNumber);
//
//                            // if current object is lready in shopping list
//                            if (CenterRepository.getCenterRepository()
//                                    .getListOfProductsInShoppingList().contains(tempObj)) {
//
//                                // get position of current item in shopping list
//                                int indexOfTempInShopingList = CenterRepository
//                                        .getCenterRepository().getListOfProductsInShoppingList()
//                                        .indexOf(tempObj);
//
//                                // increase quantity of current item in shopping
//                                // list
//                                if (Integer.parseInt(tempObj.getQuantity()) == 0) {
//
//                                    ((ECartHomeActivity) getContext())
//                                            .updateItemCount(true);
//
//                                }
//
//                                // update quanity in shopping list
//                                CenterRepository
//                                        .getCenterRepository()
//                                        .getListOfProductsInShoppingList()
//                                        .get(indexOfTempInShopingList)
//                                        .setQuantity(
//                                                String.valueOf(Integer
//                                                        .valueOf(tempObj
//                                                                .getQuantity()) + 1));
//
//                                // update checkout amount
//                                ((ECartHomeActivity) getContext()).updateCheckOutAmount(
//                                        BigDecimal.valueOf(Long
//                                                .valueOf(CenterRepository
//                                                        .getCenterRepository()
//                                                        .getMapOfProductsInCategory()
//                                                        .get(subcategoryKey)
//                                                        .get(productListNumber)
//                                                        .getSellMRP())), true);
//
//                                // update current item quanitity
//                                quanitity.setText(tempObj.getQuantity());
//
//                            } else {
//
//                                ((ECartHomeActivity) getContext())
//                                        .updateItemCount(true);
//
//                                tempObj.setQuantity(String.valueOf(1));
//
//                                quanitity.setText(tempObj.getQuantity());
//
//                                CenterRepository.getCenterRepository()
//                                        .getListOfProductsInShoppingList().add(tempObj);
//
//                                ((ECartHomeActivity) getContext()).updateCheckOutAmount(
//                                        BigDecimal.valueOf(Long
//                                                .valueOf(CenterRepository
//                                                        .getCenterRepository()
//                                                        .getMapOfProductsInCategory()
//                                                        .get(subcategoryKey)
//                                                        .get(productListNumber)
//                                                        .getSellMRP())), true);
//
//                            }
//
//                            Utils.vibrate(getContext());
//
//                        }
//                    }
//
//                });

//        rootView.findViewById(R.id.remove_item).setOnClickListener(
//                new OnClickListener() {
//
//                    @Override
//                    public void onClick(View v) {
//
//                        if (isFromCart)
//
//                        {
//
//                            if (Integer.valueOf(CenterRepository
//                                    .getCenterRepository().getListOfProductsInShoppingList()
//                                    .get(productListNumber).getQuantity()) > 2) {
//
//                                CenterRepository
//                                        .getCenterRepository()
//                                        .getListOfProductsInShoppingList()
//                                        .get(productListNumber)
//                                        .setQuantity(
//                                                String.valueOf(
//
//                                                        Integer.valueOf(CenterRepository
//                                                                .getCenterRepository()
//                                                                .getListOfProductsInShoppingList()
//                                                                .get(productListNumber)
//                                                                .getQuantity()) - 1));
//
//                                quanitity.setText(CenterRepository
//                                        .getCenterRepository().getListOfProductsInShoppingList()
//                                        .get(productListNumber).getQuantity());
//
//                                ((ECartHomeActivity) getActivity()).updateCheckOutAmount(
//                                        BigDecimal.valueOf(Long
//                                                .valueOf(CenterRepository
//                                                        .getCenterRepository()
//                                                        .getListOfProductsInShoppingList()
//                                                        .get(productListNumber)
//                                                        .getSellMRP())), false);
//
//                                Utils.vibrate(getActivity());
//                            } else if (Integer.valueOf(CenterRepository
//                                    .getCenterRepository().getListOfProductsInShoppingList()
//                                    .get(productListNumber).getQuantity()) == 1) {
//                                ((ECartHomeActivity) getActivity())
//                                        .updateItemCount(false);
//
//                                ((ECartHomeActivity) getActivity()).updateCheckOutAmount(
//                                        BigDecimal.valueOf(Long
//                                                .valueOf(CenterRepository
//                                                        .getCenterRepository()
//                                                        .getListOfProductsInShoppingList()
//                                                        .get(productListNumber)
//                                                        .getSellMRP())), false);
//
//                                CenterRepository.getCenterRepository()
//                                        .getListOfProductsInShoppingList()
//                                        .remove(productListNumber);
//
//                                if (Integer
//                                        .valueOf(((ECartHomeActivity) getActivity())
//                                                .getItemCount()) == 0) {
//
//                                    MyCartFragment.updateMyCartFragment(false);
//
//                                }
//
//                                Utils.vibrate(getActivity());
//
//                            }
//
//                        } else {
//
//                            Product tempObj = CenterRepository
//                                    .getCenterRepository().getMapOfProductsInCategory()
//                                    .get(subcategoryKey).get(productListNumber);
//
//                            if (CenterRepository.getCenterRepository()
//                                    .getListOfProductsInShoppingList().contains(tempObj)) {
//
//                                int indexOfTempInShopingList = CenterRepository
//                                        .getCenterRepository().getListOfProductsInShoppingList()
//                                        .indexOf(tempObj);
//
//                                if (Integer.valueOf(tempObj.getQuantity()) != 0) {
//
//                                    CenterRepository
//                                            .getCenterRepository()
//                                            .getListOfProductsInShoppingList()
//                                            .get(indexOfTempInShopingList)
//                                            .setQuantity(
//                                                    String.valueOf(Integer.valueOf(tempObj
//                                                            .getQuantity()) - 1));
//
//                                    ((ECartHomeActivity) getContext()).updateCheckOutAmount(
//                                            BigDecimal.valueOf(Long
//                                                    .valueOf(CenterRepository
//                                                            .getCenterRepository()
//                                                            .getMapOfProductsInCategory()
//                                                            .get(subcategoryKey)
//                                                            .get(productListNumber)
//                                                            .getSellMRP())),
//                                            false);
//
//                                    quanitity.setText(CenterRepository
//                                            .getCenterRepository()
//                                            .getListOfProductsInShoppingList()
//                                            .get(indexOfTempInShopingList)
//                                            .getQuantity());
//
//                                    Utils.vibrate(getContext());
//
//                                    if (Integer.valueOf(CenterRepository
//                                            .getCenterRepository()
//                                            .getListOfProductsInShoppingList()
//                                            .get(indexOfTempInShopingList)
//                                            .getQuantity()) == 0) {
//
//                                        CenterRepository
//                                                .getCenterRepository()
//                                                .getListOfProductsInShoppingList()
//                                                .remove(indexOfTempInShopingList);
//
//                                        ((ECartHomeActivity) getContext())
//                                                .updateItemCount(false);
//
//                                    }
//
//                                }
//
//                            } else {
//
//                            }
//
//                        }
//
//                    }
//
//                });

        rootView.setFocusableInTouchMode(true);
        rootView.requestFocus();
        rootView.setOnKeyListener(new View.OnKeyListener() {

            @Override
            public boolean onKey(View v, int keyCode, KeyEvent event) {

                if (event.getAction() == KeyEvent.ACTION_UP
                        && keyCode == KeyEvent.KEYCODE_BACK) {

                    if (isFromCart) {

                        Utils.switchContent(R.id.frag_container,
                                Utils.SHOPPING_LIST_TAG,
                                ((ECartHomeActivity) (getActivity())),
                                AnimationType.SLIDE_UP);
                    } else {

                        Utils.switchContent(R.id.frag_container,
                                Utils.PRODUCT_OVERVIEW_FRAGMENT_TAG,
                                ((ECartHomeActivity) (getActivity())),
                                AnimationType.SLIDE_RIGHT);
                    }

                }
                return true;
            }
        });

        if (isFromCart) {

            similarProductsPager.setVisibility(View.GONE);

            topSellingPager.setVisibility(View.GONE);

        } else {
            showRecomondation();
        }

        return rootView;
    }

    private void showRecomondation() {

        SimilarProductsPagerAdapter mCustomPagerAdapter = new SimilarProductsPagerAdapter(
                getActivity(), subcategoryKey);

        similarProductsPager.setAdapter(mCustomPagerAdapter);

        similarProductsPager.setOnItemClickListener(new OnItemClickListener() {

            @Override
            public void onItemClick(int position) {

                productListNumber = position;

                fillProductData();

                Utils.vibrate(getActivity());

            }
        });

        topSellingPager.setAdapter(mCustomPagerAdapter);

        topSellingPager.setOnItemClickListener(new OnItemClickListener() {

            @Override
            public void onItemClick(int position) {

                productListNumber = position;

                fillProductData();

                Utils.vibrate(getActivity());

            }
        });
    }

    public void fillProductData() {

        if (!isFromCart) {


            //Fetch and display item from Gloabl Data Model

            itemName.setText(CenterRepository.getCenterRepository()
                    .getMapOfProductsInCategory().get(subcategoryKey).get(productListNumber)
                    .Category);

            itembreed.setText("Breed: "+CenterRepository.getCenterRepository()
                    .getMapOfProductsInCategory().get(subcategoryKey).get(productListNumber)
                    .Breed);

            itemage.setText("Age: "+CenterRepository.getCenterRepository()
                    .getMapOfProductsInCategory().get(subcategoryKey).get(productListNumber)
                    .Age);

            itemgender.setText("Gender: "+CenterRepository.getCenterRepository()
                    .getMapOfProductsInCategory().get(subcategoryKey).get(productListNumber)
                    .Gender);

            itemaddress.setText("Address: "+CenterRepository.getCenterRepository()
                    .getMapOfProductsInCategory().get(subcategoryKey).get(productListNumber)
                    .City +", "+CenterRepository.getCenterRepository()
                    .getMapOfProductsInCategory().get(subcategoryKey).get(productListNumber)
                    .District);

            itemdescription.setText(CenterRepository.getCenterRepository()
                    .getMapOfProductsInCategory().get(subcategoryKey).get(productListNumber)
                    .Description);

            String sellCostString = Money.rupees(
                    BigDecimal.valueOf(CenterRepository
                            .getCenterRepository().getMapOfProductsInCategory()
                            .get(subcategoryKey).get(productListNumber)
                            .Prize)).toString()
                    + "  ";

            String buyMRP = Money.rupees(
                    BigDecimal.valueOf(CenterRepository
                            .getCenterRepository().getMapOfProductsInCategory()
                            .get(subcategoryKey).get(productListNumber)
                            .Prize)).toString();

            String costString = sellCostString + buyMRP;

            itemSellPrice.setText(costString, BufferType.SPANNABLE);

            Spannable spannable = (Spannable) itemSellPrice.getText();

            spannable.setSpan(new StrikethroughSpan(), sellCostString.length(),
                    costString.length(), Spanned.SPAN_EXCLUSIVE_EXCLUSIVE);

            mDrawableBuilder = TextDrawable.builder().beginConfig()
                    .withBorder(4).endConfig().roundRect(10);

            drawable = mDrawableBuilder.build(
                    String.valueOf(CenterRepository.getCenterRepository()
                            .getMapOfProductsInCategory().get(subcategoryKey)
                            .get(productListNumber).Category.charAt(0)),
                    mColorGenerator.getColor(CenterRepository
                            .getCenterRepository().getMapOfProductsInCategory()
                            .get(subcategoryKey).get(productListNumber)
                            .Category));

            Picasso.with(getActivity())
                    .load("https://5.imimg.com/data5/RD/OA/MY-50522996/sahiwal-cow-250x250.jpg").placeholder(drawable)  //CenterRepository.getCenterRepository().getMapOfProductsInCategory().get(subcategoryKey).get(productListNumber).getImageURL(
                    .error(drawable).fit().centerCrop()
                    .networkPolicy(NetworkPolicy.OFFLINE)
                    .into(itemImage, new Callback() {
                        @Override
                        public void onSuccess() {

                        }

                        @Override
                        public void onError() {
                            // Try again online if cache failed

                            Picasso.with(getActivity())
                                    .load(//CenterRepository.getCenterRepository().getMapOfProductsInCategory().get(subcategoryKey).get(productListNumber).getImageURL()
                                            "https://5.imimg.com/data5/RD/OA/MY-50522996/sahiwal-cow-250x250.jpg\",")
                                    .placeholder(drawable).error(drawable)
                                    .fit().centerCrop().into(itemImage);
                        }
                    });

            LabelView label = new LabelView(getActivity());

            label.setText("0");//CenterRepository.getCenterRepository().getMapOfProductsInCategory().get(subcategoryKey).get(productListNumber).getDiscount()

            label.setBackgroundColor(0xffE91E63);

            label.setTargetView(itemImage, 10, LabelView.Gravity.RIGHT_TOP);
        } else {


            //Fetch and display products from Shopping list

            itemName.setText(CenterRepository.getCenterRepository()
                    .getListOfProductsInShoppingList().get(productListNumber).Category);

            quanitity.setText("1");//CenterRepository.getCenterRepository().getListOfProductsInShoppingList().get(productListNumber).getQuantity());

            itemdescription.setText(CenterRepository.getCenterRepository()
                    .getListOfProductsInShoppingList().get(productListNumber).Description);

            String sellCostString = Money.rupees(
                    BigDecimal.valueOf(CenterRepository
                            .getCenterRepository().getListOfProductsInShoppingList()
                            .get(productListNumber).Prize)).toString()
                    + "  ";

            String buyMRP = Money.rupees(
                    BigDecimal.valueOf(CenterRepository
                            .getCenterRepository().getListOfProductsInShoppingList()
                            .get(productListNumber).Prize)).toString();

            String costString = sellCostString + buyMRP;

            itemSellPrice.setText(costString, BufferType.SPANNABLE);

            Spannable spannable = (Spannable) itemSellPrice.getText();

            spannable.setSpan(new StrikethroughSpan(), sellCostString.length(),
                    costString.length(), Spanned.SPAN_EXCLUSIVE_EXCLUSIVE);

            mDrawableBuilder = TextDrawable.builder().beginConfig()
                    .withBorder(4).endConfig().roundRect(10);

            drawable = mDrawableBuilder.build(
                    String.valueOf(CenterRepository.getCenterRepository()
                            .getListOfProductsInShoppingList().get(productListNumber)
                            .Category.charAt(0)),
                    mColorGenerator.getColor(CenterRepository
                            .getCenterRepository().getListOfProductsInShoppingList()
                            .get(productListNumber).Category));

            Picasso.with(getActivity())
                    .load("https://5.imimg.com/data5/RD/OA/MY-50522996/sahiwal-cow-250x250.jpg" //CenterRepository.getCenterRepository().getListOfProductsInShoppingList().get(productListNumber).getImageURL()
                    ).placeholder(drawable)
                    .error(drawable).fit().centerCrop()
                    .networkPolicy(NetworkPolicy.OFFLINE)
                    .into(itemImage, new Callback() {
                        @Override
                        public void onSuccess() {

                        }

                        @Override
                        public void onError() {
                            // Try again online if cache failed

                            Picasso.with(getActivity())
                                    .load("https://5.imimg.com/data5/RD/OA/MY-50522996/sahiwal-cow-250x250.jpg" //CenterRepository.getCenterRepository().getListOfProductsInShoppingList().get(productListNumber).getImageURL()
                                    )
                                    .placeholder(drawable).error(drawable)
                                    .fit().centerCrop().into(itemImage);
                        }
                    });

            LabelView label = new LabelView(getActivity());

            label.setText("0");//CenterRepository.getCenterRepository().getListOfProductsInShoppingList().get(productListNumber).getDiscount());
            label.setBackgroundColor(0xffE91E63);

            label.setTargetView(itemImage, 10, LabelView.Gravity.RIGHT_TOP);

        }
    }


}
