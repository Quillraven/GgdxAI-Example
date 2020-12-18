[![Kotlin](https://img.shields.io/badge/kotlin-1.4.21-red.svg)](http://kotlinlang.org/)
[![LibGDX](https://img.shields.io/badge/libgdx-1.9.12-green.svg)](https://libgdx.badlogicgames.com/)
[![LibKTX](https://img.shields.io/badge/libktx-1.9.12--b1-blue.svg)](https://libktx.github.io/)

![image](https://user-images.githubusercontent.com/93260/102630843-550af200-414d-11eb-8230-daf9ff071c08.png)

# GdxAI Example

This is an example project on how to use GdxAI's state machine and message dispatching functionality
to create a character that can have following states:
* Idle
* Run
* Jump
* Fall
* Attack (three different attack states)

The project uses LibGDX, LibKTX, Box2D and Ashley. The components and systems are just a quick&dirt
implementation but you can of course use them as a reference.

The main focus however sits in `state.kt` and `ai.kt` which are showing how you can use GdxAI for simple AI.

The code is explained in more detail in following video: [Link TBD]()
