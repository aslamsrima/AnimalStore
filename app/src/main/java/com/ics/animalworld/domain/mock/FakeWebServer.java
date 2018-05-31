/*
 * Copyright (c) 2017. http://hiteshsahu.com- All Rights Reserved
 * Unauthorized copying of this file, via any medium is strictly prohibited
 * If you use or distribute this project then you MUST ADD A COPY OF LICENCE
 * along with the project.
 *  Written by Hitesh Sahu <hiteshkrsahu@Gmail.com>, 2017.
 */

package com.ics.animalworld.domain.mock;

import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;
import com.ics.animalworld.model.CenterRepository;
import com.ics.animalworld.model.entities.Animals;
import com.ics.animalworld.model.entities.Product;
import com.ics.animalworld.model.entities.ProductCategoryModel;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.Map;
import java.util.concurrent.ConcurrentHashMap;

/*
 * This class serve as fake server and provides dummy product and category with real Image Urls taken from flipkart
 */
public class FakeWebServer {

    private static FakeWebServer fakeServer;
    private DatabaseReference mDatabase;

    public static FakeWebServer getFakeWebServer() {

        if (null == fakeServer) {
            fakeServer = new FakeWebServer();
        }
        return fakeServer;
    }

    void initiateFakeServer() {

        addCategory();

    }

    public void addCategory() {

        ArrayList<ProductCategoryModel> listOfCategory = new ArrayList<ProductCategoryModel>();

        listOfCategory
                .add(new ProductCategoryModel(
                        "Animals",
                        "Animals Items",
                        "10%",
                        "https://cdn.pixabay.com/photo/2017/08/04/09/39/indian-cow-2579534_960_720.jpg"));

        listOfCategory
                .add(new ProductCategoryModel(
                        "Pets",
                        "Pets Items",
                        "15%",
                        "https://images8.alphacoders.com/496/496528.jpg"));

        CenterRepository.getCenterRepository().setListOfCategory(listOfCategory);
    }

