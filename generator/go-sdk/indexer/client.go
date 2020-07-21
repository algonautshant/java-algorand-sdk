package indexer

import (
	"context"

	"github.com/algorand/go-algorand-sdk/client/v2/common"
)

const indexerAuthHeader = "X-Indexer-API-Token"

type Client common.Client

// get performs a GET request to the specific path against the server
func (c *Client) get(ctx context.Context, response interface{}, path string, request interface{}, headers []*common.Header) error {
	return (*common.Client)(c).Get(ctx, response, path, request, headers)
}

// MakeClient is the factory for constructing an IndexerClient for a given endpoint.
func MakeClient(address string, apiToken string) (c *Client, err error) {
	commonClient, err := common.MakeClient(address, indexerAuthHeader, apiToken)
	c = (*Client)(commonClient)
	return
}

// /v2/accounts/{account-id}
// Lookup account information.
func (c *Client) LookupAccountByID(accountID string) *LookupAccountByID {
	return &LookupAccountByID{c: c, accountID: accountID}
}

// /v2/accounts/{account-id}/transactions
// Lookup account transactions.
func (c *Client) LookupAccountTransactions(accountID string) *LookupAccountTransactions {
	return &LookupAccountTransactions{c: c, accountID: accountID}
}

// /v2/applications/{application-id}
// Lookup application.
func (c *Client) LookupApplicationByID(applicationId uint64) *LookupApplicationByID {
	return &LookupApplicationByID{c: c, applicationId: applicationId}
}

// /v2/assets/{asset-id}/balances
// Lookup the list of accounts who hold this asset
func (c *Client) LookupAssetBalances(assetId uint64) *LookupAssetBalances {
	return &LookupAssetBalances{c: c, assetId: assetId}
}

// /v2/assets/{asset-id}
// Lookup asset information.
func (c *Client) LookupAssetByID(assetId uint64) *LookupAssetByID {
	return &LookupAssetByID{c: c, assetId: assetId}
}

// /v2/assets/{asset-id}/transactions
// Lookup transactions for an asset.
func (c *Client) LookupAssetTransactions(assetId uint64) *LookupAssetTransactions {
	return &LookupAssetTransactions{c: c, assetId: assetId}
}

// /v2/blocks/{round-number}
// Lookup block.
func (c *Client) LookupBlock(roundNumber uint64) *LookupBlock {
	return &LookupBlock{c: c, roundNumber: roundNumber}
}

// /health
// Returns 200 if healthy.
func (c *Client) MakeHealthCheck() *MakeHealthCheck {
	return &MakeHealthCheck{c: c}
}

// /v2/accounts
// Search for accounts.
func (c *Client) SearchForAccounts() *SearchForAccounts {
	return &SearchForAccounts{c: c}
}

// /v2/applications
// Search for applications
func (c *Client) SearchForApplications() *SearchForApplications {
	return &SearchForApplications{c: c}
}

// /v2/assets
// Search for assets.
func (c *Client) SearchForAssets() *SearchForAssets {
	return &SearchForAssets{c: c}
}

// /v2/transactions
// Search for transactions.
func (c *Client) SearchForTransactions() *SearchForTransactions {
	return &SearchForTransactions{c: c}
}

