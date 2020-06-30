# QUT Technical Test

This solution uses JRS-303 for bean validation, H2 as in-memory database,
and also authenticates the user through the http header "Authorization"
using "Basic" realm, it is a simple authorization
case as it only authenticates one user/password.

To specify user/password, the application must be run with the following system properties:

_username_: __au.edu.qut.user__

_password_: __au.edu.qut.password__ 

Which can be specified using _-Dproperty=value_ in the java command line, or inside
the __application.properties__ file.