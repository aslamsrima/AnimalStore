package com.ics.animalworld.view.fragment;

import android.app.Activity;
import android.content.Intent;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.graphics.Color;
import android.net.Uri;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.v4.app.Fragment;
import android.support.v4.view.GravityCompat;
import android.support.v7.widget.Toolbar;
import android.text.Spannable;
import android.text.Spanned;
import android.text.style.StrikethroughSpan;
import android.util.Base64;
import android.view.KeyEvent;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.TextView.BufferType;

import com.google.android.gms.tasks.OnFailureListener;
import com.google.android.gms.tasks.OnSuccessListener;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.storage.FirebaseStorage;
import com.google.firebase.storage.StorageReference;
import com.ics.animalworld.R;
import com.ics.animalworld.model.CenterRepository;
import com.ics.animalworld.model.entities.Money;
import com.ics.animalworld.util.ColorGenerator;
import com.ics.animalworld.util.Common;
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

import java.io.File;
import java.math.BigDecimal;

// TODO: Auto-generated Javadoc

/**
 * Fragment that appears in the "content_frame", shows a animal.
 */
public class ProductDetailsFragment extends Fragment {

    Bitmap b;
    private int productListNumber;
    private ImageView itemImage;
    private TextView itemSellPrice, itemName, itemSupplierName, itemdescription,itemdescriptionTxt, itembreed,itembreedTxt, itemage,itemageTxt, itemgender,itemgenderTxt, itemaddress,itemaddressTxt, itemsupplier;
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
    private String type;

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

//        similarProductsPager = (ClickableViewPager) rootView
//                .findViewById(R.id.similar_products_pager);
//
//        topSellingPager = (ClickableViewPager) rootView
//                .findViewById(R.id.top_selleing_pager);

        itemSellPrice = ((TextView) rootView
                .findViewById(R.id.category_discount));

        itemName = ((TextView) rootView.findViewById(R.id.product_name));

        itembreed = ((TextView) rootView
                .findViewById(R.id.product_breed));

        itembreedTxt= ((TextView) rootView
                .findViewById(R.id.product_breed_txt));

        itemage = ((TextView) rootView
                .findViewById(R.id.product_Age));

        itemageTxt = ((TextView) rootView
                .findViewById(R.id.product_Age_txt));

        itemgender = ((TextView) rootView
                .findViewById(R.id.product_Gender));

        itemgenderTxt = ((TextView) rootView
                .findViewById(R.id.product_Gender_txt));

        itemaddress = ((TextView) rootView
                .findViewById(R.id.product_Addreess));

        itemaddressTxt = ((TextView) rootView
                .findViewById(R.id.product_Addreess_txt));

        itemdescription = ((TextView) rootView
                .findViewById(R.id.product_description));

        itemdescriptionTxt = ((TextView) rootView
                .findViewById(R.id.product_description_txt));

        itemImage = (ImageView) rootView.findViewById(R.id.product_image);

