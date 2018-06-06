

package com.ics.animalworld.domain.helper;

import android.util.Log;

import com.ics.animalworld.model.CenterRepository;
import com.ics.animalworld.model.entities.Animals;
import com.ics.animalworld.model.entities.ProductCategoryModel;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.lang.reflect.Field;
import java.util.ArrayList;
import java.util.List;

public class JSONParser {

    private int networktaskType;
    private String jsonResponse;

    /**
     * @param networktaskType
     * @param jsonResponse
     */
    public JSONParser(int networktaskType, String jsonResponse) {
        super();
        this.networktaskType = networktaskType;
        this.jsonResponse = jsonResponse;
    }

    public static JSONObject toJSON(Object object) throws JSONException,
            IllegalAccessException {
        Class c = object.getClass();
        JSONObject jsonObject = new JSONObject();
        for (Field field : c.getDeclaredFields()) {
            field.setAccessible(true);
            String name = field.getName();
            String value = String.valueOf(field.get(object));
            jsonObject.put(name, value);
        }
        System.out.println(jsonObject.toString());

        return jsonObject;
    }


    public static JSONArray toJSONFromList(List list) throws JSONException,
            IllegalAccessException {
        JSONArray jsonArray = new JSONArray();
        for (Object i : list) {
            JSONObject jstr = toJSON(i);
            // JSONObject jsonObject = new JSONObject(jstr);
            jsonArray.put(jstr);
        }
        return jsonArray;
    }

    public void parse() {

        if (jsonResponse != null) {
            try {
                switch (networktaskType) {
                    case NetworkConstants.GET_ALL_PRODUCT_BY_CATEGORY:

                        if (NetworkConstants.DEBUGABLE)
                            Log.e("Parse: ", "GetAllCategoryTask " + jsonResponse);

                        JSONArray categoryArray = new JSONArray(jsonResponse);

                        CenterRepository.getCenterRepository().getListOfCategory()
                                .clear();

                        for (int i = 0; i < categoryArray.length(); i++) {
                            CenterRepository
                                    .getCenterRepository()
                                    .getListOfCategory()
                                    .add(new ProductCategoryModel(categoryArray
                                            .getJSONObject(i).getString(
                                                    "categoryName"), categoryArray
                                            .getJSONObject(i).getString(
                                                    "productDescription"),
                                            categoryArray.getJSONObject(i)
                                                    .getString("productDiscount"),
                                            categoryArray.getJSONObject(i)
                                                    .getString("productUrl")));

                        }

                        break;

                    case NetworkConstants.GET_ALL_PRODUCT:

                        if (NetworkConstants.DEBUGABLE)
                            Log.e("Parse: ", "GetAllProductFromCategoryTask "
                                    + jsonResponse);

                        JSONObject productMapObject = new JSONObject(jsonResponse);

                        CenterRepository.getCenterRepository().getMapOfProductsInCategory()
                                .clear();

                        for (int categoryCount = 0; categoryCount < CenterRepository
                                .getCenterRepository().getListOfCategory().size(); categoryCount++) {

                            ArrayList<Animals> tempProductList = new ArrayList<Animals>();

                            // get json array for stored category

                            if (productMapObject.optJSONArray(CenterRepository
                                    .getCenterRepository().getListOfCategory()
                                    .get(categoryCount).getProductCategoryName()) != null) {

                                JSONArray productListWithCategory = productMapObject
                                        .getJSONArray(CenterRepository
                                                .getCenterRepository()
                                                .getListOfCategory()
                                                .get(categoryCount)
                                                .getProductCategoryName());

                                System.out.println(CenterRepository
                                        .getCenterRepository().getListOfCategory()
                                        .get(categoryCount)
                                        .getProductCategoryName());


                                for (int i = 0; i < productListWithCategory
                                        .length(); i++) {

                                    // get all product in that category
                                    JSONObject productListObjecty = productListWithCategory
                                            .getJSONObject(i);

                                    if (productListObjecty.length() != 0) {

                                        tempProductList.add(new Animals(

                                        ));

                                        Log.d("Parse:GetAllProduct",
                                                "tempProductList" + tempProductList);

                                    }

                                }
                            }
                            CenterRepository
                                    .getCenterRepository()
                                    .getMapOfProductsInCategory()
                                    .put(String.valueOf(categoryCount),
                                            tempProductList);

                        }

                        break;


                    case NetworkConstants.GET_SHOPPING_LIST:

                        if (NetworkConstants.DEBUGABLE)
                            Log.e("Parse: ", "GetAllProductFromCategoryTask "
                                    + jsonResponse);

                        CenterRepository.getCenterRepository().getListOfProductsInShoppingList()
                                .clear();

                        JSONArray mycartArray = new JSONArray(jsonResponse);


                        ArrayList<Animals> tempProductList = new ArrayList<Animals>();


                        for (int i = 0; i < mycartArray.length(); i++) {

                            // get all product in that category
                            JSONObject productListObjecty = mycartArray
                                    .getJSONObject(i);

                            if (productListObjecty.length() != 0) {

                                CenterRepository
                                        .getCenterRepository()
                                        .getListOfProductsInShoppingList().add(new Animals(
                                ));

                                Log.d("GetAllProduct",
                                        "tempProductList" + tempProductList);

                            }

                        }


                        break;

                    default:
                        break;
                }

                // TODO parse JSON acc to request
            } catch (JSONException e) {
                e.printStackTrace();
            }
        } else {
            Log.e("ServiceHandler", "Couldn't get any data from the url");
        }

    }

}