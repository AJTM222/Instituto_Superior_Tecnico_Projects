package ggc;

import ggc.exceptions.*;
import java.io.IOException;
import java.io.FileNotFoundException;
import java.io.BufferedInputStream;
import java.io.BufferedOutputStream;
import java.io.FileInputStream;
import java.io.FileOutputStream;
import java.io.ObjectInputStream;
import java.io.ObjectOutputStream;
import java.util.Collection;

/** Façade for access. */
public class WarehouseManager {

  /** Name of file storing current store. */
  private String _filename = "";

  /** The warehouse itself. */
  private Warehouse _warehouse = new Warehouse();

  /** Flag to see if warehouse has been modified since previous save. */
  private boolean _fileSaved = false;

  /** 
   * @return the warehouse's save filename. 
   */
  public String getfilename(){
    return _filename;
  }

/*IMPORT/SAVE/LOAD FILE------------------------------------------------------------------------ */

  /**
   * Saves current warehouse data in warehousemanager's associated filename.
   *
   * @throws IOException
   * @throws FileNotFoundException
   * @throws MissingFileAssociationException
   *
   */
  public void save() throws IOException, FileNotFoundException, MissingFileAssociationException {
      if(_fileSaved == false){
        if (_filename.equals("")){
          throw new MissingFileAssociationException();
        }
        ObjectOutputStream out = new ObjectOutputStream(new BufferedOutputStream(new FileOutputStream(_filename)));
        out.writeObject(_warehouse);
        out.close();
        _fileSaved = true;
      }
  }

  /**
   * Sets warehouse's filename saves current warehouse.
   *
   * @param filename
   * 		new warehousemanager filename.
   *
   * @throws MissingFileAssociationException
   * @throws IOException
   * @throws FileNotFoundException
   *
   */
  public void saveAs(String filename) throws MissingFileAssociationException, FileNotFoundException, IOException {
    _filename = filename;
    save();
  }

  /**
   * Loads warehouse data from a previous session.
   *
   * @param filename
   * 		file being opened.
   *
   * @throws UnavailableFileException
   *
   */
  public void load(String filename) throws UnavailableFileException, IOException, ClassNotFoundException {
    try{
      ObjectInputStream in = new ObjectInputStream(new BufferedInputStream(new FileInputStream(filename)));
      _warehouse = (Warehouse) in.readObject();
      _filename = filename;
      in.close();
      _fileSaved = false;
    }
    catch(FileNotFoundException e){
      throw new UnavailableFileException(filename);
    }
  }

  /**
   * Imports a text file used to initialize app.
   *
   * @param textfile
   * 		text file being imported.
   *
   * @throws ImportFileException
   *
   */
  public void importFile(String textfile) throws ImportFileException {
    try {
      _fileSaved = false;
	    _warehouse.importFile(textfile);
    } catch (IOException | BadEntryException | DuplicatePartnerException | UnknownPartnerException | UnknownProductException e) {
	    throw new ImportFileException(textfile);
    }
  }
  

/*DATE------------------------------------------------------------------------------------------------ */
 
  /**
   * Returns the warehouse's current date.
   *
   * @return warehouse current date.
   *
   */
  public int getDate(){
    return _warehouse.getDate();
  }
  

  /**
   * Advance warehouse's date.
   *
   * @param days
   *            days being advaced.
   *
   * @throws InvalidDaysInputException
   * 		if days <= 0.
   *
   */
  public void advanceDate(int days) throws InvalidDaysInputException {
    _warehouse.advanceDate(days);
    _fileSaved = false; 
  }

/*BALANCE------------------------------------------------------------------------------------- */

   /**
   * Returns the warehouse available balance.
   * 
   * @return warehouse's available balance.
   */
  public int availableBalance() {
    return _warehouse.availableBalance();
  }

  /**
   * Returns the warehouse accounting balance.
   * 
   * @return warehouse's accounting balance.
   */
  public int accountingBalance() {
    return _warehouse.accountingBalance();
  }

/*PARTNER------------------------------------------------------------------------------------------------ */
 
  /**
   * Check if the partner is registred in the warehouse.
   *
   * @param id
   *            partner id.
   *
   * @throws UnknownPartnerException
   */
  public Partner partner(String id) throws UnknownPartnerException{
    return _warehouse.partner(id);
  }
	
