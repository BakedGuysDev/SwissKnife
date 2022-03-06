<h1 align="center">SwissKnife</h1>

<p align="center">The Swiss Army knife for anarchy server owners.</p>

<div align="center">
    <a href="https://discord.gg/2Z4YT2qKQ2"><img src="https://img.shields.io/discord/825773898796630016?style=for-the-badge&logo=discord" alt="Discord"/></a>
    <br><br>
    <img src="https://img.shields.io/github/last-commit/EgirlsNationDev/SwissKnife?style=for-the-badge" alt="GitHub last commit"/>
    <img src="https://img.shields.io/github/commit-activity/w/EgirlsNationDev/SwissKnife?style=for-the-badge" alt="GitHub commit activity"/>
    <br>
    <img src="https://img.shields.io/tokei/lines/github/EgirlsNationDev/SwissKnife?style=for-the-badge" alt="GitHub lines of code"/>
    <img src="https://www.codefactor.io/repository/github/EgirlsNationDev/SwissKnife/badge?style=for-the-badge" alt="CodeFactor"/>
    <img src="https://img.shields.io/github/workflow/status/EgirlsNationDev/SwissKnife/Java%20CI%20with%20Maven?event=push&style=for-the-badge" alt="Github Actions CI"/>
</div>

##What is SwissKnife

Unique Paper based plugin dedicated to patching illegal items, exploits, bugs  
and basically plugin for everything that anarchy servers might need and more.

### What makes it unique?

The idea of using codebase of a utility client for a plugin that stands on the other side of things.

The plugin uses codebase from the [Meteor Client](https://github.com/MeteorDevelopment/meteor-client).  
It was stripped down and modified to work with the Bukkit event system and Yaml based config system.

### Main Features
- Multiple modules for removing illegal items
- Lag prevention modules
- Discord Webhook based lag alert
- MySQL database for statistics
- Custom items with abilities
- Exploding snowballs!
- And more... And more yet to come!

### License
All builds since version 0.1.0 to version 1.0.0 are licensed under the MIT license.  
Builds since version 1.0.0 are licensed under the GPL 3.0 license.

### Downloads

Stable releases can be found in the [releases](https://github.com/EgirlsNationDev/SwissKnife/releases) tab.  
If you want a sneak peek of what's currently being done you can download the development builds in the [actions](https://github.com/EgirlsNationDev/SwissKnife/actions/workflows/maven.yml) tab.

### EgirlsNation links
- [Website](https://egirlsnation.com/)
- [Discord](https://egirlsnation.com/discord)
- IP - play.egirlsnation.com

### Building

Maven is the recommended way to build the project.

- Clone the project with `git clone https://github.com/EgirlsNationDev/SwissKnife.git`.  
- Use `mvn clean package` in the main project directory to build the project.  
- The jar will be located at `/target/SwissKnife-version-Full.jar` (The full jar has some dependencies, for example keeper, included).

### Disclaimer

Although I think the plugin releases are stable and safe to use I want to say this to prevent being blamed for things.

If someone decides to use the plugin I or any of the contributors don't take responsibility for broken servers, dead SDD drives, thermonuclear war, or you loosing your sleep because it didn't work, and you had to fix it.

Please do some research, if you have any concerns about features included in this plugin before using it!  
YOU are choosing to make these modifications to your server, and if you point the finger at us for messing up your server, we will laugh at you.

Thank you. 
