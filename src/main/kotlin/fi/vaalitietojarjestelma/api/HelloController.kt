package fi.vaalitietojarjestelma.api

import kotlinx.serialization.Serializable
import org.springframework.http.MediaType
import org.springframework.web.bind.annotation.GetMapping
import org.springframework.web.bind.annotation.RequestMapping
import org.springframework.web.bind.annotation.RestController

@Serializable
data class HelloResponse(val message: String)

@RestController
@RequestMapping("/api")
class HelloController {

    @GetMapping("/hello", produces = [MediaType.APPLICATION_JSON_VALUE])
    fun hello(): HelloResponse = HelloResponse("Hello World")
}
