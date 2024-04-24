> [!CAUTION]
> This Testing Framework has the same base as this repository https://github.com/ismam9/java-core-web-automation-bdd . So I am not re-explain the tools we using, the project structure, and more information available on the
> repository I mentioned. The only things that change are that we are using Junit4 instead of JUnit5 thus Cucumber and Allure are implemented with JUnit4, **not with JUnit5**

# Resume
I am a Software QA Automation Engineer with experience primarily in Functional Testing for Web Browsers and Mobile Applications.

This guide is intended for beginners or junior professionals who aspire to develop a comprehensive Framework for conducting Functional Testing. There are numerous tools and frameworks available for Testing, and this guide focuses on those based on widely-used technologies such as Selenium and Cucumber. It aims to assist in common QA Automation tasks, such as implementing a Testing Framework for Regression Testing, Smoke Testing, Sanity Testing, etc.

# Purpose of the Framework.
The purpose of this guide is to explain the way we build-up & generate an allure report as a single-file-html so we can move the report to others directories or sending it vía email, etc.

Allure is one of the most used Language and Framework agnostic, but communly is it used as a Report System. It generates interactive and detailed test reports & it is open-source. It can be implemented with most of the Software Testing Tools, CI/CD, etc. It is usefull and can be used by Testers, Developers, Devops or Project Managers. 

They way it works is simple, it collects data during the execution of our project and organizated it into structured format. We can see this things:

- Test Execution: When you execute your tests, Allure captures various types of data such as test results, attachments, descriptions, steps, parameters, and more.
- Listeners: Allure integrates with test frameworks like TestNG, JUnit, Cucumber, etc., using listeners or adapters. These listeners capture events during test execution, such as test start, test finish, step execution, etc.
- Data Collection: As the tests run, Allure collects information from these listeners and stores it in memory.
- Report Generation: After the test execution is complete, Allure generates XML files containing all the collected data in a structured format. These XML files are then processed to generate the final HTML report.
- HTML Report: The HTML report is generated using the collected data from the XML files. It includes detailed information about test execution, such as test statuses (pass/fail/skipped), execution time, steps, attachments, parameters, and more. The report is highly interactive and user-friendly, allowing easy navigation and analysis of test results.
- Attachments: Allure supports attaching various types of files (screenshots, logs, videos, etc.) to test reports, providing additional context and aiding in debugging.
- History and Trends: Allure can also generate historical reports, showing trends in test execution over time. This helps in identifying patterns, tracking progress, and making data-driven decisions.

As we are using `https://github.com/ismam9/java-core-web-automation-bdd` as the base of our project. It could be a good idea to clone the mentioned repository or this one. 
> [!IMPORTANT]
> There is a change between this two repositories for the allure version we use. The differences is the allure.version on the pom.xml file on this repository we set `<allure.version>2.26.0</allure.version>` but on this repo https://github.com/ismam9/java-core-web-automation-bdd we set <allure.version>2.24.0</allure.version>. This change must not affect the rest of the workflow.


