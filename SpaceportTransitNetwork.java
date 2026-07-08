import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Paths;
import java.util.*;
import java.util.regex.*;

public class SpaceportTransitNetwork {

    double shuttleSpeed;
    final double walkSpeed = 1000 / 6.0;

    Station start, end;
    List<ShuttleCorridor> corridors;

    String content;

    int getInt(String v) {
        Pattern p = Pattern.compile(v + "\\s*=\\s*([0-9]+)");
        Matcher m = p.matcher(content);
        if (m.find()) {
            return Integer.parseInt(m.group(1));
        }
        return 0;
    }

    double getDouble(String v) {
        // TODO: Regex for double

        //match and extract double values using regex pattern
        Pattern p = Pattern.compile("[\\t ]*" + v + "[\\t ]*=[\\t ]*" + "([-]?[0-9]+(?:\\.[0-9]+)?)");
        Matcher m = p.matcher(content);
        if (m.find()) {
            return Double.parseDouble(m.group(1));
        }
        return 0.0;
    }

    Point getPoint(String v) {
        // TODO: Regex for point (x,y)

        //match coordinate format like (x, y) using regex
        Pattern p = Pattern.compile("[\\t ]*" + v + "[\\t ]*=[\\t ]*" + "\\([\\t ]*" +
            "([-]?[0-9]+)" + "[\\t ]*,[\\t ]*" + "([-]?[0-9]+)" + "[\\t ]*\\)");
        Matcher m = p.matcher(content);
        if (m.find()) {
            return new Point(Integer.parseInt(m.group(1)), Integer.parseInt(m.group(2)));
        }
        return new Point(0, 0);
    }

    public String getString(String v) {
        Pattern p = Pattern.compile("[\\t ]*" + v + "[\\t ]*=[\\t ]*" + "\"([^\"]*)\"");
        Matcher m = p.matcher(content);
        if (m.find()) {
            return m.group(1);
        }
        return "";
    }

    //parseCorridors
    List<ShuttleCorridor> parseCorridors() {
        List<ShuttleCorridor> list = new ArrayList<>();
        // TODO: Parse corridor names and stations

        //split the content to parse each corridor name and its station coordinates

        //split content by corridor_name occurrences
        Pattern namePattern = Pattern.compile("corridor_name\\s*=\\s*\"([^\"]+)\"");
        Pattern pointPattern = Pattern.compile("\\([\\t ]*" + "([-]?[0-9]+)" + "[\\t ]*,[\\t ]*" + "([-]?[0-9]+)" + "[\\t ]*\\)");

        Matcher nameMatcher = namePattern.matcher(content);
 
        List<String> names = new ArrayList<>();
        List<Integer> startIndices = new ArrayList<>();

        while (nameMatcher.find()) {
            names.add(nameMatcher.group(1));
            startIndices.add(nameMatcher.end()); //newwwwwwwwwwww
        }

        for (int i = 0; i < names.size(); i++) {
            String corridorName = names.get(i);
            int startIdx = startIndices.get(i);
            int endIdx = (i + 1 < names.size()) ? startIndices.get(i + 1) : content.length();

            String block = content.substring(startIdx, endIdx);

            List<Station> stations = new ArrayList<>();
            Matcher pm = pointPattern.matcher(block);
            int stationNum = 1;
            while (pm.find()) {
                int x = Integer.parseInt(pm.group(1));
                int y = Integer.parseInt(pm.group(2));
                String stationName = corridorName + " Station " + stationNum;
                stations.add(new Station(new Point(x, y), stationName));
                stationNum++;
            }
            list.add(new ShuttleCorridor(corridorName, stations));
        }
        return list;
    }

    void readInput(String f) {
        // TODO: Read file and initialize variables

        //read all text from the file and setup the transit network variables
        try {
            content = new String(Files.readAllBytes(Paths.get(f)));
        } catch (IOException e) {
            e.printStackTrace();
            return;
        }

        shuttleSpeed = getDouble("average_shuttle_speed");
        //shuttle speed is km/h -> convert to meters per minute
        shuttleSpeed = shuttleSpeed * 1000.0 / 60.0;

        Point sp = getPoint("origin_point");
        Point ep = getPoint("destination_point");
 
        start = new Station(sp, "Origin Point");
        end = new Station(ep, "Destination Point");

        corridors = parseCorridors();
 
    }
}