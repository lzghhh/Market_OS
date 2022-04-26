

import java.net.ConnectException;
import java.sql.CallableStatement;
import java.sql.Connection;
import java.sql.Date;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Statement;
import java.sql.Time;
import java.util.ArrayList;
import java.util.Arrays;

public class SingleMarket {
  private String managerName;
  public SingleMarket(String managerName, String managerGender,
                      Connection connection) throws SQLException {
    this.managerName = managerName;
    Statement statement = connection.createStatement();
    String sql = "CALL ADDMANAGER(?, ?)";
    CallableStatement cs = connection.prepareCall(sql);
    cs.setString(1, this.managerName);
    cs.setString(2, managerGender);
    cs.execute();
  }

  public SingleMarket(String username) {
    this.managerName = username;
  }

  public Integer showPassword(Connection connection) throws SQLException {
    int password = -1;
    Statement statement = connection.createStatement();
    String sql = "SELECT managerID FROM manager";
    ResultSet rs = statement.executeQuery(sql);

    while (rs.next()) {
      password = rs.getInt("managerID");
    }
    rs.close();
    return password;
  }

  public String showUserName(Connection connection) throws  SQLException {
    String username = "";
    Statement statement = connection.createStatement();
    String sql = "SELECT managerNmame FROM manager";
    ResultSet rs = statement.executeQuery(sql);

    while (rs.next()) {
      username = rs.getString("managerNmame");
    }
    rs.close();
    return username;
  }


  public boolean storageAdd(String itemName, int itemAmount, int itemPrice, Connection connection) throws SQLException {
    String sql = "CALL INSERTITEM(?, ?, ?)";
    CallableStatement cs = connection.prepareCall(sql);
    cs.setString(1, itemName);
    cs.setInt(2, itemAmount);
    cs.setInt(3, itemPrice);
    cs.execute();
    cs.close();
    return true;
  }

  public boolean storageChange(int itemID, int itemAmountChange, Connection connection) throws SQLException {
    String sql = "CALL DELITEM(?, ?)";
    CallableStatement cs = connection.prepareCall(sql);
    cs.setInt(1, itemID);
    cs.setInt(2, itemAmountChange);
    cs.execute();
    cs.close();
    return true;
  }

  public boolean addEmployee(String nameIN, String genderIN, String description, Connection connection) throws SQLException {
    String sql = "CALL ADDEMPLOYEE(?, ?, ?)";
    CallableStatement cs = connection.prepareCall(sql);
    cs.setString(1, nameIN);
    cs.setString(2, genderIN);
    cs.setString(3, description);
    cs.execute();
    cs.close();
    return true;
  }

  public boolean deleteEmployee(String name, int employID, Connection connection) throws SQLException {
    String sql = "CALL DELEMPLOYEE(?, ?)";
    CallableStatement cs = connection.prepareCall(sql);
    cs.setString(1, name);
    cs.setInt(2, employID);
    cs.execute();
    cs.close();
    return true;
  }

  public boolean changeManager(String managerName, String gender, Connection connection) throws SQLException {
    String sql = "CALL ADDMANAGER(?, ?)";
    CallableStatement cs = connection.prepareCall(sql);
    cs.setString(1, managerName);
    cs.setString(2, gender);
    cs.execute();
    cs.close();
    return true;
  }

  public boolean addCoupon(int couponPWD, int couponAmount, int managerID, Connection connection) throws SQLException {
    String sql = "CALL ADDCOUPON(?, ?, ?)";
    CallableStatement cs = connection.prepareCall(sql);
    cs.setInt(1, couponPWD);
    cs.setInt(2, couponAmount);
    cs.setInt(3, managerID);
    cs.execute();
    cs.close();
    return true;
  }

  public boolean delCounpon(int couponID, Connection connection) throws SQLException {
    String sql = "CALL DELCOUPON(?)";
    CallableStatement cs = connection.prepareCall(sql);
    cs.setInt(1, couponID);
    cs.execute();
    cs.close();
    return true;
  }

  public String[] showOnlineTransactions(Connection connection) throws SQLException {

    ArrayList<String> results = new ArrayList<>();
    String sql = "SELECT * FROM onlineTransaction";
    Statement statement = connection.createStatement();
    ResultSet rs = statement.executeQuery(sql);
    while (rs.next()) {
      int transacID = rs.getInt("transacID");
      Date transacDate = rs.getDate("transacDate");
      Time transacTime = rs.getTime("transacTime");
      int cardPaymentID = rs.getInt("CardPaymentID");
      String rowResult = "transacID: " + Integer.toString(transacID) + "     " +
              "transacDate: " + transacDate.toString() + "     " + "transacTime: " +
              transacTime.toString() + "     "  +
              "CardPaymentID: " + Integer.toString(cardPaymentID);
      results.add(rowResult);
    }
    rs.close();
    String[] realResultType = new String[results.size()];
    realResultType = results.toArray(realResultType);
    return realResultType;
  }

