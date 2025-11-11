# java-exam-project

This is a simple 2d treasure hunting game made as an exam project for an OOP course.

The goal of the game is to collect all the keys scattered in the world map to open the chest containing the treasure.

**Disclaimer:** The game is by no means complete or fully entertaining, it is to be intended solely as a personal excercise.

## Features

- Made entirely using Java standard library (no external libraries)
- Graphics handled with Swing

## How to Play

You can try the game by downloading the latest JAR release:
[java-treasure-1.0.jar](https://github.com/Macedonioz/java-exam-project/releases/tag/v1.0)

Then run it with:
```bash
java -jar java-treasure-1.0.0.jar
```

Or by cloning the repository (Maven is required):
```bash
git clone https://github.com/Macedonioz/java-exam-project
cd java-exam-project
mvn clean compile exec:java -Dexec.mainClass="game_logic.Main"
```

## Credits

- **Tutorial Followed:**
  - ["How to Make a 2D Game in Java"](https://www.youtube.com/watch?v=om59cwR7psI&list=PL_QPQmz5C6WUF-pOQDsbsKbaBZqXj4qSq) by [@RyiSnow](https://github.com/RyiSnow)
- **Fonts:**
  - [BitPotionExt](https://joebrogers.itch.io/bitpotion) by [@joeBRogers](https://joebrogers.itch.io/)
  - [Quaver](https://caffinate.itch.io/quaver) by [@caffi_nate](https://caffinate.itch.io/)
- **Sprites:**
  - [cute-fantasy-rpg](https://kenmi-art.itch.io/cute-fantasy-rpg) by [@KenmiPixelArt](https://kenmi-art.itch.io/)
  - [Key Items 16x16](https://dantepixels.itch.io/key-items-16x16) by [@DantePixels](https://dantepixels.itch.io/)
- **Sound:** 
  - [Final Quest - 16bit Retro RPG Music](https://ryanavx.itch.io/final-quest-music-pack) by [@RyanAvx](https://ryanavx.itch.io/)

