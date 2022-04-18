# Movie Theater Seating Challenge

## Assumptions

- Customer satisfaction can be achieved by prioritizing seating for groups consecutively within the same row, space 
permitting. Additionally, it is assumed that customers prefer to sit as far back from the screen as possible. Therefore, 
seating allocation will begin at the row farthest from the screen (for our purposes this is row A seat 1).
- Customer safety requirements can be achieved by blocking out ever other row in front of a customer group (along with the 
3 seat buffer). Therefore, it is assumed that every other row in the theater will not be used for seating allocations. 
- Seating will be allocated on a first come, first served basis. Meaning that the input file will be processed line
by line with R001 being the first allocation of seating.
- Customers agree to have their group split up if consecutive seating within the same row cannot be accommodated and 
there are enough seats in the theater,

## Build Scripts
### Before building:
1. In a terminal window, navigate to the root directory of the project.

The following script builds and tests the project:
```commandline
./gradlew build
```
The following script runs the unit tests:
```commandline
./gradlew test
```
### Before running:
1. Fork and clone this project on your local machine where you will be running it.
2. Create an Input.txt file. For example:
> R001 2
> 
> R002 3
> 
> R003 4
> 
> ...
3. In a terminal window, navigate to the root directory of the project.

To run the application, type the following into a terminal window, **but substituting the absolute file path of the actual
Input.txt file being used**.

```commandline
./gradlew run --args="/absolute/file/path/Input.txt"
```

