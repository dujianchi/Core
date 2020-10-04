package cn.dujc.chat.core

import org.springframework.boot.autoconfigure.SpringBootApplication
import org.springframework.boot.runApplication

@SpringBootApplication
class ChatCoreApplication

fun main(args: Array<String>) {
    runApplication<ChatCoreApplication>(*args)
}
