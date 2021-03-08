# Auction-System
This program is the final project at the Object-Oriented Programming course and represents an Auction House with Clients, Employees and Brokers, classes implemented using different Design Patterns.

Main functionalities:
-Design Patterns: Sigleton, Builder, Abstract Factory, Observer
-Generics
-Tests(I created json and excel files and read from those)
-multithreading
-sonarLint for better code
-Unit testing using JUnit 5
-lambda expressions

<img src="https://user-images.githubusercontent.com/56924647/110312337-a371b380-800d-11eb-8861-03dba7a82c4d.png" width="30%"></img> 


Implemented Design Patterns:
	Singleton(Administrator + Auction House)
	Builder(Product + Client)
	Abstract Factory(Product and Client factories)
	Observer(the clients are the observers, the brokers are the subjects)

Genericity: In addition to the parameterized data types used, I also have
made generic methods, such as the copyTo method that copies information
from a superclass to any subclass that extends it. Also a greater
functionality implemented using genericity is the Abstract Factory,
which can be parameterized with both <Client> and <Product>. 

Tests: In the tests folder, each test has a JSON file and an XLSX file.

Multithreading: I implemented multithreading based on the Producer-Consumer model,
where the producer is the administrator and the consumers are the brokers and
the customers. These 3 entities do not implement runnable directly, but have methods
which return a class that implements runnable for the required task.
These tasks are of 3 types: AddTask used by the Administrator, Deletetask
used by the Broker and ReadTask used by the Client. 

Implemented bonuses:
  I used SonarLint and followed as many code refactoring suggestions as possible
  provided by it

  I made 10 test units, with the JUnit5 library; tests for
  functionalities that I considered major in my application

  As extra implementation and functionality, I made 2 specialized classes
  in reading JSON and Excel files, and I used lambda expressions

For this project I read data from two types of files: a JSON file from which
I practically read all the initial information of the Auction House which
I implemented with Singleton. From JSON files, I read the lists of
products, list of clients, list of active auctions, list of brokers and
the administrator. For reading this, I used a helper class called
JSONReader, and the external JSONReader library. Products and clients are
instantiated using the AbstractFactory. After instantiating each object,
I add them in the unique instance of the auction house. 

After reading the information for the house, I also conceived an XLSX file,
from which I read the actual "orders", i.e. the requests created by customers for
certain products. These Excel files have 3 columns, the first column with the id
of the customer making the request, the second column with the id of the requested
product, and a third column with the maximum amount he is willing to provide for
product. Using an iterator, I go through each line in the Excel file, I read
the information and then call the client's signUp method. This method will
sign up for a possible auction for that product.

After calling the signUp method of a certain product ID, the auction house
will take the request, and initially check if it is valid. In other
words, it will search if the product requested by the customer exists in
the list. Then it will check if the same customer has not requested the same
product before. For all these situations, I created exceptions. After the validity
of the sign up application is confirmed, a possible auction for the requested product
is sought, and if found, the client is added to it. If the required number
of participants is reached for that auction, then the auction starts automatically.
When the auction starts, the auction house immediately notifies the brokers. These
in turn notify the customers (who are randomly assigned upon registration).
By notifying customers, who are in fact observers, I meant to assign them
an instance of a class called by me Information. I created this class to facilitate
communication between brokers and clients.

In the final step, the maximum bid is known. The auction house asks the
brokers to notify the winning client. Each broker searches the list of
customers for the winner, which they notify. This notification represents
a modification of his information file, and the program displays a colored 
String on the screen. Not lastly, the broker takes the winner's initial commission,
that is, it takes a percentage of the bid. At the end, the communication ends
between brokers and clients, i.e. brokers delete clients' information files
from the list. 

I also used unit testing to confirm some important functionalities, such as:
-all tests to run successfully
-multithreading to work
-customers really have unique ids
-exceptional situations for customers or invalid products are treated accordingly
-deterministic result, not influenced by the broker's randomization
-valid commission 

In the main function I  declare a constant that represents the test number,
and then call the 2 static file reading methods.
