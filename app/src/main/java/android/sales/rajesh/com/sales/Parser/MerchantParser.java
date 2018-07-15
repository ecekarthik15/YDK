package android.sales.rajesh.com.sales.Parser;

import android.sales.rajesh.com.sales.Core.CoreActivity;
import android.sales.rajesh.com.sales.Model.Bill;
import android.sales.rajesh.com.sales.Model.Merchant;
import android.sales.rajesh.com.sales.Result.ParseResult;
import android.sales.rajesh.com.sales.Result.MerchantParseResult;
import android.sales.rajesh.com.sales.Utils.SalesProtocol;
import android.util.Log;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;
import org.json.JSONTokener;

import java.util.ArrayList;
import java.util.List;
import java.util.Vector;

/**
 * Created by Karthik on 2/1/17.
 */

public class MerchantParser extends JSONParser {

    private String TAG = "MerchantParser";
    MerchantParseResult result;

    public MerchantParser( CoreActivity activity ) {
        this.activity = activity;
    }


    @Override
    public void doParsing(String rawResult) {
        result = new MerchantParseResult();

        if (rawResult != null) {
            JSONTokener tokener = new JSONTokener(rawResult);
            result.setRawResult(rawResult);


            try{
                JSONArray jsonArray = new JSONArray(tokener);

                int arrayCount = jsonArray.length();
                Vector<Merchant> merchantVector = new Vector<Merchant>(arrayCount);

                for (int i = 0; i < arrayCount; i++) {


                    Merchant merchantObj = new Merchant();

                    JSONObject jsonObj = jsonArray.getJSONObject(i);

                    if(jsonObj.has("id")){

                        merchantObj.setId(jsonObj.getInt("id"));

                    }

                    if(jsonObj.has("did")){

                        merchantObj.setDistrictId(jsonObj.getInt("did"));

                    }


                    if(jsonObj.has("cid")){

                        merchantObj.setCityId(jsonObj.getInt("cid"));

                    }

                    if(jsonObj.has("dtxt")){

                        merchantObj.setDistrict(""+jsonObj.getString("dtxt"));

                    }

                    if(jsonObj.has("txt")){

                        merchantObj.setName(""+jsonObj.getString("txt"));

                    }

                    if(jsonObj.has("bal")){

                        merchantObj.setTotalBalance(+jsonObj.getDouble("bal"));

                    }

                    if(jsonObj.has("ctxt")){

                        merchantObj.setCity(""+jsonObj.getString("ctxt"));


                    }


                    double collection_A = 0;
                    double collection_E = 0;


                    if(jsonObj.has("bs")){

                        JSONArray billArray = jsonObj.getJSONArray("bs");

                        if(billArray != null && billArray.length() > 0) {
                            List<Bill> billList = new ArrayList<>(billArray.length());

                            for (int j = 0; j < billArray.length(); j++){
                                JSONObject billObj = billArray.getJSONObject(j);
                                if(billObj != null) {
                                    Bill bill = new Bill();

                                    int aid = 0;

                                    bill.setMerchantId(merchantObj.getId());

                                    if(billObj.has("id")){
                                        bill.setId(billObj.getInt("id"));
                                    }
                                    if(billObj.has("bn")){
                                        bill.setBillingNumber(billObj.getString("bn"));
                                    }

                                    if(billObj.has("bd")){
                                        bill.setDate(""+billObj.getString("bd"));
                                    }
                                    if(billObj.has("atid")){
                                        aid = billObj.getInt("atid");

                                        bill.setAid(billObj.getInt("atid"));
                                    }
                                    if(billObj.has("ct")){
                                        bill.setcType(""+billObj.getString("ct"));
                                    }
                                    if(billObj.has("ba")){
                                        bill.setBillingAmount(billObj.getDouble("ba"));
                                    }
                                    if(billObj.has("bb")){
                                        double billinBalance = billObj.getDouble("bb");

                                        if(aid == SalesProtocol.PAYMENT_TYPE_A){
                                            collection_A = collection_A + billinBalance;
                                        }else if(aid == SalesProtocol.PAYMENT_TYPE_E){
                                            collection_E = collection_E+billinBalance;
                                        }
                                        bill.setBillingBalance(billinBalance);
                                    }

                                    billList.add(bill);

                                }

                            }

                            merchantObj.setTotalABalance(collection_A);
                            merchantObj.setTotalEBalance(collection_E);

                            merchantObj.setBillList(billList);
                        }

                    }

                    merchantVector.addElement(merchantObj);

                }

                result.setData(merchantVector);


            }catch (JSONException exception){

                Log.d(TAG,"Parsing Error ==================== "+exception.getMessage());

            }

        }


        Log.d(TAG,"doparsing : "+rawResult);

    }

    @Override
    public ParseResult getparseResult() {
        return result;
    }
}
