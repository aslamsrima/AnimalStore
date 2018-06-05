package com.ics.animalworld.view.adapter;

import android.content.Context;
import android.content.SyncRequest;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.support.v7.widget.RecyclerView;
import android.text.Spannable;
import android.text.Spanned;
import android.text.style.StrikethroughSpan;
import android.util.Base64;
import android.view.LayoutInflater;
import android.view.View;
import android.view.View.OnClickListener;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.TextView.BufferType;

import com.bumptech.glide.Glide;
import com.bumptech.glide.request.target.BitmapImageViewTarget;
import com.ics.animalworld.R;
import com.ics.animalworld.model.CenterRepository;
import com.ics.animalworld.model.entities.Animals;
import com.ics.animalworld.model.entities.Money;
import com.ics.animalworld.model.entities.Product;
import com.ics.animalworld.util.ColorGenerator;
import com.ics.animalworld.util.Utils;
import com.ics.animalworld.view.activities.ECartHomeActivity;
import com.ics.animalworld.view.customview.TextDrawable;
import com.ics.animalworld.view.customview.TextDrawable.IBuilder;

import java.math.BigDecimal;
import java.util.ArrayList;
import java.util.Collections;
import java.util.List;

/**
 * @author Hitesh Sahu (hiteshsahu.com)
 */
