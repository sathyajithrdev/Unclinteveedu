package com.expensetracker.unclinteveedu.helpers;


import android.Manifest;
import android.content.Context;
import android.content.Intent;
import android.content.pm.ResolveInfo;
import android.net.Uri;
import android.os.Environment;
import android.os.Parcelable;
import android.provider.MediaStore;
import android.provider.MediaStore.Images;

import com.expensetracker.unclinteveedu.R;

import java.io.File;
import java.io.IOException;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.Date;
import java.util.List;
import java.util.Locale;

public final class ImageUtil {

    public static final List<String> CAMERA_GALLERY_PERMISSION = Arrays.asList(Manifest.permission.CAMERA, Manifest.permission.READ_EXTERNAL_STORAGE);

    public ImageUtil() {
        throw new RuntimeException("Final Class, cannot be instantiated !");
    }

    public static Intent getPhoneFeatureIntent(Context context, List<String> featuresNeeded, boolean needMultiplePhotoSelection, Uri cameraImageUri) {
        Intent chooserIntent = null;

        List<Intent> intentList = new ArrayList<>();

        if (featuresNeeded.contains(Manifest.permission.CAMERA)) {

            Intent cameraIntent = new Intent(MediaStore.ACTION_IMAGE_CAPTURE);
            if (cameraImageUri != null && cameraIntent.resolveActivity(context.getPackageManager()) != null) {
                cameraIntent.putExtra("return-data", true);
                cameraIntent.putExtra(MediaStore.EXTRA_OUTPUT, cameraImageUri);
                intentList = addIntentsToList(context, intentList, cameraIntent);
            }
        }
        if (featuresNeeded.contains(Manifest.permission.READ_EXTERNAL_STORAGE)) {

            Intent galleryIntent = new Intent(Intent.ACTION_PICK, Images.Media.EXTERNAL_CONTENT_URI);
            if (galleryIntent.resolveActivity(context.getPackageManager()) != null) {
                galleryIntent.putExtra(Intent.EXTRA_ALLOW_MULTIPLE, needMultiplePhotoSelection);
                intentList = addIntentsToList(context, intentList, galleryIntent);
            }
        }


        if (intentList.size() > 0) {
            chooserIntent = Intent.createChooser(intentList.remove(intentList.size() - 1),
                    context.getString(R.string.label_pick_image_intent));
            chooserIntent.putExtra(Intent.EXTRA_INITIAL_INTENTS, intentList.toArray(new Parcelable[]{}));
        }

        return chooserIntent;
    }

    private static List<Intent> addIntentsToList(Context context, List<Intent> list, Intent intent) {
        List<ResolveInfo> resInfo = context.getPackageManager().queryIntentActivities(intent, 0);
        for (ResolveInfo resolveInfo : resInfo) {
            String packageName = resolveInfo.activityInfo.packageName;
            Intent targetedIntent = new Intent(intent);
            targetedIntent.setPackage(packageName);
            list.add(targetedIntent);
        }
        return list;
    }


    public static Uri createImageFile(Context context) throws IOException {
        return createImageFile(context, null);
    }

    private static Uri createImageFile(Context context, String fileName) throws IOException {
        // Create an image file name
        String timeStamp = new SimpleDateFormat("yyyyMMdd_HHmmss", Locale.US).format(new Date());

        String imageFileName = (isNullOrEmpty(fileName) ? "Image" : fileName) + "_" + timeStamp;
        File storageDir = context.getExternalFilesDir(Environment.DIRECTORY_PICTURES);
        File image = File.createTempFile(
                imageFileName,  /* prefix */
                ".jpg",         /* suffix */
                storageDir      /* directory */
        );
        return Uri.fromFile(image);
    }

    private static boolean isNullOrEmpty(String value) {
        return value == null || value.isEmpty();
    }
}
