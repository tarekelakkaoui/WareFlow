/*PLEASE DO NOT EDIT THIS CODE*/
/*This code was generated using the UMPLE 1.31.1.5860.78bb27cc6 modeling language!*/

package ca.mcgill.ecse.wareflow.model;
import java.util.*;
import java.sql.Date;

// line 1 "../../../../../WareFlowPersistence.ump"
// line 6 "../../../../../WareFlow.ump"
public class WareFlow
{

  //------------------------
  // MEMBER VARIABLES
  //------------------------

  //WareFlow Associations
  private List<Employee> employees;
  private List<Client> clients;
  private Manager manager;
  private List<ShipmentOrder> orders;
  private List<ItemType> itemTypes;
  private List<ItemContainer> itemContainers;

  //------------------------
  // CONSTRUCTOR
  //------------------------

  public WareFlow()
  {
    employees = new ArrayList<Employee>();
    clients = new ArrayList<Client>();
    orders = new ArrayList<ShipmentOrder>();
    itemTypes = new ArrayList<ItemType>();
    itemContainers = new ArrayList<ItemContainer>();
  }

  //------------------------
  // INTERFACE
  //------------------------
  /* Code from template association_GetMany */
  public Employee getEmployee(int index)
  {
    Employee aEmployee = employees.get(index);
    return aEmployee;
  }

  public List<Employee> getEmployees()
  {
    List<Employee> newEmployees = Collections.unmodifiableList(employees);
    return newEmployees;
  }

  public int numberOfEmployees()
  {
    int number = employees.size();
    return number;
  }

  public boolean hasEmployees()
  {
    boolean has = employees.size() > 0;
    return has;
  }

  public int indexOfEmployee(Employee aEmployee)
  {
    int index = employees.indexOf(aEmployee);
    return index;
  }
  /* Code from template association_GetMany */
  public Client getClient(int index)
  {
    Client aClient = clients.get(index);
    return aClient;
  }

  public List<Client> getClients()
  {
    List<Client> newClients = Collections.unmodifiableList(clients);
    return newClients;
  }

  public int numberOfClients()
  {
    int number = clients.size();
    return number;
  }

  public boolean hasClients()
  {
    boolean has = clients.size() > 0;
    return has;
  }

  public int indexOfClient(Client aClient)
  {
    int index = clients.indexOf(aClient);
    return index;
  }
  /* Code from template association_GetOne */
  public Manager getManager()
  {
    return manager;
  }

  public boolean hasManager()
  {
    boolean has = manager != null;
    return has;
  }
  /* Code from template association_GetMany */
  public ShipmentOrder getOrder(int index)
  {
    ShipmentOrder aOrder = orders.get(index);
    return aOrder;
  }

  public List<ShipmentOrder> getOrders()
  {
    List<ShipmentOrder> newOrders = Collections.unmodifiableList(orders);
    return newOrders;
  }

  public int numberOfOrders()
  {
    int number = orders.size();
    return number;
  }

  public boolean hasOrders()
  {
    boolean has = orders.size() > 0;
    return has;
  }

  public int indexOfOrder(ShipmentOrder aOrder)
  {
    int index = orders.indexOf(aOrder);
    return index;
  }
  /* Code from template association_GetMany */
  public ItemType getItemType(int index)
  {
    ItemType aItemType = itemTypes.get(index);
    return aItemType;
  }

  public List<ItemType> getItemTypes()
  {
    List<ItemType> newItemTypes = Collections.unmodifiableList(itemTypes);
    return newItemTypes;
  }

  public int numberOfItemTypes()
  {
    int number = itemTypes.size();
    return number;
  }

  public boolean hasItemTypes()
  {
    boolean has = itemTypes.size() > 0;
    return has;
  }

