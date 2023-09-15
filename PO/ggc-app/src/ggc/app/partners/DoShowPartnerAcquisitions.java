package ggc.app.partners;

import pt.tecnico.uilib.menus.Command;
import pt.tecnico.uilib.menus.CommandException;
import ggc.WarehouseManager;
import ggc.app.exceptions.UnknownPartnerKeyException;
import ggc.exceptions.UnknownPartnerException;

/**
 * Show all transactions for a specific partner.
 */
class DoShowPartnerAcquisitions extends Command<WarehouseManager> {

  DoShowPartnerAcquisitions(WarehouseManager receiver) {
    super(Label.SHOW_PARTNER_ACQUISITIONS, receiver);
    addStringField("partnerid", Prompt.partnerKey());
  }

  @Override
  public void execute() throws CommandException {
    try{
      for (var acquisitions : _receiver.acquisitionsByPartner(stringField("partnerid"))) {
        _display.popup(acquisitions.toString());
      }
      _display.display();
    }
    catch(UnknownPartnerException e){
      throw new UnknownPartnerKeyException(e.getId());
    }
  }


}