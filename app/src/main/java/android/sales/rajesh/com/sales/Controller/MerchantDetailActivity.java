package android.sales.rajesh.com.sales.Controller;

import android.app.Activity;
import android.content.Context;
import android.content.Intent;
import android.location.Location;
import android.os.Bundle;
import android.os.Handler;
import android.os.Message;
import android.sales.rajesh.com.sales.Core.CoreActivity;
import android.sales.rajesh.com.sales.Model.Bill;
import android.sales.rajesh.com.sales.Model.Merchant;
import android.sales.rajesh.com.sales.Model.UserLocation;
import android.sales.rajesh.com.sales.Model.UserLogin;
import android.sales.rajesh.com.sales.Utils.Constants;
import android.sales.rajesh.com.sales.Utils.LocationHelper;
import android.sales.rajesh.com.sales.Utils.SalesProtocol;
import android.sales.rajesh.com.sales.Utils.Utility;
import android.sales.rajesh.com.salesapp.R;
import android.support.v7.widget.Toolbar;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.Button;
import android.widget.LinearLayout;
import android.widget.ListView;
import android.widget.TextView;

import java.util.ArrayList;
import java.util.List;

/**
 * Created by Karthik on 2/1/17.
 */

public class MerchantDetailActivity extends CoreActivity implements View.OnClickListener{

    private static final String TAG = MerchantDetailActivity.class.getSimpleName();
    protected static final int LOAD_MERCHANTS_DETAIL_ITEM_LIST = 0;
    protected static final int LOAD_PAYMENT_DETAIL = 1;

    TextView merchantNameTV;
    TextView merchantBalanceTagTV;
    TextView merchantBalanceValueTV;
    TextView merchantaddressTV;


    Merchant selectedMerchant;

    List<Bill> billList;

    List<Bill> paymentHistoryList;

    Button homeBt;
    Button visitedBt ,paymentHistoryBt;



    ListView billsListView;

    int selectedPaymentType;

    MerchantsDetailItemListAdapter ListAdapter;

    @Override
    protected void onCreate(Bundle savedInstanceState) {

        super.onCreate(savedInstanceState);
        setContentView(R.layout.merchant_detail);

        Toolbar toolbar = (Toolbar) findViewById(R.id.app_toolbar);
        setSupportActionBar(toolbar);
        TextView titleView = (TextView) toolbar.findViewById(R.id.toolbar_title);
        homeBt = (Button) toolbar.findViewById(R.id.toolbar_home_BT);
        homeBt.setOnClickListener(this);

        View rightBt = (View) toolbar.findViewById(R.id.toolbar_right_action_Bt);
        rightBt.setVisibility(View.GONE);

        titleView.setText(R.string.customer_detail_title);


        // enables the activity icon as a 'home' button. required if "android:targetSdkVersion" > 14


        billsListView = (ListView) findViewById(R.id.merchant_detail_LV);
        visitedBt = (Button) findViewById(R.id.md_visited_BT) ;
        paymentHistoryBt = (Button)findViewById(R.id.md_payment_hitory_BT);
        billList = new ArrayList<>();

        Bundle extras = getIntent().getExtras();

        selectedMerchant = (Merchant) getIntent().getSerializableExtra("merchant");


        merchantNameTV = (TextView) findViewById(R.id.merchant_detail_name_TV);
        merchantBalanceTagTV = (TextView) findViewById(R.id.merchant_detail_balance_tag_TV);
        merchantBalanceValueTV = (TextView)findViewById(R.id.merchant_detail_balance_value_TV) ;
        merchantaddressTV = (TextView) findViewById(R.id.merchant_detail_address_TV);

        merchantNameTV.setText(selectedMerchant.getName());

        merchantaddressTV.setText(selectedMerchant.getCity()+ ", "+selectedMerchant.getDistrict());

        String formatted = Utility.getFormattedCurrency(""+selectedMerchant.getTotalBalance());

        merchantBalanceValueTV.setText(formatted);

        merchantBalanceTagTV.setText("Balance Amount: ");


        Log.d(TAG,"selected name : "+selectedMerchant.getName());




        ListAdapter = new MerchantsDetailItemListAdapter(this);


//        billsListView.setAdapter(ListAdapter);



//        ActionBar actionBarr = getSupportActionBar();
//
//        actionBarr.setDisplayShowHomeEnabled(true);
//
//        // enables the activity icon as a 'home' button. required if "android:targetSdkVersion" > 14
//        getActionBar().setHomeButtonEnabled(true);
//
//        actionBarr.setHomeButtonEnabled(true);

        visitedBt.setOnClickListener(this);
        paymentHistoryBt.setOnClickListener(this);


    }

    @Override
    protected void onResume() {
        super.onResume();

        String billListquery = "select * from "+Bill.TABLE_NAME+" where "+Bill.COLUMN_BILL_MERCHANT_ID+" = '"+selectedMerchant.getId()+"'";

        this.billList = Bill.readData(billListquery);

        selectedMerchant.setBillList(this.billList);

//        this.paymentHistoryList = this.billList;


        billsListView.setAdapter(ListAdapter);

    }

