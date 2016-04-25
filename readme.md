# Optimal Photovoltaic Systems for Residential Areas (OPSRA)
#### The official repository for the FURI research project of the same name
Created and maintained by Joseph Thweatt (Undergraduate Researcher), under the mentorship of Professor Rong Pan.

# Abstract
This research project seeks to develop decision-making methods for planning the construction of photovoltaic systems. Such methods are based on the indexing and ranking of essential parts (i.e. panels, inverters, batteries) as they pertain to the user’s purpose in building the system. From the research, one can see how photovoltaic systems can be specialized for certain purposes by analyzing the unique properties of the parts that comprise it. The scope of the research is limited to residential locations, but could be scaled to serve the needs of industrial locations if the demand is sufficient.

# Overview
OPSRA is a residential utility designed to provide users with custom PV Systems based on their location and personal needs. As of the conclusion of this FURI season, Spring 2016, the project has achieved a working state. Its capabilities are as such:
* The program can read information on photovoltaic system modules from a sqlite database and store the products into objects. The code for the objects can be found in the [Product Objects] folder.
* The program can take a user's constraints, including their budget, location, and power requirements, and use that information to find a set of systems which satisfy those constraints.
* The program can consult APIs, such as the [PVWatts solar calculator], to better determine the effectiveness of a system combination.
* The program implements a decision tree-like methodology to find workable systems for the user, then ranks them according to the user's top priority (here, we rank the systems according to price).
* The program is structured such that more algorithms, rankings, and API's can be implemented for greater precision.

Updates on the master branch will be closed until further notice. Those wishing to work with the program are encouraged to fork it and make improvements as they please.

# Using OPSRA
The current version of OPSRA consists of two packages, [System Creation] and [Product Objects]. Both packages are implemented into [PowerInfoMain], which runs a main class providing a demonstration of a user request. To implement OPSRA into your own program or application, rewrite PowerInfoMain or replace it with your own code. If you intend to modify OPSRA, first read through the package information below.

### The ProductObjects Package
This package is designed to store and manipulate product information of various parts of the system. The package currently has nine individual product types, including panels, inverters, etc. ProductObjects also has one object to hold a complete amalgam of parts (called [FullSystem], which also handles API requests) and an object to hold user constraints and location (called [Goal]). 

### The SystemCreation Package
This package handles processes essential to finding optimal systems for the user. It implements [Product Objects] to handle and manipulate product-specific information, then uses the information to make a list of possible systems. [Algorithms] is the abstract class which all decision-making class must extend. Currently, the only class in use is the [Pricing], so use that for reference. Also within the package is [DBExtraction], which takes product data from the sqlite database and converts it to objects.



[System Creation]: </src/SystemCreation>
[Product Objects]: </src/ProductObjects>
[PowerInfoMain]: </src/PowerInfoMain.java>
[FullSystem]: </src/ProductObjects/FullSystem.java>
[Goal]: </src/ProductObjects/Goal.java>
[DBExtraction]: </src/SystemCreation/DBExtraction.java>
[Algorithms]: </src/SystemCreation/Algorithms.java>
[Pricing]: </src/SystemCreation/Pricing.java>
[PVWatts solar calculator]: <https://developer.nrel.gov/docs/solar/pvwatts-v5/>
