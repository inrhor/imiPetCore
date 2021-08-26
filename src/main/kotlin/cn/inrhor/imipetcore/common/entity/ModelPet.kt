package cn.inrhor.imipetcore.common.entity

import com.ticxo.modelengine.api.model.ActiveModel
import com.ticxo.modelengine.api.model.BaseEntity
import com.ticxo.modelengine.api.model.ModeledEntity
import org.bukkit.Location
import org.bukkit.Material
import org.bukkit.World
import org.bukkit.entity.Entity
import org.bukkit.entity.EntityType
import org.bukkit.entity.Player
import org.bukkit.inventory.ItemStack
import org.bukkit.potion.PotionEffect
import org.bukkit.potion.PotionEffectType
import org.bukkit.util.Vector
import java.lang.StringBuilder
import java.util.*

/**
 * 宠物模型
 */
class ModelPet(val pet: PetData): BaseEntity<PetData> {
    override fun getBase(): PetData {
        return pet
    }

    override fun getLocation(): Location {
        return pet.getLocation()
    }

    override fun getVelocity(): Vector {
        return pet.getLocation().toVector()
    }

    override fun isOnGround(): Boolean {
        return true
    }

    override fun getWorld(): World? {
        return pet.getLocation().world
    }

    override fun getNearbyEntities(v: Double, v1: Double, v2: Double): MutableList<Entity> {
        return mutableListOf()
    }

    override fun getEntityId(): Int {
        return pet.index
    }

    override fun remove() {
        //
    }

    override fun isCustomNameVisible(): Boolean {
        return true
    }

    override fun isDead(): Boolean {
        return false
    }

    override fun getUniqueId(): UUID {
        return pet.uuid
    }

    override fun getType(): EntityType {
        return EntityType.PIG
    }

    override fun isInvulnerable(): Boolean {
        return false
    }

    override fun hasGravity(): Boolean {
        return true
    }

    override fun getHealth(): Double {
        return 20.0
    }

    override fun getMaxHealth(): Double {
        return 20.0
    }

    override fun getCustomName(): String {
        return "hello"
    }

    override fun setCustomName(s: String?) {
        //
    }

    override fun getMovementSpeed(): Double {
        return 0.2
    }

    override fun getItemInMainHand(): ItemStack {
        return ItemStack(Material.STONE)
    }

    override fun getItemInOffHand(): ItemStack {
        return ItemStack(Material.STONE)
    }

    override fun isLivingEntity(): Boolean {
        return true
    }

    override fun addPotionEffect(potion: PotionEffect?) {
        //
    }

    override fun removePotionEffect(potion: PotionEffectType?) {
        //
    }

    override fun setEntitySize(width: Float, height: Float, eye: Float) {
        //
    }

    override fun sendDespawnPacket(modeledEntity: ModeledEntity?) {
        //
    }

    override fun sendSpawnPacket(modeledEntity: ModeledEntity?) {
        //
    }

    override fun setMoveController(modeledEntity: ModeledEntity?) {
        //
    }

    override fun setMountController(model: ModeledEntity?, player: Player?, controllerId: String?) {
        //
    }

    override fun getLastX(): Double {
        return pet.getLocation().x
    }

    override fun getLastY(): Double {
        return pet.getLocation().y
    }

    override fun getLastZ(): Double {
        return pet.getLocation().z
    }

    override fun saveModelList(models: MutableMap<String, ActiveModel>) {
        if (models.isEmpty()) {
            return
        }
        val stringBuilder = StringBuilder()
        for (str in models.keys)
            stringBuilder.append(str).append(";")
        stringBuilder.deleteCharAt(stringBuilder.length - 1)
    }

    override fun getModelList(): MutableList<String> {
        return mutableListOf()
    }

    override fun setMountPoint(id: UUID) {
        //
    }

    override fun getMountPoint(): UUID {
        return UUID.randomUUID()
    }
}