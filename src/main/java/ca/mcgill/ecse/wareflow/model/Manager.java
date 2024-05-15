/*PLEASE DO NOT EDIT THIS CODE*/
/*This code was generated using the UMPLE 1.31.1.5860.78bb27cc6 modeling language!*/

package ca.mcgill.ecse.wareflow.model;
import java.util.*;

// line 42 "../../../../../WareFlow.ump"
public class Manager extends WarehouseStaff
{

  //------------------------
  // MEMBER VARIABLES
  //------------------------

  //Manager Associations
  private WareFlow wareFlow;
  private List<ShipmentOrder> ordersForApproval;

  //------------------------
  // CONSTRUCTOR
  //------------------------

  public Manager(String aUsername, String aName, String aPassword, String aPhoneNumber, WareFlow aWareFlow)
  {
    super(aUsername, aName, aPassword, aPhoneNumber);
    boolean didAddWareFlow = setWareFlow(aWareFlow);
    if (!didAddWareFlow)
    {
      throw new RuntimeException("Unable to create manager due to wareFlow. See http://manual.umple.org?RE002ViolationofAssociationMultiplicity.html");
    }
    ordersForApproval = new ArrayList<ShipmentOrder>();
  }

  //------------------------
  // INTERFACE
  //------------------------
  /* Code from template association_GetOne */
  public WareFlow getWareFlow()
  {
    return wareFlow;
  }
  /* Code from template association_GetMany */
  public ShipmentOrder getOrdersForApproval(int index)
  {
    ShipmentOrder aOrdersForApproval = ordersForApproval.get(index);
    return aOrdersForApproval;
  }

  public List<ShipmentOrder> getOrdersForApproval()
  {
    List<ShipmentOrder> newOrdersForApproval = Collections.unmodifiableList(ordersForApproval);
    return newOrdersForApproval;
  }

  public int numberOfOrdersForApproval()
  {
    int number = ordersForApproval.size();
    return number;
  }

  public boolean hasOrdersForApproval()
  {
    boolean has = ordersForApproval.size() > 0;
    return has;
  }

  public int indexOfOrdersForApproval(ShipmentOrder aOrdersForApproval)
  {
    int index = ordersForApproval.indexOf(aOrdersForApproval);
    return index;
  }
  /* Code from template association_SetOneToOptionalOne */
  public boolean setWareFlow(WareFlow aNewWareFlow)
  {
    boolean wasSet = false;
    if (aNewWareFlow == null)
    {
      //Unable to setWareFlow to null, as manager must always be associated to a wareFlow
      return wasSet;
    }
    
    Manager existingManager = aNewWareFlow.getManager();
    if (existingManager != null && !equals(existingManager))
    {
      //Unable to setWareFlow, the current wareFlow already has a manager, which would be orphaned if it were re-assigned
      return wasSet;
    }
    
    WareFlow anOldWareFlow = wareFlow;
    wareFlow = aNewWareFlow;
    wareFlow.setManager(this);

    if (anOldWareFlow != null)
    {
      anOldWareFlow.setManager(null);
    }
    wasSet = true;
    return wasSet;
  }
  /* Code from template association_MinimumNumberOfMethod */
  public static int minimumNumberOfOrdersForApproval()
  {
    return 0;
  }
  /* Code from template association_AddManyToOptionalOne */
  public boolean addOrdersForApproval(ShipmentOrder aOrdersForApproval)
  {
    boolean wasAdded = false;
    if (ordersForApproval.contains(aOrdersForApproval)) { return false; }
    Manager existingOrderApprover = aOrdersForApproval.getOrderApprover();
    if (existingOrderApprover == null)
    {
      aOrdersForApproval.setOrderApprover(this);
    }
    else if (!this.equals(existingOrderApprover))
    {
      existingOrderApprover.removeOrdersForApproval(aOrdersForApproval);
      addOrdersForApproval(aOrdersForApproval);
    }
    else
    {
      ordersForApproval.add(aOrdersForApproval);
    }
    wasAdded = true;
    return wasAdded;
  }

  public boolean removeOrdersForApproval(ShipmentOrder aOrdersForApproval)
  {
    boolean wasRemoved = false;
    if (ordersForApproval.contains(aOrdersForApproval))
    {
      ordersForApproval.remove(aOrdersForApproval);
      aOrdersForApproval.setOrderApprover(null);
      wasRemoved = true;
    }
    return wasRemoved;
  }
  /* Code from template association_AddIndexControlFunctions */
  public boolean addOrdersForApprovalAt(ShipmentOrder aOrdersForApproval, int index)
  {  
    boolean wasAdded = false;
    if(addOrdersForApproval(aOrdersForApproval))
    {
      if(index < 0 ) { index = 0; }
      if(index > numberOfOrdersForApproval()) { index = numberOfOrdersForApproval() - 1; }
      ordersForApproval.remove(aOrdersForApproval);
      ordersForApproval.add(index, aOrdersForApproval);
      wasAdded = true;
    }
    return wasAdded;
  }

  public boolean addOrMoveOrdersForApprovalAt(ShipmentOrder aOrdersForApproval, int index)
  {
    boolean wasAdded = false;
    if(ordersForApproval.contains(aOrdersForApproval))
    {
      if(index < 0 ) { index = 0; }
      if(index > numberOfOrdersForApproval()) { index = numberOfOrdersForApproval() - 1; }
      ordersForApproval.remove(aOrdersForApproval);
      ordersForApproval.add(index, aOrdersForApproval);
      wasAdded = true;
    } 
    else 
    {
      wasAdded = addOrdersForApprovalAt(aOrdersForApproval, index);
    }
    return wasAdded;
  }

  public void delete()
  {
    WareFlow existingWareFlow = wareFlow;
    wareFlow = null;
    if (existingWareFlow != null)
    {
      existingWareFlow.setManager(null);
    }
    while( !ordersForApproval.isEmpty() )
    {
      ordersForApproval.get(0).setOrderApprover(null);
    }
    super.delete();
  }

}