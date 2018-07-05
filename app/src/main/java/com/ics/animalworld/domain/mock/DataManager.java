package com.ics.animalworld.domain.mock;

import android.content.Context;

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
import com.ics.animalworld.util.TinyDB;

import java.util.ArrayList;
import java.util.Map;
import java.util.concurrent.ConcurrentHashMap;


public class DataManager {

    private static DataManager fakeServer;
    public ConcurrentHashMap<String, ArrayList<Animals>> productMap = new ConcurrentHashMap<>();
    ;
    private DatabaseReference mDatabase;
    private ArrayList<Animals> animalFoods;
    private int selectedCategory;

    private TinyDB tinydb;

    public static DataManager getInstance(Context context) {

        if (null == fakeServer) {
            fakeServer = new DataManager();
            fakeServer.tinydb = new TinyDB(context);
        }
        return fakeServer;
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

    public void parseAllAnimals(Map<String, Object> animal, int productCategory) {
        ArrayList<Animals> productlist = new ArrayList<Animals>();
        Animals myAnimal;
        Gson gson = new Gson();

        for (String s : animal.keySet()) {
            JsonElement jsonElement = gson.toJsonTree(animal.get(s));
            myAnimal = gson.fromJson(jsonElement, Animals.class);
            productlist.add(myAnimal);
        }

        ArrayList<Animals> animalslist = new ArrayList<Animals>();
        ArrayList<Animals> animalsfood = new ArrayList<Animals>();
        ArrayList<Animals> animalsmedicine = new ArrayList<Animals>();
        ArrayList<Animals> petslist = new ArrayList<Animals>();
        ArrayList<Animals> petsfood = new ArrayList<Animals>();
        ArrayList<Animals> petsmedicine = new ArrayList<Animals>();

        for (Animals item : productlist) {
            if (productCategory == 0) {
                if (item.Category.equals("Animal")) {
                    if (item.Type.equals("Animal"))
                        animalslist.add(item);
                    else if (item.Type.equals("Animal Food"))
                        animalsfood.add(item);
                    else if (item.Type.equals("Animal Medicine"))
                        animalsmedicine.add(item);

                }
            } else if (productCategory == 1) {
                if (item.Category.equals("Pet")) {
                    if (item.Type.equals("Pet"))
                        petslist.add(item);
                    else if (item.Type.equals("Pet Food"))
                        petsfood.add(item);
                    else if (item.Type.equals("Pet Medicine"))
                        petsmedicine.add(item);
                }
            }
        }
        if (productCategory == 0) {
            productMap.put("Animals", animalslist);
            productMap.put("Animal's Food", animalsfood);
            productMap.put("Animal's Medicine", animalsmedicine);

            // also save a copy in db
            tinydb.putListObject("Animals", animalslist);
            tinydb.putListObject("Animal's Food", animalsfood);
            tinydb.putListObject("Animal's Medicine", animalsmedicine);

        } else if (productCategory == 1) {
            productMap.put("Pet", petslist);
            productMap.put("Pet's Food", petsfood);
            productMap.put("Pet's Medicine", petsmedicine);

            tinydb.putListObject("Pet", petslist);
            tinydb.putListObject("Pet's Food", petsfood);
            tinydb.putListObject("Pet's Medicine", petsmedicine);
        }
        CenterRepository.getCenterRepository().clear();
        CenterRepository.getCenterRepository().setMapOfProductsInCategory(new ConcurrentHashMap<>(productMap));

    }

    public boolean hasData(int productCategory) {
        reloadDataFromDB(productCategory);
        if (productMap.size() > 0) {
            for (String key :
                    productMap.keySet()) {
                if (productMap.get(key).size() > 0)
                    return true;
            }
        }
        return false;
    }

    public void reloadDataFromDB(int productCategory) {
        productMap.clear();

        if (productCategory == 0) {
            productMap.put("Animals", tinydb.getListObject("Animals", Animals.class));
            productMap.put("Animal's Food", tinydb.getListObject("Animal's Food", Animals.class));
            productMap.put("Animal's Medicine", tinydb.getListObject("Animal's Medicine", Animals.class));
        } else if (productCategory == 1) {
            productMap.put("Pet", tinydb.getListObject("Pet", Animals.class));
            productMap.put("Pet's Food", tinydb.getListObject("Pet's Food", Animals.class));
            productMap.put("Pet's Medicine", tinydb.getListObject("Pet's Medicine", Animals.class));
        }

        CenterRepository.getCenterRepository().clear();
        CenterRepository.getCenterRepository().setMapOfProductsInCategory(new ConcurrentHashMap<>(productMap));
    }

    /*public void updateProductMapForCategory(String category, ArrayList<Animals> animals) {
        if (productMap == null) {
            productMap = new ConcurrentHashMap<>();
        }
        productMap.put(category, animals);
        CenterRepository.getCenterRepository().setMapOfProductsInCategory(productMap);
    }*/

    public void fetchAllProducts(int productCategory, final DataManagerListener listener) {
        selectedCategory = productCategory;
        mDatabase = FirebaseDatabase.getInstance().getReference().child("Animals");
        mDatabase.addListenerForSingleValueEvent(
                new ValueEventListener() {
                    @Override
                    public void onDataChange(DataSnapshot dataSnapshot) {
                        parseAllAnimals((Map<String, Object>) dataSnapshot.getValue(), selectedCategory);
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


    public interface DataManagerListener {
        void onServiceResponse(boolean success);
    }


}