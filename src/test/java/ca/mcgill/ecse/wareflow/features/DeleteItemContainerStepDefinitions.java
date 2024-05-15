package ca.mcgill.ecse.wareflow.features;

import static org.junit.jupiter.api.Assertions.assertEquals;
import java.sql.Date;
import java.util.List;
import java.util.Map;
import ca.mcgill.ecse.wareflow.application.WareFlowApplication;
import ca.mcgill.ecse.wareflow.controller.ItemContainerController;
import ca.mcgill.ecse.wareflow.model.ItemContainer;
import ca.mcgill.ecse.wareflow.model.ItemType;
import io.cucumber.java.en.Given;
import io.cucumber.java.en.Then;
import io.cucumber.java.en.When;

public class DeleteItemContainerStepDefinitions {
  /**
   * Gherkin step definition method to create and add item types to the WareFlow application.
   * 
   * @param dataTable Cucumber DataTable containing the item type information.
   *
   * @author Julia B. Grenier
   * @author Tayba Jusab
   */
  @Given("the following item types exist in the system \\(f9)")
  public void the_following_container_types_exist_in_the_system_f9(
      io.cucumber.datatable.DataTable dataTable) {
    // Turns the dataTable into a list of lists (each row becomes a list).
    List<Map<String, Object>> tableList = dataTable.asMaps(String.class, Object.class);

    // Iterates through each list to create the specified item types and add it to the WareFlow
    // application.
    for (Map<String, Object> row : tableList) {
      String name = (row.get("name")).toString();
      int expectedLifeSpanInDays = Integer.parseInt(row.get("expectedLifeSpanInDays").toString());
      ItemType type = WareFlowApplication.getWareFlow().addItemType(name, expectedLifeSpanInDays);
      WareFlowApplication.getWareFlow().addItemType(type);
    }
  }

  /**
   * Gherkin step definition method to create and add the specific containers to the WareFlow
   * application.
   * 
   * @param dataTable Cucumber DataTable containing specific container information.
   * 
   * @author Sahar Fathi
   * @author Anjali Singhal
   * @author Julia B. Grenier
   * @author Tayba Jusab
   * @author Camille Pouliot
   * @author Émilia Gagné
   */
  @Given("the following containers exist in the system \\(f9)")
  public void the_following_containers_exist_in_the_system_f9(
      io.cucumber.datatable.DataTable dataTable) {
    // Turns the data table into a list of HashMaps for which the column name is the key.
    List<Map<String, Object>> tableList = dataTable.asMaps(String.class, Object.class);

    // Iterate through each map representing a row and cast it to the appropriate type.
    for (Map<String, Object> row : tableList) {
      int containerNumber = Integer.parseInt(row.get("containerNumber").toString());
      String itemType = (row.get("type")).toString();
      int areaNumber = Integer.parseInt(row.get("areaNumber").toString());
      int slotNumber = Integer.parseInt(row.get("slotNumber").toString());
      Date addedOnDate = Date.valueOf(row.get("addedOnDate").toString());

      // Adding the specific container based on the table information.
      ItemContainer itemContainer = WareFlowApplication.getWareFlow().addItemContainer(
          containerNumber, areaNumber, slotNumber, addedOnDate, ItemType.getWithName(itemType));
      WareFlowApplication.getWareFlow().addItemContainer(itemContainer);
    }

  }

  /**
   * Gherkin step definition method to delete the specific container specified by number from the
   * WareFlow application.
   * 
   * @param containerNumber Specific container number associated to the container to be deleted.
   * 
   * @author Camille Pouliot
   * @author Émilia Gagné
   */
  @When("the manager attempts to delete the container with number {string} \\(f9)")
  public void the_manager_attempts_to_delete_the_container_with_number_f9(String containerNumber) {
    // Removes the specific container based on the container number given.
    ItemContainerController.deleteItemContainer(Integer.parseInt(containerNumber));

  }

  /**
   * Gherkin step definition method to verify the amount of containers in the application following
   * the deletion of a specific container.
   * 
   * @param expectedNumberOfContainers Expected number of containers after the specific container has
   *        been deleted.
   * 
   * @author Sahar Fathi
   * @author Anjali Singhal
   */
  @Then("the number of containers in the system shall be {string} \\(f9)")
  public void the_number_of_containers_in_the_system_shall_be_f9(
      String expectedNumberOfContainers) {
    // Confirms that the amount of containers has gone down after remvoving an container.
    assertEquals(Integer.parseInt(expectedNumberOfContainers),
        WareFlowApplication.getWareFlow().getItemContainers().size());
  }

  /**
   * Gherkin step definition method to verify that the correct containers still exist in the
   * WareFlow application.
   * 
   * @param dataTable Cucumber DataTable containing specific container information of the containers
   *        which should still be existing in the WareFlow application.
   * @author Sahar Fathi
   * @author Anjali Singhal
   * @author Julia B. Grenier
   * @author Tayba Jusab
   * @author Camille Pouliot
   * @author Émilia Gagné
   */

  @Then("the following containers shall exist in the system \\(f9)")
  public void the_following_containers_shall_exist_in_the_system_f9(
      io.cucumber.datatable.DataTable dataTable) {
    // Turns the data table into a list of HashMaps with the column name as the key.
    List<Map<String, Object>> tableList = dataTable.asMaps(String.class, Object.class);
    // This loop is used to iterate through the table at the same time as the containers in the
    // application, in order
    // to assert that they are equals with respect to their fields. Application counter augments the
    // index of the application container list.
    int applicationCounter = 0;
    for (Map<String, Object> row : tableList) {
      ItemContainer container =
          WareFlowApplication.getWareFlow().getItemContainers().get(applicationCounter);
      assertEquals(Integer.parseInt(row.get("containerNumber").toString()),
          container.getContainerNumber());
      assertEquals(row.get("type").toString(), container.getItemType().getName());
      assertEquals(Integer.parseInt(row.get("areaNumber").toString()), container.getAreaNumber());
      assertEquals(Integer.parseInt(row.get("slotNumber").toString()), container.getSlotNumber());
      assertEquals(Date.valueOf(row.get("addedOnDate").toString()), container.getAddedOnDate());
      applicationCounter++;
    }
  }
}
