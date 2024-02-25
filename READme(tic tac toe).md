# Tic Tac Toe Game

This is a simple Tic Tac Toe game implemented in Java with a graphical user interface (GUI). Players take turns marking spaces in a 3x3 grid, aiming to be the first to get three of their marks in a row.

## Features

- Graphical user interface built using Java Swing.
- Tracks player scores and displays winning ratios.
- Stores high scores in a MySQL database.
- Allows resetting of scores.
- Shows statistics of the game, including winning ratios for X and O players and draw ratios.

## Prerequisites

- Java Development Kit (JDK) installed on your system.
- MySQL database server set up and running.

## Usage

-Players take turns clicking on empty spaces in the grid to mark them with their symbol (X or O).
-The game tracks the scores of X and O players, as well as draw games.
-Click on the "Show Stats" menu item to display the winning ratios of X and O players, as well as the draw ratio.
-Click on the "High Scores" menu item to display the top scores stored in the database.
-Click on the "Reset Scores" button to clear all scores stored in the database.
-Enjoy playing Tic Tac Toe!

## Setup

1. Set up the MySQL database:

- Create a MySQL database named `tictactoe`.
- Create a table named `scores` with two columns: `player` (VARCHAR) and `wins` (INT).

2. Open the project in your Java IDE.

3. Add the MySQL JDBC driver to your project's dependencies. You can download the MySQL JDBC driver from [here](https://dev.mysql.com/downloads/connector/j/).

4. Update the MySQL connection details in the `TicTacToeGUI.java` file:

```java
connection = DriverManager.getConnection("jdbc:mysql://localhost:3306/shubham", "root", "");
