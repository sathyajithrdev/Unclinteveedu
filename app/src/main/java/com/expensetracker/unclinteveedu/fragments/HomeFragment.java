package com.expensetracker.unclinteveedu.fragments;


import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.support.v7.widget.GridLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import com.expensetracker.unclinteveedu.R;
import com.expensetracker.unclinteveedu.models.UserModel;
import com.expensetracker.unclinteveedu.adapters.UserAdapter;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;

import java.util.ArrayList;
import java.util.List;


/**
 * A simple {@link Fragment} subclass.
 */
public class HomeFragment extends Fragment {

    private UserAdapter mUserAdapter;
    private List<UserModel> mUserList;

    public static HomeFragment newInstance() {

        Bundle args = new Bundle();

        HomeFragment fragment = new HomeFragment();
        fragment.setArguments(args);
        return fragment;
    }

    public HomeFragment() {
        // Required empty public constructor
        mUserList = new ArrayList<>();
    }


    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        View view = inflater.inflate(R.layout.fragment_home, container, false);
        initializeView(view);
        return view;
    }

    private void initializeView(View view) {
        RecyclerView mRvUser = (RecyclerView) view.findViewById(R.id.rvUsers);
        mUserAdapter = new UserAdapter(this.getActivity());
        GridLayoutManager gridLayoutManager = new GridLayoutManager(this.getActivity(), 2);
        gridLayoutManager.setSpanSizeLookup(new UserSpanSizeLookup());
        mRvUser.setLayoutManager(gridLayoutManager);
        mRvUser.setAdapter(mUserAdapter);

        final FirebaseDatabase database = FirebaseDatabase.getInstance();
        DatabaseReference mUsersReference = database.getReference("users");
        mUsersReference.addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(DataSnapshot dataSnapshot) {
                for (DataSnapshot ds : dataSnapshot.getChildren()){
                    mUserList.add(ds.getValue(UserModel.class));
                }

                mUserAdapter.setUserList(mUserList);
            }

            @Override
            public void onCancelled(DatabaseError databaseError) {

            }
        });
        
    }


    private class UserSpanSizeLookup extends GridLayoutManager.SpanSizeLookup {

        UserSpanSizeLookup() {
            super();
        }

        @Override
        public int getSpanSize(int position) {
            return (position == 0 ? 2 : 1);
        }
    }

}
