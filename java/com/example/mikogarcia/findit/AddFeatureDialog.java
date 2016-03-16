package com.example.mikogarcia.findit;

import android.app.AlertDialog;
import android.app.Dialog;
import android.app.DialogFragment;
import android.content.DialogInterface;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;

import com.example.mikogarcia.findit.model.Feature;

import java.util.ArrayList;

/**
 * Created by Daniel on 3/14/2016.
 */
public class AddFeatureDialog extends DialogFragment{
    View view;

    @Override
    public Dialog onCreateDialog(Bundle savedInstanceState) {
        view = LayoutInflater.from(getActivity()).inflate(R.layout.dialog_add_feature, null);
        AlertDialog.Builder alertDialogBuilder = new AlertDialog.Builder(getActivity())
                .setTitle("Add Description")
                .setView(view)
                .setPositiveButton("Add", new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialog, int which) {
                        EditText etFeature = (EditText) view.findViewById(R.id.etFeature);
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

        return  alertDialogBuilder.create();
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

    public boolean isEmpty(String desc){
        if (desc.length() > 0) {
            return false;
        } else {
            return true;
        }
    }


}
