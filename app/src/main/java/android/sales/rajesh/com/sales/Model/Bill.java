package android.sales.rajesh.com.sales.Model;

import android.database.Cursor;
import android.icu.text.DateFormat;
import android.sales.rajesh.com.sales.Database.DatabaseManager;
import android.util.Log;

import java.io.Serializable;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.HashMap;
import java.util.List;
import java.util.Locale;

/**
 * Created by Karthik on 2/11/17.
 */

public class Bill implements Serializable,Comparable<Bill>{

    private static final String TAG = Bill.class.getSimpleName();


    private Integer id;
    private Integer merchantId;
    private String billingNumber;
    private String date;
    private Integer aid;
    private String cType;
    private Double billingAmount;
    private Double billingBalance;

    private Integer noBalance;
    private Double totalDiscounts ;
    private String modifiedAt ;
    private Integer modifiedBy ;






    public Bill(){

        this.merchantId = 0;
        this.id = 0;
        this.billingNumber = "";
        this.date = "";
        this.cType = "";
        this.billingAmount = 0.00;
        this.billingBalance = 0.00;
        this.noBalance = 0;
        this.totalDiscounts = 0.0;
        this.modifiedAt = "";
        this.modifiedBy = 0;


    }
    public Bill(int id,int merchantId,String billingNumber,String date,int aid,String cType, double billingAmount,double billingBalance ,int noBalance, double totalDiscounts, String modifiedAt, int modifiedBy){
        this.id = id;
        this.merchantId = merchantId;
        this.billingNumber = billingNumber;
        this.date = date;
        this.aid = aid;
        this.cType = cType;
        this.billingAmount = billingAmount;
        this.billingBalance = billingBalance;
        this.noBalance = noBalance;
        this.totalDiscounts = totalDiscounts;
        this.modifiedAt = modifiedAt;
        this.modifiedBy = modifiedBy;


    }

    public int getMerchantId() {
        return merchantId;
    }

    public void setMerchantId(int merchantId) {
        this.merchantId = merchantId;
    }

    public int getId() {
        return id;
    }

    public void setId(int id) {
        this.id = id;
    }

    public String getBillingNumber() {
        return billingNumber;
    }

    public void setBillingNumber(String billingNumber) {
        this.billingNumber = billingNumber;
    }

    public String getDate() {
        return date;
    }

    public void setDate(String date) {

        this.date = date;
    }
    public int getAid() {
        return aid;
    }

    public void setAid(int aid) {
        this.aid = aid;
    }

    public String getcType() {
        return cType;
    }

    public void setcType(String cType) {
        this.cType = cType;
    }


    public double getBillingBalance() {
        return billingBalance;
    }

    public void setBillingBalance(double billingBalance) {
        this.billingBalance = billingBalance;
    }

    public double getBillingAmount() {
        return billingAmount;
    }

    public void setBillingAmount(double billingAmount) {

        this.billingAmount = billingAmount;
    }


    public Integer getNoBalance() {
        return noBalance;
    }

    public void setNoBalance(Integer noBalance) {
        this.noBalance = noBalance;
    }

    public Double getTotalDiscounts() {
        return totalDiscounts;
    }

    public void setTotalDiscounts(Double totalDiscounts) {
        this.totalDiscounts = totalDiscounts;
    }

    public String getModifiedAt() {
        return modifiedAt;
    }

    public void setModifiedAt(String modifiedAt) {
        this.modifiedAt = modifiedAt;
    }

    public Integer getModifiedBy() {
        return modifiedBy;
    }

    public void setModifiedBy(Integer modifiedBy) {
        this.modifiedBy = modifiedBy;
    }
    @Override
    public int compareTo(Bill o) {
        return 0;
    }


    public static String TABLE_NAME = "MERCHANTBILLS";
    public static final String COLUMN_BILL_MERCHANT_ID = "merchantId";
    public static final String COLUMN_BILL_ID = "_id";
    public static final String COLUMN_BILL_NUMBER = "billNumber";
    public static final String COLUMN_BILL_DATE = "billDate";
    public static final String COLUMN_BILL_AID = "aId";
    public static final String COLUMN_BILL_TYPE = "commodityType";
    public static final String COLUMN_BILL_AMOUNT = "billAmount";
    public static final String COLUMN_BILL_BALANCE = "billBalance";
    public static final String COLUMN_BILL_NO_BALANCE = "noBalance";
    public static final String COLUMN_BILL_TOTAL_DISCOUNTS = "totalDiscounts";
    public static final String COLUMN_BILL_MODIFIEDAT = "modifiedAt";
    public static final String COLUMN_BILL_MODIFIEDBY = "modifiedBy";



