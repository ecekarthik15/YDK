package android.sales.rajesh.com.sales.Model;

import android.database.Cursor;
import android.sales.rajesh.com.sales.Database.DatabaseManager;
import android.util.Log;

import java.io.Serializable;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Vector;

/**
 * Created by Karthik on 1/31/17.
 */

public class Merchant implements Serializable,Comparable<Merchant>{

    private static final String TAG = Merchant.class.getSimpleName();


    private Integer id;
    private Integer districtId;
    private Integer cityId;
    private String district;

    private Integer userId;

    private String name;
    private Double totalBalance;
    private Double totalABalance;
    private Double totalEBalance;

    private String city;

    private String modifiedAt;
    private Integer modifiedBy;




    private List<Bill> billList;


    public Merchant(){

        this.id = 0;
        this.name = "";
        this.totalBalance = 0.00;
        this.totalABalance = 0.00;
        this.totalEBalance = 0.00;
        this.city = "";
        this.districtId = 0;
        this.cityId = 0;
        this.district = "";
        this.userId = 0;
        this.modifiedAt = "";
        this.modifiedBy = 0;

    }
    public Merchant(int id, int userId, String name, int districtId, int cityId, String district, double totalBalance, double totalABalance, double totalEBalance, String city, String modifiedAt, int modifiedBy){
        this.id = id;
        this.userId = userId;
        this.name = name;
        this.totalBalance = totalBalance;
        this.totalABalance = totalABalance;
        this.totalEBalance = totalEBalance;
        this.city = city;
        this.districtId = districtId;
        this.cityId = cityId;
        this.district = district;

        this.modifiedAt = modifiedAt;
        this.modifiedBy = modifiedBy;

    }


    public int getId() {
        return id;
    }

