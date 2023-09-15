package ggc;

import java.io.Serializable;
import java.lang.Math;
import java.util.Map;

import ggc.Batch;

import java.util.HashMap;
import java.util.Collections;
import java.util.ArrayList;
import java.util.Iterator;
import java.util.Collection;
import java.util.Collections;
import java.util.stream.Collectors;
import ggc.exceptions.NotEnoughProductException;
import ggc.exceptions.NotValidProductException;

public class Product implements ObservableBatch{

    /** Class serial number. */
    private static final long serialVersionUID = 202110271722L;
    
    /** Product's id. */
    private String _id;
    
    /** Product's max price. */
    private double _maxprice = 0 ;
    
    /** Product's amount. */
    private int _amount;

    /** Array containing observers who want to be notified*/
    private Map<BatchObserver, Boolean> _observers = new HashMap<BatchObserver, Boolean>();

    /** Store Product Batches. */
    private ArrayList<Batch> _batches = new ArrayList<Batch>();
    
    /**
     * Create a product.
     *
     * @param id
     *            product id.
     * @param price
     *            product price.
     * @param amount
     *            product amount.
     */
    public Product(String id, double maxprice, int amount){
        _id = id;
        _maxprice = maxprice;
        _amount = amount;
    }
    
    /**
     * @return products's id
     */
    public String getid(){
        return _id;
    }
    
    /**
     * @return products's price
     */
    public double getmaxprice(){
        return _maxprice;
    }

    /**
     * @return products's amount
     */
    public int getamount(){
        return _amount;
    }
    
    /**
     * @return number associated with simple products,
     */
    public int getN(){
        return 5;
    }
    
    /**
     * @param maxprice
     *		      price of the product.
     */
    public void setmaxprice(double maxprice){
        _maxprice = maxprice;
    }

    /**
     * @param amount
     *		      amount of the product.
     */
    public void setamount(int amount){
        _amount = amount;
    }
    
    /**
     * @param amount
     *		      amount of the product.
     * @param price
     *		      price of the product.
     */
    public void increaseAmount(int amount, double price){
        _amount += amount;
        if (_amount == amount){
            notifyObservers("NEW", price);
        }
    }
    
    /**
     * @param newbatch
     *		      batch to be added to warehouse.
     */
    public void addBatch(Batch newbatch){
        double lowestPrice = 0;
        Iterator<Batch> iterator = _batches.iterator();
        while (iterator.hasNext()) {
        
            Batch b = iterator.next();
       	    if (lowestPrice == 0){
       		    lowestPrice = b.getprice();
       	    }
       	    else if (b.getprice() < lowestPrice){
       		    lowestPrice = b.getprice();
       	    }
        }
        if(newbatch.getprice() < lowestPrice){
            notifyObservers("BARGAIN", newbatch.getprice());
        }
        _batches.add(newbatch);
    }
    
    /**
     * Registers an Observer to be notified about this product's actions.
     *
     * @param observer
     *          Observer who wants to be notified about the product's actions.
     */
    @Override
    public void registerObserver(BatchObserver observer){
	    _observers.put(observer, true);
    }

    /**
     * Removes an Observer as interested about this product's actions.
     *
     * @param observer
     *          Observer being removed as interested in the  product.
     */
    @Override
    public void removeObserver(BatchObserver observer){
    	_observers.put(observer, false);
    }

    /**
     * Notify all observers when there is an action related to a product.
     * 
     * @param type
     *          can be either NEW or BARGAIN.
     * @param price
     *		 price of the product that generates the notification. 
     */
    @Override
    public void notifyObservers(String type, double price){
    	int p = (int)Math.round(price);
        for (Map.Entry<BatchObserver, Boolean> observer : _observers.entrySet()) {
            if (observer.getValue() == true) {
                observer.getKey().update(type, _id, p);
            }
        }
    }

     /**
      *Returns all the observers as an unmodifiable map.
      *
      * @return a collection with all Product observers.
      */
    public Map<BatchObserver, Boolean> getObservers(){
        return Collections.unmodifiableMap(_observers);
    }

