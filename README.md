# Building Lift System Simulation

Building Lift System Simulation, supports any number of lifts and floors. 
Implements resource-intensive service strategy but can be tweaked to support more,
for example resource saving strategy (less lifts in transit, more stops per lift). 

## Getting Started

please set up Java and Maven to compile and run tests
need: Java 8+, Maven 3+;

*** found out that the build works without glitches on OpenJDK11/12 ***
*** issues with Oracle JDK8 probably due to VAVR library ***

## Running the tests

"mvn clean package" command will execute unit tests

### Unit tests

1. Four use test cases to test individual lift actions under different 
   conditions: going up from the 1st floor, going down from the last floor, 
               going to the last floor to pick up passengers going down, 
               after task completed - go to designated floor to meet 
               rush hour recommendations. 
2. Overall system test - 3 lifts servicing calls - up and down under rush hour (PM)
   condition. 

## Built With

* [Eclipse](https://www.eclipse.org/) - The IDE
* [Maven](https://maven.apache.org/) - Dependency Management

## Authors

* **Alex Pervukhin** - *Initial work* - [alexpervukhin](https://github.com/alexpervukhin/system)

## License

This project is licensed under the MIT License - see the [LICENSE.md](LICENSE.md) file for details

