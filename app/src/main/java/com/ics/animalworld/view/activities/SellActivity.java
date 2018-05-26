package com.ics.animalworld.view.activities;
import android.Manifest;
import android.animation.Animator;
import android.animation.AnimatorListenerAdapter;
import android.app.ActionBar;
import android.content.DialogInterface;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.graphics.Bitmap;
import android.net.Uri;
import android.os.Build;
import android.os.Bundle;
import android.os.Environment;
import android.provider.MediaStore;
import android.support.annotation.IntDef;
import android.support.v4.app.ActivityCompat;
import android.support.v4.content.ContextCompat;
import android.support.v7.app.AlertDialog;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import android.view.View;
import android.view.ViewTreeObserver;
import android.widget.Adapter;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.RadioButton;
import android.widget.RadioGroup;
import android.widget.Spinner;
import android.widget.TextView;
import android.widget.Toast;

import com.ics.animalworld.R;
import com.ics.animalworld.util.Animatrix;
import com.ics.animalworld.util.Utils;

import java.io.File;
import java.lang.annotation.Retention;
import java.lang.annotation.RetentionPolicy;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;

public class SellActivity extends AppCompatActivity{
    private RadioButton male,female;
    private TextView milktxt;
    private EditText breed, milkRec,Price,Age;
    private Spinner category, type;
    View appRoot;
    ImageView productImage;

    Toolbar toolbar;
    ArrayAdapter<String> dataAdapter,petAdapter;
    private static final int CAMERA_REQUEST = 1888;
    public static final int GRANTED = 0;
    public static final int DENIED = 1;
    public static final int BLOCKED_OR_NEVER_ASKED = 2;
    @Retention(RetentionPolicy.SOURCE)
    @IntDef({GRANTED, DENIED, BLOCKED_OR_NEVER_ASKED })
    public @interface PermissionStatus {}

    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.frag_sell);
        //getSupportActionBar().setDisplayHomeAsUpEnabled(true);
        category=(Spinner)findViewById(R.id.category);
        type=(Spinner)findViewById(R.id.type);
        milkRec=(EditText)findViewById(R.id.milkval);
        milktxt=(TextView)findViewById(R.id.milktext);
        appRoot = (View) findViewById(R.id.app_root);
        toolbar = (Toolbar) findViewById(R.id.toolbar);
        milkRec.setVisibility(View.GONE);
        milktxt.setVisibility(View.GONE);
        type.setVisibility(View.GONE);
        productImage = (ImageView) findViewById(R.id.viewImage) ;

        ActivityCompat.requestPermissions(SellActivity.this,
                new String[]{Manifest.permission.CAMERA},
                1);

        productImage.setOnClickListener(new View.OnClickListener() {

            @Override

            public void onClick(View v) {

                selectImage();

            }

        });



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
                android.R.layout.simple_spinner_item, petList);
        petAdapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);



        category.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {

            @Override
            public void onItemSelected(AdapterView<?> parentView, View selectedItemView, int position, long id) {
                // your code here
                if(position==0){
                    type.setVisibility(View.GONE);
                    milkRec.setVisibility(View.GONE);
                    milktxt.setVisibility(View.GONE);
                }
                if(position==1){
                    type.setAdapter(dataAdapter);
                    type.setVisibility(View.VISIBLE);
                    milkRec.setVisibility(View.VISIBLE);
                    milktxt.setVisibility(View.VISIBLE);
                }else if(position==2){
                    type.setAdapter(petAdapter);
                    type.setVisibility(View.VISIBLE);
                    milkRec.setVisibility(View.GONE);
                    milktxt.setVisibility(View.GONE);
                }
            }

            @Override
            public void onNothingSelected(AdapterView<?> parentView) {
                // your code here
            }







        });

    }


    private void selectImage() {



        final CharSequence[] options = { "Take Photo", "Choose from Gallery","Cancel" };



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
                    if (getPermissionStatus(SellActivity.this,Manifest.permission.CAMERA) == SellActivity.GRANTED) {
                        Intent cameraIntent = new Intent(android.provider.MediaStore.ACTION_IMAGE_CAPTURE);
                        startActivityForResult(cameraIntent, CAMERA_REQUEST);

                    }
                    else
                    {
                        Toast.makeText(SellActivity.this, "Please Provide Camera Permission", Toast.LENGTH_LONG).show();
                        ActivityCompat.requestPermissions(SellActivity.this,
                                new String[]{Manifest.permission.CAMERA},
                                1);
                    }
                }

                else if (options[item].equals("Choose from Gallery"))

                {

                    Intent intent = new   Intent(Intent.ACTION_PICK,android.provider.MediaStore.Images.Media.EXTERNAL_CONTENT_URI);

                    startActivityForResult(intent, 2);



                }

                else if (options[item].equals("Cancel")) {

                    dialog.dismiss();

                }

            }

        });

        builder.show();

    }

    private static File getOutputMediaFile(){
        File mediaStorageDir = new File(Environment.getExternalStoragePublicDirectory(
                Environment.DIRECTORY_PICTURES), "CameraDemo");

        if (!mediaStorageDir.exists()){
            if (!mediaStorageDir.mkdirs()){
                return null;
            }
        }

        String timeStamp = new SimpleDateFormat("yyyyMMdd_HHmmss").format(new Date());
        return new File(mediaStorageDir.getPath() + File.separator +
                "IMG_"+ timeStamp + ".jpg");
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
        if(ContextCompat.checkSelfPermission(activity, androidPermissionName) != PackageManager.PERMISSION_GRANTED) {
            if(!ActivityCompat.shouldShowRequestPermissionRationale(activity, androidPermissionName)){
                return BLOCKED_OR_NEVER_ASKED;
            }
            return DENIED;
        }
        return GRANTED;
    }





}
