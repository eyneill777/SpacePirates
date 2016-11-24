package rustyice.game.actors

import com.badlogic.gdx.graphics.Color
import com.badlogic.gdx.graphics.g2d.Batch
import com.badlogic.gdx.graphics.g2d.Sprite
import rustyengine.RustyEngine
import rustyice.game.Actor
import rustyice.game.character.CharacterPhysics
import rustyice.graphics.Camera
import rustyice.graphics.RenderLayer
import rustyice.input.Actions
import rustyice.input.PlayerInput
import rustyice.physics.Collision

class Player() : Actor() {
    private val characterPhysics: CharacterPhysics

    @Transient var playerInput: PlayerInput? = null
    @Transient private var boxSprite: Sprite? = null

    var speed: Float
    @Transient private var count = 0

    override fun update(delta: Float) {
        super.update(delta)
        updateControls()

        var fx = 0f
        var fy = 0f

        val playerInput = playerInput
        if(playerInput != null) {
            if (playerInput.isPressed(Actions.MOVE_UP)) {
                fy += this.speed
            }
            if (playerInput.isPressed(Actions.MOVE_DOWN)) {
                fy -= this.speed
            }
            if (playerInput.isPressed(Actions.MOVE_LEFT)) {
                fx -= this.speed
            }
            if (playerInput.isPressed(Actions.MOVE_RIGHT)) {
                fx += this.speed
            }
        }

        characterPhysics.walk(fx, fy, 1f)

        if (count > 0) {
            boxSprite?.color = Color.BLUE
        } else {
            boxSprite?.color = Color.GREEN
        }
    }

    override fun beginCollision(collision: Collision) {
        super.beginCollision(collision)
        count++
    }

    override fun endCollision(collision: Collision) {
        super.endCollision(collision)
        count--
    }

    override fun render(batch: Batch, camera: Camera, layer: RenderLayer) {
        val boxSprite = boxSprite
        if(boxSprite != null){
            boxSprite.x = x - width / 2
            boxSprite.y = y - height / 2
            boxSprite.rotation = rotation

            boxSprite.draw(batch)
        }
    }

    private fun updateControls(){
        val game = game
        if(game != null){
            if(game.playerInputs.isNotEmpty()){
                playerInput = game.playerInputs[0]
            } else {
                playerInput = null
            }
            if(game.cameras.isNotEmpty()){
                game.cameras[0].target = this
                game.cameras[0].isTracking = true
            }
        } else {
            playerInput = null
        }
    }

    override fun init() {
        super.init()

        val boxSprite = Sprite(RustyEngine.resorces.circle)
        this.boxSprite = boxSprite

        boxSprite.color = Color.CYAN

        boxSprite.setSize(width, height)
        boxSprite.setOrigin(width / 2, height / 2)
    }

    init {
        characterPhysics = CharacterPhysics()
        physicsComponent = characterPhysics
        width = 0.98f
        height = 0.98f
        speed = 6f
    }
}
