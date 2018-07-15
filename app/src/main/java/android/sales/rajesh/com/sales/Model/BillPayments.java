package android.sales.rajesh.com.sales.Model;

import android.database.Cursor;
import android.media.Image;
import android.sales.rajesh.com.sales.Database.DatabaseManager;
import android.util.Log;

import java.io.Serializable;
import java.sql.Blob;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;

/**
 * Created by Karthik on 2/20/17.
 */

public class BillPayments implements Serializable,Comparable<BillPayments> {

    private static final String TAG = BillPayments.class.getSimpleName();

    private Integer id;
    private Integer billId;
    private Long paymentDate;
    private Double paymentAmount;
    private Double billBalance;
    private Double discountAmount;
    private Integer receiptPrinted;
//    private Image receiptSignature;
    private Integer syncComplete;
    private String modifiedAt;
    private Integer modifiedBy;

    public BillPayments(){

        this.id = 0;
        this.billId = 0;
        this.paymentDate = 0L;
        this.paymentAmount = 0.0;
        this.billBalance = 0.0;
        this.discountAmount = 0.00;
        this.receiptPrinted = 0;
        this.syncComplete = 0;
        this.modifiedAt = "";
        this.modifiedBy = 0;

    }

    public BillPayments(Integer id, Integer billId, long paymentDate, Double paymentAmount, Double billBalance, Integer receiptPrinted, Double discountAmount, Integer syncComplete, String modifiedAt, Integer modifiedBy) {

        this.id = id;
        this.billId = billId;
        this.paymentDate = paymentDate;
        this.paymentAmount = paymentAmount;
        this.billBalance = billBalance;
        this.receiptPrinted = receiptPrinted;
        this.discountAmount = discountAmount;
        this.syncComplete = syncComplete;
        this.modifiedAt = modifiedAt;
        this.modifiedBy = modifiedBy;
    }

    public Integer getId() {
        return id;
    }

    public void setId(Integer id) {
        this.id = id;
    }

    public Integer getBillId() {
        return billId;
    }

    public void setBillId(Integer billId) {
        this.billId = billId;
    }

    public long getPaymentDate() {
        return paymentDate;
    }

    public void setPaymentDate(long paymentDate) {
        this.paymentDate = paymentDate;
    }

    public Double getPaymentAmount() {
        return paymentAmount;
    }

    public void setPaymentAmount(Double paymentAmount) {
        this.paymentAmount = paymentAmount;
    }

    public Double getBillBalance() {
        return billBalance;
    }

    public void setBillBalance(Double billBalance) {
        this.billBalance = billBalance;
    }

    public Integer getReceiptPrinted() {
        return receiptPrinted;
    }

    public void setReceiptPrinted(Integer receiptPrinted) {
        this.receiptPrinted = receiptPrinted;
    }

    public Double getDiscountAmount() {
        return discountAmount;
    }

    public void setDiscountAmount(Double discountAmount) {
        this.discountAmount = discountAmount;
    }

    public Integer getSyncComplete() {
        return syncComplete;
    }