  public String[] showOfflineTransactions(Connection connection) throws SQLException {
    ArrayList<String> results = new ArrayList<>();
    String sql = "SELECT * FROM physicalTransaction";
    Statement statement = connection.createStatement();
    ResultSet rs = statement.executeQuery(sql);
    while (rs.next()) {
      int transacID = rs.getInt("transacID");
      Date transacDate = rs.getDate("transacDate");
      Time transacTime = rs.getTime("transacTime");
      int cardPaymentID = rs.getInt("CardPaymentID");
      int cashID = rs.getInt("CashID");
      String rowResult = "transacID: " + Integer.toString(transacID) + "     " +
              "transacDate: " + transacDate.toString() + "     " + "transacTime: " +
              transacTime.toString() + "     " + "CardPaymentID: " +
              Integer.toString(cardPaymentID) + "     " + "CashID: " + Integer.toString(cashID);
      results.add(rowResult);
    }
    rs.close();
    String[] realResultType = new String[results.size()];
    realResultType = results.toArray(realResultType);
    return realResultType;
  }

  public String[] showOnlineSaleItems(Connection connection) throws SQLException {
    ArrayList<String> results = new ArrayList<>();
    String sql = "SELECT * FROM itemWithOnlineTransac";
    Statement statement = connection.createStatement();
    ResultSet rs = statement.executeQuery(sql);
    while (rs.next()) {
      int onlineItemsID = rs.getInt("onlineItemsID");
      int transacID = rs.getInt("transacID");
      int ItemID = rs.getInt("ItemID");
      int ItemAmount = rs.getInt("ItemAmount");
      String rowResult = "onlineItemsID: " + Integer.toString(onlineItemsID) + "     " +
              "transacID: " + Integer.toString(transacID) + "     " + "ItemID: " +
              Integer.toString(ItemID) + "     " + "ItemAmount: " + Integer.toString(ItemAmount);
      results.add(rowResult);
    }
    rs.close();
    String[] realResultType = new String[results.size()];
    realResultType = results.toArray(realResultType);
    return realResultType;
  }

  public String[] showOfflineSaleItems(Connection connection) throws SQLException {
    ArrayList<String> results = new ArrayList<>();
    String sql = "SELECT * FROM itemWithOfflineTransac";
    Statement statement = connection.createStatement();
    ResultSet rs = statement.executeQuery(sql);
    while (rs.next()) {
      int offlineItemsID = rs.getInt("offlineItemsID");
      int transacID = rs.getInt("transacID");
      int ItemID = rs.getInt("ItemID");
      int ItemAmount = rs.getInt("ItemAmount");
      String rowResult = "offlineItemsID: " + Integer.toString(offlineItemsID) + "     " +
              "transacID: " + Integer.toString(transacID) + "     " + "ItemID: " +
              Integer.toString(ItemID) + "     " + "ItemAmount: " + Integer.toString(ItemAmount);
      results.add(rowResult);
    }
    rs.close();
    String[] realResultType = new String[results.size()];
    realResultType = results.toArray(realResultType);
    return realResultType;
  }

  public String[] showItems(Connection connection) throws SQLException {
    ArrayList<String> results = new ArrayList<>();
    String sql = "SELECT * FROM item";
    Statement statement = connection.createStatement();
    ResultSet rs = statement.executeQuery(sql);
    while (rs.next()) {
      int itemID = rs.getInt("itemID");
      String itemName = rs.getString("itemName");
      int itemCount = rs.getInt("itemCount");
      int ItemPrice= rs.getInt("ItemPrice");
      String rowResult = "itemID: " + Integer.toString(itemID) + "     " +
              "itemName: " + itemName+ "     " + "itemCount: " +
              Integer.toString(itemCount) + "     "  +
              "ItemPrice: " + Integer.toString(ItemPrice);
      results.add(rowResult);
    }
    rs.close();
    String[] realResultType = new String[results.size()];
    realResultType = results.toArray(realResultType);
    return realResultType;
  }


  public String[] showEmployees(Connection connection) throws SQLException {
    ArrayList<String> results = new ArrayList<>();
    String sql = "SELECT * FROM employee";
    Statement statement = connection.createStatement();
    ResultSet rs = statement.executeQuery(sql);
    while (rs.next()) {
      int employeeID = rs.getInt("employeeID");
      String name = rs.getString("name");
      String gender = rs.getString("gender");
      String jobDescription = rs.getString("jobDescription");
      Date employeeDate = rs.getDate("employeeDate");
      String rowResult = "employeeID: " + Integer.toString(employeeID) + "     " +
              "name: " + name+ "     " + "gender: " + gender + "     "  + "jobDescription: " +
              jobDescription + "     " +"employeeDate: " + employeeDate.toString();
      results.add(rowResult);
    }
    rs.close();
    String[] realResultType = new String[results.size()];
    realResultType = results.toArray(realResultType);
    return realResultType;
  }

  public String[] showCoupons(Connection connection) throws SQLException {
    ArrayList<String> results = new ArrayList<>();
    String sql = "SELECT * FROM coupon";
    Statement statement = connection.createStatement();
    ResultSet rs = statement.executeQuery(sql);
    while (rs.next()) {
      int couponID = rs.getInt("couponID");
      int couponPWD = rs.getInt("couponPWD");
      int couponAmount = rs.getInt("couponAmount");
      int managerID = rs.getInt("managerID");
      String rowResult = "couponID: " + Integer.toString(couponID) + "     " +
              "couponPWD: " + Integer.toString(couponPWD )+ "     " + "couponAmount: " +
              Integer.toString(couponAmount) + "     "  +
              "managerID: " + Integer.toString(managerID);
      results.add(rowResult);
    }
    rs.close();
    String[] realResultType = new String[results.size()];
    realResultType = results.toArray(realResultType);
    return realResultType;
  }
}