    public void getAllElectronics(Map<String,Object> animal) {

        ConcurrentHashMap<String, ArrayList<Animals>> productMap = new ConcurrentHashMap<String, ArrayList<Animals>>();

        ArrayList<Animals> productlist = new ArrayList<Animals>();
        //Map<String,String> newMap =new HashMap<String,String>();
        Animals myAnimal=new Animals();
		for (Map.Entry<String,Object> entry : animal.entrySet()){
            if(entry.getValue() instanceof Object){
              //  newMap.put(entry.getKey(), (String) entry.getValue());
            }
        }

                //productlist.add((Animals) entry.getValue());
        //Get phone field and append to list



        
		 
        // Ovens
        /*productlist
                .add(new Product(
                        "Sahiwal Cow",
                        "Most common Indian breed cow",
                        "Commonly found breed in India. It gives highest avarage milk in litre per day as comapared to other",
                        "12500",
                        "3",
                        "12000",
                        "1",
                        "https://5.imimg.com/data5/RD/OA/MY-50522996/sahiwal-cow-250x250.jpg",
                        "cow_1"));
*/
        /*productlist
                .add(new Product(
                        "Solo Microwave Oven",
                        "Bajaj 1701MT 17 L Solo Microwave Oven",
                        "Explore the joys of cooking with IFB 17PM MEC1 Solo Microwave Oven. The budget-friendly appliance has several nifty features including Multi Power Levels and Speed Defrost to make cooking a fun-filled experience.",
                        "5000",
                        "10",
                        "4290",
                        "0",
                        "http://img6a.flixcart.com/image/microwave-new/z/j/p/bajaj-1701mt-400x400-imae4ty4vyzhaagz.jpeg",
                        "oven_2"));

        productlist
                .add(new Product(
                        "Solo Microwave Oven",
                        "Whirlpool MW 25 BG 25 L Grill Microwave Oven",
                        "http://img6a.flixcart.com/image/microwave-new/a/y/f/whirlpool-mw-25-bg-400x400-imaebagzstnngjqt.jpeg",
                        "5290",
                        "10",
                        "4290",
                        "0",
                        "http://img6a.flixcart.com/image/microwave-new/z/j/p/bajaj-1701mt-400x400-imae4ty4vyzhaagz.jpeg",
                        "oven_3"));

        productlist
                .add(new Product(
                        "Solo Microwave Oven",
                        "Morphy Richards 25CG 25 L Convection Microwave Oven",
                        "http://img5a.flixcart.com/image/microwave-new/v/q/y/morphy-richard-25cg-400x400-imadxecx93kb6q4f.jpeg",
                        "5300",
                        "12",
                        "4290",
                        "0",
                        "http://img6a.flixcart.com/image/microwave-new/z/j/p/bajaj-1701mt-400x400-imae4ty4vyzhaagz.jpeg",
                        "oven_4"));

        productlist
                .add(new Product(
                        "Solo Microwave Oven",
                        "IFB 25SC4 25 L Convection Microwave Oven",
                        "http://img5a.flixcart.com/image/microwave-new/v/q/y/morphy-richard-25cg-400x400-imadxecx93kb6q4f.jpeg",
                        "5190",
                        "10",
                        "4290",
                        "0",
                        "http://img6a.flixcart.com/image/microwave-new/y/k/m/ifb-25sc4-400x400-imaef2pztynvqjaf.jpeg",
                        "oven_5"));
*/
        productMap.put("Animals", productlist);

       // ArrayList<Product> tvList = new ArrayList<Product>();

        // TV
       /* tvList.add(new Product(
                "Calf Starter",
                "Amul Varden Calf Starter",
                "Sand Silica 4%, Crude Protein 18-20%, Crude Fibre 12%, Fat 2% ",
                "2500",
                "6",
                "2400",
                "20",
                "http://5.imimg.com/data5/VG/QR/MY-203796/varden-250x250.jpg",
                "tv_1"));
*/
       /* tvList.add(new Product(
                "LED 1",
                "Vu 80cm (32) HD Ready LED TV",
                "Enjoy movie night with the family on this 80cm LED TV from Vu. With an A+ grade panel, this TV renders crisp details that make what you're watching look realistic.",
                "17000",
                "12",
                "13990",
                "0",
                "http://img6a.flixcart.com/image/television/z/f/w/bpl-bpl080d51h-400x400-imaeeztqvhxbnam2.jpeg",
                "tv_2"));

        tvList.add(new Product(
                "LED 2",
                "Vu 80cm (32) HD Ready LED TV",
                "Enjoy movie night with the family on this 80cm LED TV from Vu. With an A+ grade panel, this TV renders crisp details that make what you're watching look realistic.",
                "18000",
                "12",
                "13990",
                "0",
                "http://img6a.flixcart.com/image/television/f/b/z/micromax-43x6300mhd-400x400-imaednxv8bgznhbx.jpeg",
                "tv_3"));

        tvList.add(new Product(
                "LED 3",
                "Vu 80cm (32) HD Ready LED TV",
                "Enjoy movie night with the family on this 80cm LED TV from Vu. With an A+ grade panel, this TV renders crisp details that make what you're watching look realistic.",
                "16000",
                "12",
                "13990",
                "0",
                "http://img6a.flixcart.com/image/television/a/w/z/vu-32d6545-400x400-imaebagzbpzqhmxc.jpeg",
                "tv_4"));

        tvList.add(new Product(
                "LED 4",
                "Vu 80cm (32) HD Ready LED TV",
                "Enjoy movie night with the family on this 80cm LED TV from Vu. With an A+ grade panel, this TV renders crisp details that make what you're watching look realistic.",
                "16000",
                "12",
                "13990",
                "0",
                "http://img6a.flixcart.com/image/television/s/r/t/lg-32lf550a-400x400-imae8nyvxyjds3qu.jpeg",
                "tv_5"));
*/
      //  productMap.put("Animals Foods", tvList);

      /*  productlist = new ArrayList<Product>();

        // Vaccum Cleaner
       productlist
                .add(new Product(
                        "Animal Anorexia Herbal Powder",
                        "Leveraging on our wide expertise & skills, we are renowned as one of the most profound manufacturers and exporters of Animal Anorexia Herbal Powder.",
                        " Our offered range of Anorexia Herbal Powder is appreciated by the clients due to its precise pH value, non toxicity and accurate composition.",
                        "85",
                        "1",
                        "90",
                        "10",
                        "http://4.imimg.com/data4/SX/AM/MY-2969813/anorexia-herbal-powder-500x500.jpg",
                        "v_cleaner_1"));
*/
      /*   productlist
                .add(new Product(
                        "Easy Clean Plus Hand-held ",
                        "Eureka Forbes Easy Clean Plus Hand-held Vacuum Cleaner",
                        "The Eureka Forbes Easy Clean vacuum cleaner is best for those who are looking for a machine that makes cleaning easier and is convenient to use. It is a compact and powerful machine with high suction and low power consumption.",
                        "2699",
                        "10",
                        "2566",
                        "0",
                        "http://img6a.flixcart.com/image/vacuum-cleaner/j/e/x/nova-vc-761h-plus-vacuum-cleaner-400x400-imaecmhyadgxzzpg.jpeg",
                        "v_cleaner_2"));

        productlist
                .add(new Product(
                        "Easy Clean Plus Hand-held ",
                        "Eureka Forbes Easy Clean Plus Hand-held Vacuum Cleaner",
                        "The Eureka Forbes Easy Clean vacuum cleaner is best for those who are looking for a machine that makes cleaning easier and is convenient to use. It is a compact and powerful machine with high suction and low power consumption.",
                        "2699",
                        "10",
                        "2566",
                        "0",
                        "http://img6a.flixcart.com/image/vacuum-cleaner/y/g/b/eureka-forbes-car-clean-car-clean-400x400-imae376v2kta5utj.jpeg",
                        "v_cleaner_3"));

        productlist
                .add(new Product(
                        "Easy Clean Plus Hand-held ",
                        "Eureka Forbes Easy Clean Plus Hand-held Vacuum Cleaner",
                        "The Eureka Forbes Easy Clean vacuum cleaner is best for those who are looking for a machine that makes cleaning easier and is convenient to use. It is a compact and powerful machine with high suction and low power consumption.",
                        "2699",
                        "10",
                        "2566",
                        "0",
                        "http://img5a.flixcart.com/image/vacuum-cleaner/m/y/g/sarita-115-400x400-imae9b5zhzjagykx.jpeg",
                        "v_cleaner_4"));

        productlist
                .add(new Product(
                        "Easy Clean Plus Hand-held ",
                        "Eureka Forbes Easy Clean Plus Hand-held Vacuum Cleaner",
                        "The Eureka Forbes Easy Clean vacuum cleaner is best for those who are looking for a machine that makes cleaning easier and is convenient to use. It is a compact and powerful machine with high suction and low power consumption.",
                        "2699",
                        "10",
                        "2566",
                        "0",
                        "http://img6a.flixcart.com/image/vacuum-cleaner/s/c/j/eureka-forbes-trendy-steel-trendy-steel-400x400-imae7vashkfj2hgk.jpeg",
                        "v_cleaner_5"));

*/
        //CenterRepository.getCenterRepository().setMapOfProductsInCategory(productMap);
        //productMap.put("Animals Medicine", productlist);
        //productlist = new ArrayList<Product>();
        productMap.put("Animals Accesories", productlist);



    }