# Project
As we said, we are using JUnit4 instead of JUnit5. So the changes are in the project are:
- The TestRunner configuration ir is the same, but the annotation we use are different. In the pom.xml we use cucumber-junit and not cucumber-junit-platform-engine
[Cucumber-Junit](https://cucumber.io/docs/installation/java/)
[Cucumber-Junit4](https://cucumber.io/docs/cucumber/api/?lang=java#junit)
- The rest of the classes are structured identically.
- The pom.xml it is were we can find more changes. Apart from Cucumber been set-up with JUnit4. Allure also it is set with JUni4.
[Allure-CucumberJVM](https://allurereport.org/docs/cucumberjvm/). You can see here the implementation of Allure on Cucumber-JVM with JUnit4 and JUnit5. **We are interested on the implementation with JUnit4**
[Allure-Starter](https://allurereport.org/start/). We use from the started the `<dependencyManagement>` and the `<depedencies>`. Obviously you should already have the dependecies of selenium, commons etc (the dependecies to make your script work)

# Allure

We saw on this other repo `https://github.com/ismam9/java-core-web-automation-bdd` on the **Run section** that Allure generates the report and the you use `allure:serve` to start a server. As the server deployes with the data generated on allure-results we can move the index.html (the main page of the report) nor sending via email for example.

So the porpouse we said it is to transform this image of the index.html with all the resources into a single-file-html style page. For this the easiest way is to use the .allure of our maven project that is generated when we add the allure dependecies.

So, when we add the allure dependecy in our project and execute and allure task on maven as `allure:report` (and the and only after allure:report) `allure:server`. We see that in our project it is generated a directory called ./allure
This ./allure directory must have /allure-x.xx.x (x version) and inside it, we found /bin, /config, /lib, /plugins. So in our case it seems something like 
./allure
--allure-2.26.0
----bin
----config
----lib
----plugins

**So, the bin directory have the allure.bat & allure (for linux and max system), this allure.bat is the tool we need for generating a single-file-html with the allure-results generate with allure**

# Build Configuration

> [!IMPORTANT]
> Once we see we have configurated correctly Cucumber with Junit4 & Allure with Cucumber-JVM on JUni4. We are ready.

Now the goal is to create a configuration in the pom.xml build whereby I can execute a Maven command that is capable of directly generating the report into a single-file-html. The result of this configuration is:
```
<plugin>
  <groupId>org.codehaus.mojo</groupId>
  <artifactId>exec-maven-plugin</artifactId>
  <version>3.2.0</version>
  <executions>
      <execution>
          <id>run-allure-report-generation</id>
          <phase>post-integration-test</phase>
          <goals>
              <goal>exec</goal>
          </goals>
          <configuration>
              <executable>${basedir}/.allure/allure-${allure.version}/bin/allure.bat</executable>
              <workingDirectory>${basedir}</workingDirectory>
              <arguments>
                  <argument>generate</argument>
                  <argument>-c</argument>
                  <argument>${project.build.directory}/allure-results</argument>
                  <argument>-o</argument>
                  <argument>${project.build.directory}/allure-report</argument>
                  <argument>--single-file</argument>
              </arguments>
          </configuration>
      </execution>
  </executions>
  </plugin>
  <plugin>
  <groupId>org.apache.maven.plugins</groupId>
  <artifactId>maven-compiler-plugin</artifactId>
  <version>3.13.0</version>
  <configuration>
      <encoding>${project.build.sourceEncoding}</encoding>
      <release>13</release>
  </configuration>
  </plugin>
```

The allure command we are trying to perform in this build configuration is `generate -c ${project.build.directory}/allure-results -o ${project.build.directory}/allure-report --single-file`
Where:
1. `generate`: Tells Allure to generate a report.
2. `-c`: Cleans the target directory before generating the report.
3. `${project.build.directory}/allure-results`: Specifies the directory where Allure will find the test result files.
4. `-o ${project.build.directory}/allure-report`: Specifies the output directory where Allure will generate the report.
4. `--single-file`: Indicates that a single-file HTML report should be generated instead of the default directory structure.


So with this build we are achieving:
1. With `exec-maven-plugin`: This plugin is configured to run during the post-integration-test phase. It executes a command specified in the <executable> element, which is the path to the Allure command-line interface (CLI) executable script **(allure.bat in this case)**. The plugin is instructed to generate an Allure report using the following arguments we describe previusly.

Overall, this configuration ensures that after the integration tests are run, the Allure report is generated using the specified CLI executable, and a single-file HTML report is produced in the designated output directory.


# Run

Now just left the execution with Maven. We already know about the main command to generate and serve the report `test -Dbrowser=edge -Dplatform=windows -Dcucumber.filter.tags=@all allure:report allure:serve`
But, now we are need to invoce `exec:exec@run-allure-report-generation` after allure generate the reports achieved with `allure:report`. So a nice finally command will be:
`test -Dbrowser=edge -Dplatform=windows -Dcucumber.filter.tags=@all allure:report exec:exec@run-allure-report-generation`

> [!TIP]
> We can edit the configuration run on Intellij and save this run configurations. You can do this Run > Edit Configurations