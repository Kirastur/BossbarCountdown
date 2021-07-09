# BossbarCountdown

Countdown as Bossbar and perform action when finished (minecraft plugin)

# Introduction
Sometimes in a quest you want to give the player only a limited amount of time to fulfill the quest requirements, e.g. rescuring the princess or finding the exit from a maze. Or you want to build a minigame  which runs for e.g. three minutes. Then you can use this plugin to display a countdown (or countup) as Bossbar. The bar gets automatically decreased (or inceased) and if the final value is reached, a user defined command is executed.

# Features
* User-defined scale, e.g. from "0" to"100", or from "3.2" to "-8.5"
* User-defined speed
* Custom templates for different Bossbar layouts
* Modify Bossbar at runtime (pause/unpause, show/hide, change progress and speed)
* User-defined start-value and end-value (goal)
* Execute command when the goad is reached (e.g. teleport the player back, or start a LibSequence sequence)
* Sends a minecraft custom event when the goal is reached
* Bossbars are cancelable
* Can be controlled by API or by commands
* Detailed documentation as Github Wiki

# Usage example
This plugin is delivered with a default template called "demo"
1. Place the plugin into the server's plugin directory and start the server
2. Login with a player 
3. Execute "bbcd start demo <Playername>" from the server console
  
As a result you will see the Bossbar counting down, and when finished a message is sent to the player.
