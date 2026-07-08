# Day 11 Final Working Demo Checklist

Before ending Day 11, confirm the trainer demo can do the following:

## Setup

- [ ] Backend Day 10 app starts on port 8080
- [ ] Frontend app starts on port 5173
- [ ] Browser opens `http://localhost:5173`

## UI checks

- [ ] Header appears
- [ ] Summary cards show total, available, assigned and maintenance counts
- [ ] Asset list appears
- [ ] Asset detail panel appears
- [ ] Clicking an asset changes the detail panel
- [ ] Search filters the asset list
- [ ] Status dropdown filters the asset list
- [ ] Empty state appears when no assets match

## API checks

- [ ] Backend Connection card loads `/api/v1/info`
- [ ] Backend Connection card loads `/api/docs`
- [ ] If backend is stopped, an error message appears

## Student understanding checks

Ask:

- [ ] What is a component?
- [ ] What is a prop?
- [ ] What is state?
- [ ] Why do we use `useEffect`?
- [ ] What is a loading state?
- [ ] What is an empty state?
- [ ] Where would the Ticket UI component tree differ from the Asset UI?
