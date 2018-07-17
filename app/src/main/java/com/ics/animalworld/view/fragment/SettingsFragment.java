
package com.ics.animalworld.view.fragment;

import android.content.Intent;
import android.graphics.Color;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.support.v4.view.GravityCompat;
import android.support.v7.widget.Toolbar;
import android.view.KeyEvent;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.RadioButton;
import android.widget.Spinner;
import android.widget.TextView;

import com.ics.animalworld.R;
import com.ics.animalworld.util.TinyDB;
import com.ics.animalworld.util.Utils;
import com.ics.animalworld.util.Utils.AnimationType;
import com.ics.animalworld.view.activities.ECartHomeActivity;

// TODO: Auto-generated Javadoc

/**
 * Fragment that appears in the "content_frame", shows a animal.
 */
public class SettingsFragment extends Fragment {

    private TextView submitLog;
    private Toolbar mToolbar;
    private Spinner spinner;
    private Button btnSave;
    String languagePref ="";
    Long selectedValue = 0L;

    /**
     * Instantiates a new settings fragment.
     */
    public SettingsFragment() {
        // Empty constructor required for fragment subclasses
    }

    public static Fragment newInstance() {
        // TODO Auto-generated method stub
        return new SettingsFragment();
    }

    /*
     * (non-Javadoc)
     *
     * @see
     * android.support.v4.app.Fragment#onCreateView(android.view.LayoutInflater,
     * android.view.ViewGroup, android.os.Bundle)
     */
    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        View rootView = inflater.inflate(R.layout.frag_settings, container,
                false);

        getActivity().setTitle("Settings");

        final TinyDB tinydb = new TinyDB(getContext());


        spinner = (Spinner) rootView.findViewById(R.id.language);

        btnSave = (Button) rootView.findViewById(R.id.dialogButtonSave);

        selectedValue = tinydb.getLong(languagePref,0);

        if(!tinydb.getString("lang").isEmpty()){

            switch (tinydb.getString("lang")){
                case  "en":
                    spinner.setSelection(0);
                    break;
                case "hi":
                    spinner.setSelection(1);
                    break;
                case "mr":
                    spinner.setSelection(0);
                    break;
            }
        }


        mToolbar = (Toolbar) rootView.findViewById(R.id.htab_toolbar);
        if (mToolbar != null) {
            ((ECartHomeActivity) getActivity()).setSupportActionBar(mToolbar);
        }

        if (mToolbar != null) {
            ((ECartHomeActivity) getActivity()).getSupportActionBar()
                    .setDisplayHomeAsUpEnabled(true);

            mToolbar.setNavigationIcon(R.drawable.ic_drawer);

        }

        mToolbar.setNavigationOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                ((ECartHomeActivity) getActivity()).getmDrawerLayout()
                        .openDrawer(GravityCompat.START);
            }
        });

        mToolbar.setTitleTextColor(Color.WHITE);

        /*submitLog = (TextView) rootView.findViewById(R.id.submit_log_txt);

        if (PreferenceHelper.getPrefernceHelperInstace().getBoolean(
                getActivity(), PreferenceHelper.SUBMIT_LOGS, true)) {

            submitLog.setText("Disable");
        } else {
            submitLog.setText("Enable");
        }

        rootView.findViewById(R.id.submit_log).setOnClickListener(
                new OnClickListener() {

                    @Override
                    public void onClick(View v) {

                        if (PreferenceHelper.getPrefernceHelperInstace()
                                .getBoolean(getActivity(),
                                        PreferenceHelper.SUBMIT_LOGS, true)) {
                            PreferenceHelper
                                    .getPrefernceHelperInstace()
                                    .setBoolean(getActivity(),
                                            PreferenceHelper.SUBMIT_LOGS, false);

                            submitLog.setText("Disable");
                        } else {
                            PreferenceHelper.getPrefernceHelperInstace()
                                    .setBoolean(getActivity(),
                                            PreferenceHelper.SUBMIT_LOGS, true);
                            submitLog.setText("Enable");
                        }

                    }
                });
*/
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
                            AnimationType.SLIDE_UP);

                }
                return true;
            }
        });


        btnSave.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {

                switch (spinner.getSelectedItemPosition()){
                    case  0:
                        tinydb.putString("lang","en");
                        break;
                    case 1:
                        tinydb.putString("lang","hi");
                        break;
                    case 2:
                        tinydb.putString("lang","mr");
                        break;
                }

                startActivity(new Intent(getActivity(), ECartHomeActivity.class));
            }
        });



        return rootView;
    }
}