    @Override
    public void onClick(View v) {

        if(v == homeBt){
            finish();
        }

        if(v == visitedBt){

            LocationHelper locationHelper = new LocationHelper();

            locationHelper.getLocation(this, new LocationHelper.LocationResult() {
                @Override
                public void gotLocation(Location location) {

                    Location currentLocation = location;

                    UserLocation loginLocation = new UserLocation();
                    UserLogin userLogin= UserLogin.getLastLoggedInUser();
                    loginLocation.setUserId(userLogin.getUserId());
                    loginLocation.setLatitude(currentLocation.getLatitude()+"");
                    loginLocation.setLongitude(currentLocation.getLongitude()+"");
                    loginLocation.setRecordedAt(Utility.getDateAndTime());
                    loginLocation.setDisplayName(UserLocation.USER_LOCATION_DISPLAY_NAME_VISITED);
                    loginLocation.setMerchantId(selectedMerchant.getId());

                    loginLocation.persistData();

                    showAlertMessage("YDKPro","You have been visited Successfully.", Constants.ALERT_WITH_OK_BUTTON);


                }
            });





        }

        if(v == paymentHistoryBt){

            Intent gotoMerchantPaymentHistory = new Intent(MerchantDetailActivity.this,
                    PaymentHistoryActivity.class);

            gotoMerchantPaymentHistory.putExtra("merchant", selectedMerchant);


            startActivity(gotoMerchantPaymentHistory);
        }
    }



    class MerchantsDetailItemListAdapter extends BaseAdapter {
        private static final int TYPE_PENDING_LIST_ITEM = 0;
        private static final int TYPE_LIST_ITEM_HEADER = 1;
        private static final int TYPE_PAYMENT_HISTORY_LIST_ITEM = 2;



        Activity context;

        private LayoutInflater factory;

        public MerchantsDetailItemListAdapter(Activity context) {
            this.context = context;
            factory = (LayoutInflater) getSystemService(Context.LAYOUT_INFLATER_SERVICE);

        }


        public int getCount() {

            int listSize = 0;


            if (billList != null) {

                listSize = billList.size() +1;
            }



            return listSize;
        }

        public boolean isEnabled(int position) {
            return true;
        }

        public int getViewTypeCount() {

            return 2;
        }

        public int getItemViewType(int position) {

            if(position == 0){
                return TYPE_LIST_ITEM_HEADER;
            }else if(position == billList.size()+1){
                return TYPE_LIST_ITEM_HEADER;
            }

            return TYPE_PENDING_LIST_ITEM;
        }

        public Object getItem(int position) {
            // TODO Auto-generated method stub
            return null;
        }

        public long getItemId(int position) {
            // TODO Auto-generated method stub
            return 0;
        }

