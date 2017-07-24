package com.expensetracker.unclinteveedu.helpers;


import android.content.Context;
import android.content.DialogInterface;
import android.support.v7.app.AlertDialog;

/*
Ver : 1.0
Last Updated : 26-Nov-2015
This class can be used to show alert dialogs
 */
public class AlertHelper {
    private Context mContext;
    private AlertDialog alertDialog;

    /**
     * @param mContext Activity context
     */
    public AlertHelper(Context mContext) {
        this.mContext = mContext;
    }

    public AlertDialog getAlertDialog() {
        return alertDialog;
    }


    public AlertDialog showAlert(String message, String positiveButtonText, boolean cancellable) {
        if (alertDialog != null && alertDialog.isShowing())
            return alertDialog;

        AlertDialog.Builder alertDialogBuilder = new AlertDialog.Builder(mContext);
        alertDialogBuilder.setMessage(message);
        alertDialogBuilder.setPositiveButton(positiveButtonText,
                new DialogInterface.OnClickListener() {
                    public void onClick(DialogInterface dialog, int which) {
                        dialog.dismiss();
                    }
                });
        alertDialog = alertDialogBuilder.create();
        alertDialog.setCancelable(cancellable);
        if (mContext != null)
            alertDialog.show();
        return alertDialog;
    }

    public AlertDialog showAlert(String message, String positiveButtonText, final PositiveCallBack positiveCallBack, boolean cancellable) {
        if (alertDialog != null && alertDialog.isShowing())
            return alertDialog;

        AlertDialog.Builder alertDialogBuilder = new AlertDialog.Builder(mContext);
        if (message != null)
            alertDialogBuilder.setMessage(message);
        if (positiveCallBack != null)
            alertDialogBuilder.setPositiveButton(positiveButtonText,
                    new DialogInterface.OnClickListener() {
                        public void onClick(DialogInterface dialog, int which) {
                            positiveCallBack.onClick(dialog, which);
                        }
                    });
        alertDialog = alertDialogBuilder.create();
        alertDialog.setCancelable(cancellable);
        if (mContext != null)
            alertDialog.show();
        return alertDialog;
    }

    public AlertDialog showAlert(String title, String message, String positiveButtonText, boolean cancellable) {
        if (alertDialog != null && alertDialog.isShowing())
            return alertDialog;

        AlertDialog.Builder alertDialogBuilder = new AlertDialog.Builder(mContext);
        alertDialogBuilder.setMessage(message);
        alertDialogBuilder.setTitle(title);
        alertDialogBuilder.setPositiveButton(positiveButtonText,
                new DialogInterface.OnClickListener() {
                    public void onClick(DialogInterface dialog, int which) {
                        dialog.dismiss();
                    }
                });
        alertDialog = alertDialogBuilder.create();
        alertDialog.setCancelable(cancellable);
        alertDialog.show();
        return alertDialog;
    }

    public AlertDialog showAlert(String title, String message, String positiveButtonText, final PositiveCallBack positiveCallBack, boolean cancellable) {
        if (alertDialog != null && alertDialog.isShowing())
            return alertDialog;

        AlertDialog.Builder alertDialogBuilder = new AlertDialog.Builder(mContext);
        if (title != null)
            alertDialogBuilder.setTitle(title);
        if (message != null)
            alertDialogBuilder.setMessage(message);
        if (positiveCallBack != null)
            alertDialogBuilder.setPositiveButton(positiveButtonText,
                    new DialogInterface.OnClickListener() {
                        public void onClick(DialogInterface dialog, int which) {
                            positiveCallBack.onClick(dialog, which);
                        }
                    });
        alertDialog = alertDialogBuilder.create();
        alertDialog.setCancelable(cancellable);
        if (mContext != null)
            alertDialog.show();
        return alertDialog;
    }

    public AlertDialog showAlert(String title, String message, String positiveButtonText, String negativeButtonText, final PositiveCallBack positiveCallBack, final NegativeCallBack negativeCallBack, boolean cancellable) {
        if (alertDialog != null && alertDialog.isShowing())
            return alertDialog;

        AlertDialog.Builder alertDialogBuilder = new AlertDialog.Builder(mContext);
        if (title != null)
            alertDialogBuilder.setTitle(title);
        if (message != null)
            alertDialogBuilder.setMessage(message);
        if (positiveCallBack != null)
            alertDialogBuilder.setPositiveButton(positiveButtonText,
                    new DialogInterface.OnClickListener() {
                        public void onClick(DialogInterface dialog, int which) {
                            positiveCallBack.onClick(dialog, which);
                        }
                    });
        if (negativeCallBack != null)
            alertDialogBuilder.setNegativeButton(negativeButtonText,
                    new DialogInterface.OnClickListener() {
                        public void onClick(DialogInterface dialog, int which) {
                            negativeCallBack.onClick(dialog, which);
                        }
                    });
        alertDialog = alertDialogBuilder.create();
        alertDialog.setCancelable(cancellable);
        if (mContext != null)
            alertDialog.show();
        return alertDialog;
    }

    public AlertDialog showAlert(String title, String message, String positiveButtonText, String negativeButtonText, String neutralButtonText, final PositiveCallBack positiveCallBack, final NegativeCallBack negativeCallBack, final NeutralCallBack neutralCallBack, boolean cancellable) {
        if (alertDialog != null && alertDialog.isShowing())
            return alertDialog;

        AlertDialog.Builder alertDialogBuilder = new AlertDialog.Builder(mContext);
        if (title != null)
            alertDialogBuilder.setTitle(title);
        if (message != null)
            alertDialogBuilder.setMessage(message);
        if (positiveCallBack != null)
            alertDialogBuilder.setPositiveButton(positiveButtonText,
                    new DialogInterface.OnClickListener() {
                        public void onClick(DialogInterface dialog, int which) {
                            positiveCallBack.onClick(dialog, which);
                        }
                    });
        if (negativeCallBack != null)
            alertDialogBuilder.setPositiveButton(negativeButtonText,
                    new DialogInterface.OnClickListener() {
                        public void onClick(DialogInterface dialog, int which) {
                            negativeCallBack.onClick(dialog, which);
                        }
                    });
        if (neutralCallBack != null)
            alertDialogBuilder.setPositiveButton(neutralButtonText,
                    new DialogInterface.OnClickListener() {
                        public void onClick(DialogInterface dialog, int which) {
                            neutralCallBack.onClick(dialog, which);
                        }
                    });
        alertDialog = alertDialogBuilder.create();
        alertDialog.setCancelable(cancellable);
        if (mContext != null)
            alertDialog.show();
        return alertDialog;
    }

    public interface PositiveCallBack {
        void onClick(DialogInterface dialog, int which);

    }

    public interface NegativeCallBack {
        void onClick(DialogInterface dialog, int which);

    }

    public interface NeutralCallBack {
        void onClick(DialogInterface dialog, int which);

    }

}
