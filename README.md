[![Kotlin](https://img.shields.io/badge/kotlin-1.4.21-red.svg)](http://kotlinlang.org/)
[![LibGDX](https://img.shields.io/badge/libgdx-1.9.12-green.svg)](https://libgdx.badlogicgames.com/)
[![LibKTX](https://img.shields.io/badge/libktx-1.9.12--b1-blue.svg)](https://libktx.github.io/)

![image](https://user-images.githubusercontent.com/93260/102691758-20f80580-420f-11eb-81df-f0104c6ac31f.png)

# GdxAI Example

This is an example project on how to use GdxAI's state machine and message dispatching functionality
to create a player character that can have following states:
* Idle
* Run
* Jump
* Fall
* Attack (three different attack states)

In addition, there is a priest character who will heal the player if injured. The priest has these states:
* Idle
* Run
* Cast

The project uses LibGDX, LibKTX, Box2D and Ashley. The components and systems are just a quick&dirt
implementation, but you can of course use them as a reference.

The main focus however sits in `com.github.quillraven.gdxaiexample.ai` package and `com.github.quillraven.gdxaiexample.ecs.system/ai.kt` 
which are showing how you can use GdxAI for simple AI.

Player controls:
* A -> Attack
* J -> Jump
* LEFT/RIGHT -> Move

`I` will trigger a delayed injured event to let the priest know that the player needs healing.

The code is explained in more detail in following video: [Link to YouTube](https://www.youtube.com/watch?v=LqAmBdKfPXU&ab_channel=Quillraven)
