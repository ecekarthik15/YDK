package android.sales.rajesh.com.sales.Controller;

import android.app.Activity;
import android.bluetooth.BluetoothAdapter;
import android.bluetooth.BluetoothDevice;
import android.bluetooth.BluetoothSocket;
import android.content.Context;
import android.content.Intent;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.graphics.Typeface;
import android.os.Bundle;
import android.os.Handler;
import android.os.Message;
import android.sales.rajesh.com.sales.Core.CoreActivity;
import android.sales.rajesh.com.sales.Model.Merchant;
import android.sales.rajesh.com.sales.Utils.Constants;
import android.sales.rajesh.com.sales.Utils.PrinterCommandTranslator;
import android.sales.rajesh.com.sales.Utils.Utility;
import android.sales.rajesh.com.salesapp.R;
import android.support.v7.widget.Toolbar;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.BaseAdapter;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Spinner;
import android.widget.TextView;
import android.widget.Toast;

import java.io.ByteArrayOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.io.OutputStream;
import java.lang.reflect.Method;
import java.text.NumberFormat;
import java.util.ArrayList;
import java.util.List;
import java.util.Locale;
import java.util.Set;
import java.util.UUID;
import java.util.zip.Inflater;

/**
 * Created by Karthik on 2/17/17.
 */

public class PaymentReceiptActivity extends CoreActivity implements View.OnClickListener {

    private static final String TAG = PaymentReceiptActivity.class.getSimpleName();

    protected static final int LOAD_BLUETOOTH_DEVICE_LIST = 0;
    protected static final int LOAD_RECEIPT_DETAIL = 1;

    // will show the statuses like bluetooth open, close or data sent
//    TextView myLabel;

    // will enable user to enter any text to be printed
    EditText myTextbox;


    TextView merchantNameTV;
    TextView merchantTotalPaidTagTV;
    TextView merchantTotalPaidValueTV;
    TextView merchantReceiptNoTagTV;
    TextView merchantReceiptNoValueTV;
    TextView merchantReceiptDateTagTV;
    TextView merchantReceiptDateValueTV;




    // android built in classes for bluetooth operations
    BluetoothAdapter mBluetoothAdapter;
    BluetoothSocket mmSocket;
    BluetoothDevice mmDevice;

    // needed for communication to bluetooth device / network
    OutputStream mmOutputStream;
    InputStream mmInputStream;
    Thread workerThread;

    int readBufferPosition;

    Button printBT,homeBt;

    Spinner deviceSpinner;


    List<BluetoothDevice> deviceList;

    DeviceListAdapter deviceListAdapter;

    private BluetoothAdapter bluetoothAdapter;
    private BluetoothSocket bluetoothSocket;
    private BluetoothDevice bluetoothDevice;
    private OutputStream outputStream;
    private InputStream inputStream;
    private Thread thread;
    private byte[] readBuffer;
    private int readBufferPos;
    private boolean stopWorker;


    Merchant selectedMerchant;

    String totalPaid;

    String paymentDate;

