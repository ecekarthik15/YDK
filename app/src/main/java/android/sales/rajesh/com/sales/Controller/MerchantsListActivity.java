package android.sales.rajesh.com.sales.Controller;

import android.app.Activity;
import android.content.Context;
import android.content.Intent;
import android.database.Cursor;
import android.graphics.Typeface;
import android.os.Bundle;
import android.os.Handler;
import android.os.Message;
import android.sales.rajesh.com.sales.Core.WebCallableCoreActivity;
import android.sales.rajesh.com.sales.Database.DatabaseManager;
import android.sales.rajesh.com.sales.Model.Merchant;
import android.sales.rajesh.com.sales.Model.Users;
import android.sales.rajesh.com.sales.Result.ParseResult;
import android.sales.rajesh.com.sales.Parser.MerchantParser;
import android.sales.rajesh.com.sales.Result.MerchantParseResult;
import android.sales.rajesh.com.sales.Utils.Constants;
import android.sales.rajesh.com.sales.Utils.Utility;
import android.sales.rajesh.com.sales.View.MerchantCurserAdapter;
import android.sales.rajesh.com.salesapp.R;
import android.support.v7.widget.Toolbar;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.ListView;
//import android.widget.Spinner;
import android.widget.Spinner;
import android.widget.TextView;

import java.util.ArrayList;
import java.util.List;
import java.util.Vector;

/**
 * Created by Karthik on 1/31/17.
 */

public class MerchantsListActivity extends WebCallableCoreActivity {

    private static final String TAG = MerchantsListActivity.class.getSimpleName();


    protected static final int LOAD_MERCHANTSLIST = 0;

    protected static final int LOAD_MERCHANT_DETAIL = 1;

    protected static final int LOAD_CITYLIST = 2;

    protected static final int LOAD_DISTRICTLIST = 3;



    DatabaseManager databaseManager ;



    List<Merchant> merchantsVector;

    ListView merchantsListView;

    MerchantCurserAdapter ListAdapter;

    Merchant selectedMerchant;

    Spinner citySP,districtSP;

    List districtList;

    List cityList;

    Users users;

    String selectedDistrict = "";
    String selectedCity= "";





    @Override
    public void loadNext(int code) {

        switch (code){

            case  LOAD_MERCHANTSLIST:{

//                Log.e(TAG,"loadNext calledddddddd : ");
//                ListAdapter.clear();
//                ListAdapter.addAll(merchantsVector);

            }
            break;

            case  LOAD_CITYLIST:{

                showCitySpinner();
            }
            break;

            case LOAD_MERCHANT_DETAIL:{
                Intent gotoMerchantDetailActivity = new Intent(MerchantsListActivity.this,
                        MerchantDetailActivity.class);
                gotoMerchantDetailActivity.putExtra("merchant", selectedMerchant);

                startActivity(gotoMerchantDetailActivity);
            }
            break;

        }

    }

    @Override
    public void loadPrev() {

        finish();

    }

    @Override
    protected void onSaveInstanceState(Bundle outState) {
        super.onSaveInstanceState(outState);

    }

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.merchants_list_activity);
        Log.d(TAG,"*************************************************");

        Toolbar toolbar = (Toolbar) findViewById(R.id.app_toolbar);
        setSupportActionBar(toolbar);
        TextView titleView = (TextView) toolbar.findViewById(R.id.toolbar_title);
        View homeBt = (View) toolbar.findViewById(R.id.toolbar_home_BT);
        homeBt.setVisibility(View.GONE);

        View rightBt = (View) toolbar.findViewById(R.id.toolbar_right_action_Bt);
        rightBt.setVisibility(View.GONE);

        titleView.setText(R.string.customer_list_title);

        merchantsListView = (ListView) findViewById(R.id.merchants_LV);

        districtSP = (Spinner) findViewById(R.id.ml_choose_district_spinner);
        citySP = (Spinner) findViewById(R.id.ml_choose_city_spinner);


        this.merchantsVector = new Vector<>();

        this.users = (Users) getIntent().getSerializableExtra("user");


