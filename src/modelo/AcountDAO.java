package model;

import java.awt.image.BufferedImage;
import java.io.ByteArrayOutputStream;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.io.OutputStreamWriter;
import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.time.LocalDate;
import java.time.format.DateTimeFormatter;
import java.time.format.FormatStyle;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.Iterator;
import java.util.List;
import java.util.Locale;
import javafx.embed.swing.SwingFXUtils;
import javafx.scene.image.Image;
import javax.imageio.ImageIO;
import model.sub.SqliteConnection;

public class AcountDAO {
   private String dbFilePath = "data.db";
   private String NAME = "IPC";
   private Acount acount;
   private HashMap<String, User> users;
   private ArrayList<Category> categories;
   private ArrayList<Charge> charges;

   protected AcountDAO(Acount acounts) throws AcountDAOException {
      this.acount = acounts;
      this.createDatabaseTablesIfNoExist();
   }

   protected void createDatabaseTablesIfNoExist() throws AcountDAOException {
      try {
         this.createUserTable();
         this.createCategoryTable();
         this.createChargeTable();
      } catch (SQLException var2) {
         String var10002 = this.NAME;
         throw new AcountDAOException(var10002 + "DAO: error connecting database with path:  " + this.dbFilePath + "\n" + var2.getMessage());
      }
   }

   private Connection connect() throws AcountDAOException {
      try {
         return SqliteConnection.connectSqlite(this.dbFilePath);
      } catch (SQLException var2) {
         String var10002 = this.NAME;
         throw new AcountDAOException(var10002 + "DAO: error connecting database with path:  " + this.dbFilePath + "\nCheck if the file exists." + var2.getMessage());
      }
   }

   private void createUserTable() throws SQLException {
      String sql = "CREATE TABLE IF NOT EXISTS user (name         TEXT,surname\tTEXT,email        TEXT,nickName\tTEXT,password\tTEXT,image\tBLOB,registerDay  TEXT,PRIMARY KEY(nickName)) WITHOUT ROWID;";
      Connection connection = SqliteConnection.connectSqlite(this.dbFilePath);
      PreparedStatement pstmt = connection.prepareStatement(sql);
      pstmt.executeUpdate();
      connection.close();
   }

   private void createCategoryTable() throws SQLException {
      String sql = "CREATE TABLE IF NOT EXISTS category (name             TEXT,description      TEXT,nickNameUser     TEXT,PRIMARY KEY (name, nickNameUser), FOREIGN KEY(nickNameUser)REFERENCES user (nickName )\t  ON UPDATE CASCADE\t  ON DELETE CASCADE ) WITHOUT ROWID;";
      Connection connection = SqliteConnection.connectSqlite(this.dbFilePath);
      PreparedStatement pstmt = connection.prepareStatement(sql);
      pstmt.executeUpdate();
      connection.close();
   }

   private void createChargeTable() throws SQLException {
      String sql = "CREATE TABLE IF NOT EXISTS charge (id               INTEGER,name             TEXT,description      TEXT,cost             REAL,units            INTEGER,date             TEXT,nameCategory     TEXT,nickNameUser     TEXT,image            BLOB,PRIMARY KEY (id), FOREIGN KEY(nameCategory,nickNameUser)REFERENCES category (name, nickNameUser)\t  ON UPDATE CASCADE\t  ON DELETE CASCADE );";
      Connection connection = SqliteConnection.connectSqlite(this.dbFilePath);
      PreparedStatement pstmt = connection.prepareStatement(sql);
      pstmt.executeUpdate();
      connection.close();
   }

   protected HashMap<String, User> getUsersDB() throws AcountDAOException {
      this.users = new HashMap();
      String sql = "SELECT * FROM user";

      try {
         Connection connnection = SqliteConnection.connectSqlite(this.dbFilePath);
         PreparedStatement pstmt = connnection.prepareStatement(sql);
         ResultSet resultSet = pstmt.executeQuery();

         while(resultSet.next()) {
            User user = this.buildUserFromResultSet(resultSet);
            user.setInsertedDB(Boolean.TRUE);
            this.users.put(user.getNickName(), user);
         }

         connnection.close();
         return this.users;
      } catch (SQLException var6) {
         String var10002 = this.NAME;
         throw new AcountDAOException(var10002 + "DAO: getUsersDB error. No users retrieved from DB\n" + var6.getMessage());
      }
   }

