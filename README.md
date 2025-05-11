🎵 Spotify Spring Boot Integration
This is a backend-only Spring Boot project where I integrated with the Spotify API to show some personal music data like my top 10 tracks, currently playing song, artists I follow, and options to play or pause tracks — all without any frontend. You can hit the endpoints and see the response in JSON.

🔐 How Authentication Works
Spotify uses OAuth 2.0, so I had to do a one-time login to get an authorization code. This is done manually by hitting a specific URL with my client ID, scopes, and redirect URI. Once I log in, Spotify sends an auth code to my /callback endpoint.

After that:

The app exchanges the code for an access token (valid for 1 hour) and a refresh token (long-lived).

These tokens are saved to a token.json file locally so that the backend can keep using them.

I also created a refresh mechanism to keep things running without needing to log in again.

🔁 Refreshing Tokens Automatically
Every time the backend makes a request to the Spotify API, it checks if the access token is older than 30 minutes. If it is, it automatically uses the refresh token to get a new access token and updates token.json.

This way:

I don’t have to keep logging in.

Everything keeps working smoothly.

Only one user (me) is authenticated, and that’s all the backend needs.

🗂 Project Structure
Just to keep things clean, I split things up:

controllers:
SpotifyAuthController.java – for handling login, callback, and refresh
SpotifyDataController.java – for actual Spotify endpoints (top tracks, play/pause, etc.)

services:
SpotifyAuthService.java – deals with token exchange, storage, and refresh
SpotifyDataService.java – talks to Spotify APIs

utils:
TokenStorageService.java – reads/writes to token.json

🧪 Endpoints You Can Try
GET /spotify/top-tracks → Returns top 10 songs

GET /spotify/now-playing → What's playing right now

GET /spotify/followed-artists → Artists I follow

PUT /spotify/pause → Pause playback

PUT /spotify/play/{trackId} → Play a track

GET /spotify/auth/refresh → Manually refresh token (optional)

Let me know if you want the actual cURL commands or Postman collection to test these out!
