package com.ics.animalworld.view.activities;

import android.Manifest;
import android.content.DialogInterface;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.os.Bundle;
import android.os.Environment;
import android.support.annotation.IntDef;
import android.support.v4.app.ActivityCompat;
import android.support.v4.content.ContextCompat;
import android.support.v7.app.AlertDialog;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import android.util.Base64;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.CheckBox;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.RadioButton;
import android.widget.RadioGroup;
import android.widget.Spinner;
import android.widget.TextView;
import android.widget.Toast;

import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.ics.animalworld.R;
import com.ics.animalworld.model.entities.Animals;

import java.io.ByteArrayOutputStream;
import java.io.File;
import java.lang.annotation.Retention;
import java.lang.annotation.RetentionPolicy;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

public class SellActivity extends AppCompatActivity {
    private RadioButton male, female;
    private TextView milktxt, subCateg;
    private EditText breed, milkRec, Price, Age, description, city, district, supplierContact, price;
    private CheckBox negotiable;
    private Spinner category, type, subCategory;
    private Button AddPost;
    private RadioGroup radioGroup;
    private RadioButton radioButton;
    View appRoot;
    ImageView productImage;
    DatabaseReference mDB;
    DatabaseReference mAnimalRef;
    private ArrayList<Animals> mAnimal;
    Toolbar toolbar;
    Bitmap bitmap;

    // List<String> subCategoryList;
    ArrayAdapter<String> dataAdapter, petAdapter, subCategoryAdapter;
    private static final int CAMERA_REQUEST = 1888;
    public static final int GRANTED = 0;
    public static final int DENIED = 1;
    public static final int BLOCKED_OR_NEVER_ASKED = 2;