    public void getAllFurnitures() {

        ConcurrentHashMap<String, ArrayList<Product>> productMap = new ConcurrentHashMap<String, ArrayList<Product>>();

        ArrayList<Product> productlist = new ArrayList<Product>();

        // Table
        productlist
                .add(new Product(
                        " Bulldog ",
                        "All Line Sitting Bulldog Puppy Figurine",
                        "Sold and Shipped from USA. More Delivery options available.Beautifully Painted With Lifelike Details.",
                        "3314",
                        "2",
                        "3500",
                        "1",
                        "https://images-na.ssl-images-amazon.com/images/I/51eYpWIIWjL.jpg",
                        "pet_1"));

     /*   productlist
                .add(new Product(
                        " Wood Coffee Table",
                        "Royal Oak Engineered Wood Coffee Table",
                        "With a contemporary design and gorgeous finish, this coffee table will be a brilliant addition to modern homes and even offices. The table has a glass table top with a floral print, and a pull-out drawer in the center.",
                        "10200",
                        "12",
                        "7000",
                        "0",
                        "http://img5a.flixcart.com/image/coffee-table/c/z/e/afr1096-sm-mango-wood-onlineshoppee-brown-400x400-imaea6c2bhwz8tns.jpeg",
                        "table_2"));

        productlist
                .add(new Product(
                        " Wood Coffee Table",
                        "Royal Oak Engineered Wood Coffee Table",
                        "With a contemporary design and gorgeous finish, this coffee table will be a brilliant addition to modern homes and even offices. The table has a glass table top with a floral print, and a pull-out drawer in the center.",
                        "10200",
                        "12",
                        "7000",
                        "0",
                        "http://img5a.flixcart.com/image/coffee-table/u/n/p/brass-table0016-rosewood-sheesham-zameerwazeer-beige-400x400-imaedwk5ksph9ut2.jpeg",
                        "table_3"));

        productlist
                .add(new Product(
                        " Wood Coffee Table",
                        "Royal Oak Engineered Wood Coffee Table",
                        "With a contemporary design and gorgeous finish, this coffee table will be a brilliant addition to modern homes and even offices. The table has a glass table top with a floral print, and a pull-out drawer in the center.",
                        "10200",
                        "12",
                        "7000",
                        "0",
                        "http://img6a.flixcart.com/image/coffee-table/v/h/h/side-tb-53-ad-particle-board-debono-acacia-dark-matt-400x400-imaecnctfgjahsnu.jpeg",
                        "table_4"));

        productlist
                .add(new Product(
                        " Wood Coffee Table",
                        "Royal Oak Engineered Wood Coffee Table",
                        "With a contemporary design and gorgeous finish, this coffee table will be a brilliant addition to modern homes and even offices. The table has a glass table top with a floral print, and a pull-out drawer in the center.",
                        "10200",
                        "12",
                        "7000",
                        "0",
                        "http://img5a.flixcart.com/image/coffee-table/c/z/e/afr1096-sm-mango-wood-onlineshoppee-brown-400x400-imaea6c2bhwz8tns.jpeg",
                        "table_5"));

        productlist
                .add(new Product(
                        " Wood Coffee Table",
                        "Royal Oak Engineered Wood Coffee Table",
                        "With a contemporary design and gorgeous finish, this coffee table will be a brilliant addition to modern homes and even offices. The table has a glass table top with a floral print, and a pull-out drawer in the center.",
                        "10200",
                        "12",
                        "7000",
                        "0",
                        "http://img5a.flixcart.com/image/coffee-table/k/y/h/1-particle-board-wood-an-wood-coffee-400x400-imae7uvzqsf4ynan.jpeg",
                        "table_6"));
*/
        productMap.put("Pets", productlist);

        productlist = new ArrayList<Product>();

        // Chair
      productlist
                .add(new Product(
                        "Royal Canin",
                        "Royal Canin Maxi Starter, 1 kg",
                        "Item Weight: 1.08 Kg. Nutritional profile which is adapted to the bitch’s high energy needs at end of gestation and during lactation",
                        "759",
                        "1",
                        "770",
                        "0",
                        "https://images-na.ssl-images-amazon.com/images/I/41lE5rA8NAL._SY450_.jpg",
                        "pet_food__1"));

     /*     productlist
                .add(new Product(
                        "Bean Bag Chair Cover",
                        "ab Homez XXXL Bean Bag Chair Cover (Without Filling)",
                        "With a contemporary design and gorgeous finish, this coffee table will be a brilliant addition to modern homes and even offices. The table has a glass table top with a floral print, and a pull-out drawer in the center.",
                        "36500",
                        "20",
                        "1200",
                        "0",
                        "http://img6a.flixcart.com/image/office-study-chair/e/f/p/flversaossblu-stainless-steel-nilkamal-400x400-imaeeptqczc5kbjg.jpeg",
                        "chair_2"));

        productlist
                .add(new Product(
                        "Bean Bag Chair Cover",
                        "ab Homez XXXL Bean Bag Chair Cover (Without Filling)",
                        "With a contemporary design and gorgeous finish, this coffee table will be a brilliant addition to modern homes and even offices. The table has a glass table top with a floral print, and a pull-out drawer in the center.",
                        "36500",
                        "20",
                        "1200",
                        "0",
                        "http://img5a.flixcart.com/image/bean-bag/7/w/b/chr-01-fab-homez-xxxl-400x400-imae9qnbfwr9vkk4.jpeg",
                        "chair_3"));

        productlist
                .add(new Product(
                        "Adiko Leatherette Office Chair",
                        "ab Homez XXXL Bean Bag Chair Cover (Without Filling)",
                        "With a contemporary design and gorgeous finish, this coffee table will be a brilliant addition to modern homes and even offices. The table has a glass table top with a floral print, and a pull-out drawer in the center.",
                        "36500",
                        "20",
                        "1200",
                        "0",
                        "http://img5a.flixcart.com/image/office-study-chair/h/z/d/adxn275-pu-leatherette-adiko-400x400-imaedpmyhzefdzgz.jpeg",
                        "chair_4"));

        productlist
                .add(new Product(
                        "Adiko Leatherette Office Chair",
                        "ab Homez XXXL Bean Bag Chair Cover (Without Filling)",
                        "With a contemporary design and gorgeous finish, this coffee table will be a brilliant addition to modern homes and even offices. The table has a glass table top with a floral print, and a pull-out drawer in the center.",
                        "36500",
                        "20",
                        "1200",
                        "0",
                        "http://img5a.flixcart.com/image/office-study-chair/h/z/d/adxn275-pu-leatherette-adiko-400x400-imaedpmyytefgvz7.jpeg",
                        "chair_5"));

        productlist
                .add(new Product(
                        "Adiko Leatherette Office Chair",
                        "ab Homez XXXL Bean Bag Chair Cover (Without Filling)",
                        "With a contemporary design and gorgeous finish, this coffee table will be a brilliant addition to modern homes and even offices. The table has a glass table top with a floral print, and a pull-out drawer in the center.",
                        "36500",
                        "20",
                        "1200",
                        "0",
                        "http://img6a.flixcart.com/image/office-study-chair/j/y/q/adpn-d021-pp-adiko-400x400-imaee2vrg9bkkxjg.jpeg",
                        "chair_6"));

        productlist
                .add(new Product(
                        "Adiko Leatherette Office Chair",
                        "ab Homez XXXL Bean Bag Chair Cover (Without Filling)",
                        "With a contemporary design and gorgeous finish, this coffee table will be a brilliant addition to modern homes and even offices. The table has a glass table top with a floral print, and a pull-out drawer in the center.",
                        "36500",
                        "20",
                        "1200",
                        "0",
                        "http://img6a.flixcart.com/image/office-study-chair/k/s/2/adxn-034-pu-leatherette-adiko-400x400-imaedpmyyyg8bdmv.jpeg",
                        "chair_7"));

        productlist
                .add(new Product(
                        "Adiko Leatherette Office Chair",
                        "ab Homez XXXL Bean Bag Chair Cover (Without Filling)",
                        "With a contemporary design and gorgeous finish, this coffee table will be a brilliant addition to modern homes and even offices. The table has a glass table top with a floral print, and a pull-out drawer in the center.",
                        "36500",
                        "20",
                        "1200",
                        "0",
                        "http://img6a.flixcart.com/image/bean-bag/t/8/n/fk0100391-star-xxxl-400x400-imae72dsb5h2r9uj.jpeg",
                        "chair_8"));

        productlist
                .add(new Product(
                        "Adiko Leatherette Office Chair",
                        "ab Homez XXXL Bean Bag Chair Cover (Without Filling)",
                        "With a contemporary design and gorgeous finish, this coffee table will be a brilliant addition to modern homes and even offices. The table has a glass table top with a floral print, and a pull-out drawer in the center.",
                        "36500",
                        "20",
                        "1200",
                        "0",
                        "http://img5a.flixcart.com/image/bean-bag/3/h/w/rydclassicgreenl-rockyard-large-400x400-imae6zfaz6qzj3jd.jpeg",
                        "chair_9"));
*/
        productMap.put("Pets Food", productlist);

        productlist = new ArrayList<Product>();

        // Chair
       productlist
                .add(new Product(
                        "Simparica Flea",
                        "Simparica Flea & Tick Chewable Tablets for Dogs",
                        "Simparica is a tasty chew for dogs that delivers new advances in fl ea, tick and mite control, helping to deliver improved client and patient satisfaction. \n" +
                                "Simparica brings a new innovative way to keep on top of fl eas and ticks by providing immediate and persistent killing activity for at least 35 days",
                        "2276",
                        "3",
                        "2500",
                        "0",
                        "https://www.vetproductsdirect.co.in/media/catalog/product/cache/373d83a1644451bb1cbf61008798968a/s/i/simparica-80mg-flea-tick-chewable-tablet-dogs-44-88-lbs-20-40-kg.jpg",
                        "pets_medicine_1"));

    /*     productlist
                .add(new Product(
                        "l Collapsible Wardrobe",
                        "Everything Imported Carbon Steel Collapsible Wardrobe",
                        "Portable Wardrobe Has Hanging Space And Shelves Which Are Very Practical And The Roll Down Cover Keeps The Dust Out",
                        "2999",
                        "20",
                        "1999",
                        "0",
                        "http://img6a.flixcart.com/image/collapsible-wardrobe/d/n/s/cb265-carbon-steel-cbeeso-dark-beige-400x400-imaefn9vha2hm9qk.jpeg",
                        "almirah_2"));

        productlist
                .add(new Product(
                        "l Collapsible Wardrobe",
                        "Everything Imported Carbon Steel Collapsible Wardrobe",
                        "Portable Wardrobe Has Hanging Space And Shelves Which Are Very Practical And The Roll Down Cover Keeps The Dust Out",
                        "2999",
                        "20",
                        "1999",
                        "0",
                        "http://img6a.flixcart.com/image/wardrobe-closet/b/v/3/srw-146-jute-pindia-blue-400x400-imaeb9g4y6tegzfn.jpeg",
                        "almirah_3"));

        productlist
                .add(new Product(
                        "l Collapsible Wardrobe",
                        "Everything Imported Carbon Steel Collapsible Wardrobe",
                        "Portable Wardrobe Has Hanging Space And Shelves Which Are Very Practical And The Roll Down Cover Keeps The Dust Out",
                        "2999",
                        "20",
                        "1999",
                        "0",
                        "http://img6a.flixcart.com/image/cupboard-almirah/y/w/q/100009052-particle-board-housefull-mahogany-400x400-imaebazkwhv64p8q.jpeg",
                        "almirah_4"));

        productlist
                .add(new Product(
                        "l Collapsible Wardrobe",
                        "Everything Imported Carbon Steel Collapsible Wardrobe",
                        "Portable Wardrobe Has Hanging Space And Shelves Which Are Very Practical And The Roll Down Cover Keeps The Dust Out",
                        "2999",
                        "20",
                        "1999",
                        "0",
                        "http://img5a.flixcart.com/image/collapsible-wardrobe/w/c/k/srw-116a-aluminium-pindia-maroon-wardrobe-400x400-imaeb9g4945dqunu.jpeg",
                        "almirah_5"));

        productlist
                .add(new Product(
                        "Metal Free Standing Wardrobe",
                        "Everything Imported Carbon Steel Collapsible Wardrobe",
                        "Portable Wardrobe Has Hanging Space And Shelves Which Are Very Practical And The Roll Down Cover Keeps The Dust Out",
                        "2999",
                        "20",
                        "1999",
                        "0",
                        "http://img6a.flixcart.com/image/wardrobe-closet/f/b/p/srw-167-jute-pindia-purple-400x400-imaeb9g4d8uvatck.jpeg",
                        "almirah_6"));
*/
       /* productMap.put("Pets Accesories", productlist);*/

        productMap.put("Pets Medicine", productlist);

        CenterRepository.getCenterRepository().setMapOfProductsInCategory(productMap);

    }

    public void getAllProducts(int productCategory) {

        if (productCategory == 0) {
			mDatabase = FirebaseDatabase.getInstance().getReference().child("Animals");
			mDatabase.addListenerForSingleValueEvent(
            new ValueEventListener() {
                @Override
                public void onDataChange(DataSnapshot dataSnapshot) {
                    getAllElectronics((Map<String,Object>) dataSnapshot.getValue());
                }

                @Override
                public void onCancelled(DatabaseError databaseError) {
                    //handle databaseError
                }
            });
            //getAllElectronics();
        } else {

            getAllFurnitures();

        }

    }

}
