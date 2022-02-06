package org.classcompanion.botlib

import com.rabbitmq.client.*
import org.classcompanion.botlib.consumes.DefaultConsume
import org.classcompanion.botlib.rabbitmq.OnConsume
import org.classcompanion.botlib.rabbitmq.RabbitmqManager


class BotLib(baseUrl: String, guildId: String) {
	private val serverQueueName = "server"
	private val botQueueName = "bot"
	private var factory: ConnectionFactory = RabbitmqManager.createFactory("test", "tset", "/", baseUrl, 5672)
	private var connection: Connection? = RabbitmqManager.makeConnection(factory)
	private var channel: Channel? = connection?.let { RabbitmqManager.createChannel(it) }
	init {
		RabbitmqManager.declareQueue(channel!!, serverQueueName)
		RabbitmqManager.declareQueue(channel!!, botQueueName)
	}

	fun sendMessage(msg: String) {
		RabbitmqManager.basicPublish(channel!!, botQueueName, msg)
		println("[x] Sent '$msg'")
	}

	fun setConsume(consume: OnConsume = DefaultConsume()) {
		RabbitmqManager.setBasicConsume(channel!!, serverQueueName, consume)
	}

	fun closeConnection() {
		channel!!.close()
		connection!!.close()
	}
}