    private static String[] keyNames = {
            COLUMN_BILL_ID,
            COLUMN_BILL_MERCHANT_ID,
            COLUMN_BILL_NUMBER,
            COLUMN_BILL_DATE,
            COLUMN_BILL_AID,
            COLUMN_BILL_TYPE,
            COLUMN_BILL_AMOUNT,
            COLUMN_BILL_BALANCE,
            COLUMN_BILL_NO_BALANCE,
            COLUMN_BILL_TOTAL_DISCOUNTS,
            COLUMN_BILL_MODIFIEDAT,
            COLUMN_BILL_MODIFIEDBY
    };

    public static final String CREATE_BILLS_TABLE =
            "CREATE TABLE " + TABLE_NAME
                    + " ( "
//                    + COLUMN_BILL_ID + " INTEGER PRIMARY KEY AUTOINCREMENT, "

                    + COLUMN_BILL_ID + " INTEGER, "
                    + COLUMN_BILL_MERCHANT_ID + " INTEGER, "
                    + COLUMN_BILL_NUMBER + " TEXT, "
                    + COLUMN_BILL_DATE + " TEXT, "
                    + COLUMN_BILL_AID + " INTEGER, "
                    + COLUMN_BILL_TYPE + " TEXT, "
                    + COLUMN_BILL_AMOUNT + " REAL, "
                    + COLUMN_BILL_BALANCE + " REAL, "
                    + COLUMN_BILL_NO_BALANCE + " INTEGER, "
                    + COLUMN_BILL_TOTAL_DISCOUNTS + " REAL, "
                    + COLUMN_BILL_MODIFIEDAT + " TEXT, "
                    + COLUMN_BILL_MODIFIEDBY + " INTEGER )";


    public static final String DROP_BILLS_TABLE =
            "DROP TABLE [ IF EXISTS ] " + TABLE_NAME;




    public void persistData(){

        HashMap<String, Object> map = new HashMap<String, Object>();

        map.put(COLUMN_BILL_ID, id);
        map.put(COLUMN_BILL_MERCHANT_ID, merchantId);
        map.put(COLUMN_BILL_NUMBER, billingNumber);
        map.put(COLUMN_BILL_DATE, date);
        map.put(COLUMN_BILL_AID, aid);
        map.put(COLUMN_BILL_TYPE, cType);
        map.put(COLUMN_BILL_AMOUNT, billingAmount);
        map.put(COLUMN_BILL_BALANCE, billingBalance);
        map.put(COLUMN_BILL_NO_BALANCE, noBalance);
        map.put(COLUMN_BILL_TOTAL_DISCOUNTS, totalDiscounts);
        map.put(COLUMN_BILL_MODIFIEDAT, modifiedAt);
        map.put(COLUMN_BILL_MODIFIEDBY, modifiedBy);


        DatabaseManager.getInstance(null).insert(TABLE_NAME, map);

    }


    public  static void deleteAllBills(){

        String deleteQuery = "delete from "+TABLE_NAME;

        DatabaseManager.getInstance(null).deleteRows(TABLE_NAME,null);

    }

    public static List<Bill> readData(String whereCondition){

        DatabaseManager.getInstance(null).open();
        Cursor cursor = DatabaseManager.getInstance(null).executeRawQuery(whereCondition);

        if(cursor == null || cursor.getCount() < 1){
            Log.i(TAG, "Cursor count is null");
            return null;
        }

        List<Bill> billList = new ArrayList<>();
        Bill bill = null;

        for (int j = 0; j < cursor.getCount(); j++) {

            cursor.moveToPosition(j);

            bill = new Bill(cursor.getInt(0), cursor.getInt(1),cursor.getString(2) ,cursor.getString(3),cursor.getInt(4),cursor.getString(5),cursor.getDouble(6),cursor.getDouble(7),cursor.getInt(8),cursor.getDouble(9),cursor.getString(10),cursor.getInt(11));
            billList.add(bill);
        }

        DatabaseManager.getInstance(null).close();

        return billList;
    }

}