    /**
     * Return all the Batches as an unmodifiable collection.
     *
     * @return a collection with all Batches.
     */
    public Collection<Batch> batches(){
        Collections.sort(_batches);
        return Collections.unmodifiableCollection(_batches);
    }

    /**
    * Return the cheapest batch.
    *
    * @return the cheapest batch of a product.
    */
    public Double getCheapestBatch(){
        if(_batches.isEmpty()){
            return getmaxprice();
        }
        else{
            if(_batches.size() > 1){
                Collections.sort(_batches, (Batch s1, Batch s2)->s1.getprice().compareTo(s2.getprice()));
            }
            return _batches.get(0).getprice();
        }
    }

    public double getreceipttotal(int amount){
        return 0;
    }

    public String receiptToString(int quantity){
        return "";
    }

    public void breakdown(int quantity, Partner partner){}
    
    /**
     * @param quantity
     * 	 quantity of the product that will be sold.
     * @param partner
     *   partner that requests the destruction.
     */
    public void destroy(int quantity, Partner partner){
        Collections.sort(_batches, (Batch s1, Batch s2)->s1.getprice().compareTo(s2.getprice()));
        setamount(getamount() - quantity);
        while( quantity > 0 && !_batches.isEmpty()){
            quantity = quantity - _batches.get(0).getstock();
            if(quantity >= 0){
                partner.removeBatch(_batches.get(0));
                _batches.remove(0);
            }
            else{
                _batches.get(0).setstock(-quantity);
            }
        }
    }
    
    /**
     * @param quantity
     * 	 quantity of the product that will be bought.
     * @param partnerid
     * 	 id of the selling partner.
     */
    public void buy(int quantity, String partnerid){
        Batch b = new Batch(_id, partnerid, getCheapestBatch(), quantity);
        _batches.add(b);
        setamount(quantity + getamount());
    }


    /**
     * @param amount
     * 	 amount of the product in the batch.
     * @return total price of the batch.
     */
    public double gettotalprice(int amount){
        Collections.sort(_batches, (Batch s1, Batch s2)->s1.getprice().compareTo(s2.getprice()));
        int i = 0;
        double price = 0;
        while(amount > 0){
            amount = amount - _batches.get(i).getstock();
            if( amount > 0){
                price = price + _batches.get(i).getprice() * _batches.get(i).getstock();
            }
            else{
                price = price + _batches.get(i).getprice() * (_batches.get(i).getstock() + amount);
            }
        }
        return price;
    }

    /**
     * @param  amount
     * 		 amount needed,
     * @throw NotEnoughProductException
     * @return true if the amount stored is equal or more than needed.
     */
    public boolean hasAmount(int amount) throws NotEnoughProductException{
        if(amount <= getamount()){
            return true;
        }
        else{
            throw new NotEnoughProductException(getid(), amount, getamount());
        }
    }

    /**
     * @param  amount
     * 		 amount that will be sold,
     * @param partner
     *       partner that requested the sell,
     * @return the price of the sell.
     */
    public double sell(int amount, Partner partner){
        double price = gettotalprice(amount);
        destroy(amount, partner);
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
    public Breakdown registerbreakdown(Partner partner,int amount,int id) throws NotValidProductException{
        throw new NotValidProductException(getid());
    }

    /**
     * Return all the Batches under the price given.
     * @param  price
     * 		 price to be compared,
     * @return a collection with all Batches under that price.
     */
    public Collection<Batch> batchesUnderPrice(double price){
        Collections.sort(_batches);
        return Collections.unmodifiableCollection(_batches.stream().filter(b->b.getprice() < price).collect(Collectors.toList()));
    }
    
    /**
     * @see java.lang.Object#toString()
     */
    @Override
    public String toString() {
        int p = (int)Math.round(getmaxprice());
        return getid() + "|" + p + "|" + getamount();
    }
}
