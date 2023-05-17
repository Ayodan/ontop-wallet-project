Guide for running the project.
Clone the repository from this link https://github.com/Ayodan/ontop-wallet-project

Open project on any IDE prefarably intellij which runs on java jdk 11 and above

Allow maven to download and add all dependencies to classpath

Set up docker desk top locally and spin up a postgres conatainer

Connect to postgres database using client like dbeaver and create a database with name "ontop"

Run the application locally with intellij

To run with docker, run mvn clean and mvn package in the root folder to generate a ontop.jar file

In the root folder of the project in terminal, create docker build -t {image_name} . to spin up a conatainer for the project
