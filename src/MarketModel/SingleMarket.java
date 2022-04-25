package MarketModel;

import java.sql.CallableStatement;
import java.sql.Connection;
import java.sql.Date;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Statement;

public class SingleMarket {
  private String managerName;
  public SingleMarket(String managerName, String managerGender, Date managerDate,
                      Connection connection) throws SQLException {
    this.managerName = managerName;
    Statement statement = connection.createStatement();
    String sql = "CALL ADDMANAGER(?, ?, ?)";
    CallableStatement cs = connection.prepareCall(sql);
    cs.setString(1, this.managerName);
    cs.setString(2, managerGender);
    cs.setDate(3, managerDate);
    cs.execute();
  }

  public Integer showPassword(Connection connection) throws SQLException {
    int password = -1;
    Statement statement = connection.createStatement();
    String sql = "SELECT managerID FROM manager";
    ResultSet rs = statement.executeQuery(sql);

    while (rs.next()) {
      password = rs.getInt("managerID");
    }
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
    return username;
  }

  public boolean makeTransaction(Connection connection) throws SQLException {
    Statement statement = connection.createStatement();
    String sql;
    sql = "";
    ResultSet rs =statement.executeQuery(sql);
    return true;
  }

  public boolean storageChange() {
    return true;
  }

  public boolean addEmployee(String nameIN, String genderIN, String description,
                             Date employedDateIN, Connection connection) throws SQLException {
    String sql = "CALL ADDEMPLOYEE(?, ?, ?, ?)";
    CallableStatement cs = connection.prepareCall(sql);
    cs.setString(1, nameIN);
    cs.setString(2, genderIN);
    cs.setString(3, description);
    cs.setDate(4, employedDateIN);
    cs.execute();
    return true;
  }

  public boolean deleteEmployee(String name, int employID, Connection connection) {
    return true;
  }

  public int findEmployeeID(String name, Connection connection) {
    return 0;
  }

  
}
