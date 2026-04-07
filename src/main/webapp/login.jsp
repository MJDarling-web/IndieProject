<%@ page contentType="text/html;charset=UTF-8" language="java" %>
<%@include file="taglib.jsp" %>
<!DOCTYPE html>
<html lang="en">
<head>
  <meta charset="UTF-8">
  <meta name="viewport" content="width=device-width, initial-scale=1.0">
  <title>Login | Commuter</title>
  <link rel="stylesheet" href="style/main.css" type="text/css" />
</head>
<body>

<c:set var="activePage" value="login" />
<%@include file="Header.jsp" %>

<main class="auth-page">
  <section class="auth-section">
    <div class="auth-card">
      <div class="auth-icon-circle">
        <svg xmlns="http://www.w3.org/2000/svg" width="32" height="32" fill="white" class="bi bi-map" viewBox="0 0 16 16">
          <path fill-rule="evenodd" d="M15.817.113A.5.5 0 0 1 16 .5v14a.5.5 0 0 1-.402.49l-5 1a.5.5 0 0 1-.196 0L5.5 15.01l-4.902.98A.5.5 0 0 1 0 15.5v-14a.5.5 0 0 1 .402-.49l5-1a.5.5 0 0 1 .196 0L10.5.99l4.902-.98a.5.5 0 0 1 .415.103M10 1.91l-4-.8v12.98l4 .8zm1 12.98 4-.8V1.11l-4 .8zm-6-.8V1.11l-4 .8v12.98z"/>
        </svg>
      </div>

      <h1 class="auth-title">Welcome Back</h1>
      <p class="auth-subtitle">Sign in to your Commuter account</p>

      <form id="authForm" class="auth-form">
        <div class="form-group">
          <label for="email">Email</label>
          <input
                  type="email"
                  id="email"
                  name="email"
                  placeholder="you@example.com"
                  required
          />
        </div>

        <div class="form-group">
          <label for="password">Password</label>
          <input
                  type="password"
                  id="password"
                  name="password"
                  placeholder="••••••••"
                  required
          />
        </div>

        <div class="auth-actions">
          <button type="button" id="signInButton" class="btn btn-primary auth-submit">
            Sign In
          </button>
        </div>

        <div class="auth-links">
          <button type="button" id="signUpButton" class="auth-text-button">
            <span>Don't have an account?</span>&nbsp;Create one
          </button>
        </div>
      </form>

      <p id="message" class="auth-message"></p>
    </div>
  </section>
</main>

<%@include file="Footer.jsp" %>

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
      message.classList.add('visible');
      return;
    }

    const accessToken = data.session?.access_token;

    if (!accessToken) {
      message.textContent = 'Sign in worked, but no session was returned.';
      message.classList.add('visible');
      return;
    }

    const response = await createJavaSession(accessToken, email);

    if (response.ok) {
      window.location.href = 'index.jsp';
    } else {
      message.textContent = 'Login worked, but app session setup failed.';
      message.classList.add('visible');
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
      message.classList.add('visible');
      return;
    }

    const accessToken = data.session?.access_token;

    if (accessToken) {
      const response = await createJavaSession(accessToken, email);

      if (response.ok) {
        window.location.href = 'index.jsp';
        return;
      } else {
        message.textContent = 'Account created, but app session setup failed.';
        message.classList.add('visible');
        return;
      }
    }

    message.textContent = 'Account created. Check your email to confirm your signup, then sign in.';
    message.classList.add('visible');
  }

  document.getElementById('signInButton').addEventListener('click', signIn);
  document.getElementById('signUpButton').addEventListener('click', signUp);
</script>
</body>
</html>