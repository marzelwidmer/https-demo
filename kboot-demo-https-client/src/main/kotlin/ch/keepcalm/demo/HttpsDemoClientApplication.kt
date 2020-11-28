package ch.keepcalm.demo

import org.slf4j.Logger
import org.slf4j.LoggerFactory
import org.springframework.beans.factory.annotation.Autowired
import org.springframework.beans.factory.annotation.Qualifier
import org.springframework.boot.ApplicationRunner
import org.springframework.boot.autoconfigure.SpringBootApplication
import org.springframework.boot.runApplication
import org.springframework.context.annotation.Bean
import org.springframework.context.support.beans
import org.springframework.http.HttpHeaders
import org.springframework.http.MediaType
import org.springframework.http.client.reactive.ClientHttpRequest
import org.springframework.web.bind.annotation.GetMapping
import org.springframework.web.bind.annotation.RestController
import org.springframework.web.reactive.function.client.ExchangeFilterFunction
import org.springframework.web.reactive.function.client.WebClient
import reactor.core.publisher.Mono
import reactor.netty.http.client.HttpClientRequest
import java.time.Duration


@SpringBootApplication
class HttpsDemoClientApplication

fun main(args: Array<String>) {
    runApplication<HttpsDemoClientApplication>(*args) {
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
class CallRemoteHttpsServer(/*private val webClient: WebClient*/) {

    private val logger = LoggerFactory.getLogger(javaClass)

    val webClient = WebClient.builder()
            .baseUrl("http://localhost:8081")
            .codecs { configurer -> configurer.defaultCodecs().maxInMemorySize(2 * 1024 * 1024) }
            .filter(logRequest(logger)).build()

    @GetMapping
    fun sayHello(): Mono<String> {
//        return WebClient.builder()
//                .baseUrl("http://localhost:8081")
//                .filter(logRequest(logger)).build()
//                .get()
//                .httpRequest {
//                    it.getNativeRequest<HttpClientRequest>().responseTimeout(Duration.ofSeconds(2))
//                }
//                .retrieve()
//                .bodyToMono(String::class.java)

//        return WebClient.create()
//                .get()
//                .uri("http://localhost:8081")
//                .retrieve()
//                .bodyToMono(String::class.java)

//        return  WebClient.create().get()
//                .uri("http://localhost:8081")
//                .httpRequest {
//                    it.getNativeRequest<HttpClientRequest>().responseTimeout(Duration.ofSeconds(2))
//                }
//                .retrieve()
//                .bodyToMono(String::class.java)

        return webClient.get()
                .httpRequest {
                    it.getNativeRequest<HttpClientRequest>().responseTimeout(Duration.ofSeconds(2))
                }
                .retrieve().bodyToMono(String::class.java)
    }
}

fun logRequest(log: Logger) = ExchangeFilterFunction.ofRequestProcessor {
    log.debug("Request: {} {}", it.method(), it.url())
    it.headers().forEach { name, values -> values.forEach { value -> log.debug("{}={}", name, value) } }
    Mono.just(it)
}


