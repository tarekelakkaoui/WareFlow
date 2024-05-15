/*PLEASE DO NOT EDIT THIS CODE*/
/*This code was generated using the UMPLE 1.31.1.5860.78bb27cc6 modeling language!*/

package ca.mcgill.ecse.wareflow.model;
import java.util.*;
import java.sql.Date;

// line 41 "../../../../../WareFlowPersistence.ump"
// line 77 "../../../../../WareFlow.ump"
public class ItemType
{

  //------------------------
  // STATIC VARIABLES
  //------------------------

  private static Map<String, ItemType> itemtypesByName = new HashMap<String, ItemType>();

  //------------------------
  // MEMBER VARIABLES
  //------------------------

  //ItemType Attributes
  private String name;
  private int expectedLifeSpanInDays;

  //ItemType Associations
  private WareFlow wareFlow;
  private List<ItemContainer> itemContainers;

  //------------------------
  // CONSTRUCTOR
  //------------------------

  public ItemType(String aName, int aExpectedLifeSpanInDays, WareFlow aWareFlow)
  {
    expectedLifeSpanInDays = aExpectedLifeSpanInDays;
    if (!setName(aName))
    {
      throw new RuntimeException("Cannot create due to duplicate name. See http://manual.umple.org?RE003ViolationofUniqueness.html");
    }
    boolean didAddWareFlow = setWareFlow(aWareFlow);
    if (!didAddWareFlow)
    {
      throw new RuntimeException("Unable to create itemType due to wareFlow. See http://manual.umple.org?RE002ViolationofAssociationMultiplicity.html");
    }
    itemContainers = new ArrayList<ItemContainer>();
  }

  //------------------------
  // INTERFACE
  //------------------------

  public boolean setName(String aName)
  {
    boolean wasSet = false;
    String anOldName = getName();
    if (anOldName != null && anOldName.equals(aName)) {
      return true;
    }
    if (hasWithName(aName)) {
      return wasSet;
    }
    name = aName;
    wasSet = true;
    if (anOldName != null) {
      itemtypesByName.remove(anOldName);
    }
    itemtypesByName.put(aName, this);
    return wasSet;
  }

  public boolean setExpectedLifeSpanInDays(int aExpectedLifeSpanInDays)
  {
    boolean wasSet = false;
    expectedLifeSpanInDays = aExpectedLifeSpanInDays;
    wasSet = true;
    return wasSet;
  }

  public String getName()
  {
    return name;
  }
  /* Code from template attribute_GetUnique */
  public static ItemType getWithName(String aName)
  {
    return itemtypesByName.get(aName);
  }
  /* Code from template attribute_HasUnique */
  public static boolean hasWithName(String aName)
  {
    return getWithName(aName) != null;
  }

  public int getExpectedLifeSpanInDays()
  {
    return expectedLifeSpanInDays;
  }
  /* Code from template association_GetOne */
  public WareFlow getWareFlow()
  {
    return wareFlow;
  }
  /* Code from template association_GetMany */
  public ItemContainer getItemContainer(int index)
  {
    ItemContainer aItemContainer = itemContainers.get(index);
    return aItemContainer;
  }

  public List<ItemContainer> getItemContainers()
  {
    List<ItemContainer> newItemContainers = Collections.unmodifiableList(itemContainers);
    return newItemContainers;
  }

  public int numberOfItemContainers()
  {
    int number = itemContainers.size();
    return number;
  }

  public boolean hasItemContainers()
  {
    boolean has = itemContainers.size() > 0;
    return has;
  }

  public int indexOfItemContainer(ItemContainer aItemContainer)
  {
    int index = itemContainers.indexOf(aItemContainer);
    return index;
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
      existingWareFlow.removeItemType(this);
    }
    wareFlow.addItemType(this);
    wasSet = true;
    return wasSet;
  }
  /* Code from template association_MinimumNumberOfMethod */
  public static int minimumNumberOfItemContainers()
  {
    return 0;
  }
  /* Code from template association_AddManyToOne */
  public ItemContainer addItemContainer(int aContainerNumber, int aAreaNumber, int aSlotNumber, Date aAddedOnDate, WareFlow aWareFlow)
  {
    return new ItemContainer(aContainerNumber, aAreaNumber, aSlotNumber, aAddedOnDate, aWareFlow, this);
  }

  public boolean addItemContainer(ItemContainer aItemContainer)
  {
    boolean wasAdded = false;
    if (itemContainers.contains(aItemContainer)) { return false; }
    ItemType existingItemType = aItemContainer.getItemType();
    boolean isNewItemType = existingItemType != null && !this.equals(existingItemType);
    if (isNewItemType)
    {
      aItemContainer.setItemType(this);
    }
    else
    {
      itemContainers.add(aItemContainer);
    }
    wasAdded = true;
    return wasAdded;
  }

  public boolean removeItemContainer(ItemContainer aItemContainer)
  {
    boolean wasRemoved = false;
    //Unable to remove aItemContainer, as it must always have a itemType
    if (!this.equals(aItemContainer.getItemType()))
    {
      itemContainers.remove(aItemContainer);
      wasRemoved = true;
    }
    return wasRemoved;
  }
  /* Code from template association_AddIndexControlFunctions */
  public boolean addItemContainerAt(ItemContainer aItemContainer, int index)
  {  
    boolean wasAdded = false;
    if(addItemContainer(aItemContainer))
    {
      if(index < 0 ) { index = 0; }
      if(index > numberOfItemContainers()) { index = numberOfItemContainers() - 1; }
      itemContainers.remove(aItemContainer);
      itemContainers.add(index, aItemContainer);
      wasAdded = true;
    }
    return wasAdded;
  }

  public boolean addOrMoveItemContainerAt(ItemContainer aItemContainer, int index)
  {
    boolean wasAdded = false;
    if(itemContainers.contains(aItemContainer))
    {
      if(index < 0 ) { index = 0; }
      if(index > numberOfItemContainers()) { index = numberOfItemContainers() - 1; }
      itemContainers.remove(aItemContainer);
      itemContainers.add(index, aItemContainer);
      wasAdded = true;
    } 
    else 
    {
      wasAdded = addItemContainerAt(aItemContainer, index);
    }
    return wasAdded;
  }

  public void delete()
  {
    itemtypesByName.remove(getName());
    WareFlow placeholderWareFlow = wareFlow;
    this.wareFlow = null;
    if(placeholderWareFlow != null)
    {
      placeholderWareFlow.removeItemType(this);
    }
    for(int i=itemContainers.size(); i > 0; i--)
    {
      ItemContainer aItemContainer = itemContainers.get(i - 1);
      aItemContainer.delete();
    }
  }

  // line 43 "../../../../../WareFlowPersistence.ump"
   public static  void reinitializeUniqueName(List<ItemType> types){
    itemtypesByName.clear();
        for (var t : types){
            itemtypesByName.put(t.getName(), t);
        }
  }


  public String toString()
  {
    return super.toString() + "["+
            "name" + ":" + getName()+ "," +
            "expectedLifeSpanInDays" + ":" + getExpectedLifeSpanInDays()+ "]" + System.getProperties().getProperty("line.separator") +
            "  " + "wareFlow = "+(getWareFlow()!=null?Integer.toHexString(System.identityHashCode(getWareFlow())):"null");
  }
}