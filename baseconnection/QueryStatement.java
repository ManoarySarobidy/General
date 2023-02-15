package baseconnection;
import java.sql.*;

public final class QueryStatement{
    String query;
    Connect connection;
    Result result;
   
    public QueryStatement( String query ) throws Exception{
        this.setQuery( query );
    }
    public QueryStatement() throws Exception{
        
    }
    public void setConnection() throws Exception{
        this.connection = new Connect();
        this.getConnection().openConnection("Postgres");
    }
    public void setConnection(Connection connection) throws Exception{
        this.connection = new Connect();
        this.getConnection().setConnection(connection);
    }

    public void executeQuery() throws Exception{
        this.getConnection().openConnection();
        Statement statement = this.getConnection().getConnection().createStatement();
        
        this.setResult( statement.executeQuery( this.getQuery() ) );
    }

    public void executeUpdate() throws Exception{
        Statement statement = this.getConnection().getConnection().createStatement();
        try{
            statement.executeUpdate( this.getQuery() );
            this.commit();
        }catch(SQLException e){
            this.getConnection().rollback();
        }
    }

    public void setResult(ResultSet resultset) throws Exception{
        this.result = new Result( resultset );
    }

    public void setConnection( Connect connex ){
        this.connection = connex;
    }
    public Connect getConnection(){
        return this.connection;
    }

    public Result getResult(){
        return this.result;
    }

    public void setQuery(String r){
        this.query = r.trim();
    }
    public String getQuery(){
        return this.query;
    }

    public void commit() throws SQLException{
        this.getConnection().commit();
    }

    public void close() throws Exception{
        this.getConnection().closeConnection();
    }
}