////////////////////////////////

Copyright 2014 University of Washington

This program is free software: you can redistribute it and/or modify
it under the terms of the GNU General Public License as published by
the Free Software Foundation, either version 3 of the License, or
(at your option) any later version.

This program is distributed in the hope that it will be useful,
but WITHOUT ANY WARRANTY; without even the implied warranty of
MERCHANTABILITY or FITNESS FOR A PARTICULAR PURPOSE.  See the
GNU General Public License for more details.

You should have received a copy of the GNU General Public License
along with this program.  If not, see <http://www.gnu.org/licenses/>.

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
a. Open a browser to:  http://localhost:8080/OBIServices/rest/gds (all graph data in JSON should be returned).

