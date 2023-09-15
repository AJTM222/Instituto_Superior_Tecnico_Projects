package ggc;

import ggc.exceptions.*;
import java.io.Serializable;
import java.io.IOException;
import ggc.exceptions.BadEntryException;
import java.util.Map;
import java.util.Iterator;
import java.util.TreeMap;
import java.util.HashMap;
import java.util.Collection;
import java.util.Collections;
import java.util.stream.Collectors;
import java.io.BufferedReader;
import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.io.FileReader;
import java.io.IOException;
import java.io.ObjectInputStream;
import java.io.ObjectOutputStream;
import java.text.Collator;
import java.util.Locale;
import java.util.Comparator;

/**
 * Class Warehouse implements a warehouse.
 */
public class Warehouse implements Serializable {

  /** Serial number for serialization. */
  private static final long serialVersionUID = 202109192006L;

  /** Current date. */
  private int _date = 0;

  /** Store Partners. */
  private Map<String, Partner> _partners = new TreeMap<String, Partner>(new CollatorWrapper());
  
  /** Store Products. */
  private Map<String, Product> _products = new TreeMap<String, Product>(new CollatorWrapper());

  /** Store transactions. */
  private Map<Integer, Transaction> _transactions = new TreeMap<Integer, Transaction>();

  /** Transactions id. */
  private int _id = 0;


/*IMPORT FILE---------------------------------------------------------------------------------*/

  /**
   * Import a text file used to initializa app.
   *
   * @param txtfile
   * 		 filename to be loaded.
   * @throws IOException
   * @throws BadEntryException
   * @throws DuplicatePartnerException
   * @throws UnknownPartnerException
   */
  void importFile(String txtfile) throws IOException, BadEntryException, DuplicatePartnerException, UnknownPartnerException, UnknownProductException{
    BufferedReader reader = new BufferedReader(new FileReader(txtfile));
    String s;
    while ((s = reader.readLine()) != null) {
      String line = new String(s.getBytes(), "UTF-8");
      if (line.charAt(0) == '#')
        continue;
  
      String[] fields = line.split("\\|");
      switch (fields[0]) {
      case "PARTNER" -> registerPartner( fields[1], fields[2], fields[3]);
      case "BATCH_S" -> registerBatchSimple( fields[1], fields[2], Double.parseDouble(fields[3]), Integer.parseInt(fields[4]));
      case "BATCH_M" -> registerBatchDerivative(fields[1], fields[2], Double.parseDouble(fields[3]), Integer.parseInt(fields[4]), Double.parseDouble(fields[5]), fields[6]);
      default -> throw new BadEntryException(fields[0]);
      }
    }
  }



/*DATE----------------------------------------------------------------------------------------*/

  /**
   * Return current date.
   *
   * @return current date.
   */
  public int getDate(){
    return _date;
  }

  /**
   * Used to advance the date.
   *
   * @param days
   *             days being advanced.
   * @throws InvalidDaysInputException
   */
  public void advanceDate(int days) throws InvalidDaysInputException{
	if (days <= 0 ) {
	      throw new InvalidDaysInputException(days);
	}	
    _date = _date + days;
  }

/*BALANCE------------------------------------------------------------------------------------- */

   /**
   * Returns the warehouse available balance.
   * 
   * @return warehouse's available balance.
   */
  public int availableBalance() {
    BalanceVisitor visitor = new BalanceVisitor();
    for(Transaction transaction : _transactions.values()) {
      if(transaction.getispaid()) {
        transaction.accept(visitor);
      }
    }
    return visitor.getTotalBalance();
  }

  /**
   * Returns the warehouse accounting balance.
   * 
   * @return warehouse's accounting balance.
   */
  public int accountingBalance() {
    BalanceVisitor visitor = new BalanceVisitor();
    for(Transaction t : _transactions.values()) {
      t.setdate(_date);
      t.accept(visitor);
    }
    return visitor.getTotalBalance();
  }


/*PARTNER------------------------------------------------------------------------------------- */

