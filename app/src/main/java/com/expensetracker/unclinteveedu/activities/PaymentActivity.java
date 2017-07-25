package com.expensetracker.unclinteveedu.activities;

import android.app.DatePickerDialog;
import android.content.Intent;
import android.graphics.Bitmap;
import android.net.Uri;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.design.widget.TextInputLayout;
import android.support.v7.widget.RecyclerView;
import android.support.v7.widget.StaggeredGridLayoutManager;
import android.view.View;
import android.widget.DatePicker;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.Toast;

import com.bumptech.glide.Glide;
import com.expensetracker.unclinteveedu.R;
import com.expensetracker.unclinteveedu.adapters.PayeeAdapter;
import com.expensetracker.unclinteveedu.helpers.FetchPath;
import com.expensetracker.unclinteveedu.helpers.ImageCompressor;
import com.expensetracker.unclinteveedu.helpers.ImageUtil;
import com.expensetracker.unclinteveedu.helpers.PhoneFeaturePermissionHelper;
import com.expensetracker.unclinteveedu.interfaces.ClickListener;
import com.expensetracker.unclinteveedu.managers.DatabaseManager;
import com.expensetracker.unclinteveedu.models.ExpenseData;
import com.expensetracker.unclinteveedu.models.UserModel;
import com.google.android.gms.tasks.OnFailureListener;
import com.google.android.gms.tasks.OnSuccessListener;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.storage.FirebaseStorage;
import com.google.firebase.storage.StorageReference;
import com.google.firebase.storage.UploadTask;

import java.io.ByteArrayOutputStream;
import java.io.File;
import java.io.IOException;
import java.text.SimpleDateFormat;
import java.util.Calendar;
import java.util.List;
import java.util.Locale;

public class PaymentActivity extends BaseActivity implements View.OnClickListener, PhoneFeaturePermissionHelper.PhoneFeaturePermission, ClickListener {

