package com.ics.animalworld.model.entities;

import com.google.firebase.database.Exclude;

import java.util.HashMap;
import java.util.Map;

public class Animals {
    public int ID;
    public String Category;
    public String SubCategory;
    public String Type;
    public int Age;
    public String Breed;
    public String Gender;
    public double Prize;
    public boolean IsPrizeNegotiable;
    public double MlkRec;
    public String Description;
    public String Status;
    public String SupplierContact;
    public String SupplierName;
    public String City;
    public String District;
    public String State;
    public String Pic;
    public String CreatedOn;
    public String ProductName;
    public String CompanyName;
    public String weight;

    //  public Date UpdatedOn;

    public Animals() {
        // Default constructor required for calls to DataSnapshot.getValue(ListItem.class)
    }

    @Exclude
    public Map<String, Object> toMap(Animals animal) {
        HashMap<String, Object> result = new HashMap<>();
        result.put("ID", animal.ID);
        result.put("Category", animal.Category);
        result.put("SubCategory", animal.SubCategory);
        result.put("Type",animal.Type);
        result.put("Age", animal.Age);
        result.put("Breed", animal.Breed);
        result.put("Gender", animal.Gender);
        result.put("Prize", animal.Prize);
        result.put("IsPrizeNegotiable", animal.IsPrizeNegotiable);
        result.put("MlkRec", animal.MlkRec);
        result.put("Description", animal.Description);
        result.put("Status", animal.Status);
        result.put("SupplierContact", animal.SupplierContact);
        result.put("SupplierName", animal.SupplierName);
        result.put("City", animal.City);
        result.put("District", animal.District);
        result.put("State", animal.State);
        result.put("Pic", animal.Pic);
        result.put("CreatedOn", animal.CreatedOn);
        result.put("ProductName", animal.ProductName);
        result.put("CompanyName", animal.CompanyName);
        result.put("weight", animal.weight);


        /* result.put("UpdatedOn", animal.UpdatedOn);*/

        return result;
    }

}
