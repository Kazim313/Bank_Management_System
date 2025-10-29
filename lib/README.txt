Place external libraries (JAR files) in this folder.

For this project, copy the MySQL Connector/J here, e.g.:

  mysql-connector-java-8.0.28.jar

Example full path on your machine:
  C:\Users\Kazim\Desktop\Bank\lib\mysql-connector-java-8.0.28.jar

Compile (PowerShell):
  cd C:\Users\Kazim\Desktop\Bank
  if (!(Test-Path out)) { mkdir out }
  $jar = "lib\mysql-connector-java-8.0.28.jar"
  javac -cp ".;$jar" -d out (Get-ChildItem -Recurse src\main\java\*.java | % FullName)

Run (PowerShell):
  $jar = "lib\mysql-connector-java-8.0.28.jar"
  java -cp ".;out;$jar" com.banking.BankingApplication


