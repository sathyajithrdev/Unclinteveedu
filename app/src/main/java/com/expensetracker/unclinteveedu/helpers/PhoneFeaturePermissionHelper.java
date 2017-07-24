package com.expensetracker.unclinteveedu.helpers;

import android.app.Activity;
import android.content.DialogInterface;
import android.content.pm.PackageManager;
import android.support.v4.app.ActivityCompat;
import android.support.v4.app.Fragment;
import android.support.v4.content.ContextCompat;


import com.expensetracker.unclinteveedu.R;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

public class PhoneFeaturePermissionHelper {

    public final static int SELECT_FEATURE_INTENT = 1;

    public final static int REQUEST_PHONE_FEATURES_PERMISSION = 0;

    private Activity mActivity;
    private Fragment mFragment;
    private String mRequestInfoMessage;
    private String mMessageToShowOnDenial;


    public PhoneFeaturePermissionHelper(PhoneFeaturePermission listener) {

        if (listener instanceof Activity) {
            mActivity = (Activity) listener;
        } else {
            mFragment = (Fragment) listener;
        }
    }

    public void getPermissions(List<String> permissionsRequired, String requestMessage, String denialMessage) {
        getPermissions(REQUEST_PHONE_FEATURES_PERMISSION, permissionsRequired, requestMessage, denialMessage);
    }

    public void getPermissions(int requestCode, List<String> permissionsRequired, String requestMessage, String denialMessage) {
        mRequestInfoMessage = requestMessage;
        mMessageToShowOnDenial = denialMessage;
        List<String> permissionsNeeded = new ArrayList<>();
        List<String> permissionsNeededWithDialogue = new ArrayList<>();

        for (String permission : permissionsRequired) {
            if (ContextCompat.checkSelfPermission(getActivity(), permission) != PackageManager.PERMISSION_GRANTED) {
                permissionsNeeded.add(permission);
                if (ActivityCompat.shouldShowRequestPermissionRationale(getActivity(), permission)) {
                    permissionsNeededWithDialogue.add(permission);
                }
            }
        }

        if (permissionsNeeded.size() > 0) {
            requestPermissions(requestCode, permissionsNeeded, permissionsNeededWithDialogue);
        } else {
            showAuthorizedFeatureSelector(requestCode, permissionsRequired);
        }
    }


    private void requestPermissions(int requestCode, List<String> permissionsNeeded, List<String> permissionsNeededWithDialogue) {

        if (permissionsNeededWithDialogue.size() > 0) {
            showConfirmationDialog(requestCode, mRequestInfoMessage, permissionsNeeded);
        } else {
            ActivityCompat.requestPermissions(getActivity(), permissionsNeeded.toArray(new String[permissionsNeeded.size()]), requestCode);
        }
    }

    private void showConfirmationDialog(final int requestCode, final String message, final List<String> permissionsNeeded) {

        AlertHelper mAlertHelper = new AlertHelper(getActivity());

        mAlertHelper.showAlert(getActivity().getString(R.string.app_name), message, getActivity().getString(R.string.ok), new AlertHelper.PositiveCallBack() {
            @Override
            public void onClick(DialogInterface dialog, int which) {
                if (mActivity != null)
                    ActivityCompat.requestPermissions(getActivity(), permissionsNeeded.toArray(new String[permissionsNeeded.size()]), requestCode);
                else
                    mFragment.requestPermissions(permissionsNeeded.toArray(new String[permissionsNeeded.size()]), requestCode);
            }
        }, false);
    }

    public void onRequestPermissionsResult(int requestCode, String permissions[], int[] grantResults) {
        List<String> featuresGranted = getFeaturesGranted(permissions, grantResults);
        if (featuresGranted.size() > 0) {
            showAuthorizedFeatureSelector(requestCode, featuresGranted);
        } else if (!isNullOrEmpty(mMessageToShowOnDenial)) {
            // Toast.makeText(getActivity(), mMessageToShowOnDenial, Toast.LENGTH_LONG).show();
            showAuthorizedFeatureDenied(requestCode, featuresGranted);
        }

    }

    private boolean isNullOrEmpty(String value) {
        return value == null || value.isEmpty();
    }

    private void showAuthorizedFeatureSelector(int requestCode, List<String> featuresAuthenticated) {
        ((PhoneFeaturePermission) (mActivity == null ? mFragment : mActivity)).phoneFeaturePermissionGranted(requestCode, featuresAuthenticated);
    }

    private void showAuthorizedFeatureDenied(int requestCode, List<String> featuresAuthenticated) {
        ((PhoneFeaturePermission) (mActivity == null ? mFragment : mActivity)).phoneFeaturePermissionDenied(requestCode, featuresAuthenticated);
    }

    private List<String> getFeaturesGranted(String[] permissions, int[] grantResults) {
        Map<String, Integer> perms = new HashMap<>();
        for (int i = 0; i < permissions.length; i++) {
            perms.put(permissions[i], grantResults[i]);
        }

        List<String> featuresGranted = new ArrayList<>();

        for (Map.Entry<String, Integer> entry : perms.entrySet()) {
            if (entry.getValue() == PackageManager.PERMISSION_GRANTED) {
                featuresGranted.add(entry.getKey());
            }
        }
        return featuresGranted;
    }

    public Activity getActivity() {
        return mActivity == null ? mFragment.getActivity() : mActivity;
    }

    public interface PhoneFeaturePermission {
        void phoneFeaturePermissionGranted(int requestCode, List<String> featuresAuthenticated);

        void phoneFeaturePermissionDenied(int requestCode, List<String> featuresAuthenticated);
    }
}
