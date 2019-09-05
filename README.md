# MC6007-T1-->BDA

## Development

Before you can build this project, you must install and configure the following dependencies on your machine:

1. [Jdk8][]: Install JDK from Oracle

2. [Node.js][]: We use Node to run a development web server and build the project.
   Depending on your system, you can install Node either from source or as a pre-packaged bundle.

After installing Node, you should be able to run the following command to install development tools.
You will only need to run this command when dependencies change in [package.json](package.json).

    npm install

We use npm scripts and [Webpack][] as our build system.

Run the following commands in two separate terminals to create a blissful development experience where your browser
auto-refreshes when files change on your hard drive.

    ./mvnw
    npm start

Npm is also used to manage CSS and JavaScript dependencies used in this application. You can upgrade dependencies by
specifying a newer version in [package.json](package.json). You can also run `npm update` and `npm install` to manage dependencies.
Add the `help` flag on any command to see how you can use it. For example, `npm help update`.

The `npm run` command will list all of the scripts available to run for this project.

3. Download [Intellij][] to be able to debug and open the application.

## Building for production

### Packaging as jar

To build the final jar and optimize the MC6007-T1-->BDA application for production, run:

    Unix: ./mvnw -Pprod clean package -DskipTests=true
    Windows: mvnw -Pprod clean package -DskipTests=true

This will concatenate and minify the client CSS and JavaScript files. It will also modify `index.html` so it references these new files.
To ensure everything worked, run:

    java -jar target/mc-6007-t1-0.0.1-SNAPSHOT.jar --spring.config.name=application --spring.config.location=classpath:/config/application.yml,classpath:/config/application-{DB_PORT_NUMBER_FROM_6001_TO_6004}.yml

Then navigate to [http://localhost:8080](http://localhost:8080) in your browser.

### Packaging as war

To package your application as a war in order to deploy it to an application server, run:

    Unix: ./mvnw -Pprod clean package -DskipTests=true
    Windows: mvnw -Pprod,war clean package -DskipTests=true

4. Notes:
   The databases will be created in the following path: /Berkeley/databases/, this path depends of the operating system for example in windows could start with a driver letter as C: or D: and in unix with only "/".
   The database names are: database6001, database6002, database6003, database6004

[node.js]: https://nodejs.org/
[webpack]: https://webpack.github.io/
[intellij]: https://www.jetbrains.com/idea/download/#section=windows/
[jdk8]: https://www.oracle.com/technetwork/java/javase/downloads/jdk8-downloads-2133151.html
