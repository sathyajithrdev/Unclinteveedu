package com.expensetracker.unclinteveedu.adapters;

import android.content.Context;
import android.support.annotation.NonNull;
import android.support.v4.content.ContextCompat;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import com.bumptech.glide.Glide;
import com.expensetracker.unclinteveedu.R;
import com.expensetracker.unclinteveedu.interfaces.ClickListener;
import com.expensetracker.unclinteveedu.models.UserModel;

import java.util.ArrayList;
import java.util.List;

public class UserAdapter extends RecyclerView.Adapter<UserAdapter.UserViewHolder> {

    private Context mContext;
    private List<UserModel> mUserList;
    private ClickListener mClickListener;

    public UserAdapter(Context context, ClickListener clickListener) {
        this.mContext = context;
        mUserList = new ArrayList<>();
        mClickListener = clickListener;
    }

    @Override
    public UserViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        View itemView = LayoutInflater.from(parent.getContext())
                .inflate(R.layout.user_list_row, parent, false);
        return new UserViewHolder(itemView);
    }

    @Override
    public void onBindViewHolder(UserViewHolder holder, int position) {

        final UserModel user = mUserList.get(position);
        Glide
                .with(mContext)
                .load(user.profileImage)
                .centerCrop()
                .into(holder.mIvUser);

        holder.mTvUserName.setText(user.name);
        holder.mTvAmount.setText(mContext.getString(R.string.Rs, getAmountSymbol(user), String.valueOf(Math.abs(user.amount))));
        holder.mTvAmount.setTextColor(ContextCompat.getColor(mContext, user.amount > 0 ? R.color.colorGreen : R.color.colorRed));
        holder.itemView.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                mClickListener.isItemClicked(user);
            }
        });
    }

    @NonNull
    private String getAmountSymbol(UserModel user) {
        if (user.amount < 0)
            return "-";
        if (user.amount > 0)
            return "+";
        return "";
    }

    @Override
    public int getItemCount() {
        return mUserList.size();
    }

    public void setUserList(List<UserModel> userList) {
        int insertFromPosition = userList.size();
        mUserList = userList;
        this.notifyItemRangeInserted(insertFromPosition, mUserList.size());
    }

    public void clearUserList() {
        mUserList = new ArrayList<>();
        notifyDataSetChanged();
    }

    class UserViewHolder extends RecyclerView.ViewHolder {

        private ImageView mIvUser;
        private TextView mTvUserName;
        private TextView mTvAmount;

        private UserViewHolder(View itemView) {
            super(itemView);
            mIvUser = (ImageView) itemView.findViewById(R.id.ivUser);
            mTvUserName = (TextView) itemView.findViewById(R.id.tvUserName);
            mTvAmount = (TextView) itemView.findViewById(R.id.tvAmount);
        }
    }
}
