package com.example.mikogarcia.findit;

import android.app.AlertDialog;
import android.app.Dialog;
import android.app.DialogFragment;
import android.app.Fragment;
import android.app.FragmentTransaction;
import android.content.DialogInterface;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.EditText;
import android.widget.TextView;

import com.example.mikogarcia.findit.model.Feature;

import java.util.ArrayList;

/**
 * Created by Daniel on 4/14/2016.
 */
public class EditFeatureDialog extends DialogFragment{
    int mNum;
    View view;
    int mStackLevel = 0;

    /**
     * Create a new instance of MyDialogFragment, providing "num"
     * as an argument.
     */
    static EditFeatureDialog newInstance(int id) {
        EditFeatureDialog f = new EditFeatureDialog();

        // Supply num input as an argument.
        Bundle args = new Bundle();
        args.putInt("id", id);
        f.setArguments(args);

        return f;
    }

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        view = LayoutInflater.from(getActivity()).inflate(R.layout.dialog_add_feature, null);
        mNum = getArguments().getInt("id");

        AlertDialog.Builder alertDialogBuilder = new AlertDialog.Builder(getActivity())
                .setTitle("Edit Description")
                .setView(view)
                .setPositiveButton("Edit", new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialog, int which) {
                        EditText etFeature = (EditText) view.findViewById(R.id.etFeature);
                        //etFeature.setHint(getArguments().getString());
                        ArrayList<Feature> features = ((AddReportActivity) getActivity()).getFeatures();
                        boolean checkFeature = true;
                        for(int i = 0; i < features.size();i++) {
                            if(etFeature.getText().toString().equals(features.get(i).getFeat())) {
                                etFeature.setError("Description already exists");
                                checkFeature = false;
                            }
                        }
                        if(checkFeature == true){
                            ((AddReportActivity) getActivity()).onAddFeature(etFeature.getText().toString());

                        }
                    }
                })
                .setNegativeButton("Cancel", new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialog, int which) {
                        dismiss();
                    }
                });


    }
    public void onStart() {
        super.onStart();
        final AlertDialog d = (AlertDialog)getDialog();
        if(d != null) {
            Button positiveButton = (Button) d.getButton(Dialog.BUTTON_POSITIVE);
            positiveButton.setOnClickListener(new View.OnClickListener()
            {
                @Override
                public void onClick(View v) {
                    EditText etFeature = (EditText) view.findViewById(R.id.etFeature);
                    ArrayList<Feature> features = ((AddReportActivity) getActivity()).getFeatures();
                    boolean checkFeature = true;
                    for(int i = 0; i < features.size();i++) {
                        if(etFeature.getText().toString().equals(features.get(i).getFeat())) {
                            etFeature.setError("Description already exists");
                            checkFeature = false;
                        }
                    }
                    if(isEmpty(etFeature.getText().toString())){
                        checkFeature = false;
                        etFeature.setError("Field must be filled");
                    }
                    if(checkFeature == true){
                        ((AddReportActivity) getActivity()).onAddFeature(etFeature.getText().toString());
                        d.dismiss();
                    }
                }
            });
            Button negativeButton = (Button)d.getButton(Dialog.BUTTON_NEGATIVE);
            negativeButton.setOnClickListener(new View.OnClickListener()
            {
                @Override
                public void onClick(View v) {
                    d.dismiss();
                }
            });
        }
    }

    void showDialog() {

        mStackLevel++;

        // DialogFragment.show() will take care of adding the fragment
        // in a transaction.  We also want to remove any currently showing
        // dialog, so make our own transaction and take care of that here.
        FragmentTransaction ft = getFragmentManager().beginTransaction();
        Fragment prev = getFragmentManager().findFragmentByTag("dialog");
        if (prev != null) {
            ft.remove(prev);
        }
        ft.addToBackStack(null);

        // Create and show the dialog.
        DialogFragment newFragment = EditFeatureDialog.newInstance(mStackLevel);
        newFragment.show(ft, "dialog");
    }

    public boolean isEmpty(String desc){
        if (desc.length() > 0) {
            return false;
        } else {
            return true;
        }
    }
}
