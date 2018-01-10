People tool was initially designed to be some kind of corporate directory (and to learn Scala/Play).
It has since morphed into areas to help me (and others) do resource planning and answer questions around organisational metrics and run a medium-large size department.

# features
## People
* Imports employee from Master file (currently an extract from SAP)
* Basic search/display of Org
* Add photos of people
* Recognize people (shoutouts)
* Corporate monthly awards
* OKR's
* Matrix teams, Tagging, Workgroups

## Project
* Ability to see what work is going on within the department
* Basic stats on why a project is being done, and for who
* Basic capacity planning (still a work in progress)



# feature requests
## general
Things are general usability of the site
### user preferences
 * OKR style
 * [x] Allow user to remove pictures of directs/add more info in table there

## People
 * ramp up monitoring of new hires

## Product feature
Around the work coming in,  getting done, or past metrics
### resource planning
 * future projects
 * current projects
 * integrate into solver
 * editing
 * integration into Jira/agile sprint boards

## CRM
* Stuff which helps manage our clients.
* Stuff to help with Incidents/bugs etc.

# To Run
for the most part it should be 
* create the DB from the schema, and evolutions
* find all the 'dist' or 'template' files and modify them to your needs (put your passwords in, and customize it with your logos, corporate values etc)
* build the docker file
* figure out how to get a load from your HR system into this. (TBD: provide a sample input file)


# Gotchas
* Auth is done via LDAP and storing stuff in the session cookie. It also uses the shoutout auth email to log you in. This may be a 'security' issue in your organisation. I judged the risk as low, but I put this note in the readme in case you might not.
* we also reset the session/user details in the preferences area.


# LEGAL NOTICE

This code is licensed under [![GNU GPL v3.0](http://www.gnu.org/graphics/gplv3-127x51.png)](http://www.gnu.org/licenses/gpl.html)
The exception to this is [Digital River](https://www.digitalriver.com/), who has licensed this under a [MIT](https://opensource.org/licenses/MIT) license.
