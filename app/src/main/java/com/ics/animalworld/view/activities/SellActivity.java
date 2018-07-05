package com.ics.animalworld.view.activities;

import android.Manifest;
import android.content.DialogInterface;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.content.res.Configuration;
import android.database.Cursor;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.graphics.Matrix;
import android.net.Uri;
import android.os.Bundle;
import android.os.Environment;
import android.provider.MediaStore;
import android.support.annotation.IntDef;
import android.support.annotation.NonNull;
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

import com.basgeekball.awesomevalidation.AwesomeValidation;
import com.basgeekball.awesomevalidation.ValidationStyle;
import com.basgeekball.awesomevalidation.utility.RegexTemplate;
import com.google.android.gms.tasks.OnFailureListener;
import com.google.android.gms.tasks.OnSuccessListener;
import com.google.common.collect.Range;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.storage.FirebaseStorage;
import com.google.firebase.storage.StorageReference;
import com.google.firebase.storage.UploadTask;
import com.ics.animalworld.R;
import com.ics.animalworld.model.entities.Animals;
import com.ics.animalworld.util.TinyDB;

import java.io.ByteArrayOutputStream;
import java.io.File;
import java.lang.annotation.Retention;
import java.lang.annotation.RetentionPolicy;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;
import java.util.HashMap;
import java.util.List;
import java.util.Locale;
import java.util.Map;

