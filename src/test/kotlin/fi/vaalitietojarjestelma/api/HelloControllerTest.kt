package fi.vaalitietojarjestelma.api

import io.kotest.core.spec.style.FunSpec
import io.kotest.extensions.spring.SpringExtension
import io.kotest.matchers.shouldBe
import org.springframework.beans.factory.annotation.Autowired
import org.springframework.boot.test.context.SpringBootTest
import org.springframework.boot.test.context.SpringBootTest.WebEnvironment.RANDOM_PORT
import org.springframework.boot.test.web.client.TestRestTemplate
import org.springframework.http.HttpStatus

@SpringBootTest(webEnvironment = RANDOM_PORT)
class HelloControllerTest : FunSpec() {

    override fun extensions() = listOf(SpringExtension)

    @Autowired
    lateinit var restTemplate: TestRestTemplate

    init {
        test("GET /api/hello returns 200 with Hello World message") {
            val response = restTemplate.getForEntity("/api/hello", HelloResponse::class.java)

            response.statusCode shouldBe HttpStatus.OK
            response.body?.message shouldBe "Hello World"
        }
    }
}
