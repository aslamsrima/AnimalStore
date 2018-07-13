package com.ics.animalworld.domain.mock;

import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;
import com.google.gson.Gson;
import com.google.gson.JsonElement;
import com.ics.animalworld.model.CenterRepository;
import com.ics.animalworld.model.entities.Animals;
import com.ics.animalworld.model.entities.ProductCategoryModel;

import java.util.ArrayList;
import java.util.Map;
import java.util.concurrent.ConcurrentHashMap;


public class FakeWebServer {

    private static FakeWebServer fakeServer;
    public ConcurrentHashMap<String, ArrayList<Animals>> productMap;
    private DatabaseReference mDatabase;
    private ArrayList<Animals> animalFoods;
    private int selectedCategory;

    public static FakeWebServer getFakeWebServer() {

        if (null == fakeServer) {
            fakeServer = new FakeWebServer();
        }
        return fakeServer;
    }

    void initiateFakeServer() {

        addCategory();

    }

    public void addCategory() {

        ArrayList<ProductCategoryModel> listOfCategory = new ArrayList<ProductCategoryModel>();

        listOfCategory
                .add(new ProductCategoryModel(
                        "Animals",
                        "Animals Items",
                        "New",
                        "http://images6.fanpop.com/image/photos/37400000/Cow-animals-37411834-500-313.jpg"));

        listOfCategory
                .add(new ProductCategoryModel(
                        "Pets",
                        "Pets Items",
                        "New",
                        "https://images8.alphacoders.com/496/496528.jpg"));
        listOfCategory
                .add(new ProductCategoryModel(
                        "Farming Tools",
                        "Farming Items",
                        "New",
                        "http://www.beatmagazinesa.co.za/wp-content/uploads/2015/02/agriculture.jpg"));

        listOfCategory
                .add(new ProductCategoryModel(
                        "Farming Products",
                        "Farming Items",
                        "New",
                        ""));

        CenterRepository.getCenterRepository().setListOfCategory(listOfCategory);
    }

    public void getAllAnimals(Map<String, Object> animal, int productCategory) {

        if (animal != null) {
            productMap = new ConcurrentHashMap<String, ArrayList<Animals>>();

            ArrayList<Animals> productlist = new ArrayList<Animals>();
            Animals myAnimal = new Animals();
            Gson gson = new Gson();

            for (String s : animal.keySet()) {
                JsonElement jsonElement = gson.toJsonTree(animal.get(s));
                myAnimal = gson.fromJson(jsonElement, Animals.class);
                productlist.add(myAnimal);
            }

            ArrayList<Animals> list = new ArrayList<Animals>();
            ArrayList<Animals> food = new ArrayList<Animals>();
            ArrayList<Animals> medicine = new ArrayList<Animals>();

            for (Animals item : productlist) {
                if (productCategory == 0) {
                    if (item.Category.equals("Animal")) {
                        if (item.Type.equals("Animal"))
                            list.add(item);
                        else if (item.Type.equals("Animal Food"))
                            food.add(item);
                        else if (item.Type.equals("Animal Medicine"))
                            medicine.add(item);

                    }
                } else if (productCategory == 1) {
                    if (item.Category.equals("Pet")) {
                        if (item.Type.equals("Pet"))
                            list.add(item);
                        else if (item.Type.equals("Pet Food"))
                            food.add(item);
                        else if (item.Type.equals("Pet Medicine"))
                            medicine.add(item);
                    }
                } else if (productCategory == 2) {
                    if (item.Category.equals("Farming Tool")) {
                        list.add(item);
                    }
                } else if (productCategory == 3) {
                    if (item.Category.equals("Farming Product")) {
                        list.add(item);
                    }
                }
            }
            if (productCategory == 0) {
                productMap.put("Animal", list);
                productMap.put("Animal's Food", food);
                productMap.put("Animal's Medicine", medicine);
            } else if (productCategory == 1) {
                productMap.put("Pet", list);
                productMap.put("Pet's Food", food);
                productMap.put("Pet's Medicine", medicine);
            } else if (productCategory == 2) {
                productMap.put("Farming Tool", list);
            } else if (productCategory == 3) {
                productMap.put("Farming Product", list);
            }
            CenterRepository.getCenterRepository().clear();
            CenterRepository.getCenterRepository().setMapOfProductsInCategory(productMap);

        }
    }

    public void updateProductMapForCategory(String category, ArrayList<Animals> animals) {
        if (productMap == null) {
            productMap = new ConcurrentHashMap<>();
        }
        productMap.put(category, animals);
        CenterRepository.getCenterRepository().setMapOfProductsInCategory(productMap);
    }

    public void getAllProducts(int productCategory, final FakeWebServiceResponseListener listener) {
        selectedCategory = productCategory;
        if (productCategory == 0) {
            mDatabase = FirebaseDatabase.getInstance().getReference().child("Animal");
        } else if (productCategory == 1) {
            mDatabase = FirebaseDatabase.getInstance().getReference().child("Pet");
        } else if (productCategory == 2) {
            mDatabase = FirebaseDatabase.getInstance().getReference().child("Farming Tool");
        } else if (productCategory == 3)
            mDatabase = FirebaseDatabase.getInstance().getReference().child("Farming Product");

        mDatabase.addListenerForSingleValueEvent(
                new ValueEventListener() {
                    @Override
                    public void onDataChange(DataSnapshot dataSnapshot) {
                        getAllAnimals((Map<String, Object>) dataSnapshot.getValue(), selectedCategory);
                        if (listener != null)
                            listener.onServiceResponse(true);
                    }

                    @Override
                    public void onCancelled(DatabaseError databaseError) {
                        //handle databaseError
                        if (listener != null)
                            listener.onServiceResponse(false);
                    }
                });


    }


    public interface FakeWebServiceResponseListener {
        void onServiceResponse(boolean success);
    }


}