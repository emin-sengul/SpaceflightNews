# Spaceflight News

[![CI](https://github.com/emin-sengul/SpaceflightNews/actions/workflows/ci.yml/badge.svg)](https://github.com/emin-sengul/SpaceflightNews/actions/workflows/ci.yml)

**English** · [Türkçe](README.tr.md)

A Kotlin Multiplatform app that reads the [Spaceflight News API](https://api.spaceflightnewsapi.net/v4/docs/).
Android and iOS share everything — data, domain, and the Compose Multiplatform UI. Each platform
contributes only its entry point and a handful of `actual` declarations.

<p align="center">
  <img src="docs/ios-feed.png" width="300" alt="The feed running on the iOS simulator">
</p>

<p align="center"><em>Running on iOS. This screenshot is taken by CI: a macOS runner builds the
Xcode project, installs the app on a simulator, launches it and captures the screen.</em></p>

## What it does

- Browses the latest articles with endless scrolling
- Full-text search across the API
- Article detail with share and "open at source"
- Favorites that survive a cache wipe and work offline
- Offline-first: the cached feed is shown immediately, the network refreshes it underneath
- Two-pane layout on tablets and wide windows

## Running it

**Android** — open the project in Android Studio and run the `androidApp` configuration, or:

```
./gradlew :androidApp:assembleDebug
```

**iOS** — open `iosApp/iosApp.xcodeproj` in Xcode and run. The Kotlin framework is built by a
Gradle build phase inside the Xcode project, so no separate step is needed.

**Tests** (35 of them, all in `commonTest` so they run on both platforms):

```
./gradlew allTests                 # every target
./gradlew iosSimulatorArm64Test    # on the iOS simulator
```

CI runs the Android build, the iOS frameworks (simulator and device), the common tests on the iOS
simulator, and boots a simulator to screenshot the running app.

## Architecture

```
androidApp ──┐
             ├── shared ── feature:articles ─┐
iosApp ──────┘              feature:detail   ├── core:ui ── core:designsystem
                            feature:favorites┘       └───── core:domain
                                                              │
                      core:data ── core:network ──────────────┤
                          │        core:database              │
                          └────────core:common ───────────────┘
```

The dependency rule runs one way: features know the domain, never the data layer. `core:data` is the
only module that knows both Ktor and Room, and it is wired in through Koin at the app boundary, so
nothing above it can reach a DTO or an entity.

| Module | Responsibility |
| --- | --- |
| `core:common` | `DataResult`, `AppError`, dispatchers, and the shared extension functions |
| `core:domain` | `Article`, the repository interface, use cases — pure Kotlin, no framework |
| `core:network` | Ktor client, DTOs, error mapping |
| `core:database` | Room entities, DAOs, the platform database builders |
| `core:data` | The offline-first repository and the mappers between the three representations |
| `core:designsystem` | Theme, brand palette, stateless components, modifiers |
| `core:ui` | Components that know the domain model, such as the article list items |
| `feature:*` | One screen each: state, intents, ViewModel, composables |
| `shared` | Navigation graph, scaffold, tab bar, DI assembly |

### Decisions worth explaining

**Offline-first with the database as the single source of truth.** The UI never observes the network.
It observes Room; the network only writes into Room. That is what makes the feed appear instantly on
a cold start and what keeps the screen stable when a refresh fails — the error surfaces as a banner
above content that is still there, instead of replacing it.

**Favorites live in their own table, storing a full snapshot of the article.** The obvious design is a
boolean column on the cached article. It breaks the moment the feed refreshes: an article that drops
off the first page is deleted from the cache, and the favorite disappears with it. Copying the whole
article into `favorite_articles` costs a little duplication and buys favorites that open offline,
forever. The two tables share their column definitions through an `@Embedded ArticleColumns`.

**Search results are deliberately never cached.** Search hits the API directly and merges favorite
state in memory. Writing them into the feed cache would mix a filtered result set into what is
supposed to be a chronological feed.

**MVI in each feature.** One immutable `UiState`, one `Intent` sealed interface, one `StateFlow`. The
ViewModel exposes state through `stateInWhileSubscribed`, so collection stops when the screen goes to
the background and resumes without re-fetching.

**No comments anywhere in the source.** Names, small functions and this file carry the explanation.
Extension functions do a lot of that work — `article.sourceUrl`, `state.emptyTitle()`,
`loadState.update(FeedLoadState::refreshing)` — each replacing a line that would otherwise have
needed one.

### A limitation of the API, not of the app

The case asks for the full article content on the detail screen. The Spaceflight News API does not
return it. Each article carries `title`, `summary`, `image_url`, `news_site`, `published_at`,
`authors` and a `url` pointing at the original publisher — there is no body field on any endpoint.

Rather than pad the screen or scrape the publisher's page, the detail screen shows everything the API
does give and offers "Read the full story on {source}", which hands off to the browser. Scraping
would have been fragile, legally murky, and a poor experience compared to the publisher's own page.

## Tech

Kotlin 2.4.10 · Compose Multiplatform 1.12.0 · AGP 9.3.0 · Gradle 9.7.1 · minSdk 24 · compileSdk 37

Ktor 3.5.2 (OkHttp on Android, Darwin on iOS) · Room 2.8.4 with the bundled SQLite driver ·
Koin 4.2.2 · kotlinx-serialization · kotlinx-coroutines · kotlinx-datetime · Coil 3 for images ·
Navigation Compose with type-safe routes · Turbine for Flow tests

### On performance

`@Immutable` state classes, stable `key` and `contentType` on every list item, `animateItem()` for
insertions, `remember` around per-row formatting and around every callback passed into a list, and
`snapshotFlow` rather than recomposition-driven scroll detection. Slow images show a shimmer
placeholder through `SubcomposeAsyncImage`; Coil shares the app's Ktor client instead of opening a
second HTTP stack.

## Attribution

Article data comes from the [Spaceflight News API](https://api.spaceflightnewsapi.net/v4/docs/),
maintained by [The Space Devs](https://thespacedevs.com/). The attribution is shown in the app: on
the splash screen and at the end of both lists.
