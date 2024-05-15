/*PLEASE DO NOT EDIT THIS CODE*/
/*This code was generated using the UMPLE 1.31.1.5860.78bb27cc6 modeling language!*/

package ca.mcgill.ecse.wareflow.model;
import java.util.*;
import java.sql.Date;

// line 1 "../../../../../WareFlowStates.ump"
// line 23 "../../../../../WareFlowPersistence.ump"
// line 47 "../../../../../WareFlow.ump"
public class ShipmentOrder
{

  //------------------------
  // ENUMERATIONS
  //------------------------

  public enum TimeEstimate { LessThanADay, OneToThreeDays, ThreeToSevenDays, OneToThreeWeeks, ThreeOrMoreWeeks }
  public enum PriorityLevel { Urgent, Normal, Low }

  //------------------------
  // STATIC VARIABLES
  //------------------------

  private static Map<Integer, ShipmentOrder> shipmentordersById = new HashMap<Integer, ShipmentOrder>();

  //------------------------
  // MEMBER VARIABLES
  //------------------------

  //ShipmentOrder Attributes
  private int id;
  private Date placedOnDate;
  private String description;
  private int quantity;
  private TimeEstimate timeToFullfill;
  private PriorityLevel priority;

  //ShipmentOrder State Machines
  public enum Status { Open, Assigned, InProgress, Completed, Closed }
  private Status status;

  //ShipmentOrder Associations
  private List<ShipmentNote> shipmentNotes;
  private WareFlow wareFlow;
  private User orderPlacer;
  private WarehouseStaff orderPicker;
  private ItemContainer container;
  private Manager orderApprover;

  //------------------------
  // CONSTRUCTOR
  //------------------------

  public ShipmentOrder(int aId, Date aPlacedOnDate, String aDescription, int aQuantity, WareFlow aWareFlow, User aOrderPlacer)
  {
    placedOnDate = aPlacedOnDate;
    description = aDescription;
    quantity = aQuantity;
    if (!setId(aId))
    {
      throw new RuntimeException("Cannot create due to duplicate id. See http://manual.umple.org?RE003ViolationofUniqueness.html");
    }
    shipmentNotes = new ArrayList<ShipmentNote>();
    boolean didAddWareFlow = setWareFlow(aWareFlow);
    if (!didAddWareFlow)
    {
      throw new RuntimeException("Unable to create order due to wareFlow. See http://manual.umple.org?RE002ViolationofAssociationMultiplicity.html");
    }
    boolean didAddOrderPlacer = setOrderPlacer(aOrderPlacer);
    if (!didAddOrderPlacer)
    {
      throw new RuntimeException("Unable to create placedOrder due to orderPlacer. See http://manual.umple.org?RE002ViolationofAssociationMultiplicity.html");
    }
    setStatus(Status.Open);
  }

  //------------------------
  // INTERFACE
  //------------------------

  public boolean setId(int aId)
  {
    boolean wasSet = false;
    Integer anOldId = getId();
    if (anOldId != null && anOldId.equals(aId)) {
      return true;
    }
    if (hasWithId(aId)) {
      return wasSet;
    }
    id = aId;
    wasSet = true;
    if (anOldId != null) {
      shipmentordersById.remove(anOldId);
    }
    shipmentordersById.put(aId, this);
    return wasSet;
  }

