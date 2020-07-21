package algod

import (
	"context"
	"fmt"

	"github.com/algorand/go-algorand-sdk/client/v2/common"
	"github.com/algorand/go-algorand-sdk/client/v2/common/models"
)

// AccountInformation /v2/accounts/{address}
// Given a specific account public key, this call returns the accounts status,
// balance and spendable amounts
type AccountInformation struct {
	c       *Client
	address string
}

// Do performs HTTP request
func (s *AccountInformation) Do(ctx context.Context,
	headers ...*common.Header) (response models.Account, err error) {
	err = s.c.get(ctx, &response,
		fmt.Sprintf("/v2/accounts/%s", s.address), nil, headers)
	return
}
