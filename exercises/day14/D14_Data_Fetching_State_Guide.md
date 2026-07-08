# Day 14 Data Fetching & State Management Guide

## API abstraction layer

An API abstraction layer keeps fetch details out of page components.

It usually handles:

- base request structure
- Authorization header
- JSON parsing
- error messages
- request body serialisation

## Context

Context shares state and functions with many components without passing props through every level.

Day 14 uses context for asset data because assets are needed by list, detail, pagination and update controls.

## Reducer

A reducer is useful when state changes are connected.

Example:

```text
loading starts -> clear error -> fetch data -> update items -> update page info -> cache result
```

This is easier to manage with actions than many separate `useState` calls.

## Cache pattern

Day 14 uses a simple in-memory cache.

Cache key:

```text
page|size|sortBy|direction
```

This is not a replacement for production tools like React Query, but it is good for teaching the idea.

## Optimistic update

Optimistic update means:

1. Change the UI immediately.
2. Send the backend request.
3. Keep the change if the backend succeeds.
4. Roll back if the backend fails.

## Performance note

Day 14 introduces:

- `useMemo` for derived filtered assets
- `useCallback` for stable functions
- `lazy` and `Suspense` for route-level loading

Keep the explanation practical. Students do not need to over-optimise every component.
