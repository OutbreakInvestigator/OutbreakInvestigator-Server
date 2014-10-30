Setting Up Dev Environment (Windows OS)
GitHub
1. Install GitHub App (windows and Mac)
2. Clone from
https://github.com/UW-EpiData/OutbreakInvestigator-Server.git


Server
1. Install Maven (more complicated on Windows than other platforms).  If you are using Netbeans 6.7 or newer IDE, maven is included.  You can skip this step.
	a. Download Maven from here: http://maven.apache.org/download.cgi
	b. Gotchas: pay careful attention to environment variables and make sure JAVA_HOME is a jdk, not just a jre.
2. Install Tomcat from http://tomcat.apache.org/
3. Open Maven project from the local Git Repo.  Build the project and run.
4. Test the server
a. Open a browser to:  http://localhost:8080/OBIServices/rest/gds (all graph data in JSON should be returned).

