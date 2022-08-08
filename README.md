
# Android Application using PKCE & Cloudentity Platform

This sample Android application obtains an access token from [Cloudentity platform](https://cloudentity.com/) using Authorization Code grant and PKCE.

A more detailed explanation is available at [App Dev Tutorials](https://cloudentity.com/developers/app-dev-tutorials/android/android-pkce-tutorial/)

## Prerequisites
 - [Cloudentity account](https://authz.cloudentity.io/register)
 - [Workspace and Client application prepared for PKCE](https://docs.authorization.cloudentity.com/features/oauth/grant_flows/auth_code_with_pkce/?q=pkce)
 - [Android SDK 26+](https://www.android.com/)

## Running the sample application
 - Clone the repository
 - Add the following to Config.kt
   - AUTHORIZE_ENDPOINT - authorize endpoint for your Cloudentity client application
   - TOKEN_ENDPOINT - token endpoint for your Cloudentity client application
   - CLIENT_ID - client ID for your Cloudentity client application
   - REDIRECT_URI - "oauth://simple-pkce.example.com" // if edited, update your Cloudentity client application redirect_uri
 - Run the application in AndroidStudio(or your preferred IDE)


Relevant Links
 - [OAUTH](https://datatracker.ietf.org/doc/html/rfc6749)
 - [OAUTH for Native Apps](https://datatracker.ietf.org/doc/html/rfc8252)
 - [PKCE](https://datatracker.ietf.org/doc/html/rfc7636)
 - [App Dev Tutorials](https://developer.cloudentity.com/app_dev_tutorials/)

