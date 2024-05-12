package model;

import java.time.LocalDate;
import javafx.scene.image.Image;

public class Charge extends Stowable {
   private int id;
   private String name;
   private String description;
   private double cost;
   private int units;
   private Image scanImage;
   private LocalDate date;
   private Category category;

   protected Charge(String name, String description, double cost, int units, Image scanImage, LocalDate date, Category category) {
      this.name = name;
      this.description = description;
      this.cost = cost;
      this.units = units;
      this.date = date;
      this.scanImage = scanImage;
      this.category = category;
      this.insertedDB = false;
   }

   public int getId() {
      return this.id;
   }

   protected void setId(int id) {
      this.id = id;
   }

   public String getName() {
      return this.name;
   }

   public void setName(String name) {
      if (this.insertedDB) {
         try {
            Acount.getInstance().getDAO().updateChargeName(this, name);
         } catch (Exception var3) {
            System.out.println("Charge.setName(): charge name not saved in DB");
         }
      }

      this.name = name;
   }

   public String getDescription() {
      return this.description;
   }

   public void setDescription(String description) {
      if (this.insertedDB) {
         try {
            Acount.getInstance().getDAO().updateChargeDescription(this, description);
         } catch (Exception var3) {
            System.out.println("Charge.setDescription(): charge description not saved in DB");
         }
      }

      this.description = description;
   }

   public LocalDate getDate() {
      return this.date;
   }

   public void setDate(LocalDate date) {
      if (this.insertedDB) {
         try {
            Acount.getInstance().getDAO().updateChargeDate(this, date);
         } catch (Exception var3) {
            System.out.println("Charge.setDate(): charge date not saved in DB");
         }
      }

      this.date = date;
   }

   public double getCost() {
      return this.cost;
   }

   public void setCost(double cost) {
      if (this.insertedDB) {
         try {
            Acount.getInstance().getDAO().updateChargeCost(this, cost);
         } catch (Exception var4) {
            System.out.println("Charge.setUnits(): charge units not saved in DB");
         }
      }

      this.units = this.units;
   }

   public int getUnits() {
      return this.units;
   }

   public void setUnits(int units) {
      if (this.insertedDB) {
         try {
            Acount.getInstance().getDAO().updateChargeUnits(this, units);
         } catch (Exception var3) {
            System.out.println("Charge.setUnits(): charge units not saved in DB");
         }
      }

      this.units = units;
   }

   public Image getImageScan() {
      return this.scanImage;
   }

   public void setImageScan(Image imageScan) {
      if (this.insertedDB) {
         try {
            Acount.getInstance().getDAO().updateChargeImageScan(this, imageScan);
         } catch (Exception var3) {
            System.out.println("Charge.setImageScan(): charge imageSanc not saved in DB");
         }
      }

      this.scanImage = this.scanImage;
   }

   public Category getCategory() {
      return this.category;
   }

   public void setCategory(Category category) {
      if (this.insertedDB) {
         try {
            Acount.getInstance().getDAO().updateChargeCategory(this, category);
         } catch (Exception var3) {
            System.out.println("Charge.setCategory(): charge category not saved in DB");
         }
      }

      this.category = category;
   }
}
