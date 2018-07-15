package android.sales.rajesh.com.sales.Controller;

import android.app.Activity;
import android.content.Context;
import android.os.Bundle;
import android.sales.rajesh.com.sales.Core.CoreActivity;
import android.sales.rajesh.com.sales.Model.Merchant;
import android.sales.rajesh.com.salesapp.R;
import android.support.v7.widget.Toolbar;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.MenuItem;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.Button;
import android.widget.ListView;
import android.widget.TextView;

import java.util.List;

/**
 * Created by Karthik on 3/10/17.
 */

public class PaymentHistoryActivity extends CoreActivity{

    private static final String TAG = PaymentHistoryActivity.class.getSimpleName();




    List paymentHistoryList;

    ListView paymentHistoryLV;

    Merchant selectedMerchant;

    MerchantsPaymentHistoryAdapter historyAdapter;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        setContentView(R.layout.merchant_payment_history_activity);

        Toolbar toolbar = (Toolbar) findViewById(R.id.app_toolbar);
        setSupportActionBar(toolbar);
        TextView titleView = (TextView) toolbar.findViewById(R.id.toolbar_title);
        Button homeBt = (Button) toolbar.findViewById(R.id.toolbar_home_BT);
        View rightBt = (View) toolbar.findViewById(R.id.toolbar_right_action_Bt);
        rightBt.setVisibility(View.GONE);
        getSupportActionBar().setDisplayHomeAsUpEnabled(true);

        homeBt.setVisibility(View.GONE);



        Bundle extras = getIntent().getExtras();

        selectedMerchant = (Merchant) getIntent().getSerializableExtra("merchant");

        titleView.setText(selectedMerchant.getName());

        paymentHistoryLV = (ListView)findViewById(R.id.merchant_payment_history_LV);


        historyAdapter = new MerchantsPaymentHistoryAdapter(this);

        paymentHistoryLV.setAdapter(historyAdapter);

        historyAdapter.notifyDataSetChanged();


    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        if(item.getItemId()==android.R.id.home){
            finish();
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


    class MerchantsPaymentHistoryAdapter extends BaseAdapter {
        private static final int TYPE_PENDING_LIST_ITEM = 0;


        Activity context;

        private LayoutInflater factory;

        public MerchantsPaymentHistoryAdapter(Activity context) {
            this.context = context;
            factory = (LayoutInflater) getSystemService(Context.LAYOUT_INFLATER_SERVICE);

        }


        public int getCount() {

            int listSize = 3;


            if (paymentHistoryList != null) {

                listSize = paymentHistoryList.size();
            }



            return listSize;
        }

        public boolean isEnabled(int position) {
            return true;
        }

        public int getViewTypeCount() {

            return 1;
        }

        public int getItemViewType(int position) {

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

                    HistoryViewHolder holder = null;
                    if (convertView == null) {
                        holder = new HistoryViewHolder();
                        convertView = factory.inflate(R.layout.merchant_payment_history_item, null);

                        holder.amountTv = (TextView) convertView
                                .findViewById(R.id.mph_amount_TV);
                        holder.receiptNoTV = (TextView) convertView
                                .findViewById(R.id.mph_receipt_NO_TV);
                        holder.dateTV = (TextView) convertView
                                .findViewById(R.id.mph_date_TV);
                        convertView.setTag(holder);
                    } else {
                        holder = (HistoryViewHolder) convertView.getTag();
                    }


                    holder.receiptNoTV.setText("1232132"+position);
                    holder.dateTV.setText("10/12/200"+position);
                    holder.amountTv.setText("Rs.10,00"+position);



                }
                break;
                default:
                    break;
            }


            return convertView;


        }

    }


    public static class HistoryViewHolder {
        public TextView receiptNoTV;
        public TextView dateTV;
        public TextView amountTv;


    }
}
