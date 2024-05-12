package model;

public class Category extends Stowable {
   private String name;
   private String description;

   protected Category() {
      this.name = "";
      this.description = "";
   }

   protected Category(String name, String desc) {
      this.name = name;
      this.description = desc;
   }

   public String getDescription() {
      return this.description;
   }

   public void setDescription(String description) {
      if (this.insertedDB) {
         try {
            Acount.getInstance().getDAO().updateCategoryDescription(this, description);
         } catch (Exception var3) {
            System.out.println("Category.setDescription(): category description not saved in DB" + var3.getMessage());
         }
      }

      this.description = description;
   }

   public String getName() {
      return this.name;
   }

   public void setName(String name) {
      if (this.insertedDB) {
         try {
            Acount.getInstance().getDAO().updateCategoryName(this, name);
         } catch (Exception var3) {
            System.out.println(var3.getMessage());
         }
      }

      this.name = name;
   }
}