   private User buildUserFromResultSet(ResultSet resultSet) throws SQLException {
      InputStream inputStream = resultSet.getBinaryStream("image");
      Image avatar = new Image(inputStream);
      LocalDate registerday = LocalDate.parse(resultSet.getString("registerDay"), DateTimeFormatter.ofLocalizedDate(FormatStyle.SHORT).localizedBy(Locale.US));
      User member = new User(resultSet.getString("name"), resultSet.getString("surname"), resultSet.getString("email"), resultSet.getString("nickName"), resultSet.getString("password"), avatar, registerday);
      return member;
   }

   protected boolean insertUser(User user) throws AcountDAOException {
      String sql = "INSERT INTO user (name, surname, email, nickName, password, image, registerDay) VALUES(?,?,?,?,?,?,?)";

      String var10002;
      try {
         Connection connection = SqliteConnection.connectSqlite(this.dbFilePath);
         PreparedStatement pstmt = connection.prepareStatement(sql);
         pstmt.setString(1, user.getName());
         pstmt.setString(2, user.getSurname());
         pstmt.setString(3, user.getEmail());
         pstmt.setString(4, user.getNickName());
         pstmt.setString(5, user.getPassword());
         pstmt.setString(7, user.getRegisterDate().format(DateTimeFormatter.ofLocalizedDate(FormatStyle.SHORT).localizedBy(Locale.US)));
         ByteArrayOutputStream byteOutput = new ByteArrayOutputStream();
         if (user.getImage() != null) {
            ImageIO.write(SwingFXUtils.fromFXImage(user.getImage(), (BufferedImage)null), "png", byteOutput);
         }

         pstmt.setBytes(6, byteOutput.toByteArray());
         int rowCount = pstmt.executeUpdate();
         if (rowCount > 0) {
            user.setInsertedDB(Boolean.TRUE);
         }

         connection.close();
         return rowCount > 0;
      } catch (IOException var7) {
         var10002 = this.NAME;
         throw new AcountDAOException(var10002 + "DAO: insert user " + user.getNickName() + " error \n" + var7.getMessage());
      } catch (SQLException var8) {
         var10002 = this.NAME;
         throw new AcountDAOException(var10002 + "DAO: insert user " + user.getNickName() + " error\n " + var8.getMessage());
      }
   }

   protected boolean updatePasswordUser(User member, String password) throws AcountDAOException {
      try {
         Connection connection = SqliteConnection.connectSqlite(this.dbFilePath);
         String sql = "UPDATE user SET password = ? WHERE nickName = ?";
         PreparedStatement pstmt = connection.prepareStatement(sql);
         pstmt.setString(1, password);
         pstmt.setString(2, member.getNickName());
         int rowCount = pstmt.executeUpdate();
         connection.close();
         return rowCount > 0;
      } catch (SQLException var7) {
         String var10002 = this.NAME;
         throw new AcountDAOException(var10002 + "DAO: updatePasswordMember " + member.getNickName() + " error \n" + var7.getMessage());
      }
   }

   protected boolean updateNameUser(User member, String name) throws AcountDAOException {
      try {
         Connection connection = SqliteConnection.connectSqlite(this.dbFilePath);
         String sql = "UPDATE user SET name = ? WHERE nickName = ?";
         PreparedStatement pstmt = connection.prepareStatement(sql);
         pstmt.setString(1, name);
         pstmt.setString(2, member.getNickName());
         int rowCount = pstmt.executeUpdate();
         connection.close();
         return rowCount > 0;
      } catch (SQLException var7) {
         String var10002 = this.NAME;
         throw new AcountDAOException(var10002 + "DAO: updateNameMember " + member.getNickName() + " error \n" + var7.getMessage());
      }
   }

   protected boolean updateImageUser(User member, Image image) throws AcountDAOException, SQLException {
      String var10002;
      try {
         Connection connection = SqliteConnection.connectSqlite(this.dbFilePath);
         String sql = "UPDATE user SET image = ? WHERE nickname = ?";
         PreparedStatement pstmt = connection.prepareStatement(sql);
         ByteArrayOutputStream byteOutput = new ByteArrayOutputStream();
         ImageIO.write(SwingFXUtils.fromFXImage(image, (BufferedImage)null), "png", byteOutput);
         pstmt.setBytes(1, byteOutput.toByteArray());
         pstmt.setString(2, member.getNickName());
         int rowCount = pstmt.executeUpdate();
         connection.close();
         return rowCount > 0;
      } catch (SQLException var8) {
         var10002 = this.NAME;
         throw new AcountDAOException(var10002 + "DAO: updateImageMember " + member.getNickName() + " error \n" + var8.getMessage());
      } catch (IOException var9) {
         var10002 = this.NAME;
         throw new AcountDAOException(var10002 + "DAO: updateImageMember " + member.getNickName() + " error \n" + var9.getMessage());
      }
   }

