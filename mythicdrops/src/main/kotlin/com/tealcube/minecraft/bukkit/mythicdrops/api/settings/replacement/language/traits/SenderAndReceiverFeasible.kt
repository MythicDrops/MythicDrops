package com.tealcube.minecraft.bukkit.mythicdrops.api.settings.replacement.language.traits

/**
 * Represents a message section with a `receiver-success`, `receiver-failure`,`sender-success`,
 * and a `sender-failure` field.
 */
interface SenderAndReceiverFeasible {
    val receiverSuccess: String
    val receiverFailure: String
    val senderSuccess: String
    val senderFailure: String
}
