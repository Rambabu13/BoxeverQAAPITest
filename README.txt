QA API Challenge for Boxever:

This project has been developed using Java (11.0.6 version) and Maven as build and run framework.

The dependencies installed are the following:
* io.rest-assured (3.0.3)
* org.testng (7.3.0)
* org.json.simple (1.1.1)

The run configuration must have the parameter '-Dtestng.dtd.http=true' included in the run line in JDK Settings->VM Options

These tests run with a personal user token of my own GITHUB account.
If you want to run them with a different account please generate it in your GITHUB account and change the 'token' String in 'src/test/java/objects/FinalData'.

As you can see I've separated the tests from the utility classes, to clarify the code. Also, I've created different tests for each flow, and a full flow test that follows the specific instructions of what the API user can do:
    * create gist
    * retrieve gist and returned gists have all the fields documentation specify
    * delete gist