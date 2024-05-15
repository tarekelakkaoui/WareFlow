/*PLEASE DO NOT EDIT THIS CODE*/
/*This code was generated using the UMPLE 1.31.1.5860.78bb27cc6 modeling language!*/

package ca.mcgill.ecse.wareflow.model;
import java.util.*;
import java.sql.Date;

// line 10 "../../../../../WareFlowPersistence.ump"
// line 16 "../../../../../WareFlow.ump"
public abstract class User
{

  //------------------------
  // STATIC VARIABLES
  //------------------------

  private static Map<String, User> usersByUsername = new HashMap<String, User>();

  //------------------------
  // MEMBER VARIABLES
  //------------------------

  //User Attributes
  private String username;
  private String name;
  private String password;
  private String phoneNumber;

  //User Associations
  private List<ShipmentOrder> placedOrders;

  //------------------------
  // CONSTRUCTOR
  //------------------------

  public User(String aUsername, String aName, String aPassword, String aPhoneNumber)
  {
    name = aName;
    password = aPassword;
    phoneNumber = aPhoneNumber;
    if (!setUsername(aUsername))
    {
      throw new RuntimeException("Cannot create due to duplicate username. See http://manual.umple.org?RE003ViolationofUniqueness.html");
    }
    placedOrders = new ArrayList<ShipmentOrder>();
  }

  //------------------------
  // INTERFACE
  //------------------------

  public boolean setUsername(String aUsername)
  {
    boolean wasSet = false;
    String anOldUsername = getUsername();
    if (anOldUsername != null && anOldUsername.equals(aUsername)) {
      return true;
    }
    if (hasWithUsername(aUsername)) {
      return wasSet;
    }
    username = aUsername;
    wasSet = true;
    if (anOldUsername != null) {
      usersByUsername.remove(anOldUsername);
    }
    usersByUsername.put(aUsername, this);
    return wasSet;
  }

  public boolean setName(String aName)
  {
    boolean wasSet = false;
    name = aName;
    wasSet = true;
    return wasSet;
  }

  public boolean setPassword(String aPassword)
  {
    boolean wasSet = false;
    password = aPassword;
    wasSet = true;
    return wasSet;
  }

  public boolean setPhoneNumber(String aPhoneNumber)
  {
    boolean wasSet = false;
    phoneNumber = aPhoneNumber;
    wasSet = true;
    return wasSet;
  }

  public String getUsername()
  {
    return username;
  }
  /* Code from template attribute_GetUnique */
  public static User getWithUsername(String aUsername)
  {
    return usersByUsername.get(aUsername);
  }
  /* Code from template attribute_HasUnique */
  public static boolean hasWithUsername(String aUsername)
  {
    return getWithUsername(aUsername) != null;
  }

  public String getName()
  {
    return name;
  }

  public String getPassword()
  {
    return password;
  }

  public String getPhoneNumber()
  {
    return phoneNumber;
  }
  /* Code from template association_GetMany */
  public ShipmentOrder getPlacedOrder(int index)
  {
    ShipmentOrder aPlacedOrder = placedOrders.get(index);
    return aPlacedOrder;
  }

  public List<ShipmentOrder> getPlacedOrders()
  {
    List<ShipmentOrder> newPlacedOrders = Collections.unmodifiableList(placedOrders);
    return newPlacedOrders;
  }

  public int numberOfPlacedOrders()
  {
    int number = placedOrders.size();
    return number;
  }

  public boolean hasPlacedOrders()
  {
    boolean has = placedOrders.size() > 0;
    return has;
  }

  public int indexOfPlacedOrder(ShipmentOrder aPlacedOrder)
  {
    int index = placedOrders.indexOf(aPlacedOrder);
    return index;
  }
  /* Code from template association_MinimumNumberOfMethod */
  public static int minimumNumberOfPlacedOrders()
  {
    return 0;
  }
  /* Code from template association_AddManyToOne */
  public ShipmentOrder addPlacedOrder(int aId, Date aPlacedOnDate, String aDescription, int aQuantity, WareFlow aWareFlow)
  {
    return new ShipmentOrder(aId, aPlacedOnDate, aDescription, aQuantity, aWareFlow, this);
  }

  public boolean addPlacedOrder(ShipmentOrder aPlacedOrder)
  {
    boolean wasAdded = false;
    if (placedOrders.contains(aPlacedOrder)) { return false; }
    User existingOrderPlacer = aPlacedOrder.getOrderPlacer();
    boolean isNewOrderPlacer = existingOrderPlacer != null && !this.equals(existingOrderPlacer);
    if (isNewOrderPlacer)
    {
      aPlacedOrder.setOrderPlacer(this);
    }
    else
    {
      placedOrders.add(aPlacedOrder);
    }
    wasAdded = true;
    return wasAdded;
  }

  public boolean removePlacedOrder(ShipmentOrder aPlacedOrder)
  {
    boolean wasRemoved = false;
    //Unable to remove aPlacedOrder, as it must always have a orderPlacer
    if (!this.equals(aPlacedOrder.getOrderPlacer()))
    {
      placedOrders.remove(aPlacedOrder);
      wasRemoved = true;
    }
    return wasRemoved;
  }
  /* Code from template association_AddIndexControlFunctions */
  public boolean addPlacedOrderAt(ShipmentOrder aPlacedOrder, int index)
  {  
    boolean wasAdded = false;
    if(addPlacedOrder(aPlacedOrder))
    {
      if(index < 0 ) { index = 0; }
      if(index > numberOfPlacedOrders()) { index = numberOfPlacedOrders() - 1; }
      placedOrders.remove(aPlacedOrder);
      placedOrders.add(index, aPlacedOrder);
      wasAdded = true;
    }
    return wasAdded;
  }

  public boolean addOrMovePlacedOrderAt(ShipmentOrder aPlacedOrder, int index)
  {
    boolean wasAdded = false;
    if(placedOrders.contains(aPlacedOrder))
    {
      if(index < 0 ) { index = 0; }
      if(index > numberOfPlacedOrders()) { index = numberOfPlacedOrders() - 1; }
      placedOrders.remove(aPlacedOrder);
      placedOrders.add(index, aPlacedOrder);
      wasAdded = true;
    } 
    else 
    {
      wasAdded = addPlacedOrderAt(aPlacedOrder, index);
    }
    return wasAdded;
  }

  public void delete()
  {
    usersByUsername.remove(getUsername());
    for(int i=placedOrders.size(); i > 0; i--)
    {
      ShipmentOrder aPlacedOrder = placedOrders.get(i - 1);
      aPlacedOrder.delete();
    }
  }

  // line 12 "../../../../../WareFlowPersistence.ump"
   public static  void reinitializeUniqueUsername(Manager manager, List<Client> clients, List<Employee> employees){
    usersByUsername.clear();
        usersByUsername.put(manager.getUsername(), (User) manager);
        for (var c : clients){
            usersByUsername.put(c.getUsername(), (User) c);
        }
        for (var e : employees){
            usersByUsername.put(e.getUsername(), (User) e);
        }
  }


  public String toString()
  {
    return super.toString() + "["+
            "username" + ":" + getUsername()+ "," +
            "name" + ":" + getName()+ "," +
            "password" + ":" + getPassword()+ "," +
            "phoneNumber" + ":" + getPhoneNumber()+ "]";
  }
}