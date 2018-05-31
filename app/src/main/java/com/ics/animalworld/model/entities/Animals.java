package com.ics.animalworld.model.entities;

import android.graphics.Bitmap;

import com.google.firebase.database.Exclude;

import java.util.Date;
import java.util.HashMap;
import java.util.Map;

public class Animals {
    public int ID;
    public String Category;
  //  public String SubCategory;
    public int Age;
    public String Breed;
    public String Gender;
    public double Prize;
    public boolean IsPrizeNegoyiable;
    public double MlkRec;
    public String Description;
    public String Status;
    public String SupplierContact;
    public String City;
    public String District;
    public String State;
  //  public Bitmap Pic;
  //  public Date CreatedOn;
  //  public Date UpdatedOn;

    public Animals() {
        // Default constructor required for calls to DataSnapshot.getValue(ListItem.class)
    }
    @Exclude
    public Map<String, Object> toMap(Animals animal) {
        HashMap<String, Object> result = new HashMap<>();
        result.put("ID", animal.ID);
        result.put("Category", animal.Category);
      //  result.put("SubCategory", animal.SubCategory);
        result.put("Age", animal.Age);
        result.put("Breed", animal.Breed);
        result.put("Gender", animal.Gender);
        result.put("Prize", animal.Prize);
        result.put("IsPrizeNegoyiable", animal.IsPrizeNegoyiable);
        result.put("MlkRec", animal.MlkRec);
        result.put("Description", animal.Description);
        result.put("Status", animal.Status);
        result.put("SupplierContact", animal.SupplierContact);
        result.put("City", animal.City);
        result.put("District", animal.District);
        result.put("State", animal.State);
      /*  result.put("Pic", animal.Pic);
        result.put("CreatedOn", animal.CreatedOn);
        result.put("UpdatedOn", animal.UpdatedOn);*/

        return result;
    }

}
