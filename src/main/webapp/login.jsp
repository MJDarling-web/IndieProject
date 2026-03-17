<%@ page contentType="text/html;charset=UTF-8" language="java" %>
<!DOCTYPE html>
<html>
<head>
  <title>Login</title>
</head>
<body>
<h2>Login</h2>

<form id="authForm">
  <label for="email">Email</label><br>
  <input type="email" id="email" name="email" required /><br><br>

  <label for="password">Password</label><br>
  <input type="password" id="password" name="password" required /><br><br>

  <button type="button" id="signInButton">Sign In</button>
  <button type="button" id="signUpButton">Create Account</button>
</form>

<p id="message"></p>

<script type="module">
  import { createClient } from 'https://cdn.jsdelivr.net/npm/@supabase/supabase-js/+esm';

  const supabase = createClient(
          'https://xsrqtserbcumqvhuhadv.supabase.co',
          'eyJhbGciOiJIUzI1NiIsInR5cCI6IkpXVCJ9.eyJpc3MiOiJzdXBhYmFzZSIsInJlZiI6InhzcnF0c2VyYmN1bXF2aHVoYWR2Iiwicm9sZSI6ImFub24iLCJpYXQiOjE3NzM2NzUwMzEsImV4cCI6MjA4OTI1MTAzMX0.ktuPbkg87CVI3VkMAgTwYv3xk0sbCvA_R7zbhp5Uq1c'
  );

  const message = document.getElementById('message');

  async function createJavaSession(accessToken, email) {
    const response = await fetch('auth/session', {
      method: 'POST',
      headers: {
        'Content-Type': 'application/json'
      },
      body: JSON.stringify({ accessToken, email })
    });

    return response;
  }

  async function signIn() {
    const email = document.getElementById('email').value.trim();
    const password = document.getElementById('password').value;

    const { data, error } = await supabase.auth.signInWithPassword({
      email,
      password
    });

    if (error) {
      message.textContent = error.message;
      return;
    }

    const accessToken = data.session?.access_token;

    if (!accessToken) {
      message.textContent = 'Sign in worked, but no session was returned.';
      return;
    }

    const response = await createJavaSession(accessToken, email);

    if (response.ok) {
      window.location.href = 'index.jsp';
    } else {
      message.textContent = 'Login worked, but app session setup failed.';
    }
  }

  async function signUp() {
    const email = document.getElementById('email').value.trim();
    const password = document.getElementById('password').value;

    const { data, error } = await supabase.auth.signUp({
      email,
      password
    });

    if (error) {
      message.textContent = error.message;
      return;
    }

    // If Confirm Email is OFF, a session may be returned immediately
    const accessToken = data.session?.access_token;

    if (accessToken) {
      const response = await createJavaSession(accessToken, email);

      if (response.ok) {
        window.location.href = 'index.jsp';
        return;
      } else {
        message.textContent = 'Account created, but app session setup failed.';
        return;
      }
    }

    // If Confirm Email is ON, no immediate session is expected
    message.textContent = 'Account created. Check your email to confirm your signup, then sign in.';
  }

  document.getElementById('signInButton').addEventListener('click', signIn);
  document.getElementById('signUpButton').addEventListener('click', signUp);
</script>
</body>
</html>