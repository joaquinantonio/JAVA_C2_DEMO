# Day 11 Code To Show Step By Step

## Step 1 — Start with App.jsx

Start with a simple component:

```jsx
export default function App() {
  return <h1>Asset Tracker UI</h1>;
}
```

Explain that every component returns UI.

---

## Step 2 — Add Layout and AppHeader

Show:

```text
src/components/Layout.jsx
src/components/AppHeader.jsx
```

Explain:

```text
Layout wraps the page.
AppHeader is a reusable visual component.
```

---

## Step 3 — Add sample data

Show:

```text
src/data/sampleAssets.js
```

Explain that Day 11 uses local data so students can focus on React.

---

## Step 4 — Add SummaryCards

Show how props work:

```jsx
<SummaryCards assets={assets} />
```

Then show the child component:

```jsx
export default function SummaryCards({ assets }) {
  return ...;
}
```

---

## Step 5 — Add AssetList

Important teaching points:

```jsx
{assets.map((asset) => (
  <button key={asset.id}>...</button>
))}
```

Explain:

- `.map()` converts data to UI
- `key` should be stable and unique
- a component can receive callback props

---

## Step 6 — Add selected asset state

Show:

```jsx
const [selectedAsset, setSelectedAsset] = useState(sampleAssets[0]);
```

Then pass it:

```jsx
<AssetList onSelectAsset={setSelectedAsset} />
<AssetDetail asset={selectedAsset} />
```

---

## Step 7 — Add FilterPanel

Show controlled inputs:

```jsx
<input value={searchText} onChange={(event) => setSearchText(event.target.value)} />
```

Explain:

```text
The input value comes from state.
When the user types, state updates.
When state updates, React re-renders the UI.
```

---

## Step 8 — Add useEffect for API information

Show:

```jsx
useEffect(() => {
  async function loadApiInformation() {
    const info = await fetchApiInfo();
    setApiInfo(info);
  }

  loadApiInformation();
}, []);
```

Explain:

- render should stay pure
- API calls belong in effects
- empty dependency array means run once after first render

---

## Step 9 — Add loading and error UI

Show:

```jsx
{loading && <LoadingMessage message="Loading API information..." />}
{error && <ErrorMessage message={error} />}
```

Explain the four UI states:

```text
loading
error
empty
success
```

---

## Step 10 — Final demo checklist

Show:

- Search works
- Status filter works
- Detail panel changes when asset selected
- API info loads when backend is running
- Error message appears when backend is stopped
