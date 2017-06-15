# student-debt-visualiser
`student-debt-visualiser` is JavaFX application to visualise student debt.

Are you a UK student or graduate who took a loan out with the Student Loans Company on or after 1st September 2012 (~£9000/yr fees) ?
Have a look at how your debt will behave over the course of its potential 30 year lifespan. 
Estimate how much interest is accumulated on your debt based on an income projection you draw with your mouse. 
(NB: an exact calculation would require a forecast of inflation for the next 30 years. Here it is assumed to be constant at 2%)

## Installation
Open up a terminal window and clone this repository
```
  $ git clone https://github.com/sina-al/student-debt-visualiser.git
```
Change directory to repository
```
  $ cd student-debt-visualiser
```
Run the application using gradle
```
  $ ./gradlew jfxRun
```

## Usage
Draw an income projection by clicking and dragging your mouse over the chart area. 
You can change the starting debt if you wish, which is set by default to the typical graduate debt of £41,000

