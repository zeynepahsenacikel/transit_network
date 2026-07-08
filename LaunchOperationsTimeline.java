import java.util.*;
import java.io.*;
import javax.xml.parsers.*;
import org.w3c.dom.*;

public class LaunchOperationsTimeline {

    public List<LaunchPlan> readXML(String file) {
        List<LaunchPlan> list = new ArrayList<>();
        // TODO: Parse XML and populate launch plans

        //parse the XML file using DOM parser to extract launch plans and operations
        try {
            DocumentBuilderFactory factory = DocumentBuilderFactory.newInstance();
            DocumentBuilder builder = factory.newDocumentBuilder();
            Document doc = builder.parse(new File(file));
            doc.getDocumentElement().normalize();
 
            NodeList planNodes = doc.getElementsByTagName("LaunchPlan");
            for (int i = 0; i < planNodes.getLength(); i++) {
                Element planEl = (Element) planNodes.item(i);
                String name = planEl.getElementsByTagName("PlanName").item(0).getTextContent().trim();
 
                NodeList opNodes = planEl.getElementsByTagName("Operation");
                List<Operation> ops = new ArrayList<>();
                for (int j = 0; j < opNodes.getLength(); j++) {
                    Element opEl = (Element) opNodes.item(j);
                    int code = Integer.parseInt(opEl.getElementsByTagName("Code").item(0).getTextContent().trim());
                    String label = opEl.getElementsByTagName("Label").item(0).getTextContent().trim();
                    int execTime = Integer.parseInt(opEl.getElementsByTagName("ExecutionTime").item(0).getTextContent().trim());
 
                    List<Integer> prereqs = new ArrayList<>();
                    NodeList reqNodes = opEl.getElementsByTagName("RequiresOperation");
                    for (int k = 0; k < reqNodes.getLength(); k++) {
                        prereqs.add(Integer.parseInt(reqNodes.item(k).getTextContent().trim()));
                    }
                    ops.add(new Operation(code, label, execTime, prereqs));
                }
                list.add(new LaunchPlan(name, ops));
            }
        } catch (Exception e) {
            e.printStackTrace();
        }

        return list;
    }

    public void printTimeline(List<LaunchPlan> plans) {
        // TODO: Iterate and print each plan

        //iterate through the plans list and call the print method for each one
        for (LaunchPlan p : plans) {
            p.print();
        }
    }
}