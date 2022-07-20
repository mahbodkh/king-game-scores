The code implements a HTTP-based mini game back-end in Java which registers game scores for different users and levels, with the capability to return high score lists per level. 
There is also a simple login system, without any authentication.

Non-functional requirements
----------------------------

1 	The server handles a lot of simultaneous requests and makes use of the available memory and CPU, while not compromising the readability and integrity of the data.
2 	It does not use any external frameworks, except for testing.
3	There is no need persistence to disk.

Functional requirements
------------------------

The functions are described in detail below and the notation <value> means a call parameter value or a return value. 
All calls result in the HTTP status code 200, unless when something goes wrong, where other http codes are returned. 
Numbers parameters and return values are sent in decimal ASCII representation as expected.
Users and levels are created “ad-hoc”, the first time they are referenced.

Login
----------

This function returns a session key in the form of a string (without spaces) which is valid for use with the other functions for 10 minutes. 
The session keys are “reasonably unique”.

Request: GET /<userid>/login
Response: <sessionkey>
<userid> : 31 bit unsigned integer number
<sessionkey> : A string representing session (valid for 10 minutes)

Example: http://localhost:8081/4711/login --> UICSNDK


Post a user's score to a level
--------------------------------

This method is called several times per user and level and does not return anything. Only requests with valid session keys are processed.

Request: POST /<levelid>/score?sessionkey=<sessionkey>
Request body: <score>
Response: (nothing)
<levelid> : 31 bit unsigned integer number
<sessionkey> : A session key string retrieved from the login function.
<score> : 31 bit unsigned integer number

Example: POST http://localhost:8081/2/score?sessionkey=UICSNDK (with the post body: 1500)


Get a high score list for a level
----------------------------------

Retrieves the high scores for a specific level. The result is a comma separated list in descending score order. 
No more than 15 scores are to be returned for each level. 
Only the highest score counts. ie: a user id can only appear at most once in the list. 
If a user hasn't submitted a score for the level, no score is present for that user. 
A request for a high score list of a level without any scores submitted is an empty string.

Request: GET /<levelid>/highscorelist
Response: CSV of <userid>=<score>
<levelid> : 31 bit unsigned integer number
<score> : 31 bit unsigned integer number
<userid> : 31 bit unsigned integer number
Example: http://localhost:8081/2/highscorelist - > 4711=1500,131=1220
