package ca.mcgill.ecse.wareflow.application;

import ca.mcgill.ecse.wareflow.javafx.fxml.WareFlowFxmlView;
import java.sql.Date;
import java.time.LocalDate;
import ca.mcgill.ecse.wareflow.javafx.fxml.WareFlowFxmlView;
import ca.mcgill.ecse.wareflow.model.WareFlow;
import ca.mcgill.ecse.wareflow.persistence.WareflowPersistence;
import javafx.application.Application;

public class WareFlowApplication {

  private static WareFlow wareFlow;
  public static final boolean DARK_MODE = true;
  private static Date currentDate;

  public static void main(String[] args) {
	  wareFlow = getWareFlow();
	  Application.launch(WareFlowFxmlView.class, args);
  }
  public static WareFlow getWareFlow() {
    if (wareFlow == null) {
      // these attributes are default, you should set them later with the setters
      wareFlow = WareflowPersistence.load();
    }
    return wareFlow;
  }

  public static Date getCurrentDate() {
    if (currentDate == null) {
      return Date.valueOf(LocalDate.now());
    }
    return currentDate;
  }

  public static void setCurrentDate(Date date) {
    currentDate = date;
  }
}