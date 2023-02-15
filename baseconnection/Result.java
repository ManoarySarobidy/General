package baseconnection;

import java.sql.*;
import java.util.Vector;
import java.lang.reflect.*;

public class Result{
    ResultSet result;
    ResultSetMetaData meta;
    int columnCount;
    
    public Result(ResultSet r) throws Exception{
        this.setResultSet(r);
        this.setResultMeta();
    }

    private void setResultSet(ResultSet r){
        this.result = r;
    }
    private void setResultMeta() throws Exception {
        this.meta = this.getResult().getMetaData();
        this.setColumnCount();
    }

    public ResultSetMetaData getMetaResult(){
        return this.meta;
    }
    public ResultSet getResult(){
        return this.result;
    }

    public void setColumnCount() throws Exception{
        this.columnCount = this.getMetaResult().getColumnCount();
    }
    public int getCount(){
        return this.columnCount;
    }

    public static Object sum( Vector<Object> object,String method) throws Exception{
        double sum = 0 ;
        for(int i = 0 ; i < object.size() ; i++){
            Method toGet = object.get(i).getClass().getMethod( method );
            sum += (double)toGet.invoke( object.get(i) );
        }
        return sum;
    }
}