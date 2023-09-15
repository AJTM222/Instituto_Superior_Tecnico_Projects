package ggc.app.transactions;

import pt.tecnico.uilib.menus.Command;
import pt.tecnico.uilib.menus.CommandException;
import ggc.WarehouseManager;
import ggc.app.exceptions.UnknownPartnerKeyException;
import ggc.exceptions.UnknownPartnerException;
import ggc.app.exceptions.UnknownProductKeyException;
import ggc.exceptions.UnknownProductException;
import pt.tecnico.uilib.forms.Form;

/**
 * Register order.
 */
public class DoRegisterAcquisitionTransaction extends Command<WarehouseManager> {

  public DoRegisterAcquisitionTransaction(WarehouseManager receiver) {
    super(Label.REGISTER_ACQUISITION_TRANSACTION, receiver);
    addStringField("partnerid", Prompt.partnerKey());
    addStringField("productid", Prompt.productKey());
    addRealField("price", Prompt.price());
    addIntegerField("amount", Prompt.amount());
  }

  @Override
  public final void execute() throws CommandException {
    try{
      Form form = new Form();
      if(!_receiver.existsProduct(stringField("productid"))){
        if(form.confirm(Prompt.addRecipe())){
          int n = form.requestInteger(Prompt.numberOfComponents());
          Double aggravation = form.requestReal(Prompt.alpha());
          String receipt = "";
          for(int i = 0;i < n; i++){
            receipt = receipt + form.requestString(Prompt.productKey()) + ":" + form.requestString(Prompt.amount()) + "#";
          }
          _receiver.registerBatchDerivative(stringField("productid"), stringField("partnerid"), realField("price") ,integerField("amount"),aggravation ,receipt);
        }
        else{
          _receiver.registerBatchSimple(stringField("productid"), stringField("partnerid"), realField("price") ,integerField("amount"));
        }
      }
      else{
      _receiver.createKnownProductBatch(stringField("productid"), stringField("partnerid"), realField("price"),integerField("amount"));
      }
      _receiver.registerAcquisiton(stringField("partnerid"), stringField("productid"), integerField("amount"), realField("price"));
    }
    catch(UnknownProductException e){
      throw new UnknownProductKeyException(e.getId());
    }
    catch(UnknownPartnerException e){
      throw new UnknownPartnerKeyException(e.getId());
    }

  }

}
