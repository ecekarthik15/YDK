package android.sales.rajesh.com.sales.Result;

import java.util.List;

/**
 * Created by Karthik on 7/17/18.
 */

public class VisitedParseResult  implements ParseResult{

    private String rawResult;


    Boolean isSuccess;
    String message;

    public String getRawResult() {
        return rawResult;
    }

    public void setRawResult(String rawResult) {
        this.rawResult = rawResult;
    }

    public Boolean getSuccess() {
        return isSuccess;
    }

    public void setSuccess(Boolean success) {
        isSuccess = success;
    }

    public String getMessage() {
        return message;
    }

    public void setMessage(String message) {
        this.message = message;
    }

    @Override
    public void setData(List data) {

    }

    @Override
    public List getData() {
        return null;
    }
}
