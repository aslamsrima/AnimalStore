

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
import com.ics.animalworld.util.TinyDB;

import java.util.ArrayList;
import java.util.Map;
import java.util.concurrent.ConcurrentHashMap;


public class FakeWebServer {

    public interface FakeWebServiceResponseListener {
        void onServiceResponse(boolean success);
    }

    private static FakeWebServer fakeServer;
    private DatabaseReference mDatabase;
    private ArrayList<Animals> animalFoods;
    private int selectedCategory;
    ConcurrentHashMap<String, ArrayList<Animals>> productMap;
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

    public void getAllAnimals(Map<String, Object> animal,int productCategory) {

        productMap = new ConcurrentHashMap<String, ArrayList<Animals>>();

        ArrayList<Animals> productlist = new ArrayList<Animals>();
        Animals myAnimal = new Animals();
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

        for (Animals item:productlist  ){
            if(productCategory==0){
            if(item.Category.equals("Animal")){
                if(item.Type.equals("Animal"))
                    animalslist.add(item);
                else if(item.Type.equals("Animal Food"))
                    animalsfood.add(item);
                else if(item.Type.equals("Animal Medicine"))
                    animalsmedicine.add(item);

            }
            }else if(productCategory==1) {
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
        if(productCategory==0) {
            productMap.put("Animals", animalslist);
            productMap.put("Animal's Food", animalsfood);
            productMap.put("Animal's Medicine", animalsmedicine);
        }else if(productCategory==1) {
            productMap.put("Pet", petslist);
            productMap.put("Pet's Food", petsfood);
            productMap.put("Pet's Medicine", petsmedicine);
        }

        CenterRepository.getCenterRepository().setMapOfProductsInCategory(productMap);

    }

    public void getAllPets(Map<String, Object> Pets) {
        ConcurrentHashMap<String, ArrayList<Animals>> productMap = new ConcurrentHashMap<String, ArrayList<Animals>>();

//        ArrayList<Animals> productlist = new ArrayList<Animals>();
//        Animals myAnimal = new Animals();
//        Gson gson = new Gson();
//
//        for (String s : Pets.keySet()) {
//            JsonElement jsonElement = gson.toJsonTree(Pets.get(s));
//            myAnimal = gson.fromJson(jsonElement, Animals.class);
//            productlist.add(myAnimal);
//        }
//
//        productMap.put("Pets", productlist);
//
//        CenterRepository.getCenterRepository().setMapOfProductsInCategory(productMap);


    }

    public void getAllFarmingTools(Map<String, Object> farmingTool) {
/*
        ConcurrentHashMap<String, ArrayList<Animals>> productMap = new ConcurrentHashMap<String, ArrayList<Animals>>();

        ArrayList<Animals> productlist = new ArrayList<Animals>();
        Animals myAnimal = new Animals();
        Gson gson = new Gson();

        for (String s : Pets.keySet()) {
            JsonElement jsonElement = gson.toJsonTree(Pets.get(s));
            myAnimal = gson.fromJson(jsonElement, Animals.class);
            productlist.add(myAnimal);
        }

        productMap.put("Pets", productlist);

        CenterRepository.getCenterRepository().setMapOfProductsInCategory(productMap);
*/


    }

    public void getAllFarmingProducts(Map<String, Object> farmingProduct) {
/*
        ConcurrentHashMap<String, ArrayList<Animals>> productMap = new ConcurrentHashMap<String, ArrayList<Animals>>();

        ArrayList<Animals> productlist = new ArrayList<Animals>();
        Animals myAnimal = new Animals();
        Gson gson = new Gson();

        for (String s : Pets.keySet()) {
            JsonElement jsonElement = gson.toJsonTree(Pets.get(s));
            myAnimal = gson.fromJson(jsonElement, Animals.class);
            productlist.add(myAnimal);
        }

        productMap.put("Pets", productlist);

        CenterRepository.getCenterRepository().setMapOfProductsInCategory(productMap);
*/


    }
    public void getAllProducts(int productCategory, final FakeWebServiceResponseListener listener) {
        selectedCategory=productCategory;
            mDatabase = FirebaseDatabase.getInstance().getReference().child("Animals");
            mDatabase.addListenerForSingleValueEvent(
                    new ValueEventListener() {
                        @Override
                        public void onDataChange(DataSnapshot dataSnapshot) {
                            getAllAnimals((Map<String, Object>) dataSnapshot.getValue(),selectedCategory);
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



}