//        ListAdapter = new MerchantsListAdapter(this,R.layout.merchant_list_item,merchantsVector);



//        merchantsListView.setAdapter(ListAdapter);

//        ListAdapter.notifyDataSetChanged();

        districtList = new ArrayList();
        cityList = new ArrayList();


        makeWebRequest(this, Constants.getMerchantsUrl(this.users.getUserId()),
                new MerchantParser( this ), Constants.GETTING_MERCHANT_REQUEST,
                false, "", false, false);





        merchantsListView.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> parent, View view, int position, long id) {

                selectedMerchant = ListAdapter.getItem(position);
                handler.sendEmptyMessage(LOAD_MERCHANT_DETAIL);

            }
        });





    }

    public boolean isRootActivity(){

        return true;

    }


    void showDistrictSpinner(){

        // Initializing an ArrayAdapter
        ArrayAdapter<String> spinnerArrayAdapter = new ArrayAdapter<String>(
                this,R.layout.ml_choose_city_item,districtList){
            @Override
            public View getDropDownView(int position, View convertView,
                                        ViewGroup parent) {
                View view = super.getDropDownView(position, convertView, parent);
                TextView tv = (TextView) view;

                if(position == 0){

                    tv.setBackgroundColor(getResources().getColor(R.color.colorPrimary));
                    tv.setTextColor(getResources().getColor(R.color.c0));
                    tv.setTypeface(null, Typeface.BOLD);        // for Bold only

                }else{
                    tv.setBackgroundColor(getResources().getColor(R.color.c0));
                    tv.setTextColor(getResources().getColor(R.color.c2));
//                    tv.setTypeface(null, Typeface.);

                }

                return view;
            }

            @Override
            public boolean isEnabled(int position) {
                // TODO Auto-generated method stub

                if(position == 0){
                    return false;
                }else{
                    return true;
                }
            }
        };
        spinnerArrayAdapter.setDropDownViewResource(R.layout.ml_choose_city_item);
        districtSP.setAdapter(spinnerArrayAdapter);

        districtSP.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
            @Override
            public void onItemSelected(AdapterView<?> parent, View view, int position, long id) {
                String selectedItemText = (String) parent.getItemAtPosition(position);
                // Notify the selected item text

                selectedDistrict = selectedItemText;


                Log.d(TAG,"city onItemSelected Called");

                String getDistinctCity = "select distinct "+Merchant.COLUMN_MERCHANT_CITY+" from "+Merchant.TABLE_NAME+" where "+Merchant.COLUMN_MERCHANT_DIST+" ='"+selectedItemText+"'";
                cityList = Merchant.readDistinctaCity(getDistinctCity);

                if(cityList != null && cityList.size() > 0) {
                    cityList.add(0,Constants.CHOOSE_CITY_HEADER);
                    handler.sendEmptyMessage(LOAD_CITYLIST);
                }else{
                    cityList = new ArrayList();
                    cityList.add(0,Constants.CHOOSE_CITY_HEADER);

                }


            }

            @Override
            public void onNothingSelected(AdapterView<?> parent) {

            }
        });
    }




    void showCitySpinner(){

        // Initializing an ArrayAdapter
        ArrayAdapter<String> spinnerArrayAdapter = new ArrayAdapter<String>(
                this,R.layout.ml_choose_city_item,cityList){
            @Override
            public View getDropDownView(int position, View convertView,
                                        ViewGroup parent) {
                View view = super.getDropDownView(position, convertView, parent);
                TextView tv = (TextView) view;

                if(position == 0){

                    tv.setBackgroundColor(getResources().getColor(R.color.colorPrimary));
                    tv.setTextColor(getResources().getColor(R.color.c0));
                    tv.setTypeface(null, Typeface.BOLD);        // for Bold only

                }else{
                    tv.setBackgroundColor(getResources().getColor(R.color.c0));
                    tv.setTextColor(getResources().getColor(R.color.c2));
//                    tv.setTypeface(null, Typeface.);

                }

                return view;
            }

            @Override
            public boolean isEnabled(int position) {
                // TODO Auto-generated method stub

                if(position == 0){
                    return false;
                }else{
                    return true;
                }
            }
        };
        spinnerArrayAdapter.setDropDownViewResource(R.layout.ml_choose_city_item);
        citySP.setAdapter(spinnerArrayAdapter);

        citySP.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
            @Override
            public void onItemSelected(AdapterView<?> parent, View view, int position, long id) {
                String selectedItemText = (String) parent.getItemAtPosition(position);
                // Notify the selected item text


                selectedCity = selectedItemText;

                Log.d(TAG,"city onItemSelected Called");

                String entireQuery = "select * from "+Merchant.TABLE_NAME;
                String whereCondn = " where ";
                String cityCondn = Merchant.COLUMN_MERCHANT_CITY+" = '";
                String andCondn = " and ";
                String distCondn = Merchant.COLUMN_MERCHANT_DIST+" = '";


                if(!selectedCity.equals(Constants.CHOOSE_CITY_HEADER) || !selectedDistrict.equals(Constants.CHOOSE_DISTRICT_HEADER)){

                    entireQuery = entireQuery+whereCondn;


                    if(!selectedCity.equals(Constants.CHOOSE_CITY_HEADER)){
                        entireQuery = entireQuery+cityCondn+selectedCity+"'";
                    }

                    if(!selectedDistrict.equals(Constants.CHOOSE_DISTRICT_HEADER)){

                        if(!selectedCity.equals(Constants.CHOOSE_CITY_HEADER)){
                            entireQuery = entireQuery+andCondn;
                        }

                        entireQuery = entireQuery+distCondn+selectedDistrict+"'";
                    }
                }



//
//
//
//                if(!selectedItemText.equals(Constants.CHOOSE_CITY_HEADER)){
//                    whereCondn = "city = '"+selectedItemText+"'";
//                }else{
//                    whereCondn = ""
//                }
//
//                if(!selectedDistrict.equals(Constants.CHOOSE_DISTRICT_HEADER)){
//                    whereCondn = whereCondn+ " and district = '"+selectedDistrict+"'";
//                }
//                String readAllData = "select * from merchants"+ whereCondn;
//
                Log.d(TAG,"city Quesry = ===== :"+entireQuery);


                Message messgae = getListViewMessgae(entireQuery);

                listHandler.sendMessage(messgae);

            }

            @Override
            public void onNothingSelected(AdapterView<?> parent) {

            }
        });
    }



    class MerchantsListAdapter extends ArrayAdapter<Merchant> {

        private static final int TYPE_LIST_ITEM = 0;
        private static final int MAX_PLP_LIST_ITEM_TYPE_COUNT = 1;

        Activity context;

        private LayoutInflater factory;


        public MerchantsListAdapter(Context context, int resource, List<Merchant> objects) {

            super(context, resource, objects);

            factory = LayoutInflater.from(context);
        }



        public boolean isEnabled(int position) {
            return true;
        }


        public View getView(int position, View convertView, ViewGroup parent) {
            // TODO Auto-generated method stub

            ViewHolder holder = null;
            int type = getItemViewType(position);
            if (convertView == null) {
                holder = new ViewHolder();
                convertView = factory.inflate(R.layout.merchant_list_item, null);

                holder.merchantNameTxt = (TextView) convertView
                        .findViewById(R.id.merchantName_TV);
                holder.amountTxt = (TextView) convertView
                        .findViewById(R.id.merchantAmount_TV);
                holder.dateTxt = (TextView) convertView
                        .findViewById(R.id.merchants_list_item_date);

                convertView.setTag(holder);
            } else {
                holder = (ViewHolder) convertView.getTag();
            }

            Merchant merchant = this.getItem(position);


            String formatted = Utility.getFormattedCurrency(""+merchant.getTotalBalance());


            holder.merchantNameTxt.setText(merchant.getName());
            holder.amountTxt.setText(formatted);
            holder.dateTxt.setText(merchant.getCity()+", "+merchant.getDistrict());

            return convertView;
        }


    }


    private Handler listHandler = new Handler() {
        @Override
        public void handleMessage(Message msg) {

            String message = (String) msg.obj;

            if(message != null && message.length() > 0){
                Cursor cursor= Merchant.readDataCurser(message);

                if(ListAdapter == null) {

                    ListAdapter = new MerchantCurserAdapter(MerchantsListActivity.this, cursor);

                    merchantsListView.setAdapter(ListAdapter);

                }else {

                    ListAdapter.changeCursor(cursor);

                }

            }


        }
    };


    private Handler handler = new Handler() {
        @Override
        public void handleMessage(Message msg) {
            Log.d(TAG, "handler loadNext[" + msg.what + "]");

            switch (msg.what){

                case  LOAD_MERCHANTSLIST:{

//                    ListAdapter.clear();
//                    ListAdapter.addAll(merchantsVector);

                }
                    break;

                case  LOAD_CITYLIST:{

                    showCitySpinner();
                }
                break;

                case  LOAD_DISTRICTLIST:{

                    showDistrictSpinner();
                }
                break;

                case LOAD_MERCHANT_DETAIL:{
                    Intent gotoMerchantDetailActivity = new Intent(MerchantsListActivity.this,
                            MerchantDetailActivity.class);
                    gotoMerchantDetailActivity.putExtra("merchant", selectedMerchant);

                    startActivity(gotoMerchantDetailActivity);
                }
                    break;

            }
            super.handleMessage(msg);

        }
    };

    public static class ViewHolder {
        public TextView merchantNameTxt;
        public TextView amountTxt;
        public TextView dateTxt;

    }


    private Message getListViewMessgae(String query){

        Message messgae = new Message();

        messgae.obj = query;

        return messgae;


    }

    @Override
    protected int useResponseData(ParseResult result, String identifier) {


        MerchantParseResult merchantPArseResult = (MerchantParseResult)result;


        if(merchantPArseResult != null && merchantPArseResult.getData() != null && merchantPArseResult.getData().size() > 0) {


            Merchant.deleteAllMerchants(this.users.getUserId());


            List<Merchant> aMerchantsVector = merchantPArseResult.getData();



            for (int i = 0; i < aMerchantsVector.size(); i++) {

                Merchant merchant = aMerchantsVector.get(i);
                merchant.setUserId(this.users.getUserId());

            }

            Merchant.persistAllMerchants(aMerchantsVector);



            String getDistinctDistrict = "select distinct "+Merchant.COLUMN_MERCHANT_DIST+" from "+Merchant.TABLE_NAME;


            String getDistinctCity = "select distinct "+Merchant.COLUMN_MERCHANT_CITY+" from "+Merchant.TABLE_NAME;

            districtList = Merchant.readDistinctaCity(getDistinctDistrict);
            districtList.add(0,Constants.CHOOSE_DISTRICT_HEADER);


            cityList = Merchant.readDistinctaCity(getDistinctCity);

            cityList.add(0,Constants.CHOOSE_CITY_HEADER);

            if(districtList != null && districtList.size() >0) {
                handler.sendEmptyMessage(LOAD_DISTRICTLIST);
            }

            if(cityList != null && cityList.size() >0) {
                handler.sendEmptyMessage(LOAD_CITYLIST);
            }


            String readAllData = "select * from "+Merchant.TABLE_NAME;

            Vector<Merchant> aMerchantVector= Merchant.readData(readAllData);


            stopLoading();


            if(aMerchantVector != null && aMerchantVector.size() > 0) {


                Message message = getListViewMessgae(readAllData);

                listHandler.sendMessage(message);


                return -1;

            }else{
                Log.e(TAG,"handler called merchantsVector null");

            }

        }

        stopLoading();



        return -1;
    }

    @Override
    protected void handleTransportException(Exception ex) {

    }
}
