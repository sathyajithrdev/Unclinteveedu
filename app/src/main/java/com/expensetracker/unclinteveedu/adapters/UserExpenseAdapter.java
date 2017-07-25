package com.expensetracker.unclinteveedu.adapters;

import android.content.Context;
import android.support.constraint.solver.Goal;
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
import com.expensetracker.unclinteveedu.models.ExpenseData;
import com.expensetracker.unclinteveedu.models.UserModel;

import java.util.ArrayList;
import java.util.List;

/**
 * Created by sathy on 7/25/2017.
 */

public class UserExpenseAdapter extends RecyclerView.Adapter<UserExpenseAdapter.UserExpenseViewHolder> {

    private Context mContext;
    private List<ExpenseData> mExpenseDataList;
    private ClickListener mClickListener;
    private boolean mIsPaymentMode;

    public UserExpenseAdapter(Context context, ClickListener clickListener, boolean isPaymentMode, List<ExpenseData> expenseDataList) {
        this.mContext = context;
        mExpenseDataList = (expenseDataList == null ? new ArrayList<ExpenseData>() : expenseDataList);
        mClickListener = clickListener;
        mIsPaymentMode = isPaymentMode;
    }

    @Override
    public UserExpenseViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        View itemView = LayoutInflater.from(parent.getContext())
                .inflate(R.layout.user_expense_row, parent, false);
        return new UserExpenseAdapter.UserExpenseViewHolder(itemView);
    }

    @Override
    public void onBindViewHolder(UserExpenseViewHolder holder, int position) {
        holder.mTvPaidTo.setVisibility(mIsPaymentMode ? View.VISIBLE : View.GONE);
        ExpenseData expenseData = mExpenseDataList.get(position);
        holder.mTvExpenseName.setText(expenseData.expenseName);
        holder.mTvDate.setText(expenseData.paymentDate);
        holder.mTvPaidTo.setText(expenseData.paidToUser);
        holder.mTvAmount.setText(String.valueOf(expenseData.amount));
        Glide.with(mContext)
                .load(expenseData.imageUrl)
                .placeholder(ContextCompat.getDrawable(mContext, R.drawable.ic_place_holder))
                .centerCrop()
                .into(holder.mIvBill);
    }

    @Override
    public int getItemCount() {
        return mExpenseDataList.size();
    }

    class UserExpenseViewHolder extends RecyclerView.ViewHolder {

        private ImageView mIvBill;
        private TextView mTvExpenseName;
        private TextView mTvAmount;
        private TextView mTvDate;
        private TextView mTvPaidTo;

        private UserExpenseViewHolder(View itemView) {
            super(itemView);
            mIvBill = (ImageView) itemView.findViewById(R.id.ivBill);
            mTvExpenseName = (TextView) itemView.findViewById(R.id.tvExpenseName);
            mTvAmount = (TextView) itemView.findViewById(R.id.tvAmount);
            mTvDate = (TextView) itemView.findViewById(R.id.tvDate);
            mTvPaidTo = (TextView) itemView.findViewById(R.id.tvPaidTo);
        }
    }
}
