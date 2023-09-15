package ggc;

import java.io.Serializable;
import java.lang.Math;
import java.util.List;

import ggc.Acquisition;
import ggc.PartnerRank;
import ggc.Transaction;

import java.util.ArrayList;
import java.util.Collections;
import java.util.Collection;

/* Class Partner contains information about the partners of the warehouse. Each partner
 * is identified by an id represented by a String. */

public class Partner implements BatchObserver{

    /** Class serial number. */
    private static final long serialVersionUID = 202110271722L;

    /** Partner's rank. */
    private PartnerRank _rank = new Normal(this);

    /** Partner's rank */
    private int _points = 0;

    /** Partner's id */
    private String _id;

    /** Partner's name */
    private String _name;

    /** Partner's address */
    private String _address;

    /** Partner's total of sales */
    private double _salestotal = 0;

    /** Partner's total of acquisitions*/
    private double _acquisitionstotal = 0;

    /** Partner's total paid*/
    private double _paidtotal = 0;
    
    /** Partner's chosen means of notification*/
    private NotificationDeliveryMode _deliveryMode;

    /** Store Partner Batches. */
    private ArrayList<Batch> _batches = new ArrayList<Batch>();
    
    /** List that contains Partner's unread notifications*/
    private List<Notification> _notifications = new ArrayList<Notification>();

    /** List that contains Partner's Acquisitions */
    private List<Acquisition> _acquisitions = new ArrayList<Acquisition>();
    
    /** List that contains Partner's Sales */
    private List<Transaction> _sales = new ArrayList<Transaction>();
    
    /** 
     * Create a partner with default delivery notification system.
     *
     * @param id
     * 		partner id.
     * @param name
     * 		partner name.
     * @param address
     *             partner address.             
     */
    public Partner(String id, String name, String address){
        _id = id;
        _name = name;
        _address = address;
        _deliveryMode = new DefaultDeliveryMode();
    }
    
    /** 
     * Create a partner.
     *
     * @param id
     * 		partner id.
     * @param name
     * 		partner name.
     * @param address
     *             partner address.
     * @param mode
     *             partner chosen delivery mode.             
     */
     public Partner(String id, String name, String address, NotificationDeliveryMode mode){
        _id = id;
        _name = name;
        _address = address;
        _deliveryMode = mode;
    }
    
    /**
     * @return Partner's id.
     */
    public String getid(){
        return _id;
    }
    
    /**
     * @return Partner's name.
     */
    public String getname(){
        return _name;
    }
    
    /**
     * @return Partner's address.
     */
    public String getaddress(){
        return _address;
    }

    /**
     * @return Partner's total acquisitions.
     */
    public Double getacquisitionstotal(){
        return _acquisitionstotal;
    }
    
    /**
     * @return Partner's total sales.
     */
    public Double getsalestotal(){
        return _salestotal;
    }
    
    /**
     * @return Partner's total paid.
     */
    public Double getpaidtotal(){
        return _paidtotal;
    }

    /**
     * Returns the partner's rank.
     * 
     * @return the partner's current rank
     */
    public PartnerRank getrank(){
        return _rank;
    }

    /**
    * @return the partne's points.
    */
    public int getpoints(){
        return _points;
    }

    /**
    * Updates partner's rank.
    * 
    * @param rank
    *          partner's new rank.
    */
    public void setrank(PartnerRank rank){
        _rank = rank;
    }

    /**
     * Updates partner's points.
     * 
     * @param points 
     *          partner's new points
     */
    public void setpoints(int points){
        _points = points;
    }

    /**
     * Updates partner's acquisitionstotal.
     * 
     * @param acquisitionstotal 
     *          partner's new acquisitionstotal
     */
    public void setacquisitionstotal(double acquisitionstotal){
        _acquisitionstotal = acquisitionstotal;
    }

    /**
     * @param newbatch
     *		      batch to be added to warehouse.
     */
    public void addBatch(Batch newbatch){
        _batches.add(newbatch);
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
     * @return Partner's current delivery mode.
     */
    public NotificationDeliveryMode getDeliveryMode(){
        return _deliveryMode;
    }
    
    /**
     * Adds an acquisition to partner's acquisition list.
     * Also updates total acquisition base prices.
     * 
     * @param acquisition
     *          acquisition being added to list of partner's acquisition.
     */
    public void addAcquisition(Acquisition acquisition){
        _acquisitions.add(acquisition);
        _acquisitionstotal = _acquisitionstotal + acquisition.getBasePrice();
    }

    /**
     * Adds a breakdown to partner's sales list.
     * Also updates total sale base prices.
     * 
     * @param breakdown
     *          breakdown being added to list of partner's sales.
     */
    public void addBreakdown(Breakdown breakdown){
        _sales.add(breakdown);
        _rank.paybreakdown(breakdown);
    }

    /**
     * Adds a sale to partner's sales list.
     * Also updates total sale base prices.
     * 
     * @param sale
     *          sale being added to list of partner's sales.
     */
    public void addSale(Sale sale){
        _sales.add(sale);
        _salestotal = _salestotal + sale.getBasePrice();
    }

    /**
     *Returns all the Partner's Acquisitions as an unmodifiable collection.
     *
     * @return a collection with the partner's acquisitions.
     */
    public Collection<Acquisition> getPartnerAcquisitions(){
        return Collections.unmodifiableCollection(_acquisitions);
    }
    
    /**
     *Returns all the Partner's Sales as an unmodifiable collection.
     *
     * @return a collection with the partner's sales.
     */
    public Collection<Transaction> getPartnerSales(){
        return Collections.unmodifiableCollection(_sales);
    }

    /**
     * removes a batch
     * 
     * @param batch
     *    batch to be removed
     */
    public void removeBatch(Batch batch){
        _batches.remove(batch);
    }
    
    /**
     * Update partner.
     *
     * @param notification
     * 	notification being added.
     */
     @Override
     public void update(String type, String productId, int price){
     	_notifications.add(_deliveryMode.deliverNotification(type, productId, price));
     	}
    /**
     * @see java.lang.Object#toString()
     */
    @Override
    public String toString(){
        int a = (int)Math.round(getacquisitionstotal());
        int s = (int)Math.round(getsalestotal());
        int p = (int)Math.round(getpaidtotal());
        return getid() + "|" + getname() + "|" + getaddress() + "|" + getrank().toString() +"|"+getpoints()+"|"+ a +"|"+ s +"|"+ p;
    }

    public void paySale(Sale sale){
        _rank.pay(sale);
        _paidtotal = _paidtotal + sale.getTotalPrice();
    }

    /**
     * Method to print Partner and its notifications and clear time
     *
     * @return partner's toString and their notifications
     */
     public String showPartner(){
     	String aux = toString();
     	for (Notification n: _notifications){
         	aux += '\n' + n.toString();
        }
     	_notifications.clear();
     	return aux;
     }

}
     
