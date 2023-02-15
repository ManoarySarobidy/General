package baseconnection;

import java.io.Closeable;
import java.io.IOException;
import java.sql.*;

public class Connect implements Closeable{
// variable declarations
    Connection oracle;
    Connection postgres;

    Connection currentConnection;

    String serverName; // for oracle server
    String postgreServer; // for postgres server

    String userName;
    String Password;

    String userPostgres;
    String postgresPassword;
    
    String database;
    String driver;
    
    public Connect(){
         this.initOracle();
         this.initPostgres();
    }
    
    public Connect( String base ){
         this.setDatabase(base);
         this.initOracle();
         this.initPostgres();
    }
    /**
     * 
     * @param driver Give the connection driver { " Oracle " , " Postgres " } 
     * @param base Give the database to be connected 
     */
    
    public Connect( String driver , String base ){
        this.setDriver(driver);
        this.setDatabase(base);
        this.initOracle();
        this.initPostgres();
    }
    
    private void initOracle(){
        this.setServerName();
        this.setUsername("sarobidy");
        this.setPassword("manoary");
    }
    
    private void initPostgres(){
        this.setPostgreServer();
        this.setUserPostgres("sarobidy");
        this.setPostgresPassword("manoary");
    }
    
// open connections
    public void openConnection( String connectionName ) throws Exception {
        if( connectionName.equalsIgnoreCase("Oracle") ){
            this.currentConnection = this.getOracle();
        }else{
            this.currentConnection = this.getPostgres();
        }
    }
    
    public void openConnection( ) throws Exception {
        if( this.getDriver().equalsIgnoreCase("Oracle") ){
            this.currentConnection = this.getOracle();
        }else{
            this.currentConnection = this.getPostgres();
        }
    }
    
    public void openOracle() throws Exception {
        Class.forName("oracle.jdbc.driver.OracleDriver");
        this.oracle = DriverManager.getConnection(this.getServer(),this.getUsername(),this.getPassword());
        this.oracle.setAutoCommit(false);
    }
    public void openPostgres() throws Exception {
        Class.forName("org.postgresql.Driver");
        this.postgres = DriverManager.getConnection(this.getPostgreServer(),this.getUserPostgres(),this.getPostgresPassword());
        this.postgres.setAutoCommit(false);
    }

    public Connection getOracle() throws Exception{
        if( this.oracle == null ) this.openOracle();
        return oracle;
    }
    
    public Connection getPostgres() throws Exception{
        if( this.postgres == null ) this.openPostgres(); 
        return postgres;
    }
// close connections
    public void closeOracle() throws Exception {
        this.getOracle().close();
    }
    public void closePostgres() throws Exception {
        this.getPostgres().close();
    }
    
    public Connection getConnection() throws Exception{
        if( this.getDriver().equalsIgnoreCase("Oracle") ){
            return getOracle();
        }else{
            return getPostgres();
        }
    }

    public Connection getCurrentConnection(){
        return this.currentConnection;
    }

    /**
     *
     * @throws SQLException
     */
    public void closeConnection() throws SQLException{
        this.getCurrentConnection().close();
    }
    public void rollback() throws SQLException{
        this.getCurrentConnection().rollback();
    }
    public void commit() throws SQLException{
        this.getCurrentConnection().commit();
    }

    public void setConnection(Connection connection){
        this.currentConnection = connection;      
    }
        
// setters and getters
    public void setServerName(){
        this.serverName = "jdbc:oracle:thin:@localhost:1521:"+this.getDatabase();
    }
    
    public void setUsername(){
        this.userName = "sarobidy";
    }
    public void setPassword(){
        this.Password = "manoary";
    }
    
    public void setUsername(String username){
        this.userName = username;
    }
    public void setPassword(String password){
        this.Password = password;
    }

    public void setPostgreServer(){
        this.postgreServer = "jdbc:postgresql://localhost:5432/" + this.getDatabase();
    }
    public void setUserPostgres(){
        this.userPostgres = "sarobidy";
    }
    public void setPostgresPassword(){
        this.postgresPassword = "manoary";
    }
    public void setUserPostgres(String username){
        this.userPostgres = username;
    }
    public void setPostgresPassword(String password){
        this.postgresPassword = password;
    }
    

    public String getUsername(){
        return this.userName;
    }public String getServer(){
        return this.serverName;
    }
    public String getPassword(){
        return this.Password;
    }
    public String getUserPostgres(){
        return this.userPostgres;
    }
    public String getPostgreServer(){
        return this.postgreServer;
    }
    public String getPostgresPassword(){
        return this.postgresPassword;
    }

    public String getDatabase() {
        return database;
    }

    private void setDatabase(String database) {
        this.database = database;
    }

    public String getDriver() {
        return driver;
    }

    private void setDriver(String driver) {
        this.driver = driver;
    }
    
    @Override
    public void close() throws IOException {
        
    }
}    