   protected boolean updateSurnameUser(User member, String surname) throws AcountDAOException {
      try {
         Connection connection = SqliteConnection.connectSqlite(this.dbFilePath);
         String sql = "UPDATE user SET surname = ? WHERE nickName = ?";
         PreparedStatement pstmt = connection.prepareStatement(sql);
         pstmt.setString(1, surname);
         pstmt.setString(2, member.getNickName());
         int rowCount = pstmt.executeUpdate();
         connection.close();
         return rowCount > 0;
      } catch (SQLException var7) {
         String var10002 = this.NAME;
         throw new AcountDAOException(var10002 + "DAO: updateSurnameMember " + member.getNickName() + " error \n" + var7.getMessage());
      }
   }

   protected boolean updateEmailUser(User member, String email) throws AcountDAOException {
      try {
         Connection connection = SqliteConnection.connectSqlite(this.dbFilePath);
         String sql = "UPDATE user SET email = ? WHERE nickName = ?";
         PreparedStatement pstmt = connection.prepareStatement(sql);
         pstmt.setString(1, email);
         pstmt.setString(2, member.getNickName());
         int rowCount = pstmt.executeUpdate();
         connection.close();
         return rowCount > 0;
      } catch (SQLException var7) {
         String var10002 = this.NAME;
         throw new AcountDAOException(var10002 + "DAO: updateEmailUser " + member.getNickName() + " error \n" + var7.getMessage());
      }
   }

   protected List<Category> getUserCategoriesDB(String nickName) throws AcountDAOException {
      this.categories = new ArrayList();
      String sql = "SELECT * FROM category WHERE nickNameUser = ? ";

      try {
         Connection connnection = SqliteConnection.connectSqlite(this.dbFilePath);
         PreparedStatement pstmt = connnection.prepareStatement(sql);
         pstmt.setString(1, nickName);
         ResultSet resultSet = pstmt.executeQuery();

         while(resultSet.next()) {
            Category category = this.buildCategoryFromResultSet(resultSet);
            category.setInsertedDB(Boolean.TRUE);
            this.categories.add(category);
         }

         connnection.close();
         return this.categories;
      } catch (SQLException var7) {
         String var10002 = this.NAME;
         throw new AcountDAOException(var10002 + "DAO: getUserCategoriesDB error. No categories retrieve from DB\n" + var7.getMessage());
      }
   }

   private Category buildCategoryFromResultSet(ResultSet resultSet) throws SQLException {
      Category category = new Category(resultSet.getString("name"), resultSet.getString("description"));
      return category;
   }

   private Category getCategoryByName(String nameCategory) {
      Iterator var2 = this.categories.iterator();

      Category category;
      do {
         if (!var2.hasNext()) {
            return null;
         }

         category = (Category)var2.next();
      } while(!category.getName().equals(nameCategory));

      return category;
   }

   protected boolean updateCategoryName(Category category, String name) throws AcountDAOException {
      try {
         Connection connection = SqliteConnection.connectSqlite(this.dbFilePath);
         String sql = "UPDATE category SET name = ? WHERE name = ? AND nickName = ?";
         PreparedStatement pstmt = connection.prepareStatement(sql);
         pstmt.setString(1, name);
         pstmt.setString(2, category.getName());
         pstmt.setString(3, this.acount.getLoggedUser().getNickName());
         int rowCount = pstmt.executeUpdate();
         connection.close();
         return rowCount > 0;
      } catch (SQLException var7) {
         String var10002 = this.NAME;
         throw new AcountDAOException(var10002 + "DAO: updateCategoryName " + category.getName() + " error \n" + var7.getMessage());
      }
   }

   protected boolean updateCategoryDescription(Category category, String description) throws AcountDAOException {
      try {
         Connection connection = SqliteConnection.connectSqlite(this.dbFilePath);
         String sql = "UPDATE category SET description = ? WHERE name = ? AND nickName = ?";
         PreparedStatement pstmt = connection.prepareStatement(sql);
         pstmt.setString(1, description);
         pstmt.setString(2, category.getName());
         pstmt.setString(3, this.acount.getLoggedUser().getNickName());
         int rowCount = pstmt.executeUpdate();
         connection.close();
         return rowCount > 0;
      } catch (SQLException var7) {
         String var10002 = this.NAME;
         throw new AcountDAOException(var10002 + "DAO: updateCategoryDespcription " + category.getName() + " error \n" + var7.getMessage());
      }
   }

