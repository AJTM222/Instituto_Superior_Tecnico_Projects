package ggc;

import java.util.Map;

import ggc.Product;
import ggc.exceptions.NotEnoughProductException;

import java.util.Collection;
import java.util.TreeMap;
import java.util.HashMap;
import java.util.LinkedHashMap;

public class DerivativeProduct extends Product{

    /** Class serial number. */
    private static final long serialVersionUID = 202110271722L;
    
    /** Product's aggravation. */
    private double _aggravation;
    
    /** Product's receipt. */
    private Map<String, Integer> _receipt = new LinkedHashMap<String, Integer>();

    /** Products from the receipt */
    private Map<String, Product> _receiptproducts = new HashMap<String, Product>();

    /**
     * Create a derivative product.
     *
     * @param id
     *            product id.
     * @param price
     *            product price.
     * @param amount
     *            product amount.
     * @param aggravation
     * 		  product aggravation.
     */
    public DerivativeProduct(String id, double price, int amount, double aggravation){
        super(id, price, amount);
        _aggravation = aggravation;
    }
    
    /**
     * Create receipt for a derivative product.
     *
     * @param id
     *            igredient id.
     * @param quantity
     *            ingredient quantity needed.
     */
    public void CreateReceipt(String id, int quantity, Product product){
        _receipt.put(id, quantity);
        _receiptproducts.put(id, product);
    }
    
    /**
     * @return derivate products's aggravation
     */    
    public double getaggravation(){
        return _aggravation;
    }
    
    /**
     *
     * @param quantity
     *            quantity of the product that will be sold.
     * @param partner
     *            partner making the breakdown.
     */
    public void breakdown(int quantity, Partner partner){
        destroy(quantity, partner);
        for (Map.Entry<String,Integer> entry : _receipt.entrySet()){
            _receiptproducts.get(entry.getKey()).buy(quantity * entry.getValue(), partner.getid());
        }
    }
    
    /**
     * @param quantity
     *            quantity of the product needed.
     * @return recipe in string.
     */
    public String receiptToString(int quantity){
        String out = "";
        for (Map.Entry<String,Integer> entry : _receipt.entrySet()){
            int v = (int)Math.round(_receiptproducts.get(entry.getKey()).getCheapestBatch() * quantity * entry.getValue());
            out =out + entry.getKey() + ":" + entry.getValue() * quantity +":" + v+ "#";
        }
        return out.substring(0,out.length() -1);
    }

    /**
     * @return number associated with derivative products,
     */
    public int getN(){
        return 3;
    }    
    
    /**
     * @param amount
     * 	 amount needed.
     * @return total cost of a recipe.
     */
    public double getreceipttotal(int amount){
        double total = 0;
        for (Map.Entry<String,Integer> entry : _receipt.entrySet()){
            total = total + _receiptproducts.get(entry.getKey()).getCheapestBatch() * amount *  entry.getValue();
        }
        return total;
    }

    @Override
    public boolean hasAmount(int amount) throws NotEnoughProductException{
        if(amount <= getamount()){
            return true;
        }
        amount = amount - getamount();
        boolean result = true;
        for (Map.Entry<String,Integer> entry : _receipt.entrySet()){
            result = result && _receiptproducts.get(entry.getKey()).hasAmount(amount * entry.getValue());
        }
        return result;
    }

    public double sell(int amount, Partner partner){
        int stock = getamount();
        double price = 0;
        if(amount > getamount()){
            price = gettotalprice(getamount());
            destroy(getamount(), partner);
            double receiptprice = 0;
            for (Map.Entry<String,Integer> entry : _receipt.entrySet()){
                receiptprice = receiptprice + _receiptproducts.get(entry.getKey()).sell((amount - stock) * entry.getValue(), partner);
            }
            if(receiptprice > getmaxprice()){
                setmaxprice(((1 + getaggravation()) * receiptprice)/ (amount -stock));
            }
            price = price + (1 + getaggravation()) * receiptprice;
        }
        else{
            price = gettotalprice(amount);
            destroy(amount, partner);
        }
        return price;
    }


    /**
     * @param  partner
     *       partner that asks for the breakdown,
     * @param  amount
     * 		 amount that will be breakdown,
     * @param id
     *       id ofthe transaction,
     * @throws NotValidProductException
     * @return the price of the sell.
     */
    public Breakdown registerbreakdown(Partner partner,int amount,int id){
        double paid = 0;
        double total = gettotalprice(amount);
        double receipt = getreceipttotal(amount);
        double baseprice = total - receipt;
        if(baseprice >= 0){
            paid = baseprice;
        }
        Breakdown breakdown = new Breakdown( id ,partner, this, amount, paid);
        breakdown.setBasePrice(baseprice);
        breakdown(amount, partner);
        return breakdown;
    }


    /**
     * @see java.lang.Object#toString()
     */
    @Override
    public String toString(){
        String out = "";
        for (Map.Entry<String,Integer> entry : _receipt.entrySet()){
            out =out + entry.getKey() + ":" + entry.getValue() + "#";
        }
        int p = (int)Math.round(getmaxprice());
        out = getid() +"|" + p +"|" + getamount() +"|" + getaggravation()+ "|" + out;
        return out.substring(0,out.length() -1);
    }
}
