# SwissKnife

Unique Paper based plugin dedicated to patching illegal items, exploits and bugs on anarchy servers, plus adding all kinds of new features to the game.  

The plugin started as a way for me to patch things on the Egirls Nation server, but as of the new codebase rewrite in version 1.0.0 it's usable on any server thanks to the extensive configuration options.
 
Pull requests are of course welcome!  

## What makes it unique?

The idea of using codebase of a utility client for a plugin that's meant to patch exploits.

The plugin uses codebase from the [Meteor Client](https://github.com/MeteorDevelopment/meteor-client).  
It was stripped and modified to work with the Bukkit event system and Yaml based config system.

## Main Features
- Multiple modules for removing illegal items
- Lag prevention modules
- Discord Webhook based lag alert
- MySQL database for statistics
- Custom items with abilities
- Exploding snowballs!
- And more... And more yet to come!

## License
All builds since version 0.1.0 to version 1.0.0 are licensed under the MIT license.  
Builds since version 1.0.0 are licensed under the GPL 3.0 license.

## Downloads

Stable releases can be found in the [releases](https://github.com/EgirlsNationDev/SwissKnife/releases) tab.  
If you want a sneak peek of what's currently being done you can download the development builds in the [actions](https://github.com/EgirlsNationDev/SwissKnife/actions/workflows/maven.yml) tab.

## EgirlsNation links
- [Website](https://egirlsnation.com/)
- [Discord](https://egirlsnation.com/discord)
- IP - play.egirlsnation.com

## Building

Maven is the recommended way to build the project.

- Clone the project with `git clone https://github.com/EgirlsNationDev/SwissKnife.git`.  
- Use `mvn clean package` in the main project directory to build the project.  
- The jar will be located at `/target/SwissKnife-version-Full.jar` (The full jar has some dependencies, for example keeper, included).

## Disclaimer

Although I think the plugin releases are stable and safe to use I want to say this to prevent being blamed for things.

If someone decides to use the plugin I or any of the contributors don't take responsibility for broken servers, dead SDD drives, thermonuclear war, or you loosing your sleep because it didn't work, and you had to fix it.

Please do some research, if you have any concerns about features included in this plugin before using it!  
YOU are choosing to make these modifications to your server, and if you point the finger at us for messing up your server, we will laugh at you.

Thank you. 