  /**
   * Return all the Partners registred at the warehouse as an unmodifiable collection.
   * 
   * @return a collection with all Partners in the warehouse.
   */  
  public Collection<Partner> partners() {
    return _warehouse.partners();
  }
  
  /**
   * Register a partner in warehouse.
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
    _fileSaved = false;
    _warehouse.registerPartner(id, name, address);
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
    _fileSaved = false;
    _warehouse.toggleProductNotifications(idpartner, idproduct);
  }

  public Collection<Transaction> lookupPaymentsByPartner(String id) throws UnknownPartnerException {
    return _warehouse.lookupPaymentsByPartner(id);
  }

/*PRODUCT--------------------------------------------------------------------------------------------------- */

  /**
   * Check if the product exists in the warehouse.
   *
   * @param id
   *            product id.
   *
   * @throws UnknownProductException
   */
  public Product product(String id) throws UnknownProductException{
    return _warehouse.product(id);
  }

  /**
   * Return all the Products in the warehouse as an unmodifiable collection.
   * 
   * @return a collection with all Products in the warehouse.
   */
  public Collection<Product> products() {
    return _warehouse.products();
  }
  
  /**
   * Register a simple product in the warehouse.
   *
   * @param id
   *            product id.
   * @param price
   *            product price.
   * @param amount
   *            product amount.
   */
  public void registerSimpleProduct(String id, double price, int amount) throws DuplicateProductException{
    _fileSaved = false;
    _warehouse.registerSimpleProduct(id, price, amount);
  }
  
  /**
   * Register a derivative batch in the warehouse.
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
  public void registerDerivativeProduct(String id, double price, int amount, double aggravation) throws DuplicateProductException{
    _fileSaved = false;
    _warehouse.registerDerivativeProduct(id, price, amount, aggravation);
  }
  
  /**
   * Register a simple batch in the warehouse.
   *
   * @param idproduct
   *            product id.
   * @param idpartner
   *            partner id.
   * @param price
   *            batch price per unit.
   * @param stock
   *            product stock.
   *
   * @throws UnknownPartnerException
   */
  public void registerBatchSimple(String idproduct, String idpartner, double price ,int stock) throws UnknownPartnerException{
    _fileSaved = false;
    _warehouse.registerBatchSimple(idproduct, idpartner, price , stock);
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
  public void registerBatchDerivative(String idproduct, String idpartner, double price ,int stock, double aggravation ,String receipt) throws UnknownPartnerException, UnknownProductException{
    _fileSaved = false;
    _warehouse.registerBatchDerivative(idproduct, idpartner, price, stock, aggravation, receipt);
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
    return _warehouse.existsProduct(id);
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
    _fileSaved = false;
    _warehouse.createKnownProductBatch( idproduct, idpartner, price , stock);
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
    return _warehouse.batchesByProduct(idproduct);
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
    return _warehouse.batchesByPartner(idpartner);
  }

/*TRANSACTION---------------------------------------------------------------------------------------------- */

  /**
   * Check if the transaction exists.
   *
   * @param id
   *            transaction id.
   *
   * @throws UnknownTransactionException
   */
  public Transaction transaction(int id) throws UnknownTransactionException{
    return _warehouse.transaction(id);
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
    _fileSaved = false;
    _warehouse.registerAcquisiton( partnerid,  productid,  amount,  price);
  }
  
  /**
   * Return all the acquisitions made by a partner.
   * 
   * @param idpartner
   *            partner's id
   * 
   * @throws UnknownPartnerException
   * @return a collection with all Partner's acquisitions.
   */
  public Collection<Acquisition> acquisitionsByPartner(String idpartner) throws UnknownPartnerException{
    return _warehouse.acquisitionsByPartner(idpartner);
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
   */
  public void registerBreakdown(String partnerid, String productid, int amount) throws UnknownPartnerException, NotEnoughProductException, UnknownProductException{
    _warehouse.registerBreakdown(partnerid, productid, amount);
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
    return _warehouse.salesByPartner(idpartner);
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
    _warehouse.registerSale(partnerid, payday, productid, amount);
  }
  
  /**
   * Pay sale.
   *
   * @param id
   *            transaction id.        
   * @throws UnknownTransactionException.
   */
  public void paySale(int id) throws UnknownTransactionException {
    _warehouse.paySale(id);
  }
}