        public View getView(int position, View convertView, ViewGroup parent) {
            // TODO Auto-generated method stub

            Log.d(TAG, "getView : " + position);

            int type = getItemViewType(position);


            switch (type) {

                case TYPE_PENDING_LIST_ITEM: {
                    Log.d(TAG, "TYPE_PENDING_LIST_ITEM : " + position);

                    ViewHolder holder = null;
                    if (convertView == null) {
                        holder = new ViewHolder();
                        convertView = factory.inflate(R.layout.merchant_detail_item, null);

                        holder.collectionDate = (TextView) convertView
                                .findViewById(R.id.md_item_total_balance_amt_date_TV);
                        holder.totalCollectionAmount = (TextView) convertView
                                .findViewById(R.id.md_item_total_balance_amt_TV);
                        holder.pendingCollectionAmount = (TextView) convertView
                                .findViewById(R.id.md_item_pending_TV);
                        holder.type = (TextView) convertView
                                .findViewById(R.id.md_item_type_TV);
                        holder.billItemLL = (LinearLayout) convertView.findViewById(R.id.billItemLL);
                        convertView.setTag(holder);
                    } else {
                        holder = (ViewHolder) convertView.getTag();
                    }

                    Bill bill = billList.get(position-1);

                    String bAmountFormatted = Utility.getFormattedCurrency(""+bill.getBillingAmount());

                    String balanceAmountFormatted = Utility.getFormattedCurrency(""+bill.getBillingBalance());

                    if(holder != null) {
                        holder.collectionDate.setText(bill.getDate());
                        holder.totalCollectionAmount.setText(bAmountFormatted);
                        holder.type.setText(bill.getcType());
                        holder.pendingCollectionAmount.setText(balanceAmountFormatted);
                        if(bill.getBc() == 1) {
                            holder.billItemLL.setBackgroundColor(getResources().getColor(R.color.c18));
                        }else {
                            holder.billItemLL.setBackgroundColor(getResources().getColor(R.color.c0));

                        }
                    }
                }
                break;
                case TYPE_LIST_ITEM_HEADER:{
                    Log.d(TAG, "TYPE_LIST_ITEM_HEADER : " + position);

                    HeaderViewHolder holder = null;
                    if (convertView == null) {
                        holder = new HeaderViewHolder();
                        convertView = factory.inflate(R.layout.merchant_detail_item_header, null);

                        holder.headerTitle = (TextView) convertView
                                .findViewById(R.id.md_list_item_header_TV);

                        convertView.setTag(holder);


                    } else {
                        holder = (HeaderViewHolder) convertView.getTag();
                    }

                    if(holder != null) {
                        if(position == 0 ){
                            holder.headerTitle.setText("PENDING BILLS");

                        }

                    }

                }
                break;
//                case TYPE_PAYMENT_HISTORY_LIST_ITEM:{
//
//                    HistoryViewHolder holder = null;
//                    if (convertView == null) {
//                        holder = new HistoryViewHolder();
//
//                        convertView = factory.inflate(R.layout.merchant_payment_history_item, null);
//
//                        holder.receiptNoTV = (TextView) convertView
//                                .findViewById(R.id.mph_receipt_NO_TV);
//                        holder.dateTV = (TextView) convertView
//                                .findViewById(R.id.mph_date_TV);
//                        holder.amountTv = (TextView) convertView
//                                .findViewById(R.id.mph_amount_TV);
//
//                        convertView.setTag(holder);
//                    } else {
//                        holder = (HistoryViewHolder) convertView.getTag();
//                    }
//
//                    if(paymentHistoryList != null && paymentHistoryList.size() > 0){
//
//
//                        holder.receiptNoTV.setVisibility(View.VISIBLE);
//                        holder.amountTv.setVisibility(View.VISIBLE);
//
//                        holder.dateTV.setVisibility(View.VISIBLE);
//
////                        String bAmountFormatted = Utility.getFormattedCurrency(bill.getBillingAmount());
////
////                        String balanceAmountFormatted = Utility.getFormattedCurrency(bill.getBillingBalance());
////
////                        if(holder != null){
////                            holder.collectionDate.setText(bill.getDate());
////                            holder.totalCollectionAmount.setText(bAmountFormatted);
////                            holder.type.setText(bill.getcType());
////                            holder.pendingCollectionAmount.setText(balanceAmountFormatted);
////                        }
////
//                    }else{
//
//                        holder.receiptNoTV.setVisibility(View.GONE);
//                        holder.amountTv.setVisibility(View.GONE);
//
//                        holder.dateTV.setVisibility(View.VISIBLE);
//
//                        holder.dateTV.setTextColor(getResources().getColor(R.color.c4));
//
//                        holder.dateTV.setText("No History Found");
//
//
//                    }
//
//                }
//                break;
                default:
                    break;
            }


            return convertView;


        }
    }

    private Handler handler = new Handler() {
        @Override
        public void handleMessage(Message msg) {
            Log.d(TAG, "handler loadNext[" + msg.what + "]");

            switch (msg.what){

                case  LOAD_MERCHANTS_DETAIL_ITEM_LIST:{

                    ListAdapter.notifyDataSetChanged();

                }
                break;

                case LOAD_PAYMENT_DETAIL:{

                    Intent gotoMerchantPaymentActivity = new Intent(MerchantDetailActivity.this,
                            MerchantPaymentActivity.class);
                    gotoMerchantPaymentActivity.putExtra("merchant", selectedMerchant);
                    gotoMerchantPaymentActivity.putExtra("payment_type", selectedPaymentType);


                    startActivity(gotoMerchantPaymentActivity);

                }
                break;

            }
            super.handleMessage(msg);

        }
    };

    public static class ViewHolder {
        public LinearLayout billItemLL;
        public TextView collectionDate;
        public TextView totalCollectionAmount;
        public TextView type;
        public TextView pendingCollectionAmount;


    }

    public static class HeaderViewHolder {
        public TextView headerTitle;

    }

    public static class HistoryViewHolder {
        public TextView receiptNoTV;
        public TextView dateTV;
        public TextView amountTv;


    }
    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        // Inflate the menu; this adds items to the action bar if it is present.
        getMenuInflater().inflate(R.menu.md_menu, menu);
        return true;
    }
    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        // Handle action bar item clicks here. The action bar will
        // automatically handle clicks on the Home/Up button, so long
        // as you specify a parent activity in AndroidManifest.xml.
        int id = item.getItemId();

        //noinspection SimplifiableIfStatement


        if(id == R.id.menu_payment_A){

            handler.sendEmptyMessage(LOAD_PAYMENT_DETAIL);


            selectedPaymentType = SalesProtocol.PAYMENT_TYPE_A;

            return true;
        }

        if(id == R.id.menu_payment_E){

            selectedPaymentType = SalesProtocol.PAYMENT_TYPE_E;

            handler.sendEmptyMessage(LOAD_PAYMENT_DETAIL);

            return true;
        }
        return super.onOptionsItemSelected(item);
    }


    @Override
    public void loadNext(int code) {

    }

    @Override
    public void loadPrev() {

        finish();

    }
}
