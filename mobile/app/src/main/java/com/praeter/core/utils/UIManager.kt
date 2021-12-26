package com.praeter.core.utils

import android.app.Activity
import android.app.AlertDialog
import android.content.Context
import android.util.Log
import android.view.View
import android.view.inputmethod.InputMethodManager
import android.widget.TextView
import android.widget.Toast
import com.google.android.material.snackbar.Snackbar
import com.praeter.R
import com.praeter.data.local.bean.SnackBarType

object UIManager {
    /**
     * Hide the keyboard
     *
     * @param view
     */
    fun hideKeyboard(context: Context, view: View) {
        val imm = context.getSystemService(Context.INPUT_METHOD_SERVICE) as InputMethodManager
        imm.hideSoftInputFromWindow(view.windowToken, 0)
    }

    /**
     * Show an alertDialog
     *
     * @param
     * @param title
     * @param message
     * @param negativeMessage
     * @param positiveMessage
     */
    fun showAlertDialog(
        activity: Activity?,
        context: Context,
        title: String?,
        message: String?,
        negativeMessage: String,
        positiveMessage: String?
    ) {
        Log.i("Activity - AlertDialog", "Show alert dialog")
        val alertDialog = AlertDialog.Builder(context)

        // Setting Dialog Title
        alertDialog.setTitle(title)

        // Setting Dialog Message
        alertDialog.setMessage(message)
        alertDialog.setNegativeButton(negativeMessage) { dialog, which ->
            showActionInToast(context, negativeMessage)
            if (negativeMessage.equals("Réessayer", ignoreCase = true)) {
                //launchActivity(context, MainActivity.class, Intent.FLAG_ACTIVITY_CLEAR_TOP|Intent.FLAG_ACTIVITY_RESET_TASK_IF_NEEDED|Intent.FLAG_ACTIVITY_NEW_TASK, null, null);
            }
            if (negativeMessage.equals(
                    "Réessayer",
                    ignoreCase = true
                ) && PraeterNetworkManager.isConnected(context)
            ) {
                dialog.dismiss()
            }
        }
        alertDialog.setPositiveButton(positiveMessage) { dialog, which ->
            showActionInToast(context, positiveMessage)
            activity?.onBackPressed()
            if (negativeMessage.equals("Quitter", ignoreCase = true)) {
                activity!!.finish()
            }
        }
        alertDialog.setCancelable(false)

        // Showing Alert Message
        alertDialog.show()
    }

    fun showActionInToast(context: Context?, textToShow: String?) {
        Toast.makeText(context, textToShow, Toast.LENGTH_LONG).show()
    }

    fun showActionInSnackBar(context: Context, view: View?, message: String?, type: SnackBarType?) {
        // create instance
        val snackBar = Snackbar.make(view!!, message!!, Snackbar.LENGTH_LONG)

        /*switch (type){
            case NORMAL:
                // set action button color
                snackBar.setActionTextColor(context.getResources().getColor(R.color.indigo));
                break;

            case WARNING:
                snackBar.setActionTextColor(context.getResources().getColor(R.color.indigo));
                break;

            case ALERT:
                snackBar.setActionTextColor(context.getResources().getColor(R.color.indigo));
                break;
        }*/

        // get snackBar view
        val snackBarView = snackBar.view

        // change snackbar text color
        val snackBarTextId = com.google.android.material.R.id.snackbar_text
        val textView = snackBarView.findViewById<View>(snackBarTextId) as TextView
        // set action button color
        when (type) {
            SnackBarType.NORMAL -> textView.setTextColor(context.resources.getColor(R.color.white))
            SnackBarType.WARNING -> textView.setTextColor(context.resources.getColor(R.color.warning))
            SnackBarType.ALERT -> textView.setTextColor(context.resources.getColor(R.color.error))
        }
        snackBar.show()
    }
}