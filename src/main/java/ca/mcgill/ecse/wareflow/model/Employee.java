/*PLEASE DO NOT EDIT THIS CODE*/
/*This code was generated using the UMPLE 1.31.1.5860.78bb27cc6 modeling language!*/

package ca.mcgill.ecse.wareflow.model;
import java.util.*;

// line 37 "../../../../../WareFlow.ump"
public class Employee extends WarehouseStaff
{

  //------------------------
  // MEMBER VARIABLES
  //------------------------

  //Employee Associations
  private WareFlow wareFlow;

  //------------------------
  // CONSTRUCTOR
  //------------------------

  public Employee(String aUsername, String aName, String aPassword, String aPhoneNumber, WareFlow aWareFlow)
  {
    super(aUsername, aName, aPassword, aPhoneNumber);
    boolean didAddWareFlow = setWareFlow(aWareFlow);
    if (!didAddWareFlow)
    {
      throw new RuntimeException("Unable to create employee due to wareFlow. See http://manual.umple.org?RE002ViolationofAssociationMultiplicity.html");
    }
  }

  //------------------------
  // INTERFACE
  //------------------------
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
      existingWareFlow.removeEmployee(this);
    }
    wareFlow.addEmployee(this);
    wasSet = true;
    return wasSet;
  }

  public void delete()
  {
    WareFlow placeholderWareFlow = wareFlow;
    this.wareFlow = null;
    if(placeholderWareFlow != null)
    {
      placeholderWareFlow.removeEmployee(this);
    }
    super.delete();
  }

}