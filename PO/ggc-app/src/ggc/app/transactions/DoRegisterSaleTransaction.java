package ggc.app.transactions;

import pt.tecnico.uilib.menus.Command;
import pt.tecnico.uilib.menus.CommandException;
import ggc.WarehouseManager;
import ggc.app.exceptions.UnknownPartnerKeyException;
import ggc.exceptions.UnknownPartnerException;
import ggc.app.exceptions.UnknownProductKeyException;
import ggc.exceptions.UnknownProductException;
import ggc.app.exceptions.UnavailableProductException;
import ggc.exceptions.NotEnoughProductException;
import ggc.exceptions.UnknownPartnerException;
import ggc.app.exceptions.UnknownPartnerKeyException;
import ggc.exceptions.UnknownProductException;
import ggc.app.exceptions.UnknownProductKeyException;

/**
 * Register order.
 */
public class DoRegisterSaleTransaction extends Command<WarehouseManager> {

  public DoRegisterSaleTransaction(WarehouseManager receiver) {
    super(Label.REGISTER_SALE_TRANSACTION, receiver);
    addStringField("partnerid", Prompt.partnerKey());
    addIntegerField("date", Prompt.paymentDeadline());
    addStringField("productid", Prompt.productKey());
    addIntegerField("amount", Prompt.amount());
  }

  @Override
  public final void execute() throws CommandException {
    try{
      _receiver.registerSale(stringField("partnerid"), integerField("date"), stringField("productid"), integerField("amount"));
    }
    catch(NotEnoughProductException e){
      throw new UnavailableProductException(e.getProductid(), e.getRequested(), e.getAvailable());
    }
    catch(UnknownPartnerException e){
      throw new UnknownPartnerKeyException(e.getId());
    }
    catch(UnknownProductException e){
      throw new UnknownProductKeyException(e.getId());
    }
  }

}
