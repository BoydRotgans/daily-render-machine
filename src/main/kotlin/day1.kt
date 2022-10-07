import org.openrndr.application
import org.openrndr.color.ColorRGBa
import org.openrndr.color.mix
import org.openrndr.extra.keyframer.Keyframer
import org.openrndr.math.Vector2
import java.io.File

fun main() = application {
    configure {
        width = 1080 / 2
        height = 1920 / 2
    }

    program {
        class Animation : Keyframer() {
            val dotOne by DoubleChannel("dotOne")
            val dotTwo by DoubleChannel("dotTwo")
            val line by DoubleChannel("line")
        }

        class DotDotLineDrawer(val index: Int, val startPoint: Vector2, val endPoint: Vector2, val radius: Double) {
            val animation = Animation()
            init {
                animation.loadFromJson(File("data/keyframes/day1.json"))
            }

            fun draw() {
                animation((seconds*2.0) - index*0.1)

                drawer.fill = ColorRGBa.WHITE
                drawer.stroke = null
                drawer.circle(startPoint, radius*animation.dotOne)

                drawer.stroke = ColorRGBa.WHITE.opacify(0.25)
                drawer.stroke = mix(ColorRGBa.RED, ColorRGBa.WHITE.opacify(0.25), animation.line)
                drawer.strokeWeight = radius * 1.0
                drawer.lineSegment(startPoint, Vector2(
                    (startPoint.x * (1.0-animation.line)) + (endPoint.x * (animation.line)),
                    (startPoint.y * (1.0-animation.line)) + (endPoint.y * (animation.line))
                ))

                drawer.fill = ColorRGBa.WHITE
                drawer.stroke = null
                drawer.circle(endPoint, radius*animation.dotTwo)
            }
        }

        val list = mutableListOf<DotDotLineDrawer>()
        list.add(DotDotLineDrawer(list.size-5, Vector2(width/2.0, height/4.0), Vector2(width/2.0, height-height/4.0), 2.0))

        (0..100).forEachIndexed { index, i ->
            if(i%2==0) {
                list.add(
                    DotDotLineDrawer(
                        list.size,
                        Vector2(Math.random() * width, Math.random() * height/2.0),
                        Vector2(width / 2.0, height / 4.0),
                        2.0
                    )
                )
            } else {
                list.add(
                    DotDotLineDrawer(
                        list.size,
                        Vector2(Math.random() * width, height/2+Math.random() * height/2.0),
                        Vector2(width/2.0, height-height/4.0),
                        2.0
                    )
                )
            }
        }

//        extend(ScreenRecorder().apply {
//            frameRate = 60
//            outputFile = "video/day1.mp4"
//            maximumDuration = 30.0
//        })
        extend {
            list.forEach {
                it.draw()
            }
        }
    }
}
