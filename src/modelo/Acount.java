package model;

import java.io.IOException;
import java.time.LocalDate;
import java.util.Collections;
import java.util.HashMap;
import java.util.List;
import javafx.scene.image.Image;

public class Acount {
   private User activeUser;
   private HashMap<String, User> users;
   private static Acount ACOUNT = null;
   private AcountDAO acountDAO;

   private void initDAO() throws AcountDAOException {
      this.acountDAO = new AcountDAO(this);
      this.users = this.acountDAO.getUsersDB();
   }

   private Acount() throws AcountDAOException, IOException {
      this.initDAO();
   }

   public static Acount getInstance() throws AcountDAOException, IOException {
      if (ACOUNT == null) {
         Class var0 = Acount.class;
         synchronized(Acount.class) {
            if (ACOUNT == null) {
               ACOUNT = new Acount();
               return ACOUNT;
            }
         }
      }

      return ACOUNT;
   }

   public Acount clone() throws CloneNotSupportedException {
      throw new CloneNotSupportedException("Club implements singleton. It is not possible to clone");
   }

   public User getLoggedUser() {
      return this.activeUser;
   }

   public boolean existsLogin(String login) {
      return this.users.get(login) != null;
   }

   public boolean logInUserByCredentials(String login, String password) throws AcountDAOException {
      User user = (User)this.users.get(login);
      if (user != null && user.chekCredentials(login, password)) {
         this.activeUser = user;
         this.acountDAO.getUserCategoriesDB(this.activeUser.getNickName());
         return true;
      } else {
         this.activeUser = null;
         return false;
      }
   }

   public boolean logOutUser() {
      if (this.activeUser == null) {
         return false;
      } else {
         this.activeUser = null;
         return true;
      }
   }

   public List<Category> getUserCategories() throws AcountDAOException {
      return this.activeUser == null ? null : Collections.unmodifiableList(this.acountDAO.getUserCategoriesDB(this.activeUser.getNickName()));
   }

   public List<Charge> getUserCharges() throws AcountDAOException {
      return this.activeUser == null ? null : Collections.unmodifiableList(this.acountDAO.getUserChargesDB(this.activeUser.getNickName()));
   }

   protected AcountDAO getDAO() {
      return this.acountDAO;
   }

   public boolean registerUser(String name, String surname, String email, String login, String password, Image image, LocalDate date) throws AcountDAOException {
      User user = new User(name, surname, email, login, password, image, date);
      if (this.acountDAO.insertUser(user)) {
         this.users.put(login, user);
         return true;
      } else {
         return false;
      }
   }

   protected boolean registerUser(User member) throws AcountDAOException {
      if (this.acountDAO.insertUser(member)) {
         this.users.put(member.getNickName(), member);
         return true;
      } else {
         return false;
      }
   }

   public boolean registerCategory(String name, String description) throws AcountDAOException {
      if (this.activeUser == null) {
         return false;
      } else {
         Category category = new Category(name, description);
         return this.acountDAO.insertCategory(category, this.activeUser.getNickName());
      }
   }

   protected boolean registerCategory(Category category) throws AcountDAOException {
      return this.acountDAO.insertCategory(category, this.activeUser.getNickName());
   }

   public boolean removeCategory(Category category) throws AcountDAOException {
      if (this.activeUser == null) {
         return false;
      } else {
         return this.acountDAO.deleteCategory(category, this.activeUser.getNickName());
      }
   }

   public boolean registerCharge(String name, String description, double cost, int units, Image scanImage, LocalDate date, Category category) throws AcountDAOException {
      if (this.activeUser == null) {
         return false;
      } else {
         Charge charge = new Charge(name, description, cost, units, scanImage, date, category);
         return this.acountDAO.insertCharge(charge, this.activeUser.getNickName());
      }
   }

   protected boolean registerCharge(Charge charge) throws AcountDAOException {
      return this.acountDAO.insertCharge(charge, this.activeUser.getNickName());
   }

   public boolean removeCharge(Charge charge) throws AcountDAOException {
      if (this.activeUser == null) {
         return false;
      } else {
         return this.acountDAO.deleteCharge(charge);
      }
   }

   public void addSimpleData() {
      try {
         this.acountDAO.addDataTest();
      } catch (Exception var2) {
         System.out.println("Club.addDataTest(): data not added in DB. " + var2.getMessage());
      }

   }
}