   protected boolean insertCategory(Category category, String nickNameUser) throws AcountDAOException {
      String sql = "INSERT INTO category (name, description, nickNameUser) VALUES(?,?,?)";

      try {
         Connection connection = SqliteConnection.connectSqlite(this.dbFilePath);
         PreparedStatement pstmt = connection.prepareStatement(sql);
         pstmt.setString(1, category.getName());
         pstmt.setString(2, category.getDescription());
         pstmt.setString(3, nickNameUser);
         int rowCount = pstmt.executeUpdate();
         if (rowCount > 0) {
            category.setInsertedDB(Boolean.TRUE);
            this.categories.add(category);
         }

         connection.close();
         return rowCount > 0;
      } catch (SQLException var7) {
         String var10002 = this.NAME;
         throw new AcountDAOException(var10002 + "DAO: insert category " + category.getName() + " error\n " + var7.getMessage());
      }
   }

   protected boolean deleteCategory(Category category, String nickNameUser) throws AcountDAOException {
      try {
         Connection connection = SqliteConnection.connectSqlite(this.dbFilePath);
         String sql = "DELETE FROM category WHERE name = ? AND nickNameUser = ?";
         PreparedStatement pstmt = connection.prepareStatement(sql);
         pstmt.setString(1, category.getName());
         pstmt.setString(2, nickNameUser);
         int rowCount = pstmt.executeUpdate();
         connection.close();
         this.categories.remove(category);
         return rowCount > 0;
      } catch (SQLException var7) {
         String var10002 = this.NAME;
         throw new AcountDAOException(var10002 + "DAO: deleteCategory  error \n" + var7.getMessage());
      }
   }

   protected ArrayList<Charge> getUserChargesDB(String nickName) throws AcountDAOException {
      this.charges = new ArrayList();
      String sql = "SELECT * FROM charge WHERE nickNameUser = ?";

      try {
         Connection connnection = SqliteConnection.connectSqlite(this.dbFilePath);
         PreparedStatement pstmt = connnection.prepareStatement(sql);
         pstmt.setString(1, nickName);
         ResultSet resultSet = pstmt.executeQuery();

         while(resultSet.next()) {
            Charge charge = this.buildChargeFromResultSet(resultSet);
            charge.setInsertedDB(Boolean.TRUE);
            this.charges.add(charge);
         }

         connnection.close();
         return this.charges;
      } catch (SQLException var7) {
         String var10002 = this.NAME;
         throw new AcountDAOException(var10002 + "DAO: getUserChargesDB error. No charges retrieve from DB\n" + var7.getMessage());
      }
   }

   private Charge buildChargeFromResultSet(ResultSet resultSet) throws SQLException {
      double cost = resultSet.getDouble("cost");
      int units = resultSet.getInt("units");
      InputStream inputStream = resultSet.getBinaryStream("image");
      Image scanImage = new Image(inputStream);
      LocalDate date = LocalDate.parse(resultSet.getString("date"), DateTimeFormatter.ofLocalizedDate(FormatStyle.SHORT).localizedBy(Locale.US));
      Category category = this.getCategoryByName(resultSet.getString("nameCategory"));
      Charge charge = new Charge(resultSet.getString("name"), resultSet.getString("description"), cost, units, scanImage, date, category);
      charge.setId(resultSet.getInt("id"));
      return charge;
   }

   protected boolean updateChargeDescription(Charge charge, String description) throws AcountDAOException {
      try {
         Connection connection = SqliteConnection.connectSqlite(this.dbFilePath);
         String sql = "UPDATE charge SET description = ? WHERE id = ?";
         PreparedStatement pstmt = connection.prepareStatement(sql);
         pstmt.setString(1, description);
         pstmt.setString(2, String.valueOf(charge.getId()));
         int rowCount = pstmt.executeUpdate();
         connection.close();
         return rowCount > 0;
      } catch (SQLException var7) {
         String var10002 = this.NAME;
         throw new AcountDAOException(var10002 + "DAO: updateChargeDescription " + charge.getName() + " error \n" + var7.getMessage());
      }
   }