public class SellActivity extends AppCompatActivity {
    public static final int GRANTED = 0;
    public static final int DENIED = 1;
    public static final int BLOCKED_OR_NEVER_ASKED = 2;
    private static final int CAMERA_REQUEST = 1888;
    View appRoot;
    ImageView productImage;
    DatabaseReference mDB;
    DatabaseReference mAnimalRef;
    Toolbar toolbar;
    Bitmap bitmap;
    Bitmap FINAL_IMAGE;
    ArrayAdapter<String> dataAdapter, petAdapter, subCategoryAdapter, petTypeAdapter;
    private RadioButton male, female;
    private TextView subCateg, typeTxt, genderTxt;
    private EditText breed, milkRec, Price, Age, description, city, district, supplierContact, price, product_name, company_name, weight;
    private CheckBox negotiable;
    private Spinner category, type, subCategory;
    private Button AddPost;
    private RadioGroup radioGroup;
    private RadioButton radioButton;
    private ArrayList<Animals> mAnimal;
    private AwesomeValidation awesomeValidation;
    private String language;
    Animals animal;

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

    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.frag_sell);
        TinyDB tiny = new TinyDB(getApplicationContext());
        language = tiny.getString("lang");
        if (language.equalsIgnoreCase(""))
            return;
        Locale myLocale = new Locale(language);//Set Selected Locale
        //  saveLocale(lang);//Save the selected locale
        Locale.setDefault(myLocale);//set new locale as default
        Configuration config = new Configuration();//get Configuration
        config.locale = myLocale;//set config locale as selected locale
        getApplicationContext().getResources().updateConfiguration(config, getApplicationContext().getResources().getDisplayMetrics());//Update the config
        // updateTexts();//Update texts according to locale

        //Initialized view elements
        category = (Spinner) findViewById(R.id.category);
        subCategory = (Spinner) findViewById(R.id.subCategory);
        type = (Spinner) findViewById(R.id.type);
        typeTxt = (TextView) findViewById(R.id.typetext);
        milkRec = (EditText) findViewById(R.id.milkval);
        milkRec.setVisibility(View.GONE);
        appRoot = (View) findViewById(R.id.app_root);
        toolbar = (Toolbar) findViewById(R.id.toolbar);
        subCateg = (TextView) findViewById(R.id.subCategorytext);
        radioGroup = (RadioGroup) findViewById(R.id.gender_group);
        productImage = (ImageView) findViewById(R.id.viewImage);
        product_name = (EditText) findViewById(R.id.reg_product_name);
        company_name = (EditText) findViewById(R.id.reg_company_name);
        weight = (EditText) findViewById(R.id.reg_weight);
        description = (EditText) findViewById(R.id.reg_description);
        Price = (EditText) findViewById(R.id.price);
        Age = (EditText) findViewById(R.id.reg_age);
        breed = (EditText) findViewById(R.id.reg_breed);
        city = (EditText) findViewById(R.id.reg_city);
        district = (EditText) findViewById(R.id.reg_district);
        genderTxt = (TextView) findViewById(R.id.GenderTxt);
        supplierContact = (EditText) findViewById(R.id.reg_supplier_contact);
        price = (EditText) findViewById(R.id.price);
        negotiable = (CheckBox) findViewById(R.id.chk_is_negotiable);
        AddPost = (Button) findViewById(R.id.btnAddPost);
        AddPost.setClickable(false);

        //Submit OnClick Listener
        AddPost.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                int selectedId = radioGroup.getCheckedRadioButtonId();
                // find the radiobutton by returned id
                radioButton = (RadioButton) findViewById(selectedId);
                submitForm(view);

            }
        });

        //Camera Permissions
        ActivityCompat.requestPermissions(SellActivity.this,
                new String[]{Manifest.permission.CAMERA},
                1);
        ActivityCompat.requestPermissions(this, new String[]{Manifest.permission.WRITE_EXTERNAL_STORAGE}, 2);
        ActivityCompat.requestPermissions(this, new String[]{Manifest.permission.READ_EXTERNAL_STORAGE}, 3);


        productImage.setOnClickListener(new View.OnClickListener() {

            @Override
            public void onClick(View v) {
                selectImage();
            }

        });

        //SubCategory List(Animals)


        List<String> subCategoryListAnimal = new ArrayList<String>();
        if (language.equals("en")) {
            subCategoryListAnimal.add("Select Animal");
            subCategoryListAnimal.add("Buffloes");
            subCategoryListAnimal.add("Cows");
            subCategoryListAnimal.add("Goats");
            subCategoryListAnimal.add("Horse");
            subCategoryListAnimal.add("Rabbit");
            subCategoryListAnimal.add("Sheeps");

        } else if (language.equals("hi")) {
            subCategoryListAnimal.add("कृपया चुने");
            subCategoryListAnimal.add("भेंस");
            subCategoryListAnimal.add("गाय");
            subCategoryListAnimal.add("बकरा");
            subCategoryListAnimal.add("घोड़ा");
            subCategoryListAnimal.add("खरगोश");
            subCategoryListAnimal.add("भेड़");
        } else if (language.equals("mr")) {
            subCategoryListAnimal.add("कृपया प्राणी निवडा");
            subCategoryListAnimal.add("म्हैस");
            subCategoryListAnimal.add("गाय");
            subCategoryListAnimal.add("शेळी");
            subCategoryListAnimal.add("अश्व");
            subCategoryListAnimal.add("खरगोश");
            subCategoryListAnimal.add("मेंढी");
        }

        subCategoryAdapter = new ArrayAdapter<String>(this, android.R.layout.simple_spinner_item, subCategoryListAnimal);
        subCategoryAdapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);


        //SubCategory List(Pets)
        List<String> subCategoryListPet = new ArrayList<String>();
        if (language.equals("en")) {
            subCategoryListPet.add("Select Pet");
            subCategoryListPet.add("Cat");
            subCategoryListPet.add("Dog");
            subCategoryListPet.add("Bird");
            subCategoryListPet.add("Fish");
        } else if (language.equals("hi")) {
            subCategoryListPet.add("कृपया चुने");
            subCategoryListPet.add("बिल्ली");
            subCategoryListPet.add("कुत्ता");
            subCategoryListPet.add("पक्षी");
            subCategoryListPet.add("मछली");
        } else if (language.equals("mr")) {
            subCategoryListPet.add("कृपया पशु निवडा");
            subCategoryListPet.add("मांजर");
            subCategoryListPet.add("कुत्रा");
            subCategoryListPet.add("पक्षी");
            subCategoryListPet.add("मासे");
        }

        petAdapter = new ArrayAdapter<String>(this, android.R.layout.simple_spinner_item, subCategoryListPet);
        petAdapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);


        //Type List(Animals)
        List<String> animalList = new ArrayList<String>();
        if (language.equals("en")) {
            animalList.add("Animal");
            animalList.add("Animal Food");
            animalList.add("Animal Medicine");
        } else if (language.equals("hi")) {
            animalList.add("जानवर");
            animalList.add("जानवर का भोजन");
            animalList.add("जानवर की दवाई");
        } else if (language.equals("mr")) {
            animalList.add("प्राणी");
            animalList.add("प्राण्यांचे अन्न");
            animalList.add("प्राण्यांचे औषधे");
        }

        dataAdapter = new ArrayAdapter<String>(this, android.R.layout.simple_spinner_item, animalList);
        dataAdapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);


        //Type List(Pets)
        List<String> petList = new ArrayList<String>();
        if (language.equals("en")) {
            petList.add("Pets");
            petList.add("Pet's Food");
            petList.add("Pet's Medicine");
        } else if (language.equals("hi")) {
            petList.add("पालतू जानवर");
            petList.add("पालतू जानवर का खाना");
            petList.add("पालतू जानवर की दवाई");
        } else if (language.equals("mr")) {
            petList.add("पाळीव प्राणी");
            petList.add("पाळीव प्राण्याचे अन्न");
            petList.add("पाळीव प्राण्याचे औषध");
        }

        petTypeAdapter = new ArrayAdapter<String>(this, android.R.layout.simple_spinner_item, petList);
        petTypeAdapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);


        //Category DropDown On select
        category.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {

            @Override
            public void onItemSelected(AdapterView<?> parentView, View selectedItemView, int position, long id) {
                if (position == 0) {
                    subCateg.setVisibility(View.GONE);
                    milkRec.setVisibility(View.GONE);
                    subCategory.setVisibility(View.GONE);
                    type.setVisibility(View.GONE);
                    typeTxt.setVisibility(View.GONE);
                    product_name.setVisibility(View.GONE);
                    company_name.setVisibility(View.GONE);
                    weight.setVisibility(View.GONE);
                    AddPost.setClickable(false);
                }
                if (position == 1) {
                    AddPost.setClickable(true);
                    milkRec.setVisibility(View.VISIBLE);
                    type.setAdapter(dataAdapter);
                    type.setVisibility(View.VISIBLE);
                    typeTxt.setVisibility(View.VISIBLE);
                    subCategory.setAdapter(subCategoryAdapter);
                    product_name.setVisibility(View.GONE);
                    company_name.setVisibility(View.GONE);
                    weight.setVisibility(View.GONE);
                } else if (position == 2) {
                    AddPost.setClickable(true);
                    type.setAdapter(petTypeAdapter);
                    type.setVisibility(View.VISIBLE);
                    typeTxt.setVisibility(View.VISIBLE);
                    subCategory.setAdapter(petAdapter);
                    subCateg.setVisibility(View.VISIBLE);
                    milkRec.setVisibility(View.GONE);
                    subCategory.setVisibility(View.VISIBLE);
                    product_name.setVisibility(View.GONE);
                    company_name.setVisibility(View.GONE);
                    weight.setVisibility(View.GONE);
                }
            }

            @Override
            public void onNothingSelected(AdapterView<?> parentView) {
                // your code here
            }

        });

        //Type dropdown onSelect
        type.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
            @Override
            public void onItemSelected(AdapterView<?> parentView, View selectedItemView, int position, long id) {
                if (position == 0) {
                    subCateg.setVisibility(View.VISIBLE);
                    subCategory.setVisibility(View.VISIBLE);
                    product_name.setVisibility(View.GONE);
                    company_name.setVisibility(View.GONE);
                    weight.setVisibility(View.GONE);
                    genderTxt.setVisibility(View.VISIBLE);
                    radioGroup.setVisibility(View.VISIBLE);
                    Age.setVisibility(View.VISIBLE);
                    breed.setVisibility(View.VISIBLE);
                    addValidationToViews();
                }
                if (position == 1) {
                    subCateg.setVisibility(View.VISIBLE);
                    milkRec.setVisibility(View.GONE);
                    subCategory.setVisibility(View.VISIBLE);
                    product_name.setVisibility(View.VISIBLE);
                    company_name.setVisibility(View.VISIBLE);
                    weight.setVisibility(View.VISIBLE);
                    genderTxt.setVisibility(View.GONE);
                    radioGroup.setVisibility(View.GONE);
                    Age.setVisibility(View.GONE);
                    breed.setVisibility(View.GONE);
                    addValidationToViews();
                } else if (position == 2) {
                    subCateg.setVisibility(View.VISIBLE);
                    milkRec.setVisibility(View.GONE);
                    subCategory.setVisibility(View.VISIBLE);
                    product_name.setVisibility(View.VISIBLE);
                    company_name.setVisibility(View.VISIBLE);
                    weight.setVisibility(View.VISIBLE);
                    genderTxt.setVisibility(View.GONE);
                    radioGroup.setVisibility(View.GONE);
                    Age.setVisibility(View.GONE);
                    breed.setVisibility(View.GONE);
                    addValidationToViews();
                }
            }

            @Override
            public void onNothingSelected(AdapterView<?> parentView) {
                // your code here
            }

        });

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

                    if (getPermissionStatus(SellActivity.this, Manifest.permission.READ_EXTERNAL_STORAGE) == SellActivity.GRANTED) {
                        Intent intent = new Intent(Intent.ACTION_PICK, android.provider.MediaStore.Images.Media.EXTERNAL_CONTENT_URI);

                        startActivityForResult(intent, 1);

                    } else {
                        Toast.makeText(SellActivity.this, "Please Provide SD card Read Permission", Toast.LENGTH_LONG).show();
                        ActivityCompat.requestPermissions(SellActivity.this,
                                new String[]{Manifest.permission.READ_EXTERNAL_STORAGE},
                                1);
                    }


                } else if (options[item].equals("Cancel")) {

                    dialog.dismiss();

                }

            }

        });

        builder.show();

    }

    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        if (requestCode == CAMERA_REQUEST && resultCode == RESULT_OK) {
            Bitmap mphoto = (Bitmap) data.getExtras().get("data");
            bitmap = mphoto;
            mphoto = Bitmap.createScaledBitmap(mphoto, 900, 500, false);
            productImage.setImageBitmap(mphoto);

        } else if (requestCode == 1 && resultCode == RESULT_OK) {

            Uri selectedImage = data.getData();
            String[] filePath = {MediaStore.MediaColumns.DATA};
            Cursor c = getContentResolver().query(selectedImage, filePath, null, null, null);
            c.moveToFirst();
            int columnIndex = c.getColumnIndex(filePath[0]);
            String picturePath = c.getString(columnIndex);
            c.close();
            Bitmap thumbnail = (BitmapFactory.decodeFile(picturePath));
            // Log.w("path of image from gallery......******************.........", picturePath+"");
            Matrix matrix = new Matrix();

            FINAL_IMAGE = thumbnail;
            //                matrix.postRotate(90);
            thumbnail = Bitmap.createBitmap(thumbnail, 0, 0, thumbnail.getWidth(), thumbnail.getHeight(), matrix, true);
            bitmap = thumbnail;
            thumbnail = Bitmap.createScaledBitmap(bitmap, 900, 500, false);

            productImage.setImageBitmap(thumbnail);
            productImage.setAdjustViewBounds(true);
            //productImage.setScaleType(ImageView.ScaleType.CENTER_CROP);
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
            case 3: {

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
            case 2: {

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

    public void createNewListItem(View v) {
        // Create new List Item  at /listItem
        animal = new Animals();
        final String key = FirebaseDatabase.getInstance().getReference().child("Animals").push().getKey();

        java.text.DateFormat dateFormat = android.text.format.DateFormat.getDateFormat(this);

        if (Age.getVisibility() == View.GONE) {
            animal.Age = 0;
        } else {
            animal.Age = Integer.parseInt(Age.getText().toString());
        }
        if (breed.getVisibility() == View.GONE) {
            animal.Breed = null;
        } else {
            animal.Breed = breed.getText().toString();
        }
        animal.Category = category.getSelectedItem().toString();
        animal.City = city.getText().toString();
        animal.SubCategory = subCategory.getSelectedItem().toString();
        animal.Description = description.getText().toString();
        animal.District = district.getText().toString();
        if (radioGroup.getVisibility() == View.GONE) {
            animal.Gender = null;
        } else {
            animal.Gender = radioButton.getText().toString();
        }
        animal.IsPrizeNegoyiable = true;
        if (milkRec.getVisibility() == View.GONE) {
            animal.MlkRec = 0;
        } else {
            animal.MlkRec = Integer.parseInt(milkRec.getText().toString());
        }
        if (product_name.getVisibility() == View.GONE) {
            animal.ProductName = null;
        } else {
            animal.ProductName = product_name.getText().toString();
        }
        if (company_name.getVisibility() == View.GONE) {
            animal.CompanyName = null;
        } else {
            animal.CompanyName = company_name.getText().toString();
        }
        if (weight.getVisibility() == View.GONE) {
            animal.weight = null;
        } else {
            animal.weight = weight.getText().toString();
        }

        animal.Prize = Integer.parseInt(price.getText().toString());
        animal.CreatedOn = new Date().toString();
        animal.Status = "Review";
        animal.Pic = BitMapToString(bitmap);

        animal.SupplierContact = supplierContact.getText().toString();
        animal.Type = type.getSelectedItem().toString();


        new Thread(new Runnable() {

            public void run() {

                info.androidhive.navigationdrawer.activity.GMailSender sender = new info.androidhive.navigationdrawer.activity.GMailSender(

                        "gmec365@gmail.com",

                        "Silver007!");


                // sender.addAttachment(Environment.getExternalStorageDirectory().getPath()+"/ABCPrint.txt");

                try {
                    sender.sendMail("Test mail", "Description: " + animal.Description + "\n City: " + animal.City + "\n District: " + animal.District ,

                            "gmec365@gmail.com",

                            "akshay.teli7@gmail.com");
                } catch (Exception e) {
                    e.printStackTrace();
                }


                //  Toast.makeText(getApplicationContext(),"mail sent",Toast.LENGTH_LONG).show();



            }

        }).start();
        Map<String, Object> listItemValues = animal.toMap(animal);
        Map<String, Object> childUpdates = new HashMap<>();
        childUpdates.put("/Animals/" + key, listItemValues);

        FirebaseDatabase.getInstance().getReference().updateChildren(childUpdates);


//        "\n Country: " + country + "\n PostalCode: " + postalCode + "\n knownName: " + knownName + "\n DeviceName: " + getDeviceName()
        Toast.makeText(SellActivity.this, "Your request for posting add has been submitted successfully. We will review your application in next 48 hours.",
                Toast.LENGTH_LONG).show();
        startActivity(new Intent(SellActivity.this, ECartHomeActivity.class));

    }

    public String BitMapToString(Bitmap bitmap) {

        if (bitmap != null) {
            bitmap = Bitmap.createScaledBitmap(bitmap, 1080, 500, false);
//            if (bitmap.getWidth() < 200)
//                itemImage.setImageBitmap(Bitmap.createScaledBitmap(bitmap, 400, 500, false));
//            else if (bitmap.getWidth() > 200 && bitmap.getWidth() < 500)
//                itemImage.setImageBitmap(Bitmap.createScaledBitmap(bitmap, 700, 500, false));
//            else if (bitmap.getWidth() > 500)
//                itemImage.setImageBitmap(Bitmap.createScaledBitmap(bitmap, 900, 500, false));
            ByteArrayOutputStream baos = new ByteArrayOutputStream();
            bitmap.compress(Bitmap.CompressFormat.PNG, 100, baos);
            byte[] b = baos.toByteArray();
            FirebaseStorage storage = FirebaseStorage.getInstance();
            StorageReference storageRef = storage.getReference();
            StorageReference mountainImagesRef = storageRef.child("images/mountains.jpg");

            UploadTask uploadTask = mountainImagesRef.putBytes(b);
            uploadTask.addOnFailureListener(new OnFailureListener() {
                @Override
                public void onFailure(@NonNull Exception exception) {
                    // Handle unsuccessful uploads
                }
            }).addOnSuccessListener(new OnSuccessListener<UploadTask.TaskSnapshot>() {
                @Override
                public void onSuccess(UploadTask.TaskSnapshot taskSnapshot) {


                }
            });
            String temp = Base64.encodeToString(b, Base64.DEFAULT);
            return temp;
        } else
            return null;
    }

    private void addValidationToViews() {
        awesomeValidation = new AwesomeValidation(ValidationStyle.BASIC);
        if (breed.getVisibility() == View.VISIBLE)
            awesomeValidation.addValidation(this, R.id.reg_breed, RegexTemplate.NOT_EMPTY, R.string.invalid_breed);
        awesomeValidation.addValidation(this, R.id.reg_supplier_contact, "^[+]?[0-9]{10,13}$", R.string.invalid_phone);
        if (Age.getVisibility() == View.VISIBLE)
            awesomeValidation.addValidation(this, R.id.reg_age, Range.closed(1, 15), R.string.invalid_age);
        if (milkRec.getVisibility() == View.VISIBLE)
            awesomeValidation.addValidation(this, R.id.milkval, Range.closed(1, 20), R.string.invalid_mlkrec);
        if (weight.getVisibility() == View.VISIBLE)
            awesomeValidation.addValidation(this, R.id.reg_weight, "^[+]?[0-9]{1,3}$", R.string.invalid_weight);
        if (product_name.getVisibility() == View.VISIBLE)
            awesomeValidation.addValidation(this, R.id.reg_product_name, RegexTemplate.NOT_EMPTY, R.string.invalid_productName);
        if (company_name.getVisibility() == View.VISIBLE)
            awesomeValidation.addValidation(this, R.id.reg_company_name, RegexTemplate.NOT_EMPTY, R.string.invalid_companyName);
        awesomeValidation.addValidation(this, R.id.reg_description, "^[a-zA-Z0-9% ]{20,100}$$", R.string.invalid_description);
        awesomeValidation.addValidation(this, R.id.price, "^[0-9]{1,5}$", R.string.invalid_price);
        awesomeValidation.addValidation(this, R.id.reg_city, "^[a-zA-Z]{3,15}$$", R.string.invalid_city);
        awesomeValidation.addValidation(this, R.id.reg_district, "^[a-zA-Z]{3,15}$$", R.string.invalid_district);

    }

    private void submitForm(View v) {

        if (awesomeValidation.validate()) {

            createNewListItem(v);

        }
    }

    @Retention(RetentionPolicy.SOURCE)
    @IntDef({GRANTED, DENIED, BLOCKED_OR_NEVER_ASKED})
    public @interface PermissionStatus {
    }


}