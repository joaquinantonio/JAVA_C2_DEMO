# Day 11 Exact Trainer Plan

## Day title

React Fundamentals: Components, State and Effects

## Main goal

Students learn how to build a React UI from small reusable components.

By the end of the day, students should understand:

- What a React component is
- How JSX works
- How props pass data from parent to child
- How state changes the UI
- How `useEffect` runs side effects such as loading API information
- How to show loading, error, empty and success states
- How to describe a component tree

---

## 9:00–9:30 — Recap backend milestone

Explain:

```text
Days 7–10 gave us a working backend.
Day 11 starts the frontend.
The frontend is what users see and interact with.
```

Show the backend briefly:

- `/api/v1/info`
- `/api/docs`
- `/api/v1/assets` requires token

Explain that Day 11 will not fully consume protected APIs yet. That comes later.

---

## 9:30–10:15 — React concepts

Teach:

- React is component-based
- JSX looks like HTML but is JavaScript
- Components are functions that return UI
- Props are inputs
- State is changing data inside the component
- Effects are used for work outside rendering, such as API calls

Whiteboard this component tree:

```text
App
├── Layout
│   └── AppHeader
├── SummaryCards
├── ApiInfoCard
├── FilterPanel
└── Workspace
    ├── AssetList
    │   └── StatusBadge
    └── AssetDetail
        └── StatusBadge
```

---

## 10:15–10:45 — Create and run the React app

Show the commands:

```bash
npm create vite@latest asset-tracker-ui-day11 -- --template react
cd asset-tracker-ui-day11
npm install
npm run dev
```

For the prepared package, students can simply run:

```bash
cd asset-tracker-ui-day11
npm install
npm run dev
```

---

## 11:00–12:00 — Build the layout and first components

Show these files:

```text
src/components/AppHeader.jsx
src/components/Layout.jsx
src/components/SummaryCards.jsx
src/components/StatusBadge.jsx
```

Teaching points:

- File per component
- Component names start with uppercase
- Props are function parameters
- Parent controls the data
- Child displays the data

---

## 12:00–1:00 — Build list/detail UI

Show:

```text
src/components/AssetList.jsx
src/components/AssetDetail.jsx
src/data/sampleAssets.js
```

Teaching points:

- `.map()` renders lists
- `key` helps React track list items
- selected item is state
- clicking a list row updates selected item

---

## 2:00–2:45 — Add filtering with state

Show:

```text
src/components/FilterPanel.jsx
src/utils/assets.js
```

Teaching points:

- Controlled inputs use state
- Derived data should be calculated from state
- Search/filter should not mutate original data

---

## 2:45–3:30 — Add useEffect and backend status

Show:

```text
src/services/api.js
src/components/ApiInfoCard.jsx
```

Teaching points:

- `useEffect(..., [])` runs after first render
- API calls are asynchronous
- UI needs loading and error states
- Vite proxy forwards `/api` requests to Spring Boot

---

## 3:45–4:30 — Student exercise: Support Desk Ticket UI

Students build the Ticket version:

```text
SupportDeskApp
├── Layout
├── TicketSummaryCards
├── TicketFilterPanel
├── TicketList
└── TicketDetail
```

Use local sample tickets first.

---

## 4:30–5:00 — Review and checkpoint

Ask students to explain:

- Which component stores the selected ticket state?
- Which components receive props?
- What does `useEffect` do?
- What should the UI show while loading?
- What should the UI show if no tickets match the filter?

Deliverable:

```text
Working React UI + component tree diagram
```
