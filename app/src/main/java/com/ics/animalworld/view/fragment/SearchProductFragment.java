package com.ics.animalworld.view.fragment;

import android.content.ActivityNotFoundException;
import android.content.Intent;
import android.os.Bundle;
import android.speech.RecognizerIntent;
import android.support.v4.app.Fragment;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.text.Editable;
import android.text.TextWatcher;
import android.view.KeyEvent;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.EditText;
import android.widget.ImageButton;
import android.widget.ProgressBar;
import android.widget.Spinner;
import android.widget.TextView;
import android.widget.Toast;

import com.ics.animalworld.R;
import com.ics.animalworld.domain.mock.FakeWebServer;
import com.ics.animalworld.model.CenterRepository;
import com.ics.animalworld.model.entities.Animals;
import com.ics.animalworld.model.entities.Product;
import com.ics.animalworld.util.TinyDB;
import com.ics.animalworld.util.Utils;
import com.ics.animalworld.util.Utils.AnimationType;
import com.ics.animalworld.view.activities.ECartHomeActivity;
import com.ics.animalworld.view.adapter.ProductListAdapter;

import java.util.ArrayList;
import java.util.Locale;

public class SearchProductFragment extends Fragment {

    private static final int REQ_SCAN_RESULT = 200;
    private final int REQ_CODE_SPEECH_INPUT = 100;
    public ProgressBar circularProgressBar;
    ArrayList<Product> searchProductList = new ArrayList<>();
    boolean searchInProgress = false;
    String subcat = "";
    String searchString;
    ProductListAdapter adapter;
    private TextView heading, LoadingTxt, Nodata;
    private ImageButton btnSpeak;
    private EditText serchInput;
    private RecyclerView serachListView;
    private Spinner Category, SubCategory;

    /** The search adapter. */
    // private SearchListArrayAdapter searchAdapter;
    /**
     * The root view.
     */
    private View rootView;

    public static Fragment newInstance() {
        // TODO Auto-generated method stub
        return new SearchProductFragment();
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {

        rootView = inflater.inflate(R.layout.frag_search_product,
                container, false);

        btnSpeak = (ImageButton) rootView.findViewById(R.id.btnSpeak);
        btnSpeak.setClickable(false);
        heading = (TextView) rootView.findViewById(R.id.txtSpeech_heading);

        serchInput = (EditText) rootView.findViewById(R.id.edt_search_input);
        Nodata = (TextView) rootView.findViewById(R.id.noitemstxt);
        LoadingTxt = (TextView) rootView.findViewById(R.id.loadertxt);
        LoadingTxt.setVisibility(View.GONE);
        Nodata.setVisibility(View.GONE);
        serchInput.setSelected(true);
        Category = (Spinner) rootView.findViewById(R.id.category);
        SubCategory = (Spinner) rootView.findViewById(R.id.subcategory);
        serachListView = (RecyclerView) rootView
                .findViewById(R.id.search_list_view);

        SubCategory.setVisibility(View.GONE);
        serchInput.setVisibility(View.GONE);
        Category.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
            @Override
            public void onItemSelected(AdapterView<?> adapterView, View view, int i, long l) {
                if (i > 0) {

                    circularProgressBar = (ProgressBar) rootView.findViewById(R.id.circular_progress1);
                    LoadingTxt.setVisibility(View.VISIBLE);
                    btnSpeak.setClickable(true);
                    if (CenterRepository.getCenterRepository() != null) {
                        if (CenterRepository.getCenterRepository().getMapOfProductsInCategory().get(Category.getSelectedItem().toString()) == null) {

                            TinyDB tinydb = new TinyDB(getContext().getApplicationContext());
                            ArrayList<Animals> productList = tinydb.getListObject(Category.getSelectedItem().toString(), Animals.class);

                            if (productList.size()==0) {
                                FakeWebServer.getFakeWebServer().getAllProducts(
                                        i - 1, new FakeWebServer.FakeWebServiceResponseListener() {
                                            @Override
                                            public void onServiceResponse(boolean success) {
                                                if (success) {
                                                    try {
                                                        if(Category.getSelectedItemPosition() == 3 || Category.getSelectedItemPosition()==4)
                                                            SubCategory.setVisibility(View.GONE);
                                                        else
                                                            SubCategory.setVisibility(View.VISIBLE);
                                                        serchInput.setVisibility(View.VISIBLE);
                                                        circularProgressBar.setVisibility(View.GONE);
                                                        LoadingTxt.setVisibility(View.GONE);
                                                    } catch (Exception e) {
                                                        e.printStackTrace();
                                                    }
                                                }
                                            }
                                        });
                            } else{
                                if(Category.getSelectedItemPosition() == 3 || Category.getSelectedItemPosition()==4)
                                    SubCategory.setVisibility(View.GONE);
                                else
                                    SubCategory.setVisibility(View.VISIBLE);
                                serchInput.setVisibility(View.VISIBLE);
                                circularProgressBar.setVisibility(View.GONE);
                                LoadingTxt.setVisibility(View.GONE);
                                FakeWebServer.getFakeWebServer().updateProductMapForCategory(Category.getSelectedItem().toString(), productList);
                            }

                        } else {
                            SubCategory.setVisibility(View.VISIBLE);
                            serchInput.setVisibility(View.VISIBLE);
                            circularProgressBar.setVisibility(View.GONE);
                            LoadingTxt.setVisibility(View.GONE);
                        }

                    }

                }


            }

            @Override
            public void onNothingSelected(AdapterView<?> adapterView) {


            }
        });

