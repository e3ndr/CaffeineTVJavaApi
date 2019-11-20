# CaffeineTVJavaApi
A (very) unofficial way of getting Caffeine related data including stream chat.

# Usage

```java
CaffeineProfile profile = new CaffeineProfile(username);
profile.isValid(); // returns false if a user doesn't exist.

EventListener listener = (new JsonListener() {
@Override
  public void onEvent(JSONObject json) {
    // Code here, relies on the json-simple library.
  }
})
CaffeineStream stream = new CaffeineStream(profile, listener); // Automatically connects, if not an error is thrown.
stream.close(); # Close.
stream.isOpen(); # Is open, directly from the websocket api.
```


# What it accesses

```
https://api.caffeine.tv/v1/users/$username # Where to get client information
wss://realtime.caffeine.tv/v2/reaper/stages/$stage-id/messages # Socket address for stream events
{"Headers":{"Authorization":"Anonymous API","X-Client-Type":"api"}} # Headers for websocket
https://images.caffeine.tv/$avatar-link # Where you can get an avatar, every client has an associated avatar_link
```