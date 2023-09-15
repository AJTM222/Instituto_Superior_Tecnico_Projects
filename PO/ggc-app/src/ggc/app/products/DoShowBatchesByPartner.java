package ggc.app.products;

import pt.tecnico.uilib.menus.Command;
import pt.tecnico.uilib.menus.CommandException;
import ggc.WarehouseManager;
import ggc.exceptions.UnknownPartnerException;
import ggc.app.exceptions.UnknownPartnerKeyException;

/**
 * Show batches supplied by partner.
 */
class DoShowBatchesByPartner extends Command<WarehouseManager> {

  DoShowBatchesByPartner(WarehouseManager receiver) {
    super(Label.SHOW_BATCHES_SUPPLIED_BY_PARTNER, receiver);
    addStringField("partnerid", Prompt.partnerKey());
  }

  @Override
  public final void execute() throws CommandException {
    try{
      for (var batch : _receiver.batchesByPartner(stringField("partnerid"))) {
        _display.popup(batch.toString());
      }
      _display.display();
    }
    catch(UnknownPartnerException e){
      throw new UnknownPartnerKeyException(e.getId());
    }
  }

}
