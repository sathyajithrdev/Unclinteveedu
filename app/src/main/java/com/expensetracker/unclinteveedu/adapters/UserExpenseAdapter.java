package com.expensetracker.unclinteveedu.adapters;

import android.content.Context;
import android.support.v4.content.ContextCompat;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import com.bumptech.glide.Glide;
import com.daimajia.swipe.SwipeLayout;
import com.daimajia.swipe.adapters.RecyclerSwipeAdapter;
import com.expensetracker.unclinteveedu.R;
import com.expensetracker.unclinteveedu.interfaces.UserExpenseActionListener;
import com.expensetracker.unclinteveedu.models.ExpenseData;

import java.util.ArrayList;
import java.util.List;
import java.util.Map;

/**
 * Created by sathy on 7/25/2017.
 * Adapter class for listing expense or payment done by user
 */

public class UserExpenseAdapter extends RecyclerSwipeAdapter<UserExpenseAdapter.UserExpenseViewHolder> {

    private Context mContext;
    private List<ExpenseData> mExpenseDataList;
    private Map<String, String> mAllUserName;
    private UserExpenseActionListener mListener;
    private boolean mIsPaymentMode;
    private int deletingItemPosition;

    public UserExpenseAdapter(Context context, UserExpenseActionListener clickListener, boolean isPaymentMode, List<ExpenseData> expenseDataList, Map<String, String> allUserNames) {
        this.mContext = context;
        mExpenseDataList = (expenseDataList == null ? new ArrayList<ExpenseData>() : expenseDataList);
        mListener = clickListener;
        mIsPaymentMode = isPaymentMode;
        mAllUserName = allUserNames;
    }

    @Override
    public UserExpenseViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        View itemView = LayoutInflater.from(parent.getContext())
                .inflate(R.layout.user_expense_row, parent, false);
        return new UserExpenseAdapter.UserExpenseViewHolder(itemView);
    }

    @Override
    public void onBindViewHolder(final UserExpenseViewHolder holder, int position) {
        final ExpenseData expenseData = mExpenseDataList.get(position);
        if (mIsPaymentMode) {
            holder.mTvUserName.setText(String.format("Paid To: %s", mAllUserName.get(expenseData.paidToUser)));
        } else {
            String userName = mAllUserName.get(expenseData.paidByUser);
            if (userName == null || userName.equals("")) {
                holder.mTvUserName.setVisibility(View.GONE);
            } else {
                holder.mTvUserName.setText(String.format("Payee: %s", userName));
            }
        }
        holder.mTvExpenseName.setText(expenseData.expenseName);
        holder.mTvDate.setText(String.format("Transaction Date : %s", expenseData.paymentDate));
        holder.mTvAmount.setText(String.format("Amount : %s", String.valueOf(expenseData.amount)));
        Glide.with(mContext)
                .load(expenseData.imageUrl)
                .placeholder(ContextCompat.getDrawable(mContext, R.drawable.ic_place_holder))
                .centerCrop()
                .into(holder.mIvBill);

        holder.buttonDelete.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                holder.swipeLayout.toggle();
                deletingItemPosition = holder.getAdapterPosition();
                mListener.deleteData(expenseData);
            }
        });

        // mItemManger is member in RecyclerSwipeAdapter Class
        mItemManger.bindView(holder.itemView, position);
    }

    @Override
    public int getItemCount() {
        return mExpenseDataList.size();
    }

    @Override
    public int getSwipeLayoutResourceId(int position) {
        return R.id.swipe;
    }

    public void expenseDeleted() {
        mExpenseDataList.remove(deletingItemPosition);
        notifyItemRemoved(deletingItemPosition);
    }

    public void expenseDeleteErroredOut() {

    }

    class UserExpenseViewHolder extends RecyclerView.ViewHolder {

        private ImageView mIvBill;
        private TextView mTvExpenseName;
        private TextView mTvAmount;
        private TextView mTvDate;
        private TextView mTvUserName;
        private View buttonDelete;
        private final SwipeLayout swipeLayout;

        private UserExpenseViewHolder(View itemView) {
            super(itemView);
            mIvBill = (ImageView) itemView.findViewById(R.id.ivBill);
            mTvExpenseName = (TextView) itemView.findViewById(R.id.tvExpenseName);
            mTvAmount = (TextView) itemView.findViewById(R.id.tvAmount);
            mTvDate = (TextView) itemView.findViewById(R.id.tvDate);
            buttonDelete = itemView.findViewById(R.id.llDelete);
            mTvUserName = (TextView) itemView.findViewById(R.id.tvUserName);
            swipeLayout = (SwipeLayout) itemView.findViewById(R.id.swipe);
        }
    }
}
