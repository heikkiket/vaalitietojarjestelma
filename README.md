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
