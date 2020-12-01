package ch.keepcalm.demo

import org.slf4j.Logger
import org.slf4j.LoggerFactory
import org.springframework.beans.factory.annotation.Value
import org.springframework.boot.ApplicationRunner
import org.springframework.boot.autoconfigure.SpringBootApplication
import org.springframework.boot.runApplication
import org.springframework.context.support.beans
import org.springframework.stereotype.Service
import org.springframework.web.bind.annotation.GetMapping
import org.springframework.web.bind.annotation.RestController
import org.springframework.web.reactive.function.client.ExchangeFilterFunction
import org.springframework.web.reactive.function.client.WebClient
import reactor.core.publisher.Mono
import reactor.netty.http.client.HttpClientRequest
import java.time.Duration

@SpringBootApplication
class HttpsDemoApplicationClient

fun main(args: Array<String>) {
    runApplication<HttpsDemoApplicationClient>(*args) {
        addInitializers(
                beans {
                    bean {
                        ApplicationRunner { println("-----------------> ApplicationRunner <-----------------") }
                    }
                }
        )
    }
}
@RestController
class CallRemoteHttpsServer(private val helloService: HelloService) {
    @GetMapping
    fun sayHello(): Mono<String> = helloService.sayHiWithResponsetimeoutAndDebugLog()
}

@Service
class HelloService(@Value("\${api.endpoint:http://localhost:8081}") private val apiEndpoint: String) {

    private val logger = LoggerFactory.getLogger(javaClass)

    private val webClient = WebClient.builder()
            .baseUrl(apiEndpoint)
            .codecs { configurer -> configurer.defaultCodecs().maxInMemorySize(2 * 1024 * 1024) }
            .filter(logRequest(logger)).build()

    fun sayHiWithResponsetimeoutAndDebugLog() = WebClient.builder()
                .baseUrl(apiEndpoint)
                .filter(logRequest(logger)).build()
                .get()
                .httpRequest {
                    it.getNativeRequest<HttpClientRequest>().responseTimeout(Duration.ofSeconds(2))
                }
                .retrieve()
                .bodyToMono(String::class.java)

    fun sayJustHello() = WebClient.create()
                .get()
                .uri(apiEndpoint)
                .retrieve()
                .bodyToMono(String::class.java)

    fun sayHiWithResponsetimeoutAndCodecLimits() = webClient.get()
            .httpRequest {
                it.getNativeRequest<HttpClientRequest>().responseTimeout(Duration.ofSeconds(2))
            }
            .retrieve().bodyToMono(String::class.java)

}

fun logRequest(log: Logger) = ExchangeFilterFunction.ofRequestProcessor {
    log.debug("Request: {} {}", it.method(), it.url())
    it.headers().forEach { name, values -> values.forEach { value -> log.debug("{}={}", name, value) } }
    Mono.just(it)
}