    int paymentType;





    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.payment_receipt_activity);

        Toolbar toolbar = (Toolbar) findViewById(R.id.app_toolbar);
        setSupportActionBar(toolbar);
        TextView titleView = (TextView) toolbar.findViewById(R.id.toolbar_title);
        homeBt = (Button) toolbar.findViewById(R.id.toolbar_home_BT);

        homeBt.setOnClickListener(this);
        View rightBt = (View) toolbar.findViewById(R.id.toolbar_right_action_Bt);
        rightBt.setVisibility(View.GONE);

        titleView.setText(R.string.customer_detail_title);

        merchantNameTV = (TextView) findViewById(R.id.pr_customer_name_value_TV);
        merchantTotalPaidTagTV = (TextView) findViewById(R.id.pr_total_bill_tag_TV);
        merchantTotalPaidValueTV = (TextView)findViewById(R.id.pr_total_bill_value_TV) ;
        merchantReceiptNoTagTV = (TextView)findViewById(R.id.pr_pending_receipt_no_tag_TV) ;
        merchantReceiptNoValueTV = (TextView)findViewById(R.id.pr_pending_receipt_no_value_TV) ;
        merchantReceiptDateTagTV = (TextView) findViewById(R.id.pr_date_tag_TV);
        merchantReceiptDateValueTV = (TextView) findViewById(R.id.pr_date_value_TV);
        deviceSpinner = (Spinner)findViewById(R.id.pr_choose_bluetooth_device_spinner) ;

        deviceList = new ArrayList<>();

        deviceListAdapter = new DeviceListAdapter(this,
                R.layout.ml_choose_city_item, R.id.ml_choose_city_TV, deviceList);

        deviceSpinner.setAdapter(deviceListAdapter);

        handler.sendEmptyMessage(LOAD_BLUETOOTH_DEVICE_LIST);

        printBT = (Button)findViewById(R.id.pr_print_BT) ;
        printBT.setOnClickListener(this);


        Bundle extras = getIntent().getExtras();

        selectedMerchant = (Merchant) getIntent().getSerializableExtra("merchant");

        Long paymentDateInMSecs = (Long) getIntent().getSerializableExtra("payment_date");

        paymentDate = Utility.getDate(paymentDateInMSecs,"dd-MM-yyyy");
        paymentType = (int) getIntent().getSerializableExtra("payment_type");

        double totalPaidinDouble = (double) getIntent().getSerializableExtra("total_payment");


        merchantNameTV.setText(selectedMerchant.getName());

        totalPaid = NumberFormat.getCurrencyInstance(new Locale("en", "in")).format((totalPaidinDouble));

        totalPaid = totalPaid.replace(" ","");

        paymentDate = paymentDate.replace("/","-");

        merchantTotalPaidValueTV.setText(totalPaid);



        merchantReceiptDateValueTV.setText(paymentDate);




        initDeviceSpinner();
        findBT();
    }

    @Override
    protected void onResume() {
        super.onResume();

//        try {
//            openBT();
//
//        }catch(Exception e) {
//            e.printStackTrace();
//        }

    }

    @Override
    protected void onPause() {
        super.onPause();

        try {
            closeBluetooth();
        }catch(Exception e) {
            e.printStackTrace();
        }


    }



    private Handler handler = new Handler() {
        @Override
        public void handleMessage(Message msg) {
            Log.d(TAG, "handler loadNext[" + msg.what + "]");

            switch (msg.what){

                case  LOAD_BLUETOOTH_DEVICE_LIST:{

                    deviceListAdapter.notifyDataSetChanged();

                }
                break;

                case LOAD_RECEIPT_DETAIL:{


                }
                break;

            }
            super.handleMessage(msg);

        }
    };


    void initDeviceSpinner(){

        deviceSpinner.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
            @Override
            public void onItemSelected(AdapterView<?> parent, View view, int position, long id) {

                if(position > 0) {

                    BluetoothDevice device = deviceList.get(position - 1);

                    mmDevice = device;

                    bluetoothDevice = device;

                    Log.d(TAG,"deivice NAme : "+device.getName());

                    try {
                        openBluetooth();
                    } catch (IOException e) {
                        Toast.makeText(PaymentReceiptActivity.this, "" + e, Toast.LENGTH_SHORT).show();
                    }
                }
            }

            @Override
            public void onNothingSelected(AdapterView<?> parent) {

            }
        });
    }



    // this will find a bluetooth printer device
    void findBT() {

        try {
            mBluetoothAdapter = BluetoothAdapter.getDefaultAdapter();

            if(mBluetoothAdapter == null) {
                showMessage("Bluetooth Devices not Available");
            }

            if(!mBluetoothAdapter.isEnabled()) {
                Intent enableBluetooth = new Intent(BluetoothAdapter.ACTION_REQUEST_ENABLE);
                startActivityForResult(enableBluetooth, 0);
            }
            deviceList = new ArrayList<>();


            Set<BluetoothDevice> pairedDevices = mBluetoothAdapter.getBondedDevices();

            if(pairedDevices.size() > 0) {
                for (BluetoothDevice device : pairedDevices) {

                    deviceList.add(device);

//                    if (device.getName().equals("RPP300")) {
//                        mmDevice = device;
//                        break;
//                    }
                }

                deviceListAdapter = new DeviceListAdapter(this,
                        R.layout.ml_choose_city_item, R.id.ml_choose_city_TV, deviceList);

                deviceSpinner.setAdapter(deviceListAdapter);



                handler.sendEmptyMessage(LOAD_BLUETOOTH_DEVICE_LIST);
            }

        }catch(Exception e){
            e.printStackTrace();
        }
    }

    @Override
    public void loadNext(int code) {

    }

    @Override
    public void loadPrev() {

        finish();

    }

    @Override
    public void onClick(View v) {

        if (v == homeBt) {
            setResult(RESULT_CODE_FINISH_ACTIVITY);
            finish();
        }

        if(v == printBT){

            if(bluetoothDevice != null) {

                try {
                    printReceipt();
                    finish();
                } catch (IOException e) {
                    Toast.makeText(PaymentReceiptActivity.this, "" + e, Toast.LENGTH_SHORT).show();
                }
            }else{

                showAlertMessage("Choose Printer","Please choose printer", Constants.ALERT_WITH_OK_BUTTON);
            }
        }

    }



    class DeviceListAdapter extends ArrayAdapter {
        protected static final int LOAD_BLUETOOTH_DEVICE_CHOOSE_HEADER_TYPE = 1;

        protected static final int LOAD_BLUETOOTH_DEVICE_NAME_TYPE = 2;


        LayoutInflater flater;

        public DeviceListAdapter(Context context, int resouceId, int textviewId, List<BluetoothDevice> list) {

            super(context, resouceId, list);

            flater = LayoutInflater.from(context);
        }
        public int getCount() {

            if(deviceList != null && deviceList.size() > 0){
                return deviceList.size()+1;
            }else{
                return 1;
            }
        }

        public int getViewTypeCount() {

            if(deviceList != null && deviceList.size() > 0){
                return 2;
            }else{
                return 1;
            }

        }

        public int getItemViewType(int position) {

            if(position == 0){
                return LOAD_BLUETOOTH_DEVICE_CHOOSE_HEADER_TYPE;
            }else {
                return LOAD_BLUETOOTH_DEVICE_NAME_TYPE;
            }
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


        public View getView(int position, View convertView, ViewGroup parent) {


            Log.d(TAG,"getView position : "+position);

            ViewHolder holder = null;

            int type = getItemViewType(position);



            if(convertView == null){
                convertView = flater.inflate(R.layout.ml_choose_city_item,parent, false);
                holder = new ViewHolder();

                holder.deviceNameTV = (TextView) convertView.findViewById(R.id.ml_choose_city_TV);

                convertView.setTag(holder);


            }else{
                holder = (ViewHolder) convertView.getTag();

            }



            switch (type){
                case LOAD_BLUETOOTH_DEVICE_CHOOSE_HEADER_TYPE:
                    holder.deviceNameTV.setText(Constants.CHOOSE_PRINTER_HEADER);

                    break;
                case LOAD_BLUETOOTH_DEVICE_NAME_TYPE:
                    BluetoothDevice device = (BluetoothDevice)getItem(position-1);

                    holder.deviceNameTV.setText(device.getName());

                    break;

                default:
                    break;
            }

            return convertView;
        }

        @Override
        public View getDropDownView(int position, View convertView, ViewGroup parent) {

            ViewHolder holder = null;

            int type = getItemViewType(position);
            Log.d(TAG,"getDropDownView position : "+position);


            if(convertView == null){
                holder = new ViewHolder();
                convertView = flater.inflate(R.layout.ml_choose_city_item,parent, false);
                holder.deviceNameTV = (TextView) convertView.findViewById(R.id.ml_choose_city_TV);

                convertView.setTag(holder);

            }else{
                holder = (ViewHolder) convertView.getTag();

            }


            switch (type){
                case LOAD_BLUETOOTH_DEVICE_CHOOSE_HEADER_TYPE:
                    holder.deviceNameTV.setText(Constants.CHOOSE_PRINTER_HEADER);

                    break;
                case LOAD_BLUETOOTH_DEVICE_NAME_TYPE:
                    BluetoothDevice device = deviceList.get(position-1);

                    holder.deviceNameTV.setText(device.getName());

                    break;

                default:
                    break;
            }

            return convertView;
        }

    }

    public static class ViewHolder {
        public TextView deviceNameTV;

    }


    public void openBluetooth() throws IOException {
        try {
            UUID uuid = UUID.fromString("00001101-0000-1000-8000-00805f9b34fb");
            bluetoothSocket = bluetoothDevice.createRfcommSocketToServiceRecord(uuid);
            bluetoothSocket.connect();
            outputStream = bluetoothSocket.getOutputStream();
            inputStream = bluetoothSocket.getInputStream();

            listenFromData();
        } catch (Exception ex) {
            Toast.makeText(getApplicationContext(), "Open Bluetooth" + ex, Toast.LENGTH_SHORT).show();
        }
    }

    private void listenFromData() {
        try {
            final Handler handler = new Handler();
            final byte delimiter = 10;
            stopWorker = false;
            readBufferPos = 0;
            readBuffer = new byte[1024];

            thread = new Thread(new Runnable() {
                @Override
                public void run() {
                    while (!Thread.currentThread().isInterrupted() && !stopWorker) {
                        try {
                            int bytesAvailable = inputStream.available();
                            if (bytesAvailable > 0) {
                                byte[] packetBytes = new byte[bytesAvailable];
                                inputStream.read(packetBytes);
                                for (int i = 0; i < bytesAvailable; i++) {
                                    byte b = packetBytes[i];
                                    if (b == delimiter) {
                                        byte[] encodedBytes = new byte[readBufferPos];
                                        System.arraycopy(readBuffer, 0, encodedBytes, 0, encodedBytes.length);
                                        final String data = new String(encodedBytes, "US-ASCII");
                                        readBufferPos = 0;

                                        handler.post(new Runnable() {
                                            @Override
                                            public void run() {
                                                Toast.makeText(getApplicationContext(), "Data" + data, Toast.LENGTH_SHORT).show();
                                            }
                                        });
                                    } else {
                                        readBuffer[readBufferPos++] = b;
                                    }
                                }
                            }
                        } catch (IOException ex) {
                            stopWorker = true;
                        }
                    }
                }
            });
            thread.start();
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    private void printReceipt() throws IOException {
        PrinterCommandTranslator translator = new PrinterCommandTranslator();
        String address = selectedMerchant.getCity()+", "+selectedMerchant.getDistrict();
        print(translator.toNormalRepeatTillEnd('-'));
        print(translator.toNormalCenterAll(selectedMerchant.getName()));
        print(translator.toNormalRepeatTillEnd('-'));
        print(translator.toNormalCenter(address));
        print(translator.toNormalCenter("   "));

        totalPaid = totalPaid.replace(" ","");

        print(translator.toNormalLeft("Receipt Number    23239393939"));
        print(translator.toNormalLeft("Total paid        "+totalPaid ));
        print(translator.toNormalLeft("Payment Date      "+ paymentDate));

        print(translator.toNormalCenter("   "));
        print(translator.toNormalCenter("   "));
        print(translator.toNormalCenter("   "));
        print(translator.toNormalCenter("   "));


    }

    void closeBluetooth() throws IOException {
        try {
            stopWorker = true;
            outputStream.close();
            inputStream.close();
            bluetoothSocket.close();
//            actionBarStatus(4);
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    private void print(byte[] cmd) {
        try {
            outputStream.write(cmd);
            outputStream.flush();
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    private void convertImagetoByte() {
        Bitmap bitmap = BitmapFactory.decodeResource(getResources(), R.drawable.ic_launcher);
        byte[] bmp_example = {(byte) 0x1B, (byte) 0x58, (byte) 0x31, (byte) 0x24, (byte) 0x2D, (byte) 0x00, (byte) 0x00, (byte) 0x00, (byte) 0x00, (byte) 0x00, (byte) 0x00, (byte) 0x00, (byte) 0x00, (byte) 0x00, (byte) 0x00, (byte) 0x00, (byte) 0x00, (byte) 0x00, (byte) 0x00, (byte) 0x00, (byte) 0x00, (byte) 0x00, (byte) 0x00, (byte) 0x00, (byte) 0x00, (byte) 0x00, (byte) 0x00, (byte) 0x00, (byte) 0x00, (byte) 0x00, (byte) 0x00, (byte) 0x00, (byte) 0x00, (byte) 0x00, (byte) 0x00, (byte) 0x00, (byte) 0x00, (byte) 0x00, (byte) 0x00, (byte) 0x00, (byte) 0x00, (byte) 0x00, (byte) 0x00, (byte) 0x00, (byte) 0x00, (byte) 0x00, (byte) 0x00, (byte) 0x00, (byte) 0x00, (byte) 0x00, (byte) 0x00, (byte) 0x00, (byte) 0x00, (byte) 0x00, (byte) 0x00, (byte) 0x00, (byte) 0x00, (byte) 0x00, (byte) 0x00, (byte) 0x00, (byte) 0x00, (byte) 0x00, (byte) 0x00, (byte) 0x00, (byte) 0x00, (byte) 0x00, (byte) 0x00, (byte) 0x00, (byte) 0x00, (byte) 0x00, (byte) 0x00, (byte) 0x00, (byte) 0x00, (byte) 0x00, (byte) 0x00, (byte) 0x00, (byte) 0x00, (byte) 0x00, (byte) 0x00, (byte) 0x00, (byte) 0x00, (byte) 0x00, (byte) 0x00, (byte) 0x00, (byte) 0x00, (byte) 0x00, (byte) 0x00, (byte) 0x00, (byte) 0x00, (byte) 0x00, (byte) 0x00, (byte) 0x00, (byte) 0x00, (byte) 0x00, (byte) 0x00, (byte) 0x00, (byte) 0x00, (byte) 0x00, (byte) 0x00, (byte) 0x00, (byte) 0x00, (byte) 0x00, (byte) 0x00, (byte) 0x00, (byte) 0x00, (byte) 0x00, (byte) 0x00, (byte) 0x00, (byte) 0x00, (byte) 0x00, (byte) 0x00, (byte) 0x00, (byte) 0x00, (byte) 0x00, (byte) 0x00, (byte) 0x00, (byte) 0x00, (byte) 0x00, (byte) 0x00, (byte) 0x00, (byte) 0x00, (byte) 0x00, (byte) 0x00, (byte) 0x00, (byte) 0x00, (byte) 0x00, (byte) 0x00, (byte) 0x00, (byte) 0x00, (byte) 0x00, (byte) 0x00, (byte) 0x00, (byte) 0x00, (byte) 0x00, (byte) 0x00, (byte) 0x00, (byte) 0x00, (byte) 0x00, (byte) 0x00, (byte) 0x00, (byte) 0x00, (byte) 0x00, (byte) 0x00, (byte) 0x00, (byte) 0x00, (byte) 0x00, (byte) 0x00, (byte) 0x00, (byte) 0x00, (byte) 0x00, (byte) 0x00, (byte) 0x00, (byte) 0x00, (byte) 0x00, (byte) 0x00, (byte) 0x00, (byte) 0x00, (byte) 0x00, (byte) 0x00, (byte) 0x00, (byte) 0x00, (byte) 0x00, (byte) 0x00, (byte) 0x00, (byte) 0x00, (byte) 0x00, (byte) 0x00, (byte) 0x00, (byte) 0x00, (byte) 0x00, (byte) 0x00, (byte) 0x00, (byte) 0x00, (byte) 0x00, (byte) 0x00, (byte) 0x00, (byte) 0x00, (byte) 0x00, (byte) 0x00, (byte) 0x00, (byte) 0x00, (byte) 0x00, (byte) 0x00, (byte) 0x00, (byte) 0x00, (byte) 0x00, (byte) 0x00, (byte) 0x00, (byte) 0x00, (byte) 0x00, (byte) 0x00, (byte) 0x00, (byte) 0x00, (byte) 0x00, (byte) 0x00, (byte) 0x00, (byte) 0x00, (byte) 0x00, (byte) 0x00, (byte) 0x00, (byte) 0x00, (byte) 0x00, (byte) 0x00, (byte) 0x00, (byte) 0x00, (byte) 0x00, (byte) 0x00, (byte) 0x00, (byte) 0x00, (byte) 0x00, (byte) 0x00, (byte) 0x00, (byte) 0x00, (byte) 0x00, (byte) 0x00, (byte) 0x00, (byte) 0x00, (byte) 0x00, (byte) 0x00, (byte) 0x00, (byte) 0x00, (byte) 0x00, (byte) 0x00, (byte) 0x00, (byte) 0x00, (byte) 0x00, (byte) 0x00, (byte) 0x00, (byte) 0x00, (byte) 0x00, (byte) 0x00, (byte) 0x00, (byte) 0x00, (byte) 0x00, (byte) 0x00, (byte) 0x00, (byte) 0x00, (byte) 0x00, (byte) 0x00, (byte) 0x00, (byte) 0x00, (byte) 0x00, (byte) 0x00, (byte) 0x00, (byte) 0x00, (byte) 0x00, (byte) 0x00, (byte) 0x00, (byte) 0x00, (byte) 0x00, (byte) 0x00, (byte) 0x00, (byte) 0x00, (byte) 0x00, (byte) 0x00, (byte) 0x00, (byte) 0x00, (byte) 0x00, (byte) 0x00, (byte) 0x00, (byte) 0x00, (byte) 0x00, (byte) 0x00, (byte) 0x00, (byte) 0x00, (byte) 0x00, (byte) 0x00, (byte) 0x00, (byte) 0x00, (byte) 0x00, (byte) 0x00, (byte) 0x00, (byte) 0x00, (byte) 0x00, (byte) 0x00, (byte) 0x00, (byte) 0x00, (byte) 0x00, (byte) 0x00, (byte) 0x00, (byte) 0x00, (byte) 0x00, (byte) 0x00, (byte) 0x00, (byte) 0x00, (byte) 0x00, (byte) 0x00, (byte) 0x00, (byte) 0x00, (byte) 0x00, (byte) 0x00, (byte) 0x00, (byte) 0x00, (byte) 0x00, (byte) 0x00, (byte) 0x00, (byte) 0x00, (byte) 0x00, (byte) 0x00, (byte) 0x00, (byte) 0x00, (byte) 0x00, (byte) 0x00, (byte) 0x00, (byte) 0x00, (byte) 0x00, (byte) 0x00, (byte) 0x00, (byte) 0x00, (byte) 0x00, (byte) 0x00, (byte) 0x00, (byte) 0x00, (byte) 0x00, (byte) 0x00, (byte) 0x00, (byte) 0x00, (byte) 0x00, (byte) 0x00, (byte) 0x00, (byte) 0x00, (byte) 0x00, (byte) 0x00, (byte) 0x00, (byte) 0x00, (byte) 0x00, (byte) 0x00, (byte) 0x00, (byte) 0x00, (byte) 0x00, (byte) 0x00, (byte) 0x00, (byte) 0x00, (byte) 0x00, (byte) 0x00, (byte) 0x0E, (byte) 0x00, (byte) 0x00, (byte) 0x00, (byte) 0x00, (byte) 0x00, (byte) 0x00, (byte) 0x00, (byte) 0x00, (byte) 0x00, (byte) 0x00, (byte) 0x00, (byte) 0x00, (byte) 0x00, (byte) 0x00, (byte) 0x00, (byte) 0x00, (byte) 0x00, (byte) 0x00, (byte) 0x00, (byte) 0x00, (byte) 0x00, (byte) 0x00, (byte) 0x00, (byte) 0x00, (byte) 0x00, (byte) 0x00, (byte) 0x00, (byte) 0x00, (byte) 0x00, (byte) 0x00, (byte) 0x00, (byte) 0x00, (byte) 0x00, (byte) 0x00, (byte) 0x00, (byte) 0x1B, (byte) 0x00, (byte) 0x00, (byte) 0x00, (byte) 0x00, (byte) 0x00, (byte) 0x00, (byte) 0x00, (byte) 0x00, (byte) 0x00, (byte) 0x00, (byte) 0x00, (byte) 0x00, (byte) 0x00, (byte) 0x00, (byte) 0x00, (byte) 0x00, (byte) 0x00, (byte) 0x00, (byte) 0x00, (byte) 0x00, (byte) 0x00, (byte) 0x00, (byte) 0x00, (byte) 0x00, (byte) 0x00, (byte) 0x00, (byte) 0x00, (byte) 0x00, (byte) 0x00, (byte) 0x00, (byte) 0x00, (byte) 0x00, (byte) 0x00, (byte) 0x00, (byte) 0x00, (byte) 0x39, (byte) 0x00, (byte) 0x00, (byte) 0x00, (byte) 0x00, (byte) 0x00, (byte) 0x00, (byte) 0x00, (byte) 0x00, (byte) 0x00, (byte) 0x00, (byte) 0x00, (byte) 0x00, (byte) 0x00, (byte) 0x00, (byte) 0x00, (byte) 0x00, (byte) 0x00, (byte) 0x00, (byte) 0x00, (byte) 0x00, (byte) 0x00, (byte) 0x00, (byte) 0x00, (byte) 0x00, (byte) 0x00, (byte) 0x00, (byte) 0x00, (byte) 0x00, (byte) 0x00, (byte) 0x00, (byte) 0x00, (byte) 0x00, (byte) 0x00, (byte) 0x00, (byte) 0x00, (byte) 0x38, (byte) 0x80, (byte) 0x00, (byte) 0x00, (byte) 0x00, (byte) 0x00, (byte) 0x00, (byte) 0x00, (byte) 0x00, (byte) 0x00, (byte) 0x00, (byte) 0x00, (byte) 0x00, (byte) 0x00, (byte) 0x00, (byte) 0x00, (byte) 0x00, (byte) 0x00, (byte) 0x00, (byte) 0x00, (byte) 0x00, (byte) 0x00, (byte) 0x00, (byte) 0x00, (byte) 0x00, (byte) 0x00, (byte) 0x00, (byte) 0x00, (byte) 0x00, (byte) 0x00, (byte) 0x00, (byte) 0x00, (byte) 0x00, (byte) 0x00, (byte) 0x00, (byte) 0x00, (byte) 0x7C, (byte) 0x40, (byte) 0x00, (byte) 0x00, (byte) 0x00, (byte) 0x00, (byte) 0x00, (byte) 0x00, (byte) 0x00, (byte) 0x00, (byte) 0x00, (byte) 0x00, (byte) 0x00, (byte) 0x00, (byte) 0x00, (byte) 0x00, (byte) 0x00, (byte) 0x00, (byte) 0x00, (byte) 0x00, (byte) 0x00, (byte) 0x00, (byte) 0x00, (byte) 0x00, (byte) 0x00, (byte) 0x00, (byte) 0x00, (byte) 0x00, (byte) 0x0F, (byte) 0x00, (byte) 0x00, (byte) 0x00, (byte) 0x00, (byte) 0x00, (byte) 0x00, (byte) 0x00, (byte) 0x7E, (byte) 0x20, (byte) 0x00, (byte) 0x00, (byte) 0x00, (byte) 0x00, (byte) 0x00, (byte) 0x00, (byte) 0x00, (byte) 0x00, (byte) 0x00, (byte) 0x00, (byte) 0x00, (byte) 0x00, (byte) 0x00, (byte) 0x00, (byte) 0x00, (byte) 0x00, (byte) 0x00, (byte) 0x00, (byte) 0x00, (byte) 0x00, (byte) 0x00, (byte) 0x00, (byte) 0x00, (byte) 0x00, (byte) 0x00, (byte) 0x00, (byte) 0x10, (byte) 0xC0, (byte) 0x00, (byte) 0x00, (byte) 0x00, (byte) 0x00, (byte) 0x00, (byte) 0x03, (byte) 0x3F, (byte) 0x10, (byte) 0x00, (byte) 0x00, (byte) 0x00, (byte) 0x00, (byte) 0x00, (byte) 0x00, (byte) 0x00, (byte) 0x00, (byte) 0x00, (byte) 0x00, (byte) 0x00, (byte) 0x00, (byte) 0x00, (byte) 0x00, (byte) 0x00, (byte) 0x00, (byte) 0x00, (byte) 0x00, (byte) 0x00, (byte) 0x00, (byte) 0x00, (byte) 0x00, (byte) 0x00, (byte) 0x00, (byte) 0x00, (byte) 0x00, (byte) 0x37, (byte) 0x40, (byte) 0x00, (byte) 0x00, (byte) 0x00, (byte) 0x00, (byte) 0x00, (byte) 0x06, (byte) 0x9F, (byte) 0x88, (byte) 0x00, (byte) 0x00, (byte) 0x00, (byte) 0x00, (byte) 0x00, (byte) 0x00, (byte) 0x00, (byte) 0x00, (byte) 0x00, (byte) 0x00, (byte) 0x00, (byte) 0x00, (byte) 0x00, (byte) 0x00, (byte) 0x00, (byte) 0x00, (byte) 0x00, (byte) 0x00, (byte) 0x00, (byte) 0x00, (byte) 0x00, (byte) 0x00, (byte) 0x00, (byte) 0x00, (byte) 0x00, (byte) 0x00, (byte) 0x25, (byte) 0x40, (byte) 0x00, (byte) 0x00, (byte) 0x00, (byte) 0x00, (byte) 0x00, (byte) 0x0C, (byte) 0x4F, (byte) 0xF0, (byte) 0x00, (byte) 0x00, (byte) 0x00, (byte) 0x00, (byte) 0x00, (byte) 0x00, (byte) 0x00, (byte) 0x00, (byte) 0x00, (byte) 0x00, (byte) 0x00, (byte) 0x00, (byte) 0x00, (byte) 0x00, (byte) 0x00, (byte) 0x00, (byte) 0x00, (byte) 0x00, (byte) 0x00, (byte) 0x00, (byte) 0x00, (byte) 0x00, (byte) 0x00, (byte) 0x00, (byte) 0x00, (byte) 0x00, (byte) 0x27, (byte) 0x40, (byte) 0x00, (byte) 0x00, (byte) 0x00, (byte) 0x00, (byte) 0x00, (byte) 0x1E, (byte) 0x27, (byte) 0xE6, (byte) 0x00, (byte) 0x03, (byte) 0xFF, (byte) 0xFC, (byte) 0x78, (byte) 0x00, (byte) 0x70, (byte) 0x00, (byte) 0xEF, (byte) 0xFF, (byte) 0xF0, (byte) 0x07, (byte) 0xFF, (byte) 0xF8, (byte) 0x7F, (byte) 0xFF, (byte) 0x1E, (byte) 0x00, (byte) 0x7D, (byte) 0xFF, (byte) 0xFE, (byte) 0x0F, (byte) 0xFF, (byte) 0xC1, (byte) 0xFF, (byte) 0xF8, (byte) 0x25, (byte) 0x40, (byte) 0x00, (byte) 0x00, (byte) 0x00, (byte) 0x00, (byte) 0x00, (byte) 0x3F, (byte) 0x93, (byte) 0xCD, (byte) 0x00, (byte) 0x03, (byte) 0xFF, (byte) 0xFE, (byte) 0x78, (byte) 0x00, (byte) 0x70, (byte) 0x00, (byte) 0xEF, (byte) 0xFF, (byte) 0xF0, (byte) 0x07, (byte) 0xFF, (byte) 0xFC, (byte) 0x7F, (byte) 0xFF, (byte) 0x9F, (byte) 0x00, (byte) 0x7D, (byte) 0xFF, (byte) 0xFF, (byte) 0x1F, (byte) 0xFF, (byte) 0xE3, (byte) 0xFF, (byte) 0xFC, (byte) 0x10, (byte) 0xC0, (byte) 0x00, (byte) 0x00, (byte) 0x00, (byte) 0x00, (byte) 0x00, (byte) 0x1F, (byte) 0xC9, (byte) 0x98, (byte) 0x80, (byte) 0x03, (byte) 0xFF, (byte) 0xFF, (byte) 0x78, (byte) 0x00, (byte) 0x70, (byte) 0x00, (byte) 0xEF, (byte) 0xFF, (byte) 0xF0, (byte) 0x07, (byte) 0xFF, (byte) 0xFC, (byte) 0xFF, (byte) 0xFF, (byte) 0x9F, (byte) 0x00, (byte) 0xFD, (byte) 0xFF, (byte) 0xFF, (byte) 0x3F, (byte) 0xFF, (byte) 0xE3, (byte) 0xFF, (byte) 0xFE, (byte) 0x0F, (byte) 0x80, (byte) 0x00, (byte) 0x00, (byte) 0x00, (byte) 0x00, (byte) 0x00, (byte) 0xCF, (byte) 0xE4, (byte) 0x3C, (byte) 0x60, (byte) 0x03, (byte) 0xC0, (byte) 0x0F, (byte) 0x78, (byte) 0x00, (byte) 0x70, (byte) 0x00, (byte) 0xEF, (byte) 0x00, (byte) 0x00, (byte) 0x07, (byte) 0xFF, (byte) 0xFC, (byte) 0xFF, (byte) 0xFF, (byte) 0x9F, (byte) 0x80, (byte) 0xFD, (byte) 0xFF, (byte) 0xFF, (byte) 0xBF, (byte) 0xFF, (byte) 0xF7, (byte) 0xFF, (byte) 0xFE, (byte) 0x00, (byte) 0x00, (byte) 0x00, (byte) 0x00, (byte) 0x00, (byte) 0x00, (byte) 0x01, (byte) 0xA7, (byte) 0xF2, (byte) 0x3F, (byte) 0x30, (byte) 0x03, (byte) 0x80, (byte) 0x07, (byte) 0x78, (byte) 0x00, (byte) 0x70, (byte) 0x00, (byte) 0xEF, (byte) 0x00, (byte) 0x00, (byte) 0x07, (byte) 0x00, (byte) 0x1C, (byte) 0xE0, (byte) 0x03, (byte) 0x9F, (byte) 0x81, (byte) 0xFD, (byte) 0xC0, (byte) 0x07, (byte) 0xB8, (byte) 0x00, (byte) 0xF7, (byte) 0x80, (byte) 0x0E, (byte) 0x00, (byte) 0x00, (byte) 0x00, (byte) 0x00, (byte) 0x00, (byte) 0x00, (byte) 0x03, (byte) 0x93, (byte) 0xFC, (byte) 0x3F, (byte) 0x98, (byte) 0x03, (byte) 0x80, (byte) 0x07, (byte) 0x78, (byte) 0x00, (byte) 0x70, (byte) 0x00, (byte) 0xEF, (byte) 0x00, (byte) 0x00, (byte) 0x07, (byte) 0x00, (byte) 0x1C, (byte) 0xE0, (byte) 0x03, (byte) 0x9F, (byte) 0xC3, (byte) 0xFD, (byte) 0xC0, (byte) 0x07, (byte) 0xB8, (byte) 0x00, (byte) 0x77, (byte) 0x80, (byte) 0x0E, (byte) 0x00, (byte) 0x00, (byte) 0x00, (byte) 0x00, (byte) 0x00, (byte) 0x00, (byte) 0x07, (byte) 0xC9, (byte) 0xF9, (byte) 0x9F, (byte) 0xCC, (byte) 0x03, (byte) 0xFF, (byte) 0xFE, (byte) 0x78, (byte) 0x00, (byte) 0x70, (byte) 0x00, (byte) 0xEF, (byte) 0xFF, (byte) 0xE0, (byte) 0x07, (byte) 0xFF, (byte) 0xFC, (byte) 0xE0, (byte) 0x03, (byte) 0x9F, (byte) 0xC3, (byte) 0xFD, (byte) 0xFF, (byte) 0xFF, (byte) 0x38, (byte) 0x00, (byte) 0x77, (byte) 0x80, (byte) 0x0E, (byte) 0x00, (byte) 0x00, (byte) 0x00, (byte) 0x00, (byte) 0x00, (byte) 0x00, (byte) 0x07, (byte) 0xE4, (byte) 0x73, (byte) 0x4F, (byte) 0xE4, (byte) 0x03, (byte) 0xFF, (byte) 0xFE, (byte) 0x78, (byte) 0x00, (byte) 0x70, (byte) 0x00, (byte) 0xEF, (byte) 0xFF, (byte) 0xE0, (byte) 0x07, (byte) 0xFF, (byte) 0xF8, (byte) 0xE7, (byte) 0xFF, (byte) 0x9D, (byte) 0xE7, (byte) 0xBD, (byte) 0xFF, (byte) 0xFF, (byte) 0x38, (byte) 0x00, (byte) 0x77, (byte) 0x80, (byte) 0x0E, (byte) 0x00, (byte) 0x00, (byte) 0x00, (byte) 0x00, (byte) 0x00, (byte) 0x00, (byte) 0x07, (byte) 0xE2, (byte) 0x72, (byte) 0x27, (byte) 0xFC, (byte) 0x03, (byte) 0xFF, (byte) 0xFE, (byte) 0x78, (byte) 0x00, (byte) 0x70, (byte) 0x00, (byte) 0xEF, (byte) 0xFF, (byte) 0xE0, (byte) 0x07, (byte) 0xFF, (byte) 0xF8, (byte) 0xE7, (byte) 0xFF, (byte) 0x9D, (byte) 0xE7, (byte) 0xBD, (byte) 0xFF, (byte) 0xFF, (byte) 0x38, (byte) 0x00, (byte) 0x77, (byte) 0x80, (byte) 0x0E, (byte) 0x00, (byte) 0x00, (byte) 0x00, (byte) 0x00, (byte) 0x00, (byte) 0x00, (byte) 0x03, (byte) 0xF1, (byte) 0x07, (byte) 0x13, (byte) 0xF8, (byte) 0x03, (byte) 0xFF, (byte) 0xFF, (byte) 0x78, (byte) 0x00, (byte) 0x70, (byte) 0x00, (byte) 0xEF, (byte) 0xFF, (byte) 0xE0, (byte) 0x07, (byte) 0xFF, (byte) 0xFC, (byte) 0xE7, (byte) 0xFF, (byte) 0x9C, (byte) 0xFF, (byte) 0x3D, (byte) 0xFF, (byte) 0xFF, (byte) 0x38, (byte) 0x00, (byte) 0x77, (byte) 0x80, (byte) 0x0E, (byte) 0x00, (byte) 0x00, (byte) 0x00, (byte) 0x00, (byte) 0x00, (byte) 0x00, (byte) 0x01, (byte) 0xF9, (byte) 0x8F, (byte) 0x89, (byte) 0xF0, (byte) 0x03, (byte) 0xC0, (byte) 0x07, (byte) 0x78, (byte) 0x00, (byte) 0x70, (byte) 0x00, (byte) 0xEF, (byte) 0x00, (byte) 0x00, (byte) 0x07, (byte) 0x00, (byte) 0x1E, (byte) 0xE7, (byte) 0xFF, (byte) 0x9C, (byte) 0xFF, (byte) 0x3D, (byte) 0xC0, (byte) 0x07, (byte) 0xB8, (byte) 0x00, (byte) 0x77, (byte) 0x80, (byte) 0x0E, (byte) 0x00, (byte) 0x00, (byte) 0x00, (byte) 0x00, (byte) 0x00, (byte) 0x00, (byte) 0x00, (byte) 0xFF, (byte) 0x8F, (byte) 0xC4, (byte) 0xE0, (byte) 0x03, (byte) 0x80, (byte) 0x07, (byte) 0x78, (byte) 0x00, (byte) 0x70, (byte) 0x00, (byte) 0xEF, (byte) 0x00, (byte) 0x00, (byte) 0x07, (byte) 0x00, (byte) 0x1E, (byte) 0xE0, (byte) 0x03, (byte) 0x9C, (byte) 0x7E, (byte) 0x3D, (byte) 0xC0, (byte) 0x03, (byte) 0xB8, (byte) 0x00, (byte) 0x77, (byte) 0x80, (byte) 0x0E, (byte) 0x00, (byte) 0x00, (byte) 0x00, (byte) 0x00, (byte) 0x00, (byte) 0x00, (byte) 0x00, (byte) 0x7E, (byte) 0x27, (byte) 0xE2, (byte) 0x00, (byte) 0x03, (byte) 0xC0, (byte) 0x07, (byte) 0x78, (byte) 0x00, (byte) 0x78, (byte) 0x01, (byte) 0xEF, (byte) 0x00, (byte) 0x00, (byte) 0x07, (byte) 0x00, (byte) 0x1E, (byte) 0xE0, (byte) 0x03, (byte) 0x9C, (byte) 0x3E, (byte) 0x3D, (byte) 0xE0, (byte) 0x07, (byte) 0xBC, (byte) 0x00, (byte) 0xF7, (byte) 0xC0, (byte) 0x1E, (byte) 0x00, (byte) 0x00, (byte) 0x00, (byte) 0x00, (byte) 0x00, (byte) 0x00, (byte) 0x00, (byte) 0x3C, (byte) 0xD3, (byte) 0xF1, (byte) 0x00, (byte) 0x03, (byte) 0xFF, (byte) 0xFF, (byte) 0x7F, (byte) 0xFF, (byte) 0x3F, (byte) 0xFF, (byte) 0xEF, (byte) 0xFF, (byte) 0xF0, (byte) 0x07, (byte) 0xFF, (byte) 0xFC, (byte) 0xE0, (byte) 0x03, (byte) 0x9C, (byte) 0x3C, (byte) 0x3D, (byte) 0xFF, (byte) 0xFF, (byte) 0xBF, (byte) 0xFF, (byte) 0xF3, (byte) 0xFF, (byte) 0xFE, (byte) 0x00, (byte) 0x00, (byte) 0x00, (byte) 0x00, (byte) 0x00, (byte) 0x00, (byte) 0x00, (byte) 0x19, (byte) 0xC9, (byte) 0xFA, (byte) 0x00, (byte) 0x03, (byte) 0xFF, (byte) 0xFF, (byte) 0x7F, (byte) 0xFF, (byte) 0x3F, (byte) 0xFF, (byte) 0xCF, (byte) 0xFF, (byte) 0xF0, (byte) 0x07, (byte) 0xFF, (byte) 0xFC, (byte) 0xE0, (byte) 0x03, (byte) 0x9C, (byte) 0x18, (byte) 0x3D, (byte) 0xFF, (byte) 0xFF, (byte) 0x1F, (byte) 0xFF, (byte) 0xE3, (byte) 0xFF, (byte) 0xFC, (byte) 0x00, (byte) 0x00, (byte) 0x00, (byte) 0x00, (byte) 0x00, (byte) 0x00, (byte) 0x00, (byte) 0x03, (byte) 0xE4, (byte) 0xFC, (byte) 0x00, (byte) 0x03, (byte) 0xFF, (byte) 0xFE, (byte) 0x7F, (byte) 0xFF, (byte) 0x1F, (byte) 0xFF, (byte) 0x8F, (byte) 0xFF, (byte) 0xF0, (byte) 0x07, (byte) 0xFF, (byte) 0xF8, (byte) 0xE0, (byte) 0x03, (byte) 0x9C, (byte) 0x18, (byte) 0x3D, (byte) 0xFF, (byte) 0xFF, (byte) 0x0F, (byte) 0xFF, (byte) 0xC1, (byte) 0xFF, (byte) 0xF8, (byte) 0x00, (byte) 0x00, (byte) 0x00, (byte) 0x00, (byte) 0x00, (byte) 0x00, (byte) 0x00, (byte) 0x07, (byte) 0xF2, (byte) 0x78, (byte) 0x00, (byte) 0x00, (byte) 0x00, (byte) 0x00, (byte) 0x00, (byte) 0x00, (byte) 0x00, (byte) 0x00, (byte) 0x00, (byte) 0x00, (byte) 0x00, (byte) 0x07, (byte) 0xFF, (byte) 0xC0, (byte) 0xC0, (byte) 0x01, (byte) 0x9C, (byte) 0x00, (byte) 0x19, (byte) 0xFF, (byte) 0xF8, (byte) 0x03, (byte) 0xFF, (byte) 0x00, (byte) 0x3F, (byte) 0xE0, (byte) 0x00, (byte) 0x00, (byte) 0x00, (byte) 0x00, (byte) 0x00, (byte) 0x00, (byte) 0x00, (byte) 0x03, (byte) 0xF9, (byte) 0x30, (byte) 0x00, (byte) 0x00, (byte) 0x00, (byte) 0x00, (byte) 0x00, (byte) 0x00, (byte) 0x00, (byte) 0x00, (byte) 0x00, (byte) 0x00, (byte) 0x00, (byte) 0x00, (byte) 0x00, (byte) 0x00, (byte) 0x00, (byte) 0x00, (byte) 0x00, (byte) 0x00, (byte) 0x00, (byte) 0x00, (byte) 0x00, (byte) 0x00, (byte) 0x00, (byte) 0x00, (byte) 0x00, (byte) 0x00, (byte) 0x00, (byte) 0x00, (byte) 0x00, (byte) 0x00, (byte) 0x00, (byte) 0x00, (byte) 0x00, (byte) 0x01, (byte) 0xFC, (byte) 0x80, (byte) 0x00, (byte) 0x00, (byte) 0x00, (byte) 0x00, (byte) 0x00, (byte) 0x00, (byte) 0x00, (byte) 0x00, (byte) 0x00, (byte) 0x00, (byte) 0x00, (byte) 0x00, (byte) 0x00, (byte) 0x00, (byte) 0x00, (byte) 0x00, (byte) 0x00, (byte) 0x00, (byte) 0x00, (byte) 0x00, (byte) 0x00, (byte) 0x00, (byte) 0x00, (byte) 0x00, (byte) 0x00, (byte) 0x00, (byte) 0x00, (byte) 0x00, (byte) 0x00, (byte) 0x00, (byte) 0x00, (byte) 0x00, (byte) 0x00, (byte) 0x00, (byte) 0xFE, (byte) 0x40, (byte) 0x00, (byte) 0x00, (byte) 0x00, (byte) 0x00, (byte) 0x00, (byte) 0x00, (byte) 0x00, (byte) 0x00, (byte) 0x00, (byte) 0x00, (byte) 0x00, (byte) 0x00, (byte) 0x00, (byte) 0x00, (byte) 0x00, (byte) 0x00, (byte) 0x00, (byte) 0x00, (byte) 0x00, (byte) 0x00, (byte) 0x00, (byte) 0x00, (byte) 0x00, (byte) 0x00, (byte) 0x00, (byte) 0x00, (byte) 0x00, (byte) 0x00, (byte) 0x00, (byte) 0x00, (byte) 0x00, (byte) 0x00, (byte) 0x00, (byte) 0x00, (byte) 0x3F, (byte) 0x80, (byte) 0x00, (byte) 0x00, (byte) 0x00, (byte) 0x00, (byte) 0x00, (byte) 0x00, (byte) 0x00, (byte) 0x00, (byte) 0x00, (byte) 0x00, (byte) 0x00, (byte) 0x00, (byte) 0x00, (byte) 0x00, (byte) 0x00, (byte) 0x00, (byte) 0x00, (byte) 0x00, (byte) 0x00, (byte) 0x00, (byte) 0x00, (byte) 0x00, (byte) 0x00, (byte) 0x00, (byte) 0x00, (byte) 0x00, (byte) 0x00, (byte) 0x00, (byte) 0x00, (byte) 0x00, (byte) 0x00, (byte) 0x00, (byte) 0x00, (byte) 0x00, (byte) 0x3F, (byte) 0x00, (byte) 0x00, (byte) 0x00, (byte) 0x00, (byte) 0x00, (byte) 0x00, (byte) 0x00, (byte) 0x00, (byte) 0x00, (byte) 0x00, (byte) 0x00, (byte) 0x00, (byte) 0x00, (byte) 0x00, (byte) 0x00, (byte) 0x00, (byte) 0x00, (byte) 0x00, (byte) 0x00, (byte) 0x00, (byte) 0x00, (byte) 0x00, (byte) 0x00, (byte) 0x00, (byte) 0x00, (byte) 0x00, (byte) 0x00, (byte) 0x00, (byte) 0x00, (byte) 0x00, (byte) 0x00, (byte) 0x00, (byte) 0x00, (byte) 0x00, (byte) 0x00, (byte) 0x1F, (byte) 0x00, (byte) 0x00, (byte) 0x00, (byte) 0x00, (byte) 0x00, (byte) 0x00, (byte) 0x00, (byte) 0x00, (byte) 0x00, (byte) 0x00, (byte) 0x00, (byte) 0x00, (byte) 0x00, (byte) 0x00, (byte) 0x00, (byte) 0x00, (byte) 0x00, (byte) 0x00, (byte) 0x00, (byte) 0x00, (byte) 0x00, (byte) 0x00, (byte) 0x00, (byte) 0x00, (byte) 0x00, (byte) 0x00, (byte) 0x00, (byte) 0x00, (byte) 0x00, (byte) 0x00, (byte) 0x00, (byte) 0x00, (byte) 0x00, (byte) 0x00, (byte) 0x00, (byte) 0x0E, (byte) 0x00, (byte) 0x00, (byte) 0x00, (byte) 0x00, (byte) 0x00, (byte) 0x00, (byte) 0x00, (byte) 0x00, (byte) 0x00, (byte) 0x00, (byte) 0x00, (byte) 0x00, (byte) 0x00, (byte) 0x00, (byte) 0x00, (byte) 0x00, (byte) 0x00, (byte) 0x00, (byte) 0x00, (byte) 0x00, (byte) 0x00, (byte) 0x00, (byte) 0x00, (byte) 0x00, (byte) 0x00, (byte) 0x00, (byte) 0x00, (byte) 0x00, (byte) 0x00, (byte) 0x00, (byte) 0x00, (byte) 0x00, (byte) 0x00, (byte) 0x00, (byte) 0x00, (byte) 0x00, (byte) 0x00, (byte) 0x00, (byte) 0x00, (byte) 0x00, (byte) 0x00, (byte) 0x00, (byte) 0x00, (byte) 0x00, (byte) 0x00, (byte) 0x00, (byte) 0x00, (byte) 0x00, (byte) 0x00, (byte) 0x00, (byte) 0x00, (byte) 0x00, (byte) 0x00, (byte) 0x00, (byte) 0x00, (byte) 0x00, (byte) 0x00, (byte) 0x00, (byte) 0x00, (byte) 0x00, (byte) 0x00, (byte) 0x00, (byte) 0x00, (byte) 0x00, (byte) 0x00, (byte) 0x00, (byte) 0x00, (byte) 0x00, (byte) 0x00, (byte) 0x00, (byte) 0x00, (byte) 0x00, (byte) 0x00, (byte) 0x00, (byte) 0x00, (byte) 0x00, (byte) 0x00, (byte) 0x00, (byte) 0x00, (byte) 0x00, (byte) 0x00, (byte) 0x00, (byte) 0x00, (byte) 0x00, (byte) 0x00, (byte) 0x00, (byte) 0x00, (byte) 0x00, (byte) 0x00, (byte) 0x00, (byte) 0x00, (byte) 0x00, (byte) 0x00, (byte) 0x00, (byte) 0x00, (byte) 0x00, (byte) 0x00, (byte) 0x00, (byte) 0x00, (byte) 0x00, (byte) 0x00, (byte) 0x00, (byte) 0x00, (byte) 0x00, (byte) 0x00, (byte) 0x00, (byte) 0x00, (byte) 0x00, (byte) 0x00, (byte) 0x00, (byte) 0x00, (byte) 0x00, (byte) 0x00, (byte) 0x00, (byte) 0x00, (byte) 0x00, (byte) 0x00, (byte) 0x00, (byte) 0x00, (byte) 0x00, (byte) 0x00, (byte) 0x00, (byte) 0x00, (byte) 0x00, (byte) 0x00, (byte) 0x00, (byte) 0x00, (byte) 0x00, (byte) 0x00, (byte) 0x00, (byte) 0x00, (byte) 0x00, (byte) 0x00, (byte) 0x00, (byte) 0x00, (byte) 0x00, (byte) 0x00,
        };

        ByteArrayOutputStream blob = new ByteArrayOutputStream();
        bitmap.compress(Bitmap.CompressFormat.PNG, 0, blob);
        byte[] bitmapdata = blob.toByteArray();
        try {
            String temp = byteArrayToHexString(bitmapdata);
            byte[] tempByte = hexStringToByteArray(temp);
            Toast.makeText(getApplicationContext(), "" + tempByte[0], Toast.LENGTH_SHORT).show();
            outputStream.write(tempByte);
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    public static String byteArrayToHexString(byte[] b) throws Exception {
        String result = "";
        for (int i = 0; i < b.length; i++) {
            result += Integer.toString((b[i] & 0xff) + 0x100, 16).substring(1);
        }
        return result;
    }

    public static byte[] hexStringToByteArray(String s) {
        int len = s.length();
        byte[] data = new byte[len / 2];
        for (int i = 0; i < len; i += 2) {
            data[i / 2] = (byte) ((Character.digit(s.charAt(i), 16) << 4) + Character.digit(s.charAt(i + 1), 16));
        }
        return data;
    }


}
