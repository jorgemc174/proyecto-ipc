package model;

import java.io.InputStream;
import java.time.LocalDate;
import java.util.regex.Matcher;
import java.util.regex.Pattern;
import javafx.scene.image.Image;

public class User extends Stowable {
   private String name;
   private String surname;
   private String email;
   private String nickName;
   private String password;
   private Image image;
   private LocalDate registerDate;

   protected User(String name, String surname, String email, String nikname, String password, Image image, LocalDate date) {
      this.name = name;
      this.surname = surname;
      this.email = email;
      this.nickName = nikname;
      this.password = password;
      if (image != null) {
         this.image = image;
      } else {
         try {
            InputStream resourceAsStream = User.class.getResourceAsStream("/avatars/default.png");
            this.image = new Image(resourceAsStream);
         } catch (NullPointerException var9) {
            this.image = null;
         }
      }

      this.registerDate = date;
      this.insertedDB = false;
   }

   public LocalDate getRegisterDate() {
      return this.registerDate;
   }

   public String getName() {
      return this.name;
   }

   public void setName(String name) {
      if (this.insertedDB) {
         try {
            Acount.getInstance().getDAO().updateNameUser(this, name);
         } catch (Exception var3) {
            System.out.println("User.setName(): user name not saved in DB");
         }
      }

      this.name = name;
   }

   public String getSurname() {
      return this.surname;
   }

   public void setSurname(String surname) {
      if (this.insertedDB) {
         try {
            Acount.getInstance().getDAO().updateSurnameUser(this, surname);
         } catch (Exception var3) {
            System.out.println("User.setSurname(): user surname not saved in DB");
         }
      }

      this.surname = surname;
   }

   public String getEmail() {
      return this.email;
   }

   public void setEmail(String email) {
      if (this.insertedDB) {
         try {
            Acount.getInstance().getDAO().updateEmailUser(this, email);
         } catch (Exception var3) {
            System.out.println("User.setEmail(): user email not saved in DB");
         }
      }

      this.email = email;
   }

   public String getNickName() {
      return this.nickName;
   }

   public String getPassword() {
      return this.password;
   }

   public void setPassword(String password) {
      if (this.insertedDB) {
         try {
            Acount.getInstance().getDAO().updatePasswordUser(this, password);
         } catch (Exception var3) {
            System.out.println("User.setPassword(): user password not saved in DB");
         }
      }

      this.password = password;
   }

   public Image getImage() {
      return this.image;
   }

   public void setImage(Image image) {
      if (this.insertedDB) {
         try {
            Acount.getInstance().getDAO().updateImageUser(this, image);
         } catch (Exception var3) {
            System.out.println("User.setImage(): user image not saved in DB");
         }
      }

      this.image = image;
   }

   public boolean checkLogin(String login) {
      return this.nickName.equals(login);
   }

   public boolean chekCredentials(String login, String password) {
      return this.nickName.equals(login) && this.password.equals(password);
   }

   public static Boolean checkEmail(String email) {
      if (email == null) {
         return false;
      } else {
         String regex = "^[\\w!#$%&'*+/=?`{|}~^-]+(?:\\.[\\w!#$%&'*+/=?`{|}~^-]+)*@(?:[a-zA-Z0-9-]+\\.)+[a-zA-Z]{2,6}$";
         Pattern pattern = Pattern.compile(regex);
         Matcher matcher = pattern.matcher(email);
         return matcher.matches();
      }
   }

   public static Boolean checkPassword(String password) {
      if (password == null) {
         return false;
      } else {
         String regex = "^(?=.*[0-9])(?=.*[a-z])(?=.*[A-Z])(?=.*[!@#$%^&+=])(?=\\S+$).{8,20}$";
         Pattern pattern = Pattern.compile(regex);
         Matcher matcher = pattern.matcher(password);
         return matcher.matches();
      }
   }

   public static Boolean checkNickName(String nickname) {
      String regex = "^[A-Za-z0-9_-]{6,15}$";
      Pattern pattern = Pattern.compile(regex);
      Matcher matcher = pattern.matcher(nickname);
      return matcher.matches();
   }
}
