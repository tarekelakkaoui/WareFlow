/*PLEASE DO NOT EDIT THIS CODE*/
/*This code was generated using the UMPLE 1.31.1.5860.78bb27cc6 modeling language!*/

package ca.mcgill.ecse.wareflow.model;
import java.util.*;
import java.sql.Date;

// line 32 "../../../../../WareFlowPersistence.ump"
// line 69 "../../../../../WareFlow.ump"
public class ItemContainer
{

  //------------------------
  // STATIC VARIABLES
  //------------------------

  private static Map<Integer, ItemContainer> itemcontainersByContainerNumber = new HashMap<Integer, ItemContainer>();

  //------------------------
  // MEMBER VARIABLES
  //------------------------

  //ItemContainer Attributes
  private int containerNumber;
  private int areaNumber;
  private int slotNumber;
  private Date addedOnDate;

  //ItemContainer Associations
  private WareFlow wareFlow;
  private List<ShipmentOrder> shipmentOrders;
  private ItemType itemType;

  //------------------------
  // CONSTRUCTOR
  //------------------------

  public ItemContainer(int aContainerNumber, int aAreaNumber, int aSlotNumber, Date aAddedOnDate, WareFlow aWareFlow, ItemType aItemType)
  {
    areaNumber = aAreaNumber;
    slotNumber = aSlotNumber;
    addedOnDate = aAddedOnDate;
    if (!setContainerNumber(aContainerNumber))
    {
      throw new RuntimeException("Cannot create due to duplicate containerNumber. See http://manual.umple.org?RE003ViolationofUniqueness.html");
    }
    boolean didAddWareFlow = setWareFlow(aWareFlow);
    if (!didAddWareFlow)
    {
      throw new RuntimeException("Unable to create itemContainer due to wareFlow. See http://manual.umple.org?RE002ViolationofAssociationMultiplicity.html");
    }
    shipmentOrders = new ArrayList<ShipmentOrder>();
    boolean didAddItemType = setItemType(aItemType);
    if (!didAddItemType)
    {
      throw new RuntimeException("Unable to create itemContainer due to itemType. See http://manual.umple.org?RE002ViolationofAssociationMultiplicity.html");
    }
  }

  //------------------------
  // INTERFACE
  //------------------------

  public boolean setContainerNumber(int aContainerNumber)
  {
    boolean wasSet = false;
    Integer anOldContainerNumber = getContainerNumber();
    if (anOldContainerNumber != null && anOldContainerNumber.equals(aContainerNumber)) {
      return true;
    }
    if (hasWithContainerNumber(aContainerNumber)) {
      return wasSet;
    }
    containerNumber = aContainerNumber;
    wasSet = true;
    if (anOldContainerNumber != null) {
      itemcontainersByContainerNumber.remove(anOldContainerNumber);
    }
    itemcontainersByContainerNumber.put(aContainerNumber, this);
    return wasSet;
  }

  public boolean setAreaNumber(int aAreaNumber)
  {
    boolean wasSet = false;
    areaNumber = aAreaNumber;
    wasSet = true;
    return wasSet;
  }

  public boolean setSlotNumber(int aSlotNumber)
  {
    boolean wasSet = false;
    slotNumber = aSlotNumber;
    wasSet = true;
    return wasSet;
  }

  public boolean setAddedOnDate(Date aAddedOnDate)
  {
    boolean wasSet = false;
    addedOnDate = aAddedOnDate;
    wasSet = true;
    return wasSet;
  }

  public int getContainerNumber()
  {
    return containerNumber;
  }
  /* Code from template attribute_GetUnique */
  public static ItemContainer getWithContainerNumber(int aContainerNumber)
  {
    return itemcontainersByContainerNumber.get(aContainerNumber);
  }
  /* Code from template attribute_HasUnique */
  public static boolean hasWithContainerNumber(int aContainerNumber)
  {
    return getWithContainerNumber(aContainerNumber) != null;
  }

  public int getAreaNumber()
  {
    return areaNumber;
  }

  public int getSlotNumber()
  {
    return slotNumber;
  }

  public Date getAddedOnDate()
  {
    return addedOnDate;
  }
  /* Code from template association_GetOne */
  public WareFlow getWareFlow()
  {
    return wareFlow;
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
  /* Code from template association_GetOne */
  public ItemType getItemType()
  {
    return itemType;
  }
  /* Code from template association_SetOneToMany */
  public boolean setWareFlow(WareFlow aWareFlow)
  {
    boolean wasSet = false;
    if (aWareFlow == null)
    {
      return wasSet;
    }

    WareFlow existingWareFlow = wareFlow;
    wareFlow = aWareFlow;
    if (existingWareFlow != null && !existingWareFlow.equals(aWareFlow))
    {
      existingWareFlow.removeItemContainer(this);
    }
    wareFlow.addItemContainer(this);
    wasSet = true;
    return wasSet;
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
    ItemContainer existingContainer = aShipmentOrder.getContainer();
    if (existingContainer == null)
    {
      aShipmentOrder.setContainer(this);
    }
    else if (!this.equals(existingContainer))
    {
      existingContainer.removeShipmentOrder(aShipmentOrder);
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
      aShipmentOrder.setContainer(null);
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
  /* Code from template association_SetOneToMany */
  public boolean setItemType(ItemType aItemType)
  {
    boolean wasSet = false;
    if (aItemType == null)
    {
      return wasSet;
    }

    ItemType existingItemType = itemType;
    itemType = aItemType;
    if (existingItemType != null && !existingItemType.equals(aItemType))
    {
      existingItemType.removeItemContainer(this);
    }
    itemType.addItemContainer(this);
    wasSet = true;
    return wasSet;
  }

  public void delete()
  {
    itemcontainersByContainerNumber.remove(getContainerNumber());
    WareFlow placeholderWareFlow = wareFlow;
    this.wareFlow = null;
    if(placeholderWareFlow != null)
    {
      placeholderWareFlow.removeItemContainer(this);
    }
    while( !shipmentOrders.isEmpty() )
    {
      shipmentOrders.get(0).setContainer(null);
    }
    ItemType placeholderItemType = itemType;
    this.itemType = null;
    if(placeholderItemType != null)
    {
      placeholderItemType.removeItemContainer(this);
    }
  }

  // line 34 "../../../../../WareFlowPersistence.ump"
   public static  void reinitializeUniqueContainerNumber(List<ItemContainer> containers){
    itemcontainersByContainerNumber.clear();
        for (var ic : containers){
            itemcontainersByContainerNumber.put(ic.getContainerNumber(), ic);
        }
  }


  public String toString()
  {
    return super.toString() + "["+
            "containerNumber" + ":" + getContainerNumber()+ "," +
            "areaNumber" + ":" + getAreaNumber()+ "," +
            "slotNumber" + ":" + getSlotNumber()+ "]" + System.getProperties().getProperty("line.separator") +
            "  " + "addedOnDate" + "=" + (getAddedOnDate() != null ? !getAddedOnDate().equals(this)  ? getAddedOnDate().toString().replaceAll("  ","    ") : "this" : "null") + System.getProperties().getProperty("line.separator") +
            "  " + "wareFlow = "+(getWareFlow()!=null?Integer.toHexString(System.identityHashCode(getWareFlow())):"null") + System.getProperties().getProperty("line.separator") +
            "  " + "itemType = "+(getItemType()!=null?Integer.toHexString(System.identityHashCode(getItemType())):"null");
  }
}