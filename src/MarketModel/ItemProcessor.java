package MarketModel;

import java.sql.CallableStatement;
import java.sql.Connection;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Statement;
import java.util.HashMap;

public class ItemProcessor {
  HashMap<Integer, Integer> items;
  String onlineFlag;
  int totalCost;

  public ItemProcessor(String isOnline) {
    this.items = new HashMap<>();
    this.onlineFlag = isOnline;
  }

  public boolean insertItem(int itemID, int amount, Connection connection) throws SQLException {
    Statement statement = connection.createStatement();
    String sql = "CALL GETSTORAGE(" + itemID + ")";
    ResultSet rs = statement.executeQuery(sql);
    int storage = 0;
    int price = -1;
    while (rs.next()) {
      storage = rs.getInt("itemCount");
      price = rs.getInt("itemPrice");
    }

    if (storage >= amount) {
      this.items.put(itemID, amount);
      this.totalCost += price * amount;
      return true;
    } else {
      return false;
    }
  }

  public boolean removeItem(int itemID) {
    this.items.remove(itemID);
    return true;
  }

  public boolean push(int CardPayment, int CardAmount, int CashAmount, int couponID, String bank,
                      Connection connection) throws SQLException {
    if (this.onlineFlag.equals("Online")) {
      String sql0 = "CALL VALIDCOUPON(?)";
      CallableStatement cs0 = connection.prepareCall(sql0);
      cs0.setInt(1, couponID);
      cs0.execute();
      ResultSet rs0 = cs0.getResultSet();
      int validflagAmount = -1;
      while(rs0.next()) {
        validflagAmount = rs0.getInt("couponAmount");
      }
      if (validflagAmount != -1) {
        this.totalCost -= validflagAmount;
      }

      if (CardAmount < this.totalCost ) {
        throw new SQLException();
      }

      String sql = "CALL CARDPAYMENT(?, ?, ?)";
      CallableStatement cs = connection.prepareCall(sql);
      cs.setInt(1, CardPayment);
      cs.setInt(2, CardAmount);
      cs.setString(3, bank);
      cs.execute();

      Statement statement0 = connection.createStatement();
      String sql2 = "SELECT MAX(cardID) FROM card";
      ResultSet rs2 = statement0.executeQuery(sql2);
      int cardID = -1;
      while(rs2.next()) {
        cardID = rs2.getInt("cardID");
      }
      rs2.close();

      String sql3 = "CALL "

      Statement statement1 = connection.createStatement();
      String sql1 = "SELECT MAX(transacID) FROM onlineTransaction";
      ResultSet rs1 = statement1.executeQuery(sql1);
      int transacID = -1;
      while (rs1.next()) {
        transacID = rs1.getInt("transacID");
      }
      rs1.close();
      if (transacID == -1) {
        throw new SQLException();
      }


      for (int itemID : this.items.keySet()) {

      }

    } else {

    }
    return true;
  }
}
