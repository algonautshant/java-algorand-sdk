package algod

import (
	"context"

	"github.com/algorand/go-algorand-sdk/client/v2/common"
	"github.com/algorand/go-algorand-sdk/client/v2/common/models"
)

// GetSupply /v2/ledger/supply
// Get the current supply reported by the ledger.
type GetSupply struct {
	c *Client
}

// Do performs HTTP request
func (s *GetSupply) Do(ctx context.Context,
	headers ...*common.Header) (response models.SupplyResponse, err error) {
	err = s.c.get(ctx, &response,
		"/v2/ledger/supply", nil, headers)
	return
}
