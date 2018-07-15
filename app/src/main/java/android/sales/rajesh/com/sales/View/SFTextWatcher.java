package android.sales.rajesh.com.sales.View;

import android.sales.rajesh.com.sales.Controller.MerchantPaymentActivity;
import android.text.Editable;
import android.text.TextWatcher;
import android.widget.EditText;

/**
 * Created by Karthik on 2/16/17.
 */

public class SFTextWatcher implements TextWatcher {

    EditText mEditText;


    public SFTextWatcher(EditText editText){
        this.mEditText = editText;
    }


    @Override
    public void beforeTextChanged(CharSequence s, int start, int count, int after) {


    }

    @Override
    public void onTextChanged(CharSequence s, int start, int before, int count) {

        if(mEditText.getTag().equals (MerchantPaymentActivity.COLLECTION_AMOUNT_EDITTEXT_TAG)){

        }

        if(mEditText.getTag().equals (MerchantPaymentActivity.DISCOUNT_AMOUNT_EDITTEXT_TAG)){

        }

    }

    @Override
    public void afterTextChanged(Editable s) {

        if(mEditText.getTag().equals (MerchantPaymentActivity.COLLECTION_AMOUNT_EDITTEXT_TAG)){

        }

        if(mEditText.getTag().equals (MerchantPaymentActivity.DISCOUNT_AMOUNT_EDITTEXT_TAG)){

        }

    }
}