  /**
   * Check if the partner exists.
   *
   * @param id
   *            partner id.
   *
   * @throws UnknownPartnerException
   */
  public Partner partner(String id) throws UnknownPartnerException{
    Partner partner = _partners.get(id);
    if(partner == null){
      throw new UnknownPartnerException(id);
    }
    return partner;
  }

  /**
   * Register a partner.
   *
   * @param id
   * 		partner id.
   * @param name
   * 		partner name.
   * @param address
   *             partner address.
   *             
   * @throws DuplicatePartnerException
   */
  public void registerPartner(String id, String name, String address) throws DuplicatePartnerException{
    if (_partners.containsKey(id)){
      throw new DuplicatePartnerException(id);
    }
    Partner newpartner = new Partner(id, name, address);
    _partners.put(id, newpartner);
    for (Product product: _products.values()) {
      product.registerObserver(newpartner);
    }
  }

  /**
   * Return all the Partners as an unmodifiable collection.
   * 
   * @return a collection with all Partners.
   */
  public Collection<Partner> partners() {
    return Collections.unmodifiableCollection(_partners.values());
  }
  
  /**
   * Activates/Deactivates a Partners's specific product notifications.
   * 
   * @param idpartner
   *          partner ID.
   * @param idproduct
   *          product ID.
   * @throws UnknownPartnerException
   * @throws UnknownProductException
   */
  public void toggleProductNotifications(String idpartner, String idproduct) throws UnknownPartnerException, UnknownProductException {
    Partner partner = partner(idpartner);
    Product product = product(idproduct);
    if (product.getObservers().get(partner) == true) {
      product.removeObserver(partner);
    } else {
      product.registerObserver(partner);
    }
  }

  public Collection<Transaction> lookupPaymentsByPartner(String id) throws UnknownPartnerException {
    Partner partner = partner(id);
    return Collections.unmodifiableCollection(partner.getPartnerSales().stream().filter(s->s.getispaid()).collect(Collectors.toList()));
  }

/*PRODUCT------------------------------------------------------------------------------------- */

  /**
   * Check if the product exists.
   *
   * @param id
   *            product id.
   *
   * @throws UnknownProductException
   */
  public Product product(String id) throws UnknownProductException{
    Product product = _products.get(id);
    if(product == null){
      throw new UnknownProductException(id);
    }
    return product;
  }

  /**
   * Register a simple product.
   *
   * @param id
   *            product id.
   * @param price
   *            product price.
   * @param amount
   *            product amount.
   */
  public void registerSimpleProduct(String id, double price, int amount){
    Product newproduct = new Product(id, price, amount);
    _products.put(id, newproduct);
    for (Partner partner: _partners.values()) {
    	newproduct.registerObserver(partner);
    }
  }   

  /**
   * Register a derivative product.
   *
   * @param id
   *            product id.
   * @param price
   *            product price.
   * @param amount
   *            product amount.
   * @param aggravation
   * 		product aggravation.
   */
  public void registerDerivativeProduct(String id, double price, int amount, double aggravation){
    Product newproduct = new DerivativeProduct(id, price, amount, aggravation);
    _products.put(id, newproduct);
    for (Partner partner: _partners.values()) {
    	newproduct.registerObserver(partner);
    }
  }  

  /**
   * Return all the Products as an unmodifiable collection.
   * 
   * @return a collection with all Products.
   */
  public Collection<Product> products() {
    return Collections.unmodifiableCollection(_products.values());
  }
  
  /**
   * Register a simple batch.
   *
   * @param idproduct
   *            product id.
   * @param idpartner
   *            partner id.
   * @param price
   *            batch price.
   * @param stock
   *            product stock.
   *
   * @throws UnknownPartnerException
   */
  public void registerBatchSimple(String idproduct, String idpartner, double price ,int stock) throws UnknownPartnerException{
    if(!_partners.containsKey(idpartner)){
      throw new UnknownPartnerException(idpartner);
    }
    if(!_products.containsKey(idproduct)){
      registerSimpleProduct(idproduct, price, stock);
    }
    else{
      Product product = _products.get(idproduct);
      product.increaseAmount(stock, price);
      if(price > product.getmaxprice()){
        product.setmaxprice(price);
      }
    }
    Product p = _products.get(idproduct);
    Batch newbatch = new Batch(idproduct,idpartner, price, stock);
    p.addBatch(newbatch);
    Partner partner = _partners.get(idpartner);
    partner.addBatch(newbatch);
  }
  
