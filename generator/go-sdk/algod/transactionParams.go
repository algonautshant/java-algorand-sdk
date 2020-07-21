package algod

import (
	"context"

	"github.com/algorand/go-algorand-sdk/client/v2/common"
	"github.com/algorand/go-algorand-sdk/client/v2/common/models"
)

// TransactionParams /v2/transactions/params
// Get parameters for constructing a new transaction
type TransactionParams struct {
	c *Client
}

// Do performs HTTP request
func (s *TransactionParams) Do(ctx context.Context,
	headers ...*common.Header) (response models.TransactionParametersResponse, err error) {
	err = s.c.get(ctx, &response,
		"/v2/transactions/params", nil, headers)
	return
}
