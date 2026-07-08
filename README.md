# AstraNova Spaceport: Launch Preparation & Transit Network 🚀🌌

## 🏗️ Project Architecture & Modules

The project is divided into two primary operational components:

### ⏱️ Part I: Launch Operations Timeline
* **Objective:** Compute the earliest possible activation, completion, and total readiness times for interdependent pre-launch procedures.
* **Algorithms & Logic:**
  * **Kahn's Algorithm (Topological Sort):** Resolves directed operational dependencies to establish a feasible graph-based sequence.
  * **Critical Path Scheduling:** Analyzes task execution durations to determine the exact minimum hours required until the mission status becomes "Launch-Ready".

### 🗺️ Part II: Spaceport Transit Planner
* **Objective:** Find the fastest route between any 2D coordinate points inside the facility by combining walking routes and autonomous shuttle lines.
* **Algorithms & Logic:**
  * **Dijkstra's Shortest Path Algorithm:** Evaluates the spaceport network to combine walking intervals (defaulting to 10 km/h) and directed shuttle corridor arcs into a minimum-time journey.
  * **Robust RegEx Parsing:** Utilizes Java regular expressions (`java.util.regex`) to robustly parse unstructured `.dat` configuration maps, tolerating flexible white-spaces and layout variations.

## 📂 File Structure

The project maps directly to the following clean flat-file architecture:

```
├── Main.java                        # System launcher and orchestrator
├── LaunchOperationsTimeline.java    # XML parser and report runner for Part I
├── LaunchPlan.java                  # Schedule optimizer (Kahn's & scheduling)
├── Operation.java                   # Model representing an individual launch task
├── SpaceportTransitNetwork.java     # RegEx input parsing engine for facility maps
├── SpaceportTransitPlanner.java     # Dijkstra shortest path routing core
├── ShuttleCorridor.java             # Shuttle lane line representation
├── Station.java                     # Transit station structure
├── Point.java                       # 2D coordinate map indicator (x, y)
├── Edge.java                        # Weighted edge for transit optimization
├── RouteInstruction.java            # Formatted step-by-step route descriptions
└── LaunchPlans.xml                  # Mission preparation configuration blueprint
```

## 🚀 Getting Started (Terminal Instructions)

* **Prerequisites:**
Java Development Kit (JDK 11 or higher) configured in your system environment.

### Compile the Project

```javac *.java```

### Run the Application

```java Main LaunchPlans.xml TransitNetwork.dat```