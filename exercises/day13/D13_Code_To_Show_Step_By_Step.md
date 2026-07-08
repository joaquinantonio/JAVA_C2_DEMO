# Day 13 Code To Show Step By Step

## Step 1 - Explain form state

Show one controlled input:

```jsx
const [title, setTitle] = useState('');

<input value={title} onChange={(event) => setTitle(event.target.value)} />
```

Explain:

```text
The input value comes from React state.
Changing the input updates React state.
```

## Step 2 - Add backend update DTO

Show `UpdateAssetRequest`.

Important fields:

```text
assetTag
name
category
serialNumber
status
location
assignedTo
```

## Step 3 - Add update method in service

Show:

```java
public AssetResponse updateAsset(String id, UpdateAssetRequest request)
```

Explain:

- find existing asset
- check duplicate asset tag if changed
- check duplicate serial number if changed
- validate status
- save changes

## Step 4 - Add PUT endpoint

Show:

```java
@PutMapping("/{id}")
public AssetResponse updateAsset(...)
```

## Step 5 - Add API functions in React

Show:

```js
createAsset(token, payload)
updateAsset(id, token, payload)
fetchAssetById(id, token)
```

## Step 6 - Add routes

Show:

```jsx
<Route path="assets/new" element={<AssetFormPage />} />
<Route path="assets/:assetId/edit" element={<AssetFormPage />} />
```

## Step 7 - Build the wizard

Show one step at a time.

1. Identity fields
2. Location and status fields
3. Review checkbox and submit

## Step 8 - Show validation

Test:

- empty asset tag
- lowercase asset tag
- missing location
- invalid assigned email
- unchecked review checkbox

## Step 9 - Show backend errors

Test duplicate asset tag or serial number.

Explain:

```text
Client-side validation improves UX.
Backend validation protects the system.
We need both.
```
