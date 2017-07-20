package com.expensetracker.unclinteveedu.adapters;

import android.content.Context;
import android.support.v4.content.ContextCompat;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import com.expensetracker.unclinteveedu.R;
import com.expensetracker.unclinteveedu.models.UserModel;

import java.util.ArrayList;
import java.util.List;

/**
 * Created by sathyajith on 21/07/17.
 * Payee adatper class
 */

public class PayeeAdapter extends RecyclerView.Adapter<PayeeAdapter.PayeeViewHolder> {

    private Context mContext;
    private List<UserModel> mUserList;
    private Integer selectedPosition;

    public PayeeAdapter(Context context) {
        this.mContext = context;
        mUserList = new ArrayList<>();
    }

    @Override
    public PayeeViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        View itemView = LayoutInflater.from(parent.getContext())
                .inflate(R.layout.payee_list_row, parent, false);
        return new PayeeViewHolder(itemView);
    }

    @Override
    public void onBindViewHolder(final PayeeViewHolder holder, int position) {
        UserModel user = mUserList.get(position);
        holder.mTvUserName.setText(user.name);
        holder.mTvUserName.setBackgroundColor(ContextCompat.getColor(mContext, selectedPosition != null && position == selectedPosition ? R.color.colorGreen : R.color.colorTransparent));
        holder.mTvUserName.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                selectedPosition = holder.getAdapterPosition();
                notifyDataSetChanged();
            }
        });
    }

    @Override
    public int getItemCount() {
        return mUserList.size();
    }

    public void setUserList(List<UserModel> userList) {
        mUserList = userList;
        this.notifyDataSetChanged();
    }

    public String getSelectedUserId() {
        return mUserList != null && selectedPosition != null ? mUserList.get(selectedPosition).userId : "";
    }

    class PayeeViewHolder extends RecyclerView.ViewHolder {
        private TextView mTvUserName;

        private PayeeViewHolder(View itemView) {
            super(itemView);
            mTvUserName = (TextView) itemView.findViewById(R.id.tvUserName);
        }
    }
}