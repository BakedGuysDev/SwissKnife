# SwissKnife - Scuffed but working

A paper based plugin dedicated to patching illegal items, exploits and that players have encountered on the Egirls Nation server, plus adding all kinds of new features to the game... It's a bit scuffed, but it works.  

The plugin was made just for one server which is Egirls Nation. You can use it on other servers, however some features may not work correctly or at all.

> As of 12. Aug. 2021 adding config options to almost everything is in progress, so if you want to use the plugin, but with some features disabled, there is a big chance you'll get a config option soon. Of course you can always make an issue to nudge us in the right direction.  
  
Pull requests are welcome!  

## Disclaimer
  
If someone will want to use it on their server I or any of the contributors don't take responsibility for broken servers, dead SDD drives, thermonuclear war, or you loosing your sleep because it didn't work and you had to fix it.  

Please do some research, if you have any concerns about features included in this plugin before using it!  
YOU are choosing to make these modifications to your server, and if you point the finger at us for messing up your server, we will laugh at you.  

Thank you.  

## Main Feaures
- Multiple checks for removing illegal items
- Lag preventions
- Discord Webhook based lag alert
- MySQL database for statistics
- Custom items with abilities
- Exploding snowballs!
- And more... And more yet to come!

## Downloads

There are no public downloads of stable versions.    
There may be some in the future however.  
If that will be the case you'll find them in the releases tab.  

If you still want to download the plugin you can do so in the actions tab.  

## EgirlsNation links
- [Website](https://egirlsnation.com/)
- [Discord](https://egirlsnation.com/discord)
- IP - play.egirlsnation.com

## Building

Maven is the recommended way to build the project.

- Clone the project with `git clone https://github.com/EgirlsNationDev/SwissKnife.git`.  
- Use `mvn clean package` in the main project directory to build the project.  
- The jar will be located at `/target/SwissKnife-version-Full.jar` (The full jar has some dependencies, for example keeper, included).
