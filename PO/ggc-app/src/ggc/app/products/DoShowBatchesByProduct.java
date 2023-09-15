package ggc.app.products;

import pt.tecnico.uilib.menus.Command;
import pt.tecnico.uilib.menus.CommandException;
import ggc.WarehouseManager;
import ggc.exceptions.UnknownProductException;
import ggc.app.exceptions.UnknownProductKeyException;

/**
 * Show all products.
 */
class DoShowBatchesByProduct extends Command<WarehouseManager> {

  DoShowBatchesByProduct(WarehouseManager receiver) {
    super(Label.SHOW_BATCHES_BY_PRODUCT, receiver);
    addStringField("productid", Prompt.productKey());
  }

  @Override
  public final void execute() throws CommandException {
    try{
      for (var batch : _receiver.batchesByProduct(stringField("productid"))) {
        _display.popup(batch.toString());
      }
      _display.display();
    }
    catch(UnknownProductException e){
      throw new UnknownProductKeyException(e.getId());
    }
  }

}
