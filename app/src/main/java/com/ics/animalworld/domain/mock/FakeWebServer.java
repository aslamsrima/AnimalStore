

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

    public interface FakeWebServiceResponseListener {
        void onServiceResponse(boolean success);
    }

    private static FakeWebServer fakeServer;
    private DatabaseReference mDatabase;
    private ArrayList<Animals> animalFoods;
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
                        "https://cdn.pixabay.com/photo/2017/08/04/09/39/indian-cow-2579534_960_720.jpg"));

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

    public void getAllAnimals(Map<String, Object> animal,final FakeWebServiceResponseListener listener) {

        productMap = new ConcurrentHashMap<String, ArrayList<Animals>>();

        ArrayList<Animals> productlist = new ArrayList<Animals>();
        Animals myAnimal = new Animals();
        Gson gson = new Gson();

        for (String s : animal.keySet()) {
            JsonElement jsonElement = gson.toJsonTree(animal.get(s));
            myAnimal = gson.fromJson(jsonElement, Animals.class);
            productlist.add(myAnimal);
        }

        productMap.put("Animals", productlist);

        CenterRepository.getCenterRepository().setMapOfProductsInCategory(productMap);

        getAnimalFoods(new FakeWebServiceResponseListener(){
            @Override
            public void onServiceResponse(boolean success) {
                if (success) {
                    try {
                        productMap.put("Animals Foods", animalFoods);
                        CenterRepository.getCenterRepository().setMapOfProductsInCategory(productMap);
                        if (listener != null)
                            listener.onServiceResponse(true);
                    } catch (Exception e) {
                        e.printStackTrace();
                    }
                }
            }
        });




    }

    public void getAllPets(Map<String, Object> Pets) {
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

        if (productCategory == 0) {

            mDatabase = FirebaseDatabase.getInstance().getReference().child("Animals");
            mDatabase.addListenerForSingleValueEvent(
                    new ValueEventListener() {
                        @Override
                        public void onDataChange(DataSnapshot dataSnapshot) {
                            getAllAnimals((Map<String, Object>) dataSnapshot.getValue(),new FakeWebServiceResponseListener(){
                                @Override
                                public void onServiceResponse(boolean success) {
                                    if (success) {
                                        try {
                                            if (listener != null)
                                                listener.onServiceResponse(true);
                                        } catch (Exception e) {
                                            e.printStackTrace();
                                        }
                                    }
                                }
                            });

                        }

                        @Override
                        public void onCancelled(DatabaseError databaseError) {
                            //handle databaseError
                            if (listener != null)
                                listener.onServiceResponse(false);
                        }
                    });

        } else if(productCategory == 1) {
            mDatabase = FirebaseDatabase.getInstance().getReference().child("Pets");
            mDatabase.addListenerForSingleValueEvent(
                    new ValueEventListener() {
                        @Override
                        public void onDataChange(DataSnapshot dataSnapshot) {
                            getAllPets((Map<String, Object>) dataSnapshot.getValue());
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


        }else if(productCategory == 2){
            mDatabase = FirebaseDatabase.getInstance().getReference().child("FarmingTools");
            mDatabase.addListenerForSingleValueEvent(
                    new ValueEventListener() {
                        @Override
                        public void onDataChange(DataSnapshot dataSnapshot) {
                            getAllFarmingTools((Map<String, Object>) dataSnapshot.getValue());
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


        }else if(productCategory ==3){
            mDatabase = FirebaseDatabase.getInstance().getReference().child("FarmingProducts");
            mDatabase.addListenerForSingleValueEvent(
                    new ValueEventListener() {
                        @Override
                        public void onDataChange(DataSnapshot dataSnapshot) {
                            getAllFarmingProducts((Map<String, Object>) dataSnapshot.getValue());
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
    private void getAnimalFoods(final FakeWebServiceResponseListener listener){
        mDatabase = FirebaseDatabase.getInstance().getReference().child("Animal_Foods");
        mDatabase.addListenerForSingleValueEvent(
                new ValueEventListener() {
                    @Override
                    public void onDataChange(DataSnapshot dataSnapshot) {
                        Map<String, Object> animalFood = (Map<String, Object>) dataSnapshot.getValue();
                        animalFoods = new ArrayList<Animals>();
                        Animals AnimalFood = new Animals();
                        Gson gson = new Gson();

                        for (String s : animalFood.keySet()) {
                            JsonElement jsonElement = gson.toJsonTree(animalFood.get(s));
                            AnimalFood = gson.fromJson(jsonElement, Animals.class);
                            animalFoods.add(AnimalFood);
                            if (listener != null)
                                listener.onServiceResponse(true);
                        }

                    }

                    @Override
                    public void onCancelled(DatabaseError databaseError) {
                        //handle databaseError
                        if (listener != null)
                            listener.onServiceResponse(false);
                    }
                });
        //return animalFoods;
    };


}