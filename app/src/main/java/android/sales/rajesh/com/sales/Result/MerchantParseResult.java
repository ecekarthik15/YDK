package android.sales.rajesh.com.sales.Result;

import android.sales.rajesh.com.sales.Model.Merchant;

import java.util.List;
import java.util.Vector;

/**
 * Created by Karthik on 2/1/17.
 */

public class MerchantParseResult implements ParseResult {

    private int resultCode;
    private String rawResult;

    private String version;
    private List<Merchant> merchantsVector;

    public int getResultCode() {
        return resultCode;
    }

    public void setResultCode(int resultCode) {
        this.resultCode = resultCode;
    }

    public String getRawResult() {
        return rawResult;
    }

    public void setRawResult(String rawResult) {
        this.rawResult = rawResult;
    }

    public String getVersion() {
        return version;
    }

    public void setVersion(String version) {
        this.version = version;
    }




    @Override
    public void setData(List data) {
        this.merchantsVector = data;
    }

    @Override
    public List getData() {
        return this.merchantsVector;
    }
}
