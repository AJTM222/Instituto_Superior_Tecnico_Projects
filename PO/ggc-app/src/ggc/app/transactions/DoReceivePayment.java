package ggc.app.transactions;

import pt.tecnico.uilib.menus.Command;
import pt.tecnico.uilib.menus.CommandException;
import ggc.WarehouseManager;
import ggc.app.exceptions.UnknownTransactionKeyException;
import ggc.exceptions.UnknownTransactionException;
/**
 * Receive payment for sale transaction.
 */
public class DoReceivePayment extends Command<WarehouseManager> {

  public DoReceivePayment(WarehouseManager receiver) {
    super(Label.RECEIVE_PAYMENT, receiver);
    addIntegerField("id", Prompt.transactionKey());
  }

  @Override
  public final void execute() throws CommandException {
    try{
      _receiver.paySale(integerField("id"));
    }
    catch(UnknownTransactionException e){
      throw new UnknownTransactionKeyException(e.getId());
    }
  }

}
