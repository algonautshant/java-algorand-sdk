package indexer

import (
	"context"

	"github.com/algorand/go-algorand-sdk/client/v2/common"
	"github.com/algorand/go-algorand-sdk/client/v2/common/models"
	"github.com/algorand/go-algorand-sdk/types"
)

// SearchForAccounts /v2/accounts
// Search for accounts.
type SearchForAccounts struct {
	c *Client
	p models.SearchForAccountsParams
}

// ApplicationId application ID
func (s *SearchForAccounts) ApplicationId(applicationId uint64) *SearchForAccounts {
	s.p.ApplicationId = applicationId
	return s
}

// AssetID asset ID
func (s *SearchForAccounts) AssetID(assetID uint64) *SearchForAccounts {
	s.p.AssetID = assetID
	return s
}

// AuthAddr include accounts configured to use this spending key.
func (s *SearchForAccounts) AuthAddr(authAddr types.Address) *SearchForAccounts {
	s.p.AuthAddr = authAddr.String()
	return s
}

// CurrencyGreaterThan results should have an amount greater than this value.
// MicroAlgos are the default currency unless an asset-id is provided, in which
// case the asset will be used.
func (s *SearchForAccounts) CurrencyGreaterThan(currencyGreaterThan uint64) *SearchForAccounts {
	s.p.CurrencyGreaterThan = currencyGreaterThan
	return s
}

// CurrencyLessThan results should have an amount less than this value. MicroAlgos
// are the default currency unless an asset-id is provided, in which case the asset
// will be used.
func (s *SearchForAccounts) CurrencyLessThan(currencyLessThan uint64) *SearchForAccounts {
	s.p.CurrencyLessThan = currencyLessThan
	return s
}

// Limit maximum number of results to return.
func (s *SearchForAccounts) Limit(limit uint64) *SearchForAccounts {
	s.p.Limit = limit
	return s
}

// Next the next page of results. Use the next token provided by the previous
// results.
func (s *SearchForAccounts) Next(next string) *SearchForAccounts {
	s.p.Next = next
	return s
}

// Round include results for the specified round. For performance reasons, this
// parameter may be disabled on some configurations.
func (s *SearchForAccounts) Round(round uint64) *SearchForAccounts {
	s.p.Round = round
	return s
}

// Do performs HTTP request
func (s *SearchForAccounts) Do(ctx context.Context,
	headers ...*common.Header) (response models.AccountsResponse, err error) {
	err = s.c.get(ctx, &response,
		"/v2/accounts", s.p, headers)
	return
}
