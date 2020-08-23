<%@page contentType="text/html"%>
<%@page pageEncoding="UTF-8"%>
<!DOCTYPE html>

<html>
  <head>
    <title>Login</title>
    <base href="${pageContext.request.contextPath}/" />
  </head>
  <body>
    <h2>Login</h2>

    <form action="" method="POST" role="form">
      <p>
        <label for="username">Username</label>
        <input type="text" id="username" name="username">
      </p>
      <p>
        <label for="password">Password</label>
        <input type="password" id="password" name="password">
      </p>
      <p>
        <button type="submit">Login</button>
      </p>
    </form>
  </body>
</html>
