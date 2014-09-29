package com.gabiq.twitterpro.fragments;

import android.os.Bundle;
import android.support.v4.app.DialogFragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.view.WindowManager;

import com.gabiq.twitterpro.R;


public class ComposeFragment extends DialogFragment {

    public ComposeFragment() {
        // Empty constructor required for DialogFragment
    }
    
    public static ComposeFragment newInstance(String title) {
        ComposeFragment frag = new ComposeFragment();
        frag.setStyle(R.style.CustomDialog, 0);
        Bundle args = new Bundle();
//        args.putString("title", title);
//        frag.setArguments(args);
        return frag;
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
            Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.fragment_compose, container);
//        mEditText = (EditText) view.findViewById(R.id.txt_your_name);
//        String title = getArguments().getString("title", "Enter Name");
//        getDialog().setTitle(title);
//        // Show soft keyboard automatically
//        mEditText.requestFocus();
        getDialog().getWindow().setSoftInputMode(
                WindowManager.LayoutParams.SOFT_INPUT_STATE_VISIBLE);
        return view;
    }
}