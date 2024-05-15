package ca.mcgill.ecse.wareflow.features;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertTrue;
import java.util.List;
import java.util.Map;
import ca.mcgill.ecse.wareflow.application.WareFlowApplication;
import ca.mcgill.ecse.wareflow.controller.ItemTypeController;
import ca.mcgill.ecse.wareflow.model.ItemType;
import ca.mcgill.ecse.wareflow.model.WareFlow;
import io.cucumber.java.en.Given;
import io.cucumber.java.en.Then;
import io.cucumber.java.en.When;

public class DeleteItemTypeStepDefinitions {
  private WareFlow wareFlow = WareFlowApplication.getWareFlow();

  /**
   * Loads item types from the input dataTable
   * 
   * @param dataTable
   * @author Vladimir Venkov (vladimirven001)
   * @author Laurent Chiricota (larrycherry13)
   * @author Samy Sabir (SamySabir)
   * @author Jessie Kurtz (jkzcodes)
   * @author Dania Bouhmidi (dania-bh)
   * @author James Luu (Jamesluu0917)
   * @author Noah Munro-Kagan (DenzelleJinx)
   */
  private void loadItemTypes(io.cucumber.datatable.DataTable dataTable) {

    List<Map<String, Object>> rows = dataTable.asMaps(String.class, Object.class);

    for (Map<String, Object> row : rows) {
      String name = (row.get("name")).toString();
      int expectedLifeSpanInDays = Integer.parseInt(row.get("expectedLifeSpanInDays").toString());
      wareFlow.addItemType(name, expectedLifeSpanInDays);
    }
  }

  /**
   * Loads the input dataTable and creates the given item types
   * 
   * @param dataTable
   * @author Vladimir Venkov (vladimirven001)
   * @author Laurent Chiricota (larrycherry13)
   * @author Samy Sabir (SamySabir)
   * @author Jessie Kurtz (jkzcodes)
   * @author Dania Bouhmidi (dania-bh)
   * @author James Luu (Jamesluu0917)
   * @author Noah Munro-Kagan (DenzelleJinx)
   */
  @Given("the following item types exist in the system \\(f7)")
  public void the_following_item_types_exist_in_the_system_f7(
      io.cucumber.datatable.DataTable dataTable) {
    loadItemTypes(dataTable);
  }

  /**
   * Calls the deleteItemType controller method to delete the item type with the input name
   * 
   * @param itemType
   * @author Vladimir Venkov (vladimirven001)
   * @author Laurent Chiricota (larrycherry13)
   * @author Samy Sabir (SamySabir)
   * @author Jessie Kurtz (jkzcodes)
   * @author Dania Bouhmidi (dania-bh)
   * @author James Luu (Jamesluu0917)
   * @author Noah Munro-Kagan (DenzelleJinx)
   */
  @When("the manager attempts to delete an item type in the system with name {string} \\(f7)")
  public void the_manager_attempts_to_delete_an_item_type_in_the_system_with_name_f7(
      String itemType) {
    ItemTypeController.deleteItemType(itemType);
  }

  /**
   * Checks that the number of item types in the system is equal to the expected input number
   * 
   * @param expectedNumberOfItemTypes
   * @author Vladimir Venkov (vladimirven001)
   * @author Laurent Chiricota (larrycherry13)
   * @author Samy Sabir (SamySabir)
   * @author Jessie Kurtz (jkzcodes)
   * @author Dania Bouhmidi (dania-bh)
   * @author James Luu (Jamesluu0917)
   * @author Noah Munro-Kagan (DenzelleJinx)
   */
  @Then("the number of item types in the system shall be {string} \\(f7)")
  public void the_number_of_item_types_in_the_system_shall_be_f7(String expectedNumberOfItemTypes) {
    int expected = Integer.parseInt(expectedNumberOfItemTypes);
    assertEquals(expected, wareFlow.numberOfItemTypes());
  }

  /**
   * Checks that the input item types in the input dataTable exist in the system
   * 
   * @param dataTable
   * @author Vladimir Venkov (vladimirven001)
   * @author Laurent Chiricota (larrycherry13)
   * @author Samy Sabir (SamySabir)
   * @author Jessie Kurtz (jkzcodes)
   * @author Dania Bouhmidi (dania-bh)
   * @author James Luu (Jamesluu0917)
   * @author Noah Munro-Kagan (DenzelleJinx)
   */
  @Then("the following item types shall exist in the system \\(f7)")
  public void the_following_item_types_shall_exist_in_the_system_f7(
      io.cucumber.datatable.DataTable dataTable) {
    List<Map<String, String>> rows = dataTable.asMaps(String.class, String.class);
    for (Map<String, String> row : rows) {
      String name = row.get("name");
      int lifeSpan = Integer.parseInt(row.get("expectedLifeSpanInDays"));
      // Checks that the itemType object with the input name exists with the corresponding lifespan
      boolean contained =
          ItemType.hasWithName(name) && ItemType.getWithName(name).getExpectedLifeSpanInDays() == lifeSpan
              && ItemType.getWithName(name).getWareFlow() == wareFlow;
      assertTrue(contained);
    }
  }
}
