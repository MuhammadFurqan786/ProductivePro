package eu.faircode.netguard;

import android.app.ProgressDialog;
import android.content.Context;
import android.content.SharedPreferences;
import android.util.Log;
import android.view.View;
import android.view.inputmethod.InputMethodManager;
import android.widget.Toast;

import java.text.DecimalFormat;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.Calendar;
import java.util.Date;
import java.util.concurrent.TimeUnit;

public class Utilities {

    static ProgressDialog dialog;
    private Context context;

    public Utilities(Context context){
        this.context = context;
    }


    public static void showProgressDialog(Context ctx, String msg) {
        dialog = new ProgressDialog(ctx);
        dialog.setProgressStyle(ProgressDialog.STYLE_SPINNER);
        dialog.setMessage(msg);
        dialog.setIndeterminate(true);
        dialog.setCanceledOnTouchOutside(false);
        dialog.show();
    }

    public static void hideProgressDialog() {
        if (dialog != null && dialog.isShowing()) {
            dialog.cancel();
            dialog = null;
        }

    }

    public static void makeToast(Context context, String message) {
        Toast.makeText(context, message, Toast.LENGTH_SHORT).show();
    }

    public static void saveInt(Context context, String key, int value) {
        SharedPreferences sharedPref = context.getSharedPreferences("SocialCeoSharedStorage", Context.MODE_PRIVATE);
        SharedPreferences.Editor editor = sharedPref.edit();
        editor.putInt(key, value);
        editor.apply();
    }

    public static int getInt(Context context, String key) {

        SharedPreferences sharedPref = context.getSharedPreferences("SocialCeoSharedStorage", Context.MODE_PRIVATE);
        int val=sharedPref.getInt(key,0);
        return val;

    }

    public static void saveString(Context context, String key, String value) {
        SharedPreferences sharedPref = context.getSharedPreferences("SocialCeoSharedStorage", Context.MODE_PRIVATE);
        SharedPreferences.Editor editor = sharedPref.edit();
        editor.putString(key, value);
        editor.apply();

    }

    public static String getString(Context context, String key) {

        SharedPreferences sharedPref = context.getSharedPreferences("SocialCeoSharedStorage", Context.MODE_PRIVATE);
        String val=sharedPref.getString(key,"");

        return val;

    }

    public static void clearSharedPref(Context context) {
        SharedPreferences sharedPref = context.getSharedPreferences("SocialCeoSharedStorage", Context.MODE_PRIVATE);
        SharedPreferences.Editor editor = sharedPref.edit();
        editor.clear();
        editor.apply();



    }

    public static void hideKeyboard(View view, Context context) {
        InputMethodManager in = (InputMethodManager) context.getSystemService(Context.INPUT_METHOD_SERVICE);
        in.hideSoftInputFromWindow(view.getWindowToken(), InputMethodManager.HIDE_NOT_ALWAYS);

    }

    public static String changeDateFormate(String date, String from, String to){

        String finalDate= date;

//        SimpleDateFormat date_formate = new SimpleDateFormat("MM/dd/yyyy");
        SimpleDateFormat date_formate = new SimpleDateFormat(from);

        Date datee = null;
        try {
            datee = date_formate.parse(date);

        } catch (ParseException e) {
            e.printStackTrace();
        }

//        SimpleDateFormat df = new SimpleDateFormat("MMM-dd-yyyy");
        SimpleDateFormat df = new SimpleDateFormat(to);
        Calendar calendar = Calendar.getInstance();
        calendar.setTime(datee);
        finalDate = df.format(calendar.getTime());

        return finalDate;

    }

    public static String differenceBetweenDated(String dates_formate, String dateEnd, String dateStr){

        String days = "";

        SimpleDateFormat sdf = new SimpleDateFormat(dates_formate);

        try {
            Date dateS = sdf.parse(dateStr);
            Date dateE = sdf.parse(dateEnd);

            long diff = dateE.getTime() - dateS.getTime();


            days = String.valueOf(TimeUnit.DAYS.convert(diff, TimeUnit.MILLISECONDS));

        } catch (ParseException e) {
            e.printStackTrace();
        }



        return days;

    }

    public static String getCurrentDate(){

        Calendar calendar = Calendar.getInstance();
        SimpleDateFormat mdformat = new SimpleDateFormat("yyyy-MM-dd");
        String currentDate = mdformat.format(calendar.getTime());

        return currentDate;

    }

    public static String formattTwoDecimal(Context context, int number){


        if (number == 0){

            return String.valueOf("RS 0.00");
        }
        else {

            String qa = String.valueOf(number);
            String nocomma = qa.replace(",", "");
            Float f = Float.parseFloat(nocomma);
            String.format("%.2f",f);
            DecimalFormat formatter = new DecimalFormat("#,###,###.00#");
            String yourFormattedString = formatter.format(f);
            return String.valueOf("RS "+yourFormattedString);
        }

    }

    public static class TimeAgo2 {

        public String covertTimeToText(String dataDate) {

            String convTime = null;

            String prefix = "";
            String suffix = "Ago";

            try {
                SimpleDateFormat dateFormat = new SimpleDateFormat("yyyy-MM-dd'T'HH:mm:ss");
                Date pasTime = dateFormat.parse(dataDate);

                Date nowTime = new Date();

                long dateDiff = nowTime.getTime() - pasTime.getTime();

                long second = TimeUnit.MILLISECONDS.toSeconds(dateDiff);
                long minute = TimeUnit.MILLISECONDS.toMinutes(dateDiff);
                long hour   = TimeUnit.MILLISECONDS.toHours(dateDiff);
                long day  = TimeUnit.MILLISECONDS.toDays(dateDiff);

                if (second < 60) {
                    convTime = second + " Seconds " + suffix;
                } else if (minute < 60) {
                    convTime = minute + " Minutes "+suffix;
                } else if (hour < 24) {
                    convTime = hour + " Hours "+suffix;
                } else if (day >= 7) {
                    if (day > 360) {
                        convTime = (day / 360) + " Years " + suffix;
                    } else if (day > 30) {
                        convTime = (day / 30) + " Months " + suffix;
                    } else {
                        convTime = (day / 7) + " Week " + suffix;
                    }
                } else if (day < 7) {
                    convTime = day+" Days "+suffix;
                }

            } catch (ParseException e) {
                e.printStackTrace();
                Log.e("ConvTimeE", e.getMessage());
            }

            return convTime;
        }

    }




}
