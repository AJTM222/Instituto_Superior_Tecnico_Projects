package ggc.app.lookups;

import pt.tecnico.uilib.menus.Command;
import pt.tecnico.uilib.menus.CommandException;
import ggc.WarehouseManager;

/**
 * Lookup products cheaper than a given price.
 */
public class DoLookupProductBatchesUnderGivenPrice extends Command<WarehouseManager> {

  public DoLookupProductBatchesUnderGivenPrice(WarehouseManager receiver) {
    super(Label.PRODUCTS_UNDER_PRICE, receiver);
    addRealField("limit", Prompt.priceLimit());
  }

  @Override
  public void execute() throws CommandException {
    for (var product : _receiver.products()) {
      for(var batch : product.batchesUnderPrice(realField("limit"))){
        _display.popup(batch.toString());
      }
    }
    _display.display();
  }

}
