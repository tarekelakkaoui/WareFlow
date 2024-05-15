/*PLEASE DO NOT EDIT THIS CODE*/
/*This code was generated using the UMPLE 1.31.1.5860.78bb27cc6 modeling language!*/

package ca.mcgill.ecse.wareflow.model;
import java.util.*;
import java.sql.Date;

// line 25 "../../../../../WareFlow.ump"
public abstract class WarehouseStaff extends User
{

  //------------------------
  // MEMBER VARIABLES
  //------------------------

  //WarehouseStaff Associations
  private List<ShipmentNote> shipmentNotes;
  private List<ShipmentOrder> shipmentOrders;

  //------------------------
  // CONSTRUCTOR
  //------------------------

  public WarehouseStaff(String aUsername, String aName, String aPassword, String aPhoneNumber)
  {
    super(aUsername, aName, aPassword, aPhoneNumber);
    shipmentNotes = new ArrayList<ShipmentNote>();
    shipmentOrders = new ArrayList<ShipmentOrder>();
  }

  //------------------------
  // INTERFACE
  //------------------------
  /* Code from template association_GetMany */
  public ShipmentNote getShipmentNote(int index)
  {
    ShipmentNote aShipmentNote = shipmentNotes.get(index);
    return aShipmentNote;
  }

  public List<ShipmentNote> getShipmentNotes()
  {
    List<ShipmentNote> newShipmentNotes = Collections.unmodifiableList(shipmentNotes);
    return newShipmentNotes;
  }

  public int numberOfShipmentNotes()
  {
    int number = shipmentNotes.size();
    return number;
  }

  public boolean hasShipmentNotes()
  {
    boolean has = shipmentNotes.size() > 0;
    return has;
  }

  public int indexOfShipmentNote(ShipmentNote aShipmentNote)
  {
    int index = shipmentNotes.indexOf(aShipmentNote);
    return index;
  }
  /* Code from template association_GetMany */
  public ShipmentOrder getShipmentOrder(int index)
  {
    ShipmentOrder aShipmentOrder = shipmentOrders.get(index);
    return aShipmentOrder;
  }

  public List<ShipmentOrder> getShipmentOrders()
  {
    List<ShipmentOrder> newShipmentOrders = Collections.unmodifiableList(shipmentOrders);
    return newShipmentOrders;
  }

  public int numberOfShipmentOrders()
  {
    int number = shipmentOrders.size();
    return number;
  }

  public boolean hasShipmentOrders()
  {
    boolean has = shipmentOrders.size() > 0;
    return has;
  }

  public int indexOfShipmentOrder(ShipmentOrder aShipmentOrder)
  {
    int index = shipmentOrders.indexOf(aShipmentOrder);
    return index;
  }
  /* Code from template association_MinimumNumberOfMethod */
  public static int minimumNumberOfShipmentNotes()
  {
    return 0;
  }
  /* Code from template association_AddManyToOne */
  public ShipmentNote addShipmentNote(Date aDate, String aDescription, ShipmentOrder aOrder)
  {
    return new ShipmentNote(aDate, aDescription, aOrder, this);
  }

  public boolean addShipmentNote(ShipmentNote aShipmentNote)
  {
    boolean wasAdded = false;
    if (shipmentNotes.contains(aShipmentNote)) { return false; }
    WarehouseStaff existingNoteTaker = aShipmentNote.getNoteTaker();
    boolean isNewNoteTaker = existingNoteTaker != null && !this.equals(existingNoteTaker);
    if (isNewNoteTaker)
    {
      aShipmentNote.setNoteTaker(this);
    }
    else
    {
      shipmentNotes.add(aShipmentNote);
    }
    wasAdded = true;
    return wasAdded;
  }