        BtnContact = (Button) rootView.findViewById(R.id.btnContact);
        itemsupplier = (TextView) rootView.findViewById(R.id.supplier_contact);
        itemSupplierName= (TextView) rootView.findViewById(R.id.supplier_name);
        itemsupplier.setVisibility(View.GONE);
        itemSupplierName.setVisibility(View.GONE);
        BtnContact.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                FirebaseUser currentUser = mAuth.getCurrentUser();
                if (currentUser != null) {
                    BtnContact.setVisibility(View.GONE);
                    itemsupplier.setVisibility(View.VISIBLE);
                    itemSupplierName.setVisibility(View.VISIBLE);
                } else {
                    Intent intent = new Intent();
                    intent.setClass(getActivity(), LogInActivity.class);
                    intent.putExtra("reqCode", Common.LOGIN_REQ);
                    startActivityForResult(intent, Common.LOGIN_REQ);
                }
            }
        });
        fillProductData();

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

                        Utils.switchFragmentWithAnimation(R.id.frag_container,
                                new ProductOverviewFragment(),
                                getActivity(),
                                Utils.PRODUCT_OVERVIEW_FRAGMENT_TAG,
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
           // showRecomondation();
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

            type = CenterRepository.getCenterRepository()
                    .getMapOfProductsInCategory().get(subcategoryKey).get(productListNumber).Type;

            if (type.equals("Animal Food") || type.equals("Animal Medicine") || type.equals("Pet Food") || type.equals("Pet Medicine") ||type.equals("Farming Tools") || type.equals("Farming Product")) {
                itemName.setText(CenterRepository.getCenterRepository()
                        .getMapOfProductsInCategory().get(subcategoryKey).get(productListNumber)
                        .ProductName);

                itemageTxt.setText("Company Name : ");

                itemage.setText( CenterRepository.getCenterRepository()
                        .getMapOfProductsInCategory().get(subcategoryKey).get(productListNumber)
                        .CompanyName);

                if(!type.equals("Farming Tools")){

                    itemgenderTxt.setText("Weight: ");

                    itemgender.setText(CenterRepository.getCenterRepository()
                            .getMapOfProductsInCategory().get(subcategoryKey).get(productListNumber)
                            .weight);

                }


            } else {
                itemName.setText(CenterRepository.getCenterRepository()
                        .getMapOfProductsInCategory().get(subcategoryKey).get(productListNumber)
                        .Breed+", "+CenterRepository.getCenterRepository()
                        .getMapOfProductsInCategory().get(subcategoryKey).get(productListNumber)
                        .SubCategory);


                itemageTxt.setText("Age : ");

                itemage.setText(Integer.toString(CenterRepository.getCenterRepository()
                        .getMapOfProductsInCategory().get(subcategoryKey).get(productListNumber)
                        .Age));

                itemgenderTxt.setText("Gender: ");

                itemgender.setText(CenterRepository.getCenterRepository()
                        .getMapOfProductsInCategory().get(subcategoryKey).get(productListNumber)
                        .Gender);
            }

            itemaddressTxt.setText("Address: ");

            itemaddress.setText(CenterRepository.getCenterRepository()
                    .getMapOfProductsInCategory().get(subcategoryKey).get(productListNumber)
                    .City + ", " + CenterRepository.getCenterRepository()
                    .getMapOfProductsInCategory().get(subcategoryKey).get(productListNumber)
                    .District);

            itemdescriptionTxt.setText("Description: ");

            itemdescription.setText( CenterRepository.getCenterRepository()
                    .getMapOfProductsInCategory().get(subcategoryKey).get(productListNumber)
                    .Description);

            itemsupplier.setText(CenterRepository.getCenterRepository()
                    .getMapOfProductsInCategory().get(subcategoryKey).get(productListNumber)
                    .SupplierContact);

            itemSupplierName.setText(CenterRepository.getCenterRepository()
                    .getMapOfProductsInCategory().get(subcategoryKey).get(productListNumber)
                    .SupplierName);


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

            String costString =  buyMRP;

            itemSellPrice.setText(costString);

//            Spannable spannable = (Spannable) itemSellPrice.getText();

//            spannable.setSpan(new StrikethroughSpan(), sellCostString.length(),
//                    costString.length(), Spanned.SPAN_EXCLUSIVE_EXCLUSIVE);

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

//

            FirebaseStorage storage = FirebaseStorage.getInstance();
            StorageReference storageRef = storage.getReference();
//                StorageReference islandRef = storageRef.child("images/mountains.jpg");
//                final long ONE_MEGABYTE = 1024 * 1024;
//                islandRef.getBytes(ONE_MEGABYTE).addOnSuccessListener(new OnSuccessListener<byte[]>() {
//                    @Override
//                    public void onSuccess(byte[] bytes) {
//                        itemImage.setImageBitmap(BitmapFactory.decodeByteArray(bytes, 0, bytes.length));
//                        // Data for "images/island.jpg" is returns, use this as needed
//                    }
//                }).addOnFailureListener(new OnFailureListener() {
//                    @Override
//                    public void onFailure(@NonNull Exception exception) {
//                        // Handle any errors
//                    }
//                });

            storageRef.child("images/mountains.jpg").getDownloadUrl().addOnSuccessListener(new OnSuccessListener<Uri>() {
                @Override
                public void onSuccess(Uri uri) {
                    Picasso.with(getActivity())
                            .load(uri.toString())
                            .error(drawable).fit().centerCrop()
                            .networkPolicy(NetworkPolicy.OFFLINE)
                            .into(itemImage, new Callback() {
                                @Override
                                public void onSuccess() {

                                }

                                @Override
                                public void onError() {

                                }
                            });

                }
            }).addOnFailureListener(new OnFailureListener() {
                @Override
                public void onFailure(@NonNull Exception exception) {
                    // Handle any errors
                }
            });

            LabelView label = new LabelView(getActivity());

            label.setText("0");//CenterRepository.getCenterRepository().getMapOfProductsInCategory().get(subcategoryKey).get(productListNumber).getDiscount()

            label.setBackgroundColor(0xffE91E63);

            label.setTargetView(itemImage, 10, LabelView.Gravity.RIGHT_TOP);

        }


    }

    @Override
    public void onActivityResult(int requestCode, int resultCode, Intent data) {
        // super.onActivityResult(requestCode, resultCode, data);
        if (requestCode == Common.LOGIN_REQ && resultCode == Activity.RESULT_OK) {
            // update visibility of label
            FirebaseUser currentUser = mAuth.getCurrentUser();
            if (currentUser != null) {
                BtnContact.setVisibility(View.GONE);
                itemsupplier.setVisibility(View.VISIBLE);
                itemSupplierName.setVisibility(View.VISIBLE);
            }
        }
    }

    public Bitmap StringToBitMap(String encodedString) {
        try {
            byte[] encodeByte = Base64.decode(encodedString, Base64.DEFAULT);
            Bitmap bitmap = BitmapFactory.decodeByteArray(encodeByte, 0, encodeByte.length);
            return bitmap;
        } catch (Exception e) {
            e.getMessage();
            return null;
        }
    }
}