  /**
   * Register a derivative batch.
   *
   * @param idproduct
   *            product id.
   * @param idpartner
   *            partner id.
   * @param price
   *            batch price.
   * @param stock
   *            product stock.
   * @param aggravation
   * 		aggravation related to derivate the product.
   * @param receipt
   *		igredients needed to make the derivative product.
   * @throws UnknownPartnerException
   */
  public void registerBatchDerivative(String idproduct, String idpartner, double price ,int stock, double aggravation ,String receipt) throws UnknownPartnerException , UnknownProductException{
    if(!_partners.containsKey(idpartner)){
      throw new UnknownPartnerException(idpartner);
    }
    if(!_products.containsKey(idproduct)){
      registerDerivativeProduct(idproduct, price, stock, aggravation);
      DerivativeProduct product = (DerivativeProduct) _products.get(idproduct);
      String split[] = receipt.split("#");

      for(String member : split){
        String fields[] = member.split(":");
        if(!existsProduct(fields[0])){
          throw new UnknownProductException(fields[0]);
        }
        product.CreateReceipt(fields[0],Integer.parseInt(fields[1]), _products.get(fields[0]));
      }
    }
    else{
      Product product = _products.get(idproduct);
      product.increaseAmount(stock, price);
      if(price > product.getmaxprice()){
        product.setmaxprice(price);
      }
    }

    Batch newbatch = new Batch(idproduct,idpartner, price, stock);
    Product p = _products.get(idproduct);
    p.addBatch(newbatch);
    Partner partner = partner(idpartner);
    partner.addBatch(newbatch);
  }

  /**
   * Checks if the product exists.
   *
   * @param id
   *            product id.
   *
   * @return true if he exists.
   */
  public boolean existsProduct(String id){
    return _products.containsKey(id);
  }
  
  /**
   * Create a new batch from a product registered before.
   *
   * @param idproduct
   *            product id.
   * @param idpartner
   *            partner id.
   * @param price
   *            batch price.
   * @param stock
   *            product stock.
   * @throws UnknownPartnerException
   */
  public void createKnownProductBatch(String idproduct, String idpartner, double price ,int stock) throws UnknownPartnerException{
    Product product = _products.get(idproduct);
    product.increaseAmount(stock, price);
    if(price > product.getmaxprice()){
      product.setmaxprice(price);
    }
    Batch newbatch = new Batch(idproduct,idpartner, price, stock);
    product.addBatch(newbatch);
    Partner partner = partner(idpartner);
    partner.addBatch(newbatch);
  }

    /**
     * Return all the Batches of a certain Product as an unmodifiable collection.
     * 
     * @param idproduct
     *            product's id
     * 
     * @return a collection with all Product's Batches.
    */
    public Collection<Batch> batchesByProduct(String idproduct) throws UnknownProductException{
      if(!_products.containsKey(idproduct)){
        throw new UnknownProductException(idproduct);
      }
      Product product = product(idproduct);
      return product.batches();
    }

    /**
     * Return all the Batches of a certain Partner as an unmodifiable collection.
     * 
     * @param idpartner
     *            Partner's id
     * 
     * @return a collection with all Partener's Batches.
    */
    public Collection<Batch> batchesByPartner(String idpartner) throws UnknownPartnerException{
      if(!_partners.containsKey(idpartner)){
        throw new UnknownPartnerException(idpartner);
      }
      Partner partner = partner(idpartner);
      return partner.batches();
    }


/*TRANSACTION------------------------------------------------------------------------------------- */