    private Calendar selectedDate = Calendar.getInstance();
    private DatabaseManager mDatabaseManager;
    private PayeeAdapter mPayeeAdapter;
    private PayeeAdapter mPaidToAdapter;
    private SimpleDateFormat sdf = new SimpleDateFormat("MM/dd/yy", Locale.getDefault());
    private TextInputLayout mTilExpenseName;
    private TextInputLayout mTilExpenseDate;
    private TextInputLayout mTilExpenseAmount;
    private EditText mEtExpenseName;
    private EditText mEtAmount;
    private EditText mEtPaymentDate;
    private String filename;
    private ImageView mIvSelectPhoto;
    private PhoneFeaturePermissionHelper mPhoneFeaturePermissionHelper;
    private Uri mCameraImageUri;
    private StorageReference mStorageRef;
    private Uri firebaseImageUrl;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_payment);
        getSupportActionBar().setTitle("Add Payment");
        getSupportActionBar().setDisplayHomeAsUpEnabled(true);
        initClickListeners();
        initComponents();

    }

    private void initComponents() {
        mDatabaseManager = new DatabaseManager();
        mStorageRef = FirebaseStorage.getInstance().getReference();
        mPhoneFeaturePermissionHelper = new PhoneFeaturePermissionHelper(this);
        List<UserModel> mUserList = mDatabaseManager.getAllUserDetails();
        List<UserModel> mPaidToList = mDatabaseManager.getAllUserDetails();
        setProgressLayout(R.id.layoutProgress);
        mTilExpenseName = (TextInputLayout) findViewById(R.id.tilExpenditure);
        mTilExpenseAmount = (TextInputLayout) findViewById(R.id.tilAmount);
        mTilExpenseDate = (TextInputLayout) findViewById(R.id.tilDate);
        mEtExpenseName = (EditText) findViewById(R.id.etExpenditure);
        mEtAmount = (EditText) findViewById(R.id.etAmount);
        mEtPaymentDate = (EditText) findViewById(R.id.etDate);
        mIvSelectPhoto = (ImageView) findViewById(R.id.ivSelectPhoto);

        RecyclerView rvPayee = (RecyclerView) findViewById(R.id.rvUsers);
        rvPayee.setLayoutManager(new StaggeredGridLayoutManager(3, StaggeredGridLayoutManager.HORIZONTAL));
        RecyclerView rvPaidTo = (RecyclerView) findViewById(R.id.rvPaidTo);
        rvPaidTo.setLayoutManager(new StaggeredGridLayoutManager(3, StaggeredGridLayoutManager.HORIZONTAL));
        mPayeeAdapter = new PayeeAdapter(this, this);
        mPaidToAdapter = new PayeeAdapter(this, this);
        rvPayee.setAdapter(mPayeeAdapter);
        rvPaidTo.setAdapter(mPaidToAdapter);
        mPayeeAdapter.setUserList(mUserList);
        mPaidToAdapter.setUserList(mPaidToList);
    }

    private void initClickListeners() {
        findViewById(R.id.etDate).setOnClickListener(this);
        findViewById(R.id.btnAddOrUpdate).setOnClickListener(this);
        findViewById(R.id.ivSelectPhoto).setOnClickListener(this);
    }

    DatePickerDialog.OnDateSetListener date = new DatePickerDialog.OnDateSetListener() {

        @Override
        public void onDateSet(DatePicker view, int year, int monthOfYear,
                              int dayOfMonth) {
            selectedDate.set(Calendar.YEAR, year);
            selectedDate.set(Calendar.MONTH, monthOfYear);
            selectedDate.set(Calendar.DAY_OF_MONTH, dayOfMonth);
            updatePaymentDate();
        }

    };

    @Override
    public void onClick(View v) {
        switch (v.getId()) {
            case R.id.etDate: {
                new DatePickerDialog(PaymentActivity.this, date, selectedDate
                        .get(Calendar.YEAR), selectedDate.get(Calendar.MONTH),
                        selectedDate.get(Calendar.DAY_OF_MONTH)).show();
                break;
            }
            case R.id.btnAddOrUpdate: {
                if (validatePaymentData()) {
                    setProgressMessage("Saving expense data...");
                    showProgress();
                    if (filename == null || filename.equals(""))
                        addPayment();
                    else
                        uploadImageToFirebase();
                }
                break;
            }

            case R.id.ivSelectPhoto: {
                mPhoneFeaturePermissionHelper.getPermissions(ImageUtil.CAMERA_GALLERY_PERMISSION,
                        getString(R.string.permission_camera),
                        getString(R.string.permission_camera));
                break;
            }
        }
    }

    private boolean validatePaymentData() {
        boolean isValid = true;
        if (mEtPaymentDate.getText().toString().equals("")) {
            mTilExpenseDate.setError("This is a mandatory field");
            isValid = false;
            mEtPaymentDate.requestFocus();
        }
        if (mEtAmount.getText().toString().equals("")) {
            mTilExpenseAmount.setError("This is a mandatory field");
            isValid = false;
            mEtAmount.requestFocus();
        }
        if (mEtExpenseName.getText().toString().equals("")) {
            mTilExpenseName.setError("This is a mandatory field");
            isValid = false;
            mEtExpenseName.requestFocus();
        }

        if (mPayeeAdapter.getSelectedUserId() == null) {
            findViewById(R.id.tvErrorPayee).setVisibility(View.VISIBLE);
            isValid = false;
        }

        if (mPaidToAdapter.getSelectedUserId() == null) {
            findViewById(R.id.tvErrorPaidTo).setVisibility(View.VISIBLE);
            isValid = false;
        }

        return isValid;
    }

    private void updatePaymentDate() {
        mEtPaymentDate.setText(sdf.format(selectedDate.getTime()));
    }

    private void addPayment() {
        final FirebaseDatabase database = FirebaseDatabase.getInstance();
        final DatabaseReference expenseRef =
                database.getReference("users/" + mPayeeAdapter.getSelectedUserId() + "/payments");
        String expenseId = expenseRef.push().getKey();
        ExpenseData expenseData = new ExpenseData();
        expenseData.id = expenseId;

        expenseData.expenseName = getEtText(R.id.etExpenditure);
        expenseData.amount = Double.parseDouble(getEtText(R.id.etAmount));
        expenseData.paidByUser = mPayeeAdapter.getSelectedUserId();
        expenseData.paidToUser = mPaidToAdapter.getSelectedUserId();
        expenseData.paymentDate = getEtText(R.id.etDate);
        expenseData.createdDate = sdf.format(Calendar.getInstance().getTime());
        expenseData.createdUser = getLoggedInUserId();
        if (firebaseImageUrl != null)
            expenseData.imageUrl = firebaseImageUrl.toString();
        expenseRef.child(expenseId).setValue(expenseData, new DatabaseReference.CompletionListener() {
            @Override
            public void onComplete(DatabaseError databaseError, DatabaseReference databaseReference) {
                if (databaseError == null) {
                    Toast.makeText(PaymentActivity.this, R.string.info_success_payment, Toast.LENGTH_LONG).show();
                    supportFinishAfterTransition();
                } else {
                    Toast.makeText(PaymentActivity.this, R.string.error_network, Toast.LENGTH_LONG).show();

                }
            }
        });
    }

    private String getEtText(int et) {
        return ((EditText) findViewById(et)).getText().toString();
    }


    @Override
    protected void onDestroy() {
        super.onDestroy();
        mDatabaseManager.onDestroy();
    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        if (resultCode == RESULT_OK && requestCode == PhoneFeaturePermissionHelper.SELECT_FEATURE_INTENT) {
            if (null != data && null != data.getData()) {
                filename = compressImage(data.getData());

            } else if (mCameraImageUri != null) {
                filename = compressImage(mCameraImageUri);
            }

            Glide.with(this)
                    .load(filename)
                    .centerCrop()
                    .into(mIvSelectPhoto);
        }
    }

    @Override
    public void phoneFeaturePermissionGranted(int requestCode, List<String> featuresAuthenticated) {
        try {
            mCameraImageUri = ImageUtil.createImageFile(this);
            startActivityForResult(ImageUtil.getPhoneFeatureIntent(this,
                    featuresAuthenticated, false, mCameraImageUri), PhoneFeaturePermissionHelper.SELECT_FEATURE_INTENT);
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    @Override
    public void phoneFeaturePermissionDenied(int requestCode, List<String> featuresAuthenticated) {

    }

    private String compressImage(Uri uri) {
        return ImageCompressor.compressImage(this, FetchPath.getPath(this, uri), null);
    }

    private void uploadImageToFirebase() {
        mIvSelectPhoto.setDrawingCacheEnabled(true);
        mIvSelectPhoto.buildDrawingCache();
        Bitmap bitmap = mIvSelectPhoto.getDrawingCache();
        ByteArrayOutputStream baos = new ByteArrayOutputStream();
        bitmap.compress(Bitmap.CompressFormat.JPEG, 100, baos);
        byte[] data = baos.toByteArray();


        StorageReference billRef = mStorageRef.child("paymentImage/" + new File(filename).getName());
        UploadTask uploadTask = billRef.putBytes(data);
        uploadTask.addOnFailureListener(new OnFailureListener() {
            @Override
            public void onFailure(@NonNull Exception exception) {
                // Handle unsuccessful uploads
                showAlert(getString(R.string.error_add_expense), true);
                hidProgress();
            }
        }).addOnSuccessListener(new OnSuccessListener<UploadTask.TaskSnapshot>() {
            @Override
            public void onSuccess(UploadTask.TaskSnapshot taskSnapshot) {
                // taskSnapshot.getMetadata() contains file metadata such as size, content-type, and download URL.
                //noinspection VisibleForTests
                firebaseImageUrl = taskSnapshot.getDownloadUrl();
                addPayment();
            }
        });
    }

    @Override
    public boolean onSupportNavigateUp() {
        supportFinishAfterTransition();
        return true;
    }

    @Override
    public void isItemClicked(Object data) {
        findViewById(R.id.tvErrorPayee).setVisibility(View.INVISIBLE);
        findViewById(R.id.tvErrorPaidTo).setVisibility(View.INVISIBLE);
    }
}
