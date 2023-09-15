package ggc.app.partners;

import pt.tecnico.uilib.menus.Command;
import pt.tecnico.uilib.menus.CommandException;
import ggc.WarehouseManager;
import ggc.app.exceptions.UnknownPartnerKeyException;
import ggc.exceptions.UnknownPartnerException;
import ggc.app.exceptions.UnknownProductKeyException;
import ggc.exceptions.UnknownProductException;

/**
 * Toggle product-related notifications.
 */
class DoToggleProductNotifications extends Command<WarehouseManager> {

  DoToggleProductNotifications(WarehouseManager receiver) {
    super(Label.TOGGLE_PRODUCT_NOTIFICATIONS, receiver);
    addStringField("idpartner", Prompt.partnerKey());
    addStringField("idproduct", Prompt.productKey());
  }

  @Override
  public void execute() throws CommandException {
    try{
      _receiver.toggleProductNotifications(stringField("idpartner"), stringField("idproduct"));
    }
    catch(UnknownPartnerException e){
      throw new UnknownPartnerKeyException(e.getId());
    }
    catch(UnknownProductException e){
      throw new UnknownProductKeyException(e.getId());
    }
  }

}