   protected boolean updateChargeDate(Charge charge, LocalDate date) throws AcountDAOException {
      try {
         Connection connection = SqliteConnection.connectSqlite(this.dbFilePath);
         String sql = "UPDATE charge SET date = ? WHERE id = ?";
         PreparedStatement pstmt = connection.prepareStatement(sql);
         pstmt.setString(1, date.format(DateTimeFormatter.ofLocalizedDate(FormatStyle.SHORT).localizedBy(Locale.US)));
         pstmt.setString(2, String.valueOf(charge.getId()));
         int rowCount = pstmt.executeUpdate();
         connection.close();
         return rowCount > 0;
      } catch (SQLException var7) {
         String var10002 = this.NAME;
         throw new AcountDAOException(var10002 + "DAO: updateChargeDate " + charge.getName() + " error \n" + var7.getMessage());
      }
   }

   protected boolean updateChargeName(Charge charge, String name) throws AcountDAOException {
      try {
         Connection connection = SqliteConnection.connectSqlite(this.dbFilePath);
         String sql = "UPDATE charge SET name = ? WHERE id = ?";
         PreparedStatement pstmt = connection.prepareStatement(sql);
         pstmt.setString(1, name);
         pstmt.setString(2, String.valueOf(charge.getId()));
         int rowCount = pstmt.executeUpdate();
         connection.close();
         return rowCount > 0;
      } catch (SQLException var7) {
         String var10002 = this.NAME;
         throw new AcountDAOException(var10002 + "DAO: updateChargeName " + charge.getName() + " error \n" + var7.getMessage());
      }
   }

   protected boolean updateChargeUnits(Charge charge, int units) throws AcountDAOException {
      try {
         Connection connection = SqliteConnection.connectSqlite(this.dbFilePath);
         String sql = "UPDATE charge SET units = ? WHERE id = ?";
         PreparedStatement pstmt = connection.prepareStatement(sql);
         pstmt.setString(1, String.valueOf(units));
         pstmt.setString(2, String.valueOf(charge.getId()));
         int rowCount = pstmt.executeUpdate();
         connection.close();
         return rowCount > 0;
      } catch (SQLException var7) {
         String var10002 = this.NAME;
         throw new AcountDAOException(var10002 + "DAO: updateChargeUnits " + charge.getName() + " error \n" + var7.getMessage());
      }
   }

   protected boolean updateChargeImageScan(Charge charge, Image imageScan) throws AcountDAOException {
      String var10002;
      try {
         Connection connection = SqliteConnection.connectSqlite(this.dbFilePath);
         String sql = "UPDATE charge SET image = ? WHERE id = ?";
         PreparedStatement pstmt = connection.prepareStatement(sql);
         ByteArrayOutputStream byteOutput = new ByteArrayOutputStream();
         ImageIO.write(SwingFXUtils.fromFXImage(imageScan, (BufferedImage)null), "png", byteOutput);
         pstmt.setBytes(1, byteOutput.toByteArray());
         pstmt.setString(2, String.valueOf(charge.getId()));
         int rowCount = pstmt.executeUpdate();
         connection.close();
         return rowCount > 0;
      } catch (SQLException var8) {
         var10002 = this.NAME;
         throw new AcountDAOException(var10002 + "DAO: updateChargeImageScan " + charge.getName() + " error \n" + var8.getMessage());
      } catch (IOException var9) {
         var10002 = this.NAME;
         throw new AcountDAOException(var10002 + "DAO: updateChargeImageScan " + charge.getName() + " error \n" + var9.getMessage());
      }
   }

   protected boolean updateChargeCategory(Charge charge, Category category) throws AcountDAOException {
      try {
         Connection connection = SqliteConnection.connectSqlite(this.dbFilePath);
         String sql = "UPDATE charge SET nameCategory = ? WHERE id = ?";
         PreparedStatement pstmt = connection.prepareStatement(sql);
         pstmt.setString(1, category.getName());
         pstmt.setString(2, String.valueOf(charge.getId()));
         int rowCount = pstmt.executeUpdate();
         connection.close();
         return rowCount > 0;
      } catch (SQLException var7) {
         String var10002 = this.NAME;
         throw new AcountDAOException(var10002 + "DAO: updateChargeCategory " + charge.getName() + " error \n" + var7.getMessage());
      }
   }

