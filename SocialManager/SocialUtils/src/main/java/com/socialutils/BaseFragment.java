package com.socialutils;

import android.app.Activity;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentActivity;

/**
 * @author Priyesh Bhargava
 */
public class BaseFragment extends Fragment implements Constants{

    public FragmentActivity baseContext;

    @Override
    public void onAttach(Activity activity) {
        super.onAttach(activity);
        baseContext= (FragmentActivity) activity;
    }
}
