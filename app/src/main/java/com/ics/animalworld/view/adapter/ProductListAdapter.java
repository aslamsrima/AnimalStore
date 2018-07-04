package com.ics.animalworld.view.adapter;

import android.content.Context;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.support.v7.widget.RecyclerView;
import android.text.Spannable;
import android.text.Spanned;
import android.text.style.StrikethroughSpan;
import android.util.Base64;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.TextView.BufferType;

import com.ics.animalworld.R;
import com.ics.animalworld.model.CenterRepository;
import com.ics.animalworld.model.entities.Animals;
import com.ics.animalworld.model.entities.Money;
import com.ics.animalworld.util.ColorGenerator;
import com.ics.animalworld.view.activities.ECartHomeActivity;
import com.ics.animalworld.view.customview.TextDrawable;
import com.ics.animalworld.view.customview.TextDrawable.IBuilder;

import java.math.BigDecimal;
import java.util.ArrayList;
import java.util.Collections;


public class ProductListAdapter extends
        RecyclerView.Adapter<ProductListAdapter.VersionViewHolder> implements
        ItemTouchHelperAdapter {

    public Context context;
    Bitmap b;
    ImageView imagView;
    private ColorGenerator mColorGenerator = ColorGenerator.MATERIAL;
    private IBuilder mDrawableBuilder;
    private TextDrawable drawable;
    private String subcategory;
    private String ImageUrl;
    private ArrayList<Animals> productList = new ArrayList<Animals>();
    private OnItemClickListener clickListener;

    public ProductListAdapter(String subcategoryKey, Context context,
                              String sortBy) {

        this.subcategory = subcategoryKey;
        productList = CenterRepository.getCenterRepository().getMapOfProductsInCategory()
                .get(subcategoryKey);

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
        productList = CenterRepository.getCenterRepository().getMapOfProductsInCategory()
                .get(subcategory);

        if (productList.get(position).Type.equals("Animal") || productList.get(position).Type.equals("Pet")) {
            holder.itemName.setText(productList.get(position)
                    .Breed + "," + productList.get(position)
                    .SubCategory);
        } else {
            holder.itemName.setText(productList.get(position)
                    .ProductName);
        }
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


        b = StringToBitMap(CenterRepository.getCenterRepository().getMapOfProductsInCategory()
                .get(subcategory).get(position).Pic.toString());

        imagView.setImageBitmap(b);


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

    private ECartHomeActivity getContext() {
        // TODO Auto-generated method stub
        return (ECartHomeActivity) context;
    }

    @Override
    public int getItemCount() {
        int itemcount = CenterRepository.getCenterRepository().getMapOfProductsInCategory()
                .get(this.subcategory) == null ? 0 : CenterRepository.getCenterRepository().getMapOfProductsInCategory()
                .get(this.subcategory).size();
        System.out.println("ProductListFragment, list adapter item count :: " + itemcount);
        return itemcount;
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


            itemName.setSelected(true);

            imagView = ((ImageView) itemView.findViewById(R.id.product_thumb));


            itemView.setOnClickListener(this);


        }

        @Override
        public void onClick(View v) {
            clickListener.onItemClick(v, getPosition());
        }
    }

}