    public void setSyncComplete(Integer syncComplete) {
        this.syncComplete = syncComplete;
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
    public int compareTo(BillPayments o) {
        return 0;
    }


    public static String TABLE_NAME = "BILLPAYMENTS";
    public static final String COLUMN_PAYMENT_ID = "_id";
    public static final String COLUMN_PAYMENT_BILL_ID = "billId";
    public static final String COLUMN_PAYMENT_DATE = "paymentDate";
    public static final String COLUMN_PAYMENT_AMOUNT = "paymentAmount";
    public static final String COLUMN_PAYMENT_BILL_BALANCE = "billBalance";
    public static final String COLUMN_PAYMENT_DISCOUNT_AMOUNT = "discountAmount";
    public static final String COLUMN_PAYMENT_RECEIPT_PRINTED = "receiptPrinted";
    public static final String COLUMN_PAYMENT_SYNC_COMPLETE = "syncComplete";
    public static final String COLUMN_PAYMENT_MODIFIEDAT = "modifiedAt";
    public static final String COLUMN_PAYMENT_MODIFIEDBY = "modifiedBy";


    private static String[] keyNames = {
            COLUMN_PAYMENT_ID,
            COLUMN_PAYMENT_BILL_ID,
            COLUMN_PAYMENT_DATE,
            COLUMN_PAYMENT_AMOUNT,
            COLUMN_PAYMENT_BILL_BALANCE,
            COLUMN_PAYMENT_DISCOUNT_AMOUNT,
            COLUMN_PAYMENT_RECEIPT_PRINTED,
            COLUMN_PAYMENT_SYNC_COMPLETE,
            COLUMN_PAYMENT_MODIFIEDAT,
            COLUMN_PAYMENT_MODIFIEDBY
    };
//CREATE TABLE "BillPayments" ( `id` INTEGER, `billId` INTEGER, `paymentDate` NUMERIC, `paymentAmount` REAL, `billBalance` REAL,
// `discountAmount` REAL, `receiptPrinted` INTEGER DEFAULT 0, `receiptSignature` BLOB, `syncComplete` INTEGER DEFAULT 1, `modifiedAt` TEXT,
// `modifiedBy` INTEGER )

    public static final String CREATE_BILLPAYMENTS_TABLE =
            "CREATE TABLE " + TABLE_NAME
                    + " ( "
                    + COLUMN_PAYMENT_ID + " INTEGER PRIMARY KEY AUTOINCREMENT, "
//                    + COLUMN_PAYMENT_ID + " INTEGER, "
                    + COLUMN_PAYMENT_BILL_ID + " INTEGER, "
                    + COLUMN_PAYMENT_DATE + " NUMERIC, "
                    + COLUMN_PAYMENT_AMOUNT + " REAL, "
                    + COLUMN_PAYMENT_BILL_BALANCE + " REAL, "
                    + COLUMN_PAYMENT_DISCOUNT_AMOUNT + " REAL, "
                    + COLUMN_PAYMENT_RECEIPT_PRINTED + " INTEGER DEFAULT 0, "
                    + COLUMN_PAYMENT_SYNC_COMPLETE + " INTEGER DEFAULT 1, "
                    + COLUMN_PAYMENT_MODIFIEDAT + " TEXT, "
                    + COLUMN_PAYMENT_MODIFIEDBY + " INTEGER )";


    public static final String DROP_BILLPAYMENTS_TABLE =
            "DROP TABLE [ IF EXISTS ] " + TABLE_NAME;


    public void persistData(){

        HashMap<String, Object> map = new HashMap<String, Object>();

//        map.put(COLUMN_PAYMENT_ID, id);
        map.put(COLUMN_PAYMENT_BILL_ID, billId);
        map.put(COLUMN_PAYMENT_DATE, paymentDate);
        map.put(COLUMN_PAYMENT_AMOUNT, paymentAmount);
        map.put(COLUMN_PAYMENT_BILL_BALANCE, billBalance);
        map.put(COLUMN_PAYMENT_DISCOUNT_AMOUNT, discountAmount);
        map.put(COLUMN_PAYMENT_RECEIPT_PRINTED, receiptPrinted);
        map.put(COLUMN_PAYMENT_SYNC_COMPLETE, syncComplete);
        map.put(COLUMN_PAYMENT_MODIFIEDAT, modifiedAt);
        map.put(COLUMN_PAYMENT_MODIFIEDBY, modifiedBy);


        DatabaseManager.getInstance(null).insert(TABLE_NAME, map);

    }


    public  static void deleteAllPayments(){

        String deleteQuery = "delete from "+TABLE_NAME;

        DatabaseManager.getInstance(null).deleteRows(TABLE_NAME,null);

    }

    public static List<BillPayments> readData(String whereCondition){

        DatabaseManager.getInstance(null).open();
        Cursor cursor = DatabaseManager.getInstance(null).executeRawQuery(whereCondition);

        if(cursor == null || cursor.getCount() < 1){
            Log.i(TAG, "Cursor count is null");
            return null;
        }

        List<BillPayments> paymentList = new ArrayList<>();
        BillPayments payment = null;

        for (int j = 0; j < cursor.getCount(); j++) {

            cursor.moveToPosition(j);
            payment = new BillPayments(cursor.getInt(0), cursor.getInt(1),cursor.getInt(2) ,cursor.getDouble(3),cursor.getDouble(4),cursor.getInt(5),cursor.getDouble(6),cursor.getInt(7),cursor.getString(8),cursor.getInt(9));
            paymentList.add(payment);
        }

        DatabaseManager.getInstance(null).close();

        return paymentList;
    }
}
