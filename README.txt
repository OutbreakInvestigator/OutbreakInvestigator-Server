////////////////////////////////

Copyright 2014 University of Washington (Neil Abernethy, Wilson Lau, Todd Detwiler)
http://faculty.washington.edu/neila/

////////////////////////////////

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
a. Open a browser to:  http://localhost:8080/OBIServices/rest/ads/vquery?query=SELECT%20Case.*%2CAddress.Lat%2CAddress.StreetAddress%2CAddress.Lng%20FROM%20Case%2CAddress%20where%20Case.DbID%3DAddress.Case_DbID%20and%20REPORT_DT%20%3E%3D%20%232009-09-01%23%20AND%20REPORT_DT%20%3C%3D%20%232009-12-31%23
 (all graph data in JSON should be returned).