    public void setId(int id) {
        this.id = id;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public double getTotalBalance() {
        return totalBalance;
    }

    public void setTotalBalance(double totalBalance) {
        this.totalBalance = totalBalance;
    }

    public double getTotalABalance() {
        return totalABalance;
    }

    public void setTotalABalance(double totalABalance) {
        this.totalABalance = totalABalance;
    }

    public double getTotalEBalance() {
        return totalEBalance;
    }

    public void setTotalEBalance(double totalEBalance) {
        this.totalEBalance = totalEBalance;
    }
    public int getDistrictId() {
        return districtId;
    }

    public void setDistrictId(int districtId) {
        this.districtId = districtId;
    }

    public int getCityId() {
        return cityId;
    }

    public void setCityId(int cityId) {
        this.cityId = cityId;
    }

    public String getDistrict() {
        return district;
    }

    public void setDistrict(String district) {
        this.district = district;
    }

    public int getUserId() {
        return userId;
    }

    public void setUserId(int userId) {
        this.userId = userId;
    }

    public String getCity() {
        return city;
    }

    public void setCity(String city) {
        this.city = city;
    }

    public String getModifiedAt() {
        return modifiedAt;
    }

    public void setModifiedAt(String modifiedAt) {
        this.modifiedAt = modifiedAt;
    }

    public int getModifiedBy() {
        return modifiedBy;
    }

    public void setModifiedBy(int modifiedBy) {
        this.modifiedBy = modifiedBy;
    }


    public List<Bill> getBillList() {
        return billList;
    }

    public void setBillList(List<Bill> billList) {
        this.billList = billList;
    }

    @Override
    public int compareTo(Merchant o) {
        return 0;
    }


    public static String TABLE_NAME = "MERCHANTS";
	public static final String COLUMN_MERCHANT_ID = "_id";
    public static final String COLUMN_MERCHANT_USER_ID = "userId";
    public static final String COLUMN_MERCHANT_NAME = "name";
    public static final String COLUMN_MERCHANT_DIST_ID = "dId";
    public static final String COLUMN_MERCHANT_CITY_ID = "cId";
    public static final String COLUMN_MERCHANT_DIST = "dName";
	public static final String COLUMN_MERCHANT_TOTAL_BALANCE = "totalBalance";
    public static final String COLUMN_MERCHANT_A_BALANCE = "aBalance";
    public static final String COLUMN_MERCHANT_E_BALANCE = "eBalance";
    public static final String COLUMN_MERCHANT_CITY = "cName";

    public static final String COLUMN_MERCHANT_MODIFIED_AT = "modifiedAt";
    public static final String COLUMN_MERCHANT_MODIFIED_BY = "modifiedBy";



    private static String[] keyNames = {
            COLUMN_MERCHANT_ID,
            COLUMN_MERCHANT_USER_ID,
            COLUMN_MERCHANT_NAME,
            COLUMN_MERCHANT_DIST_ID,
            COLUMN_MERCHANT_CITY_ID,
            COLUMN_MERCHANT_DIST,
            COLUMN_MERCHANT_TOTAL_BALANCE,
            COLUMN_MERCHANT_A_BALANCE,
            COLUMN_MERCHANT_E_BALANCE,
            COLUMN_MERCHANT_CITY,
            COLUMN_MERCHANT_MODIFIED_AT,
            COLUMN_MERCHANT_MODIFIED_BY
    };

	public static final String CREATE_MERCHANT_TABLE =
	"CREATE TABLE " + TABLE_NAME
	+ " ( "
	+ COLUMN_MERCHANT_ID + " INTEGER, "
    +COLUMN_MERCHANT_USER_ID + " INTEGER, "
    + COLUMN_MERCHANT_NAME + " TEXT, "
    + COLUMN_MERCHANT_DIST_ID + " INTEGER, "
    + COLUMN_MERCHANT_CITY_ID + " INTEGER, "
    + COLUMN_MERCHANT_DIST + " TEXT, "
	+ COLUMN_MERCHANT_TOTAL_BALANCE + " REAL, "
    + COLUMN_MERCHANT_A_BALANCE + " REAL, "
    + COLUMN_MERCHANT_E_BALANCE + " REAL, "
    +COLUMN_MERCHANT_CITY + " TEXT, "
    + COLUMN_MERCHANT_MODIFIED_AT + " TEXT, "
    + COLUMN_MERCHANT_MODIFIED_BY + " INTEGER, "
//    +" PRIMARY KEY('"+COLUMN_MERCHANT_ID+"') ) WITHOUT ROWID";
    +" PRIMARY KEY('"+COLUMN_MERCHANT_ID+"') )";

    public static final String DROP_MERCHANT_TABLE =
            "DROP TABLE [ IF EXISTS ] " + TABLE_NAME;






    private void persistData(){

		HashMap<String, Object> map = new HashMap<String, Object>();

        map.put(COLUMN_MERCHANT_ID, id);
        map.put(COLUMN_MERCHANT_USER_ID, userId);
        map.put(COLUMN_MERCHANT_NAME, name);
        map.put(COLUMN_MERCHANT_DIST_ID, districtId);
        map.put(COLUMN_MERCHANT_CITY_ID, cityId);
        map.put(COLUMN_MERCHANT_DIST, district);
        map.put(COLUMN_MERCHANT_TOTAL_BALANCE, totalBalance);
        map.put(COLUMN_MERCHANT_A_BALANCE, totalABalance);
        map.put(COLUMN_MERCHANT_E_BALANCE, totalEBalance);
        map.put(COLUMN_MERCHANT_CITY, city);
        map.put(COLUMN_MERCHANT_MODIFIED_AT, modifiedAt);
        map.put(COLUMN_MERCHANT_MODIFIED_BY, modifiedBy);



        DatabaseManager.getInstance(null).insert(TABLE_NAME, map);

        for (Bill bill:billList) {

            bill.persistData();

        }

	}


    public static void persistAllMerchants(List<Merchant> list){

        DatabaseManager.getInstance(null).open();

        DatabaseManager.getInstance(null).sqlitedb.beginTransaction();

        for (Merchant merchant:list) {
            merchant.persistData();
        }
        DatabaseManager.getInstance(null).sqlitedb.setTransactionSuccessful();

        DatabaseManager.getInstance(null).sqlitedb.endTransaction();

//        DatabaseManager.getInstance(null).close();

    }


    public  static void deleteAllMerchants(int userId){
        DatabaseManager.getInstance(null).open();

        DatabaseManager.getInstance(null).sqlitedb.beginTransaction();

        String whereCondn = COLUMN_MERCHANT_USER_ID+" == '"+userId+"'";

        DatabaseManager.getInstance(null).deleteRows(TABLE_NAME,whereCondn);

        Bill.deleteAllBills();

        DatabaseManager.getInstance(null).sqlitedb.setTransactionSuccessful();

        DatabaseManager.getInstance(null).sqlitedb.endTransaction();

//        DatabaseManager.getInstance(null).close();


    }

    public static List readDistinctDistrict(String whereCondition) {
        List<String> districtList = new ArrayList<>();


        DatabaseManager.getInstance(null).open();
        Cursor cursor = DatabaseManager.getInstance(null).executeRawQuery(whereCondition);

        if(cursor == null || cursor.getCount() < 1){
            Log.i(TAG, "Cursor count is null");
            return null;
        }

        Vector<Merchant> merchantVector = new Vector<Merchant>();
        Merchant merchant = null;

        for (int j = 0; j < cursor.getCount(); j++) {


            cursor.moveToPosition(j);

            districtList.add(cursor.getString(0));

        }

//        DatabaseManager.getInstance(null).close();


        return districtList;

    }


    public static List readDistinctaCity(String whereCondition) {
        List<String> cityList = new ArrayList<>();


        DatabaseManager.getInstance(null).open();
        Cursor cursor = DatabaseManager.getInstance(null).executeRawQuery(whereCondition);

        if(cursor == null || cursor.getCount() < 1){
            Log.i(TAG, "Cursor count is null");
            return null;
        }

        Vector<Merchant> merchantVector = new Vector<Merchant>();
        Merchant merchant = null;

        for (int j = 0; j < cursor.getCount(); j++) {


            cursor.moveToPosition(j);

            cityList.add(cursor.getString(0));

        }

//        DatabaseManager.getInstance(null).close();


        return cityList;

    }

    public static Cursor readDataCurser(String whereCondition){

        DatabaseManager.getInstance(null).open();
        Cursor cursor = DatabaseManager.getInstance(null).executeRawQuery(whereCondition);


        return cursor;

    }


    public static Vector readData(String whereCondition){

		DatabaseManager.getInstance(null).open();
		Cursor cursor = DatabaseManager.getInstance(null).executeRawQuery(whereCondition);

		if(cursor == null || cursor.getCount() < 1){
			Log.i(TAG, "Cursor count is null");
			return null;
		}

		Vector<Merchant> merchantVector = new Vector<Merchant>();
            Merchant merchant = null;

		for (int j = 0; j < cursor.getCount(); j++) {

			cursor.moveToPosition(j);

            merchant = new Merchant(cursor.getInt(0), cursor.getInt(1),cursor.getString(2) ,cursor.getInt(3),cursor.getInt(4),cursor.getString(5),cursor.getDouble(6),cursor.getDouble(7),cursor.getDouble(8),cursor.getString(9),cursor.getString(10),cursor.getInt(11));
            merchantVector.add(merchant);
		}


		return merchantVector;
	}

}