  public boolean removeShipmentNote(ShipmentNote aShipmentNote)
  {
    boolean wasRemoved = false;
    //Unable to remove aShipmentNote, as it must always have a noteTaker
    if (!this.equals(aShipmentNote.getNoteTaker()))
    {
      shipmentNotes.remove(aShipmentNote);
      wasRemoved = true;
    }
    return wasRemoved;
  }
  /* Code from template association_AddIndexControlFunctions */
  public boolean addShipmentNoteAt(ShipmentNote aShipmentNote, int index)
  {  
    boolean wasAdded = false;
    if(addShipmentNote(aShipmentNote))
    {
      if(index < 0 ) { index = 0; }
      if(index > numberOfShipmentNotes()) { index = numberOfShipmentNotes() - 1; }
      shipmentNotes.remove(aShipmentNote);
      shipmentNotes.add(index, aShipmentNote);
      wasAdded = true;
    }
    return wasAdded;
  }

  public boolean addOrMoveShipmentNoteAt(ShipmentNote aShipmentNote, int index)
  {
    boolean wasAdded = false;
    if(shipmentNotes.contains(aShipmentNote))
    {
      if(index < 0 ) { index = 0; }
      if(index > numberOfShipmentNotes()) { index = numberOfShipmentNotes() - 1; }
      shipmentNotes.remove(aShipmentNote);
      shipmentNotes.add(index, aShipmentNote);
      wasAdded = true;
    } 
    else 
    {
      wasAdded = addShipmentNoteAt(aShipmentNote, index);
    }
    return wasAdded;
  }
  /* Code from template association_MinimumNumberOfMethod */
  public static int minimumNumberOfShipmentOrders()
  {
    return 0;
  }
  /* Code from template association_AddManyToOptionalOne */
  public boolean addShipmentOrder(ShipmentOrder aShipmentOrder)
  {
    boolean wasAdded = false;
    if (shipmentOrders.contains(aShipmentOrder)) { return false; }
    WarehouseStaff existingOrderPicker = aShipmentOrder.getOrderPicker();
    if (existingOrderPicker == null)
    {
      aShipmentOrder.setOrderPicker(this);
    }
    else if (!this.equals(existingOrderPicker))
    {
      existingOrderPicker.removeShipmentOrder(aShipmentOrder);
      addShipmentOrder(aShipmentOrder);
    }
    else
    {
      shipmentOrders.add(aShipmentOrder);
    }
    wasAdded = true;
    return wasAdded;
  }

  public boolean removeShipmentOrder(ShipmentOrder aShipmentOrder)
  {
    boolean wasRemoved = false;
    if (shipmentOrders.contains(aShipmentOrder))
    {
      shipmentOrders.remove(aShipmentOrder);
      aShipmentOrder.setOrderPicker(null);
      wasRemoved = true;
    }
    return wasRemoved;
  }
  /* Code from template association_AddIndexControlFunctions */
  public boolean addShipmentOrderAt(ShipmentOrder aShipmentOrder, int index)
  {  
    boolean wasAdded = false;
    if(addShipmentOrder(aShipmentOrder))
    {
      if(index < 0 ) { index = 0; }
      if(index > numberOfShipmentOrders()) { index = numberOfShipmentOrders() - 1; }
      shipmentOrders.remove(aShipmentOrder);
      shipmentOrders.add(index, aShipmentOrder);
      wasAdded = true;
    }
    return wasAdded;
  }

  public boolean addOrMoveShipmentOrderAt(ShipmentOrder aShipmentOrder, int index)
  {
    boolean wasAdded = false;
    if(shipmentOrders.contains(aShipmentOrder))
    {
      if(index < 0 ) { index = 0; }
      if(index > numberOfShipmentOrders()) { index = numberOfShipmentOrders() - 1; }
      shipmentOrders.remove(aShipmentOrder);
      shipmentOrders.add(index, aShipmentOrder);
      wasAdded = true;
    } 
    else 
    {
      wasAdded = addShipmentOrderAt(aShipmentOrder, index);
    }
    return wasAdded;
  }

  public void delete()
  {
    for(int i=shipmentNotes.size(); i > 0; i--)
    {
      ShipmentNote aShipmentNote = shipmentNotes.get(i - 1);
      aShipmentNote.delete();
    }
    while( !shipmentOrders.isEmpty() )
    {
      shipmentOrders.get(0).setOrderPicker(null);
    }
    super.delete();
  }

}