  public int indexOfItemType(ItemType aItemType)
  {
    int index = itemTypes.indexOf(aItemType);
    return index;
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
  /* Code from template association_MinimumNumberOfMethod */
  public static int minimumNumberOfEmployees()
  {
    return 0;
  }
  /* Code from template association_AddManyToOne */
  public Employee addEmployee(String aUsername, String aName, String aPassword, String aPhoneNumber)
  {
    return new Employee(aUsername, aName, aPassword, aPhoneNumber, this);
  }

  public boolean addEmployee(Employee aEmployee)
  {
    boolean wasAdded = false;
    if (employees.contains(aEmployee)) { return false; }
    WareFlow existingWareFlow = aEmployee.getWareFlow();
    boolean isNewWareFlow = existingWareFlow != null && !this.equals(existingWareFlow);
    if (isNewWareFlow)
    {
      aEmployee.setWareFlow(this);
    }
    else
    {
      employees.add(aEmployee);
    }
    wasAdded = true;
    return wasAdded;
  }

  public boolean removeEmployee(Employee aEmployee)
  {
    boolean wasRemoved = false;
    //Unable to remove aEmployee, as it must always have a wareFlow
    if (!this.equals(aEmployee.getWareFlow()))
    {
      employees.remove(aEmployee);
      wasRemoved = true;
    }
    return wasRemoved;
  }
  /* Code from template association_AddIndexControlFunctions */
  public boolean addEmployeeAt(Employee aEmployee, int index)
  {  
    boolean wasAdded = false;
    if(addEmployee(aEmployee))
    {
      if(index < 0 ) { index = 0; }
      if(index > numberOfEmployees()) { index = numberOfEmployees() - 1; }
      employees.remove(aEmployee);
      employees.add(index, aEmployee);
      wasAdded = true;
    }
    return wasAdded;
  }

  public boolean addOrMoveEmployeeAt(Employee aEmployee, int index)
  {
    boolean wasAdded = false;
    if(employees.contains(aEmployee))
    {
      if(index < 0 ) { index = 0; }
      if(index > numberOfEmployees()) { index = numberOfEmployees() - 1; }
      employees.remove(aEmployee);
      employees.add(index, aEmployee);
      wasAdded = true;
    } 
    else 
    {
      wasAdded = addEmployeeAt(aEmployee, index);
    }
    return wasAdded;
  }
  /* Code from template association_MinimumNumberOfMethod */
  public static int minimumNumberOfClients()
  {
    return 0;
  }
  /* Code from template association_AddManyToOne */
  public Client addClient(String aUsername, String aName, String aPassword, String aPhoneNumber, String aAddress)
  {
    return new Client(aUsername, aName, aPassword, aPhoneNumber, aAddress, this);
  }

  public boolean addClient(Client aClient)
  {
    boolean wasAdded = false;
    if (clients.contains(aClient)) { return false; }
    WareFlow existingWareFlow = aClient.getWareFlow();
    boolean isNewWareFlow = existingWareFlow != null && !this.equals(existingWareFlow);
    if (isNewWareFlow)
    {
      aClient.setWareFlow(this);
    }
    else
    {
      clients.add(aClient);
    }
    wasAdded = true;
    return wasAdded;
  }

  public boolean removeClient(Client aClient)
  {
    boolean wasRemoved = false;
    //Unable to remove aClient, as it must always have a wareFlow
    if (!this.equals(aClient.getWareFlow()))
    {
      clients.remove(aClient);
      wasRemoved = true;
    }
    return wasRemoved;
  }
  /* Code from template association_AddIndexControlFunctions */
  public boolean addClientAt(Client aClient, int index)
  {  
    boolean wasAdded = false;
    if(addClient(aClient))
    {
      if(index < 0 ) { index = 0; }
      if(index > numberOfClients()) { index = numberOfClients() - 1; }
      clients.remove(aClient);
      clients.add(index, aClient);
      wasAdded = true;
    }
    return wasAdded;
  }

  public boolean addOrMoveClientAt(Client aClient, int index)
  {
    boolean wasAdded = false;
    if(clients.contains(aClient))
    {
      if(index < 0 ) { index = 0; }
      if(index > numberOfClients()) { index = numberOfClients() - 1; }
      clients.remove(aClient);
      clients.add(index, aClient);
      wasAdded = true;
    } 
    else 
    {
      wasAdded = addClientAt(aClient, index);
    }
    return wasAdded;
  }
  /* Code from template association_SetOptionalOneToOne */
  public boolean setManager(Manager aNewManager)
  {
    boolean wasSet = false;
    if (manager != null && !manager.equals(aNewManager) && equals(manager.getWareFlow()))
    {
      //Unable to setManager, as existing manager would become an orphan
      return wasSet;
    }

    manager = aNewManager;
    WareFlow anOldWareFlow = aNewManager != null ? aNewManager.getWareFlow() : null;

    if (!this.equals(anOldWareFlow))
    {
      if (anOldWareFlow != null)
      {
        anOldWareFlow.manager = null;
      }
      if (manager != null)
      {
        manager.setWareFlow(this);
      }
    }
    wasSet = true;
    return wasSet;
  }
  /* Code from template association_MinimumNumberOfMethod */
  public static int minimumNumberOfOrders()
  {
    return 0;
  }
  /* Code from template association_AddManyToOne */
  public ShipmentOrder addOrder(int aId, Date aPlacedOnDate, String aDescription, int aQuantity, User aOrderPlacer)
  {
    return new ShipmentOrder(aId, aPlacedOnDate, aDescription, aQuantity, this, aOrderPlacer);
  }

  public boolean addOrder(ShipmentOrder aOrder)
  {
    boolean wasAdded = false;
    if (orders.contains(aOrder)) { return false; }
    WareFlow existingWareFlow = aOrder.getWareFlow();
    boolean isNewWareFlow = existingWareFlow != null && !this.equals(existingWareFlow);
    if (isNewWareFlow)
    {
      aOrder.setWareFlow(this);
    }
    else
    {
      orders.add(aOrder);
    }
    wasAdded = true;
    return wasAdded;
  }

  public boolean removeOrder(ShipmentOrder aOrder)
  {
    boolean wasRemoved = false;
    //Unable to remove aOrder, as it must always have a wareFlow
    if (!this.equals(aOrder.getWareFlow()))
    {
      orders.remove(aOrder);
      wasRemoved = true;
    }
    return wasRemoved;
  }
  /* Code from template association_AddIndexControlFunctions */
  public boolean addOrderAt(ShipmentOrder aOrder, int index)
  {  
    boolean wasAdded = false;
    if(addOrder(aOrder))
    {
      if(index < 0 ) { index = 0; }
      if(index > numberOfOrders()) { index = numberOfOrders() - 1; }
      orders.remove(aOrder);
      orders.add(index, aOrder);
      wasAdded = true;
    }
    return wasAdded;
  }

  public boolean addOrMoveOrderAt(ShipmentOrder aOrder, int index)
  {
    boolean wasAdded = false;
    if(orders.contains(aOrder))
    {
      if(index < 0 ) { index = 0; }
      if(index > numberOfOrders()) { index = numberOfOrders() - 1; }
      orders.remove(aOrder);
      orders.add(index, aOrder);
      wasAdded = true;
    } 
    else 
    {
      wasAdded = addOrderAt(aOrder, index);
    }
    return wasAdded;
  }
  /* Code from template association_MinimumNumberOfMethod */
  public static int minimumNumberOfItemTypes()
  {
    return 0;
  }
  /* Code from template association_AddManyToOne */
  public ItemType addItemType(String aName, int aExpectedLifeSpanInDays)
  {
    return new ItemType(aName, aExpectedLifeSpanInDays, this);
  }

  public boolean addItemType(ItemType aItemType)
  {
    boolean wasAdded = false;
    if (itemTypes.contains(aItemType)) { return false; }
    WareFlow existingWareFlow = aItemType.getWareFlow();
    boolean isNewWareFlow = existingWareFlow != null && !this.equals(existingWareFlow);
    if (isNewWareFlow)
    {
      aItemType.setWareFlow(this);
    }
    else
    {
      itemTypes.add(aItemType);
    }
    wasAdded = true;
    return wasAdded;
  }

  public boolean removeItemType(ItemType aItemType)
  {
    boolean wasRemoved = false;
    //Unable to remove aItemType, as it must always have a wareFlow
    if (!this.equals(aItemType.getWareFlow()))
    {
      itemTypes.remove(aItemType);
      wasRemoved = true;
    }
    return wasRemoved;
  }
  /* Code from template association_AddIndexControlFunctions */
  public boolean addItemTypeAt(ItemType aItemType, int index)
  {  
    boolean wasAdded = false;
    if(addItemType(aItemType))
    {
      if(index < 0 ) { index = 0; }
      if(index > numberOfItemTypes()) { index = numberOfItemTypes() - 1; }
      itemTypes.remove(aItemType);
      itemTypes.add(index, aItemType);
      wasAdded = true;
    }
    return wasAdded;
  }

  public boolean addOrMoveItemTypeAt(ItemType aItemType, int index)
  {
    boolean wasAdded = false;
    if(itemTypes.contains(aItemType))
    {
      if(index < 0 ) { index = 0; }
      if(index > numberOfItemTypes()) { index = numberOfItemTypes() - 1; }
      itemTypes.remove(aItemType);
      itemTypes.add(index, aItemType);
      wasAdded = true;
    } 
    else 
    {
      wasAdded = addItemTypeAt(aItemType, index);
    }
    return wasAdded;
  }
  /* Code from template association_MinimumNumberOfMethod */
  public static int minimumNumberOfItemContainers()
  {
    return 0;
  }
  /* Code from template association_AddManyToOne */
  public ItemContainer addItemContainer(int aContainerNumber, int aAreaNumber, int aSlotNumber, Date aAddedOnDate, ItemType aItemType)
  {
    return new ItemContainer(aContainerNumber, aAreaNumber, aSlotNumber, aAddedOnDate, this, aItemType);
  }

  public boolean addItemContainer(ItemContainer aItemContainer)
  {
    boolean wasAdded = false;
    if (itemContainers.contains(aItemContainer)) { return false; }
    WareFlow existingWareFlow = aItemContainer.getWareFlow();
    boolean isNewWareFlow = existingWareFlow != null && !this.equals(existingWareFlow);
    if (isNewWareFlow)
    {
      aItemContainer.setWareFlow(this);
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
    //Unable to remove aItemContainer, as it must always have a wareFlow
    if (!this.equals(aItemContainer.getWareFlow()))
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
    while (employees.size() > 0)
    {
      Employee aEmployee = employees.get(employees.size() - 1);
      aEmployee.delete();
      employees.remove(aEmployee);
    }
    
    while (clients.size() > 0)
    {
      Client aClient = clients.get(clients.size() - 1);
      aClient.delete();
      clients.remove(aClient);
    }
    
    Manager existingManager = manager;
    manager = null;
    if (existingManager != null)
    {
      existingManager.delete();
      existingManager.setWareFlow(null);
    }
    while (orders.size() > 0)
    {
      ShipmentOrder aOrder = orders.get(orders.size() - 1);
      aOrder.delete();
      orders.remove(aOrder);
    }
    
    while (itemTypes.size() > 0)
    {
      ItemType aItemType = itemTypes.get(itemTypes.size() - 1);
      aItemType.delete();
      itemTypes.remove(aItemType);
    }
    
    while (itemContainers.size() > 0)
    {
      ItemContainer aItemContainer = itemContainers.get(itemContainers.size() - 1);
      aItemContainer.delete();
      itemContainers.remove(aItemContainer);
    }
    
  }

  // line 3 "../../../../../WareFlowPersistence.ump"
   public void reinitialize(){
    User.reinitializeUniqueUsername(getManager(), getClients(), getEmployees());
        ShipmentOrder.reinitializeUniqueId(getOrders());
        ItemContainer.reinitializeUniqueContainerNumber(getItemContainers());
        ItemType.reinitializeUniqueName(getItemTypes());
  }

}