

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
                        "10%",
                        "https://cdn.pixabay.com/photo/2017/08/04/09/39/indian-cow-2579534_960_720.jpg"));

        listOfCategory
                .add(new ProductCategoryModel(
                        "Pets",
                        "Pets Items",
                        "15%",
                        "https://images8.alphacoders.com/496/496528.jpg"));

        CenterRepository.getCenterRepository().setListOfCategory(listOfCategory);
    }

    public void getAllAnimals(Map<String, Object> animal) {

        ConcurrentHashMap<String, ArrayList<Animals>> productMap = new ConcurrentHashMap<String, ArrayList<Animals>>();

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

    }

    public void getAllPets() {


    }

    public void getAllProducts(int productCategory, final FakeWebServiceResponseListener listener) {

        if (productCategory == 0) {
            mDatabase = FirebaseDatabase.getInstance().getReference().child("Animals");
            mDatabase.addListenerForSingleValueEvent(
                    new ValueEventListener() {
                        @Override
                        public void onDataChange(DataSnapshot dataSnapshot) {
                            getAllAnimals((Map<String, Object>) dataSnapshot.getValue());
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

        } else {

            getAllPets();

        }

    }

}