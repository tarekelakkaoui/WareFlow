/*PLEASE DO NOT EDIT THIS CODE*/
/*This code was generated using the UMPLE 1.31.1.5860.78bb27cc6 modeling language!*/

package ca.mcgill.ecse.wareflow.model;
import java.util.*;

// line 31 "../../../../../WareFlow.ump"
public class Client extends User
{

  //------------------------
  // MEMBER VARIABLES
  //------------------------

  //Client Attributes
  private String address;

  //Client Associations
  private WareFlow wareFlow;

  //------------------------
  // CONSTRUCTOR
  //------------------------

  public Client(String aUsername, String aName, String aPassword, String aPhoneNumber, String aAddress, WareFlow aWareFlow)
  {
    super(aUsername, aName, aPassword, aPhoneNumber);
    address = aAddress;
    boolean didAddWareFlow = setWareFlow(aWareFlow);
    if (!didAddWareFlow)
    {
      throw new RuntimeException("Unable to create client due to wareFlow. See http://manual.umple.org?RE002ViolationofAssociationMultiplicity.html");
    }
  }

  //------------------------
  // INTERFACE
  //------------------------

  public boolean setAddress(String aAddress)
  {
    boolean wasSet = false;
    address = aAddress;
    wasSet = true;
    return wasSet;
  }

  public String getAddress()
  {
    return address;
  }
  /* Code from template association_GetOne */
  public WareFlow getWareFlow()
  {
    return wareFlow;
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
      existingWareFlow.removeClient(this);
    }
    wareFlow.addClient(this);
    wasSet = true;
    return wasSet;
  }

  public void delete()
  {
    WareFlow placeholderWareFlow = wareFlow;
    this.wareFlow = null;
    if(placeholderWareFlow != null)
    {
      placeholderWareFlow.removeClient(this);
    }
    super.delete();
  }


  public String toString()
  {
    return super.toString() + "["+
            "address" + ":" + getAddress()+ "]" + System.getProperties().getProperty("line.separator") +
            "  " + "wareFlow = "+(getWareFlow()!=null?Integer.toHexString(System.identityHashCode(getWareFlow())):"null");
  }
}