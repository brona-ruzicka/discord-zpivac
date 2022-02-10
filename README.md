# Zpívač
Very basic music bot for Discord. Written in java.

The project includes a kind-of library `discord4j.extension.commands` which functions as a unified interface for both standart message and slash commands.

Repo includes maven pom file with all dependencies. The program expects a `bot.roperties` file in  the `resources` folder. This file should include `token`, the discord token, and `prefix`, the prefix the bot will listen for in messages.

To update application command structure on discord use the `updateCommands` function in the main `Zpivac` class.
