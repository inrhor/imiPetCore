package cn.inrhor.imipetcore.common.script.kether

import cn.inrhor.imipetcore.common.location.distanceLoc
import org.bukkit.Location
import org.bukkit.entity.Entity
import taboolib.common5.Coerce
import taboolib.library.kether.ArgTypes
import taboolib.library.kether.ParsedAction
import taboolib.module.kether.*
import java.util.concurrent.CompletableFuture

class PositionAction {

    companion object {

        /**
         * loc add x y z to location
         * loc get entity
         */
        @KetherParser(["locUtil"], shared = true)
        fun parserPosition() = scriptParser {
            it.switch {
                case("add") {
                    val x = it.next(ArgTypes.ACTION)
                    val y = it.next(ArgTypes.ACTION)
                    val z = it.next(ArgTypes.ACTION)
                    it.expect("to")
                    val loc = it.next(ArgTypes.ACTION)
                    ActionLoc(x, y, z, loc)
                }
                case("get") {
                    val entity = it.next(ArgTypes.ACTION)
                    ActionEntityLoc(entity)
                }
            }
        }

        @KetherParser(["distance"])
        fun parserDis() = scriptParser {
            val v1 = it.next(ArgTypes.ACTION)
            it.expect("to")
            val v2 = it.next(ArgTypes.ACTION)
            ActionDistance(v1, v2)
        }

        private val tokenType = ArgTypes.listOf {
            it.nextToken()
        }

        @KetherParser(["worldCheck"])
        fun parserWorld() = scriptParser {
            val v1 = it.next(ArgTypes.ACTION)
            it.mark()
            when (it.expects("to", "where")) {
                "to" -> {
                    val v2 = it.next(ArgTypes.ACTION)
                    ActionWorld(v1, v2)
                }
                "where" -> {
                    val v2 = it.next(tokenType)
                    ActionWorldWhere(v1, v2)
                }
                "about" -> {
                    val v2 = it.next(tokenType)
                    ActionWorldAbout(v1, v2)
                }
                else -> error("world x ??")
            }
        }
    }

    class ActionDistance(val v1: ParsedAction<*>, val v2: ParsedAction<*>): ScriptAction<Double>() {
        override fun run(frame: ScriptFrame): CompletableFuture<Double> {
            return frame.newFrame(v1).run<Entity>().thenApply { i1 ->
                frame.newFrame(v2).run<Entity>().thenApply { i2 ->
                    i1.distanceLoc(i2)
                }.join()
            }
        }
    }

    class ActionEntityLoc(val entity: ParsedAction<*>): ScriptAction<Location>() {
        override fun run(frame: ScriptFrame): CompletableFuture<Location> {
            val future = CompletableFuture<Location>()
            frame.newFrame(entity).run<Entity>().thenApply {
                future.complete(it?.location)
            }
            return future
        }
    }

    class ActionLoc(val x: ParsedAction<*>, val y: ParsedAction<*>, val z: ParsedAction<*>, val loc: ParsedAction<*>): ScriptAction<Location>() {
        override fun run(frame: ScriptFrame): CompletableFuture<Location> {
            val future = CompletableFuture<Location>()
            frame.newFrame(x).run<Any>().thenApply { i1 ->
                frame.newFrame(y).run<Any>().thenApply { i2 ->
                    frame.newFrame(z).run<Any>().thenApply { i3 ->
                        frame.newFrame(loc).run<Location>().thenApply { l ->
                            future.complete(l.add(Coerce.toDouble(i1), Coerce.toDouble(i2), Coerce.toDouble(i3)))
                        }
                    }
                }
            }
            return future
        }
    }

    class ActionWorld(val v1: ParsedAction<*>, val v2: ParsedAction<*>): ScriptAction<Boolean>() {
        override fun run(frame: ScriptFrame): CompletableFuture<Boolean> {
            return frame.newFrame(v1).run<Entity>().thenApply { i1 ->
                frame.newFrame(v2).run<Entity>().thenApply { i2 ->
                    i1.world == i2.world
                }.join()
            }
        }
    }

    class ActionWorldWhere(val v1: ParsedAction<*>, val v2: List<String>): ScriptAction<Boolean>() {
        override fun run(frame: ScriptFrame): CompletableFuture<Boolean> {
            return frame.newFrame(v1).run<Entity>().thenApply { i1 ->
                v2.contains(i1.world.name)
            }
        }
    }

    class ActionWorldAbout(val v1: ParsedAction<*>, val v2: List<String>): ScriptAction<Boolean>() {
        override fun run(frame: ScriptFrame): CompletableFuture<Boolean> {
            return frame.newFrame(v1).run<Entity>().thenApply { i1 ->
                val world = i1.world.name
                v2.forEach {
                    if (world.contains(it)) return@thenApply true
                }
                false
            }
        }
    }

}