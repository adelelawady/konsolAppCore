<div align="center">
 
  <img src="https://github.com/user-attachments/assets/deb70e45-e60e-4dd7-b17b-1af2cfe567dc" alt="KonsolCore Logo" width="200"/>
  <h1>KonsolCore</h1>
</div>

<div align="center">

![Version](https://img.shields.io/badge/version-0.0.1-blue.svg?style=for-the-badge)
![License](https://img.shields.io/badge/license-Apache%202.0-green.svg?style=for-the-badge)
![JHipster](https://img.shields.io/badge/JHipster-7.9.3-purple.svg?style=for-the-badge&logo=jhipster)
![MongoDB](https://img.shields.io/badge/MongoDB-4.4+-darkgreen.svg?style=for-the-badge&logo=mongodb)
![Spring Boot](https://img.shields.io/badge/Spring%20Boot-2.7-brightgreen.svg?style=for-the-badge&logo=springboot)
![Angular](https://img.shields.io/badge/Angular-14.x-red.svg?style=for-the-badge&logo=angular)
![Node](https://img.shields.io/badge/Node-14.x-green.svg?style=for-the-badge&logo=node.js)
[![Downloads](https://img.shields.io/github/downloads/adelelawady/konsolAppCore/total.svg)](https://github.com/adelelawady/konsolAppCore/releases/)

<p align="center">
  <strong>🚀 A Modern Business Management System</strong><br>
  Built with JHipster, Spring Boot, and Angular
</p>

<div align="center">
  <a href="https://github.com/adelelawady/konsolAppCore/releases/download/3.0/KonsolCore.Setup.3.0.0.exe">
    <img src="https://img.shields.io/badge/Download_for-Windows-0078D6?style=for-the-badge&logo=windows&logoColor=white" alt="Download for Windows" />
  </a>
</div>

[🏠 Homepage](https://adelelawady.github.io/konsolAppCore/) |
[📖 API Documentation](ApiDocumentation.md) |
[🐛 Issues](https://github.com/adelelawady/issues) |
[📝 License](LICENSE)

</div>

## 🌟 Overview

KonsolCore is a comprehensive business management system that provides powerful APIs for:

- 🏪 Store & Inventory Management
- 📦 Item Tracking & Analysis
- 🧾 Invoice Processing
- 💰 Financial Operations
- 🎮 Gaming Services Management
- 👥 User Account Management

<div align="center">
  <img width="1125" alt="Screenshot 2025-02-27 174949" src="https://github.com/user-attachments/assets/caa63dc9-575f-40ec-abfe-b6e28dfd9c75" />
</div>

## ✨ Features

### 🏪 Store Management

- 📋 Complete store CRUD operations
- 📦 Store inventory tracking
- 🏢 Multi-store item management
- 📊 Store item quantity management
- 🏷️ Store names and metadata management

### 📦 Item Management

- 🔄 Full item lifecycle management
- 🗂️ Item categorization
- 📏 Item unit management
- 📈 Item analysis and charts
- 🗃️ Category-based item listing
- 💰 Item price tracking
- ⏭️ Previous/Next item navigation
- 📥 Bulk item operations

### 🧾 Invoice Management

- 📝 Invoice creation and management
- 📋 Invoice item tracking
- 🔍 Invoice search and filtering
- 👀 Invoice view customization
- 🔄 Invoice status management
- 📚 Batch invoice processing

### 💰 Financial Management

#### 💵 Money Management

- 💳 Financial transaction tracking
- 💱 Money movement monitoring
- 📜 Transaction history
- 📊 Financial reporting
- 🔍 Search and pagination support

#### 🏦 Bank Management

- 🏛️ Bank account operations
- 💸 Bank transaction tracking
- 📊 Bank balance analysis
- 📈 Transaction monitoring

### 👥 User Account Management

- 👤 User profile management
- 📜 Transaction history tracking
- 💰 Account balance monitoring
- 🔍 User search capabilities
- 📚 Batch user operations

### 🎮 PlayStation Service Management

- 🖥️ Device session tracking
- ⏯️ Session start/stop management
- 🧾 Session invoicing
- 🎮 Device type management
- 📊 Real-time session monitoring

## 🚀 Running the Server

### Prerequisites

Before starting the application, ensure you have:

1. MongoDB installed and running on your device

   ```bash
   # Start MongoDB (command may vary based on installation)
   mongod --dbpath=/path/to/data/db

   # Default MongoDB connection URL
   mongodb://localhost:27017/KonsolCore
   ```

2. Node.js and npm installed
3. Java 11 or later installed

### Development Mode

To run the server and frontend in development mode with hot-reload:

```bash
# Start the Spring Boot server
./mvnw
```

In a separate terminal, start the Angular development server

```bash
npm install
npm start
```

### Run compiled application jar file :

```bash
# run compiled application jar file
java -jar konsol-core-3.0.jar
```

### Production Mode

To run the server in production mode:

build the application:

```bash
./mvnw -Pprod clean verify -DskipTests
```

run the application:

```bash
java -jar target/*.jar
```

## 📚 API Documentation

### 🔗 Core API Endpoints

#### 🏪 Store Management (`/api/stores`)

- `GET /stores` - List all stores (supports pagination)
- `POST /stores` - Create new store
- `GET /stores/{id}` - Get store details
- `PATCH /stores/{id}` - Update store
- `DELETE /stores/{id}` - Delete store
- `GET /stores/names` - Get all store names
- `POST /stores/storeItems` - Manage store items

#### 📦 Item Management (`/api/items`)

- `GET /items` - List all items
- `POST /items` - Create new item
- `GET /items/{id}` - Get item details
- `PUT /items/{id}` - Update item
- `DELETE /items/{id}` - Delete item
- `GET /items/{id}/units` - Get item units
- `POST /items/view` - Search items
- `POST /items/{id}/analysis` - Get item analysis
- `POST /items/{id}/charts` - Get item charts

#### 🧾 Invoice Management (`/api/invoices`)

- `GET /invoices` - List all invoices
- `POST /invoices` - Create invoice
- `GET /invoices/{id}` - Get invoice details
- `PATCH /invoices/{id}` - Update invoice
- `DELETE /invoices/{id}` - Delete invoice
- `POST /invoices/view` - Search invoices
- `GET /invoices/{id}/invoiceItems` - Get invoice items

#### 💰 Financial Management

##### Money Management (`/api/monies`)

- `GET /monies` - List all transactions
- `POST /monies` - Create transaction
- `GET /monies/{id}` - Get transaction details
- `POST /monies/view` - Search transactions

##### 🏦 Bank Management (`/api/banks`)

- `GET /banks/{id}` - Get bank details
- `POST /banks/{id}/transactions` - Get bank transactions
- `GET /banks/{id}/analysis` - Get bank analysis

#### 🎮 PlayStation Management (`/api/playstation`)

- `POST /playstation/device/{id}/session/start` - Start device session
- `POST /playstation/device/{id}/session/stop` - Stop device session
- `POST /playstation/device/{id}/session/invoice/update` - Update session invoice
- `GET /playstation/device/type/{id}` - Get device types

### 📖 API Documentation Access

The API documentation is available through OpenAPI (Swagger):

- 📑 OpenAPI JSON: http://localhost:8080/api/v3/api-docs
- 🔍 Swagger UI: http://localhost:8080/swagger-ui.html

To enable API documentation, ensure the `api-docs` profile is active in your configuration:

## Project Structure

Node is required for generation and recommended for development. `package.json` is always generated for a better development experience with prettier, commit hooks, scripts and so on.

In the project root, JHipster generates configuration files for tools like git, prettier, eslint, husky, and others that are well known and you can find references in the web.

`/src/*` structure follows default Java structure.

- `.yo-rc.json` - Yeoman configuration file
  JHipster configuration is stored in this file at `generator-jhipster` key. You may find `generator-jhipster-*` for specific blueprints configuration.
- `.yo-resolve` (optional) - Yeoman conflict resolver
  Allows to use a specific action when conflicts are found skipping prompts for files that matches a pattern. Each line should match `[pattern] [action]` with pattern been a [Minimatch](https://github.com/isaacs/minimatch#minimatch) pattern and action been one of skip (default if ommited) or force. Lines starting with `#` are considered comments and are ignored.
- `.jhipster/*.json` - JHipster entity configuration files

- `npmw` - wrapper to use locally installed npm.
  JHipster installs Node and npm locally using the build tool by default. This wrapper makes sure npm is installed locally and uses it avoiding some differences different versions can cause. By using `./npmw` instead of the traditional `npm` you can configure a Node-less environment to develop or test your application.
- `/src/main/docker` - Docker configurations for the application and services that the application depends on

## Development

Before you can build this project, you must install and configure the following dependencies on your machine:

1. [Node.js][]: We use Node to run a development web server and build the project.
   Depending on your system, you can install Node either from source or as a pre-packaged bundle.

After installing Node, you should be able to run the following command to install development tools.
You will only need to run this command when dependencies change in [package.json](package.json).

```
npm install
```

We use npm scripts and [Angular CLI][] with [Webpack][] as our build system.

Run the following commands in two separate terminals to create a blissful development experience where your browser
auto-refreshes when files change on your hard drive.

```
./mvnw
npm start
```

Npm is also used to manage CSS and JavaScript dependencies used in this application. You can upgrade dependencies by
specifying a newer version in [package.json](package.json). You can also run `npm update` and `npm install` to manage dependencies.
Add the `help` flag on any command to see how you can use it. For example, `npm help update`.

The `npm run` command will list all of the scripts available to run for this project.

### PWA Support

JHipster ships with PWA (Progressive Web App) support, and it's turned off by default. One of the main components of a PWA is a service worker.

The service worker initialization code is disabled by default. To enable it, uncomment the following code in `src/main/webapp/app/app.module.ts`:

```typescript
ServiceWorkerModule.register('ngsw-worker.js', { enabled: false }),
```

### Managing dependencies

For example, to add [Leaflet][] library as a runtime dependency of your application, you would run following command:

```
npm install --save --save-exact leaflet
```

To benefit from TypeScript type definitions from [DefinitelyTyped][] repository in development, you would run following command:

```
npm install --save-dev --save-exact @types/leaflet
```

Then you would import the JS and CSS files specified in library's installation instructions so that [Webpack][] knows about them:
Edit [src/main/webapp/app/app.module.ts](src/main/webapp/app/app.module.ts) file:

```
import 'leaflet/dist/leaflet.js';
```

Edit [src/main/webapp/content/scss/vendor.scss](src/main/webapp/content/scss/vendor.scss) file:

```
@import '~leaflet/dist/leaflet.css';
```

Note: There are still a few other things remaining to do for Leaflet that we won't detail here.

For further instructions on how to develop with JHipster, have a look at [Using JHipster in development][].

### Using Angular CLI

You can also use [Angular CLI][] to generate some custom client code.

For example, the following command:

```
ng generate component my-component
```

will generate few files:

```
create src/main/webapp/app/my-component/my-component.component.html
create src/main/webapp/app/my-component/my-component.component.ts
update src/main/webapp/app/app.module.ts
```

### JHipster Control Center

JHipster Control Center can help you manage and control your application(s). You can start a local control center server (accessible on http://localhost:7419) with:

```
docker-compose -f src/main/docker/jhipster-control-center.yml up
```

### Doing API-First development using openapi-generator-cli

[OpenAPI-Generator]() is configured for this application. You can generate API code from the `src/main/resources/swagger/api.yml` definition file by running:

```bash
./mvnw generate-sources
```

Then implements the generated delegate classes with `@Service` classes.

To edit the `api.yml` definition file, you can use a tool such as [Swagger-Editor](). Start a local instance of the swagger-editor using docker by running: `docker-compose -f src/main/docker/swagger-editor.yml up -d`. The editor will then be reachable at [http://localhost:7742](http://localhost:7742).

Refer to [Doing API-First development][] for more details.

## Building for production

### Packaging as jar

To build the final jar and optimize the KonsolCore application for production, run:

```
./mvnw -Pprod clean verify -DskipTests
```

This will concatenate and minify the client CSS and JavaScript files. It will also modify `index.html` so it references these new files.
To ensure everything worked, run:

```
java -jar target/*.jar
```

Then navigate to [http://localhost:8080](http://localhost:8080) in your browser.

Refer to [Using JHipster in production][] for more details.

### Packaging as war

To package your application as a war in order to deploy it to an application server, run:

```
./mvnw -Pprod,war clean verify
```

## Testing

To launch your application's tests, run:

```
./mvnw verify
```

### Client tests

Unit tests are run by [Jest][]. They're located in [src/test/javascript/](src/test/javascript/) and can be run with:

```
npm test
```

For more information, refer to the [Running tests page][].

### Code quality

Sonar is used to analyse code quality. You can start a local Sonar server (accessible on http://localhost:9001) with:

```
docker-compose -f src/main/docker/sonar.yml up -d
```

Note: we have turned off authentication in [src/main/docker/sonar.yml](src/main/docker/sonar.yml) for out of the box experience while trying out SonarQube, for real use cases turn it back on.

You can run a Sonar analysis with using the [sonar-scanner](https://docs.sonarqube.org/display/SCAN/Analyzing+with+SonarQube+Scanner) or by using the maven plugin.

Then, run a Sonar analysis:

```
./mvnw -Pprod clean verify sonar:sonar
```

If you need to re-run the Sonar phase, please be sure to specify at least the `initialize` phase since Sonar properties are loaded from the sonar-project.properties file.

```
./mvnw initialize sonar:sonar
```

For more information, refer to the [Code quality page][].

## Using Docker to simplify development (optional)

You can use Docker to improve your JHipster development experience. A number of docker-compose configuration are available in the [src/main/docker](src/main/docker) folder to launch required third party services.

For example, to start a mongodb database in a docker container, run:

```
docker-compose -f src/main/docker/mongodb.yml up -d
```

To stop it and remove the container, run:

```
docker-compose -f src/main/docker/mongodb.yml down
```

You can also fully dockerize your application and all the services that it depends on.
To achieve this, first build a docker image of your app by running:

```
npm run java:docker
```

Or build a arm64 docker image when using an arm64 processor os like MacOS with M1 processor family running:

```
npm run java:docker:arm64
```

Then run:

```
docker-compose -f src/main/docker/app.yml up -d
```

When running Docker Desktop on MacOS Big Sur or later, consider enabling experimental `Use the new Virtualization framework` for better processing performance ([disk access performance is worse](https://github.com/docker/roadmap/issues/7)).

For more information refer to [Using Docker and Docker-Compose][], this page also contains information on the docker-compose sub-generator (`jhipster docker-compose`), which is able to generate docker configurations for one or several JHipster applications.

## Continuous Integration (optional)

To configure CI for your project, run the ci-cd sub-generator (`jhipster ci-cd`), this will let you generate configuration files for a number of Continuous Integration systems. Consult the [Setting up Continuous Integration][] page for more information.

[jhipster homepage and latest documentation]: https://www.jhipster.tech
[jhipster 7.9.3 archive]: https://www.jhipster.tech/documentation-archive/v7.9.3
[using jhipster in development]: https://www.jhipster.tech/documentation-archive/v7.9.3/development/
[using docker and docker-compose]: https://www.jhipster.tech/documentation-archive/v7.9.3/docker-compose
[using jhipster in production]: https://www.jhipster.tech/documentation-archive/v7.9.3/production/
[running tests page]: https://www.jhipster.tech/documentation-archive/v7.9.3/running-tests/
[code quality page]: https://www.jhipster.tech/documentation-archive/v7.9.3/code-quality/
[setting up continuous integration]: https://www.jhipster.tech/documentation-archive/v7.9.3/setting-up-ci/
[node.js]: https://nodejs.org/
[npm]: https://www.npmjs.com/
[webpack]: https://webpack.github.io/
[browsersync]: https://www.browsersync.io/
[jest]: https://facebook.github.io/jest/
[leaflet]: https://leafletjs.com/
[definitelytyped]: https://definitelytyped.org/
[angular cli]: https://cli.angular.io/
[openapi-generator]: https://openapi-generator.tech
[swagger-editor]: https://editor.swagger.io
[doing api-first development]: https://www.jhipster.tech/documentation-archive/v7.9.3/doing-api-first-development/
