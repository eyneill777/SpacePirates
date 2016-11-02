package rustyice.screens.menus

import com.badlogic.gdx.Gdx
import com.badlogic.gdx.graphics.g2d.Batch
import com.badlogic.gdx.scenes.scene2d.Actor
import com.badlogic.gdx.scenes.scene2d.ui.ButtonGroup
import com.badlogic.gdx.scenes.scene2d.ui.TextButton
import com.badlogic.gdx.scenes.scene2d.utils.ChangeListener
import com.kotcrab.vis.ui.widget.*
import rustyice.core.GeneralSettings
import rustyice.screens.Screen
import rustyice.screens.effects.ClickSound
import rustyice.screens.effects.GuiEffects
import rustyice.screens.util.ButtonPressed

class SettingsScreen: Screen {
    private val BUTT_WIDTH = 150f
    private val PAD = 5f
    private val FADE_DURATION = 0.5f

    private val catButtions: ButtonGroup<TextButton>
    private val root: VisTable
    private val categoryPane: VisTable
    private val settingPane: VisTable
    private val bottemPane: VisTable

    private val fullscreenCheck: VisCheckBox
    private val vsyncCheck: VisCheckBox

    private val soundSlider: VisSlider
    private val soundLabel: VisLabel
    private val soundLevelLabel: VisLabel
    private val musicSlider: VisSlider
    private val musicLabel: VisLabel
    private val musicLevelLabel: VisLabel

    private val videoButt: TextButton
    private val audioButt: TextButton
    private val inputButt: TextButton

    constructor(): super(){
        root = VisTable()
        root.setFillParent(true)

        categoryPane = VisTable()
        settingPane = VisTable()
        bottemPane = VisTable()

        videoButt = VisTextButton("Video", "toggle")
        videoButt.addListener(ButtonPressed{
            switchToVideo()
        })
        ClickSound.addDefault(videoButt)

        audioButt = VisTextButton("Audio", "toggle")
        audioButt.addListener(ButtonPressed{
                switchAudio()
        })
        ClickSound.addDefault(audioButt)
        
        inputButt = VisTextButton("Input", "toggle")
        inputButt.addListener(ButtonPressed {
            switchInput()
        })
        ClickSound.addDefault(inputButt)

        val backButt = VisTextButton("Back")
        backButt.addListener(ButtonPressed {
            screenManager.popScreen()
        })
        ClickSound.addDefault(backButt)

        val applyButt = VisTextButton("Apply")
        applyButt.addListener(ButtonPressed {
                applySettings()
        })
        ClickSound.addDefault(applyButt)

        catButtions = ButtonGroup(videoButt, audioButt, inputButt)
        catButtions.setMinCheckCount(1)
        catButtions.setMaxCheckCount(1)

        // <video>
        fullscreenCheck = VisCheckBox("Fullscreen")
        fullscreenCheck.isChecked = GeneralSettings.fullscreen
        ClickSound.addDefault(fullscreenCheck)

        vsyncCheck = VisCheckBox("VSync")
        vsyncCheck.isChecked = GeneralSettings.vsync
        ClickSound.addDefault(vsyncCheck)
        // </video>
        // <audio>
        soundSlider = VisSlider(0f, 1f, 0.1f, false)
        soundLabel = VisLabel("Sound: ")
        soundLevelLabel = VisLabel("")

        soundSlider.addListener(object: ChangeListener() {
            override fun changed(event: ChangeEvent, actor: Actor) {
                soundLevelLabel.setText("${(soundSlider.value * 100).toInt()}%")
                GeneralSettings.soundVolume = soundSlider.value
            }
        });

        musicSlider = VisSlider(0f, 1f, 0.1f, false)
        musicLabel = VisLabel("Music: ")
        musicLevelLabel = VisLabel("")

        musicSlider.addListener(object: ChangeListener() {
            override fun changed(event: ChangeEvent, actor: Actor) {
                musicLevelLabel.setText("${(musicSlider.value * 100).toInt()}%")
                GeneralSettings.musicVolume = musicSlider.value
            }
        })
        // </audio>

        categoryPane.add(videoButt).prefWidth(BUTT_WIDTH).pad(PAD).row()
        categoryPane.add(audioButt).prefWidth(BUTT_WIDTH).pad(PAD).row()
        categoryPane.add(inputButt).prefWidth(BUTT_WIDTH).pad(PAD)

        bottemPane.add(backButt).expandX()
        bottemPane.add(applyButt).expandX()

        root.add(categoryPane).fillY().right()
        root.add(settingPane).fill().prefSize(800f, 640f).row()
        root.add(bottemPane).colspan(2).fillX()
        // root.setDebug(true, true);
    }

    private fun switchToVideo() {
        settingPane.clear()
        settingPane.add(fullscreenCheck).space(5f).row()
        settingPane.add(vsyncCheck).space(5f)
    }

    private fun switchAudio() {
        settingPane.clear()
        settingPane.add(soundLabel).pad(20f)
        settingPane.add(soundSlider).pad(20f).prefWidth(100f)
        settingPane.add(soundLevelLabel).pad(20f).row()

        settingPane.add(musicLabel).pad(20f)
        settingPane.add(musicSlider).pad(20f).prefWidth(100f)
        settingPane.add(musicLevelLabel).pad(20f).row()

        soundSlider.value = GeneralSettings.soundVolume
        soundSlider.fire(ChangeListener.ChangeEvent())
        musicSlider.value = GeneralSettings.musicVolume
        musicSlider.fire(ChangeListener.ChangeEvent())
    }

    private fun switchInput() {
        settingPane.clear()
    }

    private fun applySettings() {
        val cat = catButtions.checked
        if (cat == videoButt) {
            if (fullscreenCheck.isChecked != GeneralSettings.fullscreen) {
                GeneralSettings.fullscreen = fullscreenCheck.isChecked
                if (GeneralSettings.fullscreen) {
                    Gdx.graphics.setFullscreenMode(Gdx.graphics.displayMode)
                } else {
                    Gdx.graphics.setWindowedMode(GeneralSettings.width, GeneralSettings.height)
                }
            }
            GeneralSettings.vsync = vsyncCheck.isChecked
            Gdx.graphics.setVSync(vsyncCheck.isChecked)
        } else if (cat == audioButt) {

        } else if (cat == inputButt) {

        }
    }

    override fun show() {
        stage.addActor(root);

        GuiEffects.fadeIn(root, FADE_DURATION).start(tweenManager)
    }

    override fun hide() {
        GuiEffects.fadeOut(root, FADE_DURATION, {
            stage.actors.removeValue(root, true)
        }).start(tweenManager)
    }

    override fun dispose() {}

    override fun resize(width: Int, height: Int) {}

    override fun render(batch: Batch, delta: Float) {}
}
