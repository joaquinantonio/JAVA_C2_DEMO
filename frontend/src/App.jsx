import { useEffect, useMemo, useState } from 'react';
import Layout from './components/Layout.jsx';
import SummaryCards from './components/SummaryCards.jsx';
import FilterPanel from './components/FilterPanel.jsx';
import AssetList from './components/AssetList.jsx';
import AssetDetail from './components/AssetDetail.jsx';
import ApiInfoCard from './components/ApiInfoCard.jsx';
import { sampleAssets } from './data/sampleAssets.js';
import { fetchApiDocs, fetchApiInfo } from './services/api.js';
import { filterAssets } from './utils/assets.js';

export default function App() {
  const [assets] = useState(sampleAssets);
  const [selectedAsset, setSelectedAsset] = useState(sampleAssets[0]);
  const [searchText, setSearchText] = useState('');
  const [statusFilter, setStatusFilter] = useState('ALL');
  const [apiInfo, setApiInfo] = useState(null);
  const [apiDocs, setApiDocs] = useState(null);
  const [loadingApi, setLoadingApi] = useState(true);
  const [apiError, setApiError] = useState('');

  const filteredAssets = useMemo(
    () => filterAssets(assets, searchText, statusFilter),
    [assets, searchText, statusFilter]
  );

  useEffect(() => {
    let ignore = false;

    async function loadApiInformation() {
      try {
        setLoadingApi(true);
        setApiError('');

        const [info, docs] = await Promise.all([fetchApiInfo(), fetchApiDocs()]);

        if (!ignore) {
          setApiInfo(info);
          setApiDocs(docs);
        }
      } catch (error) {
        if (!ignore) {
          setApiError(
            'Could not connect to the Day 10 backend. Start Spring Boot on port 8080 and try again.'
          );
          console.error(error);
        }
      } finally {
        if (!ignore) {
          setLoadingApi(false);
        }
      }
    }

    loadApiInformation();

    return () => {
      ignore = true;
    };
  }, []);

  useEffect(() => {
    if (filteredAssets.length === 0) {
      setSelectedAsset(null);
      return;
    }

    const selectedStillVisible = filteredAssets.some((asset) => asset.id === selectedAsset?.id);

    if (!selectedStillVisible) {
      setSelectedAsset(filteredAssets[0]);
    }
  }, [filteredAssets, selectedAsset]);

  return (
    <Layout>
      <SummaryCards assets={assets} />

      <ApiInfoCard
        loading={loadingApi}
        error={apiError}
        apiInfo={apiInfo}
        apiDocs={apiDocs}
      />

      <FilterPanel
        searchText={searchText}
        statusFilter={statusFilter}
        onSearchChange={setSearchText}
        onStatusChange={setStatusFilter}
      />

      <section className="workspace-grid">
        <AssetList
          assets={filteredAssets}
          selectedAssetId={selectedAsset?.id}
          onSelectAsset={setSelectedAsset}
        />
        <AssetDetail asset={selectedAsset} />
      </section>
    </Layout>
  );
}