    @Retention(RetentionPolicy.SOURCE)
    @IntDef({GRANTED, DENIED, BLOCKED_OR_NEVER_ASKED})
    public @interface PermissionStatus {
    }

    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.frag_sell);
        //getSupportActionBar().setDisplayHomeAsUpEnabled(true);


        category = (Spinner) findViewById(R.id.category);
        subCategory = (Spinner) findViewById(R.id.subCategory);
        //type=(Spinner)findViewById(R.id.type);
        milkRec = (EditText) findViewById(R.id.milkval);
        milktxt = (TextView) findViewById(R.id.milktext);
        appRoot = (View) findViewById(R.id.app_root);
        toolbar = (Toolbar) findViewById(R.id.toolbar);
        subCateg = (TextView) findViewById(R.id.subCategorytext);
        milkRec.setVisibility(View.GONE);
        milktxt.setVisibility(View.GONE);
        radioGroup = (RadioGroup) findViewById(R.id.radio_group);
        //  type.setVisibility(View.GONE);
        productImage = (ImageView) findViewById(R.id.viewImage);
        AddPost = (Button) findViewById(R.id.btnAddPost);

        description = (EditText) findViewById(R.id.reg_description);
        Price = (EditText) findViewById(R.id.price);
        Age = (EditText) findViewById(R.id.reg_age);
        breed = (EditText) findViewById(R.id.reg_breed);
        city = (EditText) findViewById(R.id.reg_city);
        district = (EditText) findViewById(R.id.reg_district);

        supplierContact = (EditText) findViewById(R.id.reg_supplier_contact);
        price = (EditText) findViewById(R.id.price);
        negotiable = (CheckBox) findViewById(R.id.chk_is_negotiable);


        AddPost.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                int selectedId = radioGroup.getCheckedRadioButtonId();

                // find the radiobutton by returned id
                radioButton = (RadioButton) findViewById(selectedId);
                createNewListItem(view);
            }
        });
        ActivityCompat.requestPermissions(SellActivity.this,
                new String[]{Manifest.permission.CAMERA},
                1);

        productImage.setOnClickListener(new View.OnClickListener() {

            @Override

            public void onClick(View v) {

                selectImage();

            }

        });

        List<String> subCategoryListAnimal = new ArrayList<String>();
        subCategoryListAnimal.add("Select Animal");
        subCategoryListAnimal.add("Buffloes");
        subCategoryListAnimal.add("Cows");
        subCategoryListAnimal.add("Goats");
        subCategoryListAnimal.add("Sheeps");
        subCategoryListAnimal.add("Horse");

        List<String> subCategoryListPet = new ArrayList<String>();
        subCategoryListPet.add("Select Pet");
        subCategoryListPet.add("Buffloes");
        subCategoryListPet.add("Cows");
        subCategoryListPet.add("Goats");
        subCategoryListPet.add("Sheeps");
        subCategoryListPet.add("Horse");


        List<String> animalList = new ArrayList<String>();
        animalList.add("Animal");
        animalList.add("Animal Accesories");
        animalList.add("Animal Food");
        animalList.add("Animal Medicine");

        List<String> petList = new ArrayList<String>();
        petList.add("Pet");
        petList.add("Pet Accesories");
        petList.add("Pet Food");
        petList.add("Pet Medicine");
        /* Assign the name array to that adapter and
        also choose a simple layout for the list items */
        dataAdapter = new ArrayAdapter<String>(this,
                android.R.layout.simple_spinner_item, animalList);
        dataAdapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);

        petAdapter = new ArrayAdapter<String>(this,
                android.R.layout.simple_spinner_item, subCategoryListPet);
        petAdapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);


        subCategoryAdapter = new ArrayAdapter<String>(this,
                android.R.layout.simple_spinner_item, subCategoryListAnimal);
        subCategoryAdapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);


        category.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {

            @Override
            public void onItemSelected(AdapterView<?> parentView, View selectedItemView, int position, long id) {
                // your code here
                if (position == 0) {
                    subCateg.setVisibility(View.GONE);
                    milkRec.setVisibility(View.GONE);
                    subCategory.setVisibility(View.GONE);
                    milktxt.setVisibility(View.GONE);
                }
                if (position == 1) {
                    //type.setAdapter(dataAdapter);
                    subCateg.setVisibility(View.VISIBLE);
                    subCategory.setAdapter(subCategoryAdapter);
                    subCategory.setVisibility(View.VISIBLE);
                    milkRec.setVisibility(View.VISIBLE);
                    milktxt.setVisibility(View.VISIBLE);
                } else if (position == 2) {
                    subCategory.setAdapter(petAdapter);
                    subCateg.setVisibility(View.VISIBLE);
                    milkRec.setVisibility(View.GONE);
                    milktxt.setVisibility(View.GONE);
                    subCategory.setVisibility(View.VISIBLE);
                }
            }

            @Override
            public void onNothingSelected(AdapterView<?> parentView) {
                // your code here
            }

        });
        mDB = FirebaseDatabase.getInstance().getReference();
        mAnimalRef = mDB.child("Animals");
        mAnimal = new ArrayList<>();

    }


    private void selectImage() {


        final CharSequence[] options = {"Take Photo", "Choose from Gallery", "Cancel"};


        AlertDialog.Builder builder = new AlertDialog.Builder(SellActivity.this);

        builder.setTitle("Add Photo!");

        builder.setItems(options, new DialogInterface.OnClickListener() {

            @Override

            public void onClick(DialogInterface dialog, int item) {

                if (options[item].equals("Take Photo"))

                {


//                    Intent intent = new Intent(MediaStore.ACTION_IMAGE_CAPTURE);
//
//                    File f = new File(android.os.Environment.getExternalStorageDirectory(), "temp.jpg");
//
//                    intent.putExtra(MediaStore.EXTRA_OUTPUT, Uri.fromFile(f));
//
//                    startActivityForResult(intent, 1);
//                    Intent intent = new Intent(MediaStore.ACTION_IMAGE_CAPTURE);
//                    Uri file = Uri.fromFile(getOutputMediaFile());
//                    intent.putExtra(MediaStore.EXTRA_OUTPUT, file);
//
//                    startActivityForResult(intent, 100);
                    if (getPermissionStatus(SellActivity.this, Manifest.permission.CAMERA) == SellActivity.GRANTED) {
                        Intent cameraIntent = new Intent(android.provider.MediaStore.ACTION_IMAGE_CAPTURE);
                        startActivityForResult(cameraIntent, CAMERA_REQUEST);

                    } else {
                        Toast.makeText(SellActivity.this, "Please Provide Camera Permission", Toast.LENGTH_LONG).show();
                        ActivityCompat.requestPermissions(SellActivity.this,
                                new String[]{Manifest.permission.CAMERA},
                                1);
                    }
                } else if (options[item].equals("Choose from Gallery"))

                {

                    Intent intent = new Intent(Intent.ACTION_PICK, android.provider.MediaStore.Images.Media.EXTERNAL_CONTENT_URI);

                    startActivityForResult(intent, 2);


                } else if (options[item].equals("Cancel")) {

                    dialog.dismiss();

                }

            }

        });

        builder.show();

    }

    private static File getOutputMediaFile() {
        File mediaStorageDir = new File(Environment.getExternalStoragePublicDirectory(
                Environment.DIRECTORY_PICTURES), "CameraDemo");

        if (!mediaStorageDir.exists()) {
            if (!mediaStorageDir.mkdirs()) {
                return null;
            }
        }

        String timeStamp = new SimpleDateFormat("yyyyMMdd_HHmmss").format(new Date());
        return new File(mediaStorageDir.getPath() + File.separator +
                "IMG_" + timeStamp + ".jpg");
    }

    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        if (requestCode == CAMERA_REQUEST && resultCode == RESULT_OK) {
            Bitmap mphoto = (Bitmap) data.getExtras().get("data");
            productImage.setImageBitmap(mphoto);
        }
    }

    @Override
    public void onRequestPermissionsResult(int requestCode,
                                           String permissions[], int[] grantResults) {
        switch (requestCode) {
            case 1: {

                // If request is cancelled, the result arrays are empty.
                if (grantResults.length > 0
                        && grantResults[0] == PackageManager.PERMISSION_GRANTED) {

                    // permission was granted, yay! Do the
                    // contacts-related task you need to do.
                } else {

                    // permission denied, boo! Disable the
                    // functionality that depends on this permission.
//                    Toast.makeText(SellActivity.this, "Permission denied for camera", Toast.LENGTH_SHORT).show();
//                    super.onRequestPermissionsResult(requestCode, permissions, grantResults);
                }
                return;
            }
            default:
                super.onRequestPermissionsResult(requestCode, permissions, grantResults);

                // other 'case' lines to check for other
                // permissions this app might request
        }
    }

    @PermissionStatus
    public static int getPermissionStatus(SellActivity activity, String androidPermissionName) {
        if (ContextCompat.checkSelfPermission(activity, androidPermissionName) != PackageManager.PERMISSION_GRANTED) {
            if (!ActivityCompat.shouldShowRequestPermissionRationale(activity, androidPermissionName)) {
                return BLOCKED_OR_NEVER_ASKED;
            }
            return DENIED;
        }
        return GRANTED;
    }

    public void createNewListItem(View v) {
        // Create new List Item  at /listItem

        final String key = FirebaseDatabase.getInstance().getReference().child("Animals").push().getKey();
        //LayoutInflater li = LayoutInflater.from(this);
        //View getListItemView = li.inflate(R.layout.dialog_get_list_item, null);

        //String listItemText = userInput.getText().toString();
//        ByteArrayOutputStream stream = new ByteArrayOutputStream();
//
//        bitmap.compress(Bitmap.CompressFormat.JPEG, 100, stream);
//        byte[] byteFormat = stream.toByteArray();
//        String encodedImage = Base64.encodeToString(byteFormat, Base64.NO_WRAP);

        Bitmap largeIcon = BitmapFactory.decodeResource(getResources(), R.drawable.cart);
        java.text.DateFormat dateFormat = android.text.format.DateFormat.getDateFormat(this);
        Animals animal = new Animals();
        animal.Age = Integer.parseInt(Age.getText().toString());
        animal.Breed = breed.getText().toString();
        animal.Category = category.getSelectedItem().toString();
        animal.City = city.getText().toString();
        animal.SubCategory = subCategory.getSelectedItem().toString();
        animal.Description = description.getText().toString();
        animal.District = district.getText().toString();
        animal.Gender = radioButton.getText().toString();
        animal.ID = 1;
        animal.IsPrizeNegoyiable = true;
        animal.MlkRec = Integer.parseInt(milkRec.getText().toString());
        animal.Prize = Integer.parseInt(price.getText().toString());
        animal.CreatedOn = new Date().toString();
        animal.Status = "Review";
        animal.Pic = BitMapToString(largeIcon);
        animal.SupplierContact = supplierContact.getText().toString();


        Map<String, Object> listItemValues = animal.toMap(animal);
        Map<String, Object> childUpdates = new HashMap<>();
        childUpdates.put("/Animals/" + key, listItemValues);
        FirebaseDatabase.getInstance().getReference().updateChildren(childUpdates);
        // set dialog message


    }

    public String BitMapToString(Bitmap bitmap) {
        ByteArrayOutputStream baos = new ByteArrayOutputStream();
        bitmap.compress(Bitmap.CompressFormat.PNG, 100, baos);
        byte[] b = baos.toByteArray();
        String temp = Base64.encodeToString(b, Base64.DEFAULT);
        return temp;
    }


}