        SubCategory.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
            @Override
            public void onItemSelected(AdapterView<?> adapterView, View view, int i, long l) {

            }

            @Override
            public void onNothingSelected(AdapterView<?> adapterView) {

            }
        });


        serchInput.addTextChangedListener(new TextWatcher() {

            @Override
            public void onTextChanged(CharSequence inputString, int arg1,
                                      int arg2, int arg3) {

                heading.setText("Showing results for "
                        + inputString.toString().toLowerCase());
                subcat = "";

                switch (Category.getSelectedItemPosition()){

                    case 1:
                        if (SubCategory.getSelectedItem().toString().equals("Food")) {
                            subcat = "Animal's Food";
                        } else if (SubCategory.getSelectedItem().toString().equals("Animal / Pet")) {
                            subcat = "Animal";
                        } else {
                            subcat = "Animal's Medicine";
                        }
                        break;
                    case 2:
                        if (SubCategory.getSelectedItem().toString().equals("Animal / Pet")) {
                            subcat = "Pet";
                        } else if (SubCategory.getSelectedItem().toString().equals("Food")) {
                            subcat = "Pet's Food";
                        } else {
                            subcat = "Pet's Medicine";
                        }
                        break;
                    case 3:
                        subcat = Category.getSelectedItem().toString();
                        break;
                    case 4:
                        subcat = Category.getSelectedItem().toString();
                        break;
                }



                searchString = inputString.toString();
                serachListView.setVisibility(View.GONE);
                LinearLayoutManager linearLayoutManager = new LinearLayoutManager(
                        getActivity().getBaseContext());
                serachListView.setLayoutManager(linearLayoutManager);
                serachListView.setHasFixedSize(true);
                adapter = new ProductListAdapter(subcat,
                        getActivity(), searchString.toString());

                if (adapter.getItemCount() > 0) {
                    serachListView.setAdapter(adapter);
                    serachListView.setVisibility(View.VISIBLE);
                    Nodata.setVisibility(View.GONE);
                } else {
                    serachListView.setVisibility(View.GONE);
                    Nodata.setVisibility(View.VISIBLE);
                }


                adapter.SetOnItemClickListener(new ProductListAdapter.OnItemClickListener() {

                    @Override
                    public void onItemClick(View view, int position) {

                        Utils.switchFragmentWithAnimation(R.id.frag_container,
                                new ProductDetailsFragment(subcat, position, false),
                                ((ECartHomeActivity) (getContext())), null,
                                AnimationType.SLIDE_LEFT);

                    }
                });
//                (new ProductListAdapter("Animals",getActivity().getApplicationContext(),""));

            }

            @Override
            public void beforeTextChanged(CharSequence arg0, int arg1,
                                          int arg2, int arg3) {

                heading.setText("Search");

            }

            @Override
            public void afterTextChanged(Editable arg0) {
            }
        });
        btnSpeak.setOnClickListener(new View.OnClickListener() {

            @Override
            public void onClick(View v) {
                promptSpeechInput();
            }
        });

        rootView.setFocusableInTouchMode(true);
        rootView.requestFocus();
        rootView.setOnKeyListener(new View.OnKeyListener() {

            @Override
            public boolean onKey(View v, int keyCode, KeyEvent event) {

                if (event.getAction() == KeyEvent.ACTION_UP
                        && keyCode == KeyEvent.KEYCODE_BACK) {

                    Utils.switchContent(R.id.frag_container,
                            Utils.HOME_FRAGMENT,
                            ((ECartHomeActivity) (getContext())),
                            AnimationType.SLIDE_DOWN);
                }
                return true;
            }
        });
        return rootView;

    }

    /**
     * Showing google speech input dialog.
     */
    private void promptSpeechInput() {
        searchInProgress = true;
        searchProductList.clear();

        heading.setText("Search Products");

        Intent intent = new Intent(RecognizerIntent.ACTION_RECOGNIZE_SPEECH);
        intent.putExtra(RecognizerIntent.EXTRA_LANGUAGE_MODEL,
                RecognizerIntent.LANGUAGE_MODEL_FREE_FORM);
        intent.putExtra(RecognizerIntent.EXTRA_LANGUAGE, Locale.getDefault());
        intent.putExtra(RecognizerIntent.EXTRA_PROMPT, "What do you wish for");
        try {
            startActivityForResult(intent, REQ_CODE_SPEECH_INPUT);
        } catch (ActivityNotFoundException a) {
            Toast.makeText(getActivity(),
                    "Voice search not supported by your device ",
                    Toast.LENGTH_SHORT).show();
        }
    }

    /**
     * Receiving speech input.
     *
     * @param requestCode the request code
     * @param resultCode  the result code
     * @param data        the data
     */
    @Override
    public void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);

        searchInProgress = false;

        if (resultCode == getActivity().RESULT_OK && null != data) {
            switch (requestCode) {
                case REQ_CODE_SPEECH_INPUT: {

                    ArrayList<String> result = data
                            .getStringArrayListExtra(RecognizerIntent.EXTRA_RESULTS);

                    heading.setText("Showing Results for " + result.get(0));
                    subcat = "";
                    if (Category.getSelectedItem().toString().equals("Animal")) {
                        if (SubCategory.getSelectedItem().toString().equals("Food")) {
                            subcat = "Animal's Food";
                        } else if (SubCategory.getSelectedItem().toString().equals("Medicine")) {
                            subcat = "Animals";
                        } else {
                            subcat = "Animal's Medicine";
                        }

                    } else {
                        if (SubCategory.getSelectedItem().toString().equals("Medicine")) {
                            subcat = "Pet";
                        } else if (SubCategory.getSelectedItem().toString().equals("Food")) {
                            subcat = "Pet's Food";
                        } else {
                            subcat = "Pet's Medicine";
                        }
                    }

                    serachListView.setVisibility(View.GONE);
                    LinearLayoutManager linearLayoutManager = new LinearLayoutManager(
                            getActivity().getBaseContext());
                    serachListView.setLayoutManager(linearLayoutManager);
                    serachListView.setHasFixedSize(true);

                    ProductListAdapter adapter = new ProductListAdapter(subcat,
                            getActivity(), result.get(0));
                    serachListView.setAdapter(adapter);

                    serachListView.setVisibility(View.VISIBLE);
                    adapter.SetOnItemClickListener(new ProductListAdapter.OnItemClickListener() {

                        @Override
                        public void onItemClick(View view, int position) {

                            Utils.switchFragmentWithAnimation(R.id.frag_container,
                                    new ProductDetailsFragment("Animals", position, false),
                                    ((ECartHomeActivity) (getContext())), null,
                                    AnimationType.SLIDE_LEFT);

                        }
                    });

                    break;
                }

                case REQ_SCAN_RESULT:
                    //
                    // String contents = data.getStringExtra("SCAN_RESULT");
                    // String format = data.getStringExtra("SCAN_RESULT_FORMAT");
                    // Toast.makeText(getActivity(), "Scan Success", 1000).show();
                    break;

            }

        }

    }

}
