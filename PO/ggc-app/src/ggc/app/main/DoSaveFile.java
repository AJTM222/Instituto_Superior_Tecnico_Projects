package ggc.app.main;

import pt.tecnico.uilib.menus.Command;
import pt.tecnico.uilib.menus.CommandException;
import ggc.WarehouseManager;
import ggc.exceptions.MissingFileAssociationException;
import java.io.IOException;
import java.io.FileNotFoundException;
import pt.tecnico.uilib.forms.Form;

/**
 * Save current state to file under current name (if unnamed, query for name).
 */
class DoSaveFile extends Command<WarehouseManager> {

  /** @param receiver */
  DoSaveFile(WarehouseManager receiver) {
    super(Label.SAVE, receiver);
  }

  @Override
  public final void execute() throws CommandException {
    try {
      _receiver.save();
    } 
    catch (MissingFileAssociationException e) {
      try{
        Form newFileName = new Form();
        _receiver.saveAs(newFileName.requestString(Prompt.newSaveAs()));
      }
      catch(MissingFileAssociationException | IOException ufe){
        ufe.printStackTrace();
      }
    }
    catch(IOException e){
      e.printStackTrace();
    }
  }

}