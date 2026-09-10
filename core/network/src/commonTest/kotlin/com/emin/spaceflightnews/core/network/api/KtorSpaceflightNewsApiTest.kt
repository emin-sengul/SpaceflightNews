package com.emin.spaceflightnews.core.network.api

import com.emin.spaceflightnews.core.common.AppError
import com.emin.spaceflightnews.core.common.DataResult
import com.emin.spaceflightnews.core.common.fold
import com.emin.spaceflightnews.core.network.installSpaceflightNewsDefaults
import io.ktor.client.HttpClient
import io.ktor.client.engine.mock.MockEngine
import io.ktor.client.engine.mock.respond
import io.ktor.client.request.HttpRequestData
import io.ktor.http.HttpHeaders
import io.ktor.http.HttpStatusCode
import io.ktor.http.headersOf
import kotlinx.coroutines.test.runTest
import kotlin.test.Test
import kotlin.test.assertEquals
import kotlin.test.assertIs
import kotlin.test.assertNotNull
import kotlin.test.assertNull
import kotlin.test.assertTrue
import kotlin.test.fail

class KtorSpaceflightNewsApiTest {
    private var lastRequest: HttpRequestData? = null

    private fun apiReturning(
        status: HttpStatusCode = HttpStatusCode.OK,
        body: String,
    ): SpaceflightNewsApi {
        val engine = MockEngine { request ->
            lastRequest = request
            respond(
                content = body,
                status = status,
                headers = headersOf(HttpHeaders.ContentType, "application/json"),
            )
        }
        val client = HttpClient(engine) { installSpaceflightNewsDefaults(enableLogging = false) }
        return KtorSpaceflightNewsApi(client)
    }

    @Test
    fun decodesAPageOfArticles() = runTest {
        val api = apiReturning(body = ARTICLES_PAGE_JSON)

        val page = api.getArticles(limit = 20, offset = 0).orFail()

        assertEquals(35966, page.count)
        val article = page.results.single()
        assertEquals(39853L, article.id)
        assertEquals("Isar Aerospace Soars to Orbit", article.title)

        assertEquals("Space Scout", article.newsSite)
        assertEquals("https://example.com/spectrum.png", article.imageUrl)
        assertEquals("2026-09-07T16:39:11Z", article.publishedAt)
        assertEquals("Scarlet Dominik", article.authors.single().name)
    }

    @Test
    fun toleratesArticlesWithMissingOptionalFields() = runTest {
        val api = apiReturning(body = MINIMAL_ARTICLE_JSON)

        val page = api.getArticles(limit = 20, offset = 0).orFail()

        val article = page.results.single()
        assertNull(article.imageUrl)
        assertEquals("", article.summary)
        assertTrue(article.authors.isEmpty())
    }

    @Test
    fun sendsPagingAndSearchParameters() = runTest {
        val api = apiReturning(body = EMPTY_PAGE_JSON)

        api.getArticles(limit = 25, offset = 50, search = "mars")

        val request = assertNotNull(lastRequest)
        assertEquals("25", request.url.parameters["limit"])
        assertEquals("50", request.url.parameters["offset"])
        assertEquals("mars", request.url.parameters["search"])
        assertEquals(SpaceflightNewsApi.ORDERING_NEWEST_FIRST, request.url.parameters["ordering"])
        assertTrue(request.url.encodedPath.endsWith("/articles/"))
    }

    @Test
    fun omitsTheSearchParameterWhenTheQueryIsBlank() = runTest {
        val api = apiReturning(body = EMPTY_PAGE_JSON)

        api.getArticles(limit = 20, offset = 0, search = "   ")

        assertNull(assertNotNull(lastRequest).url.parameters["search"])
    }

    @Test
    fun mapsClientErrorsToAppError() = runTest {
        val api = apiReturning(status = HttpStatusCode.NotFound, body = "{}")

        val result = api.getArticle(id = 1)

        assertEquals(AppError.ClientError(404), assertIs<DataResult.Failure>(result).error)
    }

    @Test
    fun mapsServerErrorsToAppError() = runTest {
        val api = apiReturning(status = HttpStatusCode.InternalServerError, body = "{}")

        val result = api.getArticles(limit = 20, offset = 0)

        assertEquals(AppError.ServerError(500), assertIs<DataResult.Failure>(result).error)
    }

    @Test
    fun mapsAnUnreadablePayloadToASerializationError() = runTest {
        val api = apiReturning(body = """{"count": "not-a-number", "results": []}""")

        val result = api.getArticles(limit = 20, offset = 0)

        assertEquals(AppError.Serialization, assertIs<DataResult.Failure>(result).error)
    }

    private fun <T> DataResult<T>.orFail(): T = fold(
        onSuccess = { it },
        onFailure = { error -> fail("Expected a successful response but failed with $error") },
    )

    private companion object {
        const val ARTICLES_PAGE_JSON = """
        {
          "count": 35966,
          "next": "https://api.spaceflightnewsapi.net/v4/articles/?limit=1&offset=1",
          "previous": null,
          "results": [
            {
              "id": 39853,
              "title": "Isar Aerospace Soars to Orbit",
              "authors": [{"name": "Scarlet Dominik", "socials": null}],
              "url": "https://example.com/article",
              "image_url": "https://example.com/spectrum.png",
              "news_site": "Space Scout",
              "summary": "A major improvement over the first flight.",
              "published_at": "2026-09-07T16:39:11Z",
              "updated_at": "2026-09-07T16:40:29.598987Z",
              "featured": false,
              "launches": [],
              "events": []
            }
          ]
        }
        """

        const val MINIMAL_ARTICLE_JSON = """
        {
          "count": 1,
          "results": [
            {
              "id": 7,
              "title": "A sparse article",
              "published_at": "2026-01-01T00:00:00Z"
            }
          ]
        }
        """

        const val EMPTY_PAGE_JSON = """{"count": 0, "next": null, "previous": null, "results": []}"""
    }
}
