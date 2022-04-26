import java.sql.*;
import java.awt.BorderLayout;
import java.awt.Color;
import java.awt.Dimension;
import java.awt.FlowLayout;
import java.awt.Point;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.awt.event.KeyEvent;
import java.awt.image.BufferedImage;
import java.io.File;
import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.Statement;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Objects;
import java.util.Scanner;
import javax.swing.BorderFactory;
import javax.swing.Box;
import javax.swing.BoxLayout;
import javax.swing.ImageIcon;
import javax.swing.JButton;
import javax.swing.JFileChooser;
import javax.swing.JFrame;
import javax.swing.JLabel;
import javax.swing.JMenu;
import javax.swing.JMenuBar;
import javax.swing.JMenuItem;
import javax.swing.JOptionPane;
import javax.swing.JPanel;
import javax.swing.JPasswordField;
import javax.swing.JScrollPane;
import javax.swing.JTextArea;
import javax.swing.JTextField;
import javax.swing.KeyStroke;
import javax.swing.UIManager;
import javax.swing.UIManager.LookAndFeelInfo;
import javax.swing.UnsupportedLookAndFeelException;
import javax.swing.filechooser.FileNameExtensionFilter;

public class UILogin {
  static final String JDBC = "com.mysql.cj.jdbc.Driver";
  static final String DB_URL = "jdbc:mysql://localhost:3306/mard";

  static String USER ="root";
  static String PWD = "Kwx202904213";

  private Connection connection;
  private static JLabel password1, label, managerNameLanel,managerGenderLabel;
  private static JTextField username, managerName,managerGender;
  private static JButton login,create;
  private static JPasswordField Password;
  JPanel panel = new JPanel();
  JFrame frame = new JFrame();
  private String password = "";
  private String UserName = "";

  public UILogin(Connection connection){
    this.connection = connection;
    panel.setLayout(null);
    frame.setTitle("LOGIN PAGE");
    frame.setLocation(new Point(500, 300));
    frame.add(panel);
    frame.setSize(new Dimension(400, 200));
    frame.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);

    try {
      String sql = "SELECT managerName FROM manager";
      Statement statement = connection.createStatement();
      ResultSet rs = statement.executeQuery(sql);
      while (rs.next()) {
        UserName = rs.getString("managerName");
      }
      rs.close();
    }
    catch (SQLException e){
      e.printStackTrace();
      this.UserName = "";
    }

    if(this.UserName.equals("")){
      createManagerPage();
    }
    else{
      loginPage();
    }

    frame.setVisible(true);
  }

  private void loginPage(){
    label = new JLabel("Username");
    label.setBounds(100, 8, 70, 20);
    panel.add(label);
    //constructing user name
    username = new JTextField();
    username.setBounds(100, 27, 193, 28);
    panel.add(username);
//    // Password Label constructor
//    password1 = new JLabel("Password");
//    password1.setBounds(100, 55, 70, 20);
//    panel.add(password1);
//    // Password TextField
//    Password = new JPasswordField();
//    Password.setBounds(100, 75, 193, 28);
//    panel.add(Password);

    // Button constructor
    login = new JButton("Login");
    login.setBounds(100, 90, 90, 25);
    login.setForeground(Color.WHITE);
    login.setBackground(Color.BLACK);
    login.addActionListener((evt-> this.loginActions()));
    panel.add(login);
  }

  private void createManagerPage(){
    //Creating a manager
    // manager Name, Gender
    managerNameLanel = new JLabel("Manager Name");
    managerNameLanel.setBounds(100, 8, 100, 20);
    panel.add(managerNameLanel);
    //constructing user name
    managerName = new JTextField();
    managerName.setBounds(100, 27, 193, 28);
    panel.add(managerName);

    managerGenderLabel = new JLabel("Gender");
    managerGenderLabel.setBounds(100, 55, 70, 20);
    panel.add(managerGenderLabel);

    //constructing user name
    managerGender = new JTextField();
    managerGender.setBounds(100, 75, 193, 28);
    panel.add(managerGender);

    // Button constructor
    create = new JButton("Login");
    create.setBounds(100, 110, 90, 25);
    create.setForeground(Color.WHITE);
    create.setBackground(Color.BLACK);
    create.addActionListener((evt-> this.createMangagerAction()));
    panel.add(create);
  }

  public void loginActions() {
    String Username = username.getText();

    //Ask database for correctness of password and usernaeme
    if (Username.equals(this.UserName) ){
      JOptionPane.showMessageDialog(null, "Login Successful");
      this.frame.setVisible(false);
      SingleMarket market = new SingleMarket(Username);
      this.loginSucceeded(market,connection);
    }
    else
      JOptionPane.showMessageDialog(null, "Username mismatch ");
  }

  public void createMangagerAction(){
    String name, gender;
    SingleMarket market;
    while(true) {
      name = managerName.getText();
      gender = managerGender.getText();
      //continue creating manager when not successfully created.
      if(name.equals("") || gender.equals("")){
        JOptionPane.showMessageDialog(null, "Unable to create manager, please retry");
        return;
      }

      JOptionPane.showMessageDialog(null, "Manager assigned with name:" + name
          + ", gender:" + gender);
      try {
        market = new SingleMarket(name, gender, connection);
        break;
      } catch (SQLException e) {
        JOptionPane.showMessageDialog(null, "Unable to create manager, please retry");
        e.printStackTrace();
        return;
      }
    }

    this.frame.setVisible(false);
    this.loginSucceeded(market,connection);
  }

  public void loginSucceeded(SingleMarket market, Connection connection){
    UIImplementation ui = new UIImplementation(market,connection);
    ui.setVisible(true);

//    ui.queryOutput("A");
  }


  public static void main(String args[]) throws ClassNotFoundException, SQLException  //static method
  {


    Scanner scan = new Scanner(System.in);
    Connection connection = null;
    Statement statement = null;
    Class.forName(JDBC);
    connection = DriverManager.getConnection(DB_URL, USER, PWD);



    UILogin login = new UILogin(connection);
  }

  public void updatePassword(String password){
    this.password = password;
  }
}