   protected boolean updateChargeCost(Charge charge, double cost) throws AcountDAOException {
      try {
         Connection connection = SqliteConnection.connectSqlite(this.dbFilePath);
         String sql = "UPDATE charge SET cost = ? WHERE id = ?";
         PreparedStatement pstmt = connection.prepareStatement(sql);
         pstmt.setString(1, String.valueOf(cost));
         pstmt.setString(2, String.valueOf(charge.getId()));
         int rowCount = pstmt.executeUpdate();
         connection.close();
         return rowCount > 0;
      } catch (SQLException var8) {
         String var10002 = this.NAME;
         throw new AcountDAOException(var10002 + "DAO: updateChargeCost " + charge.getName() + " error \n" + var8.getMessage());
      }
   }

   protected boolean insertCharge(Charge charge, String nickName) throws AcountDAOException {
      String sql = "INSERT INTO charge (name, description, cost, units, date, nameCategory, nickNameUser,image ) VALUES(?,?,?,?,?,?,?,?);";

      String var10002;
      try {
         Connection connection = SqliteConnection.connectSqlite(this.dbFilePath);
         PreparedStatement pstmt = connection.prepareStatement(sql);
         pstmt.setString(1, charge.getName());
         pstmt.setString(2, charge.getDescription());
         pstmt.setString(3, String.valueOf(charge.getCost()));
         pstmt.setString(4, String.valueOf(charge.getUnits()));
         pstmt.setString(5, charge.getDate().format(DateTimeFormatter.ofLocalizedDate(FormatStyle.SHORT).localizedBy(Locale.US)));
         pstmt.setString(6, charge.getCategory().getName());
         pstmt.setString(7, nickName);
         ByteArrayOutputStream byteOutput = new ByteArrayOutputStream();
         if (charge.getImageScan() != null) {
            ImageIO.write(SwingFXUtils.fromFXImage(charge.getImageScan(), (BufferedImage)null), "png", byteOutput);
         }

         pstmt.setBytes(8, byteOutput.toByteArray());
         int rowCount = pstmt.executeUpdate();
         if (rowCount > 0) {
            charge.setInsertedDB(Boolean.TRUE);
            ResultSet generatedKeys = pstmt.getGeneratedKeys();
            charge.setId(generatedKeys.getInt(1));
         }

         connection.close();
         return rowCount > 0;
      } catch (IOException var9) {
         var10002 = this.NAME;
         throw new AcountDAOException(var10002 + "DAO: insert charge " + charge.getName() + " error \n" + var9.getMessage());
      } catch (SQLException var10) {
         var10002 = this.NAME;
         throw new AcountDAOException(var10002 + "DAO: insert charge " + charge.getName() + " error\n " + var10.getMessage());
      }
   }

   protected boolean deleteCharge(Charge charge) throws AcountDAOException {
      try {
         Connection connection = SqliteConnection.connectSqlite(this.dbFilePath);
         String sql = "DELETE FROM charge WHERE id = ?";
         PreparedStatement pstmt = connection.prepareStatement(sql);
         pstmt.setString(1, String.valueOf(charge.getId()));
         int rowCount = pstmt.executeUpdate();
         connection.close();
         return rowCount > 0;
      } catch (SQLException var6) {
         String var10002 = this.NAME;
         throw new AcountDAOException(var10002 + "DAO: deleteCharge  error \n" + var6.getMessage());
      }
   }

   public void toTextFile(String filePath) throws IOException, AcountDAOException {
      OutputStreamWriter writer = new OutputStreamWriter(new FileOutputStream(filePath), "UTF-8");
      writer.write("===================\n");
      writer.write(" Customers IN DB \n");
      writer.write("===================\n");
      writer.write("===================\n");
      writer.write("===================\n");
      writer.write(" Farmers IN DB \n");
      writer.write("===================\n");
      writer.write("===================\n");
      writer.close();
   }

   protected void addDataTest() throws AcountDAOException {
      for(int i = 1; i < 6; ++i) {
         User member = new User("User", String.valueOf(i), "666666666", "user" + i, "123456x", (Image)null, LocalDate.now());
         this.acount.registerUser(member);
      }

   }

   protected void resetInitialDataAcount() throws AcountDAOException {
      try {
         Connection connection = SqliteConnection.connectSqlite(this.dbFilePath);
         PreparedStatement pstmt = connection.prepareStatement("DELETE FROM charge");
         pstmt.executeUpdate();
         pstmt = connection.prepareStatement("DELETE FROM category");
         pstmt.executeUpdate();
         pstmt = connection.prepareStatement("DELETE FROM user");
         pstmt.executeUpdate();
         connection.close();
      } catch (SQLException var3) {
         String var10002 = this.NAME;
         throw new AcountDAOException(var10002 + "DAO: removeAllUserData error. B\n" + var3.getMessage());
      }
   }
}