  public boolean setPlacedOnDate(Date aPlacedOnDate)
  {
    boolean wasSet = false;
    placedOnDate = aPlacedOnDate;
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

  public boolean setQuantity(int aQuantity)
  {
    boolean wasSet = false;
    quantity = aQuantity;
    wasSet = true;
    return wasSet;
  }

  public boolean setTimeToFullfill(TimeEstimate aTimeToFullfill)
  {
    boolean wasSet = false;
    timeToFullfill = aTimeToFullfill;
    wasSet = true;
    return wasSet;
  }

  public boolean setPriority(PriorityLevel aPriority)
  {
    boolean wasSet = false;
    priority = aPriority;
    wasSet = true;
    return wasSet;
  }

  public int getId()
  {
    return id;
  }
  /* Code from template attribute_GetUnique */
  public static ShipmentOrder getWithId(int aId)
  {
    return shipmentordersById.get(aId);
  }
  /* Code from template attribute_HasUnique */
  public static boolean hasWithId(int aId)
  {
    return getWithId(aId) != null;
  }

  public Date getPlacedOnDate()
  {
    return placedOnDate;
  }

  public String getDescription()
  {
    return description;
  }

  public int getQuantity()
  {
    return quantity;
  }

  public TimeEstimate getTimeToFullfill()
  {
    return timeToFullfill;
  }

  public PriorityLevel getPriority()
  {
    return priority;
  }

  public String getStatusFullName()
  {
    String answer = status.toString();
    return answer;
  }

  public Status getStatus()
  {
    return status;
  }

  public boolean assignOrder(WarehouseStaff staff,TimeEstimate timeEstimate,PriorityLevel priority,boolean requiresApproval)
  {
    boolean wasEventProcessed = false;
    
    Status aStatus = status;
    switch (aStatus)
    {
      case Open:
        if (canBeAssigned(staff))
        {
        // line 5 "../../../../../WareFlowStates.ump"
          doAssignOrder(staff, timeEstimate, priority, requiresApproval);
          setStatus(Status.Assigned);
          wasEventProcessed = true;
          break;
        }
        if (!(canBeAssigned(staff)))
        {
        // line 10 "../../../../../WareFlowStates.ump"
          rejectAssignOrder("Staff to assign does not exist.");
          setStatus(Status.Open);
          wasEventProcessed = true;
          break;
        }
        break;
      case Assigned:
        // line 37 "../../../../../WareFlowStates.ump"
        rejectAssignOrder("The shipment order is already assigned.");
        setStatus(Status.Assigned);
        wasEventProcessed = true;
        break;
      case InProgress:
        // line 61 "../../../../../WareFlowStates.ump"
        rejectAssignOrder("Cannot assign a shipment order which is in progress.");
        setStatus(Status.InProgress);
        wasEventProcessed = true;
        break;
      case Completed:
        // line 92 "../../../../../WareFlowStates.ump"
        rejectAssignOrder("Cannot assign a shipment order which is completed.");
        setStatus(Status.Completed);
        wasEventProcessed = true;
        break;
      case Closed:
        // line 115 "../../../../../WareFlowStates.ump"
        rejectAssignOrder("Cannot assign a shipment order which is closed.");
        setStatus(Status.Closed);
        wasEventProcessed = true;
        break;
      default:
        // Other states do respond to this event
    }

    return wasEventProcessed;
  }

  public boolean startOrder()
  {
    boolean wasEventProcessed = false;
    
    Status aStatus = status;
    switch (aStatus)
    {
      case Open:
        // line 15 "../../../../../WareFlowStates.ump"
        rejectStartOrder("Cannot start a shipment order which is open.");
        setStatus(Status.Open);
        wasEventProcessed = true;
        break;
      case Assigned:
        setStatus(Status.InProgress);
        wasEventProcessed = true;
        break;
      case InProgress:
        // line 66 "../../../../../WareFlowStates.ump"
        rejectStartOrder("The shipment order is already in progress.");
        setStatus(Status.InProgress);
        wasEventProcessed = true;
        break;
      case Completed:
        // line 97 "../../../../../WareFlowStates.ump"
        rejectStartOrder("Cannot start a shipment order which is completed.");
        setStatus(Status.Completed);
        wasEventProcessed = true;
        break;
      case Closed:
        // line 120 "../../../../../WareFlowStates.ump"
        rejectStartOrder("Cannot start a shipment order which is closed.");
        setStatus(Status.Closed);
        wasEventProcessed = true;
        break;
      default:
        // Other states do respond to this event
    }

    return wasEventProcessed;
  }

  public boolean completeOrder()
  {
    boolean wasEventProcessed = false;
    
    Status aStatus = status;
    switch (aStatus)
    {
      case Open:
        // line 20 "../../../../../WareFlowStates.ump"
        rejectCompleteOrder("Cannot complete a shipment order which is open.");
        setStatus(Status.Open);
        wasEventProcessed = true;
        break;
      case Assigned:
        // line 44 "../../../../../WareFlowStates.ump"
        rejectCompleteOrder("Cannot complete a shipment order which is assigned.");
        setStatus(Status.Assigned);
        wasEventProcessed = true;
        break;
      case InProgress:
        if (this.hasOrderApprover())
        {
        // line 71 "../../../../../WareFlowStates.ump"
          
          setStatus(Status.Completed);
          wasEventProcessed = true;
          break;
        }
        if (!(this.hasOrderApprover()))
        {
        // line 76 "../../../../../WareFlowStates.ump"
          
          setStatus(Status.Closed);
          wasEventProcessed = true;
          break;
        }
        break;
      case Completed:
        // line 109 "../../../../../WareFlowStates.ump"
        rejectCompleteOrder("The shipment order is already completed.");
        setStatus(Status.Completed);
        wasEventProcessed = true;
        break;
      case Closed:
        // line 125 "../../../../../WareFlowStates.ump"
        rejectCompleteOrder("The shipment order is already closed.");
        setStatus(Status.Closed);
        wasEventProcessed = true;
        break;
      default:
        // Other states do respond to this event
    }

    return wasEventProcessed;
  }

  public boolean disapproveOrder(Date date,String description)
  {
    boolean wasEventProcessed = false;
    
    Status aStatus = status;
    switch (aStatus)
    {
      case Open:
        // line 25 "../../../../../WareFlowStates.ump"
        rejectDisapproveOrder("Cannot disapprove a shipment order which is open.");
        setStatus(Status.Open);
        wasEventProcessed = true;
        break;
      case Assigned:
        // line 49 "../../../../../WareFlowStates.ump"
        rejectDisapproveOrder("Cannot disapprove a shipment order which is assigned.");
        setStatus(Status.Assigned);
        wasEventProcessed = true;
        break;
      case InProgress:
        // line 80 "../../../../../WareFlowStates.ump"
        rejectDisapproveOrder("Cannot disapprove a shipment order which is in progress.");
        setStatus(Status.InProgress);
        wasEventProcessed = true;
        break;
      case Completed:
        // line 102 "../../../../../WareFlowStates.ump"
        doDisapproveOrder(date, description);
        setStatus(Status.InProgress);
        wasEventProcessed = true;
        break;
      case Closed:
        // line 130 "../../../../../WareFlowStates.ump"
        rejectDisapproveOrder("Cannot disapprove a shipment order which is closed.");
        setStatus(Status.Closed);
        wasEventProcessed = true;
        break;
      default:
        // Other states do respond to this event
    }

    return wasEventProcessed;
  }

  public boolean approveShipment()
  {
    boolean wasEventProcessed = false;
    
    Status aStatus = status;
    switch (aStatus)
    {
      case Open:
        // line 30 "../../../../../WareFlowStates.ump"
        rejectApproveShipment("Cannot approve a shipment order which is open.");
        setStatus(Status.Open);
        wasEventProcessed = true;
        break;
      case Assigned:
        // line 54 "../../../../../WareFlowStates.ump"
        rejectApproveShipment("Cannot approve a shipment order which is assigned.");
        setStatus(Status.Assigned);
        wasEventProcessed = true;
        break;
      case InProgress:
        // line 85 "../../../../../WareFlowStates.ump"
        rejectApproveShipment("Cannot approve a shipment order which is in progress.");
        setStatus(Status.InProgress);
        wasEventProcessed = true;
        break;
      case Completed:
        setStatus(Status.Closed);
        wasEventProcessed = true;
        break;
      case Closed:
        // line 135 "../../../../../WareFlowStates.ump"
        rejectApproveShipment("The shipment order is already closed.");
        setStatus(Status.Closed);
        wasEventProcessed = true;
        break;
      default:
        // Other states do respond to this event
    }

    return wasEventProcessed;
  }

  private void setStatus(Status aStatus)
  {
    status = aStatus;
  }
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
  /* Code from template association_GetOne */
  public WareFlow getWareFlow()
  {
    return wareFlow;
  }
  /* Code from template association_GetOne */
  public User getOrderPlacer()
  {
    return orderPlacer;
  }
  /* Code from template association_GetOne */
  public WarehouseStaff getOrderPicker()
  {
    return orderPicker;
  }

  public boolean hasOrderPicker()
  {
    boolean has = orderPicker != null;
    return has;
  }
  /* Code from template association_GetOne */
  public ItemContainer getContainer()
  {
    return container;
  }

  public boolean hasContainer()
  {
    boolean has = container != null;
    return has;
  }
  /* Code from template association_GetOne */
  public Manager getOrderApprover()
  {
    return orderApprover;
  }

  public boolean hasOrderApprover()
  {
    boolean has = orderApprover != null;
    return has;
  }
  /* Code from template association_MinimumNumberOfMethod */
  public static int minimumNumberOfShipmentNotes()
  {
    return 0;
  }
  /* Code from template association_AddManyToOne */
  public ShipmentNote addShipmentNote(Date aDate, String aDescription, WarehouseStaff aNoteTaker)
  {
    return new ShipmentNote(aDate, aDescription, this, aNoteTaker);
  }

  public boolean addShipmentNote(ShipmentNote aShipmentNote)
  {
    boolean wasAdded = false;
    if (shipmentNotes.contains(aShipmentNote)) { return false; }
    ShipmentOrder existingOrder = aShipmentNote.getOrder();
    boolean isNewOrder = existingOrder != null && !this.equals(existingOrder);
    if (isNewOrder)
    {
      aShipmentNote.setOrder(this);
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
    //Unable to remove aShipmentNote, as it must always have a order
    if (!this.equals(aShipmentNote.getOrder()))
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
      existingWareFlow.removeOrder(this);
    }
    wareFlow.addOrder(this);
    wasSet = true;
    return wasSet;
  }
  /* Code from template association_SetOneToMany */
  public boolean setOrderPlacer(User aOrderPlacer)
  {
    boolean wasSet = false;
    if (aOrderPlacer == null)
    {
      return wasSet;
    }

    User existingOrderPlacer = orderPlacer;
    orderPlacer = aOrderPlacer;
    if (existingOrderPlacer != null && !existingOrderPlacer.equals(aOrderPlacer))
    {
      existingOrderPlacer.removePlacedOrder(this);
    }
    orderPlacer.addPlacedOrder(this);
    wasSet = true;
    return wasSet;
  }
  /* Code from template association_SetOptionalOneToMany */
  public boolean setOrderPicker(WarehouseStaff aOrderPicker)
  {
    boolean wasSet = false;
    WarehouseStaff existingOrderPicker = orderPicker;
    orderPicker = aOrderPicker;
    if (existingOrderPicker != null && !existingOrderPicker.equals(aOrderPicker))
    {
      existingOrderPicker.removeShipmentOrder(this);
    }
    if (aOrderPicker != null)
    {
      aOrderPicker.addShipmentOrder(this);
    }
    wasSet = true;
    return wasSet;
  }
  /* Code from template association_SetOptionalOneToMany */
  public boolean setContainer(ItemContainer aContainer)
  {
    boolean wasSet = false;
    ItemContainer existingContainer = container;
    container = aContainer;
    if (existingContainer != null && !existingContainer.equals(aContainer))
    {
      existingContainer.removeShipmentOrder(this);
    }
    if (aContainer != null)
    {
      aContainer.addShipmentOrder(this);
    }
    wasSet = true;
    return wasSet;
  }
  /* Code from template association_SetOptionalOneToMany */
  public boolean setOrderApprover(Manager aOrderApprover)
  {
    boolean wasSet = false;
    Manager existingOrderApprover = orderApprover;
    orderApprover = aOrderApprover;
    if (existingOrderApprover != null && !existingOrderApprover.equals(aOrderApprover))
    {
      existingOrderApprover.removeOrdersForApproval(this);
    }
    if (aOrderApprover != null)
    {
      aOrderApprover.addOrdersForApproval(this);
    }
    wasSet = true;
    return wasSet;
  }

  public void delete()
  {
    shipmentordersById.remove(getId());
    while (shipmentNotes.size() > 0)
    {
      ShipmentNote aShipmentNote = shipmentNotes.get(shipmentNotes.size() - 1);
      aShipmentNote.delete();
      shipmentNotes.remove(aShipmentNote);
    }
    
    WareFlow placeholderWareFlow = wareFlow;
    this.wareFlow = null;
    if(placeholderWareFlow != null)
    {
      placeholderWareFlow.removeOrder(this);
    }
    User placeholderOrderPlacer = orderPlacer;
    this.orderPlacer = null;
    if(placeholderOrderPlacer != null)
    {
      placeholderOrderPlacer.removePlacedOrder(this);
    }
    if (orderPicker != null)
    {
      WarehouseStaff placeholderOrderPicker = orderPicker;
      this.orderPicker = null;
      placeholderOrderPicker.removeShipmentOrder(this);
    }
    if (container != null)
    {
      ItemContainer placeholderContainer = container;
      this.container = null;
      placeholderContainer.removeShipmentOrder(this);
    }
    if (orderApprover != null)
    {
      Manager placeholderOrderApprover = orderApprover;
      this.orderApprover = null;
      placeholderOrderApprover.removeOrdersForApproval(this);
    }
  }

  // line 143 "../../../../../WareFlowStates.ump"
   private void doAssignOrder(WarehouseStaff staff, TimeEstimate timeEstimate, PriorityLevel priority, boolean requiresApproval){
    WareFlow wareFlow = this.getWareFlow();
    this.setOrderPicker(staff);
    this.setTimeToFullfill(timeEstimate);
    this.setPriority(priority);
    if (requiresApproval){
      this.setOrderApprover(wareFlow.getManager());
    }
  }

  // line 153 "../../../../../WareFlowStates.ump"
   private boolean canBeAssigned(WarehouseStaff staff){
    WareFlow wareFlow = this.getWareFlow();
    if(staff != null) {
	    if(!staff.getUsername().equalsIgnoreCase("manager")) {
		    Employee employee = (Employee) staff;
		    return (wareFlow.indexOfEmployee(employee) != -1);
	    }
	    return true;
    }
    return false;
  }

  // line 165 "../../../../../WareFlowStates.ump"
   private void rejectAssignOrder(String error){
    throw new RuntimeException(error);
  }

  // line 169 "../../../../../WareFlowStates.ump"
   private void rejectStartOrder(String error){
    throw new RuntimeException(error);
  }

  // line 173 "../../../../../WareFlowStates.ump"
   private void rejectCompleteOrder(String error){
    throw new RuntimeException(error);
  }

  // line 177 "../../../../../WareFlowStates.ump"
   private void doDisapproveOrder(Date date, String description){
    this.addShipmentNote(date, description, this.getOrderApprover());
  }

  // line 181 "../../../../../WareFlowStates.ump"
   private void rejectDisapproveOrder(String error){
    throw new RuntimeException(error);
  }

  // line 185 "../../../../../WareFlowStates.ump"
   private void rejectApproveShipment(String error){
    throw new RuntimeException(error);
  }

  // line 25 "../../../../../WareFlowPersistence.ump"
   public static  void reinitializeUniqueId(List<ShipmentOrder> orders){
    shipmentordersById.clear();
        for (var o : orders){
            shipmentordersById.put(o.getId(), o);
        }
  }


  public String toString()
  {
    return super.toString() + "["+
            "id" + ":" + getId()+ "," +
            "description" + ":" + getDescription()+ "," +
            "quantity" + ":" + getQuantity()+ "]" + System.getProperties().getProperty("line.separator") +
            "  " + "placedOnDate" + "=" + (getPlacedOnDate() != null ? !getPlacedOnDate().equals(this)  ? getPlacedOnDate().toString().replaceAll("  ","    ") : "this" : "null") + System.getProperties().getProperty("line.separator") +
            "  " + "timeToFullfill" + "=" + (getTimeToFullfill() != null ? !getTimeToFullfill().equals(this)  ? getTimeToFullfill().toString().replaceAll("  ","    ") : "this" : "null") + System.getProperties().getProperty("line.separator") +
            "  " + "priority" + "=" + (getPriority() != null ? !getPriority().equals(this)  ? getPriority().toString().replaceAll("  ","    ") : "this" : "null") + System.getProperties().getProperty("line.separator") +
            "  " + "wareFlow = "+(getWareFlow()!=null?Integer.toHexString(System.identityHashCode(getWareFlow())):"null") + System.getProperties().getProperty("line.separator") +
            "  " + "orderPlacer = "+(getOrderPlacer()!=null?Integer.toHexString(System.identityHashCode(getOrderPlacer())):"null") + System.getProperties().getProperty("line.separator") +
            "  " + "orderPicker = "+(getOrderPicker()!=null?Integer.toHexString(System.identityHashCode(getOrderPicker())):"null") + System.getProperties().getProperty("line.separator") +
            "  " + "container = "+(getContainer()!=null?Integer.toHexString(System.identityHashCode(getContainer())):"null") + System.getProperties().getProperty("line.separator") +
            "  " + "orderApprover = "+(getOrderApprover()!=null?Integer.toHexString(System.identityHashCode(getOrderApprover())):"null");
  }
}