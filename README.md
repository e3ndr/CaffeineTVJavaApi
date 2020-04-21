# CaffeineTVJavaApi
A (very) unofficial way of getting Caffeine related data including stream chat.

**UPDATE**
This project is long dead, swing is buggy and even then it doesn't work well with OBS.
As an alternative you can use either [Casterlabs](https://casterlabs.co) (Runs more like classic streamlabs or maelstream) or [Caffeinated](https://github.com/thehelvijs/Caffeineated) (Runs locally).
Thanks for your interest and I hope this repo atleast gives you some development knowledge on Caffeine.
  
  
# Usage

You can find the project on JitPack.

```java
CaffeineProfile profile = new CaffeineProfile(username);
profile.isValid(); // returns false if a user doesn't exist.

EventListener listener = (new ChatListener() {
  @Override
  public void onEvent(Chat chat) {
    // Code here
  }
})
CaffeineStream stream = new CaffeineStream(profile, listener); // Automatically connects, if not an error is thrown.
stream.close(); // Close.
stream.isOpen(); // Is open, directly from the websocket api.
```


# What it accesses

```
https://api.caffeine.tv/v1/users/$username # Where to get client information
wss://realtime.caffeine.tv/v2/reaper/stages/$stage-id/messages # Socket address for stream events
{"Headers":{"Authorization":"Anonymous API","X-Client-Type":"api"}} # Headers for websocket
https://images.caffeine.tv/$avatar-link # Where you can get an avatar, every client has an associated avatar_link
```
