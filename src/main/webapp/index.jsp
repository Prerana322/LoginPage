<!DOCTYPE html>
<html>
<body>
    <form action="LoginServlet" method="post">
         <label for="emailid">Emailid:</label>
         <input type="text" id="emailid" name="emailid" required><br><br>
         <label for="password">Password:</label>
         <input type="password" id="password" name="password" required><br><br>
         <input type="submit" value="Login">
    </form>
    <p>Don't have an account? <a href="Signup.jsp">Signup</a></p>
</body>
</html>