public class ProductListAdapter extends
        RecyclerView.Adapter<ProductListAdapter.VersionViewHolder> implements
        ItemTouchHelperAdapter {

    private ColorGenerator mColorGenerator = ColorGenerator.MATERIAL;
    Bitmap b;
    private IBuilder mDrawableBuilder;
    ImageView imagView;
    private TextDrawable drawable;

    private String ImageUrl;

    private List<Animals> productList = new ArrayList<Animals>();
    private OnItemClickListener clickListener;

    private Context context;

    public ProductListAdapter(String subcategoryKey, Context context,
                              boolean isCartlist) {

        if (isCartlist) {

            productList = CenterRepository.getCenterRepository()
                    .getListOfProductsInShoppingList();

        } else {

            productList = CenterRepository.getCenterRepository().getMapOfProductsInCategory()
                    .get(subcategoryKey);
        }

        this.context = context;
    }

    @Override
    public VersionViewHolder onCreateViewHolder(ViewGroup viewGroup, int i) {
        View view = LayoutInflater.from(viewGroup.getContext()).inflate(
                R.layout.item_product_list, viewGroup, false);


        VersionViewHolder viewHolder = new VersionViewHolder(view);
        return viewHolder;
    }

    @Override
    public void onBindViewHolder(final VersionViewHolder holder,
                                 final int position) {

        holder.itemName.setText(productList.get(position)
                .Category);

        holder.itemDesc.setText(productList.get(position)
                .Description);

        String sellCostString = Money.rupees(
                BigDecimal.valueOf(productList.get(position)
                        .Prize)).toString()
                + "  ";

        String buyMRP = Money.rupees(
                BigDecimal.valueOf(productList.get(position)
                        .Prize)).toString();

        String costString = sellCostString + buyMRP;

        holder.itemCost.setText(costString, BufferType.SPANNABLE);

        Spannable spannable = (Spannable) holder.itemCost.getText();

        spannable.setSpan(new StrikethroughSpan(), sellCostString.length(),
                costString.length(), Spanned.SPAN_EXCLUSIVE_EXCLUSIVE);

        mDrawableBuilder = TextDrawable.builder().beginConfig().withBorder(4)
                .endConfig().roundRect(10);

        drawable = mDrawableBuilder.build(String.valueOf(productList
                .get(position).Category.charAt(0)), mColorGenerator
                .getColor(productList.get(position).Breed));


         b = StringToBitMap(productList.get(1).Pic.toString());
        //ImageUrl ="https://5.imimg.com/data5/RD/OA/MY-50522996/sahiwal-cow-250x250.jpg";// productList.get(position).getImageURL();

        imagView.setImageBitmap(b);

//        Glide.with(context).load(b).placeholder(drawable)
//                .error(drawable).animate(R.anim.base_slide_right_in)
//                .centerCrop().into(holder.imagView);


//        holder.addItem.findViewById(R.id.add_item).setOnClickListener(
//                new OnClickListener() {
//
//                    @Override
//                    public void onClick(View v) {
//
//
//                        //current object
//                        Product tempObj = productList.get(position);
//
//
//                        //if current object is lready in shopping list
//                        if (CenterRepository.getCenterRepository()
//                                .getListOfProductsInShoppingList().contains(tempObj)) {
//
//
//                            //get position of current item in shopping list
//                            int indexOfTempInShopingList = CenterRepository
//                                    .getCenterRepository().getListOfProductsInShoppingList()
//                                    .indexOf(tempObj);
//
//                            // increase quantity of current item in shopping list
//                            if (Integer.parseInt(tempObj.getQuantity()) == 0) {
//
//                                ((ECartHomeActivity) getContext())
//                                        .updateItemCount(true);
//
//                            }
//
//
//                            // update quanity in shopping list
//                            CenterRepository
//                                    .getCenterRepository()
//                                    .getListOfProductsInShoppingList()
//                                    .get(indexOfTempInShopingList)
//                                    .setQuantity(
//                                            String.valueOf(Integer
//                                                    .valueOf(tempObj
//                                                            .getQuantity()) + 1));
//
//
//                            //update checkout amount
//                            ((ECartHomeActivity) getContext()).updateCheckOutAmount(
//                                    BigDecimal
//                                            .valueOf(Long
//                                                    .valueOf(productList
//                                                            .get(position)
//                                                            .getSellMRP())),
//                                    true);
//
//                            // update current item quanitity
//                            holder.quanitity.setText(tempObj.getQuantity());
//
//                        } else {
//
//                            ((ECartHomeActivity) getContext()).updateItemCount(true);
//
//                            tempObj.setQuantity(String.valueOf(1));
//
//                            holder.quanitity.setText(tempObj.getQuantity());
//
//                            CenterRepository.getCenterRepository()
//                                    .getListOfProductsInShoppingList().add(tempObj);
//
//                            ((ECartHomeActivity) getContext()).updateCheckOutAmount(
//                                    BigDecimal
//                                            .valueOf(Long
//                                                    .valueOf(productList
//                                                            .get(position)
//                                                            .getSellMRP())),
//                                    true);
//
//                        }
//
//                        Utils.vibrate(getContext());
//
//                    }
//                });

//        holder.removeItem.setOnClickListener(new OnClickListener() {
//
//            @Override
//            public void onClick(View v) {
//
//                Product tempObj = (productList).get(position);
//
//                if (CenterRepository.getCenterRepository().getListOfProductsInShoppingList()
//                        .contains(tempObj)) {
//
//                    int indexOfTempInShopingList = CenterRepository
//                            .getCenterRepository().getListOfProductsInShoppingList()
//                            .indexOf(tempObj);
//
//                    if (Integer.valueOf(tempObj.getQuantity()) != 0) {
//
//                        CenterRepository
//                                .getCenterRepository()
//                                .getListOfProductsInShoppingList()
//                                .get(indexOfTempInShopingList)
//                                .setQuantity(
//                                        String.valueOf(Integer.valueOf(tempObj
//                                                .getQuantity()) - 1));
//
//                        ((ECartHomeActivity) getContext()).updateCheckOutAmount(
//                                BigDecimal.valueOf(Long.valueOf(productList
//                                        .get(position).getSellMRP())),
//                                false);
//
//                        holder.quanitity.setText(CenterRepository
//                                .getCenterRepository().getListOfProductsInShoppingList()
//                                .get(indexOfTempInShopingList).getQuantity());
//
//                        Utils.vibrate(getContext());
//
//                        if (Integer.valueOf(CenterRepository
//                                .getCenterRepository().getListOfProductsInShoppingList()
//                                .get(indexOfTempInShopingList).getQuantity()) == 0) {
//
//                            CenterRepository.getCenterRepository()
//                                    .getListOfProductsInShoppingList()
//                                    .remove(indexOfTempInShopingList);
//
//                            notifyDataSetChanged();
//
//                            ((ECartHomeActivity) getContext())
//                                    .updateItemCount(false);
//
//                        }
//
//                    }
//
//                } else {
//
//                }
//
//            }
//
//        });

    }

    public Bitmap StringToBitMap(String encodedString){
        try {
            byte [] encodeByte= Base64.decode(encodedString,Base64.DEFAULT);
            Bitmap bitmap= BitmapFactory.decodeByteArray(encodeByte, 0, encodeByte.length);
            return bitmap;
        } catch(Exception e) {
            e.getMessage();
            return null;
        }
    }

    private ECartHomeActivity getContext() {
        // TODO Auto-generated method stub
        return (ECartHomeActivity) context;
    }

    @Override
    public int getItemCount() {
        return productList == null ? 0 : productList.size();
    }

    public void SetOnItemClickListener(
            final OnItemClickListener itemClickListener) {
        this.clickListener = itemClickListener;
    }

    @Override
    public void onItemDismiss(int position) {
        productList.remove(position);
        notifyItemRemoved(position);
    }

    @Override
    public void onItemMove(int fromPosition, int toPosition) {
        if (fromPosition < toPosition) {
            for (int i = fromPosition; i < toPosition; i++) {
                Collections.swap(productList, i, i + 1);
            }
        } else {
            for (int i = fromPosition; i > toPosition; i--) {
                Collections.swap(productList, i, i - 1);
            }
        }
        notifyItemMoved(fromPosition, toPosition);
    }

    public interface OnItemClickListener {
        public void onItemClick(View view, int position);
    }

    class VersionViewHolder extends RecyclerView.ViewHolder implements
            View.OnClickListener {
        TextView itemName, itemDesc, itemCost, availability, quanitity,
                addItem, removeItem;


        public VersionViewHolder(View itemView) {
            super(itemView);

            itemName = (TextView) itemView.findViewById(R.id.item_name);

            itemDesc = (TextView) itemView.findViewById(R.id.item_short_desc);

            itemCost = (TextView) itemView.findViewById(R.id.item_price);

            availability = (TextView) itemView
                    .findViewById(R.id.iteam_avilable);

            //quanitity = (TextView) itemView.findViewById(R.id.iteam_amount);

            itemName.setSelected(true);

            imagView = ((ImageView) itemView.findViewById(R.id.product_thumb));

//            addItem = ((TextView) itemView.findViewById(R.id.add_item));
//
//            removeItem = ((TextView) itemView.findViewById(R.id.remove_item));

            itemView.setOnClickListener(this);


        }

        @Override
        public void onClick(View v) {
            clickListener.onItemClick(v, getPosition());
        }
    }

}