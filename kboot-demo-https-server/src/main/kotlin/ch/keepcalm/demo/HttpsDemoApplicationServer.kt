package ch.keepcalm.demo

import org.springframework.boot.autoconfigure.SpringBootApplication
import org.springframework.boot.runApplication
import org.springframework.web.bind.annotation.GetMapping
import org.springframework.web.bind.annotation.RestController

@SpringBootApplication
class HttpsDemoApplicationServer
fun main(args: Array<String>) {
	runApplication<HttpsDemoApplicationServer>(*args)
}
@RestController
class HelloController{
	@GetMapping
	fun sayHello() = "Hello from Server"
}
