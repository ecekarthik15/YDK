package android.sales.rajesh.com.sales.Result;

import android.sales.rajesh.com.sales.Model.Users;

import java.util.List;

/**
 * Created by Karthik on 2/11/17.
 */

public class LoginParseResult implements ParseResult {


    private String rawResult;

    Users users;

    public String getRawResult() {
        return rawResult;
    }

    public void setRawResult(String rawResult) {
        this.rawResult = rawResult;
    }




    public Users getUsers() {
        return users;
    }

    public void setUsers(Users users) {
        this.users = users;
    }


    @Override
    public void setData(List data) {

    }

    @Override
    public List getData() {
        return null;
    }
}
