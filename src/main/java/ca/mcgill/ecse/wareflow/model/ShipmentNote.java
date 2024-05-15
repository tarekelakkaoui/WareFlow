/*PLEASE DO NOT EDIT THIS CODE*/
/*This code was generated using the UMPLE 1.31.1.5860.78bb27cc6 modeling language!*/

package ca.mcgill.ecse.wareflow.model;
import java.sql.Date;

// line 63 "../../../../../WareFlow.ump"
public class ShipmentNote
{

  //------------------------
  // MEMBER VARIABLES
  //------------------------

  //ShipmentNote Attributes
  private Date date;
  private String description;

  //ShipmentNote Associations
  private ShipmentOrder order;
  private WarehouseStaff noteTaker;

  //------------------------
  // CONSTRUCTOR
  //------------------------

  public ShipmentNote(Date aDate, String aDescription, ShipmentOrder aOrder, WarehouseStaff aNoteTaker)
  {
    date = aDate;
    description = aDescription;
    boolean didAddOrder = setOrder(aOrder);
    if (!didAddOrder)
    {
      throw new RuntimeException("Unable to create shipmentNote due to order. See http://manual.umple.org?RE002ViolationofAssociationMultiplicity.html");
    }
    boolean didAddNoteTaker = setNoteTaker(aNoteTaker);
    if (!didAddNoteTaker)
    {
      throw new RuntimeException("Unable to create shipmentNote due to noteTaker. See http://manual.umple.org?RE002ViolationofAssociationMultiplicity.html");
    }
  }

  //------------------------
  // INTERFACE
  //------------------------

  public boolean setDate(Date aDate)
  {
    boolean wasSet = false;
    date = aDate;
    wasSet = true;
    return wasSet;
  }

  public boolean setDescription(String aDescription)
  {
    boolean wasSet = false;
    description = aDescription;
    wasSet = true;
    return wasSet;
  }

  public Date getDate()
  {
    return date;
  }

  public String getDescription()
  {
    return description;
  }
  /* Code from template association_GetOne */
  public ShipmentOrder getOrder()
  {
    return order;
  }
  /* Code from template association_GetOne */
  public WarehouseStaff getNoteTaker()
  {
    return noteTaker;
  }
  /* Code from template association_SetOneToMany */
  public boolean setOrder(ShipmentOrder aOrder)
  {
    boolean wasSet = false;
    if (aOrder == null)
    {
      return wasSet;
    }

    ShipmentOrder existingOrder = order;
    order = aOrder;
    if (existingOrder != null && !existingOrder.equals(aOrder))
    {
      existingOrder.removeShipmentNote(this);
    }
    order.addShipmentNote(this);
    wasSet = true;
    return wasSet;
  }
  /* Code from template association_SetOneToMany */
  public boolean setNoteTaker(WarehouseStaff aNoteTaker)
  {
    boolean wasSet = false;
    if (aNoteTaker == null)
    {
      return wasSet;
    }

    WarehouseStaff existingNoteTaker = noteTaker;
    noteTaker = aNoteTaker;
    if (existingNoteTaker != null && !existingNoteTaker.equals(aNoteTaker))
    {
      existingNoteTaker.removeShipmentNote(this);
    }
    noteTaker.addShipmentNote(this);
    wasSet = true;
    return wasSet;
  }

  public void delete()
  {
    ShipmentOrder placeholderOrder = order;
    this.order = null;
    if(placeholderOrder != null)
    {
      placeholderOrder.removeShipmentNote(this);
    }
    WarehouseStaff placeholderNoteTaker = noteTaker;
    this.noteTaker = null;
    if(placeholderNoteTaker != null)
    {
      placeholderNoteTaker.removeShipmentNote(this);
    }
  }


  public String toString()
  {
    return super.toString() + "["+
            "description" + ":" + getDescription()+ "]" + System.getProperties().getProperty("line.separator") +
            "  " + "date" + "=" + (getDate() != null ? !getDate().equals(this)  ? getDate().toString().replaceAll("  ","    ") : "this" : "null") + System.getProperties().getProperty("line.separator") +
            "  " + "order = "+(getOrder()!=null?Integer.toHexString(System.identityHashCode(getOrder())):"null") + System.getProperties().getProperty("line.separator") +
            "  " + "noteTaker = "+(getNoteTaker()!=null?Integer.toHexString(System.identityHashCode(getNoteTaker())):"null");
  }
}