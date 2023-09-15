package ggc.app.main;

import pt.tecnico.uilib.menus.Command;
import pt.tecnico.uilib.menus.CommandException;
import ggc.WarehouseManager;
import ggc.exceptions.UnavailableFileException;
import ggc.app.exceptions.FileOpenFailedException;
import java.io.IOException;


/**
 * Open existing saved state.
 */
class DoOpenFile extends Command<WarehouseManager> {

  /** @param receiver */
  DoOpenFile(WarehouseManager receiver) {
    super(Label.OPEN, receiver);
    addStringField("fileName", Prompt.openFile());
  }

  @Override
  public final void execute() throws CommandException {
    
    try{
      _receiver.load(stringField("fileName"));

    } catch (UnavailableFileException  ufe) {
      throw new FileOpenFailedException(ufe.getFilename());
    }
    catch(IOException | ClassNotFoundException e){}
  }

}
