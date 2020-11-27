package ch.keepcalm.demo

import org.springframework.boot.ApplicationRunner
import org.springframework.boot.autoconfigure.SpringBootApplication
import org.springframework.boot.runApplication
import org.springframework.boot.web.client.RestTemplateBuilder
import org.springframework.context.annotation.Bean
import org.springframework.context.annotation.Configuration
import org.springframework.context.support.beans
import org.springframework.web.bind.annotation.GetMapping
import org.springframework.web.bind.annotation.RestController
import org.springframework.web.client.RestTemplate

@SpringBootApplication
class HttpsDemoClientApplication
fun main(args: Array<String>) {
	runApplication<HttpsDemoClientApplication>(*args){
		addInitializers(
				beans {
					bean {
						ApplicationRunner { println("-----------------> ApplicationRunner <-----------------") }
					}
				}
		)
	}
}
@Configuration
class BeanConfiguration {
	@Bean
	internal fun restTemplate(restTemplateBuilder: RestTemplateBuilder) = restTemplateBuilder.build()
}
@RestController
class CallRemoteHttpsServer (private val restTemplate: RestTemplate){
	@GetMapping
	fun sayHello() = restTemplate.getForEntity("http://localhost:8081", String::class.java)
}
