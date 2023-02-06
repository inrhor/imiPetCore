package cn.inrhor.imipetcore.common.hook.protocol.version

class EntityMap1: EntityMap() {

    /**
     * 1.13-1.16.5 ProtocolLib 实体类型 对应 实体序号
     *
     * https://wiki.vg/index.php?title=Entity_metadata&oldid=16539
     */
    private val entityMap1 = mapOf(
        "BAT" to 3,
        "BEE" to 4,
        "BLAZE" to 5,
        "CAT" to 7,
        "CAVE_SPIDER" to 8,
        "CHICKEN" to 9,
        "COD" to 10,
        "COW" to 11,
        "CREEPER" to 12,
        "DOLPHIN" to 13,
        "DONKEY" to 14,
        "DROWNED" to 16,
        "ELDER_GUARDIAN" to 17,
        "ENDER_DRAGON" to 19,
        "ENDERMAN" to 20,
        "ENDERMITE" to 21,
        "EVOKER" to 22,
        "EVOKER_FANGS" to 23,
        "FOX" to 28,
        "GHAST" to 29,
        "GIANT" to 30,
        "GUARDIAN" to 31,
        "HOGLIN" to 32,
        "HORSE" to 33,
        "HUSK" to 34,
        "ILLUSIONER" to 35,
        "IRON_GOLEM" to 36,
        "LLAMA" to 42,
        "MAGMA_CUBE" to 44,
        "MULE" to 52,
        "MUSHROOM_COW" to 53,
        "OCELOT" to 54,
        "PANDA" to 56,
        "PARROT" to 57,
        "PHANTOM" to 58,
        "PIG" to 59,
        "PIGLIN" to 60,
        "PIGLIN_BRUTE" to 61,
        "PILLAGER" to 62,
        "POLAR_BEAR" to 63,
        "PUFFERFISH" to 65,
        "RABBIT" to 66,
        "RAVAGER" to 67,
        "SALMON" to 68,
        "SHEEP" to 69,
        "SHULKER" to 70,
        "SILVERFISH" to 72,
        "SKELETON" to 73,
        "SKELETON_HORSE" to 74,
        "SLIME" to 75,
        "SNOWMAN" to 77,
        "SPIDER" to 80,
        "SQUID" to 81,
        "STRAY" to 82,
        "STRIDER" to 83,
        "TRADER_LLAMA" to 89,
        "TROPICAL_FISH" to 90,
        "TURTLE" to 91,
        "VEX" to 92,
        "VILLAGER" to 93,
        "VINDICATOR" to 94,
        "WANDERING_TRADER" to 95,
        "WITCH" to 96,
        "WITHER" to 97,
        "WITHER_SKELETON" to 98,
        "WOLF" to 100,
        "ZOGLIN" to 101,
        "ZOMBIE" to 102,
        "ZOMBIE_HORSE" to 103,
        "ZOMBIE_VILLAGER" to 104,
        "ZOMBIFIED_PIGLIN" to 105,
    )

    override fun getEntityId(entityType: String): Int {
        return entityMap1[entityType] ?: 67
    }

}