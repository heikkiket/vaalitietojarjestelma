# Heikin vaalitietojärjestelmä

## Johdanto
Tavoitteena on synnyttää 2030-luvun digitaalinen kyber äänikatalooki.
Tämä se on. Halvalla tehty ja helkkarin hyvä.

## TODO
Vaalipiirit

## Running

```bash
./gradlew bootRun
```

The API will be available at `http://localhost:8080`. To verify:

```bash
curl http://localhost:8080/api/hello
```

Or with Docker Compose:

```bash
docker compose run --rm -p 8080:8080 app ./gradlew bootRun --no-daemon
```

### Tests

```bash
./gradlew test
```

Or with Docker Compose:

```bash
docker compose run --rm app ./gradlew test --no-daemon
```

If `docker compose run` isn't available, use `exec` against a running container instead:

```bash
docker compose up -d app
docker compose exec app ./gradlew test --no-daemon
```

#### Gherkin use-case specs

Use cases are defined as Gherkin `.feature` files under `src/test/resources/features`, with step
definitions in `src/test/kotlin/fi/vaalitietojarjestelma/behavior`. Scenarios without finished step
definitions are tagged `@wip` and excluded from the default test run.

Run only the Gherkin specs:

```bash
docker compose run --rm app ./gradlew test --tests "fi.vaalitietojarjestelma.behavior.RunCucumberTest" --no-daemon
```

Run one specific scenario by name (add `-Dcucumber.filter.tags="@wip"` if the scenario is still tagged `@wip`):

```bash
docker compose run --rm app ./gradlew test --tests "fi.vaalitietojarjestelma.behavior.RunCucumberTest" -Dcucumber.filter.name="Chairperson submits the ballot tally after the polling station closes" -Dcucumber.filter.tags="@wip" --no-daemon
```

### Docker

```bash
docker build -t vaalitietojarjestelma .
docker run -p 8080:8080 vaalitietojarjestelma
```

## Speksit
The electoral information system has to be a modular one. The UI has to have a clear separation from backend via REST API.

## Core Domain types

### Election

### Candidate

### Vote

### Voter

### Candidate List

### Party

### Result

### CandidateResult

### CandidateListResult
