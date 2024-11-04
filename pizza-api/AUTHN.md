To login with google:

1. Setup the google project and get:
   * client id (QUARKUS_OIDC_CLIENT_ID)
   * client secret (QUARKUS_OIDC_CREDENTIALS_SECRET)
   * encoded redirect uri (http%3A%2F%2Flocalhost%3A8080%2F_oidc%2Fcallback)
1. Get the authorization code
   ```
   echo "https://accounts.google.com/o/oauth2/auth?response_type=code&client_id=$QUARKUS_OIDC_CLIENT_ID&redirect_uri=$OIDC_REDIRECT_URI&scope=openid%20email%20profile"
   export OIDC_AUTH_CODE='4%2F0AVG7fiQNtRFqw7jjXVyq8jPiBrPj-XrQmuef5si8LPYgIMlZ5k-xIc1PpYkoFXszm6pojQ&scope=email+profile+openid+https%3A%2F%2Fwww.googleapis.com%2Fauth%2Fuserinfo.profile+https%3A%2F%2Fwww.googleapis.com%2Fauth%2Fuserinfo.email'
   ```
   1. exchange the auth code for the access token
      ```
      export OIDC_ACCESS_TOKEN=$(curl -s -X POST https://oauth2.googleapis.com/token \
      -H "Content-Type: application/x-www-form-urlencoded" \
      -d "code=$OIDC_AUTH_CODE" \
      -d "client_id=$QUARKUS_OIDC_CLIENT_ID" \
      -d "client_secret=$QUARKUS_OIDC_CREDENTIALS_SECRET" \
      -d "redirect_uri=$OIDC_REDIRECT_URI" \
      -d "grant_type=authorization_code" \
         | jq -r '.access_token')
      ```
1. Use the access token
   ```
   curl -X GET http://localhost:8080/api/whoami \
   -H "Accept: application/json"\
   -H "Authorization: Bearer $OIDC_ACCESS_TOKEN" 
   ```