  /**
   * Check if the transaction exists.
   *
   * @param id
   *            transaction id.
   *
   * @throws UnknownTransactionException
   */
  public Transaction transaction(int id) throws UnknownTransactionException{
    Transaction transaction = _transactions.get(id);
    if(transaction == null){
      throw new UnknownTransactionException(id);
    }
    transaction.setdate(getDate());
    return transaction;
  }

/*ACQUISITION------------------------------------------------------------------------------------- */
  
  /**
   * Register an acquisition.
   *
   * @param idproduct
   *            product id.
   * @param idpartner
   *            partner id.
   * @param amount
   *            amount of the product.
   * @param price
   *            acquisition price.
   * @throws UnknownPartnerException
   */
  public void registerAcquisiton(String partnerid, String productid, int amount, double price) throws UnknownPartnerException{
    Partner partner = partner(partnerid);
    Acquisition acquisition = new Acquisition( _id ,partnerid, productid, amount);
    acquisition.setPaymentDate(_date);
    acquisition.setBasePrice(price * amount);
    partner.addAcquisition(acquisition);
    _transactions.put(_id++, acquisition);
  }
  
  /**
   * Return all the acquisitions made by a partner.
   * 
   * @param idpartner
   *            partner's id
   * @throws UnknownPartnerException
   * @return a collection with all Partner's acquisitions.
   */
  public Collection<Acquisition> acquisitionsByPartner(String idpartner) throws UnknownPartnerException{
    Partner partner = partner(idpartner);
    return partner.getPartnerAcquisitions();
  }

/*BREAKDOWN-------------------------------------------------------------------------------------------- */
  
  /**
   * Register a breakdown.
   *
   * @param idproduct
   *            product id.
   * @param idpartner
   *            partner id.
   * @param amount
   *            amount of the product.          
   * @throws UnknownPartnerException
   * @throws NotEnoughProductException
   * @throws UnknownProductException
   * @throws NotValidProductException
   */
  public void registerBreakdown(String partnerid, String productid, int amount) throws UnknownPartnerException, NotEnoughProductException, UnknownProductException{
    Partner partner = partner(partnerid);
    Product product = product(productid);
    if(amount > product.getamount()){
      throw new NotEnoughProductException(productid, amount, product.getamount());
    }
    try{
      Breakdown breakdown = product.registerbreakdown(partner, amount, _id);
      breakdown.setPaymentDate(_date);
      _transactions.put(_id++, breakdown);
      partner.addBreakdown(breakdown);
    }
    catch(NotValidProductException e){}
  }
  
  /**
   * Return all the sales made by a partner.
   * 
   * @param idpartner
   *            partner's id
   * @throws UnknownPartnerException 
   * @return a collection with all Partner's sales.
   */
  public Collection<Transaction> salesByPartner(String idpartner) throws UnknownPartnerException{
    Partner partner = partner(idpartner);
    return partner.getPartnerSales();
  }

/*SALE-------------------------------------------------------------------------------------------- */
  
  /**
   * Register a sale.
   *
   * @param partnerid
   *            partner id.
   * @param payday
   *            day the sale is paid.
   * @param productid
   *            product id.
   * @param amount
   *            amount of the product.          
   * @throws UnknownPartnerException
   * @throws NotEnoughProductException
   * @throws UnknownProductException
   */
  public void registerSale(String partnerid, int payday ,String productid, int amount) throws UnknownPartnerException, NotEnoughProductException, UnknownProductException{
    Partner partner = partner(partnerid);
    Product product = product(productid);
    if(product.hasAmount(amount)){
      double price = product.sell(amount, partner);
      Sale sale = new Sale(_id, partner, product, payday, amount);
      sale.setdate(_date);
      sale.setBasePrice(price);
      partner.addSale(sale);
      _transactions.put(_id++, sale);
    }
  }
  
  /**
   * Pay sale.
   *
   * @param id
   *            transaction id.        
   * @throws UnknownTransactionException.
   */
  public void paySale(int id) throws UnknownTransactionException {
    if(!_transactions.containsKey(id)){
      throw new UnknownTransactionException(id);
    }
    Transaction transaction = _transactions.get(id);
    if (transaction.getispaid() == false){
      transaction.setdate(_date);
      transaction.pay();
    }
  }

}
